package twilightforest.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import tamaized.beanification.Autowired;
import twilightforest.util.DisplayUtil;
import twilightforest.world.components.structures.util.ProgressionPiece;

import java.util.List;

@tamaized.beanification.Component
public class DisplayPiecesCommand {
	@Autowired
	private DisplayUtil displayUtil;

	public LiteralArgumentBuilder<CommandSourceStack> register() {
		return Commands.literal("display_pieces").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
			.then(Commands.argument("filter_structure", ResourceKeyArgument.key(Registries.STRUCTURE)).executes(this::debugDisplayPieces));
	}

	private int debugDisplayPieces(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Holder.Reference<Structure> structure = ResourceKeyArgument.getStructure(context, "filter_structure");

		if (!structure.isBound()) return 0;

		CommandSourceStack source = context.getSource();
		ServerLevel level = source.getLevel();
		BlockPos commandPos = BlockPos.containing(source.getPosition());

		StructureStart structureAt = level.structureManager().getStructureAt(commandPos, structure.value());

		BoundingBox structureBox = structureAt.getBoundingBox();
		int successes = this.displayUtil.spawnBlockDisplay(level, structureBox, Blocks.RED_STAINED_GLASS.defaultBlockState(), 0.01f) ? 1 : 0;

		List<StructurePiece> structurePieces = structureAt.getPieces();
		int maxPieces = structurePieces.size();
		for (StructurePiece piece : structurePieces) {
			BlockState displayState = piece instanceof ProgressionPiece shieldablePiece && shieldablePiece.isComponentProtected() ? Blocks.LIME_STAINED_GLASS.defaultBlockState() : Blocks.LIGHT_BLUE_STAINED_GLASS.defaultBlockState();
			Identifier key = BuiltInRegistries.STRUCTURE_PIECE.getKey(piece.getType());
			float padding = Mth.lerp((float) successes / maxPieces, 0.003f, 0.025f);
			BoundingBox boundingBox = piece.getBoundingBox();
			if (this.displayUtil.spawnBlockDisplay(level, boundingBox, displayState, padding)) {
				MutableComponent nameLabel = key == null
					? Component.translatable("commands.tffeature.display_pieces.missing_key")
					: Component.literal(key.toString());
				this.displayUtil.setTextEntity(level, (boundingBox.minX() + boundingBox.maxX() + 1) * 0.5, boundingBox.minY() - padding, boundingBox.maxZ() + padding + 1, Display.BillboardConstraints.FIXED, nameLabel);

				successes++;
			}
		}

		return successes;
	}
}
