package twilightforest.structures.finalcastle;

import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.structures.StructureTFComponent;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleFoundation48 extends StructureTFComponent {

	public ComponentTFFinalCastleFoundation48() {
	}

	public ComponentTFFinalCastleFoundation48(Random rand, int i, StructureTFComponent sideTower) {
		super(i);

		this.setCoordBaseMode(sideTower.getCoordBaseMode());
		this.boundingBox = new StructureBoundingBox(sideTower.getBoundingBox().minX, sideTower.getBoundingBox().minY, sideTower.getBoundingBox().minZ, sideTower.getBoundingBox().maxX, sideTower.getBoundingBox().minY - 1, sideTower.getBoundingBox().maxZ);

	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponent) {
			this.deco = ((StructureTFComponent) parent).deco;
		}
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// foundation
		for (int x = 4; x < 45; x++) {
			for (int z = 4; z < 45; z++) {
				this.replaceAirAndLiquidDownwards(world, deco.blockState, x, -1, z, sbb);
			}

		}

		int mid = 16;
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			// do corner
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 3, -2, 3, rotation, sbb);

			// directly under castle
			this.fillBlocksRotated(world, sbb, 2, -2, 1, 46, -1, 1, deco.blockState, rotation);
			this.fillBlocksRotated(world, sbb, 2, -4, 2, 45, -1, 2, deco.blockState, rotation);
			this.fillBlocksRotated(world, sbb, 4, -6, 3, 44, -1, 3, deco.blockState, rotation);

			// pilings
			for (int i = 9; i < 45; i += 6) {
				makePiling(world, sbb, mid, rotation, i);
			}

			makePiling(world, sbb, mid, rotation, 4);
			makePiling(world, sbb, mid, rotation, 44);

		}

// add supports for entrance bridge
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 21, -2, 0, Rotation.CLOCKWISE_90, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 21, -4, 1, Rotation.CLOCKWISE_90, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 21, -6, 2, Rotation.CLOCKWISE_90, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 27, -2, 0, Rotation.CLOCKWISE_90, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 27, -4, 1, Rotation.CLOCKWISE_90, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 27, -6, 2, Rotation.CLOCKWISE_90, sbb);

		return true;
	}

	private void makePiling(World world, StructureBoundingBox sbb, int mid, Rotation rotation, int i) {
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, i, -7, 3, rotation, sbb);
		this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, i, -mid, 2, rotation, sbb);

		this.setBlockStateRotated(world, deco.blockState, i, -1, 0, rotation, sbb);
		this.setBlockStateRotated(world, deco.blockState, i, -3, 1, rotation, sbb);
		this.setBlockStateRotated(world, deco.blockState, i, -5, 2, rotation, sbb);
	}

}
