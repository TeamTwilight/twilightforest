package twilightforest.client.event;

import com.google.common.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.map.RegisterMapDecorationRenderersEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import twilightforest.TwilightForestMod;
import twilightforest.client.*;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.*;
import twilightforest.client.model.block.BrazierModel;
import twilightforest.client.model.block.ReactorDebrisModel;
import twilightforest.client.model.block.aurorablock.NoiseVaryingModelLoader;
import twilightforest.client.model.block.connected.ConnectedTextureModelLoader;
import twilightforest.client.model.block.forcefield.ForceFieldModelLoader;
import twilightforest.client.model.block.giantblock.GiantBlockModelLoader;
import twilightforest.client.model.block.leaves.BakedLeavesModel;
import twilightforest.client.model.block.patch.PatchModelLoader;
import twilightforest.client.model.entity.*;
import twilightforest.client.particle.*;
import twilightforest.client.properties.*;
import twilightforest.client.renderer.PotionFlaskTooltipComponent;
import twilightforest.client.renderer.TFSimpleArmorRenderer;
import twilightforest.client.renderer.block.*;
import twilightforest.client.renderer.entity.*;
import twilightforest.client.renderer.entity.layers.IceLayer;
import twilightforest.client.renderer.entity.layers.ShieldLayer;
import twilightforest.client.renderer.map.ConqueredMapIconRenderer;
import twilightforest.client.renderer.map.MagicMapPlayerIconRenderer;
import twilightforest.client.renderer.special.*;
import twilightforest.enums.BossVariant;
import twilightforest.init.*;
import twilightforest.item.BrittleFlaskItem;
import twilightforest.item.TrophyItem;
import twilightforest.item.mapdata.TFMagicMapData;
import twilightforest.potions.FrostedEffect;
import twilightforest.util.woods.TFWoodTypes;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class RegistrationEvents {

	private static boolean optifinePresent = false;

	public static void initModBusEvents(IEventBus bus) {
		bus.addListener(EntityRenderersEvent.AddLayers.class, RegistrationEvents::attachRenderLayers);
		bus.addListener(RegistrationEvents::bakeCustomModels);
		bus.addListener(RegistrationEvents::cacheJarLids);
		bus.addListener(RegistrationEvents::clientSetup);
		bus.addListener(RegistrationEvents::registerAdditionalModels);
		bus.addListener(RegistrationEvents::registerClientReloadListeners);
		bus.addListener(RegistrationEvents::registerDimEffects);
		bus.addListener(RegistrationEvents::registerEntityRenderers);
		bus.addListener(RegistrationEvents::registerLayerDefinitions);
		bus.addListener(RegistrationEvents::registerModelLoaders);
		bus.addListener(RegistrationEvents::registerScreens);
		bus.addListener(RegistrationEvents::registerClientExtensions);
		bus.addListener(RegistrationEvents::registerMapDecorators);
		bus.addListener(RegistrationEvents::registerParticleFactories);
		bus.addListener(RegistrationEvents::registerCustomRenderData);
		bus.addListener(RegistrationEvents::registerSpecialModelTypes);
		bus.addListener(RegistrationEvents::registerSpecialModels);

		bus.addListener(RegistrationEvents::registerConditionalProperties);
		bus.addListener(RegistrationEvents::registerRangeProperties);
		bus.addListener(RegistrationEvents::registerSelectProperties);

		bus.addListener(ColorHandler::registerBlockColors);
		bus.addListener(ColorHandler::registerTintSources);

		bus.addListener(OverlayHandler::registerOverlays);

		bus.addListener(TFShaders::registerShaders);

		bus.addListener(RegisterClientTooltipComponentFactoriesEvent.class, event -> event.register(BrittleFlaskItem.Tooltip.class, PotionFlaskTooltipComponent::new));
	}

	private static void registerModelLoaders(ModelEvent.RegisterLoaders event) {
		event.register(TwilightForestMod.prefix("patch"), PatchModelLoader.INSTANCE);
		event.register(TwilightForestMod.prefix("giant_block"), GiantBlockModelLoader.INSTANCE);
		event.register(TwilightForestMod.prefix("force_field"), ForceFieldModelLoader.INSTANCE);
		event.register(TwilightForestMod.prefix("connected_texture_block"), ConnectedTextureModelLoader.INSTANCE);
		event.register(TwilightForestMod.prefix("noise_varying"), NoiseVaryingModelLoader.INSTANCE);
	}

	private static void bakeCustomModels(ModelEvent.ModifyBakingResult event) {
//		ItemProperties.register(TFItems.CRUMBLE_HORN.get(), TwilightForestMod.prefix("tooting"), (stack, world, entity, i) ->
//			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
//		);

		Map<ModelResourceLocation, BakedModel> models = event.getBakingResult().blockStateModels();
		List<Map.Entry<ModelResourceLocation, BakedModel>> leavesModels = models.entrySet().stream()
			.filter(entry -> entry.getKey().id().equals(TwilightForestMod.prefix("fallen_leaves"))).toList();

		leavesModels.forEach(entry -> models.put(entry.getKey(), new BakedLeavesModel(entry.getValue())));

		BakedModel defaultReactorDebrisModel = models.get(new ModelResourceLocation(ResourceLocation.withDefaultNamespace("netherrack"), ""));
		models.put(new ModelResourceLocation(TwilightForestMod.prefix("reactor_debris"), ""), new ReactorDebrisModel(defaultReactorDebrisModel));
	}

	private static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
		event.register(ShieldLayer.LOC);

		for (JarRenderer.LidResource lid : JarRenderer.LID_LOCATION_LIST.get()) {
			ResourceLocation location = lid.resourceLocation();
			String name = location.getPath();
			if (lid.customPath() != null) name = lid.customPath();
			event.register(TwilightForestMod.prefix("block/lid/" + name));
		}
	}

	private static void cacheJarLids(ModelEvent.BakingCompleted event) {
		JarRenderer.LID_LOCATION_LIST.get().forEach((lid) -> {
			String name = lid.resourceLocation().getPath();
			if (lid.customPath() != null) name = lid.customPath();
			JarRenderer.LIDS.put(lid.lid(), event.getBakingResult().standaloneModels().get(TwilightForestMod.prefix("block/lid/" + name)));
		});
	}

	private static void registerDimEffects(RegisterDimensionSpecialEffectsEvent event) {
		event.register(TFDimension.DIMENSION_RENDERER, new TwilightForestRenderInfo(128.0F, false, DimensionSpecialEffects.SkyType.OVERWORLD, false, false));
	}

	private static void clientSetup(FMLClientSetupEvent evt) {
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

	private static void registerClientReloadListeners(AddClientReloadListenersEvent event) {
		MagicPaintingTextureManager.instance = new MagicPaintingTextureManager(Minecraft.getInstance().getTextureManager());
		event.addListener(TwilightForestMod.prefix("magic_paintings"), MagicPaintingTextureManager.instance);
		event.addListener(TwilightForestMod.prefix("texture_generators"), TextureGeneratorReloadListener.INSTANCE);
		event.addListener(TwilightForestMod.prefix("armor_models"), new TFSimpleArmorRenderer.ResourceReloadListener());
	}

	private static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(TFMenuTypes.UNCRAFTING.get(), UncraftingScreen::new);
	}

	private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(TFEntities.BOAR.get(), BoarRenderer::new);
		event.registerEntityRenderer(TFEntities.BIGHORN_SHEEP.get(), BighornRenderer::new);
		event.registerEntityRenderer(TFEntities.DEER.get(), DeerRenderer::new);
		event.registerEntityRenderer(TFEntities.REDCAP.get(), RedcapRenderer::new);
		event.registerEntityRenderer(TFEntities.SKELETON_DRUID.get(), SkeletonDruidRenderer::new);
		event.registerEntityRenderer(TFEntities.HOSTILE_WOLF.get(), HostileWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.WRAITH.get(), WraithRenderer::new);
		event.registerEntityRenderer(TFEntities.HYDRA.get(), HydraRenderer::new);
		event.registerEntityRenderer(TFEntities.LICH.get(), LichRenderer::new);
		event.registerEntityRenderer(TFEntities.PENGUIN.get(), m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), 0.375F, "penguin.png"));
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
		event.registerEntityRenderer(TFEntities.CARMINITE_GHASTLING.get(), m -> new TFGhastRenderer<>(m, new TFGhastModel(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
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
		event.registerEntityRenderer(TFEntities.LICH_BOMB.get(), c -> new CustomProjectileTextureRenderer(c, ResourceLocation.withDefaultNamespace("textures/item/magma_cream.png"), 1.0F, true, true));
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

		//boats
		event.registerEntityRenderer(TFEntities.TWILIGHT_OAK_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TWILIGHT_OAK_BOAT));
		event.registerEntityRenderer(TFEntities.CANOPY_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.CANOPY_BOAT));
		event.registerEntityRenderer(TFEntities.MANGROVE_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MANGROVE_BOAT));
		event.registerEntityRenderer(TFEntities.DARK_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.DARK_BOAT));
		event.registerEntityRenderer(TFEntities.TIME_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TIME_BOAT));
		event.registerEntityRenderer(TFEntities.TRANSFORMATION_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TRANSFORMATION_BOAT));
		event.registerEntityRenderer(TFEntities.MINING_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MINING_BOAT));
		event.registerEntityRenderer(TFEntities.SORTING_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.SORTING_BOAT));
		event.registerEntityRenderer(TFEntities.VEILWOOD_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.VEILWOOD_BOAT));
		event.registerEntityRenderer(TFEntities.TWILIGHT_OAK_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TWILIGHT_OAK_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.CANOPY_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.CANOPY_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.MANGROVE_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MANGROVE_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.DARK_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.DARK_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.TIME_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TIME_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.TRANSFORMATION_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TRANSFORMATION_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.MINING_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MINING_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.SORTING_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.SORTING_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.VEILWOOD_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.VEILWOOD_CHEST_BOAT));


		// Block Entities
		event.registerBlockEntityRenderer(TFBlockEntities.FIREFLY.get(), FireflyRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.CICADA.get(), CicadaRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.MOONWORM.get(), MoonwormRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.WEBWORM.get(), WebwormRenderer::new);
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
	}

	private static void registerConditionalProperties(RegisterConditionalItemModelPropertyEvent event) {
		event.register(TwilightForestMod.prefix("natural_dimension"), NaturalDimension.TYPE);
		event.register(TwilightForestMod.prefix("moonworm_queen_pulse"), MoonwormQueenPulse.TYPE);
		event.register(TwilightForestMod.prefix("ore_meter_flash"), OreMeterFlash.TYPE);
	}

	private static void registerRangeProperties(RegisterRangeSelectItemModelPropertyEvent event) {
		event.register(TwilightForestMod.prefix("potion_flask_dosage"), PotionFlaskDosage.TYPE);
		event.register(TwilightForestMod.prefix("potion_flask_damage"), PotionFlaskDamage.TYPE);
	}

	private static void registerSelectProperties(RegisterSelectItemModelPropertyEvent event) {
		event.register(TwilightForestMod.prefix("experiment_115_variant"), Experiment115Type.TYPE);
	}

	private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(TFModelLayers.ARCTIC_ARMOR_INNER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.ARCTIC_ARMOR_OUTER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.FIERY_ARMOR_INNER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.FIERY_ARMOR_OUTER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32));
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
		event.registerLayerDefinition(TFModelLayers.BUNNY, BunnyModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GOLEM, CarminiteGolemModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GHASTGUARD, TFGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GHASTLING, TFGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.CHAIN, ChainModel::create);
		event.registerLayerDefinition(TFModelLayers.CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create);
		event.registerLayerDefinition(TFModelLayers.DEATH_TOME, DeathTomeModel::create);
		event.registerLayerDefinition(TFModelLayers.DEER, DeerModel::create);
		event.registerLayerDefinition(TFModelLayers.FIRE_BEETLE, FireBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.GIANT_MINER, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.HARBINGER_CUBE, HarbingerCubeModel::create);
		event.registerLayerDefinition(TFModelLayers.HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.HELMET_CRAB, HelmetCrabModel::create);
		event.registerLayerDefinition(TFModelLayers.HOSTILE_WOLF, () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
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
		event.registerLayerDefinition(TFModelLayers.MIST_WOLF, () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
		event.registerLayerDefinition(TFModelLayers.MOSQUITO_SWARM, MosquitoSwarmModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA, NagaModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA_BODY, NagaModel::create);
		event.registerLayerDefinition(TFModelLayers.NOOP, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0));
		event.registerLayerDefinition(TFModelLayers.PENGUIN, PenguinModel::create);
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
		event.registerLayerDefinition(TFModelLayers.WINTER_WOLF, () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
		event.registerLayerDefinition(TFModelLayers.WRAITH, WraithModel::create);
		event.registerLayerDefinition(TFModelLayers.YETI, YetiModel::create);

		event.registerLayerDefinition(TFModelLayers.CICADA, CicadaModel::create);
		event.registerLayerDefinition(TFModelLayers.FIREFLY, FireflyModel::create);
		event.registerLayerDefinition(TFModelLayers.KEEPSAKE_CASKET, () -> KeepsakeCasketModel.create(true));
		event.registerLayerDefinition(TFModelLayers.SKULL_CHEST, () -> KeepsakeCasketModel.create(false));
		event.registerLayerDefinition(TFModelLayers.MOONWORM, MoonwormModel::create);
		event.registerLayerDefinition(TFModelLayers.BRAZIER, BrazierModel::create);
		event.registerLayerDefinition(TFModelLayers.WEBWORM, WebwormModel::create);

		event.registerLayerDefinition(TFModelLayers.RED_THREAD, RedThreadModel::create);

		event.registerLayerDefinition(TFModelLayers.KNIGHTMETAL_SHIELD, KnightmetalShieldModel::create);

		Supplier<LayerDefinition> boatModel = BoatModel::createBoatModel;
		Supplier<LayerDefinition> chestBoatModel = BoatModel::createChestBoatModel;

		event.registerLayerDefinition(TFModelLayers.TWILIGHT_OAK_BOAT, boatModel);
		event.registerLayerDefinition(TFModelLayers.CANOPY_BOAT, boatModel);
		event.registerLayerDefinition(TFModelLayers.MANGROVE_BOAT, boatModel);
		event.registerLayerDefinition(TFModelLayers.DARK_BOAT, boatModel);
		event.registerLayerDefinition(TFModelLayers.TIME_BOAT, boatModel);
		event.registerLayerDefinition(TFModelLayers.TRANSFORMATION_BOAT, boatModel);
		event.registerLayerDefinition(TFModelLayers.MINING_BOAT, boatModel);
		event.registerLayerDefinition(TFModelLayers.SORTING_BOAT, boatModel);
		event.registerLayerDefinition(TFModelLayers.VEILWOOD_BOAT, boatModel);

		event.registerLayerDefinition(TFModelLayers.TWILIGHT_OAK_CHEST_BOAT, chestBoatModel);
		event.registerLayerDefinition(TFModelLayers.CANOPY_CHEST_BOAT, chestBoatModel);
		event.registerLayerDefinition(TFModelLayers.MANGROVE_CHEST_BOAT, chestBoatModel);
		event.registerLayerDefinition(TFModelLayers.DARK_CHEST_BOAT, chestBoatModel);
		event.registerLayerDefinition(TFModelLayers.TIME_CHEST_BOAT, chestBoatModel);
		event.registerLayerDefinition(TFModelLayers.TRANSFORMATION_CHEST_BOAT, chestBoatModel);
		event.registerLayerDefinition(TFModelLayers.MINING_CHEST_BOAT, chestBoatModel);
		event.registerLayerDefinition(TFModelLayers.SORTING_CHEST_BOAT, chestBoatModel);
		event.registerLayerDefinition(TFModelLayers.VEILWOOD_CHEST_BOAT, chestBoatModel);
	}

	private static void registerSpecialModelTypes(RegisterSpecialModelRendererEvent event) {
		event.register(TwilightForestMod.prefix("skull_candle"), SkullCandleSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("keepsake_casket"), KeepsakeCasketSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("skull_chest"), SkullChestSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("candelabra"), CandelabraSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("boss_trophy"), TrophySpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("cicada"), CicadaSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("firefly"), FireflySpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("moonworm"), MoonwormSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("webworm"), WebwormSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("knightmetal_shield"), KnightmetalShieldSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("mystic_crown"), MysticCrownSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("mason_jar"), MasonJarSpecialRenderer.Unbaked.MAP_CODEC);
		event.register(TwilightForestMod.prefix("tf_chest"), TFChestSpecialRenderer.Unbaked.MAP_CODEC);
	}

	private static void registerSpecialModels(RegisterSpecialBlockModelRendererEvent event) {
		event.register(TFBlocks.TWILIGHT_OAK_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("twilight_oak/normal")));
		event.register(TFBlocks.CANOPY_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("canopy/normal")));
		event.register(TFBlocks.MANGROVE_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("mangrove/normal")));
		event.register(TFBlocks.DARK_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("darkwood/normal")));
		event.register(TFBlocks.TIME_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("time/normal")));
		event.register(TFBlocks.TRANSFORMATION_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("tranformation/normal")));
		event.register(TFBlocks.MINING_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("mining/normal")));
		event.register(TFBlocks.SORTING_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("sorting/normal")));
		event.register(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("twilight_oak/trapped")));
		event.register(TFBlocks.CANOPY_TRAPPED_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("canopy/trapped")));
		event.register(TFBlocks.MANGROVE_TRAPPED_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("mangrove/trapped")));
		event.register(TFBlocks.DARK_TRAPPED_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("darkwood/trapped")));
		event.register(TFBlocks.TIME_TRAPPED_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("time/trapped")));
		event.register(TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("tranformation/trapped")));
		event.register(TFBlocks.MINING_TRAPPED_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("mining/trapped")));
		event.register(TFBlocks.SORTING_TRAPPED_CHEST.get(), new TFChestSpecialRenderer.Unbaked(TwilightForestMod.prefix("sorting/trapped")));

		event.register(TFBlocks.SKULL_CHEST.get(), new SkullChestSpecialRenderer.Unbaked());
		event.register(TFBlocks.KEEPSAKE_CASKET.get(), new KeepsakeCasketSpecialRenderer.Unbaked());
		event.register(TFBlocks.CANDELABRA.get(), new CandelabraSpecialRenderer.Unbaked());
		event.register(TFBlocks.CICADA.get(), new CicadaSpecialRenderer.Unbaked());
		event.register(TFBlocks.FIREFLY.get(), new FireflySpecialRenderer.Unbaked());
		event.register(TFBlocks.MOONWORM.get(), new MoonwormSpecialRenderer.Unbaked());
		event.register(TFBlocks.WEBWORM.get(), new WebwormSpecialRenderer.Unbaked());

		event.register(TFBlocks.FIREFLY_JAR.get(), new MasonJarSpecialRenderer.Unbaked(TFBlocks.TWILIGHT_OAK_LOG.asItem()));
		event.register(TFBlocks.CICADA_JAR.get(), new MasonJarSpecialRenderer.Unbaked(TFBlocks.CANOPY_LOG.asItem()));
		event.register(TFBlocks.MASON_JAR.get(), new MasonJarSpecialRenderer.Unbaked(TFBlocks.TWILIGHT_OAK_LOG.asItem()));

		event.register(TFBlocks.ZOMBIE_SKULL_CANDLE.get(), new SkullCandleSpecialRenderer.Unbaked(SkullBlock.Types.ZOMBIE));
		event.register(TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get(), new SkullCandleSpecialRenderer.Unbaked(SkullBlock.Types.ZOMBIE));
		event.register(TFBlocks.SKELETON_SKULL_CANDLE.get(), new SkullCandleSpecialRenderer.Unbaked(SkullBlock.Types.SKELETON));
		event.register(TFBlocks.SKELETON_WALL_SKULL_CANDLE.get(), new SkullCandleSpecialRenderer.Unbaked(SkullBlock.Types.SKELETON));
		event.register(TFBlocks.CREEPER_SKULL_CANDLE.get(), new SkullCandleSpecialRenderer.Unbaked(SkullBlock.Types.CREEPER));
		event.register(TFBlocks.CREEPER_WALL_SKULL_CANDLE.get(), new SkullCandleSpecialRenderer.Unbaked(SkullBlock.Types.CREEPER));
		event.register(TFBlocks.WITHER_SKELE_SKULL_CANDLE.get(), new SkullCandleSpecialRenderer.Unbaked(SkullBlock.Types.WITHER_SKELETON));
		event.register(TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get(), new SkullCandleSpecialRenderer.Unbaked(SkullBlock.Types.WITHER_SKELETON));
		event.register(TFBlocks.PLAYER_SKULL_CANDLE.get(), new SkullCandleSpecialRenderer.Unbaked(SkullBlock.Types.PLAYER));
		event.register(TFBlocks.PLAYER_WALL_SKULL_CANDLE.get(), new SkullCandleSpecialRenderer.Unbaked(SkullBlock.Types.PLAYER));
		event.register(TFBlocks.PIGLIN_SKULL_CANDLE.get(), new SkullCandleSpecialRenderer.Unbaked(SkullBlock.Types.PIGLIN));
		event.register(TFBlocks.PIGLIN_WALL_SKULL_CANDLE.get(), new SkullCandleSpecialRenderer.Unbaked(SkullBlock.Types.PIGLIN));

		event.register(TFBlocks.NAGA_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.NAGA));
		event.register(TFBlocks.NAGA_WALL_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.NAGA));
		event.register(TFBlocks.LICH_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.LICH));
		event.register(TFBlocks.LICH_WALL_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.LICH));
		event.register(TFBlocks.MINOSHROOM_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.MINOSHROOM));
		event.register(TFBlocks.MINOSHROOM_WALL_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.MINOSHROOM));
		event.register(TFBlocks.HYDRA_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.HYDRA));
		event.register(TFBlocks.HYDRA_WALL_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.HYDRA));
		event.register(TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.KNIGHT_PHANTOM));
		event.register(TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.KNIGHT_PHANTOM));
		event.register(TFBlocks.UR_GHAST_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.UR_GHAST));
		event.register(TFBlocks.UR_GHAST_WALL_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.UR_GHAST));
		event.register(TFBlocks.ALPHA_YETI_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.ALPHA_YETI));
		event.register(TFBlocks.ALPHA_YETI_WALL_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.ALPHA_YETI));
		event.register(TFBlocks.SNOW_QUEEN_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.SNOW_QUEEN));
		event.register(TFBlocks.SNOW_QUEEN_WALL_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.SNOW_QUEEN));
		event.register(TFBlocks.QUEST_RAM_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.QUEST_RAM));
		event.register(TFBlocks.QUEST_RAM_WALL_TROPHY.get(), new TrophySpecialRenderer.Unbaked(BossVariant.QUEST_RAM));
	}

	private static void registerParticleFactories(RegisterParticleProvidersEvent event) {
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
		event.registerSpriteSet(TFParticleType.MAGIC_EFFECT.get(), MagicEffectParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.ANGRY_LICH.get(), AngryLichParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.TWILIGHT_ORB.get(), (SpriteSet sprite) -> new CustomTextureParticle.Factory(sprite, true));
		event.registerSpriteSet(TFParticleType.SHIELD_BREAK.get(), CustomTextureParticle.ShieldBreak::new);
	}

	private static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerBlock(new IClientBlockExtensions() {
			@Override
			public boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) {
				if (level.random.nextBoolean() && target instanceof BlockHitResult hitResult) { // No clue why the parameter isn't blockHitResult, this should be always true, but we check just in case
					BlockPos pos = hitResult.getBlockPos();
					BlockState blockstate = level.getBlockState(pos);
					if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
						Direction side = hitResult.getDirection();

						int posX = pos.getX();
						int posY = pos.getY();
						int posZ = pos.getZ();

						AABB aabb = blockstate.getShape(level, pos).bounds();
						double x = (double) posX + level.random.nextDouble() * (aabb.maxX - aabb.minX - (double) 0.2F) + (double) 0.1F + aabb.minX;
						double y = (double) posY + level.random.nextDouble() * (aabb.maxY - aabb.minY - (double) 0.2F) + (double) 0.1F + aabb.minY;
						double z = (double) posZ + level.random.nextDouble() * (aabb.maxZ - aabb.minZ - (double) 0.2F) + (double) 0.1F + aabb.minZ;

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
						if (level.random.nextInt(3) == 1) continue;
						for (int ySlice = 0; ySlice < yMax; ++ySlice) {
							if (level.random.nextInt(3) == 1) continue;
							for (int zSlice = 0; zSlice < zMax; ++zSlice) {
								if (level.random.nextInt(3) == 1) continue;

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

//		event.registerItem(ISTER.CLIENT_ITEM_EXTENSION,
//			TFBlocks.CICADA.asItem(), TFBlocks.FIREFLY.asItem(), TFBlocks.MOONWORM.asItem(), TFBlocks.SKULL_CHEST.asItem(), TFBlocks.KEEPSAKE_CASKET.asItem(), TFBlocks.CANDELABRA.asItem(), TFBlocks.BRAZIER.asItem(),
//			TFItems.CICADA_JAR.get(), TFItems.FIREFLY_JAR.get(), TFItems.MASON_JAR.get(), TFItems.KNIGHTMETAL_SHIELD.get(), TFItems.MYSTIC_CROWN.value(),
//			TFBlocks.TWILIGHT_OAK_CHEST.asItem(), TFBlocks.CANOPY_CHEST.asItem(), TFBlocks.MANGROVE_CHEST.asItem(), TFBlocks.DARK_CHEST.asItem(), TFBlocks.TIME_CHEST.asItem(), TFBlocks.TRANSFORMATION_CHEST.asItem(), TFBlocks.MINING_CHEST.asItem(), TFBlocks.SORTING_CHEST.asItem(),
//			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.asItem(), TFBlocks.CANOPY_TRAPPED_CHEST.asItem(), TFBlocks.MANGROVE_TRAPPED_CHEST.asItem(), TFBlocks.DARK_TRAPPED_CHEST.asItem(), TFBlocks.TIME_TRAPPED_CHEST.asItem(), TFBlocks.TRANSFORMATION_TRAPPED_CHEST.asItem(), TFBlocks.MINING_TRAPPED_CHEST.asItem(), TFBlocks.SORTING_TRAPPED_CHEST.asItem(),
//			TFItems.NAGA_TROPHY.get(), TFItems.LICH_TROPHY.get(), TFItems.MINOSHROOM_TROPHY.get(), TFItems.HYDRA_TROPHY.get(), TFItems.KNIGHT_PHANTOM_TROPHY.get(), TFItems.UR_GHAST_TROPHY.get(), TFItems.ALPHA_YETI_TROPHY.get(), TFItems.SNOW_QUEEN_TROPHY.get(), TFItems.QUEST_RAM_TROPHY.get(),
//			TFItems.CREEPER_SKULL_CANDLE.get(), TFItems.PIGLIN_SKULL_CANDLE.get(), TFItems.PLAYER_SKULL_CANDLE.get(), TFItems.SKELETON_SKULL_CANDLE.get(), TFItems.WITHER_SKELETON_SKULL_CANDLE.get(), TFItems.ZOMBIE_SKULL_CANDLE.get()
//		);

		event.registerItem(
			new TFSimpleArmorRenderer(HumanoidArmorModel::new, TFModelLayers.ARCTIC_ARMOR_INNER, TFModelLayers.ARCTIC_ARMOR_OUTER),
			TFItems.ARCTIC_HELMET.get(), TFItems.ARCTIC_CHESTPLATE.get(), TFItems.ARCTIC_LEGGINGS.get(), TFItems.ARCTIC_BOOTS.get());
		event.registerItem(
			new TFSimpleArmorRenderer(FieryArmorModel::new, TFModelLayers.FIERY_ARMOR_INNER, TFModelLayers.FIERY_ARMOR_OUTER),
			TFItems.FIERY_HELMET.get(), TFItems.FIERY_CHESTPLATE.get(), TFItems.FIERY_LEGGINGS.get(), TFItems.FIERY_BOOTS.get()
		);
		event.registerItem(
			new TFSimpleArmorRenderer(HumanoidArmorModel::new, TFModelLayers.KNIGHTMETAL_ARMOR_INNER, TFModelLayers.KNIGHTMETAL_ARMOR_OUTER),
			TFItems.KNIGHTMETAL_HELMET.get(), TFItems.KNIGHTMETAL_CHESTPLATE.get(), TFItems.KNIGHTMETAL_LEGGINGS.get(), TFItems.KNIGHTMETAL_BOOTS.get()
		);
		event.registerItem(
			new TFSimpleArmorRenderer(HumanoidArmorModel::new, TFModelLayers.PHANTOM_ARMOR_INNER, TFModelLayers.PHANTOM_ARMOR_OUTER),
			TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get()
		);
		event.registerItem(
			new TFSimpleArmorRenderer(HumanoidArmorModel::new, TFModelLayers.YETI_ARMOR_INNER, TFModelLayers.YETI_ARMOR_OUTER),
			TFItems.YETI_HELMET.get(), TFItems.YETI_CHESTPLATE.get(), TFItems.YETI_LEGGINGS.get(), TFItems.YETI_BOOTS.get()
		);
	}

	private static void registerMapDecorators(RegisterMapDecorationRenderersEvent event) {
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

	@SuppressWarnings("unchecked")//Should be fine…?
	private static <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<S>> void attachRenderLayers(EntityRenderersEvent.AddLayers event) {
		BakedMultiPartRenderers.bakeMultiPartRenderers(event.getContext());
		for (EntityType<?> type : event.getEntityTypes()) {
			var renderer = event.getRenderer(type);
			if (renderer instanceof LivingEntityRenderer<?, ?, ?> living) {
				attachRenderLayers((LivingEntityRenderer<T, S, M>) living);
			}
		}

		event.getSkins().forEach(renderer -> {
			LivingEntityRenderer<Player, PlayerRenderState, EntityModel<PlayerRenderState>> skin = event.getSkin(renderer);
			attachRenderLayers(Objects.requireNonNull(skin));
		});
	}

	private static <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<S>> void attachRenderLayers(LivingEntityRenderer<T, S, M> renderer) {
		renderer.addLayer(new ShieldLayer<>(renderer));
		renderer.addLayer(new IceLayer<>(renderer));
	}

	public static boolean isOptifinePresent() {
		return optifinePresent;
	}

	public static void registerCustomRenderData(RegisterRenderStateModifiersEvent event) {
		event.registerEntityModifier(new TypeToken<LivingEntityRenderer<?, ?, ?>>() {}, (living, state) -> state.setRenderData(ShieldLayer.SHIELD_COUNT_KEY, ShieldLayer.getShieldCount(living)));
		event.registerEntityModifier(new TypeToken<LivingEntityRenderer<?, ?, ?>>() {}, (living, state) -> {
			AttributeInstance speed = living.getAttribute(Attributes.MOVEMENT_SPEED);
			if (speed == null) return;

			AttributeModifier frost = speed.getModifier(FrostedEffect.MOVEMENT_SPEED_MODIFIER);
			if (frost == null) return;

            state.setRenderData(IceLayer.FROST_COUNT_KEY, frost.amount());
			state.setRenderData(IceLayer.FROST_ID_KEY, living.getId());
        });
		event.registerEntityModifier(new TypeToken<LivingEntityRenderer<?, ?, ?>>() {}, (living, state) -> state.setRenderData(ClientEvents.HEAD_KEY, living.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof TrophyItem || ClientEvents.areCuriosEquipped(living)));

		event.registerMapModifier((data, state) -> state.setRenderData(TFMagicMapData.MAGIC_MAP_KEY, data instanceof TFMagicMapData));
		event.registerMapModifier((data, state) -> state.setRenderData(TFMagicMapData.CONQUERED_STRUCTURES_KEY, data instanceof TFMagicMapData map ? map.conqueredStructures : List.of()));
	}
}
