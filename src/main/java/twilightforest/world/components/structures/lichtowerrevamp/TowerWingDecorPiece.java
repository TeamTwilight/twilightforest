package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
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
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.TFStructureHelper;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

public class TowerWingDecorPiece extends TwilightJigsawPiece implements PieceBeardifierModifier {
	private final boolean isInCenterTower;

	public TowerWingDecorPiece(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.TOWER_DECOR.value(), compoundTag, ctx, readSettings(compoundTag));

		TowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(TowerUtil.ROOM_SPAWNERS));
		this.isInCenterTower = compoundTag.getBoolean("is_in_central");
	}

	public TowerWingDecorPiece(int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, JigsawPlaceContext jigsawContext, boolean isInCenterTower) {
		super(TFStructurePieceTypes.TOWER_DECOR.value(), genDepth, structureManager, templateLocation, jigsawContext);

		TowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(TowerUtil.ROOM_SPAWNERS));
		this.isInCenterTower = isInCenterTower;
	}

	public static void addDecor(TwilightTemplateStructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int newDepth, StructureTemplateManager structureManager, boolean isInCenterTower) {
		ResourceLocation decorId = TowerUtil.rollRandomDecor(random, false);
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(parent.templatePosition(), connection.pos(), connection.orientation(), structureManager, decorId, "twilightforest:lich_tower/decor", random);

		if (placeableJunction != null) {
			StructurePiece decor = new TowerWingDecorPiece(newDepth, structureManager, decorId, placeableJunction, isInCenterTower);
			pieceAccessor.addPiece(decor);
			decor.addChildren(parent, pieceAccessor, random);
		}
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.putBoolean("is_in_central", this.isInCenterTower);
	}

	@Override
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen) {
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

		switch (label) {
			case "sapling" -> {
				level.setBlock(pos, TFStructureHelper.randomPlant(random), 2);
			}
			case "tree" -> {
				ResourceKey<ConfiguredFeature<?, ?>> randomTree = TFStructureHelper.randomTree(random.nextInt(4));
				Registry<ConfiguredFeature<?, ?>> featureRegistry = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
				if (!featureRegistry.get(randomTree).place(level, chunkGen, random, pos)) {
					level.setBlock(pos, TFStructureHelper.randomPlant(random), 2);
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
