package twilightforest.structures.darktower;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.Random;

public class ComponentTFDarkTowerBeard extends StructureTFComponentOld {

	protected int size;
	protected int height;

	public ComponentTFDarkTowerBeard(TemplateManager manager, CompoundNBT nbt) {
		super(TFDarkTowerPieces.TFDTBea, nbt);
		this.size = nbt.getInt("beardSize");
		this.height = nbt.getInt("beardHeight");
	}

	public ComponentTFDarkTowerBeard(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(TFDarkTowerPieces.TFDTBea, feature, i);

		this.setCoordBaseMode(wing.getCoordBaseMode());
		this.size = wing.size;
		this.height = size / 2;

		// just hang out at the very bottom of the tower
		this.boundingBox = new MutableBoundingBox(wing.getBoundingBox().minX, wing.getBoundingBox().minY - this.height, wing.getBoundingBox().minZ, wing.getBoundingBox().maxX, wing.getBoundingBox().minY, wing.getBoundingBox().maxZ);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);

		tagCompound.putInt("beardSize", this.size);
		tagCompound.putInt("beardHeight", this.height);
	}

	/**
	 * Makes a dark tower type beard
	 */
	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		makeDarkBeard(world, sbb, 0, 0, size - 1, height - 1, size - 1);

		return true;
	}

	protected void makeDarkBeard(ISeedReader world, MutableBoundingBox sbb, int minX, int minZ, int maxX, int maxY, int maxZ) {
		BlockState frameState = TFBlocks.tower_wood_encased.get().getDefaultState();

		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				if (x == minX || x == maxX || z == minZ || z == maxZ) {
					int length = Math.min(Math.abs(x - height) - 1, Math.abs(z - height) - 1);

					if (length == height - 1) {
						length++;
					}

					if (length == -1) {
						length = 1;
					}

					for (int y = maxY; y >= height - length; y--) {
						// wall
						this.setBlockState(world, frameState, x, y, z, sbb);
					}
				}
			}
		}
	}
}
