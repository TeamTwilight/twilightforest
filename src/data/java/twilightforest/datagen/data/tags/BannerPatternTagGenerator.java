package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBannerPatterns;
import twilightforest.tags.TFBannerPatternTags;

import java.util.concurrent.CompletableFuture;

public class BannerPatternTagGenerator extends TagsProvider<BannerPattern> {

	public BannerPatternTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.BANNER_PATTERN, provider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFBannerPatternTags.NAGA_BANNER_PATTERN).add(TFBannerPatterns.NAGA);
		this.tag(TFBannerPatternTags.LICH_BANNER_PATTERN).add(TFBannerPatterns.LICH);
		this.tag(TFBannerPatternTags.MINOSHROOM_BANNER_PATTERN).add(TFBannerPatterns.MINOSHROOM);
		this.tag(TFBannerPatternTags.HYDRA_BANNER_PATTERN).add(TFBannerPatterns.HYDRA);
		this.tag(TFBannerPatternTags.KNIGHT_PHANTOM_BANNER_PATTERN).add(TFBannerPatterns.KNIGHT_PHANTOM);
		this.tag(TFBannerPatternTags.UR_GHAST_BANNER_PATTERN).add(TFBannerPatterns.UR_GHAST);
		this.tag(TFBannerPatternTags.ALPHA_YETI_BANNER_PATTERN).add(TFBannerPatterns.ALPHA_YETI);
		this.tag(TFBannerPatternTags.SNOW_QUEEN_BANNER_PATTERN).add(TFBannerPatterns.SNOW_QUEEN);
		this.tag(TFBannerPatternTags.QUESTING_RAM_BANNER_PATTERN).add(TFBannerPatterns.QUESTING_RAM);
	}

	@Override
	public String getName() {
		return "Twilight Forest Banner Pattern Tags";
	}
}
