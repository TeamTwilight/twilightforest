package twilightforest.client.event;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.animal.wolf.AdultWolfModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.monster.silverfish.SilverfishModel;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.model.monster.spider.SpiderModel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.map.RegisterMapDecorationRenderersEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.TwilightForestMod;
import twilightforest.client.*;
import twilightforest.init.TFDimension;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.*;
import twilightforest.client.model.block.BrazierModel;
import twilightforest.client.model.block.ReactorDebrisModel;
import twilightforest.client.model.block.UnbakedReactorDebrisBlockStateModel;
import twilightforest.client.model.block.aurorablock.UnbakedNoiseVaryingBlockStateModel;
import twilightforest.client.model.block.connected.ConnectedTextureModelLoader;
import twilightforest.client.model.block.forcefield.ForceFieldModelLoader;
import twilightforest.client.model.block.forcefield.UnbakedForceFieldBlockStateModel;
import twilightforest.client.model.block.patch.UnbakedPatchBlockStateModel;
import twilightforest.client.model.block.giantblock.UnbakedGiantBlockStateModel;
import twilightforest.client.model.block.patch.PatchModelLoader;
import twilightforest.client.model.entity.*;
import twilightforest.client.model.item.TravellersGearItemModel;
import twilightforest.client.model.item.TrollsteinnModel;
import twilightforest.client.particle.*;
import twilightforest.client.properties.*;
import twilightforest.client.renderer.armor.TFArmorRenderer;
import twilightforest.client.renderer.armor.TFSimpleArmorRenderer;
import twilightforest.client.renderer.block.*;
import twilightforest.client.renderer.entity.*;
import twilightforest.client.renderer.entity.layers.IceLayer;
import twilightforest.client.renderer.entity.layers.ShieldLayer;
import twilightforest.client.renderer.map.ConqueredMapIconRenderer;
import twilightforest.client.renderer.map.MagicMapPlayerIconRenderer;
import twilightforest.client.renderer.special.*;
import twilightforest.client.renderer.tooltip.ItemDisplayTooltipComponent;
import twilightforest.client.renderer.tooltip.PotionFlaskTooltipComponent;
import twilightforest.client.renderer.tooltip.TravellersBeltTooltipComponent;
import twilightforest.init.*;
import twilightforest.item.ArcticArmorItem;
import twilightforest.item.PotionFlaskItem;
import twilightforest.item.mapdata.TFMagicMapData;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.TravellersGogglesItem;
import twilightforest.util.woods.TFWoodTypes;

import java.util.ArrayList;
import java.util.Objects;

@Component(dist = Dist.CLIENT)
public class ClientRegistrationEvents {

	private static boolean optifinePresent = false;

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(EntityRenderersEvent.AddLayers.class, this::attachRenderLayers);
		bus.addListener(this::jarLidSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::registerAdditionalModels);
		bus.addListener(this::registerClientReloadListeners);
		bus.addListener(this::registerAtlases);
		bus.addListener(this::registerEntityRenderers);
		bus.addListener(this::registerLayerDefinitions);
		bus.addListener(this::registerModelLoaders);
		bus.addListener(this::registerBlockStateModels);
		bus.addListener(this::registerScreens);
		bus.addListener(this::registerClientExtensions);
		bus.addListener(this::registerMapDecorators);
		bus.addListener(this::registerParticleFactories);
		bus.addListener(this::registerCustomEnvironmentEffects);
		bus.addListener(this::registerConditionalProperties);
		bus.addListener(this::registerRangeProperties);
		bus.addListener(this::registerSelectProperties);
		bus.addListener(this::registerItemModels);

		bus.addListener(RegisterKeyMappingsEvent.class, event -> TFKeyBinds.KEY_MAPPINGS.forEach(event::register));

		bus.addListener(this::registerSpecialModelRenderers);

		bus.addListener(ColorHandler::registerBlockColors);
		bus.addListener(ColorHandler::registerItemColors);

		bus.addListener(TFShaders::registerRenderPipelines);

		bus.addListener(OverlayHandler::registerOverlays);

		bus.addListener(RegisterClientTooltipComponentFactoriesEvent.class, event -> {
			event.register(PotionFlaskItem.Tooltip.class, PotionFlaskTooltipComponent::new);
			event.register(TravellersArmorBeltItem.Tooltip.class, TravellersBeltTooltipComponent::new);
			event.register(TravellersGogglesItem.Tooltip.class, ItemDisplayTooltipComponent::new);
		});
	}

	private void registerBlockStateModels(RegisterBlockStateModels event) {
		event.registerModel(TwilightForestMod.prefix("giant_block"), UnbakedGiantBlockStateModel.MAP_CODEC);
		event.registerModel(TwilightForestMod.prefix("noise_varying"), UnbakedNoiseVaryingBlockStateModel.MAP_CODEC);
		event.registerModel(TwilightForestMod.prefix("force_field"), UnbakedForceFieldBlockStateModel.MAP_CODEC);
		event.registerModel(TwilightForestMod.prefix("reactor_debris"), UnbakedReactorDebrisBlockStateModel.MAP_CODEC);
		event.registerModel(TwilightForestMod.prefix("patch"), UnbakedPatchBlockStateModel.MAP_CODEC);
	}

	private void registerItemModels(RegisterItemModelsEvent event) {
		event.register(TwilightForestMod.prefix("travellers_gear"), TravellersGearItemModel.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("trollsteinn"), TrollsteinnModel.Unbaked.MAP_CODEC);
	}

	private void registerSpecialModelRenderers(RegisterSpecialModelRendererEvent event) {
		event.register(TwilightForestMod.prefix("trophy"), TrophySpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("skull_candle"), SkullCandleSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("candelabra"), CandelabraSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("moonworm"), MoonwormSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("skull_chest"), SkullChestSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("keepsake_casket"), KeepsakeCasketSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("mystic_crown"), MysticCrownSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("mason_jar"), MasonJarSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("firefly"), FireflySpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("cicada"), CicadaSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("knightmetal_shield"), KnightmetalShieldSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("brazier"), BrazierSpecialRenderer.Unbaked.MAP_CODEC);
	}

	private void registerModelLoaders(ModelEvent.RegisterLoaders event) {
		event.register(TwilightForestMod.prefix("patch"), PatchModelLoader.INSTANCE);
		event.register(TwilightForestMod.prefix("force_field"), ForceFieldModelLoader.INSTANCE);
		event.register(TwilightForestMod.prefix("connected_texture_block"), ConnectedTextureModelLoader.INSTANCE);

		// Fallback loaders for missing/unported model types
		// These strip loader-specific JSON keys and deserialize as standard models
		// NOTE: Using anonymous inner classes instead of lambdas to avoid beanification corruption
		event.register(Identifier.fromNamespaceAndPath("neoforge", "item_layers"), new FallbackLoader("neoforge_data", "render_types"));
		event.register(Identifier.fromNamespaceAndPath("neoforge", "separate_transforms"), new FallbackLoader("perspectives", "base"));
		
		event.register(TwilightForestMod.prefix("giant_block"), new FallbackLoader("parent_block"));
		event.register(TwilightForestMod.prefix("royal_rags"), new FallbackLoader());
		event.register(TwilightForestMod.prefix("travellers_gear"), new FallbackLoader("modifier_directory", "broken_modifier_directory"));
	}

	/**
	 * A fallback UnbakedModelLoader that strips the "loader" key and any additional specified keys
	 * from the JSON, then deserializes the remaining JSON as a standard UnbakedModel.
	 * <p>
	 * This is implemented as a concrete class (not a lambda) to avoid bytecode corruption
	 * from the beanification transformer.
	 */
	private static final class FallbackLoader implements UnbakedModelLoader<UnbakedModel> {
		private final String[] keysToRemove;

		FallbackLoader(String... keysToRemove) {
			this.keysToRemove = keysToRemove;
		}

		@Override
		public UnbakedModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
			jsonObject.remove("loader");
			for (String key : keysToRemove) {
				jsonObject.remove(key);
			}
			return deserializationContext.deserialize(jsonObject, UnbakedModel.class);
		}
	}

	private void registerConditionalProperties(RegisterConditionalItemModelPropertyEvent event) {
		event.register(TwilightForestMod.prefix("moonworm_queen_pulse"), MoonwormQueenPulse.TYPE);
		event.register(TwilightForestMod.prefix("ore_meter_flash"), OreMeterFlash.TYPE);
	}

	private void registerRangeProperties(RegisterRangeSelectItemModelPropertyEvent event) {
		event.register(TwilightForestMod.prefix("potion_flask_dosage"), PotionFlaskDosage.TYPE);
		event.register(TwilightForestMod.prefix("potion_flask_damage"), PotionFlaskDamage.TYPE);
	}

	private void registerSelectProperties(RegisterSelectItemModelPropertyEvent event) {
		event.register(TwilightForestMod.prefix("experiment_115_variant"), Experiment115Type.TYPE);
	}

	private void jarLidSetup(FMLClientSetupEvent event) {
		// Populate JarRenderer.LIDS map from LID_LOCATION_LIST at init time.
		// This replaces the old cacheJarLids which used the removed ModelEvent.BakingCompleted.
		event.enqueueWork(() -> {
			for (JarRenderer.LidResource lid : JarRenderer.LID_LOCATION_LIST.get()) {
				BuiltInRegistries.BLOCK.getOptional(lid.identifier()).ifPresent(block -> {
					JarRenderer.LIDS.put(lid.lid(), block.defaultBlockState());
				});
			}
		});
	}

	private void registerAdditionalModels(ModelEvent.RegisterStandalone event) {
		// Not needed in 26.1.2: TrollsteinnModel is registered via RegisterItemModelsEvent,
		// ReactorDebrisModel is registered via RegisterBlockStateModels,
		// Jar lids use block model resolver at render time.
	}

	private void clientSetup(FMLClientSetupEvent evt) {
		try {
			Class.forName("net.optifine.Config");
			optifinePresent = true;
		} catch (ClassNotFoundException e) {
			optifinePresent = false;
		}

		evt.enqueueWork(() -> {
			Sheets.addWoodType(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.CANOPY_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.MANGROVE_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.DARK_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.TIME_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.TRANSFORMATION_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.MINING_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.SORTING_WOOD_TYPE);
		});
	}

	private void registerAtlases(RegisterTextureAtlasesEvent event) {
		event.register(new AtlasManager.AtlasConfig(MagicPaintingAtlasInfo.ATLAS_LOCATION, MagicPaintingAtlasInfo.ATLAS_INFO_LOCATION, false));
	}

	private void registerClientReloadListeners(AddClientReloadListenersEvent event) {
		event.addListener(TwilightForestMod.prefix("texture_generator"), TextureGeneratorReloadListener.INSTANCE);
		event.addListener(TwilightForestMod.prefix("armor_cache"), new TFArmorRenderer.ResourceReloadListener());
	}

	private void registerScreens(RegisterMenuScreensEvent event) {
		event.register(TFMenuTypes.UNCRAFTING.get(), UncraftingScreen::new);
	}

	private void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(TFEntities.BOAR.get(), BoarRenderer::new);
		event.registerEntityRenderer(TFEntities.BIGHORN_SHEEP.get(), BighornRenderer::new);
		event.registerEntityRenderer(TFEntities.DEER.get(), DeerRenderer::new);
		event.registerEntityRenderer(TFEntities.REDCAP.get(), RedcapRenderer::new);
		event.registerEntityRenderer(TFEntities.SKELETON_DRUID.get(), SkeletonDruidRenderer::new);
		event.registerEntityRenderer(TFEntities.HOSTILE_WOLF.get(), HostileWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.WRAITH.get(), WraithRenderer::new);
		event.registerEntityRenderer(TFEntities.HYDRA.get(), HydraRenderer::new);
		event.registerEntityRenderer(TFEntities.LICH.get(), LichRenderer::new);
		event.registerEntityRenderer(TFEntities.PENGUIN.get(), m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN_BABY)), 0.375F, "penguin.png"));
		event.registerEntityRenderer(TFEntities.LICH_MINION.get(), LichMinionRenderer::new);
		event.registerEntityRenderer(TFEntities.LOYAL_ZOMBIE.get(), LoyalZombieRenderer::new);
		event.registerEntityRenderer(TFEntities.TINY_BIRD.get(), TinyBirdRenderer::new);
		event.registerEntityRenderer(TFEntities.SQUIRREL.get(), SquirrelRenderer::new);
		event.registerEntityRenderer(TFEntities.DWARF_RABBIT.get(), BunnyRenderer::new);
		event.registerEntityRenderer(TFEntities.RAVEN.get(), m -> new BirdRenderer<>(m, new RavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png"));
		event.registerEntityRenderer(TFEntities.QUEST_RAM.get(), QuestRamRenderer::new);
		event.registerEntityRenderer(TFEntities.KOBOLD.get(), KoboldRenderer::new);
		//event.registerEntityRenderer(TFEntities.BOGGARD.get(), m -> new RenderTFBiped<>(m, new BipedModel<>(0), 0.625F, "kobold.png"));
		event.registerEntityRenderer(TFEntities.MOSQUITO_SWARM.get(), MosquitoSwarmRenderer::new);
		event.registerEntityRenderer(TFEntities.DEATH_TOME.get(), DeathTomeRenderer::new);
		event.registerEntityRenderer(TFEntities.MINOTAUR.get(), MinotaurRenderer::new);
		event.registerEntityRenderer(TFEntities.MINOSHROOM.get(), MinoshroomRenderer::new);
		event.registerEntityRenderer(TFEntities.FIRE_BEETLE.get(), FireBeetleRenderer::new);
		event.registerEntityRenderer(TFEntities.SLIME_BEETLE.get(), SlimeBeetleRenderer::new);
		event.registerEntityRenderer(TFEntities.PINCH_BEETLE.get(), PinchBeetleRenderer::new);
		event.registerEntityRenderer(TFEntities.MIST_WOLF.get(), MistWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.CARMINITE_GHASTLING.get(), m -> new TFGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
		event.registerEntityRenderer(TFEntities.CARMINITE_GOLEM.get(), CarminiteGolemRenderer::new);
		event.registerEntityRenderer(TFEntities.TOWERWOOD_BORER.get(), TowerwoodBorerRenderer::new);
		event.registerEntityRenderer(TFEntities.CARMINITE_GHASTGUARD.get(), CarminiteGhastRenderer::new);
		event.registerEntityRenderer(TFEntities.UR_GHAST.get(), UrGhastRenderer::new);
		event.registerEntityRenderer(TFEntities.BLOCKCHAIN_GOBLIN.get(), BlockChainGoblinRenderer::new);
		event.registerEntityRenderer(TFEntities.UPPER_GOBLIN_KNIGHT.get(), UpperGoblinKnightRenderer::new);
		event.registerEntityRenderer(TFEntities.LOWER_GOBLIN_KNIGHT.get(), LowerGoblinKnightRenderer::new);
		event.registerEntityRenderer(TFEntities.HELMET_CRAB.get(), HelmetCrabRenderer::new);
		event.registerEntityRenderer(TFEntities.KNIGHT_PHANTOM.get(), KnightPhantomRenderer::new);
		event.registerEntityRenderer(TFEntities.NAGA.get(), NagaRenderer::new);
		event.registerEntityRenderer(TFEntities.SWARM_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 0.25F, "swarmspider.png", 0.5F));
		event.registerEntityRenderer(TFEntities.KING_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 1.25F, "kingspider.png", 1.9F));
		event.registerEntityRenderer(TFEntities.CARMINITE_BROODLING.get(), m -> new TFSpiderRenderer<>(m, 0.6F, "towerbroodling.png", 0.7F));
		event.registerEntityRenderer(TFEntities.HEDGE_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 0.8F, "hedgespider.png", 1.0F));
		event.registerEntityRenderer(TFEntities.REDCAP_SAPPER.get(), RedcapSapperRenderer::new);
		event.registerEntityRenderer(TFEntities.MAZE_SLIME.get(), MazeSlimeRenderer::new);
		event.registerEntityRenderer(TFEntities.YETI.get(), YetiRenderer::new);
		event.registerEntityRenderer(TFEntities.PROTECTION_BOX.get(), ProtectionBoxRenderer::new);
		event.registerEntityRenderer(TFEntities.MAGIC_PAINTING.get(), MagicPaintingRenderer::new);
		event.registerEntityRenderer(TFEntities.ALPHA_YETI.get(), AlphaYetiRenderer::new);
		event.registerEntityRenderer(TFEntities.WINTER_WOLF.get(), WinterWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.SNOW_GUARDIAN.get(), SnowGuardianRenderer::new);
		event.registerEntityRenderer(TFEntities.STABLE_ICE_CORE.get(), StableIceCoreRenderer::new);
		event.registerEntityRenderer(TFEntities.UNSTABLE_ICE_CORE.get(), UnstableIceCoreRenderer::new);
		event.registerEntityRenderer(TFEntities.SNOW_QUEEN.get(), SnowQueenRenderer::new);
		event.registerEntityRenderer(TFEntities.TROLL.get(), TrollRenderer::new);
		event.registerEntityRenderer(TFEntities.GIANT_MINER.get(), TFGiantRenderer::new);
		event.registerEntityRenderer(TFEntities.ARMORED_GIANT.get(), TFGiantRenderer::new);
		event.registerEntityRenderer(TFEntities.ICE_CRYSTAL.get(), IceCrystalRenderer::new);
		event.registerEntityRenderer(TFEntities.CHAIN_BLOCK.get(), BlockChainRenderer::new);
		event.registerEntityRenderer(TFEntities.CUBE_OF_ANNIHILATION.get(), CubeOfAnnihilationRenderer::new);
		event.registerEntityRenderer(TFEntities.HARBINGER_CUBE.get(), HarbingerCubeRenderer::new);
		event.registerEntityRenderer(TFEntities.ADHERENT.get(), AdherentRenderer::new);
		event.registerEntityRenderer(TFEntities.ROVING_CUBE.get(), RovingCubeRenderer::new);
		event.registerEntityRenderer(TFEntities.RISING_ZOMBIE.get(), RisingZombieRenderer::new);
		event.registerEntityRenderer(TFEntities.PLATEAU_BOSS.get(), NoopRenderer::new);

		// projectiles
		event.registerEntityRenderer(TFEntities.NATURE_BOLT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.LICH_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false));
		event.registerEntityRenderer(TFEntities.WAND_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false));
		event.registerEntityRenderer(TFEntities.LICH_BOMB.get(), c -> new CustomProjectileTextureRenderer(c, Identifier.withDefaultNamespace("textures/item/magma_cream.png"), 1.0F, true, true));
		event.registerEntityRenderer(TFEntities.TOME_BOLT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.HYDRA_MORTAR.get(), HydraMortarRenderer::new);
		event.registerEntityRenderer(TFEntities.SLIME_BLOB.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.MOONWORM_SHOT.get(), MoonwormShotRenderer::new);
		event.registerEntityRenderer(TFEntities.CHARM_EFFECT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.THROWN_WEP.get(), ThrownWepRenderer::new);
		event.registerEntityRenderer(TFEntities.FALLING_ICE.get(), FallingIceRenderer::new);
		event.registerEntityRenderer(TFEntities.THROWN_ICE.get(), ThrownIceRenderer::new);
		event.registerEntityRenderer(TFEntities.THROWN_BLOCK.get(), ThrownBlockRenderer::new);
		event.registerEntityRenderer(TFEntities.ICE_SNOWBALL.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.SLIDER.get(), SlideBlockRenderer::new);
		event.registerEntityRenderer(TFEntities.SEEKER_ARROW.get(), DefaultArrowRenderer::new);
		event.registerEntityRenderer(TFEntities.ICE_ARROW.get(), DefaultArrowRenderer::new);

		// Boats
		event.registerEntityRenderer(TFEntities.TWILIGHT_OAK_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TWILIGHT_OAK_BOAT));
		event.registerEntityRenderer(TFEntities.TWILIGHT_OAK_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TWILIGHT_OAK_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.CANOPY_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.CANOPY_BOAT));
		event.registerEntityRenderer(TFEntities.CANOPY_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.CANOPY_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.MANGROVE_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MANGROVE_BOAT));
		event.registerEntityRenderer(TFEntities.MANGROVE_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MANGROVE_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.DARK_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.DARK_BOAT));
		event.registerEntityRenderer(TFEntities.DARK_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.DARK_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.TIME_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TIME_BOAT));
		event.registerEntityRenderer(TFEntities.TIME_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TIME_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.TRANSFORMATION_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TRANSFORMATION_BOAT));
		event.registerEntityRenderer(TFEntities.TRANSFORMATION_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TRANSFORMATION_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.MINING_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MINING_BOAT));
		event.registerEntityRenderer(TFEntities.MINING_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MINING_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.SORTING_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.SORTING_BOAT));
		event.registerEntityRenderer(TFEntities.SORTING_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.SORTING_CHEST_BOAT));

		// Block Entities
		event.registerBlockEntityRenderer(TFBlockEntities.FIREFLY.get(), FireflyRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.CICADA.get(), CicadaRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.MOONWORM.get(), MoonwormRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.TROPHY.get(), TrophyRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.TF_CHEST.get(), TFChestRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.TF_TRAPPED_CHEST.get(), TFChestRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.SKULL_CHEST.get(), SkullChestRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.KEEPSAKE_CASKET.get(), KeepsakeCasketRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.SKULL_CANDLE.get(), SkullCandleRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.REACTOR_DEBRIS.get(), ReactorDebrisRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.RED_THREAD.get(), RedThreadRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.CANDELABRA.get(), CandelabraRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.JAR.get(), JarRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.MASON_JAR.get(), JarRenderer.MasonJarRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.OMINOUS_CANDLE.get(), OminousCandleRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.SINISTER_SPAWNER.get(), SinisterSpawnerRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.BRAZIER.get(), BrazierRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.DRYING_RACK.get(), DryingRackRenderer::new);
	}

	private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(TFModelLayers.ARCTIC_ARMOR_INNER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.ARCTIC_ARMOR_OUTER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.FIERY_ARMOR_INNER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.FIERY_ARMOR_OUTER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.TRAVELLERS_ARMOR_HELMET, () -> LayerDefinition.create(TravellersGearModels.addGogglePieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, () -> LayerDefinition.create(TravellersGearModels.addGlovePieces(new CubeDeformation(0.295F), false), 64, 32)); // TODO: reduce to 0.25F (+ dx?) without z-fighting in the player's inventory view
		event.registerLayerDefinition(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM, () -> LayerDefinition.create(TravellersGearModels.addGlovePieces(new CubeDeformation(0.295F), true), 64, 32));
		event.registerLayerDefinition(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, () -> TravellersWingsModel.createLayer(0.25F));
		event.registerLayerDefinition(TFModelLayers.TRAVELLERS_ARMOR_BOOTS, () -> LayerDefinition.create(TravellersGearModels.addBootPieces(new CubeDeformation(0.5F)), 64, 32));
		event.registerLayerDefinition(TFModelLayers.KNIGHTMETAL_ARMOR_INNER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.KNIGHTMETAL_ARMOR_OUTER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.PHANTOM_ARMOR_INNER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.PHANTOM_ARMOR_OUTER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.YETI_ARMOR_INNER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.YETI_ARMOR_OUTER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));

		event.registerLayerDefinition(TFModelLayers.ALPHA_YETI_TROPHY, AlphaYetiModel::createTrophy);
		event.registerLayerDefinition(TFModelLayers.HYDRA_TROPHY, HydraHeadModel::create);
		event.registerLayerDefinition(TFModelLayers.KNIGHT_PHANTOM_TROPHY, KnightPhantomModel::createTrophy);
		event.registerLayerDefinition(TFModelLayers.LICH_TROPHY, LichModel::create);
		event.registerLayerDefinition(TFModelLayers.MINOSHROOM_TROPHY, MinoshroomModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA_TROPHY, NagaModel::create);
		event.registerLayerDefinition(TFModelLayers.QUEST_RAM_TROPHY, QuestRamModel::create);
		event.registerLayerDefinition(TFModelLayers.SNOW_QUEEN_TROPHY, SnowQueenModel::create);
		event.registerLayerDefinition(TFModelLayers.UR_GHAST_TROPHY, UrGhastModel::create);

		event.registerLayerDefinition(TFModelLayers.ADHERENT, AdherentModel::create);
		event.registerLayerDefinition(TFModelLayers.ALPHA_YETI, AlphaYetiModel::create);
		event.registerLayerDefinition(TFModelLayers.ARMORED_GIANT, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.BIGHORN_SHEEP, BighornModel::create);
		event.registerLayerDefinition(TFModelLayers.BIGHORN_SHEEP_BABY, () -> BighornModel.create().apply(BighornModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(TFModelLayers.BLOCKCHAIN_GOBLIN, BlockChainGoblinModel::create);
		event.registerLayerDefinition(TFModelLayers.BOAR, BoarModel::create);
		event.registerLayerDefinition(TFModelLayers.BOAR_BABY, () -> BoarModel.create().apply(BoarModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(TFModelLayers.BUNNY, BunnyModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GOLEM, CarminiteGolemModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GHASTGUARD, TFGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GHASTLING, TFGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.CHAIN, ChainModel::create);
		event.registerLayerDefinition(TFModelLayers.CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create);
		event.registerLayerDefinition(TFModelLayers.DEATH_TOME, DeathTomeModel::create);
		event.registerLayerDefinition(TFModelLayers.DEER, DeerModel::create);
		event.registerLayerDefinition(TFModelLayers.DEER_BABY, () -> DeerModel.create().apply(DeerModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(TFModelLayers.FIRE_BEETLE, FireBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.GIANT_MINER, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.HARBINGER_CUBE, HarbingerCubeModel::create);
		event.registerLayerDefinition(TFModelLayers.HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.HELMET_CRAB, HelmetCrabModel::create);
		event.registerLayerDefinition(TFModelLayers.HOSTILE_WOLF, () -> LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32));
		event.registerLayerDefinition(TFModelLayers.HYDRA_HEAD, HydraHeadModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA, HydraModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA_MORTAR, HydraMortarModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA_NECK, HydraNeckModel::create);
		event.registerLayerDefinition(TFModelLayers.ICE_CRYSTAL, IceCrystalModel::create);
		event.registerLayerDefinition(TFModelLayers.KING_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.KNIGHT_PHANTOM, KnightPhantomModel::create);
		event.registerLayerDefinition(TFModelLayers.KOBOLD, KoboldModel::create);
		event.registerLayerDefinition(TFModelLayers.LICH_MINION, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.LICH, LichModel::create);
		event.registerLayerDefinition(TFModelLayers.LOWER_GOBLIN_KNIGHT, LowerGoblinKnightModel::create);
		event.registerLayerDefinition(TFModelLayers.LOYAL_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.MAZE_SLIME, SlimeModel::createInnerBodyLayer);
		event.registerLayerDefinition(TFModelLayers.MAZE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
		event.registerLayerDefinition(TFModelLayers.MINOSHROOM, MinoshroomModel::create);
		event.registerLayerDefinition(TFModelLayers.MINOTAUR, MinotaurModel::create);
		event.registerLayerDefinition(TFModelLayers.MIST_WOLF, () -> LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32));
		event.registerLayerDefinition(TFModelLayers.MOSQUITO_SWARM, MosquitoSwarmModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA, NagaModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA_BODY, NagaModel::create);
		event.registerLayerDefinition(TFModelLayers.NOOP, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0));
		event.registerLayerDefinition(TFModelLayers.PENGUIN, PenguinModel::create);
		event.registerLayerDefinition(TFModelLayers.PENGUIN_BABY, () -> PenguinModel.create().apply(PenguinModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(TFModelLayers.PINCH_BEETLE, PinchBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.PROTECTION_BOX, () -> LayerDefinition.create(ProtectionBoxModel.createMesh(), 16, 16));
		event.registerLayerDefinition(TFModelLayers.QUEST_RAM, QuestRamModel::create);
		event.registerLayerDefinition(TFModelLayers.RAVEN, RavenModel::create);
		event.registerLayerDefinition(TFModelLayers.REDCAP, RedcapModel::create);
		event.registerLayerDefinition(TFModelLayers.REDCAP_ARMOR_INNER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.7F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.REDCAP_ARMOR_OUTER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.65F), 0.7F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.RISING_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.ROVING_CUBE, CubeOfAnnihilationModel::create);
		event.registerLayerDefinition(TFModelLayers.SKELETON_DRUID, SkeletonDruidModel::create);
		event.registerLayerDefinition(TFModelLayers.SLIME_BEETLE, SlimeBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.SLIME_BEETLE_TAIL, SlimeBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.SNOW_QUEEN, SnowQueenModel::create);
		event.registerLayerDefinition(TFModelLayers.CHAIN_BLOCK, SpikeBlockModel::create);
		event.registerLayerDefinition(TFModelLayers.SQUIRREL, SquirrelModel::create);
		event.registerLayerDefinition(TFModelLayers.STABLE_ICE_CORE, StableIceCoreModel::create);
		event.registerLayerDefinition(TFModelLayers.SWARM_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.TINY_BIRD, TinyBirdModel::create);
		event.registerLayerDefinition(TFModelLayers.TOWERWOOD_BORER, SilverfishModel::createBodyLayer);
		event.registerLayerDefinition(TFModelLayers.TROLL, TrollModel::create);
		event.registerLayerDefinition(TFModelLayers.UNSTABLE_ICE_CORE, UnstableIceCoreModel::create);
		event.registerLayerDefinition(TFModelLayers.UPPER_GOBLIN_KNIGHT, UpperGoblinKnightModel::create);
		event.registerLayerDefinition(TFModelLayers.UR_GHAST, UrGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.WINTER_WOLF, () -> LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32));
		event.registerLayerDefinition(TFModelLayers.WRAITH, WraithModel::create);
		event.registerLayerDefinition(TFModelLayers.YETI, YetiModel::create);

		event.registerLayerDefinition(TFModelLayers.CICADA, CicadaModel::create);
		event.registerLayerDefinition(TFModelLayers.FIREFLY, FireflyModel::create);
		event.registerLayerDefinition(TFModelLayers.KEEPSAKE_CASKET, () -> KeepsakeCasketModel.create(true));
		event.registerLayerDefinition(TFModelLayers.SKULL_CHEST, () -> KeepsakeCasketModel.create(false));
		event.registerLayerDefinition(TFModelLayers.MOONWORM, MoonwormModel::create);
		event.registerLayerDefinition(TFModelLayers.BRAZIER, BrazierModel::create);

		event.registerLayerDefinition(TFModelLayers.RED_THREAD, RedThreadModel::create);

		event.registerLayerDefinition(TFModelLayers.KNIGHTMETAL_SHIELD, KnightmetalShieldModel::create);

		// Boats
		event.registerLayerDefinition(TFModelLayers.TWILIGHT_OAK_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.TWILIGHT_OAK_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.CANOPY_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.CANOPY_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.MANGROVE_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.MANGROVE_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.DARK_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.DARK_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.TIME_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.TIME_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.TRANSFORMATION_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.TRANSFORMATION_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.MINING_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.MINING_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.SORTING_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.SORTING_CHEST_BOAT, BoatModel::createChestBoatModel);
	}

	private void registerParticleFactories(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(TFParticleType.LARGE_FLAME.get(), LargeFlameParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.LEAF_RUNE.get(), LeafRuneParticle.Factory::new);
		event.registerSpecial(TFParticleType.BOSS_TEAR.get(), new GhastTearParticle.Factory());
		event.registerSpriteSet(TFParticleType.GHAST_TRAP.get(), GhastTrapParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.PROTECTION.get(), ProtectionParticle.Factory::new); //probably not a good idea, but worth a shot
		event.registerSpriteSet(TFParticleType.SNOW.get(), SnowParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.SNOW_GUARDIAN.get(), SnowGuardianParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.SNOW_WARNING.get(), SnowWarningParticle.SimpleFactory::new);
		event.registerSpriteSet(TFParticleType.EXTENDED_SNOW_WARNING.get(), SnowWarningParticle.ExtendedFactory::new);
		event.registerSpriteSet(TFParticleType.ICE_BEAM.get(), IceBeamParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.ANNIHILATE.get(), AnnihilateParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.PERFECT_DODGE.get(), PerfectDodgeParticle.Provider::new);
		event.registerSpriteSet(TFParticleType.DOUBLE_JUMP.get(), DoubleJumpParticle.Provider::new);
		event.registerSpriteSet(TFParticleType.HUGE_SMOKE.get(), SmokeScaleParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.FIREFLY.get(), FireflyParticle.StationaryProvider::new);
		event.registerSpriteSet(TFParticleType.WANDERING_FIREFLY.get(), FireflyParticle.WanderingProvider::new);
		event.registerSpriteSet(TFParticleType.PARTICLE_SPAWNER_FIREFLY.get(), FireflyParticle.ParticleSpawnerProvider::new);
		event.registerSpriteSet(TFParticleType.FALLEN_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.DIM_FLAME.get(), FlameParticle.SmallFlameProvider::new);
		event.registerSpriteSet(TFParticleType.OMINOUS_FLAME.get(), FlameParticle.SmallFlameProvider::new);
		event.registerSpriteSet(TFParticleType.SORTING_PARTICLE.get(), SortingParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.TRANSFORMATION_PARTICLE.get(), TransformationParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.LOG_CORE_PARTICLE.get(), LogCoreParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.CLOUD_PUFF.get(), CloudPuffParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.DRYING_RACK.get(), DryingRackParticle.Provider::new);
		event.registerSpriteSet(TFParticleType.MAGIC_EFFECT.get(), MagicEffectParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.ANGRY_LICH.get(), AngryLichParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.TWILIGHT_ORB.get(), (SpriteSet sprite) -> new CustomTextureParticle.Factory(sprite, true));
		event.registerSpriteSet(TFParticleType.SHIELD_BREAK.get(), CustomTextureParticle.ShieldBreak::new);
	}

	private void registerClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerBlock(new IClientBlockExtensions() {
			@Override
			public boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) {
				if (level.getRandom().nextBoolean() && target instanceof BlockHitResult hitResult) { // No clue why the parameter isn't blockHitResult, this should be always true, but we check just in case
					BlockPos pos = hitResult.getBlockPos();
					BlockState blockstate = level.getBlockState(pos);
					if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
						Direction side = hitResult.getDirection();

						int posX = pos.getX();
						int posY = pos.getY();
						int posZ = pos.getZ();

						AABB aabb = blockstate.getShape(level, pos).bounds();
						double x = (double) posX + level.getRandom().nextDouble() * (aabb.maxX - aabb.minX - (double) 0.2F) + (double) 0.1F + aabb.minX;
						double y = (double) posY + level.getRandom().nextDouble() * (aabb.maxY - aabb.minY - (double) 0.2F) + (double) 0.1F + aabb.minY;
						double z = (double) posZ + level.getRandom().nextDouble() * (aabb.maxZ - aabb.minZ - (double) 0.2F) + (double) 0.1F + aabb.minZ;

						if (side == Direction.DOWN) y = (double) posY + aabb.minY - (double) 0.1F;
						if (side == Direction.UP) y = (double) posY + aabb.maxY + (double) 0.1F;

						if (side == Direction.NORTH) z = (double) posZ + aabb.minZ - (double) 0.1F;
						if (side == Direction.SOUTH) z = (double) posZ + aabb.maxZ + (double) 0.1F;

						if (side == Direction.WEST) x = (double) posX + aabb.minX - (double) 0.1F;
						if (side == Direction.EAST) x = (double) posX + aabb.maxX + (double) 0.1F;

						Particle particle = Minecraft.getInstance().particleEngine.createParticle(TFParticleType.CLOUD_PUFF.get(), x, y, z, (double) side.getStepX() * 0.01D, (double) side.getStepY() * 0.01D, (double) side.getStepZ() * 0.01D);
						if (particle == null) return true;
						manager.add(particle);
					}
				}
				return true;
			}

			@Override
			public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine manager) {
				state.getShape(level, pos).forAllBoxes((boxX, boxY, boxZ, boxX1, boxY1, boxZ1) -> {
					double xSize = Math.min(1.0D, boxX1 - boxX);
					double ySize = Math.min(1.0D, boxY1 - boxY);
					double zSize = Math.min(1.0D, boxZ1 - boxZ);

					int xMax = Math.max(2, Mth.ceil(xSize / 0.25D));
					int yMax = Math.max(2, Mth.ceil(ySize / 0.25D));
					int zMax = Math.max(2, Mth.ceil(zSize / 0.25D));

					for (int xSlice = 0; xSlice < xMax; ++xSlice) {
						if (level.getRandom().nextInt(3) == 1) continue;
						for (int ySlice = 0; ySlice < yMax; ++ySlice) {
							if (level.getRandom().nextInt(3) == 1) continue;
							for (int zSlice = 0; zSlice < zMax; ++zSlice) {
								if (level.getRandom().nextInt(3) == 1) continue;

								double speedX = ((double) xSlice + 0.5D) / (double) xMax;
								double speedY = ((double) ySlice + 0.5D) / (double) yMax;
								double speedZ = ((double) zSlice + 0.5D) / (double) zMax;

								double x = speedX * xSize + boxX;
								double y = speedY * ySize + boxY;
								double z = speedZ * zSize + boxZ;

								speedX = (speedX - 0.5D) * 0.05D;
								speedY = (speedY - 0.5D) * 0.05D;
								speedZ = (speedZ - 0.5D) * 0.05D;

								Particle particle = Minecraft.getInstance().particleEngine.createParticle(TFParticleType.CLOUD_PUFF.get(), (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, speedX, speedY, speedZ);
								if (particle == null) return;
								manager.add(particle);
							}
						}
					}
				});
				return true;
			}
		}, TFBlocks.WISPY_CLOUD.get(), TFBlocks.RAINY_CLOUD.get(), TFBlocks.SNOWY_CLOUD.get(), TFBlocks.FLUFFY_CLOUD.get());

		event.registerItem(
			new ArcticArmorItem.ArmorRender(),
			TFItems.ARCTIC_HELMET.get(), TFItems.ARCTIC_CHESTPLATE.get(), TFItems.ARCTIC_LEGGINGS.get(), TFItems.ARCTIC_BOOTS.get());
		event.registerItem(
			new TFSimpleArmorRenderer(FieryArmorModel::new, TFModelLayers.FIERY_ARMOR_INNER, TFModelLayers.FIERY_ARMOR_OUTER),
			TFItems.FIERY_HELMET.get(), TFItems.FIERY_CHESTPLATE.get(), TFItems.FIERY_LEGGINGS.get(), TFItems.FIERY_BOOTS.get()
		);
		event.registerItem(
			new TravellersArmorItem.ArmorRender(),
			TFItems.TRAVELLERS_GOGGLES.get(), TFItems.TRAVELLERS_VEST.get(), TFItems.TRAVELLERS_GLOVES.get(), TFItems.TRAVELLERS_WINGS.get(), TFItems.TRAVELLERS_BELT.get(), TFItems.TRAVELLERS_BOOTS.get()
		);
		event.registerItem(
			new TFSimpleArmorRenderer(TFArmorModel::new, TFModelLayers.KNIGHTMETAL_ARMOR_INNER, TFModelLayers.KNIGHTMETAL_ARMOR_OUTER),
			TFItems.KNIGHTMETAL_HELMET.get(), TFItems.KNIGHTMETAL_CHESTPLATE.get(), TFItems.KNIGHTMETAL_LEGGINGS.get(), TFItems.KNIGHTMETAL_BOOTS.get()
		);
		event.registerItem(
			new TFSimpleArmorRenderer(TFArmorModel::new, TFModelLayers.PHANTOM_ARMOR_INNER, TFModelLayers.PHANTOM_ARMOR_OUTER),
			TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get()
		);
		event.registerItem(
			new TFSimpleArmorRenderer(YetiArmorModel::new, TFModelLayers.YETI_ARMOR_INNER, TFModelLayers.YETI_ARMOR_OUTER),
			TFItems.YETI_HELMET.get(), TFItems.YETI_CHESTPLATE.get(), TFItems.YETI_LEGGINGS.get(), TFItems.YETI_BOOTS.get()
		);
	}

	private void registerMapDecorators(RegisterMapDecorationRenderersEvent event) {
		event.register(MapDecorationTypes.PLAYER.value(), new MagicMapPlayerIconRenderer());
		event.register(TFMapDecorations.QUEST_GROVE.get(), new ConqueredMapIconRenderer());
		event.register(TFMapDecorations.NAGA_COURTYARD.get(), new ConqueredMapIconRenderer());
		event.register(TFMapDecorations.LICH_TOWER.get(), new ConqueredMapIconRenderer());
		event.register(TFMapDecorations.LABYRINTH.get(), new ConqueredMapIconRenderer());
		event.register(TFMapDecorations.HYDRA_LAIR.get(), new ConqueredMapIconRenderer());
		event.register(TFMapDecorations.KNIGHT_STRONGHOLD.get(), new ConqueredMapIconRenderer());
		event.register(TFMapDecorations.DARK_TOWER.get(), new ConqueredMapIconRenderer());
		event.register(TFMapDecorations.YETI_LAIR.get(), new ConqueredMapIconRenderer());
		event.register(TFMapDecorations.AURORA_PALACE.get(), new ConqueredMapIconRenderer());
		event.register(TFMapDecorations.FINAL_CASTLE.get(), new ConqueredMapIconRenderer());
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void attachRenderLayers(EntityRenderersEvent.AddLayers event) {
		BakedMultiPartRenderers.bakeMultiPartRenderers(event.getContext());
		for (EntityType<?> type : event.getEntityTypes()) {
			var renderer = event.getRenderer(type);
			if (renderer instanceof LivingEntityRenderer living) {
				attachRenderLayers(living);
			}
		}

		event.getSkins().forEach(renderer -> {
			AvatarRenderer<AbstractClientPlayer> skin = event.getPlayerRenderer(renderer);
			attachRenderLayers(Objects.requireNonNull(skin));
		});
	}

	private <T extends LivingEntityRenderState, M extends EntityModel<T>> void attachRenderLayers(LivingEntityRenderer<?, T, M> renderer) {
		renderer.addLayer(new ShieldLayer<>(renderer));
		renderer.addLayer(new IceLayer<>(renderer));
	}

	public void registerCustomEnvironmentEffects(RegisterCustomEnvironmentEffectRendererEvent event) {
		event.registerSkyboxRenderer(TFDimension.DIMENSION_RENDERER, TwilightForestRenderInfo.INSTANCE);
		event.registerWeatherEffectRenderer(TFDimension.DIMENSION_RENDERER, TwilightForestRenderInfo.INSTANCE);
	}

	public static boolean isOptifinePresent() {
		return optifinePresent;
	}
}
