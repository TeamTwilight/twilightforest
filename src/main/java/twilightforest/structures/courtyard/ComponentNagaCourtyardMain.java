package twilightforest.structures.courtyard;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;

import java.util.Random;

public class ComponentNagaCourtyardMain extends StructureMazeGenerator {
	static int ROW_OF_CELLS = 8;
	static int RADIUS = (int) ((((ROW_OF_CELLS - 2) / 2.0F) * 12.0F) + 8);
	static int DIAMETER = 2 * RADIUS + 1; //TODO: Unused

	static final float HEDGE_FLOOF = 0.5f;

	static final float WALL_DECAY = 0.1f;
	static final float WALL_INTEGRITY = 0.9f;

	public ComponentNagaCourtyardMain(TemplateManager manager, CompoundNBT nbt) {
		super(NagaCourtyardPieces.TFNCMn, nbt);
	}

	public ComponentNagaCourtyardMain(TFFeature feature, Random rand, int i, int x, int y, int z) {
		super(NagaCourtyardPieces.TFNCMn, feature, rand, i, ROW_OF_CELLS, ROW_OF_CELLS);

		this.setCoordBaseMode(Direction.NORTH);

		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox(x, y, z, -RADIUS, -1, -RADIUS, RADIUS * 2, 10, RADIUS * 2, this.getCoordBaseMode());
	}

	@Override
	public boolean generate(IWorld world, ChunkGenerator<?> generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn) {
		// naga spawner seems important
		setBlockState(world, TFBlocks.boss_spawner.get().getDefaultState(), RADIUS, 2, RADIUS, sbb);

		return true;
	}
}
