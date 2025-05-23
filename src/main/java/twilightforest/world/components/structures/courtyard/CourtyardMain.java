package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;

public class CourtyardMain extends StructureMazeGenerator {
	static final int ROW_OF_CELLS = 8;
	static final int RADIUS = (int) ((((ROW_OF_CELLS - 2) / 2.0F) * 12.0F) + 8);

	static final float HEDGE_FLOOF = 0.5f;

	static final float WALL_DECAY = 0.1f;
	static final float WALL_INTEGRITY = 0.95f;

	static final BlockRotProcessor WALL_INTEGRITY_PROCESSOR = new BlockRotProcessor(CourtyardMain.WALL_INTEGRITY);
	static final BlockRotProcessor WALL_DECAY_PROCESSOR = new BlockRotProcessor(CourtyardMain.WALL_DECAY);

	public CourtyardMain(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(ctx.structureTemplateManager(), TFStructurePieceTypes.TFNCMn.get(), nbt);
	}

	public CourtyardMain(RandomSource rand, int i, int x, int y, int z, StructureTemplateManager structureManager) {
		super(TFStructurePieceTypes.TFNCMn.get(), rand, i, ROW_OF_CELLS, ROW_OF_CELLS, x, y, z, structureManager);

		this.setOrientation(Direction.NORTH);

		this.boundingBox = BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, -RADIUS / 2, -1, -RADIUS / 2, RADIUS, 10, RADIUS, this.getOrientation(), false);
		this.sizeConstraints = BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, -RADIUS, -1, -RADIUS, RADIUS * 2, 10, RADIUS * 2, this.getOrientation(), false);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// naga spawner seems important
		placeBlock(world, TFBlocks.NAGA_BOSS_SPAWNER.get().defaultBlockState(), RADIUS / 2, 3, RADIUS / 2, sbb);
	}
}
