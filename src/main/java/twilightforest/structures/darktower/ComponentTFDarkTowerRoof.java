package twilightforest.structures.darktower;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerRoof;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.List;
import java.util.Random;

public class ComponentTFDarkTowerRoof extends ComponentTFTowerRoof {
	public ComponentTFDarkTowerRoof() {
	}

	public ComponentTFDarkTowerRoof(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i, wing);

		// same alignment
		this.setCoordBaseMode(wing.getCoordBaseMode());
		// same size
		this.size = wing.size; // assuming only square towers and roofs right now.
		this.height = 12;

		// just hang out at the very top of the tower
		makeCapBB(wing);

		// spawn list!
		this.spawnListIndex = 1;
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
	}

	/**
	 * A fence around the roof!
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// fence
		for (int x = 0; x <= size - 1; x++) {
			for (int z = 0; z <= size - 1; z++) {
				if (x == 0 || x == size - 1 || z == 0 || z == size - 1) {
					setBlockState(world, deco.fenceState, x, 1, z, sbb);
				}
			}
		}

		setBlockState(world, deco.accentState, 0, 1, 0, sbb);
		setBlockState(world, deco.accentState, size - 1, 1, 0, sbb);
		setBlockState(world, deco.accentState, 0, 1, size - 1, sbb);
		setBlockState(world, deco.accentState, size - 1, 1, size - 1, sbb);

		return true;
	}

}
