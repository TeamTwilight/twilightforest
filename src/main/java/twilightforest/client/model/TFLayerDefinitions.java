package twilightforest.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.armor.*;
import twilightforest.client.model.entity.*;
import twilightforest.client.model.entity.newmodels.*;
import twilightforest.client.model.tileentity.*;
import twilightforest.client.model.tileentity.legacy.*;
import twilightforest.client.renderer.entity.TwilightBoatRenderer;
import twilightforest.client.renderer.tileentity.CasketTileEntityRenderer;
import twilightforest.entity.TwilightBoat;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFLayerDefinitions {

	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {

		for(TwilightBoat.Type boatType : TwilightBoat.Type.values()) {
			event.registerLayerDefinition(TwilightBoatRenderer.createBoatModelName(boatType), BoatModel::createBodyModel);
			event.registerLayerDefinition(TwilightBoatRenderer.createChestBoatModelName(boatType), ChestBoatModel::createBodyModel);
		}

		event.registerLayerDefinition(TFModelLayers.ARCTIC_ARMOR_INNER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.ARCTIC_ARMOR_OUTER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.FIERY_ARMOR_INNER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.FIERY_ARMOR_OUTER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.KNIGHTMETAL_ARMOR_INNER, () -> LayerDefinition.create(KnightmetalArmorModel.setup(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.KNIGHTMETAL_ARMOR_OUTER, () -> LayerDefinition.create(KnightmetalArmorModel.setup(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.PHANTOM_ARMOR_INNER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.PHANTOM_ARMOR_OUTER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.YETI_ARMOR_INNER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.YETI_ARMOR_OUTER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));

		event.registerLayerDefinition(TFModelLayers.ALPHA_YETI_TROPHY, AlphaYetiTrophyModel::createHead);
		event.registerLayerDefinition(TFModelLayers.HYDRA_TROPHY, HydraTrophyModel::createHead);
		event.registerLayerDefinition(TFModelLayers.KNIGHT_PHANTOM_TROPHY, KnightPhantomTrophyModel::createHead);
		event.registerLayerDefinition(TFModelLayers.LICH_TROPHY, LichTrophyModel::createHead);
		event.registerLayerDefinition(TFModelLayers.MINOSHROOM_TROPHY, MinoshroomTrophyModel::createHead);
		event.registerLayerDefinition(TFModelLayers.NAGA_TROPHY, NagaTrophyModel::createHead);
		event.registerLayerDefinition(TFModelLayers.QUEST_RAM_TROPHY, QuestRamTrophyModel::createHead);
		event.registerLayerDefinition(TFModelLayers.SNOW_QUEEN_TROPHY, SnowQueenTrophyModel::createHead);
		event.registerLayerDefinition(TFModelLayers.UR_GHAST_TROPHY, UrGhastTrophyModel::createHead);

		event.registerLayerDefinition(TFModelLayers.NEW_HYDRA_TROPHY, HydraTrophyLegacyModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_MINOSHROOM_TROPHY, MinoshroomTrophyLegacyModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_QUEST_RAM_TROPHY, QuestRamTrophyLegacyModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_SNOW_QUEEN_TROPHY, SnowQueenTrophyLegacyModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_UR_GHAST_TROPHY, UrGhastTrophyLegacyModel::create);

		event.registerLayerDefinition(TFModelLayers.ADHERENT, AdherentModel::create);
		event.registerLayerDefinition(TFModelLayers.ALPHA_YETI, AlphaYetiModel::create);
		event.registerLayerDefinition(TFModelLayers.ARMORED_GIANT, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.BIGHORN_SHEEP, NewBighornModel::create);
		event.registerLayerDefinition(TFModelLayers.BIGHORN_SHEEP_FUR, BighornFurLayer::create);
		event.registerLayerDefinition(TFModelLayers.BLOCKCHAIN_GOBLIN, NewBlockChainGoblinModel::create);
		event.registerLayerDefinition(TFModelLayers.BOAR, NewBoarModel::create);
		event.registerLayerDefinition(TFModelLayers.BUNNY, BunnyModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GOLEM, CarminiteGolemModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GHASTGUARD, TFGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GHASTLING, TFGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.CHAIN, ChainModel::create);
		event.registerLayerDefinition(TFModelLayers.CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create);
		event.registerLayerDefinition(TFModelLayers.DEATH_TOME, DeathTomeModel::create);
		event.registerLayerDefinition(TFModelLayers.DEER, NewDeerModel::create);
		event.registerLayerDefinition(TFModelLayers.FIRE_BEETLE, NewFireBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.GIANT_MINER, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.HARBINGER_CUBE, HarbingerCubeModel::create);
		event.registerLayerDefinition(TFModelLayers.HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.HELMET_CRAB, NewHelmetCrabModel::create);
		event.registerLayerDefinition(TFModelLayers.HOSTILE_WOLF, HostileWolfModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA_HEAD, NewHydraHeadModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA, NewHydraModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA_MORTAR, HydraMortarModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA_NECK, NewHydraNeckModel::create);
		event.registerLayerDefinition(TFModelLayers.ICE_CRYSTAL, IceCrystalModel::create);
		event.registerLayerDefinition(TFModelLayers.KING_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.KNIGHT_PHANTOM, KnightPhantomModel::create);
		event.registerLayerDefinition(TFModelLayers.KOBOLD, NewKoboldModel::create);
		event.registerLayerDefinition(TFModelLayers.LICH_MINION, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.LICH, LichModel::create);
		event.registerLayerDefinition(TFModelLayers.LOWER_GOBLIN_KNIGHT, NewLowerGoblinKnightModel::create);
		event.registerLayerDefinition(TFModelLayers.LOYAL_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.MAZE_SLIME, SlimeModel::createInnerBodyLayer);
		event.registerLayerDefinition(TFModelLayers.MAZE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
		event.registerLayerDefinition(TFModelLayers.MINOSHROOM, NewMinoshroomModel::create);
		event.registerLayerDefinition(TFModelLayers.MINOTAUR, NewMinotaurModel::create);
		event.registerLayerDefinition(TFModelLayers.MIST_WOLF, WolfModel::createBodyLayer);
		event.registerLayerDefinition(TFModelLayers.MOSQUITO_SWARM, MosquitoSwarmModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA, NewNagaModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA_BODY, NewNagaModel::create);
		event.registerLayerDefinition(TFModelLayers.NOOP, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0));
		event.registerLayerDefinition(TFModelLayers.PENGUIN, PenguinModel::create);
		event.registerLayerDefinition(TFModelLayers.PINCH_BEETLE, NewPinchBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.PROTECTION_BOX, () -> LayerDefinition.create(ProtectionBoxModel.createMesh(), 16, 16));
		event.registerLayerDefinition(TFModelLayers.QUEST_RAM, NewQuestRamModel::create);
		event.registerLayerDefinition(TFModelLayers.RAVEN, NewRavenModel::create);
		event.registerLayerDefinition(TFModelLayers.REDCAP, NewRedcapModel::create);
		event.registerLayerDefinition(TFModelLayers.REDCAP_ARMOR_INNER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.7F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.REDCAP_ARMOR_OUTER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.65F), 0.7F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.RISING_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.ROVING_CUBE, CubeOfAnnihilationModel::create);
		event.registerLayerDefinition(TFModelLayers.SKELETON_DRUID, SkeletonDruidModel::create);
		event.registerLayerDefinition(TFModelLayers.SLIME_BEETLE, NewSlimeBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.SLIME_BEETLE_TAIL, NewSlimeBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.SNOW_QUEEN, NewSnowQueenModel::create);
		event.registerLayerDefinition(TFModelLayers.CHAIN_BLOCK, SpikeBlockModel::create);
		event.registerLayerDefinition(TFModelLayers.SQUIRREL, NewSquirrelModel::create);
		event.registerLayerDefinition(TFModelLayers.STABLE_ICE_CORE, StableIceCoreModel::create);
		event.registerLayerDefinition(TFModelLayers.SWARM_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.TINY_BIRD, NewTinyBirdModel::create);
		event.registerLayerDefinition(TFModelLayers.TOWERWOOD_BORER, SilverfishModel::createBodyLayer);
		event.registerLayerDefinition(TFModelLayers.TROLL, NewTrollModel::create);
		event.registerLayerDefinition(TFModelLayers.UNSTABLE_ICE_CORE, UnstableIceCoreModel::create);
		event.registerLayerDefinition(TFModelLayers.UPPER_GOBLIN_KNIGHT, NewUpperGoblinKnightModel::create);
		event.registerLayerDefinition(TFModelLayers.UR_GHAST, NewUrGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.WINTER_WOLF, WolfModel::createBodyLayer);
		event.registerLayerDefinition(TFModelLayers.WRAITH, WraithModel::create);
		event.registerLayerDefinition(TFModelLayers.YETI, YetiModel::create);

		event.registerLayerDefinition(TFModelLayers.NEW_BIGHORN_SHEEP, BighornModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_BLOCKCHAIN_GOBLIN, BlockChainGoblinModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_BOAR, BoarModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_DEER, DeerModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_FIRE_BEETLE, FireBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_HELMET_CRAB, HelmetCrabModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_HYDRA, HydraModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_HYDRA_HEAD, HydraHeadModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_HYDRA_NECK, HydraNeckModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_KOBOLD, KoboldModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_LOWER_GOBLIN_KNIGHT, LowerGoblinKnightModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_MINOSHROOM, MinoshroomModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_MINOTAUR, MinotaurModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_NAGA, NagaModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_NAGA_BODY, NagaModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_PINCH_BEETLE, PinchBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_QUEST_RAM, QuestRamModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_RAVEN, RavenModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_REDCAP, RedcapModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_SLIME_BEETLE, SlimeBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_SLIME_BEETLE_TAIL, SlimeBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_SNOW_QUEEN, SnowQueenModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_SQUIRREL, SquirrelModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_TINY_BIRD, TinyBirdModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_TROLL, TrollModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_UPPER_GOBLIN_KNIGHT, UpperGoblinKnightModel::create);
		event.registerLayerDefinition(TFModelLayers.NEW_UR_GHAST, UrGhastModel::create);

		event.registerLayerDefinition(TFModelLayers.CICADA, CicadaModel::create);
		event.registerLayerDefinition(TFModelLayers.FIREFLY, FireflyModel::create);
		event.registerLayerDefinition(TFModelLayers.KEEPSAKE_CASKET, CasketTileEntityRenderer::create);
		event.registerLayerDefinition(TFModelLayers.MOONWORM, MoonwormModel::create);

		event.registerLayerDefinition(TFModelLayers.RED_THREAD, RedThreadModel::create);

		event.registerLayerDefinition(TFModelLayers.KNIGHTMETAL_SHIELD, KnightmetalShieldModel::create);
	}
}
