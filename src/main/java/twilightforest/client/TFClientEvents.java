package twilightforest.client;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IWeatherRenderHandler;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TFConfig;
import twilightforest.TFEventListener;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.item.FullbrightBakedModel;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.client.renderer.entity.LayerShields;
import twilightforest.item.TFItems;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Dist.CLIENT)
public class TFClientEvents {

	@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModBusEvents {

		@SubscribeEvent
		public static void modelBake(ModelBakeEvent event) {
			fullbrightItem(event, TFItems.fiery_ingot);
			fullbrightItem(event, TFItems.fiery_boots);
			fullbrightItem(event, TFItems.fiery_chestplate);
			fullbrightItem(event, TFItems.fiery_helmet);
			fullbrightItem(event, TFItems.fiery_leggings);
			fullbrightItem(event, TFItems.fiery_pickaxe);
			fullbrightItem(event, TFItems.fiery_sword);
			fullbright(event, TFBlocks.fiery_block.getId(), "");
		}

		private static void fullbrightItem(ModelBakeEvent event, RegistryObject<Item> item) {
			fullbright(event, Objects.requireNonNull(item.getId()), "inventory");
		}

		private static void fullbright(ModelBakeEvent event, ResourceLocation rl, String state) {
			ModelResourceLocation mrl = new ModelResourceLocation(rl, state);
			event.getModelRegistry().put(mrl, new FullbrightBakedModel(event.getModelRegistry().get(mrl)));
		}

		@SubscribeEvent
		public static void texStitch(TextureStitchEvent.Pre evt) {
			AtlasTexture map = evt.getMap();

			//TODO: Removed until Tinkers' Construct is available
		/*map.setTextureEntry( new MoltenFieryTexture   ( new ResourceLocation( "minecraft", "blocks/lava_still"  ), RegisterBlockEvent.moltenFieryStill                                        ));
		map.setTextureEntry( new MoltenFieryTexture   ( new ResourceLocation( "minecraft", "blocks/lava_flow"   ), RegisterBlockEvent.moltenFieryFlow                                         ));
		map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "minecraft", "blocks/lava_still"  ), RegisterBlockEvent.moltenKnightmetalStill, true, KNIGHTMETAL_GRADIENT_MAP  ));
		map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "minecraft", "blocks/lava_flow"   ), RegisterBlockEvent.moltenKnightmetalFlow , true, KNIGHTMETAL_GRADIENT_MAP  ));
		map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "minecraft", "blocks/water_still" ), RegisterBlockEvent.essenceFieryStill     , true, FIERY_ESSENCE_GRADIENT_MAP));
		map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "minecraft", "blocks/water_flow"  ), RegisterBlockEvent.essenceFieryFlow      , true, FIERY_ESSENCE_GRADIENT_MAP));*/

			//TODO: Removed until Immersive Engineering is available
		/*if (TFCompat.IMMERSIVEENGINEERING.isActivated()) {
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "revolvers/shaders/revolver_grip" ), IEShaderRegister.PROCESSED_REVOLVER_GRIP_LAYER, true, EASY_GRAYSCALING_MAP ));
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "revolvers/shaders/revolver_0"    ), IEShaderRegister.PROCESSED_REVOLVER_LAYER     , true, EASY_GRAYSCALING_MAP ));
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "items/shaders/chemthrower_0"     ), IEShaderRegister.PROCESSED_CHEMTHROW_LAYER    , true, EASY_GRAYSCALING_MAP ));
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "items/shaders/drill_diesel_0"    ), IEShaderRegister.PROCESSED_DRILL_LAYER        , true, EASY_GRAYSCALING_MAP ));
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "items/shaders/railgun_0"         ), IEShaderRegister.PROCESSED_RAILGUN_LAYER      , true, EASY_GRAYSCALING_MAP ));
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "items/shaders/shield_0"          ), IEShaderRegister.PROCESSED_SHIELD_LAYER       , true, EASY_GRAYSCALING_MAP ));
		//	map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", ""                                ), IEShaderRegister.PROCESSED_MINECART_LAYER     , true, EASY_GRAYSCALING_MAP ));
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "blocks/shaders/balloon_0"        ), IEShaderRegister.PROCESSED_BALLOON_LAYER      , true, EASY_GRAYSCALING_MAP ));

			final String[] types = new String[]{ "1_0", "1_2", "1_4", "1_5", "1_6" };

			for (IEShaderRegister.CaseType caseType : IEShaderRegister.CaseType.everythingButMinecart()) {
				for (String type : types) {
					map.setTextureEntry(new GradientMappedTexture(
							IEShaderRegister.ModType.IMMERSIVE_ENGINEERING.provideTex(caseType, type),
							IEShaderRegister.ModType.TWILIGHT_FOREST.provideTex(caseType, type),
							true, EASY_GRAYSCALING_MAP
					));
				}
			}
		}*/
		}

		//TODO: Fields are unused due to missing compat
	/*public static final GradientNode[] KNIGHTMETAL_GRADIENT_MAP = {
			new GradientNode(0.0f , 0xFF_33_32_32),
			new GradientNode(0.1f , 0xFF_6A_73_5E),
			new GradientNode(0.15f, 0xFF_80_8C_72),
			new GradientNode(0.3f , 0xFF_A3_B3_91),
			new GradientNode(0.6f , 0xFF_C4_D6_AE),
			new GradientNode(1.0f , 0xFF_E7_FC_CD)
	};

	public static final GradientNode[] FIERY_ESSENCE_GRADIENT_MAP = {
			new GradientNode(0.2f, 0xFF_3D_17_17),
			new GradientNode(0.8f, 0xFF_5C_0B_0B)
	};

	public static final GradientNode[] EASY_GRAYSCALING_MAP = {
			new GradientNode(0.0f, 0xFF_80_80_80),
			new GradientNode(0.5f, 0xFF_AA_AA_AA), // AAAAAAaaaaaaaaaaa
			new GradientNode(1.0f, 0xFF_FF_FF_FF)
	};*/

		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event) {
			ModelLoader.addSpecialModel(LayerShields.LOC);
			ModelLoader.addSpecialModel(new ModelResourceLocation(TwilightForestMod.prefix("trophy"), "inventory"));
			ModelLoader.addSpecialModel(new ModelResourceLocation(TwilightForestMod.prefix("trophy_minor"), "inventory"));
			ModelLoader.addSpecialModel(new ModelResourceLocation(TwilightForestMod.prefix("trophy_quest"), "inventory"));
		}
	}

	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
	@SubscribeEvent
	public static void preOverlay(RenderGameOverlayEvent.Pre event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTHMOUNT) {
			if (TFEventListener.isRidingUnfriendly(Minecraft.getInstance().player)) {
				event.setCanceled(true);
			}
		}
	}

	/**
	 * Render effects in first-person perspective
	 */
	@SubscribeEvent
	public static void renderWorldLast(RenderWorldLastEvent event) {

		if (!TFConfig.CLIENT_CONFIG.firstPersonEffects.get()) return;

		GameSettings settings = Minecraft.getInstance().gameSettings;
		if (settings.getPointOfView() != PointOfView.FIRST_PERSON || settings.hideGUI) return;

		Entity entity = Minecraft.getInstance().getRenderViewEntity();
		if (entity instanceof LivingEntity) {
			EntityRenderer<? extends Entity> renderer = Minecraft.getInstance().getRenderManager().getRenderer(entity);
			if (renderer instanceof LivingRenderer<?,?>) {
				for (RenderEffect effect : RenderEffect.VALUES) {
					if (effect.shouldRender((LivingEntity) entity, true)) {
						effect.render((LivingEntity) entity, ((LivingRenderer<?,?>) renderer).getEntityModel(), 0.0, 0.0, 0.0, event.getPartialTicks(), true);
					}
				}
			}
		}
	}

	/**
	 * On the tick, we kill the vignette
	 */
	@SubscribeEvent
	public static void renderTick(TickEvent.RenderTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			Minecraft minecraft = Minecraft.getInstance();

			// only fire if we're in the twilight forest
			if (minecraft.world != null && "twilightforest".equals(minecraft.world.getDimensionKey().getLocation().getNamespace())) {
				// vignette
				if (minecraft.ingameGUI != null) {
					minecraft.ingameGUI.prevVignetteBrightness = 0.0F;
				}
			}//*/

			if (minecraft.player != null && TFEventListener.isRidingUnfriendly(minecraft.player)) {
				if (minecraft.ingameGUI != null) {
					minecraft.ingameGUI.setOverlayMessage(StringTextComponent.EMPTY, false);
				}
			}
		}
	}

	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		time++;

		Minecraft mc = Minecraft.getInstance();
		float partial = mc.getRenderPartialTicks();

		rotationTickerI = (rotationTickerI >= 359 ? 0 : rotationTickerI + 1);
		sineTickerI = (sineTickerI >= SINE_TICKER_BOUND ? 0 : sineTickerI + 1);

		rotationTicker = rotationTickerI + partial;
		sineTicker = sineTicker + partial;

		BugModelAnimationHelper.animate();
		DimensionRenderInfo info = DimensionRenderInfo.field_239208_a_.get(TwilightForestMod.prefix("renderer"));

		// add weather box if needed
		if (!mc.isGamePaused() && mc.world != null && info instanceof TwilightForestRenderInfo) {
			IWeatherRenderHandler weatherRenderer = info.getWeatherRenderHandler();
			if (weatherRenderer instanceof TFWeatherRenderer)
				((TFWeatherRenderer) weatherRenderer).tick();
		}
	}

	public static int time = 0;
	private static int rotationTickerI = 0;
	private static int sineTickerI = 0;
	public static float rotationTicker = 0;
	public static float sineTicker = 0;
	public static final float PI = (float) Math.PI;
	private static final int SINE_TICKER_BOUND = (int) ((PI * 200.0F) - 1.0F);
}
