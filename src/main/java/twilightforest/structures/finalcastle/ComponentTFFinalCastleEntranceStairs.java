package twilightforest.structures.finalcastle;

import net.minecraft.block.BlockStairs;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;

import java.util.List;
import java.util.Random;

/**
 * Stair blocks heading to the entrance tower doors
 */
public class ComponentTFFinalCastleEntranceStairs extends StructureTFComponentOld {

	public ComponentTFFinalCastleEntranceStairs() {
	}

	public ComponentTFFinalCastleEntranceStairs(TFFeature feature, int index, int x, int y, int z, EnumFacing direction) {
		super(feature, index);
		this.setCoordBaseMode(direction);
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox2(x, y, z, 0, -1, -5, 12, 0, 12, direction);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {

		int size = 13;

		for (int x = 1; x < size; x++) {

			this.placeStairs(world, sbb, x, 1 - x, 5, EnumFacing.EAST);

			for (int z = 0; z <= x; z++) {

				if (z > 0 && z <= size / 2) {
					this.placeStairs(world, sbb, x, 1 - x, 5 - z, EnumFacing.EAST);
					this.placeStairs(world, sbb, x, 1 - x, 5 + z, EnumFacing.EAST);
				}

				if (x <= size / 2) {
					this.placeStairs(world, sbb, z, 1 - x, 5 - x, EnumFacing.NORTH);
					this.placeStairs(world, sbb, z, 1 - x, 5 + x, EnumFacing.SOUTH);
				}
			}
		}

		this.replaceAirAndLiquidDownwards(world, deco.blockState, 0, 0, 5, sbb);


		return true;
	}

	private void placeStairs(World world, StructureBoundingBox sbb, int x, int y, int z, EnumFacing facing) {
		if (this.getBlockStateFromPos(world, x, y, z, sbb).getBlock().isReplaceable(world, this.getBlockPosWithOffset(x, y, z))) {
			//this.setBlockState(world, deco.blockState, x, y, z, sbb);
			this.setBlockState(world, deco.stairState.withProperty(BlockStairs.FACING, facing), x, y, z, sbb);
			this.replaceAirAndLiquidDownwards(world, deco.blockState, x, y - 1, z, sbb);
		}
	}


}
