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

	public static TextureMapping woodDoorTexture(String doorName) {
		return new TextureMapping()
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/wood/door/" + doorName + "_upper")))
			.put(TextureSlot.BOTTOM, new Material(TwilightForestMod.prefix("block/wood/door/" + doorName + "_lower")));
	}

	public static TextureMapping woodDoorTextureWithSide(String doorName) {
		return woodDoorTexture(doorName)
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/wood/door/" + doorName + "_side")));
	}

	public static TextureMapping woodTrapdoorTexture(String trapdoorName) {
		return new TextureMapping()
			.put(TextureSlot.TEXTURE, new Material(TwilightForestMod.prefix("block/wood/trapdoor/" + trapdoorName + "_trapdoor")));
	}

	// 卡米尼特反应堆（不活跃）
	public static TextureMapping carminiteReactorOff() {
		return TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_reactor_off")))
			.put(TFTextureSlot.ALL_2, new Material(TwilightForestMod.prefix("block/tower_device_level_1/towerdev_reactor_off_1")))
			.put(TFTextureSlot.ALL_3, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_reactor_off_2")));
	}

	// 卡米尼特建造机
	public static TextureMapping carminiteBuilderBlock(String suffix) {
		String baseName = switch (suffix) {
			case "" -> "towerdev_builder_off";
			case "_on" -> "towerdev_builder_on";
			case "_timeout" -> "towerdev_builder_timeout";
			default -> "towerdev_builder_off";
		};
		String level1, level2;
		if ("_timeout".equals(suffix)) {
			level1 = "tower_device_level_2/" + baseName + "_1";
			level2 = "tower_device_level_3/" + baseName + "_2";
		} else {
			level1 = "tower_device_level_1/" + baseName + "_1";
			level2 = "tower_device_level_2/" + baseName + "_2";
		}
		return TextureMapping.cube(new Material(TwilightForestMod.prefix("block/" + baseName)))
			.put(TFTextureSlot.ALL_2, new Material(TwilightForestMod.prefix("block/" + level1)))
			.put(TFTextureSlot.ALL_3, new Material(TwilightForestMod.prefix("block/" + level2)));
	}

	// ===== 三层面板设备 - 地精陷阱 (Ghast Trap) =====
	public static TextureMapping ghastTrapOff() {
		return new TextureMapping()
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/towerdev_ghasttrap_off")))
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/towerdev_ghasttraplid_off")))
			.put(TFTextureSlot.TOP_2, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1")))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
			.put(TFTextureSlot.SIDE_2, new Material(TwilightForestMod.prefix("block/tower_device_level_1/towerdev_ghasttrap_off_1")))
			.put(TFTextureSlot.SIDE_3, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_ghasttrap_off_2")));
	}

	public static TextureMapping ghastTrapOn() {
		return new TextureMapping()
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/towerdev_ghasttrap_on")))
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/towerdev_ghasttraplid_on")))
			.put(TFTextureSlot.TOP_2, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_ghasttraplid_on_1")))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
			.put(TFTextureSlot.SIDE_2, new Material(TwilightForestMod.prefix("block/tower_device_level_1/towerdev_ghasttrap_on_1")))
			.put(TFTextureSlot.SIDE_3, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_ghasttrap_on_2")))
			.put(TFTextureSlot.TOP_3, new Material(TwilightForestMod.prefix("block/tower_device_level_3/towerdev_ghasttraplid_on_2")));
	}

	// ===== Encased Fire Jet =====
	public static TextureMapping encasedFireJetOff() {
		return new TextureMapping()
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/towerdev_firejet_off")))
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/towerdev_ghasttraplid_off")))
			.put(TFTextureSlot.TOP_2, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1")))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
			.put(TFTextureSlot.SIDE_2, new Material(TwilightForestMod.prefix("block/tower_device_level_1/towerdev_firejet_1")))
			.put(TFTextureSlot.SIDE_3, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_firejet_off_1")));
	}

	public static TextureMapping encasedFireJetOn() {
		return new TextureMapping()
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/towerdev_firejet_on")))
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/towerdev_ghasttraplid_on")))
			.put(TFTextureSlot.TOP_2, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_ghasttraplid_on_1")))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
			.put(TFTextureSlot.SIDE_2, new Material(TwilightForestMod.prefix("block/tower_device_level_1/towerdev_firejet_1")))
			.put(TFTextureSlot.SIDE_3, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_firejet_on_1")))
			.put(TFTextureSlot.TOP_3, new Material(TwilightForestMod.prefix("block/tower_device_level_3/towerdev_ghasttraplid_on_2")));
	}

	// ===== Encased Smoker =====
	public static TextureMapping encasedSmokerOff() {
		return new TextureMapping()
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/towerdev_smoker_off")))
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/towerdev_ghasttraplid_off")))
			.put(TFTextureSlot.TOP_2, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1")))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
			.put(TFTextureSlot.SIDE_2, new Material(TwilightForestMod.prefix("block/tower_device_level_1/towerdev_smoker_1")))
			.put(TFTextureSlot.SIDE_3, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_smoker_off_1")));
	}

	public static TextureMapping encasedSmokerOn() {
		return new TextureMapping()
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/towerdev_smoker_on")))
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/towerdev_ghasttraplid_on")))
			.put(TFTextureSlot.TOP_2, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_ghasttraplid_on_1")))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
			.put(TFTextureSlot.SIDE_2, new Material(TwilightForestMod.prefix("block/tower_device_level_1/towerdev_smoker_1")))
			.put(TFTextureSlot.SIDE_3, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_smoker_on_1")))
			.put(TFTextureSlot.TOP_3, new Material(TwilightForestMod.prefix("block/tower_device_level_3/towerdev_ghasttraplid_on_2")));
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
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/uncrafting_side")))
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/uncrafting_top")))
			.put(TFTextureSlot.TOP_2, new Material(TwilightForestMod.prefix("block/uncrafting_glow")))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.JUNGLE_PLANKS));
	}

	public static TextureMapping uncraftingTableOn(Block block) {
		return uncraftingTable(block).put(TFTextureSlot.SIDE_2, new Material(TwilightForestMod.prefix("block/uncrafting_glow_side")));
	}

	public static TextureMapping ctmBlock(Block block) {
		var overlay = TextureMapping.getBlockTexture(block);
		return ctmBlock(null, overlay.sprite());
	}

	public static TextureMapping forcefield(Block block) {
		var tex = TextureMapping.getBlockTexture(block);
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

	public static TextureMapping towerDeviceThreeLayer(String deviceName, String suffix) {
		return TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_" + deviceName + suffix)))
			.put(TFTextureSlot.ALL_2, new Material(TwilightForestMod.prefix("block/tower_device_level_1/towerdev_" + deviceName + suffix + "_1")))
			.put(TFTextureSlot.ALL_3, new Material(TwilightForestMod.prefix("block/tower_device_level_2/towerdev_" + deviceName + suffix + "_2")));
	}

	public static TextureMapping towerDeviceThreeLayerForBuilder(String deviceName, String suffix) {
		String baseName = "towerdev_" + deviceName + suffix;
		String level1, level2;
		if ("_timeout".equals(suffix)) {
			level1 = "tower_device_level_2/" + baseName + "_1";
			level2 = "tower_device_level_3/" + baseName + "_2";
		} else {
			level1 = "tower_device_level_1/" + baseName + "_1";
			level2 = "tower_device_level_2/" + baseName + "_2";
		}
		return TextureMapping.cube(new Material(TwilightForestMod.prefix("block/" + baseName)))
			.put(TFTextureSlot.ALL_2, new Material(TwilightForestMod.prefix("block/" + level1)))
			.put(TFTextureSlot.ALL_3, new Material(TwilightForestMod.prefix("block/" + level2)));
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
		String northName = north.getSerializedName().replace("ur_ghast", "ur-ghast");
		String southName = south.getSerializedName().replace("ur_ghast", "ur-ghast");
		String eastName = east.getSerializedName().replace("ur_ghast", "ur-ghast");
		String westName = west.getSerializedName().replace("ur_ghast", "ur-ghast");

		var mapping = new TextureMapping()
			.put(TextureSlot.NORTH, new Material(TwilightForestMod.prefix("block/pedestal/" + northName + "_latent")))
			.put(TextureSlot.SOUTH, new Material(TwilightForestMod.prefix("block/pedestal/" + southName + "_latent")))
			.put(TextureSlot.EAST, new Material(TwilightForestMod.prefix("block/pedestal/" + eastName + "_latent")))
			.put(TextureSlot.WEST, new Material(TwilightForestMod.prefix("block/pedestal/" + westName + "_latent")));

		if (active) {
			mapping = mapping
				.put(TFTextureSlot.NORTH2, new Material(TwilightForestMod.prefix("block/pedestal/" + northName + "_glow")))
				.put(TFTextureSlot.SOUTH2, new Material(TwilightForestMod.prefix("block/pedestal/" + southName + "_glow")))
				.put(TFTextureSlot.EAST2, new Material(TwilightForestMod.prefix("block/pedestal/" + eastName + "_glow")))
				.put(TFTextureSlot.WEST2, new Material(TwilightForestMod.prefix("block/pedestal/" + westName + "_glow")))
				.put(TFTextureSlot.NORTH3, new Material(TwilightForestMod.prefix("block/pedestal/" + northName)))
				.put(TFTextureSlot.SOUTH3, new Material(TwilightForestMod.prefix("block/pedestal/" + southName)))
				.put(TFTextureSlot.EAST3, new Material(TwilightForestMod.prefix("block/pedestal/" + eastName)))
				.put(TFTextureSlot.WEST3, new Material(TwilightForestMod.prefix("block/pedestal/" + westName)));
		}
		return mapping;
	}
}
