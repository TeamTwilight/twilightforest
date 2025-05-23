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
import twilightforest.world.components.structures.TFStructureComponentOld;


public class MazeMoundComponent extends TFStructureComponentOld {

	public MazeMoundComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMMound.get(), nbt);
	}

	public static final int DIAMETER = 35;

	private MazeUpperEntranceComponent mazeAbove;

	public MazeMoundComponent(int i, RandomSource rand, int x, int y, int z) {
		super(TFStructurePieceTypes.TFMMMound.get(), i, new BoundingBox(x, y, z, x + DIAMETER, y + 12, z + DIAMETER));
		this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(structurecomponent, list, random);

		// add aboveground maze entrance building
		mazeAbove = new MazeUpperEntranceComponent(3, random, boundingBox.minX() + 10, boundingBox.minY(), boundingBox.minZ() + 10);
		list.addPiece(mazeAbove);
		mazeAbove.addChildren(this, list, random);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// Mound shape contributed by Structure itself
	}
}
