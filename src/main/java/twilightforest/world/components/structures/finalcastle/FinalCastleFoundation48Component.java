package twilightforest.world.components.structures.finalcastle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.util.RotationUtil;

import java.util.Random;

public class FinalCastleFoundation48Component extends TFStructureComponentOld {

	public FinalCastleFoundation48Component(ServerLevel level, CompoundTag nbt) {
		super(FinalCastlePieces.TFFCToF48, nbt);
	}

	//TODO: Parameter "rand" is unused. Remove?
	public FinalCastleFoundation48Component(TFFeature feature, Random rand, int i, TFStructureComponentOld sideTower, int x, int y, int z) {
		super(FinalCastlePieces.TFFCToF48, feature, i, x, y, z);

		this.setOrientation(sideTower.getOrientation());
		this.boundingBox = new BoundingBox(sideTower.getBoundingBox().minX(), sideTower.getBoundingBox().minY(), sideTower.getBoundingBox().minZ(), sideTower.getBoundingBox().maxX(), sideTower.getBoundingBox().maxY() - 1, sideTower.getBoundingBox().maxZ());
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random randomIn, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
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

		return true;
	}

	private void makePiling(WorldGenLevel world, BoundingBox sbb, int mid, Rotation rotation, int i) {
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, i, -7, 3, rotation, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, i, -mid, 2, rotation, sbb);

		this.setBlockStateRotated(world, deco.blockState, i, -1, 0, rotation, sbb);
		this.setBlockStateRotated(world, deco.blockState, i, -3, 1, rotation, sbb);
		this.setBlockStateRotated(world, deco.blockState, i, -5, 2, rotation, sbb);
	}
}
