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


public class FinalCastleRoof13CrenellatedComponent extends TFStructureComponentOld {

	public FinalCastleRoof13CrenellatedComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCRo13Cr.get(), nbt);
	}

	public FinalCastleRoof13CrenellatedComponent(int i, TFStructureComponentOld sideTower, int x, int y, int z) {
		super(TFStructurePieceTypes.TFFCRo13Cr.get(), i, x, y, z);

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
		// assume square
		int size = this.boundingBox.maxX() - this.boundingBox.minX();

		for (Rotation rotation : RotationUtil.ROTATIONS) {
			// corner
			this.fillBlocksRotated(world, sbb, 0, -1, 0, 3, 3, 3, deco.blockState, rotation);
			this.setBlockStateRotated(world, deco.blockState, 1, -2, 2, rotation, sbb);
			this.setBlockStateRotated(world, deco.blockState, 1, -2, 1, rotation, sbb);
			this.setBlockStateRotated(world, deco.blockState, 2, -2, 1, rotation, sbb);

			// walls
			this.fillBlocksRotated(world, sbb, 4, 0, 1, size - 4, 1, 1, deco.blockState, rotation);

			// smaller crenellations
			for (int x = 5; x < size - 5; x += 4) {
				this.fillBlocksRotated(world, sbb, x, 0, 0, x + 2, 3, 2, deco.blockState, rotation);
				this.setBlockStateRotated(world, deco.blockState, x + 1, -1, 1, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, x + 1, -2, 1, rotation, sbb);
			}
		}
	}
}
