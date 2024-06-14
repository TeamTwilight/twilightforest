package twilightforest.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import twilightforest.TwilightForestMod;
import twilightforest.client.*;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.*;
import twilightforest.client.model.block.aurorablock.NoiseVaryingModelLoader;
import twilightforest.client.model.block.doors.CastleDoorModelLoader;
import twilightforest.client.model.block.forcefield.ForceFieldModelLoader;
import twilightforest.client.model.block.giantblock.GiantBlockModelLoader;
import twilightforest.client.model.block.leaves.BakedLeavesModel;
import twilightforest.client.model.block.patch.PatchModelLoader;
import twilightforest.client.model.entity.*;
import twilightforest.client.model.item.TrollsteinnModel;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.entity.*;
import twilightforest.client.renderer.tileentity.*;
import twilightforest.entity.TwilightBoat;
import twilightforest.init.*;
import twilightforest.util.TFWoodTypes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RegistrationEvents {

	private static boolean optifinePresent = false;

	public static void initModBusEvents(IEventBus bus) {
		bus.addListener(EntityRenderersEvent.AddLayers.class, RegistrationEvents::attachRenderLayers);
		bus.addListener(RegistrationEvents::bakeCustomModels);
		bus.addListener(RegistrationEvents::clientSetup);
		bus.addListener(RegistrationEvents::registerAdditionalModels);
		bus.addListener(RegistrationEvents::registerClientReloadListeners);
		bus.addListener(RegistrationEvents::registerDimEffects);
		bus.addListener(RegistrationEvents::registerEntityRenderers);
		bus.addListener(RegistrationEvents::registerLayerDefinitions);
		bus.addListener(RegistrationEvents::registerModelLoaders);
		bus.addListener(RegistrationEvents::registerScreens);

		bus.addListener(ColorHandler::registerBlockColors);
		bus.addListener(ColorHandler::registerItemColors);

		bus.addListener(OverlayHandler::registerOverlays);

		bus.addListener(TFShaders::registerShaders);
	}

	private static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
		event.register(TwilightForestMod.prefix("patch"), PatchModelLoader.INSTANCE);
		event.register(TwilightForestMod.prefix("giant_block"), GiantBlockModelLoader.INSTANCE);
		event.register(TwilightForestMod.prefix("force_field"), ForceFieldModelLoader.INSTANCE);
		event.register(TwilightForestMod.prefix("castle_door"), CastleDoorModelLoader.INSTANCE);
		event.register(TwilightForestMod.prefix("noise_varying"), NoiseVaryingModelLoader.INSTANCE);
	}

	private static void bakeCustomModels(ModelEvent.ModifyBakingResult event) {
		TFItems.addItemModelProperties();

		Map<ResourceLocation, BakedModel> models = event.getModels();
		List<Map.Entry<ResourceLocation, BakedModel>> leavesModels = models.entrySet().stream()
			.filter(entry -> entry.getKey().getNamespace().equals(TwilightForestMod.ID) && entry.getKey().getPath().contains("leaves") && !entry.getKey().getPath().contains("dark")).toList();

		leavesModels.forEach(entry -> models.put(entry.getKey(), new BakedLeavesModel(entry.getValue())));

		BakedModel oldModel = event.getModels().get(new ModelResourceLocation("twilightforest", "trollsteinn", "inventory"));
		models.put(new ModelResourceLocation("twilightforest", "trollsteinn", "inventory"), new TrollsteinnModel(oldModel));
	}

	private static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
		event.register(ShieldLayer.LOC);
		event.register(new ModelResourceLocation(TwilightForestMod.prefix("trophy"), "inventory"));
		event.register(new ModelResourceLocation(TwilightForestMod.prefix("trophy_minor"), "inventory"));
		event.register(new ModelResourceLocation(TwilightForestMod.prefix("trophy_quest"), "inventory"));
		event.register(new ModelResourceLocation(TwilightForestMod.prefix("trollsteinn_light"), "inventory"));
	}

	private static void registerDimEffects(RegisterDimensionSpecialEffectsEvent event) {
		TFSkyRenderer.createStars();
		event.register(TFDimension.DIMENSION_RENDERER, new TwilightForestRenderInfo(128.0F, false, DimensionSpecialEffects.SkyType.NONE, false, false));
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

	private static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
		((ReloadableResourceManager)Minecraft.getInstance().getResourceManager()).listeners.addFirst(JappaPackReloadListener.INSTANCE);
		MagicPaintingTextureManager.instance = new MagicPaintingTextureManager(Minecraft.getInstance().getTextureManager());
		event.registerReloadListener(MagicPaintingTextureManager.instance);
	}

	private static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(TFMenuTypes.UNCRAFTING.get(), UncraftingScreen::new);
	}

	private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		boolean jappa = JappaPackReloadListener.INSTANCE.isJappaPackLoaded();
		event.registerEntityRenderer(TFEntities.BOAT.get(), m -> new TwilightBoatRenderer(m, false));
		event.registerEntityRenderer(TFEntities.CHEST_BOAT.get(), m -> new TwilightBoatRenderer(m, true));
		event.registerEntityRenderer(TFEntities.BOAR.get(), m -> new BoarRenderer<>(m, new BoarModel<>(m.bakeLayer(TFModelLayers.BOAR))));
		event.registerEntityRenderer(TFEntities.BIGHORN_SHEEP.get(), m -> new BighornRenderer(m, new BighornModel<>(m.bakeLayer(TFModelLayers.BIGHORN_SHEEP)), 0.7F));
		event.registerEntityRenderer(TFEntities.DEER.get(), m -> new TFGenericMobRenderer<>(m, new DeerModel(m.bakeLayer(TFModelLayers.DEER)), 0.7F, "wilddeer.png"));
		event.registerEntityRenderer(TFEntities.REDCAP.get(), m -> new TFBipedRenderer<>(m, new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcap.png"));
		event.registerEntityRenderer(TFEntities.SKELETON_DRUID.get(), m -> new TFBipedRenderer<>(m, new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID)), 0.5F, "skeletondruid.png"));
		event.registerEntityRenderer(TFEntities.HOSTILE_WOLF.get(), HostileWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.WRAITH.get(), m -> new WraithRenderer(m, new WraithModel(m.bakeLayer(TFModelLayers.WRAITH)), 0.5F));
		event.registerEntityRenderer(TFEntities.HYDRA.get(), m -> new HydraRenderer<>(m, new HydraModel(m.bakeLayer(TFModelLayers.HYDRA)), 4.0F));
		event.registerEntityRenderer(TFEntities.LICH.get(), m -> new LichRenderer(m, new LichModel(m.bakeLayer(TFModelLayers.LICH)), 0.6F));
		event.registerEntityRenderer(TFEntities.PENGUIN.get(), m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), 0.375F, "penguin.png"));
		event.registerEntityRenderer(TFEntities.LICH_MINION.get(), m -> new TFBipedRenderer<>(m, new LichMinionModel(m.bakeLayer(TFModelLayers.LICH_MINION)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		event.registerEntityRenderer(TFEntities.LOYAL_ZOMBIE.get(), m -> new TFBipedRenderer<>(m, new LoyalZombieModel(m.bakeLayer(TFModelLayers.LOYAL_ZOMBIE)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		event.registerEntityRenderer(TFEntities.TINY_BIRD.get(), m -> new TinyBirdRenderer<>(m, new TinyBirdModel(m.bakeLayer(TFModelLayers.TINY_BIRD)), 0.3F));
		event.registerEntityRenderer(TFEntities.SQUIRREL.get(), m -> new TFGenericMobRenderer<>(m, new SquirrelModel(m.bakeLayer(TFModelLayers.SQUIRREL)), 0.3F, "squirrel2.png"));
		event.registerEntityRenderer(TFEntities.DWARF_RABBIT.get(), m -> new BunnyRenderer(m, new BunnyModel(m.bakeLayer(TFModelLayers.BUNNY)), 0.3F));
		event.registerEntityRenderer(TFEntities.RAVEN.get(), m -> new BirdRenderer<>(m, new RavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png"));
		event.registerEntityRenderer(TFEntities.QUEST_RAM.get(), m -> new QuestRamRenderer<>(m, new QuestRamModel<>(m.bakeLayer(TFModelLayers.QUEST_RAM))));
		event.registerEntityRenderer(TFEntities.KOBOLD.get(), m -> new TFBipedRenderer<>(m, new KoboldModel(m.bakeLayer(TFModelLayers.KOBOLD)), 0.4F, "kobold.png"));
		//event.registerEntityRenderer(TFEntities.BOGGARD.get(), m -> new RenderTFBiped<>(m, new BipedModel<>(0), 0.625F, "kobold.png"));
		event.registerEntityRenderer(TFEntities.MOSQUITO_SWARM.get(), MosquitoSwarmRenderer::new);
		event.registerEntityRenderer(TFEntities.DEATH_TOME.get(), m -> new TFGenericMobRenderer<>(m, new DeathTomeModel(m.bakeLayer(TFModelLayers.DEATH_TOME)), 0.3F, "textures/entity/enchanting_table_book.png"));
		event.registerEntityRenderer(TFEntities.MINOTAUR.get(), m -> new TFBipedRenderer<>(m, new MinotaurModel(m.bakeLayer(TFModelLayers.MINOTAUR)), 0.625F, "minotaur.png"));
		event.registerEntityRenderer(TFEntities.MINOSHROOM.get(), m -> new MinoshroomRenderer<>(m, new MinoshroomModel<>(m.bakeLayer(TFModelLayers.MINOSHROOM)), 0.625F));
		event.registerEntityRenderer(TFEntities.FIRE_BEETLE.get(), m -> new TFGenericMobRenderer<>(m, new FireBeetleModel(m.bakeLayer(TFModelLayers.FIRE_BEETLE)), 0.8F, "firebeetle.png"));
		event.registerEntityRenderer(TFEntities.SLIME_BEETLE.get(), m ->  new SlimeBeetleRenderer<>(m, new SlimeBeetleModel<>(m.bakeLayer(TFModelLayers.SLIME_BEETLE)), m.bakeLayer(TFModelLayers.SLIME_BEETLE_TAIL), 0.6F));
		event.registerEntityRenderer(TFEntities.PINCH_BEETLE.get(), m -> new TFGenericMobRenderer<>(m, new PinchBeetleModel(m.bakeLayer(TFModelLayers.PINCH_BEETLE)), 0.6F, "pinchbeetle.png"));
		event.registerEntityRenderer(TFEntities.MIST_WOLF.get(), MistWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.CARMINITE_GHASTLING.get(), m -> new TFGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
		event.registerEntityRenderer(TFEntities.CARMINITE_GOLEM.get(), m -> new CarminiteGolemRenderer<>(m, new CarminiteGolemModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GOLEM)), 0.75F));
		event.registerEntityRenderer(TFEntities.TOWERWOOD_BORER.get(), m -> new TFGenericMobRenderer<>(m, new SilverfishModel<>(m.bakeLayer(ModelLayers.SILVERFISH)), 0.3F, "towertermite.png"));
		event.registerEntityRenderer(TFEntities.CARMINITE_GHASTGUARD.get(), m -> new CarminiteGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTGUARD)), 3.0F));
		event.registerEntityRenderer(TFEntities.UR_GHAST.get(), m -> new UrGhastRenderer<>(m, new UrGhastModel(m.bakeLayer(TFModelLayers.UR_GHAST)), 8.0F, 24.0F));
		event.registerEntityRenderer(TFEntities.BLOCKCHAIN_GOBLIN.get(), m -> new BlockChainGoblinRenderer<>(m, new BlockChainGoblinModel<>(m.bakeLayer(TFModelLayers.BLOCKCHAIN_GOBLIN)), 0.4F));
		event.registerEntityRenderer(TFEntities.UPPER_GOBLIN_KNIGHT.get(), m -> new UpperGoblinKnightRenderer<>(m, new UpperGoblinKnightModel(m.bakeLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT)), 0.625F));
		event.registerEntityRenderer(TFEntities.LOWER_GOBLIN_KNIGHT.get(), m -> new TFBipedRenderer<>(m, new LowerGoblinKnightModel(m.bakeLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT)), 0.625F, "doublegoblin.png"));
		event.registerEntityRenderer(TFEntities.HELMET_CRAB.get(), m -> new TFGenericMobRenderer<>(m, new HelmetCrabModel(m.bakeLayer(TFModelLayers.HELMET_CRAB)), 0.625F, "helmetcrab.png"));
		event.registerEntityRenderer(TFEntities.KNIGHT_PHANTOM.get(), m -> new KnightPhantomRenderer(m, new KnightPhantomModel(m.bakeLayer(TFModelLayers.KNIGHT_PHANTOM)), 0.625F));
		event.registerEntityRenderer(TFEntities.NAGA.get(), m -> new NagaRenderer<>(m, new NagaModel<>(m.bakeLayer(TFModelLayers.NAGA)), 1.45F));
		event.registerEntityRenderer(TFEntities.SWARM_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 0.25F, "swarmspider.png", 0.5F));
		event.registerEntityRenderer(TFEntities.KING_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 1.25F, "kingspider.png", 1.9F));
		event.registerEntityRenderer(TFEntities.CARMINITE_BROODLING.get(), m -> new TFSpiderRenderer<>(m, 0.6F, "towerbroodling.png", 0.7F));
		event.registerEntityRenderer(TFEntities.HEDGE_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 0.8F, "hedgespider.png", 1.0F));
		event.registerEntityRenderer(TFEntities.REDCAP_SAPPER.get(), m -> new TFBipedRenderer<>(m, new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcapsapper.png"));
		event.registerEntityRenderer(TFEntities.MAZE_SLIME.get(), m -> new MazeSlimeRenderer(m, 0.625F));
		event.registerEntityRenderer(TFEntities.YETI.get(), m -> new TFBipedRenderer<>(m, new YetiModel<>(m.bakeLayer(TFModelLayers.YETI)), 0.625F, "yeti2.png"));
		event.registerEntityRenderer(TFEntities.PROTECTION_BOX.get(), ProtectionBoxRenderer::new);
		event.registerEntityRenderer(TFEntities.MAGIC_PAINTING.get(), MagicPaintingRenderer::new);
		event.registerEntityRenderer(TFEntities.ALPHA_YETI.get(), m -> new TFBipedRenderer<>(m, new AlphaYetiModel(m.bakeLayer(TFModelLayers.ALPHA_YETI)), 1.75F, "yetialpha.png"));
		event.registerEntityRenderer(TFEntities.WINTER_WOLF.get(), WinterWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.SNOW_GUARDIAN.get(), m -> new SnowGuardianRenderer(m, new NoopModel<>(m.bakeLayer(TFModelLayers.NOOP))));
		event.registerEntityRenderer(TFEntities.STABLE_ICE_CORE.get(), m -> new StableIceCoreRenderer(m, new StableIceCoreModel(m.bakeLayer(TFModelLayers.STABLE_ICE_CORE))));
		event.registerEntityRenderer(TFEntities.UNSTABLE_ICE_CORE.get(), m -> new UnstableIceCoreRenderer<>(m, new UnstableIceCoreModel<>(m.bakeLayer(TFModelLayers.UNSTABLE_ICE_CORE))));
		event.registerEntityRenderer(TFEntities.SNOW_QUEEN.get(), m -> new SnowQueenRenderer<>(m, new SnowQueenModel(m.bakeLayer(TFModelLayers.SNOW_QUEEN))));
		event.registerEntityRenderer(TFEntities.TROLL.get(), m -> new TFBipedRenderer<>(m, new TrollModel(m.bakeLayer(TFModelLayers.TROLL)), 0.625F, "troll.png"));
		event.registerEntityRenderer(TFEntities.GIANT_MINER.get(), TFGiantRenderer::new);
		event.registerEntityRenderer(TFEntities.ARMORED_GIANT.get(), TFGiantRenderer::new);
		event.registerEntityRenderer(TFEntities.ICE_CRYSTAL.get(), IceCrystalRenderer::new);
		event.registerEntityRenderer(TFEntities.CHAIN_BLOCK.get(), BlockChainRenderer::new);
		event.registerEntityRenderer(TFEntities.CUBE_OF_ANNIHILATION.get(), CubeOfAnnihilationRenderer::new);
		event.registerEntityRenderer(TFEntities.HARBINGER_CUBE.get(), m -> new TFGenericMobRenderer<>(m, new HarbingerCubeModel<>(m.bakeLayer(TFModelLayers.HARBINGER_CUBE)), 1.0F, "apocalypse2.png"));
		event.registerEntityRenderer(TFEntities.ADHERENT.get(), AdherentRenderer::new);
		event.registerEntityRenderer(TFEntities.ROVING_CUBE.get(), RovingCubeRenderer::new);
		event.registerEntityRenderer(TFEntities.RISING_ZOMBIE.get(), m -> new TFBipedRenderer<>(m, new RisingZombieModel(m.bakeLayer(TFModelLayers.RISING_ZOMBIE)), new RisingZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new RisingZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		event.registerEntityRenderer(TFEntities.PLATEAU_BOSS.get(), NoopRenderer::new);

		// projectiles
		event.registerEntityRenderer(TFEntities.NATURE_BOLT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.LICH_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/item/twilight_orb.png")));
		event.registerEntityRenderer(TFEntities.WAND_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/item/twilight_orb.png")));
		event.registerEntityRenderer(TFEntities.TOME_BOLT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.HYDRA_MORTAR.get(), HydraMortarRenderer::new);
		event.registerEntityRenderer(TFEntities.SLIME_BLOB.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.MOONWORM_SHOT.get(), MoonwormShotRenderer::new);
		event.registerEntityRenderer(TFEntities.CHARM_EFFECT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.LICH_BOMB.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.THROWN_WEP.get(), ThrownWepRenderer::new);
		event.registerEntityRenderer(TFEntities.FALLING_ICE.get(), FallingIceRenderer::new);
		event.registerEntityRenderer(TFEntities.THROWN_ICE.get(), ThrownIceRenderer::new);
		event.registerEntityRenderer(TFEntities.THROWN_BLOCK.get(), ThrownBlockRenderer::new);
		event.registerEntityRenderer(TFEntities.ICE_SNOWBALL.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.SLIDER.get(), SlideBlockRenderer::new);
		event.registerEntityRenderer(TFEntities.SEEKER_ARROW.get(), DefaultArrowRenderer::new);
		event.registerEntityRenderer(TFEntities.ICE_ARROW.get(), DefaultArrowRenderer::new);

		// Block Entities
		event.registerBlockEntityRenderer(TFBlockEntities.FIREFLY.get(), FireflyTileEntityRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.CICADA.get(), CicadaTileEntityRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.MOONWORM.get(), MoonwormTileEntityRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.TROPHY.get(), TrophyTileEntityRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.TF_SIGN.get(), SignRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.TF_HANGING_SIGN.get(), HangingSignRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.TF_CHEST.get(), TFChestTileEntityRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.TF_TRAPPED_CHEST.get(), TFChestTileEntityRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.KEEPSAKE_CASKET.get(), CasketTileEntityRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.SKULL_CANDLE.get(), SkullCandleTileEntityRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.RED_THREAD.get(), RedThreadRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.CANDELABRA.get(), CandelabraTileEntityRenderer::new);
	}

	private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		for (TwilightBoat.Type boatType : TwilightBoat.Type.values()) {
			event.registerLayerDefinition(TwilightBoatRenderer.createBoatModelName(boatType), BoatModel::createBodyModel);
			event.registerLayerDefinition(TwilightBoatRenderer.createChestBoatModelName(boatType), ChestBoatModel::createBodyModel);
		}

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
		event.registerLayerDefinition(TFModelLayers.HYDRA_TROPHY, HydraHeadModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.KNIGHT_PHANTOM_TROPHY, KnightPhantomModel::createTrophy);
		event.registerLayerDefinition(TFModelLayers.LICH_TROPHY, LichModel::create);
		event.registerLayerDefinition(TFModelLayers.MINOSHROOM_TROPHY, MinoshroomModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.NAGA_TROPHY, NagaModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.QUEST_RAM_TROPHY, QuestRamModel::checkForPackTrophyEdition);
		event.registerLayerDefinition(TFModelLayers.SNOW_QUEEN_TROPHY, SnowQueenModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.UR_GHAST_TROPHY, UrGhastModel::create);

		event.registerLayerDefinition(TFModelLayers.ADHERENT, AdherentModel::create);
		event.registerLayerDefinition(TFModelLayers.ALPHA_YETI, AlphaYetiModel::create);
		event.registerLayerDefinition(TFModelLayers.ARMORED_GIANT, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.BIGHORN_SHEEP, BighornModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.BLOCKCHAIN_GOBLIN, BlockChainGoblinModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.BOAR, BoarModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.BUNNY, BunnyModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GOLEM, CarminiteGolemModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GHASTGUARD, TFGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GHASTLING, TFGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.CHAIN, ChainModel::create);
		event.registerLayerDefinition(TFModelLayers.CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create);
		event.registerLayerDefinition(TFModelLayers.DEATH_TOME, DeathTomeModel::create);
		event.registerLayerDefinition(TFModelLayers.DEER, DeerModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.FIRE_BEETLE, FireBeetleModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.GIANT_MINER, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.HARBINGER_CUBE, HarbingerCubeModel::create);
		event.registerLayerDefinition(TFModelLayers.HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.HELMET_CRAB, HelmetCrabModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.HOSTILE_WOLF, () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
		event.registerLayerDefinition(TFModelLayers.HYDRA_HEAD, HydraHeadModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.HYDRA, HydraModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.HYDRA_MORTAR, HydraMortarModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA_NECK, HydraNeckModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.ICE_CRYSTAL, IceCrystalModel::create);
		event.registerLayerDefinition(TFModelLayers.KING_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.KNIGHT_PHANTOM, KnightPhantomModel::create);
		event.registerLayerDefinition(TFModelLayers.KOBOLD, KoboldModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.LICH_MINION, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.LICH, LichModel::create);
		event.registerLayerDefinition(TFModelLayers.LOWER_GOBLIN_KNIGHT, LowerGoblinKnightModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.LOYAL_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.MAZE_SLIME, SlimeModel::createInnerBodyLayer);
		event.registerLayerDefinition(TFModelLayers.MAZE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
		event.registerLayerDefinition(TFModelLayers.MINOSHROOM, MinoshroomModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.MINOTAUR, MinotaurModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.MIST_WOLF, () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
		event.registerLayerDefinition(TFModelLayers.MOSQUITO_SWARM, MosquitoSwarmModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA, NagaModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.NAGA_BODY, NagaModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.NOOP, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0));
		event.registerLayerDefinition(TFModelLayers.PENGUIN, PenguinModel::create);
		event.registerLayerDefinition(TFModelLayers.PINCH_BEETLE, PinchBeetleModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.PROTECTION_BOX, () -> LayerDefinition.create(ProtectionBoxModel.createMesh(), 16, 16));
		event.registerLayerDefinition(TFModelLayers.QUEST_RAM, QuestRamModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.RAVEN, RavenModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.REDCAP, RedcapModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.REDCAP_ARMOR_INNER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.7F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.REDCAP_ARMOR_OUTER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.65F), 0.7F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.RISING_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.ROVING_CUBE, CubeOfAnnihilationModel::create);
		event.registerLayerDefinition(TFModelLayers.SKELETON_DRUID, SkeletonDruidModel::create);
		event.registerLayerDefinition(TFModelLayers.SLIME_BEETLE, SlimeBeetleModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.SLIME_BEETLE_TAIL, SlimeBeetleModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.SNOW_QUEEN, SnowQueenModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.CHAIN_BLOCK, SpikeBlockModel::create);
		event.registerLayerDefinition(TFModelLayers.SQUIRREL, SquirrelModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.STABLE_ICE_CORE, StableIceCoreModel::create);
		event.registerLayerDefinition(TFModelLayers.SWARM_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.TINY_BIRD, TinyBirdModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.TOWERWOOD_BORER, SilverfishModel::createBodyLayer);
		event.registerLayerDefinition(TFModelLayers.TROLL, TrollModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.UNSTABLE_ICE_CORE, UnstableIceCoreModel::create);
		event.registerLayerDefinition(TFModelLayers.UPPER_GOBLIN_KNIGHT, UpperGoblinKnightModel::checkForPack);
		event.registerLayerDefinition(TFModelLayers.UR_GHAST, UrGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.WINTER_WOLF, () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
		event.registerLayerDefinition(TFModelLayers.WRAITH, WraithModel::create);
		event.registerLayerDefinition(TFModelLayers.YETI, YetiModel::create);

		event.registerLayerDefinition(TFModelLayers.CICADA, CicadaModel::create);
		event.registerLayerDefinition(TFModelLayers.FIREFLY, FireflyModel::create);
		event.registerLayerDefinition(TFModelLayers.KEEPSAKE_CASKET, CasketTileEntityRenderer::create);
		event.registerLayerDefinition(TFModelLayers.MOONWORM, MoonwormModel::create);

		event.registerLayerDefinition(TFModelLayers.RED_THREAD, RedThreadModel::create);

		event.registerLayerDefinition(TFModelLayers.KNIGHTMETAL_SHIELD, KnightmetalShieldModel::create);
	}

	private static void attachRenderLayers(EntityRenderersEvent.AddLayers event) {
		for (EntityType<?> type : event.getEntityTypes()) {
			var renderer = event.getRenderer(type);
			if (renderer instanceof LivingEntityRenderer<?, ?> living) {
				attachRenderLayers(living);
			}
		}

		event.getSkins().forEach(renderer -> {
			LivingEntityRenderer<Player, EntityModel<Player>> skin = event.getSkin(renderer);
			attachRenderLayers(Objects.requireNonNull(skin));
		});
	}

	private static <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(LivingEntityRenderer<T, M> renderer) {
		renderer.addLayer(new ShieldLayer<>(renderer));
		renderer.addLayer(new IceLayer<>(renderer));
	}

	public static boolean isOptifinePresent() {
		return optifinePresent;
	}
}
