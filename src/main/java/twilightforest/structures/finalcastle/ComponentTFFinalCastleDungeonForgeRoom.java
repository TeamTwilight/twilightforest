package twilightforest.structures.finalcastle;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;

import java.util.Random;

public class ComponentTFFinalCastleDungeonForgeRoom extends StructureTFComponentOld {
	public ComponentTFFinalCastleDungeonForgeRoom() {
	}

	public ComponentTFFinalCastleDungeonForgeRoom(TFFeature feature, Random rand, int i, int x, int y, int z, EnumFacing direction) {
		super(feature, i);
		this.spawnListIndex = 3; // forge monsters
		this.setCoordBaseMode(direction);
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox2(x, y, z, -15, 0, -15, 50, 30, 50, direction);
	}


	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		this.fillWithAir(world, sbb, 0, 0, 0, 50, 30, 50);

		// sign
		this.placeSignAtCurrentPosition(world, 25, 0, 25, "Mini-boss 2", "Gives talisman", sbb);

		return true;
	}

}
