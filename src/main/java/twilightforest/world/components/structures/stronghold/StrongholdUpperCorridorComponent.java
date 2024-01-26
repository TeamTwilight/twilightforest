package twilightforest.world.components.structures.stronghold;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;


public class StrongholdUpperCorridorComponent extends KnightStrongholdComponent {

	public StrongholdUpperCorridorComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFSUCo.get(), nbt);
	}

	public StrongholdUpperCorridorComponent(int i, Direction facing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFSUCo.get(), i, facing, x, y, z);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return BoundingBox.orientBox(x, y, z, -2, -1, 0, 5, 5, 9, facing);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(parent, list, random);

		// make a random component at the end
		addNewUpperComponent(parent, list, random, Rotation.NONE, 2, 1, 9);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		/*if (this.edgesLiquid(world, sbb)) {
			return false;
		} else */{
			placeUpperStrongholdWalls(world, sbb, 0, 0, 0, 4, 4, 8, rand, deco.randomBlocks);

			// entrance doorway
			placeSmallDoorwayAt(world, 2, 2, 1, 0, sbb);

			// end
			placeSmallDoorwayAt(world, 2, 2, 1, 8, sbb);
		}
	}

	@Override
	public boolean isComponentProtected() {
		return false;
	}
}
