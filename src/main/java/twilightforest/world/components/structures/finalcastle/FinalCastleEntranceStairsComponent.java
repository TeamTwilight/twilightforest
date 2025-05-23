package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TFStructureComponentOld;


/**
 * Stair blocks heading to the entrance tower doors
 */
public class FinalCastleEntranceStairsComponent extends TFStructureComponentOld {

	public FinalCastleEntranceStairsComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCEnSt.get(), nbt);
	}

	public FinalCastleEntranceStairsComponent(int index, int x, int y, int z, Direction direction) {
		super(TFStructurePieceTypes.TFFCEnSt.get(), index, x, y, z);
		this.setOrientation(direction);
		this.boundingBox = TFStructureComponentOld.getComponentToAddBoundingBox2(x, y, z, 0, -1, -5, 12, 0, 12, direction);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource randomIn, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		int size = 13;

		for (int x = 1; x < size; x++) {

			this.placeStairs(world, sbb, x, 1 - x, 5, Direction.EAST);

			for (int z = 0; z <= x; z++) {

				if (z > 0 && z <= size / 2) {
					this.placeStairs(world, sbb, x, 1 - x, 5 - z, Direction.EAST);
					this.placeStairs(world, sbb, x, 1 - x, 5 + z, Direction.EAST);
				}

				if (x <= size / 2) {
					this.placeStairs(world, sbb, z, 1 - x, 5 - x, Direction.NORTH);
					this.placeStairs(world, sbb, z, 1 - x, 5 + x, Direction.SOUTH);
				}
			}
		}

		this.fillColumnDown(world, deco.blockState, 0, 0, 5, sbb);
	}

	private void placeStairs(WorldGenLevel world, BoundingBox sbb, int x, int y, int z, Direction facing) {
		if (this.getBlock(world, x, y, z, sbb).canBeReplaced()) {
			this.placeBlock(world, deco.stairState.setValue(StairBlock.FACING, facing), x, y, z, sbb);
			this.fillColumnDown(world, deco.blockState, x, y - 1, z, sbb);
		}
	}
}
