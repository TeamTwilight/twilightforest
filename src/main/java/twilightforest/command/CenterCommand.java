package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.commons.lang3.text.WordUtils;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFGenerationSettings;

public class CenterCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("center").requires(cs -> cs.hasPermission(2)).executes(CenterCommand::run);
    }

    private static int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();

        if (!TFGenerationSettings.usesTwilightChunkGenerator(source.getLevel())) {
            throw TFCommand.NOT_IN_TF.create();
        }

        int dx = Mth.floor(source.getPosition().x());
        int dz = Mth.floor(source.getPosition().z());
        BlockPos cc = TFFeature.getNearestCenterXYZ(dx >> 4, dz >> 4);
        TFFeature closestFeature = TFFeature.getFeatureAt(cc.getX(), cc.getZ(), source.getLevel());
        boolean fc = TFFeature.isInFeatureChunk(dx, dz);

        if(closestFeature == TFFeature.NOTHING) {
            source.sendSuccess(new TranslatableComponent("commands.tffeature.none_nearby").withStyle(ChatFormatting.RED), false);
        } else {
            String structurename = new TranslatableComponent("structure." + closestFeature.name).withStyle(ChatFormatting.DARK_GREEN).getString();
            source.sendSuccess(new TranslatableComponent("commands.tffeature.nearest", structurename), false);
            source.sendSuccess(new TranslatableComponent("commands.tffeature.center", cc), false);
            source.sendSuccess(new TranslatableComponent("commands.tffeature.chunk", fc), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
