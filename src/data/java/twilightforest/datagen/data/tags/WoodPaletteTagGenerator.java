package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.tags.TFWoodPaletteTags;
import twilightforest.util.woods.WoodPalette;

import java.util.concurrent.CompletableFuture;

public class WoodPaletteTagGenerator extends TagsProvider<@NotNull WoodPalette> {

	public WoodPaletteTagGenerator(PackOutput output, CompletableFuture< HolderLookup.Provider> provider) {
		super(output, TFRegistries.Keys.WOOD_PALETTES, provider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		getOrCreateRawBuilder(TFWoodPaletteTags.WELL_SWIZZLE_MASK).addElement(WoodPalettes.OAK.identifier());
		getOrCreateRawBuilder(TFWoodPaletteTags.DRUID_HUT_SWIZZLE_MASK).addElement(WoodPalettes.OAK.identifier()).addElement(WoodPalettes.SPRUCE.identifier()).addElement(WoodPalettes.BIRCH.identifier());

		getOrCreateRawBuilder(TFWoodPaletteTags.COMMON_PALETTES).addElement(WoodPalettes.SPRUCE.identifier()).addElement(WoodPalettes.CANOPY.identifier());
		getOrCreateRawBuilder(TFWoodPaletteTags.UNCOMMON_PALETTES).addElement(WoodPalettes.OAK.identifier()).addElement(WoodPalettes.DARKWOOD.identifier()).addElement(WoodPalettes.TWILIGHT_OAK.identifier());
		getOrCreateRawBuilder(TFWoodPaletteTags.RARE_PALETTES).addElement(WoodPalettes.BIRCH.identifier()).addElement(WoodPalettes.JUNGLE.identifier()).addElement(WoodPalettes.MANGROVE.identifier());
		getOrCreateRawBuilder(TFWoodPaletteTags.TREASURE_PALETTES).addElement(WoodPalettes.TIMEWOOD.identifier()).addElement(WoodPalettes.TRANSWOOD.identifier()).addElement(WoodPalettes.MINEWOOD.identifier()).addElement(WoodPalettes.SORTWOOD.identifier());
	}

	@Override
	public String getName() {
		return "Twilight Forest Wood Palette Tags";
	}
}
