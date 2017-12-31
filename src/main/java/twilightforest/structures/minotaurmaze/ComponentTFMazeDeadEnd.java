package twilightforest.structures.minotaurmaze;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;

import java.util.Random;

public class ComponentTFMazeDeadEnd extends StructureTFComponentOld {

	public ComponentTFMazeDeadEnd() {
		super();
	}

	public ComponentTFMazeDeadEnd(TFFeature feature, int i, int x, int y, int z, EnumFacing rotation) {
		super(feature, i);
		this.setCoordBaseMode(rotation);
		this.boundingBox = new StructureBoundingBox(x, y, z, x + 5, y + 5, z + 5);

	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		this.fillWithBlocks(world, sbb, 1, 1, 0, 4, 4, 0, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
		this.fillWithAir(world, sbb, 2, 1, 0, 3, 3, 0);
		return true;
	}
}
