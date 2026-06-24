package twilightforest.datagen.assets.models;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
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

	public static TextureMapping threeLayerDevice(String texture, String suffix) {
		if (suffix.isEmpty()) suffix = "_off";
		return new TextureMapping()
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/towerdev_" + texture + suffix)))
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/towerdev_ghasttraplid_off")))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
			.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(TFBlocks.ENCASED_TOWERWOOD.get()));
	}

	public static TextureMapping threeLayerDeviceOn(String texture) {
		return threeLayerDevice(texture, "_on")
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/towerdev_ghasttraplid_on")));
	}

	public static TextureMapping uncraftingTable() {
		return new TextureMapping()
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/uncrafting_side")))
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/uncrafting_top")))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.JUNGLE_PLANKS))
			.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.JUNGLE_PLANKS));
	}

	public static TextureMapping uncraftingTableOn() {
		return uncraftingTable().put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/uncrafting_particle")));
	}

	public static TextureMapping ctmBlock(Block block) {
		var overlay = TextureMapping.getBlockTexture(block);
		return ctmBlock(null, overlay.sprite());
	}

	public static TextureMapping forcefield(Block forcefield) {
		var tex = TextureMapping.getBlockTexture(forcefield);
		return new TextureMapping().put(TextureSlot.PANE, tex);
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

	public static TextureMapping sideDoor(String type) {
		return new TextureMapping()
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/wood/door/" + type + "_upper")))
			.put(TextureSlot.BOTTOM, new Material(TwilightForestMod.prefix("block/wood/door/" + type + "_lower")))
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/wood/door/" + type + "_side")));
	}

	public static TextureMapping trophyPedestal(boolean active, BossVariant north, BossVariant south, BossVariant east, BossVariant west) {
		var mapping = new TextureMapping()
			.put(TextureSlot.NORTH, new Material(TwilightForestMod.prefix("block/pedestal/" + clean(north.getSerializedName()) + "_latent")))
			.put(TextureSlot.SOUTH, new Material(TwilightForestMod.prefix("block/pedestal/" + clean(south.getSerializedName()) + "_latent")))
			.put(TextureSlot.EAST, new Material(TwilightForestMod.prefix("block/pedestal/" + clean(east.getSerializedName()) + "_latent")))
			.put(TextureSlot.WEST, new Material(TwilightForestMod.prefix("block/pedestal/" + clean(west.getSerializedName()) + "_latent")))
			.put(TextureSlot.UP, new Material(TwilightForestMod.prefix("block/pedestal/top")))
			.put(TextureSlot.DOWN, new Material(TwilightForestMod.prefix("block/pedestal/top")))
			.put(TextureSlot.PARTICLE, new Material(Identifier.withDefaultNamespace("block/stone")));

		if (active) {
			mapping = mapping
				.put(TextureSlot.NORTH, new Material(TwilightForestMod.prefix("block/pedestal/" + clean(north.getSerializedName()) + "_flat")))
				.put(TextureSlot.SOUTH, new Material(TwilightForestMod.prefix("block/pedestal/" + clean(south.getSerializedName()) + "_flat")))
				.put(TextureSlot.EAST, new Material(TwilightForestMod.prefix("block/pedestal/" + clean(east.getSerializedName()) + "_flat")))
				.put(TextureSlot.WEST, new Material(TwilightForestMod.prefix("block/pedestal/" + clean(west.getSerializedName()) + "_flat")))
				.put(TextureSlot.UP, new Material(TwilightForestMod.prefix("block/pedestal/top_flat")));
		}
		return mapping;
	}

	private static String clean(String bossVariant) {
		if (bossVariant.equals("snow_queen")) return bossVariant;
		return bossVariant.replace('_', '-'); // ur_ghast -> ur-ghast
	}

	public static TextureMapping door(String type) {
		return new TextureMapping()
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/wood/door/" + type + "_upper")))
			.put(TextureSlot.BOTTOM, new Material(TwilightForestMod.prefix("block/wood/door/" + type + "_lower")));
	}

	public static TextureMapping crossEmissiveWithCustomSuffix(Block block, String suffix) {
		return (new TextureMapping()).put(TextureSlot.CROSS, TextureMapping.getBlockTexture(block)).put(TextureSlot.CROSS_EMISSIVE, TextureMapping.getBlockTexture(block, suffix));
	}
}
