package twilightforest.datagen.assets;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;
import twilightforest.TwilightForestMod;
import twilightforest.client.MagicPaintingAtlasInfo;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.init.TFMaterialAssetGroup;
import twilightforest.init.custom.MagicPaintingVariants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AtlasGenerator extends SpriteSourceProvider {

	public AtlasGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, TwilightForestMod.ID);
	}

	@Override
	protected void gather() {
		atlas(AtlasIds.SHIELD_PATTERNS).addSource(new SingleFile(TwilightForestMod.prefix("entity/knightmetal_shield"), Optional.of(TwilightForestMod.prefix("entity/shield/knightmetal_shield"))));
		atlas(MagicPaintingAtlasInfo.ATLAS_INFO_LOCATION).addSource(new SingleFile(MagicPaintingAtlasInfo.BACK_SPRITE_LOCATION, Optional.empty()));

		MagicPaintingVariants.MAGIC_PAINTING_ATLAS_HELPER.forEach((location, parallaxVariant) -> {
			location = location.withPrefix(MagicPaintingAtlasInfo.MAGIC_PAINTING_PATH + "/");
			for (MagicPaintingVariant.Layer layer : parallaxVariant.layers()) {
				atlas(MagicPaintingAtlasInfo.ATLAS_INFO_LOCATION).addSource(new SingleFile(location.withSuffix("/" + layer.path()), Optional.empty()));
			}
		});

		Identifier trimPalette = Identifier.withDefaultNamespace("trims/color_palettes/trim_palette");
		Map<String, Identifier> trimPermutations = trimPermutations();
		atlas(AtlasIds.ARMOR_TRIMS).addSource(new PalettedPermutations(armorTrimTextures(), trimPalette, trimPermutations));
		atlas(AtlasIds.ITEMS).addSource(new PalettedPermutations(
			List.of(
				ItemModelGenerators.TRIM_PREFIX_HELMET,
				ItemModelGenerators.TRIM_PREFIX_CHESTPLATE,
				ItemModelGenerators.TRIM_PREFIX_LEGGINGS,
				ItemModelGenerators.TRIM_PREFIX_BOOTS
			),
			trimPalette,
			trimPermutations
		));
	}

	private Map<String, Identifier> trimPermutations() {
		return ImmutableMap.ofEntries(
			trimPermutation(TFMaterialAssetGroup.IRONWOOD, "ironwood"),
			trimPermutation(TFMaterialAssetGroup.STEELEAF, "steeleaf"),
			trimPermutation(TFMaterialAssetGroup.KNIGHTMETAL, "knightmetal"),
			trimPermutation(TFMaterialAssetGroup.FIERY, "fiery"),
			trimPermutation(TFMaterialAssetGroup.NAGA_SCALE, "naga_scale"),
			trimPermutation(TFMaterialAssetGroup.CARMINITE, "carminite")
		);
	}

	private Map.Entry<String, Identifier> trimPermutation(MaterialAssetGroup group, String palette) {
		return Map.entry(group.base().suffix(), TwilightForestMod.prefix("trims/color_palettes/" + palette));
	}

	private List<Identifier> armorTrimTextures() {
		List<ResourceKey<TrimPattern>> patterns = List.of(
			TrimPatterns.SENTRY,
			TrimPatterns.DUNE,
			TrimPatterns.COAST,
			TrimPatterns.WILD,
			TrimPatterns.WARD,
			TrimPatterns.EYE,
			TrimPatterns.VEX,
			TrimPatterns.TIDE,
			TrimPatterns.SNOUT,
			TrimPatterns.RIB,
			TrimPatterns.SPIRE,
			TrimPatterns.WAYFINDER,
			TrimPatterns.SHAPER,
			TrimPatterns.SILENCE,
			TrimPatterns.RAISER,
			TrimPatterns.HOST,
			TrimPatterns.FLOW,
			TrimPatterns.BOLT
		);
		List<EquipmentClientInfo.LayerType> layers = List.of(EquipmentClientInfo.LayerType.HUMANOID, EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS);

		List<Identifier> textures = new ArrayList<>();
		for (ResourceKey<TrimPattern> pattern : patterns) {
			Identifier assetId = TrimPatterns.defaultAssetId(pattern);
			for (EquipmentClientInfo.LayerType layer : layers) {
				textures.add(assetId.withPath(path -> layer.trimAssetPrefix() + "/" + path));
			}
		}
		return textures;
	}
}
