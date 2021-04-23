package twilightforest.structures.finalcastle;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;

/**
 * Pointy cone roof with variable height
 */
public class ComponentTFFinalCastleRoof13Conical extends StructureTFComponentOld {

	public int slope;

	public ComponentTFFinalCastleRoof13Conical(TemplateManager manager, CompoundNBT nbt) {
		super(TFFinalCastlePieces.TFFCRo13Con, nbt);
		this.slope = nbt.getInt("slope");
	}

	public ComponentTFFinalCastleRoof13Conical(TFFeature feature, Random rand, int i, StructureTFComponentOld sideTower) {
		super(TFFinalCastlePieces.TFFCRo13Con, feature, i);

		this.slope = 2 + rand.nextInt(3) + rand.nextInt(3);

		int height = slope * 4;

		this.setCoordBaseMode(sideTower.getCoordBaseMode());
		this.boundingBox = new MutableBoundingBox(sideTower.getBoundingBox().minX - 2, sideTower.getBoundingBox().maxY - 1, sideTower.getBoundingBox().minZ - 2, sideTower.getBoundingBox().maxX + 2, sideTower.getBoundingBox().maxY + height - 1, sideTower.getBoundingBox().maxZ + 2);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putInt("slope", this.slope);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random randomIn, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
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

		return true;
	}
}
