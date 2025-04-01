package twilightforest.datagen.assets.models;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class TFTextureMapping {

	public static TextureMapping twoLayerBlock(Block block) {
		return TextureMapping.cube(block)
			.put(TFTextureSlot.ALL_2, TextureMapping.getBlockTexture(block, "_layer_1"));
	}

	public static TextureMapping threeLayerBlock(Block block) {
		return TextureMapping.cube(block)
			.put(TFTextureSlot.ALL_2, TextureMapping.getBlockTexture(block, "_layer_1"))
			.put(TFTextureSlot.ALL_3, TextureMapping.getBlockTexture(block, "_layer_2"));
	}

	public static TextureMapping ctmBlock(Block block) {
		var overlay = TextureMapping.getBlockTexture(block);
		return ctmBlock(null, overlay);
	}

	public static TextureMapping forcefield(Block block) {
		var tex = TextureMapping.getBlockTexture(block);
		return new TextureMapping().put(TextureSlot.PANE, tex).put(TextureSlot.PARTICLE, tex);
	}

	public static TextureMapping ctmBlock(@Nullable ResourceLocation base, ResourceLocation overlay) {
		TextureMapping mapping = new TextureMapping();
		if (base != null) {
			mapping = mapping.put(TFTextureSlot.CTM_BASE, base).put(TextureSlot.PARTICLE, base);
		} else {
			mapping = mapping.put(TextureSlot.PARTICLE, overlay);
		}
		return mapping.put(TFTextureSlot.CTM_OVERLAY, overlay).put(TFTextureSlot.CTM_OVERLAY_CONNECTED, overlay.withSuffix("_ctm"));
	}
}
