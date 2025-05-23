package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.world.components.structures.TFStructureComponentOld;


/**
 * This component is the base for the maze/ruins combo feature.  There are village-like ruins above and a maze underneath.
 *
 * @author Ben
 */
public class MazeRuinsComponent extends TFStructureComponentOld {

	public MazeRuinsComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMRuins.get(), nbt);
	}

	public MazeRuinsComponent(int i, int x, int y, int z) {
		super(TFStructurePieceTypes.TFMMRuins.get(), i, BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 0, 0, 0, Direction.SOUTH, false));
		this.setOrientation(Direction.SOUTH);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(structurecomponent, list, random);

		// add a maze
		MinotaurMazeComponent maze = new MinotaurMazeComponent(1, boundingBox.minX(), boundingBox.minY() - 14, boundingBox.minZ(), 1, random);
		list.addPiece(maze);
		maze.addChildren(this, list, random);

		// add maze entrance shaft
		MazeEntranceShaftComponent mazeEnter = new MazeEntranceShaftComponent(2, random, boundingBox.minX() + 1, boundingBox.minY(), boundingBox.minZ() + 1);
		list.addPiece(mazeEnter);
		mazeEnter.addChildren(this, list, random);

		// add aboveground maze entrance building
		MazeMoundComponent mazeAbove = new MazeMoundComponent(2, random, boundingBox.minX() - 14, boundingBox.maxY(), boundingBox.minZ() - 14);
		list.addPiece(mazeAbove);
		mazeAbove.addChildren(this, list, random);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
	 * the end, it adds Fences...
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// I have no components
	}
}
