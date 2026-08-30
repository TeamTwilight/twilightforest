package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.tags.TFWoodPaletteTags;
import twilightforest.util.woods.WoodPalette;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WoodPaletteTagGenerator extends KeyTagProvider<WoodPalette> {

	public WoodPaletteTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, TFRegistries.Keys.WOOD_PALETTES, provider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(TFWoodPaletteTags.WELL_SWIZZLE_MASK).add(WoodPalettes.OAK);
		tag(TFWoodPaletteTags.DRUID_HUT_SWIZZLE_MASK).addAll(List.of(WoodPalettes.OAK, WoodPalettes.SPRUCE, WoodPalettes.BIRCH));

		tag(TFWoodPaletteTags.COMMON_PALETTES).addAll(List.of(WoodPalettes.SPRUCE, WoodPalettes.CANOPY));
		tag(TFWoodPaletteTags.UNCOMMON_PALETTES).addAll(List.of(WoodPalettes.OAK, WoodPalettes.DARKWOOD, WoodPalettes.TWILIGHT_OAK));
		tag(TFWoodPaletteTags.RARE_PALETTES).addAll(List.of(WoodPalettes.BIRCH, WoodPalettes.JUNGLE, WoodPalettes.MANGROVE));
		tag(TFWoodPaletteTags.TREASURE_PALETTES).addAll(List.of(WoodPalettes.TIMEWOOD, WoodPalettes.TRANSWOOD, WoodPalettes.MINEWOOD, WoodPalettes.SORTWOOD));
	}

	@Override
	public String getName() {
		return "Twilight Forest Wood Palette Tags";
	}
}
