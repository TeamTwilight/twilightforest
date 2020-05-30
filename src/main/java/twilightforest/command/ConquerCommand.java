package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import twilightforest.world.ChunkGeneratorTFBase;
import twilightforest.world.TFWorld;

public class ConquerCommand {
    private static final SimpleCommandExceptionType NOT_IN_STRUCTURE = new SimpleCommandExceptionType(new TranslationTextComponent("commands.tffeature.structure.required"));

    public static LiteralArgumentBuilder<CommandSource> register() {
        LiteralArgumentBuilder<CommandSource> conquer = Commands.literal("conquer").requires(cs -> cs.hasPermissionLevel(2)).executes(ctx -> changeStructureActivity(ctx.getSource(), true));
        LiteralArgumentBuilder<CommandSource> reactivate = Commands.literal("reactivate").requires(cs -> cs.hasPermissionLevel(2)).executes(ctx -> changeStructureActivity(ctx.getSource(), false));
        return conquer.then(reactivate);
    }

    private static int changeStructureActivity(CommandSource source, boolean flag) throws CommandSyntaxException {
        if (!TFWorld.isTwilightForest(source.getWorld())) {
            throw TFCommand.NOT_IN_TF.create();
        }

        // are you in a structure?
        ChunkGeneratorTFBase chunkGenerator = TFWorld.getChunkGenerator(source.getWorld());

        BlockPos pos = new BlockPos(source.getPos());
        if (chunkGenerator != null/* && chunkGenerator.isBlockInStructureBB(pos)*/) {
            //source.sendFeedback(new TranslationTextComponent("commands.tffeature.structure.conquer.update", chunkGenerator.isStructureConquered(pos), flag), true);
            //chunkGenerator.setStructureConquered(pos, flag);
            return Command.SINGLE_SUCCESS;
        } else {
            throw NOT_IN_STRUCTURE.create();
        }
    }
}
