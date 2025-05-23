package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
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
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class FinalCastleFoundation48Component extends TFStructureComponentOld {

	public FinalCastleFoundation48Component(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCToF48.get(), nbt);
	}

	public FinalCastleFoundation48Component(int i, TFStructureComponentOld sideTower, int x, int y, int z) {
		super(TFStructurePieceTypes.TFFCToF48.get(), i, x, y, z);

		this.setOrientation(sideTower.getOrientation());
		this.boundingBox = new BoundingBox(sideTower.getBoundingBox().minX(), sideTower.getBoundingBox().minY(), sideTower.getBoundingBox().minZ(), sideTower.getBoundingBox().maxX(), sideTower.getBoundingBox().maxY() - 1, sideTower.getBoundingBox().maxZ());
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource randomIn, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// foundation
		for (int x = 4; x < 45; x++) {
			for (int z = 4; z < 45; z++) {
				this.fillColumnDown(world, deco.blockState, x, -1, z, sbb);
			}
		}

		int mid = 16;
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			// do corner
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 3, -2, 3, rotation, sbb);

			// directly under castle
			this.fillBlocksRotated(world, sbb, 2, -2, 1, 46, -1, 1, deco.blockState, rotation);
			this.fillBlocksRotated(world, sbb, 2, -4, 2, 45, -1, 2, deco.blockState, rotation);
			this.fillBlocksRotated(world, sbb, 4, -6, 3, 44, -1, 3, deco.blockState, rotation);

			// pilings
			for (int i = 9; i < 45; i += 6) {
				makePiling(world, sbb, mid, rotation, i);
			}

			makePiling(world, sbb, mid, rotation, 4);
			makePiling(world, sbb, mid, rotation, 44);
		}

		// add supports for entrance bridge
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 21, -2, 0, Rotation.CLOCKWISE_90, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 21, -4, 1, Rotation.CLOCKWISE_90, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 21, -6, 2, Rotation.CLOCKWISE_90, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 27, -2, 0, Rotation.CLOCKWISE_90, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 27, -4, 1, Rotation.CLOCKWISE_90, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 27, -6, 2, Rotation.CLOCKWISE_90, sbb);
	}

	private void makePiling(WorldGenLevel world, BoundingBox sbb, int mid, Rotation rotation, int i) {
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, i, -7, 3, rotation, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, i, -mid, 2, rotation, sbb);

		this.setBlockStateRotated(world, deco.blockState, i, -1, 0, rotation, sbb);
		this.setBlockStateRotated(world, deco.blockState, i, -3, 1, rotation, sbb);
		this.setBlockStateRotated(world, deco.blockState, i, -5, 2, rotation, sbb);
	}
}
