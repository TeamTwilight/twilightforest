package twilightforest.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IWeatherRenderHandler;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TFConfig;
import twilightforest.TFEventListener;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.item.FullbrightBakedModel;
import twilightforest.client.model.item.TintIndexAwareFullbrightBakedModel;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.client.renderer.entity.ShieldLayer;
import twilightforest.client.renderer.tileentity.TwilightChestRenderer;
import twilightforest.compat.IECompat;
import twilightforest.compat.TFCompat;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.item.*;

import java.util.Objects;
import java.util.function.UnaryOperator;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Dist.CLIENT)
public class TFClientEvents {

	@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModBusEvents {
		@SubscribeEvent
		public static void registerLoaders(ModelRegistryEvent event) {
			ModelLoaderRegistry.registerLoader(TwilightForestMod.prefix("patch"), PatchModelLoader.INSTANCE);
		}

		@Deprecated // tterrag said this would become deprecated soon in favor of above method
		@SubscribeEvent
		public static void modelBake(ModelBakeEvent event) {
			// TODO Unhardcode, into using Model Deserializers and load from JSON instead
			fullbrightItem(event, TFItems.FIERY_INGOT);
			fullbrightItem(event, TFItems.FIERY_BOOTS);
			fullbrightItem(event, TFItems.FIERY_CHESTPLATE);
			fullbrightItem(event, TFItems.FIERY_HELMET);
			fullbrightItem(event, TFItems.FIERY_LEGGINGS);
			fullbrightItem(event, TFItems.FIERY_PICKAXE);
			fullbrightItem(event, TFItems.FIERY_SWORD);

			fullbrightItem(event, TFItems.RED_THREAD);

			fullbrightBlock(event, TFBlocks.FIERY_BLOCK);

			if(!ModList.get().isLoaded("ctm")) {
				tintedFullbrightBlock(event, TFBlocks.PINK_CASTLE_RUNE_BRICK, FullbrightBakedModel::disableCache);
				tintedFullbrightBlock(event, TFBlocks.BLUE_CASTLE_RUNE_BRICK, FullbrightBakedModel::disableCache);
				tintedFullbrightBlock(event, TFBlocks.YELLOW_CASTLE_RUNE_BRICK, FullbrightBakedModel::disableCache);
				tintedFullbrightBlock(event, TFBlocks.VIOLET_CASTLE_RUNE_BRICK, FullbrightBakedModel::disableCache);
			}

			if(ModList.get().isLoaded(TFCompat.IE_ID)) {
				IECompat.registerShaderModels(event);
			}
		}

		private static void fullbrightItem(ModelBakeEvent event, RegistryObject<Item> item) {
			fullbrightItem(event, item, f -> f);
		}

		private static void fullbrightItem(ModelBakeEvent event, RegistryObject<Item> item, UnaryOperator<FullbrightBakedModel> process) {
			fullbright(event, Objects.requireNonNull(item.getId()), "inventory", process);
		}

		private static void fullbrightBlock(ModelBakeEvent event, RegistryObject<Block> block) {
			fullbrightBlock(event, block, f -> f);
		}

		private static void fullbrightBlock(ModelBakeEvent event, RegistryObject<Block> block, UnaryOperator<FullbrightBakedModel> process) {
			fullbright(event, Objects.requireNonNull(block.getId()), "inventory", process);
			fullbright(event, Objects.requireNonNull(block.getId()), "", process);
		}

		private static void fullbright(ModelBakeEvent event, ResourceLocation rl, String state, UnaryOperator<FullbrightBakedModel> process) {
			ModelResourceLocation mrl = new ModelResourceLocation(rl, state);
			event.getModelRegistry().put(mrl, process.apply(new FullbrightBakedModel(event.getModelRegistry().get(mrl))));
		}

		private static void tintedFullbrightItem(ModelBakeEvent event, RegistryObject<Item> item) {
			tintedFullbrightItem(event, item, f -> f);
		}

		private static void tintedFullbrightItem(ModelBakeEvent event, RegistryObject<Item> item, UnaryOperator<FullbrightBakedModel> process) {
			tintedFullbright(event, Objects.requireNonNull(item.getId()), "inventory", process);
		}

		private static void tintedFullbrightBlock(ModelBakeEvent event, RegistryObject<Block> block) {
			tintedFullbrightBlock(event, block, f -> f);
		}

		private static void tintedFullbrightBlock(ModelBakeEvent event, RegistryObject<Block> block, UnaryOperator<FullbrightBakedModel> process) {
			tintedFullbright(event, Objects.requireNonNull(block.getId()), "inventory", process);
			tintedFullbright(event, Objects.requireNonNull(block.getId()), "", process);
		}

		private static void tintedFullbright(ModelBakeEvent event, ResourceLocation rl, String state, UnaryOperator<FullbrightBakedModel> process) {
			ModelResourceLocation mrl = new ModelResourceLocation(rl, state);
			event.getModelRegistry().put(mrl, process.apply(new TintIndexAwareFullbrightBakedModel(event.getModelRegistry().get(mrl))));
		}

		@SubscribeEvent
		public static void texStitch(TextureStitchEvent.Pre evt) {
			TextureAtlas map = evt.getAtlas();

			if (Sheets.CHEST_SHEET.equals(map.location()))
				TwilightChestRenderer.MATERIALS.values().stream()
						.flatMap(e -> e.values().stream())
						.map(Material::texture)
						.forEach(evt::addSprite);

			if (Sheets.BANNER_SHEET.equals(map.location()) || Sheets.SHIELD_SHEET.equals(map.location())) {
				for (BannerPattern pattern : BannerPattern.values()) {
					if (pattern.getFilename().startsWith(TwilightForestMod.ID)) {
						evt.addSprite(pattern.location(Sheets.BANNER_SHEET.equals(map.location())));
					}
				}
			}

			evt.addSprite(TwilightForestMod.prefix("block/mosspatch"));

		//FIXME bring back if you can get GradientMappedTexture working
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
			}*/

			//TODO: Removed until Tinkers' Construct is available
		/*map.setTextureEntry( new MoltenFieryTexture   ( new ResourceLocation( "minecraft", "blocks/lava_still"  ), RegisterBlockEvent.moltenFieryStill                                        ));
		map.setTextureEntry( new MoltenFieryTexture   ( new ResourceLocation( "minecraft", "blocks/lava_flow"   ), RegisterBlockEvent.moltenFieryFlow                                         ));
		map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "minecraft", "blocks/lava_still"  ), RegisterBlockEvent.moltenKnightmetalStill, true, KNIGHTMETAL_GRADIENT_MAP  ));
		map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "minecraft", "blocks/lava_flow"   ), RegisterBlockEvent.moltenKnightmetalFlow , true, KNIGHTMETAL_GRADIENT_MAP  ));
		map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "minecraft", "blocks/water_still" ), RegisterBlockEvent.essenceFieryStill     , true, FIERY_ESSENCE_GRADIENT_MAP));
		map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "minecraft", "blocks/water_flow"  ), RegisterBlockEvent.essenceFieryFlow      , true, FIERY_ESSENCE_GRADIENT_MAP));*/
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
			ForgeModelBakery.addSpecialModel(ShieldLayer.LOC);
			ForgeModelBakery.addSpecialModel(new ModelResourceLocation(TwilightForestMod.prefix("trophy"), "inventory"));
			ForgeModelBakery.addSpecialModel(new ModelResourceLocation(TwilightForestMod.prefix("trophy_minor"), "inventory"));
			ForgeModelBakery.addSpecialModel(new ModelResourceLocation(TwilightForestMod.prefix("trophy_quest"), "inventory"));

			ForgeModelBakery.addSpecialModel(TwilightForestMod.prefix("block/casket_obsidian"));
			ForgeModelBakery.addSpecialModel(TwilightForestMod.prefix("block/casket_stone"));
			ForgeModelBakery.addSpecialModel(TwilightForestMod.prefix("block/casket_basalt"));
		}
	}

	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
	@SubscribeEvent
	public static void preOverlay(RenderGameOverlayEvent.PreLayer event) {
		if (event.getOverlay() == ForgeIngameGui.MOUNT_HEALTH_ELEMENT) {
			if (TFEventListener.isRidingUnfriendly(Minecraft.getInstance().player)) {
				event.setCanceled(true);
			}
		}
	}

	/**
	 * Render effects in first-person perspective
	 */
	@SubscribeEvent
	public static void renderWorldLast(RenderLevelLastEvent event) {

		if (!TFConfig.CLIENT_CONFIG.firstPersonEffects.get()) return;

		Options settings = Minecraft.getInstance().options;
		if (settings.getCameraType() != CameraType.FIRST_PERSON || settings.hideGui) return;

		Entity entity = Minecraft.getInstance().getCameraEntity();
		if (entity instanceof LivingEntity) {
			EntityRenderer<? extends Entity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
			if (renderer instanceof LivingEntityRenderer<?,?>) {
				for (RenderEffect effect : RenderEffect.VALUES) {
					if (effect.shouldRender((LivingEntity) entity, true)) {
						effect.render((LivingEntity) entity, ((LivingEntityRenderer<?,?>) renderer).getModel(), 0.0, 0.0, 0.0, event.getPartialTick(), true);
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
			if (minecraft.level != null && "twilightforest".equals(minecraft.level.dimension().location().getNamespace())) {
				// vignette
				if (minecraft.gui != null) {
					minecraft.gui.vignetteBrightness = 0.0F;
				}
			}//*/

			if (minecraft.player != null && TFEventListener.isRidingUnfriendly(minecraft.player)) {
				if (minecraft.gui != null) {
					minecraft.gui.setOverlayMessage(TextComponent.EMPTY, false);
				}
			}
		}
	}

	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		time++;

		Minecraft mc = Minecraft.getInstance();
		float partial = mc.getFrameTime();

		rotationTickerI = (rotationTickerI >= 359 ? 0 : rotationTickerI + 1);
		sineTickerI = (sineTickerI >= SINE_TICKER_BOUND ? 0 : sineTickerI + 1);

		rotationTicker = rotationTickerI + partial;
		sineTicker = sineTicker + partial;

		BugModelAnimationHelper.animate();
		DimensionSpecialEffects info = DimensionSpecialEffects.EFFECTS.get(TwilightForestMod.prefix("renderer"));

		// add weather box if needed
		if (!mc.isPaused() && mc.level != null && info instanceof TwilightForestRenderInfo) {
			IWeatherRenderHandler weatherRenderer = info.getWeatherRenderHandler();
			if (weatherRenderer instanceof TFWeatherRenderer)
				((TFWeatherRenderer) weatherRenderer).tick();
		}
	}

	private static final MutableComponent WIP_TEXT_0 = new TranslatableComponent("twilightforest.misc.wip0").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	private static final MutableComponent WIP_TEXT_1 = new TranslatableComponent("twilightforest.misc.wip1").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	private static final MutableComponent NYI_TEXT = new TranslatableComponent("twilightforest.misc.nyi").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));

	@SubscribeEvent
	public static void tooltipEvent(ItemTooltipEvent event) {
		ItemStack item = event.getItemStack();

		/*
			There's some kinda crash here where the "Tag % used before it was bound" crash happens from
			StaticTagHelper$Wrapper.resolve() because the tag wrapped is null. I assume this crash happens because
			somehow the game attempts to load a tooltip for an item in the main menu or something upon
			resourcepack reload when the player has not loaded into a save. See Issue #1270 for crashlog
		*/

		//TODO do we need this anymore? If so, ho do we re-add it?
//		boolean wip = (ItemTagGenerator.WIP instanceof StaticTagHelper.Wrapper<Item> wrappedWIP) && wrappedWIP.tag != null && item.is(wrappedWIP);
		// WIP takes precedence over NYI
//		boolean nyi = !wip && (ItemTagGenerator.NYI instanceof StaticTagHelper.Wrapper<Item> wrappedNYI) && wrappedNYI.tag != null && item.is(wrappedNYI);

//		if (!wip && !nyi)
//			return;

		//if (item.getDisplayName() instanceof MutableComponent displayName)
		//	displayName/*.append(wip ? " [WIP]" : " [NYI]")*/.setStyle(displayName.getStyle().withColor(ChatFormatting.DARK_GRAY));

		if(!item.is(ItemTagGenerator.WIP) && !item.is(ItemTagGenerator.NYI)) return;

		if (item.is(ItemTagGenerator.WIP)) {
			event.getToolTip().add(WIP_TEXT_0);
			event.getToolTip().add(WIP_TEXT_1);
		} else {
			event.getToolTip().add(NYI_TEXT);
		}
	}

	public static int time = 0;
	private static int rotationTickerI = 0;
	private static int sineTickerI = 0;
	public static float rotationTicker = 0;
	public static float sineTicker = 0;
	public static final float PI = (float) Math.PI;
	private static final int SINE_TICKER_BOUND = (int) ((PI * 200.0F) - 1.0F);

	/**
	 * Zooms in the FOV while using a bow, just like vanilla does in the AbstractClientPlayer's getFieldOfViewModifier() method (1.18.2)
	 */
	@SubscribeEvent
	public static void FOVUpdate(EntityViewRenderEvent.FieldOfView event) {
		if (event.getCamera().getEntity() instanceof LivingEntity living && living.isUsingItem()) {
			Item useItem = living.getUseItem().getItem();
			if (useItem instanceof TripleBowItem || useItem instanceof EnderBowItem || useItem instanceof IceBowItem || useItem instanceof SeekerBowItem) {
				float f = (living.getTicksUsingItem() + (float)event.getPartialTicks()) / 20F;
				f = f > 1.0F ? 1.0F : f * f;
				event.setFOV(event.getFOV() * (1.0F - f * 0.15F));
			}
		}
	}
}
