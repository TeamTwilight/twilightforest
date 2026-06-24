package twilightforest.datagen.assets.models;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.block.giantblock.GiantBlockBuilder;

/**
 * Class for using ModelTemplates to reference existing block models.
 */
public class TFModelTemplates extends ModelTemplates {
	public static final ModelTemplate TWO_LAYER_BLOCK = create("twilightforest:util/two_layer_block_15", TextureSlot.ALL, TFTextureSlot.ALL_2);
	public static final ModelTemplate TWO_LAYER_BLOCK_DARKER = create("twilightforest:util/two_layer_block_10", TextureSlot.ALL, TFTextureSlot.ALL_2);
	public static final ModelTemplate THREE_LAYER_BLOCK = create("twilightforest:util/three_layer_block", TextureSlot.ALL, TFTextureSlot.ALL_2, TFTextureSlot.ALL_3);
	public static final ModelTemplate SMALL_CUBE = create("twilightforest:util/small_cube", TextureSlot.ALL);

	public static final ModelTemplate THREE_LAYER_DEVICE = create("twilightforest:util/three_layer_device", TextureSlot.BOTTOM, TextureSlot.TOP, TFTextureSlot.TOP_2, TextureSlot.SIDE, TFTextureSlot.SIDE_2, TFTextureSlot.SIDE_3);
	public static final ModelTemplate THREE_LAYER_DEVICE_ACTIVE = create("twilightforest:util/three_layer_device_active", TextureSlot.BOTTOM, TextureSlot.TOP, TFTextureSlot.TOP_2, TFTextureSlot.TOP_3, TextureSlot.SIDE, TFTextureSlot.SIDE_2, TFTextureSlot.SIDE_3);

	public static final ModelTemplate CUBE_BOTTOM_2_LAYER_TOP = create("twilightforest:util/cube_bottom_2_layer_top", TextureSlot.BOTTOM, TextureSlot.TOP, TFTextureSlot.TOP_2, TextureSlot.SIDE);
	public static final ModelTemplate TWO_LAYER_COLUMN_NO_BOTTOM = create("twilightforest:util/two_layer_column_no_bottom", TextureSlot.BOTTOM, TextureSlot.TOP, TFTextureSlot.TOP_2, TextureSlot.SIDE, TFTextureSlot.SIDE_2);

	public static final ModelTemplate BISECTED_STAIRS_STRAIGHT = create("twilightforest:util/bisected_stairs", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TFTextureSlot.MIDDLE);
	public static final ModelTemplate BISECTED_STAIRS_INNER = create("twilightforest:util/bisected_inner_stairs", "_inner", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TFTextureSlot.MIDDLE);
	public static final ModelTemplate BISECTED_STAIRS_OUTER = create("twilightforest:util/bisected_outer_stairs", "_outer", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TFTextureSlot.MIDDLE);

	public static final ModelTemplate CUBE_COLUMN_ROTATIONALLY_SPECIAL_X = create("twilightforest:util/cube_column_rotationally_special_x", "_special_x", TextureSlot.END, TFTextureSlot.SIDE_A, TFTextureSlot.SIDE_B);
	public static final ModelTemplate CUBE_COLUMN_ROTATIONALLY_SPECIAL_Z = create("twilightforest:util/cube_column_rotationally_special_z", "_special_z", TextureSlot.END, TFTextureSlot.SIDE_A, TFTextureSlot.SIDE_B);

	public static final ModelTemplate FORCEFIELD = create("twilightforest:forcefield", TextureSlot.PANE, TextureSlot.PARTICLE).extend().parent(Identifier.withDefaultNamespace("block/block")).ambientOcclusion(false).build();
	public static final ModelTemplate FULLBRIGHT_BLOCK = create("twilightforest:util/fullbright_cube", TextureSlot.ALL);

	public static final ModelTemplate CTM_NO_BASE = create("minecraft:thin_block", TextureSlot.PARTICLE, TFTextureSlot.CTM_OVERLAY, TFTextureSlot.CTM_OVERLAY_CONNECTED).extend().parent(Identifier.withDefaultNamespace("block/block")).build();
	public static final ModelTemplate CTM = create("minecraft:thin_block", TextureSlot.PARTICLE, TFTextureSlot.CTM_BASE, TFTextureSlot.CTM_OVERLAY, TFTextureSlot.CTM_OVERLAY_CONNECTED).extend().parent(Identifier.withDefaultNamespace("block/block")).build();
	public static final ModelTemplate GIANT_BLOCK = create("twilightforest:giant_block", TextureSlot.PARTICLE, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN).extend().parent(Identifier.withDefaultNamespace("block/block")).customLoader(GiantBlockBuilder::new, builder -> {}).build();

	public static final ModelTemplate BANISTER_CONNECTED = create("twilightforest:banister_connected", "_connected", TextureSlot.TEXTURE);
	public static final ModelTemplate BANISTER_CONNECTED_EXTENDED = create("twilightforest:banister_connected_extended", "_connected_extended", TextureSlot.TEXTURE);
	public static final ModelTemplate BANISTER_SHORT = create("twilightforest:banister_short", "_short", TextureSlot.TEXTURE);
	public static final ModelTemplate BANISTER_SHORT_EXTENDED = create("twilightforest:banister_short_extended", "_short_extended", TextureSlot.TEXTURE);
	public static final ModelTemplate BANISTER_TALL = create("twilightforest:banister_tall", "_tall", TextureSlot.TEXTURE);
	public static final ModelTemplate BANISTER_TALL_EXTENDED = create("twilightforest:banister_tall_extended", "_tall_extended", TextureSlot.TEXTURE);
	public static final ModelTemplate BANISTER_INVENTORY = createItem("twilightforest:banister_item", "_inventory", TextureSlot.TEXTURE);
	public static final ModelTemplate DRYING_RACK = create("twilightforest:template_drying_rack", TextureSlot.TEXTURE);

	public static final ModelTemplate CORRECTED_DOOR_BOTTOM_LEFT = create("twilightforest:util/corrected_door_bottom_left", "_bottom_left", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate CORRECTED_DOOR_BOTTOM_LEFT_OPEN = create("twilightforest:util/corrected_door_bottom_left_open", "_bottom_left_open", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate CORRECTED_DOOR_BOTTOM_RIGHT = create("twilightforest:util/corrected_door_bottom_right", "_bottom_right", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate CORRECTED_DOOR_BOTTOM_RIGHT_OPEN = create("twilightforest:util/corrected_door_bottom_right_open", "_bottom_right_open", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate CORRECTED_DOOR_TOP_LEFT = create("twilightforest:util/corrected_door_top_left", "_top_left", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate CORRECTED_DOOR_TOP_LEFT_OPEN = create("twilightforest:util/corrected_door_top_left_open", "_top_left_open", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate CORRECTED_DOOR_TOP_RIGHT = create("twilightforest:util/corrected_door_top_right", "_top_right", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate CORRECTED_DOOR_TOP_RIGHT_OPEN = create("twilightforest:util/corrected_door_top_right_open", "_top_right_open", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);

	public static final ModelTemplate HORIZONTAL_HOLLOW_LOG = create("twilightforest:horizontal_hollow_log", TextureSlot.SIDE, TextureSlot.END, TextureSlot.INSIDE);
	public static final ModelTemplate HORIZONTAL_HOLLOW_LOG_CARPET = create("twilightforest:horizontal_hollow_log_carpet", TextureSlot.SIDE, TextureSlot.END, TextureSlot.INSIDE, TFTextureSlot.CARPET, TFTextureSlot.OVERHANG);
	public static final ModelTemplate HORIZONTAL_HOLLOW_LOG_PLANT = create("twilightforest:horizontal_hollow_log_plant", "_inventory", TextureSlot.SIDE, TextureSlot.END, TextureSlot.INSIDE, TFTextureSlot.CARPET, TFTextureSlot.OVERHANG, TextureSlot.PLANT);
	public static final ModelTemplate VERTICAL_HOLLOW_LOG = create("twilightforest:vertical_hollow_log", TextureSlot.SIDE, TextureSlot.END, TextureSlot.INSIDE);
	public static final ModelTemplate CLIMBABLE_HOLLOW_LOG = create("twilightforest:climbable_hollow_log", TextureSlot.SIDE, TextureSlot.END, TextureSlot.INSIDE, TFTextureSlot.CLIMBABLE);

	public static final ModelTemplate THORNS_MAIN = create("twilightforest:thorns_main", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_main")).build();
	public static final ModelTemplate THORNS = create("twilightforest:thorns", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns")).build();
	public static final ModelTemplate THORNS_SECTION_TOP = create("twilightforest:thorns_section_top", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_section_top")).build();
	public static final ModelTemplate THORNS_SECTION_BOTTOM = create("twilightforest:thorns_section_bottom", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_section_bottom")).build();
	public static final ModelTemplate THORNS_NO_SECTION = create("twilightforest:thorns_no_section", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_no_section")).build();
	public static final ModelTemplate THORNS_NO_SECTION_ALT = create("twilightforest:thorns_no_section_alt", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_no_section_alt")).build();
	public static final ModelTemplate POTTED_THORN = create("twilightforest:potted_thorn_template", TextureSlot.SIDE, TextureSlot.END);

	public static final ModelTemplate CASTLE_RUNE_TEMPLATE = create("twilightforest:castle_rune_template", TextureSlot.ALL, TFTextureSlot.RUNE);
	public static final ModelTemplate TINTED_CUBE_BOTTOM_TOP = create("twilightforest:tinted_cube_bottom_top", TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);
	public static final ModelTemplate TINTED_BLOCK = create("twilightforest:tinted_block", TextureSlot.ALL);
	public static final ModelTemplate TINTED_SLAB_BOTTOM = create("twilightforest:tinted_slab_bottom", TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);
	public static final ModelTemplate TINTED_SLAB_TOP = create("twilightforest:tinted_slab_top", "_top", TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);
	public static final ModelTemplate TROPHY_PEDESTAL = create("twilightforest:template_trophy_pedestal", TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN, TextureSlot.PARTICLE);

	public static final ModelTemplate GIANT_BLOCK_BASE = createItem("twilightforest:giant_block_base", TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN);
	public static final ModelTemplate GIANT_BLOCK_GUI = createItem("twilightforest:giant_block_gui", TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN);
	public static final ModelTemplate GIANT_TOOL = createItem("twilightforest:giant_tool_base", TextureSlot.LAYER0);
	public static final ModelTemplate MOON_DIAL = createItem("twilightforest:moon_dial_template", TextureSlot.LAYER0);
	public static final ModelTemplate SPECIAL_HANDHELD = createItem("twilightforest:special_handheld", TextureSlot.LAYER0);
	public static final ModelTemplate TWO_LAYERED_HANDHELD = createItem("handheld", TextureSlot.LAYER0, TextureSlot.LAYER1);
	public static final ModelTemplate TWO_LAYERED_BOW = createItem("bow", TextureSlot.LAYER0, TextureSlot.LAYER1);

	public static final ModelTemplate JAR_LID = create("twilightforest:jar_lid", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/jar_lid")).build();

	public static final ModelTemplate SMALL_BUSH = create("twilightforest:util/small_bush", TextureSlot.ALL);
	public static final ModelTemplate MEDIUM_BUSH = create("twilightforest:util/medium_bush", TextureSlot.ALL);
	public static final ModelTemplate LARGE_BUSH = create("twilightforest:util/large_bush", TextureSlot.ALL);
}
