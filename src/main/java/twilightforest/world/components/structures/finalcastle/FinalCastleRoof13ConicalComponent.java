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


/**
 * Pointy cone roof with variable height
 */
public class FinalCastleRoof13ConicalComponent extends TFStructureComponentOld {

	public final int slope;

	public FinalCastleRoof13ConicalComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCRo13Con.get(), nbt);
		this.slope = nbt.getInt("slope");
	}

	public FinalCastleRoof13ConicalComponent(RandomSource rand, int i, TFStructureComponentOld sideTower, int x, int y, int z) {
		super(TFStructurePieceTypes.TFFCRo13Con.get(), i, x, y, z);

		this.slope = 2 + rand.nextInt(3) + rand.nextInt(3);

		int height = slope * 4;

		this.setOrientation(sideTower.getOrientation());
		this.boundingBox = new BoundingBox(sideTower.getBoundingBox().minX() - 2, sideTower.getBoundingBox().maxY() - 1, sideTower.getBoundingBox().minZ() - 2, sideTower.getBoundingBox().maxX() + 2, sideTower.getBoundingBox().maxY() + height - 1, sideTower.getBoundingBox().maxZ() + 2);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putInt("slope", this.slope);
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
			this.fillBlocksRotated(world, sbb, 0, -1, 0, 3, 2, 3, deco.blockState, rotation);
			this.setBlockStateRotated(world, deco.blockState, 1, -2, 2, rotation, sbb);
			this.setBlockStateRotated(world, deco.blockState, 1, -2, 1, rotation, sbb);
			this.setBlockStateRotated(world, deco.blockState, 2, -2, 1, rotation, sbb);

			this.fillBlocksRotated(world, sbb, 4, 0, 1, 12, 1, 1, deco.blockState, rotation);

			// more teeny crenellations
			for (int i = 3; i < 13; i += 2) {
				this.fillBlocksRotated(world, sbb, i, -1, 1, i, 2, 1, deco.blockState, rotation);
			}

			// cone roof
			for (int i = 2; i < 9; i++) {
				int base = 2 - slope;
				if (i < 7) {
					this.fillBlocksRotated(world, sbb, i - 1, ((i - 1) * slope) + base, i - 1, i, (i * slope) + base - 1, i, deco.blockState, rotation);
				} else {
					this.fillBlocksRotated(world, sbb, 16 - i, ((i - 1) * slope) + base, i, 16 - i, (i * slope) + base - 1, i, deco.roofState, rotation);
				}
				this.fillBlocksRotated(world, sbb, i + 1, ((i - 1) * slope) + base, i, 15 - i, (i * slope) + base - 1, i, deco.roofState, rotation);
			}

			// point!
			this.fillBlocksRotated(world, sbb, 8, (slope * 6) + 2, 8, 8, (slope * 7) + 2, 8, deco.roofState, rotation);
		}
	}
}
