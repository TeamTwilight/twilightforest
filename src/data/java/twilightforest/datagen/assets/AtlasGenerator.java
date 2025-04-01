package twilightforest.datagen.assets;

import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;
import twilightforest.TwilightForestMod;
import twilightforest.client.MagicPaintingTextureManager;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.init.custom.MagicPaintingVariants;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AtlasGenerator extends SpriteSourceProvider {

	public AtlasGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, TwilightForestMod.ID);
	}

	@Override
	protected void gather() {
		this.atlas(SHIELD_PATTERNS_ATLAS).addSource(new SingleFile(TwilightForestMod.prefix("entity/knightmetal_shield"), Optional.empty()));
		this.atlas(MagicPaintingTextureManager.ATLAS_INFO_LOCATION).addSource(new SingleFile(MagicPaintingTextureManager.BACK_SPRITE_LOCATION, Optional.empty()));

		MagicPaintingVariants.MAGIC_PAINTING_ATLAS_HELPER.forEach((location, parallaxVariant) -> {
			location = location.withPrefix(MagicPaintingTextureManager.MAGIC_PAINTING_PATH + "/");
			for (MagicPaintingVariant.Layer layer : parallaxVariant.layers()) {
				this.atlas(MagicPaintingTextureManager.ATLAS_INFO_LOCATION).addSource(new SingleFile(location.withSuffix("/" + layer.path()), Optional.empty()));
			}
		});
	}
}
