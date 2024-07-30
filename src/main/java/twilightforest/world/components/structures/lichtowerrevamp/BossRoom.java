package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.CustomTagGenerator;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.DirectionUtil;
import twilightforest.util.EntityUtil;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.TwilightJigsawPiece;

import java.util.List;

public final class BossRoom extends TwilightJigsawPiece implements PieceBeardifierModifier {
	public BossRoom(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.TOWER_BOSS_ROOM.get(), compoundTag, ctx, readSettings(compoundTag));

		TowerUtil.addDefaultProcessors(this.placeSettings);
	}

	public BossRoom(StructureTemplateManager structureManager, JigsawPlaceContext jigsawContext) {
		super(TFStructurePieceTypes.TOWER_BOSS_ROOM.get(), 1, structureManager, TwilightForestMod.prefix("lich_tower/tower_boss_room"), jigsawContext);

		TowerUtil.addDefaultProcessors(this.placeSettings);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureCenterPos) {
		super.postProcess(level, structureManager, chunkGen, random, chunkBounds, chunkPos, structureCenterPos);

		BlockPos center = this.getSourceJigsaw().pos().offset(this.templatePosition).above(9);
		long aLong = center.asLong();
		random.setSeed(aLong);
		BlockState candle = Blocks.CANDLE.defaultBlockState().setValue(CandleBlock.LIT, true);
		float angle = 0;
		for (int i = 0; i < 20; i++) {
			angle += random.nextFloat(); // Incrementing angle each iteration ensures candles among all 4 sides of the room
			float range = Mth.clampedLerp(7, 11, random.nextFloat());
			int x = Math.round(Mth.cos(angle) * range);
			int z = Math.round(Mth.sin(angle) * range);
			int y = random.nextInt(3);
			BlockPos placeAt = center.offset(x, y, z);

			if (chunkBounds.isInside(placeAt) && level.getBlockState(placeAt).isAir()) {
				level.setBlock(placeAt, candle, 2);
			}
		}
	}

	@Override
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen) {
		placePainting(label, pos, level, random, chunkBounds, this.placeSettings.getRotation(), 3, 3, CustomTagGenerator.PaintingVariantTagGenerator.LICH_BOSS_PAINTINGS);
	}

	public static void placePainting(String label, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBounds, Rotation rotation, int limitTries, int rarityFactor, TagKey<PaintingVariant> lichTowerPaintings) {
		if (!chunkBounds.isInside(pos) || random.nextInt(rarityFactor) != 0)
			return;

		String[] params = label.split(":");

		if (params.length != 3)
			return;

		@Nullable
		Direction dir = DirectionUtil.fromStringOrElse(params[0], null);

		if (dir == null || dir.getAxis().isVertical() || !StringUtils.isNumeric(params[1]) || !StringUtils.isNumeric(params[2]) || !(level instanceof WorldGenLevel genLevel))
			return;

		dir = rotation.rotate(dir);

		int width = Integer.parseInt(params[1]);
		int height = Integer.parseInt(params[2]);
		int maxArea = width * height;

		List<Holder<PaintingVariant>> paintingsOfSizeOrSmaller = EntityUtil.getPaintingsOfSizeOrSmaller(genLevel, lichTowerPaintings, width, height);
		Util.shuffle(paintingsOfSizeOrSmaller, random);
		for (Holder<PaintingVariant> paintingHolder : paintingsOfSizeOrSmaller) {
			PaintingVariant painting = paintingHolder.value();
			int area = painting.width() * painting.height();
			if (random.nextInt(maxArea) <= area) {
				if (EntityUtil.tryHangPainting(genLevel, pos, dir, paintingHolder) || limitTries-- <= 0) {
					break;
				}
			}
		}
	}

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
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
