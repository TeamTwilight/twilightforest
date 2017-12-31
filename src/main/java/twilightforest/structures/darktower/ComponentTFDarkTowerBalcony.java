package twilightforest.structures.darktower;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.List;
import java.util.Random;

public class ComponentTFDarkTowerBalcony extends ComponentTFTowerWing {

	public ComponentTFDarkTowerBalcony() {
	}

	protected ComponentTFDarkTowerBalcony(TFFeature feature, int i, int x, int y, int z, EnumFacing direction) {
		super(feature, i, x, y, z, 5, 5, direction);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {

		// make floor
		fillWithBlocks(world, sbb, 0, 0, 0, 2, 0, 4, deco.accentState, Blocks.AIR.getDefaultState(), false);
		fillWithBlocks(world, sbb, 0, 0, 1, 1, 0, 3, deco.blockState, Blocks.AIR.getDefaultState(), false);

		fillWithBlocks(world, sbb, 0, 1, 0, 2, 1, 4, deco.fenceState, Blocks.AIR.getDefaultState(), false);

		this.setBlockState(world, deco.accentState, 2, 1, 0, sbb);
		this.setBlockState(world, deco.accentState, 2, 1, 4, sbb);

		// clear inside
		fillWithAir(world, sbb, 0, 1, 1, 1, 1, 3);

		return true;
	}


}
