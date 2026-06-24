package twilightforest.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import tamaized.beanification.Autowired;
import twilightforest.util.DisplayUtil;

@tamaized.beanification.Component
public class StructureDistanceCommand {
	@Autowired
	private DisplayUtil displayUtil;

	public ArgumentBuilder<CommandSourceStack, ?> register() {
		return Commands.literal("generator_radius").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
			.then(Commands.argument("filter_structure", ResourceKeyArgument.key(Registries.STRUCTURE)).executes(this::structureDistance));
	}

	private int structureDistance(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Holder.Reference<Structure> structure = ResourceKeyArgument.getStructure(context, "filter_structure");

		if (!structure.isBound()) return 0;

		CommandSourceStack source = context.getSource();
		ServerLevel level = source.getLevel();

		BlockPos commandPos = BlockPos.containing(source.getPosition());
		int sectionY = SectionPos.blockToSectionCoord(commandPos.getY() - 16) << 4;
		StructureStart structureAt = level.structureManager().getStructureAt(commandPos, structure.value());

		ChunkPos centerChunk = structureAt.getChunkPos();
		for (ChunkPos chunkPos : ChunkPos.rangeClosed(centerChunk, 8).toList()) {
			int diffX = chunkPos.x() - centerChunk.x();
			int diffZ = chunkPos.z() - centerChunk.z();
			int squareRadiusDist = Math.max(Mth.abs(diffX), Mth.abs(diffZ));

			int minX = chunkPos.getMinBlockX();
			int maxX = chunkPos.getMaxBlockX();
			int minZ = chunkPos.getMinBlockZ();
			int maxZ = chunkPos.getMaxBlockZ();
			BoundingBox boundingBox = new BoundingBox(minX, sectionY, minZ, maxX, sectionY + 15, maxZ);

			BlockState displayState = switch (squareRadiusDist) {
				case 1 -> Blocks.RED_STAINED_GLASS.defaultBlockState();
				case 2 -> Blocks.ORANGE_STAINED_GLASS.defaultBlockState();
				case 3 -> Blocks.YELLOW_STAINED_GLASS.defaultBlockState();
				case 4 -> Blocks.LIME_STAINED_GLASS.defaultBlockState();
				case 5 -> Blocks.BLUE_STAINED_GLASS.defaultBlockState();
				case 6 -> Blocks.PURPLE_STAINED_GLASS.defaultBlockState();
				case 7 -> Blocks.MAGENTA_STAINED_GLASS.defaultBlockState();
				case 8 -> Blocks.PINK_STAINED_GLASS.defaultBlockState();
				default -> Blocks.GLASS.defaultBlockState();
			};

			this.displayUtil.spawnBlockDisplay(level, boundingBox, displayState, -0.1f);

			double x = (boundingBox.minX() + boundingBox.maxX() + 1) * 0.5;
			double z = (boundingBox.minZ() + boundingBox.maxZ() + 1) * 0.5;

			String deltaChunkCoord = diffX + ", " + diffZ;
			this.displayUtil.setTextEntity(level, x, boundingBox.maxY() + 1, z, Display.BillboardConstraints.CENTER, Component.literal(deltaChunkCoord));

			Component radiusInfo = squareRadiusDist == 0
				? Component.translatable("commands.tffeature.generator_radius.center_chunk")
				: Component.translatable("commands.tffeature.generator_radius.radius", squareRadiusDist);
			this.displayUtil.setTextEntity(level, x, boundingBox.maxY() + 2, z, Display.BillboardConstraints.CENTER, radiusInfo);
		}

		return 0;
	}
}
