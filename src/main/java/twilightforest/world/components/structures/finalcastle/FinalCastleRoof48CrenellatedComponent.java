package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class FinalCastleRoof48CrenellatedComponent extends TFStructureComponentOld {

	public FinalCastleRoof48CrenellatedComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCRo48Cr.get(), nbt);
	}

	public FinalCastleRoof48CrenellatedComponent(int i, TFStructureComponentOld keep, int x, int y, int z) {
		super(TFStructurePieceTypes.TFFCRo48Cr.get(), i, x, y, z);

		int height = 5;

		this.setOrientation(keep.getOrientation());
		this.boundingBox = new BoundingBox(keep.getBoundingBox().minX() - 2, keep.getBoundingBox().maxY() - 1, keep.getBoundingBox().minZ() - 2, keep.getBoundingBox().maxX() + 2, keep.getBoundingBox().maxY() + height - 1, keep.getBoundingBox().maxZ() + 2);

	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource randomIn, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
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
	}
}
