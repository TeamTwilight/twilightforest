package twilightforest.structures.icetower;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.structures.lichtower.ComponentTFTowerRoof;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.Random;

public class ComponentTFIceTowerRoof extends ComponentTFTowerRoof {

	public ComponentTFIceTowerRoof() {
	}

	public ComponentTFIceTowerRoof(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i, wing);

		// same alignment
		this.setCoordBaseMode(wing.getCoordBaseMode());
		// same size
		this.size = wing.size; // assuming only square towers and roofs right now.
		this.height = 12;

		this.deco = wing.deco;

		// just hang out at the very top of the tower
		makeCapBB(wing);
	}


	/**
	 * Swoopy ice roof
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		super.addComponentParts(world, rand, sbb);

		for (int x = 0; x < this.size; x++) {
			for (int z = 0; z < this.size; z++) {
				//int rHeight = this.size - (int) MathHelper.sqrt_float(x * z); // interesting office building pattern
				int rHeight = Math.round(MathHelper.sqrt(x * x + z * z));
				//int rHeight = MathHelper.ceiling_float_int(Math.min(x * x / 9F, z * z / 9F));

				for (int y = 0; y < rHeight; y++) {
					this.setBlockState(world, deco.blockState, x, y, z, sbb);

				}
			}
		}

		return true;
	}

}
