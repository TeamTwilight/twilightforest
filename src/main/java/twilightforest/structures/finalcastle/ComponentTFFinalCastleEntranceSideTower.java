package twilightforest.structures.finalcastle;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.block.BlockTFCastleMagic;
import twilightforest.structures.StructureTFComponent;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleEntranceSideTower extends ComponentTFFinalCastleMazeTower13 {
	public ComponentTFFinalCastleEntranceSideTower() {
	}

	public ComponentTFFinalCastleEntranceSideTower(Random rand, int i, int x, int y, int z, int floors, int entranceFloor, EnumFacing direction) {
		super(rand, i, x, y, z, floors, entranceFloor, BlockTFCastleMagic.VALID_COLORS.get(0), direction);

		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void buildComponent(StructureComponent parent, List list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponent) {
			this.deco = ((StructureTFComponent) parent).deco;
		}

		// add foundation
		ComponentTFFinalCastleFoundation13 foundation = new ComponentTFFinalCastleFoundation13(rand, 4, this);
		list.add(foundation);
		foundation.buildComponent(this, list, rand);

		// add roof
		StructureTFComponent roof = new ComponentTFFinalCastleRoof13Peaked(rand, 4, this);
		list.add(roof);
		roof.buildComponent(this, list, rand);
	}

}
