package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import twilightforest.beans.Autowired;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.TFStructureHelper;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

public class LichTowerRoomDecor extends TwilightJigsawPiece implements PieceBeardifierModifier {
	@Autowired
	private static LichTowerUtil lichTowerUtil;

	public LichTowerRoomDecor(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_TOWER_DECOR.value(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(lichTowerUtil.getRoomSpawnerProcessor()));
	}

	public LichTowerRoomDecor(int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, JigsawPlaceContext jigsawContext) {
		super(TFStructurePieceTypes.LICH_TOWER_DECOR.value(), genDepth, structureManager, templateLocation, jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(lichTowerUtil.getRoomSpawnerProcessor()));
	}

	public static void addDecor(TwilightTemplateStructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int newDepth, StructureTemplateManager structureManager) {
		ResourceLocation decorId = lichTowerUtil.rollRandomDecor(random, false);
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(parent.templatePosition(), connection.pos(), connection.orientation(), structureManager, decorId, "twilightforest:lich_tower/decor", random);

		if (placeableJunction != null) {
			StructurePiece decor = new LichTowerRoomDecor(newDepth, structureManager, decorId, placeableJunction);
			pieceAccessor.addPiece(decor);
			decor.addChildren(parent, pieceAccessor, random);
		}
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);
	}

	@Override
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen) {
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);

		switch (label) {
			case "sapling" -> {
				level.setBlock(pos, TFStructureHelper.randomPlant(random), Block.UPDATE_CLIENTS);
			}
			case "tree" -> {
				ResourceKey<ConfiguredFeature<?, ?>> randomTree = TFStructureHelper.randomTree(random.nextInt(4));
				Registry<ConfiguredFeature<?, ?>> featureRegistry = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
				if (!featureRegistry.get(randomTree).place(level, chunkGen, random, pos)) {
					level.setBlock(pos, TFStructureHelper.randomPlant(random), Block.UPDATE_CLIENTS);
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
