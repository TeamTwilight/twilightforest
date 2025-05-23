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


public class FinalCastleRoof9CrenellatedComponent extends TFStructureComponentOld {

	public FinalCastleRoof9CrenellatedComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCRo9Cr.get(), nbt);
	}

	public FinalCastleRoof9CrenellatedComponent(int i, TFStructureComponentOld sideTower, int x, int y, int z) {
		super(TFStructurePieceTypes.TFFCRo9Cr.get(), i, x, y, z);

		int height = 5;

		this.setOrientation(sideTower.getOrientation());
		this.boundingBox = new BoundingBox(sideTower.getBoundingBox().minX() - 2, sideTower.getBoundingBox().maxY() - 1, sideTower.getBoundingBox().minZ() - 2, sideTower.getBoundingBox().maxX() + 2, sideTower.getBoundingBox().maxY() + height - 1, sideTower.getBoundingBox().maxZ() + 2);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource randomIn, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			this.fillBlocksRotated(world, sbb, 0, -1, 0, 2, 3, 2, deco.blockState, rotation);
			this.setBlockStateRotated(world, deco.blockState, 1, -2, 2, rotation, sbb);
			this.setBlockStateRotated(world, deco.blockState, 1, -2, 1, rotation, sbb);
			this.setBlockStateRotated(world, deco.blockState, 2, -2, 1, rotation, sbb);

			this.setBlockStateRotated(world, deco.blockState, 3, 0, 1, rotation, sbb);
			this.setBlockStateRotated(world, deco.blockState, 3, 1, 1, rotation, sbb);

			this.fillBlocksRotated(world, sbb, 4, 0, 0, 5, 3, 2, deco.blockState, rotation);

			this.setBlockStateRotated(world, deco.blockState, 6, 0, 1, rotation, sbb);
			this.setBlockStateRotated(world, deco.blockState, 6, 1, 1, rotation, sbb);

			this.fillBlocksRotated(world, sbb, 7, 0, 0, 8, 3, 2, deco.blockState, rotation);

			this.setBlockStateRotated(world, deco.blockState, 9, 0, 1, rotation, sbb);
			this.setBlockStateRotated(world, deco.blockState, 9, 1, 1, rotation, sbb);
		}
	}
}
