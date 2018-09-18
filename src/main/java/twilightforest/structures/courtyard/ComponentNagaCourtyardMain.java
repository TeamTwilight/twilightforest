package twilightforest.structures.courtyard;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;

import java.util.Random;

public class ComponentNagaCourtyardMain extends StructureMazeGenerator {
	static int ROW_OF_CELLS = 8;
	static int RADIUS = (int) ((((ROW_OF_CELLS - 2) / 2.0F) * 12.0F) + 8);
	static int DIAMETER = 2 * RADIUS + 1;

	static final float HEDGE_FLOOF = 0.5f;

	static final float WALL_DECAY = 0.1f;
	static final float WALL_INTEGRITY = 0.9f;

	@SuppressWarnings("unused")
	public ComponentNagaCourtyardMain() {
		super();
	}

	public ComponentNagaCourtyardMain(TFFeature feature, World world, Random rand, int i, int x, int y, int z) {
		super(feature, rand, i, ROW_OF_CELLS, ROW_OF_CELLS);

		this.setCoordBaseMode(EnumFacing.NORTH);

		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox(x, y, z, -RADIUS, -1, -RADIUS, RADIUS * 2, 10, RADIUS * 2, this.getCoordBaseMode());
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// naga spawner seems important
		setBlockState(world, TFBlocks.boss_spawner.getDefaultState(), RADIUS, 2, RADIUS, sbb);

		return true;
	}
}
