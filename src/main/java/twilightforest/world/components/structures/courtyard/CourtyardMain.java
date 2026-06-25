package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.util.StructureTemplateDefinitions;

public class CourtyardMain extends StructureMazeGenerator {
	static final int ROW_OF_CELLS = 8;
	static final int RADIUS = (int) ((((ROW_OF_CELLS - 2) / 2.0F) * 12.0F) + 8);

	static final float HEDGE_FLOOF = 0.5f;

	static final float WALL_DECAY = 0.1f;
	static final float WALL_INTEGRITY = 0.95f;

	static final BlockRotProcessor WALL_INTEGRITY_PROCESSOR = new BlockRotProcessor(CourtyardMain.WALL_INTEGRITY);
	static final BlockRotProcessor WALL_DECAY_PROCESSOR = new BlockRotProcessor(CourtyardMain.WALL_DECAY);

	public static final Identifier CENTER_POOL = TwilightForestMod.prefix("courtyard/center");

	@Deprecated // TODO remove in 1.22
	private final boolean placeSpawner;

	public CourtyardMain(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(ctx.structureTemplateManager(), TFStructurePieceTypes.TFNCMn.get(), nbt);

		this.placeSpawner = nbt.getBooleanOr("placeSpawner", true); // For old versions of the courtyard that didn't place the naga spawner template
	}

	@SuppressWarnings("this-escape")
	public CourtyardMain(RandomSource rand, int i, int x, int y, int z, StructureTemplateManager structureManager) {
		super(TFStructurePieceTypes.TFNCMn.get(), rand, i, ROW_OF_CELLS, ROW_OF_CELLS, x, y, z, structureManager);

		this.setOrientation(Direction.NORTH);

		this.boundingBox = BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, -RADIUS / 2, -1, -RADIUS / 2, RADIUS, 10, RADIUS, this.getOrientation(), false);
		this.sizeConstraints = BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, -RADIUS, -1, -RADIUS, RADIUS * 2, 10, RADIUS * 2, this.getOrientation(), false);

		this.placeSpawner = true;
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);

		tagCompound.putBoolean("placeSpawner", this.placeSpawner);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(parent, list, random);

		BlockPos pos = this.getWorldPos(RADIUS / 2, 1, RADIUS / 2).immutable();
		Direction direction = Rotation.getRandom(random).rotate(Direction.SOUTH);
		FrontAndTop oriented = FrontAndTop.fromFrontAndTop(Direction.UP, direction);

		TwilightJigsawPiece bossSpawner = TwilightJigsawPiece.initializeTemplateFromPool(CENTER_POOL, pos.mutable(), oriented, "twilightforest:center", random, this.genDepth + 1, this.structureManager);
		if (bossSpawner != null) {
			list.addPiece(bossSpawner);
			// bossSpawner.addChildren(parent, list, random);
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		if (this.placeSpawner) {
			// TODO remove in 1.22
			placeBlock(world, TFBlocks.NAGA_BOSS_SPAWNER.get().defaultBlockState(), RADIUS / 2, 3, RADIUS / 2, sbb);
		}
	}
}
