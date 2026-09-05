package twilightforest.datagen.assets.models;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;
import twilightforest.TwilightForestMod;
import twilightforest.enums.BossVariant;
import twilightforest.init.TFBlocks;

public class TFTextureMapping {

	public static TextureMapping twoLayerBlock(Block block, String suffix) {
		return TextureMapping.cube(TextureMapping.getBlockTexture(block, suffix))
			.put(TFTextureSlot.ALL_2, TextureMapping.getBlockTexture(block, suffix + "_layer_1"));
	}

	public static TextureMapping threeLayerBlock(Block block, String suffix) {
		return twoLayerBlock(block, suffix)
			.put(TFTextureSlot.ALL_3, TextureMapping.getBlockTexture(block, suffix + "_layer_2"));
	}

	public static TextureMapping threeLayerDevice(Block block, Block topBlock, String suffix) {
		return new TextureMapping()
			.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, suffix))
			.put(TextureSlot.TOP, TextureMapping.getBlockTexture(topBlock, suffix + "_top"))
			.put(TFTextureSlot.TOP_2, TextureMapping.getBlockTexture(topBlock, suffix + "_top_layer_1"))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
			.put(TFTextureSlot.SIDE_2, TextureMapping.getBlockTexture(block, "_layer_1"))
			.put(TFTextureSlot.SIDE_3, TextureMapping.getBlockTexture(block, suffix + "_layer_2"));
	}

	public static TextureMapping threeLayerDeviceOn(Block block, Block topBlock) {
		return threeLayerDevice(block, topBlock, "_on")
			.put(TFTextureSlot.TOP_3, TextureMapping.getBlockTexture(topBlock, "_on_top_layer_2"));
	}

	public static TextureMapping uncraftingTable(Block block) {
		return new TextureMapping()
			.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block))
			.put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
			.put(TFTextureSlot.TOP_2, TextureMapping.getBlockTexture(block, "_top_glow"))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.JUNGLE_PLANKS))
			.copyForced(TextureSlot.BOTTOM, TextureSlot.PARTICLE);
	}

	public static TextureMapping uncraftingTableOn(Block block) {
		return uncraftingTable(block).put(TFTextureSlot.SIDE_2, TextureMapping.getBlockTexture(block, "_glow"));
	}

	public static TextureMapping ctmBlock(Block block) {
		var overlay = TextureMapping.getBlockTexture(block);
		return ctmBlock(null, overlay.sprite());
	}

	public static TextureMapping forcefield() {
		var tex = new Material(TwilightForestMod.prefix("block/forcefield"));
		return new TextureMapping().put(TextureSlot.PANE, tex).put(TextureSlot.PARTICLE, tex);
	}

	public static TextureMapping giantBlock(Block block) {
		var tex = TextureMapping.getBlockTexture(block);
		return giantBlock(tex, tex);
	}

	public static TextureMapping giantBlock(Material side, Material end) {
		return new TextureMapping()
			.put(TextureSlot.PARTICLE, side)
			.put(TextureSlot.NORTH, side)
			.put(TextureSlot.SOUTH, side)
			.put(TextureSlot.EAST, side)
			.put(TextureSlot.WEST, side)
			.put(TextureSlot.UP, end)
			.put(TextureSlot.DOWN, end);
	}

	public static TextureMapping ctmBlock(@Nullable Identifier base, Identifier overlay) {
		TextureMapping mapping = new TextureMapping();
		if (base != null) {
			mapping = mapping.put(TFTextureSlot.CTM_BASE, new Material(base)).put(TextureSlot.PARTICLE, new Material(base));
		} else {
			mapping = mapping.put(TextureSlot.PARTICLE, new Material(overlay));
		}
		return mapping.put(TFTextureSlot.CTM_OVERLAY, new Material(overlay)).put(TFTextureSlot.CTM_OVERLAY_CONNECTED, new Material(overlay.withSuffix("_ctm")));
	}

	public static TextureMapping sideDoor(Block block) {
		return new TextureMapping()
			.put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_bottom"))
			.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"));
	}

	public static TextureMapping trophyPedestal(Block block, boolean active, BossVariant north, BossVariant south, BossVariant east, BossVariant west) {
		var mapping = new TextureMapping()
			.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_" + north.getSerializedName() + "_latent"))
			.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_" + south.getSerializedName() + "_latent"))
			.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_" + east.getSerializedName() + "_latent"))
			.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_" + west.getSerializedName() + "_latent"));

		if (active) {
			mapping = mapping
				.put(TFTextureSlot.NORTH2, TextureMapping.getBlockTexture(block, "_" + north.getSerializedName() + "_glow"))
				.put(TFTextureSlot.SOUTH2, TextureMapping.getBlockTexture(block, "_" + south.getSerializedName() + "_glow"))
				.put(TFTextureSlot.EAST2, TextureMapping.getBlockTexture(block, "_" + east.getSerializedName() + "_glow"))
				.put(TFTextureSlot.WEST2, TextureMapping.getBlockTexture(block, "_" + west.getSerializedName() + "_glow"))
				.put(TFTextureSlot.NORTH3, TextureMapping.getBlockTexture(block, "_" + north.getSerializedName()))
				.put(TFTextureSlot.SOUTH3, TextureMapping.getBlockTexture(block, "_" + south.getSerializedName()))
				.put(TFTextureSlot.EAST3, TextureMapping.getBlockTexture(block, "_" + east.getSerializedName()))
				.put(TFTextureSlot.WEST3, TextureMapping.getBlockTexture(block, "_" + west.getSerializedName()));
		}
		return mapping;
	}
}
