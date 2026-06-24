package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;

public class LichYardLights extends StructurePiece implements PieceBeardifierModifier {
	private final Direction.Axis placeAxis;

	public LichYardLights(BoundingBox boundingBox, Direction.Axis placeAxis) {
		super(TFStructurePieceTypes.LICH_YARD_LIGHTS.value(), 0, boundingBox);

		this.placeAxis = placeAxis;
	}

	public LichYardLights(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(TFStructurePieceTypes.LICH_YARD_LIGHTS.value(), tag);
		this.placeAxis = tag.contains("axis") ? Direction.Axis.values()[tag.getInt("axis")] : Direction.Axis.Y;
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putInt("axis", this.placeAxis.ordinal());
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureCenterPos) {
		BoundingBox boxIntersection = BoundingBoxUtils.getIntersectionOfSBBs(this.boundingBox, chunkBounds.inflatedBy(-1));

		if (boxIntersection == null)
			return;

		for (int z = boxIntersection.minZ(); z <= boxIntersection.maxZ(); z++) {
			for (int x = boxIntersection.minX(); x <= boxIntersection.maxX(); x++) {
				int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z);
				BlockPos placeAt = new BlockPos(x, y, z);

				// Check for specifically normal air so that cave air is avoided
				if (level.getBlockState(placeAt).is(Blocks.AIR) && random.nextFloat() <= 0.125f && level.getBlockState(placeAt.north()).is(Blocks.AIR) && level.getBlockState(placeAt.south()).is(Blocks.AIR) && level.getBlockState(placeAt.east()).is(Blocks.AIR) && level.getBlockState(placeAt.west()).is(Blocks.AIR)) {
					if (this.placeAxis == Direction.Axis.Z ? (Math.min(x - this.boundingBox.minX(), this.boundingBox.maxX() - x) < 3) : (Math.min(z - this.boundingBox.minZ(), this.boundingBox.maxZ() - z) < 3)) {
						level.setBlock(placeAt, TFBlocks.WROUGHT_IRON_FENCE.value().defaultBlockState(), Block.UPDATE_ALL);
						level.setBlock(placeAt.above(), Blocks.CANDLE.defaultBlockState().setValue(CandleBlock.LIT, true), Block.UPDATE_ALL);
					}
				}
			}
		}
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return TerrainAdjustment.NONE;
	}

	@Override
	public int getGroundLevelDelta() {
		return 0;
	}
}
