package twilightforest.compat.ie;

import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.shader.DynamicShaderLayer;
import blusunrize.immersiveengineering.api.shader.ShaderLayer;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import blusunrize.immersiveengineering.api.shader.impl.*;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.shader.ShaderManager;
import twilightforest.client.shader.ShaderUniform;
import twilightforest.item.TFItems;

import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

public class IEShaderRegister {
	// Layer Constants
	private static final ShaderLayer NULL_LAYER                = new ShaderLayer( null                                                                                                       , 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_REVOLVER_LAYER  = new ShaderLayer( new ResourceLocation("immersiveengineering", "revolvers/shaders/revolver_uncoloured"           ), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_CHEMTHROW_LAYER = new ShaderLayer( new ResourceLocation("immersiveengineering", "item/shaders/chemthrower_uncoloured"             ), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_DRILL_LAYER     = new ShaderLayer( new ResourceLocation("immersiveengineering", "item/shaders/drill_diesel_uncoloured"            ), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_RAILGUN_LAYER   = new ShaderLayer( new ResourceLocation("immersiveengineering", "item/shaders/railgun_uncoloured"                 ), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_SHIELD_LAYER    = new ShaderLayer( new ResourceLocation("immersiveengineering", "item/shaders/shield_uncoloured"                  ), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_MINECART_LAYER  = new ShaderLayer( new ResourceLocation("immersiveengineering", "textures/models/shaders/minecart_uncoloured.png" ), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_BALLOON_LAYER   = new ShaderLayer( new ResourceLocation("immersiveengineering", "block/shaders/balloon_uncoloured"                ), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_BANNER_LAYER    = new ShaderLayer( new ResourceLocation("immersiveengineering", "block/shaders/banner_uncoloured"                  ), 0xFFFFFFFF);

	//FIXME placeholders to prevent log error spam. Once the event in TFClientEvents is working again, get rid of these and use the bottom ones
	public static final ResourceLocation PROCESSED_REVOLVER_GRIP_LAYER = new ResourceLocation("immersiveengineering", "revolvers/shaders/revolver_grip");
	public static final ResourceLocation PROCESSED_REVOLVER_LAYER      = new ResourceLocation("immersiveengineering", "revolvers/shaders/revolver_0");
	public static final ResourceLocation PROCESSED_CHEMTHROW_LAYER     = new ResourceLocation("immersiveengineering", "item/shaders/chemthrower_0");
	public static final ResourceLocation PROCESSED_DRILL_LAYER         = new ResourceLocation("immersiveengineering", "item/shaders/drill_diesel_0");
	public static final ResourceLocation PROCESSED_RAILGUN_LAYER       = new ResourceLocation("immersiveengineering", "item/shaders/railgun_0");
	public static final ResourceLocation PROCESSED_SHIELD_LAYER        = new ResourceLocation("immersiveengineering", "item/shaders/shield_0");
//	public static final ResourceLocation PROCESSED_MINECART_LAYER      = new ResourceLocation("immersiveengineering", "textures/models/shaders/minecart_0");
	public static final ResourceLocation PROCESSED_BALLOON_LAYER       = new ResourceLocation("immersiveengineering", "block/shaders/balloon_0");

//	public static final ResourceLocation PROCESSED_REVOLVER_GRIP_LAYER = new ResourceLocation(TwilightForestMod.ID, "items/immersiveengineering/revolver_grip_processed");
//	public static final ResourceLocation PROCESSED_REVOLVER_LAYER      = new ResourceLocation(TwilightForestMod.ID, "items/immersiveengineering/revolver_processed");
//	public static final ResourceLocation PROCESSED_CHEMTHROW_LAYER     = new ResourceLocation(TwilightForestMod.ID, "items/immersiveengineering/chemthrower_processed");
//	public static final ResourceLocation PROCESSED_DRILL_LAYER         = new ResourceLocation(TwilightForestMod.ID, "items/immersiveengineering/drill_processed");
//	public static final ResourceLocation PROCESSED_RAILGUN_LAYER       = new ResourceLocation(TwilightForestMod.ID, "items/immersiveengineering/railgun_processed");
//	public static final ResourceLocation PROCESSED_SHIELD_LAYER        = new ResourceLocation(TwilightForestMod.ID, "items/immersiveengineering/shield_processed");
//	//public static final ResourceLocation PROCESSED_MINECART_LAYER      = new ResourceLocation(TwilightForestMod.ID, "textures/items/immersiveengineering/minecart_processed.png");
//	public static final ResourceLocation PROCESSED_BALLOON_LAYER       = new ResourceLocation(TwilightForestMod.ID, "blocks/immersiveengineering/balloon_processed");

	private static final ResourceLocation TEXTURE_STARS = new ResourceLocation("textures/entity/end_portal.png");

	private static final BiConsumer<IntConsumer, Boolean> TWILIGHT_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) {
			ShaderManager.useShader(ShaderManager.twilightSkyShader, shaderCallback);
			ARBShaderObjects.glCreateShaderObjectARB(ARBShaderObjects.GL_INT_VEC2_ARB);
			Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE_STARS);
		} else {
			ShaderManager.releaseShader();
		}
		ARBShaderObjects.glCreateShaderObjectARB(ARBShaderObjects.GL_OBJECT_SHADER_SOURCE_LENGTH_ARB);
		Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
	};

	// TODO There's got to be a better way!
	private static final BiConsumer<IntConsumer, Boolean> FIREFLY_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) ShaderManager.useShader(ShaderManager.fireflyShader, shaderCallback);
		else ShaderManager.releaseShader();
	};

	private static final BiConsumer<IntConsumer, Boolean> CARMINITE_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) ShaderManager.useShader(ShaderManager.carminiteShader, shaderCallback);
		else ShaderManager.releaseShader();
	};

	private static final BiConsumer<IntConsumer, Boolean> DEVICE_RED_ENERGY_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) ShaderManager.useShader(ShaderManager.towerDeviceShader, shaderCallback);
		else ShaderManager.releaseShader();
	};

	private static final BiConsumer<IntConsumer, Boolean> DEVICE_YELLOW_ENERGY_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) {
			GlStateManager.getTexLevelParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			ShaderManager.useShader(ShaderManager.yellowCircuitShader, shaderCallback);
		} else {
			ShaderManager.releaseShader();
			GlStateManager.getTexLevelParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}
	};

	private static final BiConsumer<IntConsumer, Boolean> AURORA_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) ShaderManager.useShader(ShaderManager.auroraShader, shaderCallback);
		else ShaderManager.releaseShader();
	};

	private static final BiConsumer<IntConsumer, Boolean> RAM_TRICONSUMER = (shaderCallback, pre) -> {
		if(pre) Minecraft.getInstance().gameRenderer.getLightTexture().disableLightmap();
		else Minecraft.getInstance().gameRenderer.getLightTexture().enableLightmap();
	};

	//private static final BiConsumer<IntConsumer, Boolean> OUTLINE_TRICONSUMER = (shaderCallback, pre) -> {
	//    if (pre) {
	//        GlStateManager.pushMatrix();
	//    } else {
	//        GlStateManager.popMatrix();
	//    }
	//    //if (pre) {
	//    //    //GlStateManager.pushMatrix();
	//    //    //GlStateManager.scalef(1.05f, 1.05f, 1.05f);
	//    //    GlStateManager.enableCull();
	//    //    //GL11.glFrontFace(GL11.GL_CW);
	//    //    GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
	//    //    ShaderHelper.useShader(ShaderHelper.outlineShader, shaderCallback);
	//    //} else {
	//    //    ShaderHelper.releaseShader();
	//    //    GlStateManager.cullFace(GlStateManager.CullFace.BACK);
	//    //    //GL11.glFrontFace(GL11.GL_CCW);
	//    //    GlStateManager.disableCull();
	//    //    //GlStateManager.scalef(0.8333f, 0.8333f, 0.8333f);
	//    //    //GlStateManager.popMatrix();
	//    //}
	//};

	// m Mod
	// t CaseType
	// s Suffix
	// c Color
	private static final ShaderLayerProvider<?> LAYER_PROVIDER = (m, t, s, c) -> new ShaderLayer(m.provideTex(t, s), c);
	private static final ShaderLayerProvider<?> TOWER_DEVICE_SHADER_PROVIDER = (m, t, s, c) -> new ShaderConsumerLayer(ModType.TWILIGHT_FOREST.provideTex(t, "energy"), 0xFFFFFFFF, DEVICE_RED_ENERGY_TRICONSUMER, ShaderManager.Uniforms.STAR_UNIFORMS);
	private static final ShaderLayerProvider<?> YELLOW_CIRCUIT_SHADER_PROVIDER = (m, t, s, c) -> new ShaderConsumerLayer(ModType.IMMERSIVE_ENGINEERING.provideTex(t, "circuit"), 0xFF_BA_EE_02, DEVICE_YELLOW_ENERGY_TRICONSUMER, ShaderManager.Uniforms.STAR_UNIFORMS);

	// Registering
	private static List<ShaderRegistry.ShaderRegistryEntry> SHADERS;
	private static List<ShaderRegistry.ShaderRegistryEntry> NONBOSSES;

	private static final Rarity RARITY = TwilightForestMod.getRarity();

	//FIXME some shaders broke, pls fix
	public static void initShaders() {
		NONBOSSES = ImmutableList.of(
				// MAIN COLOR, MINOR COLOR (EDGES), SECONDARY COLOR (GRIP, etc)
//				registerShaderCases( "Twilight", ModType.IMMERSIVE_ENGINEERING, "1_4", RARITY, 0xFF_4C_64_5B, 0xFF_28_25_3F, 0xFF_00_AA_00, 0xFF_FF_FF_FF, (m, t, s, c) -> new ShaderConsumerLayer( m.provideTex(t, s), 0xFFFFFFFF, TWILIGHT_TRICONSUMER , ShaderManager.Uniforms.STAR_UNIFORMS )).setInfo("Twilight Forest", "Twilight Forest", "twilightforest"),
//				registerShaderCases( "Firefly", ModType.IMMERSIVE_ENGINEERING, "1_6", RARITY, 0xFF_66_41_40, 0xFF_F5_99_2F, 0xFF_C0_FF_00, 0xFF_C0_FF_00,  LAYER_PROVIDER, (m, t, s, c) -> new ShaderConsumerLayer(ModType.IMMERSIVE_ENGINEERING.provideTex(t, "0"), 0xFFFFFFFF, FIREFLY_TRICONSUMER, ShaderManager.Uniforms.TIME_UNIFORM)).setInfo("Twilight Forest", "Firefly", "firefly"),
				registerShaderCases( "Pinch Beetle", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY, 0xFF_BC_93_27, 0xFF_24_16_09, 0xFF_24_16_09, 0xFF_44_44_44, LAYER_PROVIDER, (m, t, s, c) -> new ShaderLayer( m.provideTex(t, "1_6"), c)).setInfo("Twilight Forest", "Pinch Beetle", "pinch_beetle"),

				registerShaderCases( "Snakestone", ModType.TWILIGHT_FOREST, "streaks", RARITY, 0xFF_9F_9F_9F, 0xFF_68_68_68, 0xFF_60_60_60, 0xFF_FF_FF_FF, LAYER_PROVIDER, (m, t, s, c) -> new ShaderLayer( ModType.TWILIGHT_FOREST.provideTex(t, "scales"), 0xFF_50_50_50 ), (m, t, s, c) -> new ShaderLayer( ModType.IMMERSIVE_ENGINEERING.provideTex(t, "circuit"), 0xFF_58_58_58 ) ).setInfo("Twilight Forest", "Nagastone", "courtyard"),

				registerShaderCases( "Mazestone", ModType.TWILIGHT_FOREST, "scales", RARITY, 0xFF_8E_99_8E, 0xFF_50_59_50, 0xFF_70_7B_70, 0xFF_FF_FF_FF, LAYER_PROVIDER).setInfo("Twilight Forest", "Mazestone", "mazestone"),

				registerShaderCases("Underbrick", ModType.TWILIGHT_FOREST, "scales", RARITY, 0xFF_85_68_45, 0xFF_76_7F_76, 0xFF_61_4D_33, 0xFF_FF_FF_FF, LAYER_PROVIDER).setInfo("Twilight Forest", "Underbrick", "underbrick"),
//				registerShaderCasesTopped("Towerwood", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY, 0xFF_A6_65_3A, 0xFF_F5_DA_93, 0xFF_83_5A_35, 0xFF_FF_FF_FF, new ShaderLayerProvider<?>[]{ LAYER_PROVIDER, YELLOW_CIRCUIT_SHADER_PROVIDER}, TOWER_DEVICE_SHADER_PROVIDER ).setInfo("Twilight Forest", "Towerwood Planks", "towerwood"),
//				registerShaderCasesTopped("Carminite", ModType.TWILIGHT_FOREST, "carminite", RARITY, 0xFF_72_00_00, 0xFF_FF_00_00, 0xFF_FF_00_00, 0xFF_FF_00_00, new ShaderLayerProvider<?>[]{(m, t, s, c) -> new ShaderConsumerLayer(m.provideTex(t, s), 0xFFFFFFFF, CARMINITE_TRICONSUMER, ShaderManager.Uniforms.STAR_UNIFORMS)}, TOWER_DEVICE_SHADER_PROVIDER).setInfo("Twilight Forest", "Carminite", "carminite"),

//				registerShaderCases("Auroralized", ModType.IMMERSIVE_ENGINEERING, "1_5", RARITY, 0xFF_00_FF_FF, 0xFF_00_FF_00, 0xFF_00_00_FF, 0xFF_FF_FF_FF, (m, t, s, c) -> new ShaderConsumerLayer( m.provideTex(t, s), 0xFFFFFFFF, AURORA_TRICONSUMER, ShaderManager.Uniforms.TIME_UNIFORM)).setInfo("Twilight Forest", "Aurora Palace", "aurora"),

				registerShaderCases("Ironwood", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY, 0xFF_6B_61_61, 0xFF_5F_4D_40, 0xFF_5E_57_4B, 0xFF_FF_FF_FF, (m, t, s, c) -> new ShaderLayer(ModType.TWILIGHT_FOREST.provideTex(t, "streaks"), 0xFF_79_7C_43 ), LAYER_PROVIDER).setInfo("Twilight Forest", "Ironwood", "ironwood"),
				registerShaderCases("Steeleaf", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY, 0xFF_52_87_3A, 0xFF_1E_32_14, 0xFF_41_62_30, 0xFF_FF_FF_FF, (m, t, s, c) -> new ShaderLayer(ModType.IMMERSIVE_ENGINEERING.provideTex(t, "1_4"), 0xFF_41_62_30 ), (m, t, s, c) -> new ShaderLayer( ModType.TWILIGHT_FOREST.provideTex(t, "streaks"), 0xFF_6D_A2_5E ), LAYER_PROVIDER ).setInfo("Twilight Forest", "Steeleaf", "steeleaf"),
				registerShaderCases("Knightly", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY, 0xFF_E7_FC_CD, 0xFF_4D_4C_4B, 0xFF_80_8C_72, 0xFF_FF_FF_FF, LAYER_PROVIDER).setInfo("Twilight Forest", "Knightly", "knightly"),
				registerShaderCases("Fiery", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY, 0xFF_19_13_13, 0xFF_FD_D4_5D, 0xFF_77_35_11, 0xFF_FF_FF_FF, LAYER_PROVIDER).setInfo("Twilight Forest", "Fiery", "fiery"),

				registerShaderCases("Final Castle", ModType.TWILIGHT_FOREST, "scales", RARITY, 0xFF_EC_EA_E6, 0xFF_C8_BB_BC, 0xFF_00_FF_FF, 0xFF_00_FF_FF, LAYER_PROVIDER).setInfo("Twilight Forest", "Final Castle", "finalcastle"),
// TODO Throbbing effect
//				registerShaderCases("Cube of Annihilation", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY, 0xFF_00_00_03, 0xFF_14_00_40, 0xFF_00_00_03, 0xFF_14_00_40, (m, t, s, c) -> new ShaderConsumerLayer( m.provideTex(t, s), 0xFF_14_00_40, OUTLINE_TRICONSUMER, ShaderHelper.TIME_UNIFORM)).setInfo("Twilight Forest", "Cube of Annilation", "cube_of_annilation"),
				registerShaderCases("Cube of Annihilation", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY, 0xFF_00_00_03, 0xFF_14_00_40, 0xFF_00_00_03, 0xFF_14_00_40, (m, t, s, c) -> new ShaderLayer( m.provideTex(t, s), 0xFF_14_00_40)).setInfo("Twilight Forest", "Cube of Annilation", "cube_of_annilation")
		);

		ImmutableList.Builder<ShaderRegistry.ShaderRegistryEntry> listBuilder = ImmutableList.builder();

		listBuilder.addAll(NONBOSSES);

		listBuilder.add(
				registerShaderCases("Questing Ram", ModType.TWILIGHT_FOREST, "streaks", RARITY, 0xFF_F9_E1_C8, 0xFF_9A_85_69, 0xFF_2F_2B_36, 0xFF_90_D8_EF, LAYER_PROVIDER, (m, t, s, c) -> new ShaderConsumerLayer(ModType.IMMERSIVE_ENGINEERING.provideTex(t, "circuit"), 0x30_90_D8_EF, RAM_TRICONSUMER, ShaderManager.Uniforms.STAR_UNIFORMS)).setInfo("Twilight Forest", "Questing Ram", "questing_ram"),

				registerShaderCases("Naga", ModType.TWILIGHT_FOREST, "scales", RARITY, 0xFF_32_5D_25, 0xFF_17_29_11, 0xFF_A5_D4_16, 0xFF_FF_FF_FF, LAYER_PROVIDER, (m, t, s, c) -> new ShaderLayer( ModType.IMMERSIVE_ENGINEERING.provideTex(t, "shark"), 0xFF_FF_FF_FF)).setInfo("Twilight Forest", "Naga Boss", "naga"),
				registerShaderCases("Lich", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY, 0xFF_DF_D9_CC, 0xFF_C3_9C_00, 0xFF_3A_04_75, 0xFF_FF_FF_FF, LAYER_PROVIDER).setInfo("Twilight Forest", "Twilight Lich Boss", "lich"),

				registerShaderCases("Minoshroom", ModType.IMMERSIVE_ENGINEERING, "1_6", RARITY, 0xFF_A8_10_12, 0xFF_B3_B3_B3, 0xFF_33_EB_CB, 0xFF_FF_FF_FF, LAYER_PROVIDER).setInfo("Twilight Forest", "Minoshroom Miniboss", "minoshroom"),
				registerShaderCases("Hydra", ModType.TWILIGHT_FOREST, "scales", RARITY, 0xFF_14_29_40, 0xFF_29_80_6B, 0xFF_F1_0A_92, 0xFF_FF_FF_FF, LAYER_PROVIDER, (m, t, s, c) -> new ShaderLayer( ModType.IMMERSIVE_ENGINEERING.provideTex(t, "shark"), 0xFF_FF_FF_FF)).setInfo("Twilight Forest", "Hydra Boss", "hydra"),

				registerShaderCases("Knight Phantom", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY, 0xCC_40_6D_05, 0xFF_36_35_34, 0xFF_7A_5C_49, 0xFF_FF_FF_FF, LAYER_PROVIDER).setInfo("Twilight Forest", "Knight Phantom Minibosses", "knight_phantom"),
				registerShaderCases("Ur-Ghast", ModType.IMMERSIVE_ENGINEERING, "1_2", RARITY, 0xFF_F9_F9_F9, 0xFF_9A_37_37, 0xFF_56_56_56, 0xFF_FF_FF_FF, LAYER_PROVIDER).setInfo("Twilight Forest", "Ur-Ghast", "ur-ghast"),

				registerShaderCases("Alpha Yeti", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY, 0xFF_FC_FC_FC, 0xFF_4A_80_CE, 0xFF_25_3F_66, 0xFF_FF_FF_FF, LAYER_PROVIDER).setInfo("Twilight Forest", "Alpha Yeti", "alpha_yeti"),
				registerShaderCases("Snow Queen", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY, 0xFF_DC_FB_FF, 0xFF_C3_9C_00, 0xFF_03_05_89, 0xFF_FF_FF_FF, LAYER_PROVIDER).setInfo("Twilight Forest", "Snow Queen", "snow_queen")
		);

		SHADERS = listBuilder.build();
	}

	public static List<ShaderRegistry.ShaderRegistryEntry> getAllTwilightShaders() {
		return SHADERS;
	}

	public static List<ShaderRegistry.ShaderRegistryEntry> getAllNonbossShaders() {
		return NONBOSSES;
	}

	// Shaderizing!
	private static class ShaderConsumerLayer extends DynamicShaderLayer {

		private final BiConsumer<IntConsumer, Boolean> render;
		private final IntConsumer shaderCallback;

		ShaderConsumerLayer(ResourceLocation texture, int colour, BiConsumer<IntConsumer, Boolean> render, ShaderUniform[] shaderParams) {
			super(texture, colour);
			this.render = render;

			shaderCallback = shader -> { for(ShaderUniform param: shaderParams) { param.assignUniform(shader); } };
		}

		@OnlyIn(Dist.CLIENT)
		@Override
		public RenderType getRenderType(RenderType baseType) {
			if(this.render == null) {
				return baseType;
			} else {
				return new RenderType(
						"shader_" + baseType + render,
						DefaultVertexFormats.BLOCK,
						GL11.GL_QUADS,
						256,
						false,
						true,
						() -> {
							baseType.setupRenderState();
							render.accept(shaderCallback, true);
						},
						() -> {
							render.accept(shaderCallback, false);
							baseType.clearRenderState();
						}
				){

				};
			}
		}
	}

	@SafeVarargs
	@SuppressWarnings({"rawtypes", "varargs"})
	private static ShaderRegistry.ShaderRegistryEntry registerShaderCasesTopped(String name, ModType mod, String overlayType, Rarity rarity, int bodyColor, int colorSecondary, int gripColor, int colorBlade, ShaderLayerProvider<? extends ShaderLayer>[] providers, ShaderLayerProvider<? extends ShaderLayer>... extraProviders) {
		ResourceLocation modName = new ResourceLocation(TwilightForestMod.ID, name.toLowerCase(Locale.ROOT).replace(" ", "_"));
		ShaderRegistry.registerShader_Item(modName, rarity, gripColor, bodyColor, colorSecondary);

		registerShaderCaseRevolver   (modName, gripColor, bodyColor, colorBlade, rarity, provideFromProviders( mod, CaseType.REVOLVER        , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.REVOLVER        , overlayType                , colorSecondary, extraProviders ));
		registerShaderCaseChemthrower(modName, gripColor, bodyColor,             rarity, provideFromProviders( mod, CaseType.CHEMICAL_THROWER, overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.CHEMICAL_THROWER, overlayType                , colorSecondary, extraProviders ));
		registerShaderCaseDrill      (modName, gripColor, bodyColor, colorBlade, rarity, provideFromProviders( mod, CaseType.DRILL           , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.DRILL           , overlayType                , colorSecondary, extraProviders ));
		registerShaderCaseRailgun    (modName, gripColor, bodyColor,             rarity, provideFromProviders( mod, CaseType.RAILGUN         , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.RAILGUN         , overlayType                , colorSecondary, extraProviders ));
		registerShaderCaseShield     (modName, gripColor, bodyColor,             rarity, provideFromProviders( mod, CaseType.SHIELD          , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.SHIELD          , overlayType                , colorSecondary, extraProviders ));
		registerShaderCaseMinecart   (modName, gripColor, bodyColor,             rarity, provideFromProviders( mod, CaseType.MINECART        , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.MINECART        , overlayType + ".png"       , colorSecondary, extraProviders ));
		registerShaderCaseBalloon    (modName, gripColor, bodyColor,             rarity, provideFromProviders( mod, CaseType.BALLOON         , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.BALLOON         , overlayType                , colorSecondary, extraProviders ));
		registerShaderCaseBanner     (modName, gripColor, bodyColor,             rarity, provideFromProviders( mod, CaseType.BALLOON         , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.BANNER          , overlayType                , colorSecondary, extraProviders ));

		// Since shaders won't occur in a way we'd like them to, we should register any additional variants ourselves if we know of any
		for (ShaderRegistry.IShaderRegistryMethod method : ShaderRegistry.shaderRegistrationMethods) {
			method.apply(modName, overlayType, rarity, gripColor, bodyColor, colorSecondary, colorBlade, null, 0);
		}

		return ShaderRegistry.shaderRegistry.get(modName).setCrateLoot(false).setBagLoot(false).setInLowerBags(false).setReplicationCost(() -> new IngredientWithSize(Ingredient.fromItems(TFItems.ore_meter.get())));
	}

	@SafeVarargs
	@SuppressWarnings("varargs")
	private static ShaderRegistry.ShaderRegistryEntry registerShaderCases(String name, ModType type, String overlayType, Rarity rarity, int bodyColor, int colorSecondary, int gripColor, int colorBlade, ShaderLayerProvider<? extends ShaderLayer>... providers) {
		return registerShaderCasesTopped(name, type, overlayType, rarity, bodyColor, colorSecondary, gripColor, colorBlade, providers);
	}

	// Shader Case Registration helpers
	@FunctionalInterface
	private interface ShaderLayerProvider<T extends ShaderLayer> {
		T get(ModType mod, CaseType type, String suffix, int color);
	}

	private static ShaderLayer[] provideFromProviders( ModType mod, CaseType type, String suffix, int color, ShaderLayerProvider<? extends ShaderLayer>[] layerProviders) {
		ShaderLayer[] array = new ShaderLayer[layerProviders.length];

		for (int i = 0; i < layerProviders.length; i++)
			array[i] = layerProviders[i].get( mod, type, suffix, color );

		return array;
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseRevolver registerShaderCaseRevolver(ResourceLocation name, int gripColor, int bodyColor, int bladeColor, Rarity rarity, ShaderLayer [] additionalLayers, ShaderLayer ... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new ShaderCaseRevolver(
				shaderLayerBuilder.add(
						new ShaderLayer(PROCESSED_REVOLVER_GRIP_LAYER, gripColor),
						new ShaderLayer(PROCESSED_REVOLVER_LAYER, bodyColor),
						new ShaderLayer(PROCESSED_REVOLVER_LAYER, bladeColor)
				).add(additionalLayers).add(UNCOLORED_REVOLVER_LAYER).add(topLayers).build()), rarity);
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseChemthrower registerShaderCaseChemthrower(ResourceLocation name, int gripColor, int bodyColor, Rarity rarity, ShaderLayer [] additionalLayers, ShaderLayer ... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new TFShaderCaseChemthrower( 3 + additionalLayers.length,
				shaderLayerBuilder.add(
						new ShaderLayer(PROCESSED_CHEMTHROW_LAYER, gripColor),
						new ShaderLayer(PROCESSED_CHEMTHROW_LAYER, bodyColor)
				).add(additionalLayers).add(UNCOLORED_CHEMTHROW_LAYER).add(topLayers).build()), rarity);
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseDrill registerShaderCaseDrill(ResourceLocation name, int gripColor, int bodyColor, int bladeColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new TFShaderCaseDrill( 5 + additionalLayers.length,
				shaderLayerBuilder.add(
						new ShaderLayer (PROCESSED_DRILL_LAYER, gripColor),
						new ShaderLayer (PROCESSED_DRILL_LAYER, bodyColor)
				).add(UNCOLORED_DRILL_LAYER).add(additionalLayers).add(UNCOLORED_DRILL_LAYER).add(NULL_LAYER).add(topLayers).build()), rarity);//.addHeadLayers(new ShaderCase (new ResourceLocation("immersiveengineering", "items/drill_iron"), bladeColor));
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseRailgun registerShaderCaseRailgun(ResourceLocation name, int gripColor, int bodyColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new TFShaderCaseRailgun( 3 + additionalLayers.length,
				shaderLayerBuilder.add(
						new ShaderLayer(PROCESSED_RAILGUN_LAYER, gripColor),
						new ShaderLayer(PROCESSED_RAILGUN_LAYER, bodyColor)
				).add(additionalLayers).add(UNCOLORED_RAILGUN_LAYER).add(topLayers).build()), rarity);
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseShield registerShaderCaseShield(ResourceLocation name, int gripColor, int bodyColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new TFShaderCaseShield( 3 + additionalLayers.length,
				shaderLayerBuilder.add(
						new ShaderLayer (PROCESSED_SHIELD_LAYER, gripColor),
						new ShaderLayer (PROCESSED_SHIELD_LAYER, bodyColor)
				).add(additionalLayers).add(UNCOLORED_SHIELD_LAYER).add(topLayers).build()), rarity);
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseMinecart registerShaderCaseMinecart(ResourceLocation name, int bodyColor, int secondaryColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new ShaderCaseMinecart(
				shaderLayerBuilder.add(
						new ShaderLayer ( new ResourceLocation( "immersiveengineering", "textures/models/shaders/minecart_0.png"   ), bodyColor),
						new ShaderLayer ( new ResourceLocation( "immersiveengineering", "textures/models/shaders/minecart_1_0.png" ), secondaryColor)
				).add(additionalLayers).add(UNCOLORED_MINECART_LAYER).add(topLayers).build()), rarity);
	}

	@SuppressWarnings("UnusedReturnValue")
	public static ShaderCaseBanner registerShaderCaseBanner(ResourceLocation name, int bodyColor, int secondaryColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new ShaderCaseBanner(
				shaderLayerBuilder.add(
						new ShaderLayer(new ResourceLocation("immersiveengineering", "block/shaders/banner_0"), bodyColor),
						new ShaderLayer(new ResourceLocation("immersiveengineering", "block/shaders/banner_1_0"), secondaryColor)
				).add(additionalLayers).add(UNCOLORED_BANNER_LAYER).add(topLayers).build()), rarity);
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseBalloon registerShaderCaseBalloon(ResourceLocation name, int gripColor, int bodyColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new ShaderCaseBalloon(
				shaderLayerBuilder.add(
						new ShaderLayer (PROCESSED_BALLOON_LAYER, gripColor),
						new ShaderLayer (PROCESSED_BALLOON_LAYER, bodyColor)
				).add(additionalLayers).add(UNCOLORED_BALLOON_LAYER).add(topLayers).build()), rarity);
	}

	public enum ModType {

		IMMERSIVE_ENGINEERING("immersiveengineering") {
			@Override
			String getPath(CaseType caseType, String suffix) {
				switch (caseType) {
					case REVOLVER:
						return "revolvers/shaders/revolver_" + suffix;
					case CHEMICAL_THROWER:
						return "item/shaders/chemthrower_" + suffix;
					case DRILL:
						return "item/shaders/drill_diesel_" + suffix;
					case RAILGUN:
						return "item/shaders/railgun_" + suffix;
					case SHIELD:
						return "item/shaders/shield_" + suffix;
					case MINECART:
						return "textures/models/shaders/minecart_" + suffix + ".png";
					case BALLOON:
						return "block/shaders/balloon_" + suffix;
					case BANNER:
						return "block/shaders/banner_" + suffix;
				}

				return "";
			}
		},
		TWILIGHT_FOREST(TwilightForestMod.ID) {
			@Override
			String getPath(CaseType caseType, String suffix) {
				switch (caseType) {
					case REVOLVER:
						return "items/immersiveengineering/revolver_" + suffix;
					case CHEMICAL_THROWER:
						return "items/immersiveengineering/chemthrower_" + suffix;
					case DRILL:
						return "items/immersiveengineering/drill_" + suffix;
					case RAILGUN:
						return "items/immersiveengineering/railgun_" + suffix;
					case SHIELD:
						return "items/immersiveengineering/shield_" + suffix;
					case MINECART:
						return "textures/model/immersiveengineering/minecart_" + suffix + ".png";
					case BALLOON:
						return "block/immersiveengineering/balloon_" + suffix;
					case BANNER:
						return "block/immersiveengineering/banner_" + suffix;
				}

				return "";
			}

			@Override
			public ResourceLocation provideTex(CaseType caseType, String suffix) {
				if (caseType == CaseType.MINECART && suffix.startsWith("1_")) {
					return IMMERSIVE_ENGINEERING.provideTex(caseType, suffix);
				}
				return super.provideTex(caseType, suffix);
			}
		};

		private final String namespace;

		ModType(String namespace) {
			this.namespace = namespace;
		}

		abstract String getPath(CaseType caseType, String suffix);

		public ResourceLocation provideTex(CaseType caseType, String suffix) {
			return new ResourceLocation(namespace, getPath(caseType, suffix));
		}
	}

	public enum CaseType {

		REVOLVER,
		CHEMICAL_THROWER,
		DRILL,
		RAILGUN,
		SHIELD,
		MINECART,
		BALLOON,
		BANNER;

		public static CaseType[] everythingButMinecart() {
			return new CaseType[]{ REVOLVER, CHEMICAL_THROWER, DRILL, RAILGUN, SHIELD, BALLOON, BANNER };
		}
	}
}