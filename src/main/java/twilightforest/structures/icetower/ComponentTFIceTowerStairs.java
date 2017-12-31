package twilightforest.structures.icetower;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.List;
import java.util.Random;

public class ComponentTFIceTowerStairs extends ComponentTFTowerWing {

	public ComponentTFIceTowerStairs() {
	}


	public ComponentTFIceTowerStairs(TFFeature feature, int index, int x, int y, int z, int size, int height, EnumFacing direction) {
		super(feature, index, x, y, z, size, height, direction);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {

		for (int x = 1; x < this.size; x++) {

			this.placeStairs(world, sbb, x, 1 - x, 5, 2);

			for (int z = 0; z <= x; z++) {

				if (z > 0 && z <= this.size / 2) {
					this.placeStairs(world, sbb, x, 1 - x, 5 - z, 2);
					this.placeStairs(world, sbb, x, 1 - x, 5 + z, 2);
				}

				if (x <= this.size / 2) {
					this.placeStairs(world, sbb, z, 1 - x, 5 - x, 1);
					this.placeStairs(world, sbb, z, 1 - x, 5 + x, 3);
				}

			}

		}

		this.setBlockState(world, deco.blockState, 0, 0, 5, sbb);


		return true;
	}

	private void placeStairs(World world, StructureBoundingBox sbb, int x, int y, int z, int stairMeta) {
		BlockPos pos = new BlockPos(x, y, z);
		if (this.getBlockStateFromPos(world, x, y, z, sbb).getBlock().isReplaceable(world, pos)) {
			this.setBlockState(world, deco.blockState, x, y, z, sbb);
			//this.setBlockState(world, deco.stairID, this.getStairMeta(stairMeta), x, y, z, sbb);
			this.setBlockState(world, deco.blockState, x, y - 1, z, sbb);
		}
	}
}
