package twilightforest.datagen.assets.models;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

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

	public static final ModelTemplate CANDELABRA = create("twilightforest:candelabra");
	public static final ModelTemplate WALL_CANDELABRA = create("twilightforest:wall_candelabra");

	public static final ModelTemplate FORCEFIELD = create("twilightforest:forcefield", TextureSlot.PANE, TextureSlot.PARTICLE).extend().parent(ResourceLocation.withDefaultNamespace("block/cube_all")).ambientOcclusion(false).renderType("translucent").build();
	public static final ModelTemplate FULLBRIGHT_BLOCK = create("twilightforest:fullbright_block", TextureSlot.ALL).extend()
		.parent(ResourceLocation.withDefaultNamespace("block/cube"))
		.element(builder -> builder.allFaces((direction, faceBuilder) -> faceBuilder.texture(TextureSlot.ALL).emissivity(15, 15).cullface(direction))).build();

	public static final ModelTemplate CTM_NO_BASE = create("twilightforest:ctm_no_base", TextureSlot.PARTICLE, TFTextureSlot.CTM_OVERLAY, TFTextureSlot.CTM_OVERLAY_CONNECTED).extend().parent(ResourceLocation.withDefaultNamespace("block/block")).build();
	public static final ModelTemplate CTM = create("twilightforest:ctm", TextureSlot.PARTICLE, TFTextureSlot.CTM_BASE, TFTextureSlot.CTM_OVERLAY, TFTextureSlot.CTM_OVERLAY_CONNECTED).extend().parent(ResourceLocation.withDefaultNamespace("block/block")).build();

	public static final ModelTemplate THORNS_MAIN = create("twilightforest:thorns_main", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_main")).renderType("cutout").build();
	public static final ModelTemplate THORNS = create("twilightforest:thorns", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns")).renderType("cutout").build();
	public static final ModelTemplate THORNS_SECTION_TOP = create("twilightforest:thorns_section_top", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_section_top")).renderType("cutout").build();
	public static final ModelTemplate THORNS_SECTION_BOTTOM = create("twilightforest:thorns_section_bottom", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_section_bottom")).renderType("cutout").build();
	public static final ModelTemplate THORNS_NO_SECTION = create("twilightforest:thorns_no_section", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_no_section")).renderType("cutout").build();
	public static final ModelTemplate THORNS_NO_SECTION_ALT = create("twilightforest:thorns_no_section_alt", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_no_section_alt")).renderType("cutout").build();

	public static final ModelTemplate GIANT_TOOL = createItem("twilightforest:giant_tool_base", TextureSlot.LAYER0);
	public static final ModelTemplate MOON_DIAL = createItem("twilightforest:moon_dial_template", TextureSlot.LAYER0);
	public static final ModelTemplate SPECIAL_HANDHELD = createItem("twilightforest:special_handheld", TextureSlot.LAYER0);
	public static final ModelTemplate TWO_LAYERED_HANDHELD = createItem("handheld", TextureSlot.LAYER0, TextureSlot.LAYER1);
	public static final ModelTemplate TWO_LAYERED_BOW = createItem("bow", TextureSlot.LAYER0, TextureSlot.LAYER1);

}
