package twilightforest.data.models;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

public class TFBlockModelTemplates extends ModelTemplates {

	public static final ModelTemplate ANTIBUILDER = create("antibuilder", TextureSlot.ALL, TFTextureSlot.ALL_2, TFTextureSlot.ALL_3).extend()
		.parent(ResourceLocation.withDefaultNamespace("block/cube_all"))
		.element(builder -> builder.allFaces((direction, faceBuilder) -> faceBuilder.texture(TextureSlot.ALL).cullface(direction)))
		.element(builder -> builder.allFaces((direction, faceBuilder) -> faceBuilder.emissivity(15, 15).texture(TFTextureSlot.ALL_2).cullface(direction)))
		.element(builder -> builder.allFaces((direction, faceBuilder) -> faceBuilder.emissivity(10, 10).texture(TFTextureSlot.ALL_3).cullface(direction))).build();
	public static final ModelTemplate ANTIBUILT_BLOCK = create("antibuilt_block", TextureSlot.ALL, TFTextureSlot.ALL_2).extend()
		.parent(ResourceLocation.withDefaultNamespace("block/cube_all"))
		.element(builder -> builder.allFaces((direction, faceBuilder) -> faceBuilder.texture(TextureSlot.ALL).cullface(direction)))
		.element(builder -> builder.allFaces((direction, faceBuilder) -> faceBuilder.emissivity(10, 10).texture(TFTextureSlot.ALL_2).cullface(direction))).build();
	public static final ModelTemplate BISECTED_STAIRS_STRAIGHT = create("bisected_stairs", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TFTextureSlot.MIDDLE);
	public static final ModelTemplate BISECTED_STAIRS_INNER = create("bisected_inner_stairs", "_inner", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TFTextureSlot.MIDDLE);
	public static final ModelTemplate BISECTED_STAIRS_OUTER = create("bisected_outer_stairs", "_outer", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TFTextureSlot.MIDDLE);

	public static final ModelTemplate FORCEFIELD = create("forcefield", TextureSlot.PANE, TextureSlot.PARTICLE).extend().parent(ResourceLocation.withDefaultNamespace("block/cube_all")).ambientOcclusion(false).renderType("translucent").build();

	public static final ModelTemplate CTM_NO_BASE = create("ctm_no_base", TextureSlot.PARTICLE, TFTextureSlot.CTM_OVERLAY, TFTextureSlot.CTM_OVERLAY_CONNECTED).extend().parent(ResourceLocation.withDefaultNamespace("block/block")).build();
	public static final ModelTemplate CTM = create("ctm", TextureSlot.PARTICLE, TFTextureSlot.CTM_BASE, TFTextureSlot.CTM_OVERLAY, TFTextureSlot.CTM_OVERLAY_CONNECTED).extend().parent(ResourceLocation.withDefaultNamespace("block/block")).build();

	public static final ModelTemplate THORNS_MAIN = create("thorns_main", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_main")).renderType("cutout").build();
	public static final ModelTemplate THORNS_TOP = create("thorns_top", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_top")).renderType("cutout").build();
	public static final ModelTemplate THORNS_BOTTOM = create("thorns_bottom", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_bottom")).renderType("cutout").build();
	public static final ModelTemplate THORNS_SIDE = create("thorns_no_section", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_no_section")).renderType("cutout").build();
	public static final ModelTemplate THORNS_SIDE_ALT = create("thorns_no_section_alt", TextureSlot.SIDE, TextureSlot.END).extend().parent(TwilightForestMod.prefix("block/thorns_no_section_alt")).renderType("cutout").build();

}
