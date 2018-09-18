package twilightforest.compat.ie;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.shader.*;
import blusunrize.immersiveengineering.client.ClientUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.util.TriConsumer;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.shader.ShaderManager;
import twilightforest.client.shader.ShaderUniform;
import twilightforest.item.TFItems;

import java.util.List;
import java.util.function.IntConsumer;

public class IEShaderRegister {
    // Layer Constants
    private static final ShaderCase.ShaderLayer NULL_LAYER                = new ShaderCase.ShaderLayer( null                                                                                            , 0xFFFFFFFF);
    private static final ShaderCase.ShaderLayer UNCOLORED_REVOLVER_LAYER  = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "revolvers/shaders/revolver_uncoloured"           ), 0xFFFFFFFF);
    private static final ShaderCase.ShaderLayer UNCOLORED_CHEMTHROW_LAYER = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "items/shaders/chemthrower_uncoloured"            ), 0xFFFFFFFF);
    private static final ShaderCase.ShaderLayer UNCOLORED_DRILL_LAYER     = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "items/shaders/drill_diesel_uncoloured"           ), 0xFFFFFFFF);
    private static final ShaderCase.ShaderLayer UNCOLORED_RAILGUN_LAYER   = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "items/shaders/railgun_uncoloured"                ), 0xFFFFFFFF);
    private static final ShaderCase.ShaderLayer UNCOLORED_SHIELD_LAYER    = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "items/shaders/shield_uncoloured"                 ), 0xFFFFFFFF);
    private static final ShaderCase.ShaderLayer UNCOLORED_MINECART_LAYER  = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "textures/models/shaders/minecart_uncoloured.png" ), 0xFFFFFFFF);
    private static final ShaderCase.ShaderLayer UNCOLORED_BALLOON_LAYER   = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "blocks/shaders/balloon_uncoloured"               ), 0xFFFFFFFF);

    public static final ResourceLocation PROCESSED_REVOLVER_GRIP_LAYER = new ResourceLocation("twilightforest", "items/immersiveengineering/revolver_grip_processed");
    public static final ResourceLocation PROCESSED_REVOLVER_LAYER      = new ResourceLocation("twilightforest", "items/immersiveengineering/revolver_processed");
    public static final ResourceLocation PROCESSED_CHEMTHROW_LAYER     = new ResourceLocation("twilightforest", "items/immersiveengineering/chemthrower_processed");
    public static final ResourceLocation PROCESSED_DRILL_LAYER         = new ResourceLocation("twilightforest", "items/immersiveengineering/drill_processed");
    public static final ResourceLocation PROCESSED_RAILGUN_LAYER       = new ResourceLocation("twilightforest", "items/immersiveengineering/railgun_processed");
    public static final ResourceLocation PROCESSED_SHIELD_LAYER        = new ResourceLocation("twilightforest", "items/immersiveengineering/shield_processed");
    //public static final ResourceLocation PROCESSED_MINECART_LAYER      = new ResourceLocation("twilightforest", "textures/items/immersiveengineering/minecart_processed.png");
    public static final ResourceLocation PROCESSED_BALLOON_LAYER       = new ResourceLocation("twilightforest", "blocks/immersiveengineering/balloon_processed");

    private static final ResourceLocation TEXTURE_STARS = new ResourceLocation("textures/entity/end_portal.png");

    private static final TriConsumer<IntConsumer, Boolean, Float> TWILIGHT_TRICONSUMER = (shaderCallback, pre, partialTick) -> {
        if (pre) {
            ShaderManager.useShader(ShaderManager.twilightSkyShader, shaderCallback);

            OpenGlHelper.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_STARS);

            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        } else {
            ShaderManager.releaseShader();

            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        }
    };

    // TODO There's got to be a better way!
    private static final TriConsumer<IntConsumer, Boolean, Float> FIREFLY_TRICONSUMER = (shaderCallback, pre, partialTick) -> {
        if (pre) ShaderManager.useShader(ShaderManager.fireflyShader, shaderCallback);
        else     ShaderManager.releaseShader();
    };

    private static final TriConsumer<IntConsumer, Boolean, Float> CARMINITE_TRICONSUMER = (shaderCallback, pre, partialTick) -> {
        if (pre) ShaderManager.useShader(ShaderManager.carminiteShader, shaderCallback);
        else ShaderManager.releaseShader();
    };

    private static final TriConsumer<IntConsumer, Boolean, Float> DEVICE_RED_ENERGY_TRICONSUMER = (shaderCallback, pre, partialTick) -> {
        if (pre) ShaderManager.useShader(ShaderManager.towerDeviceShader, shaderCallback);
        else     ShaderManager.releaseShader();
    };

    private static final TriConsumer<IntConsumer, Boolean, Float> DEVICE_YELLOW_ENERGY_TRICONSUMER = (shaderCallback, pre, partialTick) -> {
        if (pre) {
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            ShaderManager.useShader(ShaderManager.yellowCircuitShader, shaderCallback);
        } else {
            ShaderManager.releaseShader();
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
    };

    private static final TriConsumer<IntConsumer, Boolean, Float> AURORA_TRICONSUMER = (shaderCallback, pre, partialTick) -> {
        if (pre) ShaderManager.useShader(ShaderManager.auroraShader, shaderCallback);
        else     ShaderManager.releaseShader();
    };

    private static final TriConsumer<IntConsumer, Boolean, Float> RAM_TRICONSUMER = (shaderCallback, pre, partialTick) -> {
        ClientUtils.toggleLightmap(pre, true);
    };

    //private static final TriConsumer<IntConsumer, Boolean, Float> OUTLINE_TRICONSUMER = (shaderCallback, pre, partialTick) -> {
    //    if (pre) {
    //        GlStateManager.pushMatrix();
    //    } else {
    //        GlStateManager.popMatrix();
    //    }
    //    //if (pre) {
    //    //    //GlStateManager.pushMatrix();
    //    //    //GlStateManager.scale(1.05f, 1.05f, 1.05f);
    //    //    GlStateManager.enableCull();
    //    //    //GL11.glFrontFace(GL11.GL_CW);
    //    //    GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
    //    //    ShaderHelper.useShader(ShaderHelper.outlineShader, shaderCallback);
    //    //} else {
    //    //    ShaderHelper.releaseShader();
    //    //    GlStateManager.cullFace(GlStateManager.CullFace.BACK);
    //    //    //GL11.glFrontFace(GL11.GL_CCW);
    //    //    GlStateManager.disableCull();
    //    //    //GlStateManager.scale(0.8333f, 0.8333f, 0.8333f);
    //    //    //GlStateManager.popMatrix();
    //    //}
    //};

    // m Mod
    // t CaseType
    // s Suffix
    // c Color
    private static final ShaderLayerProvider LAYER_PROVIDER                 = (m, t, s, c) -> new ShaderCase.ShaderLayer( new ResourceLocation( m.provideTex(t, s) ), c );
    private static final ShaderLayerProvider TOWER_DEVICE_SHADER_PROVIDER   = (m, t, s, c) -> new ShaderConsumerLayer   ( new ResourceLocation( ModType.TWILIGHTFOREST.provideTex(t, "energy")), 0xFFFFFFFF, DEVICE_RED_ENERGY_TRICONSUMER, ShaderManager.Uniforms.STAR_UNIFORMS );
    private static final ShaderLayerProvider YELLOW_CIRCUIT_SHADER_PROVIDER = (m, t, s, c) -> new ShaderConsumerLayer   ( new ResourceLocation( ModType.IMMERSIVEENGINEERING.provideTex(t, "circuit")), 0xFF_BA_EE_02, DEVICE_YELLOW_ENERGY_TRICONSUMER, ShaderManager.Uniforms.STAR_UNIFORMS );

    // Registering
    private static List<ShaderRegistry.ShaderRegistryEntry> SHADERS;
    private static List<ShaderRegistry.ShaderRegistryEntry> NONBOSSES;

    private static final EnumRarity RARITY = TwilightForestMod.getRarity();

    static {
        if (RARITY != EnumRarity.EPIC)
            ShaderRegistry.rarityWeightMap.put(RARITY, 1);

        initShaders();
    }

    public static void initShaders() {
        NONBOSSES = ImmutableList.of(
                // MAIN COLOR, MINOR COLOR (EDGES), SECONDARY COLOR (GRIP, etc)
                registerShaderCases      ( "Twilight"            , ModType.TWILIGHTFOREST, "1_4"      , RARITY, 0xFF_4C_64_5B, 0xFF_28_25_3F, 0xFF_00_AA_00, 0xFF_FF_FF_FF, (m, t, s, c) -> new ShaderConsumerLayer( new ResourceLocation(m.provideTex(t, s)), 0xFFFFFFFF, TWILIGHT_TRICONSUMER , ShaderManager.Uniforms.STAR_UNIFORMS )                                                               ).setInfo("Twilight Forest", "Twilight Forest"          , "twilightforest"     ),
                registerShaderCases      ( "Firefly"             , ModType.TWILIGHTFOREST, "1_6"      , RARITY, 0xFF_66_41_40, 0xFF_F5_99_2F, 0xFF_C0_FF_00, 0xFF_C0_FF_00, LAYER_PROVIDER, (m, t, s, c) -> new ShaderConsumerLayer( new ResourceLocation(ModType.TWILIGHTFOREST.provideTex(t, "processed")), 0xFFFFFFFF, FIREFLY_TRICONSUMER  , ShaderManager.Uniforms.TIME_UNIFORM  )                ).setInfo("Twilight Forest", "Firefly"                  , "firefly"            ),
                registerShaderCases      ( "Pinch Beetle"        , ModType.TWILIGHTFOREST, "1_0"      , RARITY, 0xFF_BC_93_27, 0xFF_24_16_09, 0xFF_24_16_09, 0xFF_44_44_44, LAYER_PROVIDER, (m, t, s, c) -> new ShaderCase.ShaderLayer( new ResourceLocation( m.provideTex(t, "1_6" )), c )                                                                                                  ).setInfo("Twilight Forest", "Pinch Beetle"             , "pinch_beetle"       ),

                registerShaderCases      ( "Snakestone"          , ModType.TWILIGHTFOREST, "streaks"  , RARITY, 0xFF_9F_9F_9F, 0xFF_68_68_68, 0xFF_60_60_60, 0xFF_FF_FF_FF, LAYER_PROVIDER, (m, t, s, c) -> new ShaderCase.ShaderLayer( new ResourceLocation( ModType.TWILIGHTFOREST.provideTex(t, "scales") ), 0xFF_50_50_50 ), (m, t, s, c) -> new ShaderCase.ShaderLayer( new ResourceLocation( ModType.IMMERSIVEENGINEERING.provideTex(t, "circuit") ), 0xFF_58_58_58 ) ).setInfo("Twilight Forest", "Nagastone"                , "courtyard"          ),

                registerShaderCases      ( "Mazestone"           , ModType.TWILIGHTFOREST, "scales"   , RARITY, 0xFF_8E_99_8E, 0xFF_50_59_50, 0xFF_70_7B_70, 0xFF_FF_FF_FF, LAYER_PROVIDER                                                                                                                                                                                                   ).setInfo("Twilight Forest", "Mazestone"                , "mazestone"          ),

                registerShaderCases      ( "Underbrick"          , ModType.TWILIGHTFOREST, "scales"   , RARITY, 0xFF_85_68_45, 0xFF_76_7F_76, 0xFF_61_4D_33, 0xFF_FF_FF_FF, LAYER_PROVIDER                                                                                                                                                                                                   ).setInfo("Twilight Forest", "Underbrick"               , "underbrick"         ),
                registerShaderCasesTopped( "Towerwood"           , ModType.TWILIGHTFOREST, "1_0"      , RARITY, 0xFF_A6_65_3A, 0xFF_F5_DA_93, 0xFF_83_5A_35, 0xFF_FF_FF_FF, new ShaderLayerProvider<?>[]{ LAYER_PROVIDER, YELLOW_CIRCUIT_SHADER_PROVIDER }                                                                                                    , TOWER_DEVICE_SHADER_PROVIDER ).setInfo("Twilight Forest", "Towerwood Planks"         , "towerwood"          ),
                registerShaderCasesTopped( "Carminite"           , ModType.TWILIGHTFOREST, "carminite", RARITY, 0xFF_72_00_00, 0xFF_FF_00_00, 0xFF_FF_00_00, 0xFF_FF_00_00, new ShaderLayerProvider<?>[]{ (m, t, s, c) -> new ShaderConsumerLayer( new ResourceLocation(m.provideTex(t, s)), 0xFFFFFFFF, CARMINITE_TRICONSUMER, ShaderManager.Uniforms.STAR_UNIFORMS ) }, TOWER_DEVICE_SHADER_PROVIDER ).setInfo("Twilight Forest", "Carminite"                , "carminite"          ),

                registerShaderCases      ( "Auroralized"         , ModType.TWILIGHTFOREST, "1_5"      , RARITY, 0xFF_00_FF_FF, 0xFF_00_FF_00, 0xFF_00_00_FF, 0xFF_FF_FF_FF, (m, t, s, c) -> new ShaderConsumerLayer( new ResourceLocation(m.provideTex(t, s)), 0xFFFFFFFF, AURORA_TRICONSUMER   , ShaderManager.Uniforms.TIME_UNIFORM  )                                                               ).setInfo("Twilight Forest", "Aurora Palace"            , "aurora"             ),

                registerShaderCases      ( "Ironwood"            , ModType.TWILIGHTFOREST, "1_0"      , RARITY, 0xFF_6B_61_61, 0xFF_5F_4D_40, 0xFF_5E_57_4B, 0xFF_FF_FF_FF, (m, t, s, c) -> new ShaderCase.ShaderLayer( new ResourceLocation( ModType.TWILIGHTFOREST.provideTex(t, "streaks") ), 0xFF_79_7C_43 ), LAYER_PROVIDER                                                             ).setInfo("Twilight Forest", "Ironwood"                 , "ironwood"           ),
                registerShaderCases      ( "Steeleaf"            , ModType.TWILIGHTFOREST, "1_0"      , RARITY, 0xFF_52_87_3A, 0xFF_1E_32_14, 0xFF_41_62_30, 0xFF_FF_FF_FF, (m, t, s, c) -> new ShaderCase.ShaderLayer( new ResourceLocation( ModType.IMMERSIVEENGINEERING.provideTex(t, "1_4") ), 0xFF_41_62_30 ), (m, t, s, c) -> new ShaderCase.ShaderLayer( new ResourceLocation( ModType.TWILIGHTFOREST.provideTex(t, "streaks") ), 0xFF_6D_A2_5E ), LAYER_PROVIDER ).setInfo("Twilight Forest", "Steeleaf"                 , "steeleaf"           ),
                registerShaderCases      ( "Knightly"            , ModType.TWILIGHTFOREST, "1_0"      , RARITY, 0xFF_E7_FC_CD, 0xFF_4D_4C_4B, 0xFF_80_8C_72, 0xFF_FF_FF_FF, LAYER_PROVIDER                                                                                                                                                                                                   ).setInfo("Twilight Forest", "Knightly"                 , "knightly"           ),
                registerShaderCases      ( "Fiery"               , ModType.TWILIGHTFOREST, "1_0"      , RARITY, 0xFF_19_13_13, 0xFF_FD_D4_5D, 0xFF_77_35_11, 0xFF_FF_FF_FF, LAYER_PROVIDER                                                                                                                                                                                                   ).setInfo("Twilight Forest", "Fiery"                    , "fiery"              ),

                registerShaderCases      ( "Final Castle"        , ModType.TWILIGHTFOREST, "scales"   , RARITY, 0xFF_EC_EA_E6, 0xFF_C8_BB_BC, 0xFF_00_FF_FF, 0xFF_00_FF_FF, LAYER_PROVIDER                                                                                                                                                                                                   ).setInfo("Twilight Forest", "Final Castle"             , "finalcastle"        ),
                // TODO Throbbing effect
                //registerShaderCases      ( "Cube of Annihilation", ModType.TWILIGHTFOREST, "1_0"      , RARITY, 0xFF_00_00_03, 0xFF_14_00_40, 0xFF_00_00_03, 0xFF_14_00_40, (m, t, s, c) -> new ShaderConsumerLayer( new ResourceLocation(m.provideTex(t, s)), 0xFF_14_00_40, OUTLINE_TRICONSUMER, ShaderHelper.TIME_UNIFORM )                                                             ).setInfo("Twilight Forest", "Cube of Annilation"       , "cube_of_annilation" )
                registerShaderCases      ( "Cube of Annihilation", ModType.TWILIGHTFOREST, "1_0"      , RARITY, 0xFF_00_00_03, 0xFF_14_00_40, 0xFF_00_00_03, 0xFF_14_00_40, (m, t, s, c) -> new ShaderCase.ShaderLayer( new ResourceLocation(m.provideTex(t, s)), 0xFF_14_00_40 )                                                                                                            ).setInfo("Twilight Forest", "Cube of Annilation"       , "cube_of_annilation" )
        );

        ImmutableList.Builder<ShaderRegistry.ShaderRegistryEntry> listBuilder = ImmutableList.builder();

        listBuilder.addAll(NONBOSSES);

        listBuilder.add(
                registerShaderCases      ( "Questing Ram"        , ModType.TWILIGHTFOREST, "streaks"  , RARITY, 0xFF_F9_E1_C8, 0xFF_9A_85_69, 0xFF_2F_2B_36, 0xFF_90_D8_EF, LAYER_PROVIDER, (m, t, s, c) -> new ShaderConsumerLayer( new ResourceLocation( ModType.IMMERSIVEENGINEERING.provideTex(t, "circuit" )), 0x30_90_D8_EF, RAM_TRICONSUMER )                                         ).setInfo("Twilight Forest", "Questing Ram"             , "questing_ram"       ),

                registerShaderCases      ( "Naga"                , ModType.TWILIGHTFOREST, "scales"   , RARITY, 0xFF_32_5D_25, 0xFF_17_29_11, 0xFF_A5_D4_16, 0xFF_FF_FF_FF, LAYER_PROVIDER, (m, t, s, c) -> new ShaderCase.ShaderLayer( new ResourceLocation( ModType.IMMERSIVEENGINEERING.provideTex(t, "shark") ), 0xFF_FF_FF_FF )                                                         ).setInfo("Twilight Forest", "Naga Boss"                , "naga"               ),
                registerShaderCases      ( "Lich"                , ModType.TWILIGHTFOREST, "1_0"      , RARITY, 0xFF_DF_D9_CC, 0xFF_C3_9C_00, 0xFF_3A_04_75, 0xFF_FF_FF_FF, LAYER_PROVIDER                                                                                                                                                                                                   ).setInfo("Twilight Forest", "Twilight Lich Boss"       , "lich"               ),

                registerShaderCases      ( "Minoshroom"          , ModType.TWILIGHTFOREST, "1_6"      , RARITY, 0xFF_A8_10_12, 0xFF_B3_B3_B3, 0xFF_33_EB_CB, 0xFF_FF_FF_FF, LAYER_PROVIDER                                                                                                                                                                                                   ).setInfo("Twilight Forest", "Minoshroom Miniboss"      , "minoshroom"         ),
                registerShaderCases      ( "Hydra"               , ModType.TWILIGHTFOREST, "scales"   , RARITY, 0xFF_14_29_40, 0xFF_29_80_6B, 0xFF_F1_0A_92, 0xFF_FF_FF_FF, LAYER_PROVIDER, (m, t, s, c) -> new ShaderCase.ShaderLayer( new ResourceLocation( ModType.IMMERSIVEENGINEERING.provideTex(t, "shark") ), 0xFF_FF_FF_FF )                                                         ).setInfo("Twilight Forest", "Hydra Boss"               , "hydra"              ),

                registerShaderCases      ( "Knight Phantom"      , ModType.TWILIGHTFOREST, "1_0"      , RARITY, 0xCC_40_6D_05, 0xFF_36_35_34, 0xFF_7A_5C_49, 0xFF_FF_FF_FF, LAYER_PROVIDER                                                                                                                                                                                                   ).setInfo("Twilight Forest", "Knight Phantom Minibosses", "knight_phantom"     ),
                registerShaderCases      ( "Ur-Ghast"            , ModType.TWILIGHTFOREST, "1_2"      , RARITY, 0xFF_F9_F9_F9, 0xFF_9A_37_37, 0xFF_56_56_56, 0xFF_FF_FF_FF, LAYER_PROVIDER                                                                                                                                                                                                   ).setInfo("Twilight Forest", "Ur-Ghast"                 , "ur-ghast"           ),

                registerShaderCases      ( "Alpha Yeti"          , ModType.TWILIGHTFOREST, "1_0"      , RARITY, 0xFF_FC_FC_FC, 0xFF_4A_80_CE, 0xFF_25_3F_66, 0xFF_FF_FF_FF, LAYER_PROVIDER                                                                                                                                                                                                   ).setInfo("Twilight Forest", "Alpha Yeti"               , "alpha_yeti"         ),
                registerShaderCases      ( "Snow Queen"          , ModType.TWILIGHTFOREST, "1_0"      , RARITY, 0xFF_DC_FB_FF, 0xFF_C3_9C_00, 0xFF_03_05_89, 0xFF_FF_FF_FF, LAYER_PROVIDER                                                                                                                                                                                                   ).setInfo("Twilight Forest", "Snow Queen"               , "snow_queen"         )
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
    private static class ShaderConsumerLayer extends ShaderCase.DynamicShaderLayer {
        private final TriConsumer<IntConsumer, Boolean, Float> render;
        private final IntConsumer shaderCallback;

        ShaderConsumerLayer(ResourceLocation texture, int colour, TriConsumer<IntConsumer, Boolean, Float> render, ShaderUniform... shaderParams) {
            super(texture, colour);
            this.render = render;

            shaderCallback = shader -> { for(ShaderUniform param: shaderParams) { param.assignUniform(shader); } };
        }

        @Override
        public void modifyRender(boolean pre, float partialTick) {
            this.render.accept(shaderCallback, pre, partialTick);
        }
    }

    @SafeVarargs
    private static ShaderRegistry.ShaderRegistryEntry registerShaderCasesTopped(String name, ModType mod, String overlayType, EnumRarity rarity, int bodyColor, int colorSecondary, int gripColor, int colorBlade, ShaderLayerProvider<? extends ShaderCase.ShaderLayer>[] providers, ShaderLayerProvider<? extends ShaderCase.ShaderLayer>... extraProviders) {
        ShaderRegistry.registerShader_Item(name, rarity, gripColor, bodyColor, colorSecondary);

        registerShaderCaseRevolver   ( name, gripColor, bodyColor, colorBlade, rarity, provideFromProviders( mod, CaseType.REVOLVER        , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.REVOLVER        , overlayType          , colorSecondary, extraProviders ));
        registerShaderCaseChemthrower( name, gripColor, bodyColor,             rarity, provideFromProviders( mod, CaseType.CHEMICAL_THROWER, overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.CHEMICAL_THROWER, overlayType          , colorSecondary, extraProviders ));
        registerShaderCaseDrill      ( name, gripColor, bodyColor, colorBlade, rarity, provideFromProviders( mod, CaseType.DRILL           , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.DRILL           , overlayType          , colorSecondary, extraProviders ));
        registerShaderCaseRailgun    ( name, gripColor, bodyColor,             rarity, provideFromProviders( mod, CaseType.RAILGUN         , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.RAILGUN         , overlayType          , colorSecondary, extraProviders ));
        registerShaderCaseShield     ( name, gripColor, bodyColor,             rarity, provideFromProviders( mod, CaseType.SHIELD          , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.SHIELD          , overlayType          , colorSecondary, extraProviders ));
        registerShaderCaseMinecart   ( name, gripColor, bodyColor,             rarity, provideFromProviders( mod, CaseType.MINECART        , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.MINECART        , overlayType + ".png" , colorSecondary, extraProviders ));
        registerShaderCaseBalloon    ( name, gripColor, bodyColor,             rarity, provideFromProviders( mod, CaseType.BALLOON         , overlayType, colorSecondary, providers ), provideFromProviders( mod, CaseType.BALLOON         , overlayType          , colorSecondary, extraProviders ));

        // Since shaders won't occur in a way we'd like them to, we should register any additional variants ourselves if we know of any
        for(ShaderRegistry.IShaderRegistryMethod method : ShaderRegistry.shaderRegistrationMethods)
            method.apply(name, overlayType, rarity, gripColor, bodyColor, colorSecondary, colorBlade, null, 0);

        return ShaderRegistry.shaderRegistry.get(name).setCrateLoot(false).setBagLoot(false).setInLowerBags(false).setReplicationCost(new IngredientStack(new ItemStack(TFItems.ore_meter)));
    }

    @SafeVarargs
    private static ShaderRegistry.ShaderRegistryEntry registerShaderCases(String name, ModType type, String overlayType, EnumRarity rarity, int bodyColor, int colorSecondary, int gripColor, int colorBlade, ShaderLayerProvider<? extends ShaderCase.ShaderLayer>... providers) {
        return registerShaderCasesTopped(name, type, overlayType, rarity, bodyColor, colorSecondary, gripColor, colorBlade, providers);
    }

    // Shader Case Registration helpers
    @FunctionalInterface
    private interface ShaderLayerProvider<T extends ShaderCase.ShaderLayer> {
        T get(ModType mod, CaseType type, String suffix, int color);
    }

    private static ShaderCase.ShaderLayer[] provideFromProviders( ModType mod, CaseType type, String suffix, int color, ShaderLayerProvider<? extends ShaderCase.ShaderLayer>[] layerProviders) {
        ShaderCase.ShaderLayer[] array = new ShaderCase.ShaderLayer[layerProviders.length];

        for (int i = 0; i < layerProviders.length; i++)
            array[i] = layerProviders[i].get( mod, type, suffix, color );

        return array;
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseRevolver registerShaderCaseRevolver(String name, int gripColor, int bodyColor, int bladeColor, EnumRarity rarity, ShaderCase.ShaderLayer[] additionalLayers, ShaderCase.ShaderLayer... topLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new ShaderCaseRevolver(
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer(PROCESSED_REVOLVER_GRIP_LAYER, gripColor),
                        new ShaderCase.ShaderLayer(PROCESSED_REVOLVER_LAYER, bodyColor),
                        new ShaderCase.ShaderLayer(PROCESSED_REVOLVER_LAYER, bladeColor)
                ).add(additionalLayers).add(UNCOLORED_REVOLVER_LAYER).add(topLayers).build()), rarity);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseChemthrower registerShaderCaseChemthrower(String name, int gripColor, int bodyColor, EnumRarity rarity, ShaderCase.ShaderLayer[] additionalLayers, ShaderCase.ShaderLayer... topLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new TFShaderCaseChemthrower( 3 + additionalLayers.length,
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer(PROCESSED_CHEMTHROW_LAYER, gripColor),
                        new ShaderCase.ShaderLayer(PROCESSED_CHEMTHROW_LAYER, bodyColor)
                ).add(additionalLayers).add(UNCOLORED_CHEMTHROW_LAYER).add(topLayers).build()), rarity);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseDrill registerShaderCaseDrill(String name, int gripColor, int bodyColor, int bladeColor, EnumRarity rarity, ShaderCase.ShaderLayer[] additionalLayers, ShaderCase.ShaderLayer... topLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new TFShaderCaseDrill( 5 + additionalLayers.length,
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer(PROCESSED_DRILL_LAYER, gripColor),
                        new ShaderCase.ShaderLayer(PROCESSED_DRILL_LAYER, bodyColor)
                ).add(UNCOLORED_DRILL_LAYER).add(additionalLayers).add(UNCOLORED_DRILL_LAYER).add(NULL_LAYER).add(topLayers).build()), rarity);//.addHeadLayers(new ShaderCase.ShaderLayer(new ResourceLocation("immersiveengineering", "items/drill_iron"), bladeColor));
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseRailgun registerShaderCaseRailgun(String name, int gripColor, int bodyColor, EnumRarity rarity, ShaderCase.ShaderLayer[] additionalLayers, ShaderCase.ShaderLayer... topLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new TFShaderCaseRailgun( 3 + additionalLayers.length,
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer(PROCESSED_RAILGUN_LAYER, gripColor),
                        new ShaderCase.ShaderLayer(PROCESSED_RAILGUN_LAYER, bodyColor)
                ).add(additionalLayers).add(UNCOLORED_RAILGUN_LAYER).add(topLayers).build()), rarity);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseShield registerShaderCaseShield(String name, int gripColor, int bodyColor, EnumRarity rarity, ShaderCase.ShaderLayer[] additionalLayers, ShaderCase.ShaderLayer... topLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new TFShaderCaseShield( 3 + additionalLayers.length,
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer(PROCESSED_SHIELD_LAYER, gripColor),
                        new ShaderCase.ShaderLayer(PROCESSED_SHIELD_LAYER, bodyColor)
                ).add(additionalLayers).add(UNCOLORED_SHIELD_LAYER).add(topLayers).build()), rarity);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseMinecart registerShaderCaseMinecart(String name, int bodyColor, int secondaryColor, EnumRarity rarity, ShaderCase.ShaderLayer[] additionalLayers, ShaderCase.ShaderLayer... topLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new ShaderCaseMinecart(
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer( new ResourceLocation( "immersiveengineering", "textures/models/shaders/minecart_0.png"   ), bodyColor),
                        new ShaderCase.ShaderLayer( new ResourceLocation( "immersiveengineering", "textures/models/shaders/minecart_1_0.png" ), secondaryColor)
                ).add(additionalLayers).add(UNCOLORED_MINECART_LAYER).add(topLayers).build()), rarity);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseBalloon registerShaderCaseBalloon(String name, int gripColor, int bodyColor, EnumRarity rarity, ShaderCase.ShaderLayer[] additionalLayers, ShaderCase.ShaderLayer... topLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new ShaderCaseBalloon(
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer(PROCESSED_BALLOON_LAYER, gripColor),
                        new ShaderCase.ShaderLayer(PROCESSED_BALLOON_LAYER, bodyColor)
                ).add(additionalLayers).add(UNCOLORED_BALLOON_LAYER).add(topLayers).build()), rarity);
    }

    public enum ModType {
        IMMERSIVEENGINEERING {
            @Override
            public String provideTex(CaseType caseType, String suffix) {
                switch (caseType) {
                    case REVOLVER:
                        return "immersiveengineering:revolvers/shaders/revolver_" + suffix;
                    case CHEMICAL_THROWER:
                        return "immersiveengineering:items/shaders/chemthrower_" + suffix;
                    case DRILL:
                        return "immersiveengineering:items/shaders/drill_diesel_" + suffix;
                    case RAILGUN:
                        return "immersiveengineering:items/shaders/railgun_" + suffix;
                    case SHIELD:
                        return "immersiveengineering:items/shaders/shield_" + suffix;
                    case MINECART:
                        return "immersiveengineering:textures/models/shaders/minecart_" + suffix + ".png";
                    case BALLOON:
                        return "immersiveengineering:blocks/shaders/balloon_" + suffix;
                }

                return "";
            }
        },
        TWILIGHTFOREST {
            @Override
            public String provideTex(CaseType caseType, String suffix) {
                switch (caseType) {
                    case REVOLVER:
                        return "twilightforest:items/immersiveengineering/revolver_" + suffix;
                    case CHEMICAL_THROWER:
                        return "twilightforest:items/immersiveengineering/chemthrower_" + suffix;
                    case DRILL:
                        return "twilightforest:items/immersiveengineering/drill_" + suffix;
                    case RAILGUN:
                        return "twilightforest:items/immersiveengineering/railgun_" + suffix;
                    case SHIELD:
                        return "twilightforest:items/immersiveengineering/shield_" + suffix;
                    case MINECART:
                        if (suffix.startsWith("1_"))
                            return IMMERSIVEENGINEERING.provideTex(caseType, suffix);
                        return "twilightforest:textures/model/immersiveengineering/minecart_" + suffix + ".png";
                    case BALLOON:
                        return "twilightforest:blocks/immersiveengineering/balloon_" + suffix;
                }

                return "";
            }
        };

        public abstract String provideTex(CaseType caseType, String suffix);
    }

    public enum CaseType {
        REVOLVER,
        CHEMICAL_THROWER,
        DRILL,
        RAILGUN,
        SHIELD,
        MINECART,
        BALLOON;

        public static CaseType[] everythingButMinecart() {
            return new CaseType[]{ REVOLVER, CHEMICAL_THROWER, DRILL, RAILGUN, SHIELD, BALLOON };
        }
    }
}
