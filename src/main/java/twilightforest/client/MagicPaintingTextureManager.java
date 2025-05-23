package twilightforest.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.MagicPaintingVariant;

public class MagicPaintingTextureManager extends TextureAtlasHolder {
	public final static String MAGIC_PAINTING_PATH = "magic_paintings";
	public static final ResourceLocation ATLAS_LOCATION = TwilightForestMod.prefix("textures/atlas/magic_paintings.png");
	public static final ResourceLocation ATLAS_INFO_LOCATION = ResourceLocation.withDefaultNamespace(MAGIC_PAINTING_PATH);
	public static final ResourceLocation BACK_SPRITE_LOCATION = TwilightForestMod.prefix(MAGIC_PAINTING_PATH + "/back");

	public static MagicPaintingTextureManager instance;

	public MagicPaintingTextureManager(TextureManager textureManager) {
		super(textureManager, ATLAS_LOCATION, ATLAS_INFO_LOCATION);
	}

	public TextureAtlasSprite getLayerSprite(ResourceLocation variant, MagicPaintingVariant.Layer layer) {
		return this.getSprite(variant.withPrefix(MAGIC_PAINTING_PATH + "/").withSuffix("/" + layer.path()));
	}

	public TextureAtlasSprite getBackSprite(MagicPaintingVariant variant) {
		return this.getSprite(variant.backTexture());
	}
}