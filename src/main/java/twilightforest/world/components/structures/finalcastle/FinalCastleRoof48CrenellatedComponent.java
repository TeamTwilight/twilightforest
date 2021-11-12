package twilightforest.world.components.structures.finalcastle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
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
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.util.RotationUtil;

import java.util.Random;

public class FinalCastleRoof48CrenellatedComponent extends TFStructureComponentOld {

	public FinalCastleRoof48CrenellatedComponent(ServerLevel level, CompoundTag nbt) {
		super(FinalCastlePieces.TFFCRo48Cr, nbt);
	}

	public FinalCastleRoof48CrenellatedComponent(TFFeature feature, int i, TFStructureComponentOld keep, int x, int y, int z) {
		super(FinalCastlePieces.TFFCRo48Cr,feature, i, x, y, z);

		int height = 5;

		this.setOrientation(keep.getOrientation());
		this.boundingBox = new BoundingBox(keep.getBoundingBox().minX() - 2, keep.getBoundingBox().maxY() - 1, keep.getBoundingBox().minZ() - 2, keep.getBoundingBox().maxX() + 2, keep.getBoundingBox().maxY() + height - 1, keep.getBoundingBox().maxZ() + 2);

	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random randomIn, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// add second layer of floor
		final BlockState castleMagic = TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get().defaultBlockState();
		this.generateBox(world, sbb, 2, 2, 2, 50, 2, 50, castleMagic, castleMagic, false);

		// crenellations
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			this.fillBlocksRotated(world, sbb, 3, 1, 1, 45, 3, 1, deco.blockState, rotation);

			for (int i = 10; i < 41; i += 5) {
				this.fillBlocksRotated(world, sbb, i, 1, 0, i + 2, 5, 2, deco.blockState, rotation);
				this.setBlockStateRotated(world, deco.blockState, i + 1, 0, 1, rotation, sbb);
			}
		}

		return true;
	}
}
