package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.renderer.entity.LayerIce;
import twilightforest.client.renderer.entity.LayerShields;
import twilightforest.entity.TFEntities;
import twilightforest.inventory.TFContainers;
import twilightforest.item.ItemTFArcticArmor;
import twilightforest.item.ItemTFFieryArmor;
import twilightforest.item.ItemTFKnightlyArmor;
import twilightforest.item.ItemTFPhantomArmor;
import twilightforest.item.ItemTFYetiArmor;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TFTileEntities;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = TwilightForestMod.ID)
public class TFClientSetup {

	public static boolean optifinePresent = false;

	@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = TwilightForestMod.ID)
	public static class ForgeEvents {

		private static boolean optifineWarningShown = false;

		@SubscribeEvent
		public static void showOptifineWarning(GuiScreenEvent.InitGuiEvent.Post event) {
			if (optifinePresent && !optifineWarningShown && !TFConfig.CLIENT_CONFIG.disableOptifineNagScreen.get() && event.getGui() instanceof MainMenuScreen) {
				optifineWarningShown = true;
				Minecraft.getInstance().displayGuiScreen(new OptifineWarningScreen(event.getGui()));
			}
		}

	}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
		try {
			Class.forName("net.optifine.Config");
			optifinePresent = true;
		} catch (ClassNotFoundException e) {
			optifinePresent = false;
		}
		TFItems.addItemModelProperties();

        ItemTFKnightlyArmor.initArmorModel();
        ItemTFPhantomArmor.initArmorModel();
        ItemTFYetiArmor.initArmorModel();
        ItemTFArcticArmor.initArmorModel();
        ItemTFFieryArmor.initArmorModel();
        MinecraftForge.EVENT_BUS.register(new LoadingScreenListener());
        RenderLayerRegistration.init();
        TFEntities.registerEntityRenderer();
        TFTileEntities.registerTileEntityRenders();
        TFContainers.renderScreens();

        TwilightForestRenderInfo renderInfo = new TwilightForestRenderInfo(128.0F, false, DimensionRenderInfo.FogType.NONE, false, false);
        DimensionRenderInfo.field_239208_a_.put(TwilightForestMod.prefix("renderer"), renderInfo);

        evt.enqueueWork(() -> {
            Atlases.addWoodType(TFBlocks.TWILIGHT_OAK);
            Atlases.addWoodType(TFBlocks.CANOPY);
            Atlases.addWoodType(TFBlocks.MANGROVE);
            Atlases.addWoodType(TFBlocks.DARKWOOD);
            Atlases.addWoodType(TFBlocks.TIMEWOOD);
            Atlases.addWoodType(TFBlocks.TRANSFORMATION);
            Atlases.addWoodType(TFBlocks.MINING);
            Atlases.addWoodType(TFBlocks.SORTING);
        });
       
    }

    public static void addLegacyPack() {
        //noinspection ConstantConditions
        if (Minecraft.getInstance() == null)
            // Normally Minecraft Client is never null except when generating through runData
            return;

        Minecraft.getInstance().getResourcePackList().addPackFinder(
                (consumer, iFactory) -> consumer.accept(
                        ResourcePackInfo.createResourcePack(
                                TwilightForestMod.prefix("classic_textures").toString(),
                                false,
                                () -> new TwilightLegacyPack(
                                        ModList
                                                .get()
                                                .getModFileById(TwilightForestMod.ID)
                                                .getFile()
                                ),
                                iFactory,
                                ResourcePackInfo.Priority.TOP,
                                iTextComponent -> iTextComponent
                        )
                )
        );
    }

    @SubscribeEvent // FIXME there's a few IDE warnings, find out what this is all about
    public static void loadComplete(FMLLoadCompleteEvent evt) {
        Minecraft.getInstance().getRenderManager().renderers.values().forEach(r -> {
            if (r instanceof LivingRenderer) {
                attachRenderLayers((LivingRenderer<?, ?>) r);
            }
        });
    }

    private static <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(LivingRenderer<T, M> renderer) {
        renderer.addLayer(new LayerShields<>(renderer));
        renderer.addLayer(new LayerIce<>(renderer));
    }
}
