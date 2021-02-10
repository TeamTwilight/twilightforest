package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.resources.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import twilightforest.TwilightForestMod;
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

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFClientSetup {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        TFItems.addItemModelProperties();

        ItemTFKnightlyArmor.initArmorModel();
        ItemTFPhantomArmor.initArmorModel();
        ItemTFYetiArmor.initArmorModel();
        ItemTFArcticArmor.initArmorModel();
        ItemTFFieryArmor.initArmorModel();
        // FIXME MinecraftForge.EVENT_BUS.register(new LoadingScreenListener());
        RenderLayerRegistration.init();
        TFEntities.registerEntityRenderer();
        TFTileEntities.registerTileEntityRenders();
        TFContainers.renderScreens();

        TwilightForestRenderInfo renderInfo = new TwilightForestRenderInfo(128.0F, false, DimensionRenderInfo.FogType.NONE, false, false);
        DimensionRenderInfo.field_239208_a_.put(TwilightForestMod.prefix("renderer"), renderInfo);
        
        //[VanillaCopy] Allows bows to use animations when being fired
        ItemModelsProperties.registerProperty(TFItems.ender_bow.get(), new ResourceLocation("pull"), (stack, world, entity) -> {
            if (entity == null) {
               return 0.0F;
            } else {
               return entity.getActiveItemStack() != stack ? 0.0F : (stack.getUseDuration() - entity.getItemInUseCount()) / 20.0F;
            }
         });
        
        ItemModelsProperties.registerProperty(TFItems.ender_bow.get(), new ResourceLocation("pulling"), (stack, world, entity) -> {
            return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
         });
        
        ItemModelsProperties.registerProperty(TFItems.ice_bow.get(), new ResourceLocation("pull"), (stack, world, entity) -> {
            if (entity == null) {
               return 0.0F;
            } else {
               return entity.getActiveItemStack() != stack ? 0.0F : (stack.getUseDuration() - entity.getItemInUseCount()) / 20.0F;
            }
         });
        
        ItemModelsProperties.registerProperty(TFItems.ice_bow.get(), new ResourceLocation("pulling"), (stack, world, entity) -> {
            return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
         });
        
        ItemModelsProperties.registerProperty(TFItems.seeker_bow.get(), new ResourceLocation("pull"), (stack, world, entity) -> {
            if (entity == null) {
               return 0.0F;
            } else {
               return entity.getActiveItemStack() != stack ? 0.0F : (stack.getUseDuration() - entity.getItemInUseCount()) / 20.0F;
            }
         });
        
        ItemModelsProperties.registerProperty(TFItems.seeker_bow.get(), new ResourceLocation("pulling"), (stack, world, entity) -> {
            return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
         });
        
        ItemModelsProperties.registerProperty(TFItems.triple_bow.get(), new ResourceLocation("pull"), (stack, world, entity) -> {
            if (entity == null) {
               return 0.0F;
            } else {
               return entity.getActiveItemStack() != stack ? 0.0F : (stack.getUseDuration() - entity.getItemInUseCount()) / 20.0F;
            }
         });
        
        ItemModelsProperties.registerProperty(TFItems.triple_bow.get(), new ResourceLocation("pulling"), (stack, world, entity) -> {
            return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
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
