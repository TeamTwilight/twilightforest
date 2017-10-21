package twilightforest.structures.darktower;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.Random;

public class ComponentTFDarkTowerRoofCactus extends ComponentTFDarkTowerRoof {

	public ComponentTFDarkTowerRoofCactus() {
	}

	public ComponentTFDarkTowerRoofCactus(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i, wing);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		super.addComponentParts(world, rand, sbb);

		// antenna
		for (int y = 1; y < 10; y++) {
			setBlockState(world, deco.blockState, size / 2, y, size / 2, sbb);
		}
		setBlockState(world, deco.accentState, size / 2, 10, size / 2, sbb);

		setBlockState(world, deco.accentState, size / 2 - 1, 1, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 1, 1, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 1, size / 2 - 1, sbb);
		setBlockState(world, deco.accentState, size / 2, 1, size / 2 + 1, sbb);

		// cactus things
		setBlockState(world, deco.accentState, size / 2 + 1, 7, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 2, 7, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 2, 8, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 2, 9, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 3, 9, size / 2, sbb);

		setBlockState(world, deco.accentState, size / 2, 6, size / 2 + 1, sbb);
		setBlockState(world, deco.accentState, size / 2, 6, size / 2 + 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 7, size / 2 + 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 8, size / 2 + 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 8, size / 2 + 3, sbb);

		setBlockState(world, deco.accentState, size / 2 - 1, 5, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 - 2, 5, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 - 2, 6, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 - 2, 7, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 - 3, 7, size / 2, sbb);

		setBlockState(world, deco.accentState, size / 2, 4, size / 2 - 1, sbb);
		setBlockState(world, deco.accentState, size / 2, 4, size / 2 - 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 5, size / 2 - 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 6, size / 2 - 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 6, size / 2 - 3, sbb);


		return true;
	}
}
