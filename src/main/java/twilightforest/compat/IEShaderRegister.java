package twilightforest.compat;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.shader.*;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.util.TriConsumer;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.TFClientEvents;
import twilightforest.client.shader.ShaderHelper;
import twilightforest.client.shader.ShaderUniform;
import twilightforest.item.TFItems;

import java.util.List;
import java.util.function.IntConsumer;

public class IEShaderRegister {
    // Layer Constants
    private static final ShaderCase.ShaderLayer nullLayer = new ShaderCase.ShaderLayer( null, 0xFF_FF_FF_FF);
    private static final ShaderCase.ShaderLayer uncoloredRevolverLayer  = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "revolvers/shaders/revolver_uncoloured"           ), 0xFF_FF_FF_FF);
    private static final ShaderCase.ShaderLayer uncoloredChemthrowLayer = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "items/shaders/chemthrower_uncoloured"            ), 0xFF_FF_FF_FF);
    private static final ShaderCase.ShaderLayer uncoloredDrillLayer     = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "items/shaders/drill_diesel_uncoloured"           ), 0xFF_FF_FF_FF);
    private static final ShaderCase.ShaderLayer uncoloredRailgunLayer   = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "items/shaders/railgun_uncoloured"                ), 0xFF_FF_FF_FF);
    private static final ShaderCase.ShaderLayer uncoloredShieldLayer    = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "items/shaders/shield_uncoloured"                 ), 0xFF_FF_FF_FF);
    private static final ShaderCase.ShaderLayer uncoloredMinecartLayer  = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "textures/models/shaders/minecart_uncoloured.png" ), 0xFF_FF_FF_FF);
    private static final ShaderCase.ShaderLayer uncoloredBalloonLayer   = new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "blocks/shaders/balloon_uncoloured"               ), 0xFF_FF_FF_FF);

    public static final ResourceLocation processedRevolverGripLayer = new ResourceLocation("twilightforest", "items/immersiveengineering/revolver_grip_processed");
    public static final ResourceLocation processedRevolverLayer     = new ResourceLocation("twilightforest", "items/immersiveengineering/revolver_processed");
    public static final ResourceLocation processedChemthrowLayer    = new ResourceLocation("twilightforest", "items/immersiveengineering/chemthrower_processed");
    public static final ResourceLocation processedDrillLayer        = new ResourceLocation("twilightforest", "items/immersiveengineering/drill_processed");
    public static final ResourceLocation processedRailgunLayer      = new ResourceLocation("twilightforest", "items/immersiveengineering/railgun_processed");
    public static final ResourceLocation processedShieldLayer       = new ResourceLocation("twilightforest", "items/immersiveengineering/shield_processed");
    //public static final ResourceLocation processedMinecartLayer     = new ResourceLocation("twilightforest", "textures/items/immersiveengineering/minecart_processed.png");
    public static final ResourceLocation processedBalloonLayer      = new ResourceLocation("twilightforest", "items/immersiveengineering/balloon_processed");

    // Shader Constants
    private static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    private static final ResourceLocation TEXTURE_STARS = new ResourceLocation("textures/entity/end_portal.png");

    private static final TriConsumer<IntConsumer, Boolean, Float> TWILIGHT_TRICONSUMER = (shaderCallback, pre, partialTick) -> {
        if (pre) {
            //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            ShaderHelper.useShader(ShaderHelper.twilightSkyShader, shaderCallback);

            //OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            //MINECRAFT.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            OpenGlHelper.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
            MINECRAFT.getTextureManager().bindTexture(TEXTURE_STARS);
        } else {
            ShaderHelper.releaseShader();

            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            MINECRAFT.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
    };

    private static final TriConsumer<IntConsumer, Boolean, Float> AURORA_TRICONSUMER = (shaderCallback, pre, partialTick) -> {
        if (pre) {
            //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            ShaderHelper.useShader(ShaderHelper.auroraShader, shaderCallback);
        } else {
            ShaderHelper.releaseShader();
            //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
    };

    // Registering
    private static List<ShaderRegistry.ShaderRegistryEntry> SHADERS;

    private static IResourceManagerReloadListener shaderReloadListener;

    private static final EnumRarity RARITY = TwilightForestMod.getRarity();

    static {
        if (RARITY != EnumRarity.EPIC)
            ShaderRegistry.rarityWeightMap.put(RARITY, 1);

        IResourceManager iManager;

        // Call this a bad idea, but it works for reloadability for now
        if ((iManager = MINECRAFT.getResourceManager()) instanceof SimpleReloadableResourceManager) ((SimpleReloadableResourceManager) iManager).registerReloadListener(shaderReloadListener = (manager -> SHADERS = ImmutableList.of(
                // MAIN COLOR, MINOR COLOR (EDGES), SECONDARY COLOR (GRIP, etc)
                registerWithShaders          ("Twilight"      , "4"     , RARITY, 0xFF_4C_64_5B, 0xFF_28_25_3F, 0xFF_00_AA_00, 0xFF_FF_FF_FF, TWILIGHT_TRICONSUMER, ShaderHelper.STAR_UNIFORMS ).setInfo("Twilight Forest", "Twilight Forest"          , "twilightforest"),

                ShaderRegistry.registerShader("Snakestone"    , "0"     , RARITY, 0xFF_80_80_80, 0xFF_80_80_80, 0xFF_80_80_80, 0xFF_FF_FF_FF, null, 0, false, true                             ).setInfo("Twilight Forest", "Nagastone"                , "courtyard"     ),
                registerWithTwilightType     ("Naga"          , "scales", RARITY, 0xFF_32_5D_25, 0xFF_17_29_11, 0xFF_A5_D4_16, 0xFF_FF_FF_FF                                                   ).setInfo("Twilight Forest", "Naga Boss"                , "naga"          ),
                ShaderRegistry.registerShader("Lich"          , "0"     , RARITY, 0xFF_DF_D9_CC, 0xFF_C3_9C_00, 0xFF_3A_04_75, 0xFF_FF_FF_FF, null, 0, false, true                             ).setInfo("Twilight Forest", "Twilight Lich Boss"       , "lich"          ),

                ShaderRegistry.registerShader("Mazestone"     , "0"     , RARITY, 0xFF_70_7B_70, 0xFF_8E_99_8E, 0xFF_65_6E_65, 0xFF_FF_FF_FF, null, 0, false, true                             ).setInfo("Twilight Forest", "Mazestone"                , "mazestone"     ),
                ShaderRegistry.registerShader("Minoshroom"    , "0"     , RARITY, 0xFF_A8_10_12, 0xFF_B3_B3_B3, 0xFF_33_EB_CB, 0xFF_FF_FF_FF, null, 0, false, true                             ).setInfo("Twilight Forest", "Minoshroom Miniboss"      , "minoshroom"    ),
                registerWithTwilightType     ("Hydra"         , "scales", RARITY, 0xFF_14_29_40, 0xFF_29_80_6B, 0xFF_F1_0A_92, 0xFF_FF_FF_FF                                                   ).setInfo("Twilight Forest", "Hydra Boss"               , "hydra"         ),

                registerWithTwilightType     ("Underbrick"    , "scales", RARITY, 0xFF_85_68_45, 0xFF_76_7F_76, 0xFF_61_4D_33, 0xFF_FF_FF_FF                                                   ).setInfo("Twilight Forest", "Underbrick"               , "underbrick"    ),
                ShaderRegistry.registerShader("Knight Phantom", "0"     , RARITY, 0xCC_40_6D_05, 0xFF_36_35_34, 0xFF_7A_5C_49, 0xFF_FF_FF_FF, null, 0, false, true                             ).setInfo("Twilight Forest", "Knight Phantom Minibosses", "knight_phantom"),
                ShaderRegistry.registerShader("Towerwood"     , "0"     , RARITY, 0xFF_83_5A_35, 0xFF_F5_DA_93, 0xFF_A6_65_3A, 0xFF_FF_FF_FF, null, 0, false, true                             ).setInfo("Twilight Forest", "Towerwood Planks"         , "towerwood"     ),
                ShaderRegistry.registerShader("Ur-Ghast"      , "2"     , RARITY, 0xFF_F9_F9_F9, 0xFF_9A_37_37, 0xFF_56_56_56, 0xFF_FF_FF_FF, null, 0, false, true                             ).setInfo("Twilight Forest", "Ur-Ghast"                 , "ur-ghast"      ),

                ShaderRegistry.registerShader("Alpha Yeti"    , "0"     , RARITY, 0xFF_FC_FC_FC, 0xFF_4A_80_CE, 0xFF_25_3F_66, 0xFF_FF_FF_FF, null, 0, false, true                             ).setInfo("Twilight Forest", "Alpha Yeti"               , "alpha_yeti"    ),
                registerWithShaders          ("Auroralized"   , "5"     , RARITY, 0xFF_00_FF_FF, 0xFF_00_FF_FF, 0xFF_00_00_FF, 0xFF_FF_FF_FF, AURORA_TRICONSUMER, ShaderHelper.AURORA_UNIFORMS ).setInfo("Twilight Forest", "Aurora Palace"            , "aurora"        ),
                ShaderRegistry.registerShader("Snow Queen"    , "0"     , RARITY, 0xFF_DC_FB_FF, 0xFF_C3_9C_00, 0xFF_03_05_89, 0xFF_FF_FF_FF, null, 0, false, true                             ).setInfo("Twilight Forest", "Snow Queen"               , "snow_queen"    ),

                ShaderRegistry.registerShader("Knightly"      , "0"     , RARITY, 0xFF_E7_FC_CD, 0xFF_4D_4C_4B, 0xFF_80_8C_72, 0xFF_FF_FF_FF, null, 0, false, true                             ).setInfo("Twilight Forest", "Knightly"                 , "knightly"      ),
                ShaderRegistry.registerShader("Fiery"         , "0"     , RARITY, 0xFF_19_13_13, 0xFF_FD_D4_5D, 0xFF_77_35_11, 0xFF_FF_FF_FF, null, 0, false, true                             ).setInfo("Twilight Forest", "Fiery"                    , "fiery"         ),

                registerWithTwilightType     ("Final Castle"  , "scales", RARITY, 0xFF_EC_EA_E6, 0xFF_C8_BB_BC, 0xFF_00_FF_FF, 0xFF_00_FF_FF                                                   ).setInfo("Twilight Forest", "Final Castle"             , "finalcastle"   )
        )));
    }

    public static List<ShaderRegistry.ShaderRegistryEntry> getAllTwilightShaders() {
        return SHADERS;
    }

    public static IResourceManagerReloadListener getShaderReloadListener() {
        return shaderReloadListener;
    }

    // Shaderizing!
    private static class ShaderConsumerShaderLayer extends ShaderCase.DynamicShaderLayer {
        private final TriConsumer<IntConsumer, Boolean, Float> render;
        private final IntConsumer shaderCallback;

        ShaderConsumerShaderLayer(ResourceLocation texture, int colour, TriConsumer<IntConsumer, Boolean, Float> render, ShaderUniform... shaderParams) {
            super(texture, colour);
            this.render = render;

            shaderCallback = shader -> { for(ShaderUniform param: shaderParams) { param.assignUniform(shader); } };
        }

        @Override
        public void modifyRender(boolean pre, float partialTick) {
            this.render.accept(shaderCallback, pre, partialTick);
        }
    }

    private static ShaderRegistry.ShaderRegistryEntry registerWithTwilightType(String name, String overlayType, EnumRarity rarity, int colourPrimary, int colourSecondary, int colourBackground, int colourBlade) {
        //if (true) return ShaderRegistry.registerShader(name, "4", rarity, colourPrimary, colourSecondary, colourBackground, colourBlade, null, 0, false, true).setInLowerBags(false);

        ShaderRegistry.registerShader_Item(name, rarity, colourBackground, colourPrimary, colourSecondary);

        registerShaderCaseRevolver( name, colourBackground, colourPrimary, colourBlade, rarity,
                new ShaderCase.ShaderLayer( new ResourceLocation("twilightforest", "items/immersiveengineering/revolver_" + overlayType ), colourSecondary )
        );

        registerShaderCaseChemthrower( name, colourBackground, colourPrimary, rarity,
                new ShaderCase.ShaderLayer( new ResourceLocation("twilightforest", "items/immersiveengineering/chemthrower_" + overlayType ), colourSecondary )
        );

        registerShaderCaseDrill( name, colourBackground, colourPrimary, rarity,
                new ShaderCase.ShaderLayer( new ResourceLocation("twilightforest", "items/immersiveengineering/drill_" + overlayType ), colourSecondary )
        );

        registerShaderCaseRailgun( name, colourBackground, colourPrimary, rarity,
                new ShaderCase.ShaderLayer( new ResourceLocation("twilightforest", "items/immersiveengineering/railgun_" + overlayType ), colourSecondary )
        );

        registerShaderCaseShield( name, colourBackground, colourPrimary, rarity,
                new ShaderCase.ShaderLayer( new ResourceLocation("twilightforest", "items/immersiveengineering/shield_" + overlayType ), colourSecondary )
        );

        registerShaderCaseMinecart( name, colourBackground, colourPrimary, rarity,
                new ShaderCase.ShaderLayer( new ResourceLocation("twilightforest", "textures/items/immersiveengineering/minecart_" + overlayType + ".png"), colourSecondary )
        );

        registerShaderCaseBalloon( name, colourBackground, colourPrimary, rarity,
                new ShaderCase.ShaderLayer( new ResourceLocation("twilightforest", "blocks/shaders/balloon_" + overlayType ), colourSecondary )
        );

        for(ShaderRegistry.IShaderRegistryMethod method : ShaderRegistry.shaderRegistrationMethods)
            method.apply(name, overlayType, rarity, colourBackground, colourPrimary, colourSecondary, colourBlade, null, 0);

        return ShaderRegistry.shaderRegistry.get(name).setCrateLoot(false).setBagLoot(true).setInLowerBags(false).setReplicationCost(new IngredientStack(new ItemStack(TFItems.ore_meter)));
    }

    private static ShaderRegistry.ShaderRegistryEntry registerWithShaders(String name, String overlayType, EnumRarity rarity, int colourPrimary, int colourSecondary, int colourBackground, int colourBlade, TriConsumer<IntConsumer, Boolean, Float> render, ShaderUniform... shaderParams) {
        //if (true) return ShaderRegistry.registerShader(name, "4", rarity, colourPrimary, colourSecondary, colourBackground, colourBlade, null, 0, false, true).setInLowerBags(false);

        ShaderRegistry.registerShader_Item(name, rarity, colourBackground, colourPrimary, colourSecondary);

        registerShaderCaseRevolver( name, colourBackground, colourPrimary, colourBlade, rarity,
                new ShaderConsumerShaderLayer( new ResourceLocation("immersiveengineering", "revolvers/shaders/revolver_1_" + overlayType ), 0xFF_FF_FF_FF, render, shaderParams)
        );

        registerShaderCaseChemthrower( name, colourBackground, colourPrimary, rarity,
                new ShaderConsumerShaderLayer( new ResourceLocation("immersiveengineering", "items/shaders/chemthrower_1_" + overlayType ), 0xFF_FF_FF_FF, render, shaderParams)
        );

        registerShaderCaseDrill( name, colourBackground, colourPrimary, rarity,
                new ShaderConsumerShaderLayer( new ResourceLocation("immersiveengineering", "items/shaders/drill_diesel_1_" + overlayType ), 0xFF_FF_FF_FF, render, shaderParams)
        );

        registerShaderCaseRailgun( name, colourBackground, colourPrimary, rarity,
                new ShaderConsumerShaderLayer( new ResourceLocation("immersiveengineering", "items/shaders/railgun_1_" + overlayType ), 0xFF_FF_FF_FF, render, shaderParams)
        );

        registerShaderCaseShield( name, colourBackground, colourPrimary, rarity,
                new ShaderConsumerShaderLayer( new ResourceLocation("immersiveengineering", "items/shaders/shield_1_" + overlayType ), 0xFF_FF_FF_FF, render, shaderParams)
        );

        registerShaderCaseMinecart( name, colourBackground, colourPrimary, rarity,
                new ShaderCase.ShaderLayer   ( new ResourceLocation("immersiveengineering", "textures/models/shaders/minecart_1_5.png"), colourSecondary),
                new ShaderConsumerShaderLayer( new ResourceLocation("immersiveengineering", "textures/models/shaders/minecart_1_" + overlayType + ".png"), 0xFF_FF_FF_FF, render, shaderParams)
        );

        registerShaderCaseBalloon( name, colourBackground, colourPrimary, rarity,
                new ShaderConsumerShaderLayer( new ResourceLocation("immersiveengineering", "blocks/shaders/balloon_1_" + overlayType ), 0xFF_FF_FF_FF, render, shaderParams)
        );

        for(ShaderRegistry.IShaderRegistryMethod method : ShaderRegistry.shaderRegistrationMethods)
            method.apply(name, overlayType, rarity, colourBackground, colourPrimary, colourSecondary, colourBlade, null, 0);

        return ShaderRegistry.shaderRegistry.get(name).setCrateLoot(false).setBagLoot(true).setInLowerBags(false).setReplicationCost(new IngredientStack(new ItemStack(TFItems.ore_meter)));
    }

    // Shader Case Registration helpers

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseRevolver registerShaderCaseRevolver(String name, int gripColor, int mainColor, int bladeColor, EnumRarity rarity, ShaderCase.ShaderLayer... additionalLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new ShaderCaseRevolver(
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer( processedRevolverGripLayer, gripColor),
                        new ShaderCase.ShaderLayer( processedRevolverLayer    , mainColor),
                        new ShaderCase.ShaderLayer( processedRevolverLayer    , bladeColor)
                ).add(additionalLayers).add(uncoloredRevolverLayer).build()), rarity);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseChemthrower registerShaderCaseChemthrower(String name, int gripColor, int mainColor, EnumRarity rarity, ShaderCase.ShaderLayer... additionalLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new ShaderCaseChemthrower(
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer( processedChemthrowLayer, gripColor),
                        new ShaderCase.ShaderLayer( processedChemthrowLayer, mainColor)
                ).add(additionalLayers).add(uncoloredChemthrowLayer).build()), rarity);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseDrill registerShaderCaseDrill(String name, int gripColor, int mainColor, EnumRarity rarity, ShaderCase.ShaderLayer... additionalLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        //.addHeadLayers(new ShaderLayer(new ResourceLocation("immersiveengineering", "items/drill_iron"), 0xFF_FF_FF_FF))

        return ShaderRegistry.registerShaderCase(name, new ShaderCaseDrill(
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer( processedDrillLayer, gripColor),
                        new ShaderCase.ShaderLayer( processedDrillLayer, mainColor)
                ).add(additionalLayers).add(uncoloredDrillLayer).add(nullLayer).build()), rarity);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseRailgun registerShaderCaseRailgun(String name, int gripColor, int mainColor, EnumRarity rarity, ShaderCase.ShaderLayer... additionalLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new ShaderCaseRailgun(
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer( processedRailgunLayer, gripColor),
                        new ShaderCase.ShaderLayer( processedRailgunLayer, mainColor)
                ).add(uncoloredRailgunLayer).build()), rarity);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseShield registerShaderCaseShield(String name, int gripColor, int mainColor, EnumRarity rarity, ShaderCase.ShaderLayer... additionalLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new ShaderCaseShield(
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer( processedShieldLayer, gripColor),
                        new ShaderCase.ShaderLayer( processedShieldLayer, mainColor)
                ).add(additionalLayers).add(uncoloredShieldLayer).build()), rarity);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseMinecart registerShaderCaseMinecart(String name, int bodyColor, int secondaryColor, EnumRarity rarity, ShaderCase.ShaderLayer... additionalLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new ShaderCaseMinecart(
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "textures/models/shaders/minecart_0.png"   ), bodyColor),
                        new ShaderCase.ShaderLayer( new ResourceLocation("immersiveengineering", "textures/models/shaders/minecart_1_0.png" ), secondaryColor)
                ).add(additionalLayers).add(uncoloredMinecartLayer).build()), rarity);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static ShaderCaseBalloon registerShaderCaseBalloon(String name, int gripColor, int mainColor, EnumRarity rarity, ShaderCase.ShaderLayer... additionalLayers) {
        ImmutableList.Builder<ShaderCase.ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

        return ShaderRegistry.registerShaderCase(name, new ShaderCaseBalloon(
                shaderLayerBuilder.add(
                        new ShaderCase.ShaderLayer( processedBalloonLayer, gripColor),
                        new ShaderCase.ShaderLayer( processedBalloonLayer, mainColor)
                ).add(additionalLayers).add(uncoloredBalloonLayer).build()), rarity);
    }
}
