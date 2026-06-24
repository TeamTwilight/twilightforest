package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBannerPatterns;
import twilightforest.tags.TFBannerPatternTags;

import java.util.concurrent.CompletableFuture;

public class BannerPatternTagGenerator extends TagsProvider<@NotNull BannerPattern> {

	public BannerPatternTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.BANNER_PATTERN, provider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.getOrCreateRawBuilder(TFBannerPatternTags.NAGA_BANNER_PATTERN).addElement(TFBannerPatterns.NAGA.identifier());
		this.getOrCreateRawBuilder(TFBannerPatternTags.LICH_BANNER_PATTERN).addElement(TFBannerPatterns.LICH.identifier());
		this.getOrCreateRawBuilder(TFBannerPatternTags.MINOSHROOM_BANNER_PATTERN).addElement(TFBannerPatterns.MINOSHROOM.identifier());
		this.getOrCreateRawBuilder(TFBannerPatternTags.HYDRA_BANNER_PATTERN).addElement(TFBannerPatterns.HYDRA.identifier());
		this.getOrCreateRawBuilder(TFBannerPatternTags.KNIGHT_PHANTOM_BANNER_PATTERN).addElement(TFBannerPatterns.KNIGHT_PHANTOM.identifier());
		this.getOrCreateRawBuilder(TFBannerPatternTags.UR_GHAST_BANNER_PATTERN).addElement(TFBannerPatterns.UR_GHAST.identifier());
		this.getOrCreateRawBuilder(TFBannerPatternTags.ALPHA_YETI_BANNER_PATTERN).addElement(TFBannerPatterns.ALPHA_YETI.identifier());
		this.getOrCreateRawBuilder(TFBannerPatternTags.SNOW_QUEEN_BANNER_PATTERN).addElement(TFBannerPatterns.SNOW_QUEEN.identifier());
		this.getOrCreateRawBuilder(TFBannerPatternTags.QUESTING_RAM_BANNER_PATTERN).addElement(TFBannerPatterns.QUESTING_RAM.identifier());
	}

	@Override
	public String getName() {
		return "Twilight Forest Banner Pattern Tags";
	}
}
