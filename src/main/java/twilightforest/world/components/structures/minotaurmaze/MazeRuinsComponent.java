package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.util.Random;

/**
 * This component is the base for the maze/ruins combo feature.  There are village-like ruins above and a maze underneath.
 *
 * @author Ben
 */
public class MazeRuinsComponent extends TFStructureComponentOld {

	public MazeRuinsComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMRuins, nbt);
	}

	public MazeRuinsComponent(TFFeature feature, int i, int x, int y, int z) {
		super(MinotaurMazePieces.TFMMRuins, feature, i, feature.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 0, 0, 0, Direction.SOUTH));
		this.setOrientation(Direction.SOUTH);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, Random random) {
		super.addChildren(structurecomponent, list, random);

		// add a maze
		MinotaurMazeComponent maze = new MinotaurMazeComponent(getFeatureType(), 1, boundingBox.minX(), boundingBox.minY() - 14, boundingBox.minZ(), 1);
		list.addPiece(maze);
		maze.addChildren(this, list, random);

		// add maze entrance shaft
		MazeEntranceShaftComponent mazeEnter = new MazeEntranceShaftComponent(getFeatureType(), 2, random, boundingBox.minX() + 1, boundingBox.minY(), boundingBox.minZ() + 1);
		list.addPiece(mazeEnter);
		mazeEnter.addChildren(this, list, random);

		// add aboveground maze entrance building
		MazeMoundComponent mazeAbove = new MazeMoundComponent(getFeatureType(), 2, random, boundingBox.minX() - 14, boundingBox.maxY(), boundingBox.minZ() - 14);
		list.addPiece(mazeAbove);
		mazeAbove.addChildren(this, list, random);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
	 * the end, it adds Fences...
	 */
	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// I have no components
		return true;
	}
}
