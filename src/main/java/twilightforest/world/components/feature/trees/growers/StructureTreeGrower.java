package twilightforest.world.components.feature.trees.growers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import twilightforest.init.TFStructures;
import twilightforest.world.components.structures.TreeGrowerStartable;

import java.util.Optional;

public class StructureTreeGrower extends TreeGrower {
	public StructureTreeGrower(String name) {
		super(name, Optional.empty(), Optional.empty(), Optional.empty());
	}

	//copied from PlaceCommand.placeStructure
	@Override
	public boolean growTree(ServerLevel level, ChunkGenerator generator, BlockPos pos, BlockState state, RandomSource random) {
		Structure structure = level.registryAccess().registryOrThrow(Registries.STRUCTURE).getOrThrow(TFStructures.HOLLOW_TREE);

		if (!(structure instanceof TreeGrowerStartable treeGrowerStartable) || !treeGrowerStartable.checkSaplingClearance(level, pos))
			return false;

		StructureStart structurestart = treeGrowerStartable.generateFromSapling(level.registryAccess(), generator, generator.getBiomeSource(), level.getChunkSource().randomState(), level.getStructureManager(), level.getSeed(), pos, level);

		if (!structurestart.isValid())
			return false;

		BoundingBox boundingbox = structurestart.getBoundingBox();
		ChunkPos start = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()), SectionPos.blockToSectionCoord(boundingbox.minZ()));
		ChunkPos end = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()), SectionPos.blockToSectionCoord(boundingbox.maxZ()));

		if (ChunkPos.rangeClosed(start, end).noneMatch(currentChunkPos -> level.isLoaded(currentChunkPos.getWorldPosition())))
			return false;

		level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);

		ChunkPos.rangeClosed(start, end).forEach(
			chunkPos -> structurestart.placeInChunk(
				level,
				level.structureManager(),
				generator,
				level.getRandom(),
				new BoundingBox(
					chunkPos.getMinBlockX(),
					level.getMinBuildHeight(),
					chunkPos.getMinBlockZ(),
					chunkPos.getMaxBlockX(),
					level.getMaxBuildHeight(),
					chunkPos.getMaxBlockZ()
				),
				chunkPos
			)
		);

		return true;
	}
}
