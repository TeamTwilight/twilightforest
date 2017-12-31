package twilightforest.structures.finalcastle;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFCastleMagic;
import twilightforest.structures.StructureTFComponentOld;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleEntranceSideTower extends ComponentTFFinalCastleMazeTower13 {
	public ComponentTFFinalCastleEntranceSideTower() {
	}

	public ComponentTFFinalCastleEntranceSideTower(TFFeature feature, Random rand, int i, int x, int y, int z, int floors, int entranceFloor, EnumFacing direction) {
		super(feature, rand, i, x, y, z, floors, entranceFloor, BlockTFCastleMagic.VALID_COLORS.get(0), direction);

		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}

		// add foundation
		ComponentTFFinalCastleFoundation13 foundation = new ComponentTFFinalCastleFoundation13(getFeatureType(), rand, 4, this);
		list.add(foundation);
		foundation.buildComponent(this, list, rand);

		// add roof
		StructureTFComponentOld roof = new ComponentTFFinalCastleRoof13Peaked(getFeatureType(), rand, 4, this);
		list.add(roof);
		roof.buildComponent(this, list, rand);
	}

}
