package twilightforest.structures.lichtower;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;

import java.util.Random;

/**
 * A flat tower roof using slabs that is larger than the tower under it.
 *
 * @author Ben
 */
public class ComponentTFTowerRoofSlabForwards extends ComponentTFTowerRoofSlab {

	public ComponentTFTowerRoofSlabForwards(TemplateManager manager, CompoundNBT nbt) {
		super(TFLichTowerPieces.TFLTRSF, nbt);
	}

	public ComponentTFTowerRoofSlabForwards(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(TFLichTowerPieces.TFLTRSF, feature, i, wing);

		// same alignment
		this.setCoordBaseMode(wing.getCoordBaseMode());
		// the overhang roof is like a cap roof that's 2 sizes bigger
		this.size = wing.size + 2; // assuming only square towers and roofs right now.
		this.height = size / 2;

		// bounding box
		makeAttachedOverhangBB(wing);
	}

	/**
	 * Makes flat hip roof
	 */
	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		BlockState birchSlab = Blocks.BIRCH_SLAB.getDefaultState();
		BlockState birchDoubleSlab = Blocks.BIRCH_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE);

		for (int y = 0; y <= height; y++) {
			int min = 2 * y;
			int max = size - (2 * y) - 1;
			for (int x = 0; x <= max - 1; x++) {
				for (int z = min; z <= max; z++) {
					if (x == max - 1 || z == min || z == max) {
						setBlockState(world, birchSlab, x, y, z, sbb);
					} else {
						setBlockState(world, birchDoubleSlab, x, y, z, sbb);
					}
				}
			}
		}
		return true;
	}
}
