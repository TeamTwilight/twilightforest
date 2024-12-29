package twilightforest.world.components.structures.lichtowerrevamp;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import twilightforest.TFRegistries;
import twilightforest.beans.Autowired;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.util.woods.WoodPalette;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.processors.WoodMultiPaletteSwizzle;
import twilightforest.world.components.structures.TwilightJigsawPiece;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LichTowerSpawnerBridge extends TwilightJigsawPiece implements PieceBeardifierModifier {
	@Autowired
	private static LichTowerUtil lichTowerUtil;

	private final boolean invertedPalette;

	public LichTowerSpawnerBridge(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_SPAWNER_BRIDGE.get(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(lichTowerUtil.getCentralBridgeSpawnerProcessor()));

		this.invertedPalette = compoundTag.getBoolean("inverted");

		if (this.invertedPalette) {
			RegistryAccess registryAccess = ctx.registryAccess();
			addInvertedWoodProcessors(registryAccess, this.placeSettings);
		}
	}

	private static void addInvertedWoodProcessors(RegistryAccess registryAccess, StructurePlaceSettings placeSettings) {
		Optional<Registry<WoodPalette>> woodPalettes = registryAccess.registry(TFRegistries.Keys.WOOD_PALETTES);

		if (woodPalettes.isPresent()) {
			Registry<WoodPalette> woodPaletteRegistry = woodPalettes.get();
			Holder<WoodPalette> twiOak = woodPaletteRegistry.getHolderOrThrow(WoodPalettes.TWILIGHT_OAK);
			Holder<WoodPalette> canopy = woodPaletteRegistry.getHolderOrThrow(WoodPalettes.CANOPY);
			placeSettings.addProcessor(new WoodMultiPaletteSwizzle(List.of(
				Pair.of(twiOak, canopy),
				Pair.of(canopy, twiOak)
			)));
		}
	}

	public LichTowerSpawnerBridge(int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, JigsawPlaceContext jigsawContext, boolean invertedPalette) {
		super(TFStructurePieceTypes.LICH_SPAWNER_BRIDGE.get(), genDepth, structureManager, templateLocation, jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(lichTowerUtil.getCentralBridgeSpawnerProcessor()));

		this.invertedPalette = invertedPalette;

		if (this.invertedPalette) {
			RegistryAccess registryAccess = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer()).registryAccess();
			addInvertedWoodProcessors(registryAccess, this.placeSettings);
		}
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.putBoolean("inverted", this.invertedPalette);
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

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
	}
}
