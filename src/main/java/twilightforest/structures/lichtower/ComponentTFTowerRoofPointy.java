package twilightforest.structures.lichtower;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;

import java.util.Random;

public class ComponentTFTowerRoofPointy extends ComponentTFTowerRoof {

	public ComponentTFTowerRoofPointy(TemplateManager manager, CompoundNBT nbt) {
		super(TFLichTowerPieces.TFLTRP, nbt);
	}

	public ComponentTFTowerRoofPointy(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
	}

	public ComponentTFTowerRoofPointy(IStructurePieceType piece, TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(piece, feature, i);

		// same facing, but it doesn't matter
		this.setCoordBaseMode(wing.getCoordBaseMode());

		this.size = wing.size; // assuming only square towers and roofs right now.
		this.height = size;

		// just hang out at the very top of the tower
		makeCapBB(wing);
	}

	/**
	 * Makes a pointy roof out of stuff
	 */
	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		BlockState birchSlab = Blocks.BIRCH_SLAB.getDefaultState();
		BlockState birchPlanks = Blocks.BIRCH_PLANKS.getDefaultState();

		for (int y = 0; y <= height; y++) {
			int min, mid, max;
			int slopeChange = slopeChangeForSize();
			if (y < slopeChange) {
				min = y;
				max = size - y - 1;
			} else {
				min = (y + slopeChange) / 2;
				max = size - ((y + slopeChange) / 2) - 1;
			}
			mid = min + ((max - min) / 2);
			for (int x = min; x <= max; x++) {
				for (int z = min; z <= max; z++) {
					setBlockState(world, birchPlanks, x, y, z, sbb);
					// some of these are unnecessary and will just be overwritten by a normal block, but whatevs.
					if ((x == min && (z == min || z == max)) || (x == max && (z == min || z == max))) {
						setBlockState(world, birchSlab, x, y + 1, z, sbb);
					}
					// mid blocks
					if (((((x == min || x == max) && z == mid) && x % 2 == 0) || (((z == min || z == max) && x == mid) && z % 2 == 0)) && mid != min + 1) {
						setBlockState(world, birchSlab, x, y + 1, z, sbb);
					}
				}
			}
		}
		return true;
	}

	public int slopeChangeForSize() {
		if (size > 10) {
			return 3;
		} else if (size > 6) {
			return 2;
		} else {
			return 1;
		}
	}
}
