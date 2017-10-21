package twilightforest.structures.darktower;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.Random;

public class ComponentTFDarkTowerRoofFourPost extends ComponentTFDarkTowerRoof {

	public ComponentTFDarkTowerRoofFourPost() {
	}

	public ComponentTFDarkTowerRoofFourPost(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i, wing);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		super.addComponentParts(world, rand, sbb);

		makeSmallAntenna(world, sbb, 4, size - 2, size - 2);
		makeSmallAntenna(world, sbb, 5, 1, size - 2);
		makeSmallAntenna(world, sbb, 6, size - 2, 1);
		makeSmallAntenna(world, sbb, 7, 1, 1);


		return true;
	}

	private void makeSmallAntenna(World world, StructureBoundingBox sbb, int height, int x, int z) {
		// antenna
		for (int y = 1; y < height; y++) {
			setBlockState(world, deco.blockState, x, y, z, sbb);
		}
		setBlockState(world, deco.accentState, x, height + 0, z, sbb);
		setBlockState(world, deco.accentState, x, height + 1, z, sbb);
		setBlockState(world, deco.accentState, x + 1, height + 1, z, sbb);
		setBlockState(world, deco.accentState, x - 1, height + 1, z, sbb);
		setBlockState(world, deco.accentState, x, height + 1, z + 1, sbb);
		setBlockState(world, deco.accentState, x, height + 1, z - 1, sbb);
		setBlockState(world, deco.accentState, x, height + 2, z, sbb);
	}

}
