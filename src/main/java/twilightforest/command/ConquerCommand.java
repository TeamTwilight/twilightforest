package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import twilightforest.util.LandmarkUtil;
import twilightforest.world.components.structures.start.TFStructureStart;

import java.util.Optional;

public class ConquerCommand {
	private static final SimpleCommandExceptionType NOT_IN_STRUCTURE = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.structure.required"));

	public static LiteralArgumentBuilder<CommandSourceStack> register() {
		LiteralArgumentBuilder<CommandSourceStack> conquer = Commands.literal("conquer").requires(cs -> cs.hasPermission(2)).executes(ctx -> changeStructureActivity(ctx.getSource(), true));
		LiteralArgumentBuilder<CommandSourceStack> reactivate = Commands.literal("reactivate").requires(cs -> cs.hasPermission(2)).executes(ctx -> changeStructureActivity(ctx.getSource(), false));
		return conquer.then(reactivate);
	}

	private static int changeStructureActivity(CommandSourceStack source, boolean flag) throws CommandSyntaxException {
		BlockPos pos = BlockPos.containing(source.getPosition());
        Optional<StructureStart> struct = LandmarkUtil.locateNearestLandmarkStart(source.getLevel(), SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));

        if (struct.isPresent() && struct.get().getBoundingBox().isInside(pos) && struct.get() instanceof TFStructureStart TFStructureStart) {
            source.sendSuccess(() -> Component.translatable("commands.tffeature.structure.conquer.update", TFStructureStart.isConquered(), flag), true);

            TFStructureStart.setConquered(flag, source.getLevel());
        } else {
            throw NOT_IN_STRUCTURE.create();
        }

        return Command.SINGLE_SUCCESS;
    }
}
