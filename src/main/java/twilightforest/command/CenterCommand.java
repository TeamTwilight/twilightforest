package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import twilightforest.TFFeature;

public class CenterCommand {
    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("center").requires(cs -> cs.hasPermissionLevel(2)).executes(CenterCommand::run);
    }

    // TODO FIX
    private static int run(CommandContext<CommandSource> ctx) {
        if (true)
            throw new CommandException(new StringTextComponent("This command is not supported!"));

        CommandSource source = ctx.getSource();
        int dx = MathHelper.floor(source.getPos().getX());
        int dz = MathHelper.floor(source.getPos().getZ());

        BlockPos cc = TFFeature.getNearestCenterXYZ(dx >> 4, dz >> 4);

        boolean fc = TFFeature.isInFeatureChunk(dx, dz);
        source.sendFeedback(new TranslationTextComponent("commands.tffeature.center", cc), false);
        source.sendFeedback(new TranslationTextComponent("commands.tffeature.chunk", fc), false);
        return Command.SINGLE_SUCCESS;
    }
}
