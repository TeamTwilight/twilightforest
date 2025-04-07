package twilightforest.datagen.assets.models;

import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.Optional;

public class TFModelTemplates extends ModelTemplates {

	public static final ModelTemplate ANTIBUILDER = create("twilightforest:antibuilder", TextureSlot.ALL, TFTextureSlot.ALL_2, TFTextureSlot.ALL_3).extend()
		.parent(ResourceLocation.withDefaultNamespace("block/cube_all"))
		.element(builder -> builder.allFaces((direction, faceBuilder) -> faceBuilder.texture(TextureSlot.ALL).cullface(direction)))
		.element(builder -> builder.allFaces((direction, faceBuilder) -> faceBuilder.emissivity(15, 15).texture(TFTextureSlot.ALL_2).cullface(direction)))
		.element(builder -> builder.allFaces((direction, faceBuilder) -> faceBuilder.emissivity(10, 10).texture(TFTextureSlot.ALL_3).cullface(direction))).build();
	public static final ModelTemplate ANTIBUILT_BLOCK = create("twilightforest:antibuilt_block", TextureSlot.ALL, TFTextureSlot.ALL_2).extend()
		.parent(ResourceLocation.withDefaultNamespace("block/cube_all"))
		.element(builder -> builder.allFaces((direction, faceBuilder) -> faceBuilder.texture(TextureSlot.ALL).cullface(direction)))
		.element(builder -> builder.allFaces((direction, faceBuilder) -> faceBuilder.emissivity(10, 10).texture(TFTextureSlot.ALL_2).cullface(direction))).build();

	public static final ModelTemplate BISECTED_STAIRS_STRAIGHT = create("twilightforest:util/bisected_stairs", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TFTextureSlot.MIDDLE);
	public static final ModelTemplate BISECTED_STAIRS_INNER = create("twilightforest:util/bisected_inner_stairs", "_inner", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TFTextureSlot.MIDDLE);
	public static final ModelTemplate BISECTED_STAIRS_OUTER = create("twilightforest:util/bisected_outer_stairs", "_outer", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TFTextureSlot.MIDDLE);

	public static final ModelTemplate CUBE_COLUMN_ROTATIONALLY_SPECIAL_X = create("twilightforest:util/cube_column_rotationally_special_x", "_special_x", TextureSlot.END, TFTextureSlot.SIDE_A, TFTextureSlot.SIDE_B);
	public static final ModelTemplate CUBE_COLUMN_ROTATIONALLY_SPECIAL_Z = create("twilightforest:util/cube_column_rotationally_special_z", "_special_z", TextureSlot.END, TFTextureSlot.SIDE_A, TFTextureSlot.SIDE_B);

	public static final ModelTemplate FORCEFIELD = create("twilightforest:forcefield", TextureSlot.PANE, TextureSlot.PARTICLE).extend().parent(ResourceLocation.withDefaultNamespace("block/cube_all")).ambientOcclusion(false).renderType("translucent").build();
	public static final ModelTemplate FULLBRIGHT_BLOCK = create("twilightforest:fullbright_block", TextureSlot.ALL).extend()
		.parent(ResourceLocation.withDefaultNamespace("block/cube"))
		.element(builder -> builder.allFaces((direction, faceBuilder) -> faceBuilder.texture(TextureSlot.ALL).emissivity(15, 15).cullface(direction))).build();

	public static final ModelTemplate CTM_NO_BASE = create("twilightforest:ctm_no_base", TextureSlot.PARTICLE, TFTextureSlot.CTM_OVERLAY, TFTextureSlot.CTM_OVERLAY_CONNECTED).extend().parent(ResourceLocation.withDefaultNamespace("block/block")).build();
	public static final ModelTemplate CTM = create("twilightforest:ctm", TextureSlot.PARTICLE, TFTextureSlot.CTM_BASE, TFTextureSlot.CTM_OVERLAY, TFTextureSlot.CTM_OVERLAY_CONNECTED).extend().parent(ResourceLocation.withDefaultNamespace("block/block")).build();

	public static final ModelTemplate BANISTER_CONNECTED = create("twilightforest:banister_connected", "_connected", TextureSlot.TEXTURE);
	public static final ModelTemplate BANISTER_CONNECTED_EXTENDED = create("twilightforest:banister_connected_extended", "_connected_extended", TextureSlot.TEXTURE);
	public static final ModelTemplate BANISTER_SHORT = create("twilightforest:banister_short", "_short", TextureSlot.TEXTURE);
	public static final ModelTemplate BANISTER_SHORT_EXTENDED = create("twilightforest:banister_short_extended", "_short_extended", TextureSlot.TEXTURE);
	public static final ModelTemplate BANISTER_TALL = create("twilightforest:banister_tall", "_tall", TextureSlot.TEXTURE);
	public static final ModelTemplate BANISTER_TALL_EXTENDED = create("twilightforest:banister_tall_extended", "_tall_extended", TextureSlot.TEXTURE);
	public static final ModelTemplate BANISTER_INVENTORY = createItem("twilightforest:banister_inventory", "_inventory", TextureSlot.TEXTURE);

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

	public static final ModelTemplate THORNS_MAIN = create("twilightforest:thorns_main", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_main")).renderType("cutout").build();
	public static final ModelTemplate THORNS = create("twilightforest:thorns", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns")).renderType("cutout").build();
	public static final ModelTemplate THORNS_SECTION_TOP = create("twilightforest:thorns_section_top", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_section_top")).renderType("cutout").build();
	public static final ModelTemplate THORNS_SECTION_BOTTOM = create("twilightforest:thorns_section_bottom", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_section_bottom")).renderType("cutout").build();
	public static final ModelTemplate THORNS_NO_SECTION = create("twilightforest:thorns_no_section", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_no_section")).renderType("cutout").build();
	public static final ModelTemplate THORNS_NO_SECTION_ALT = create("twilightforest:thorns_no_section_alt", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_no_section_alt")).renderType("cutout").build();

	public static final ModelTemplate CASTLE_RUNE_TEMPLATE = create("twilightforest:castle_rune_template", TextureSlot.ALL, TFTextureSlot.RUNE);
	public static final ModelTemplate TINTED_CUBE_BOTTOM_TOP = create("twilightforest:tinted_cube_bottom_top", TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);

	public static final ModelTemplate GIANT_TOOL = createItem("twilightforest:giant_tool_base", TextureSlot.LAYER0);
	public static final ModelTemplate MOON_DIAL = createItem("twilightforest:moon_dial_template", TextureSlot.LAYER0);
	public static final ModelTemplate SPECIAL_HANDHELD = createItem("twilightforest:special_handheld", TextureSlot.LAYER0);
	public static final ModelTemplate TWO_LAYERED_HANDHELD = createItem("handheld", TextureSlot.LAYER0, TextureSlot.LAYER1);
	public static final ModelTemplate TWO_LAYERED_BOW = createItem("bow", TextureSlot.LAYER0, TextureSlot.LAYER1);
}
