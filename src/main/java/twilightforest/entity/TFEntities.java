package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.model.SilverfishModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.*;
import twilightforest.client.model.entity.finalcastle.ModelTFCastleGuardian;
import twilightforest.client.renderer.entity.*;
import twilightforest.entity.boss.*;
import twilightforest.entity.finalcastle.EntityTFCastleGuardian;
import twilightforest.entity.passive.*;
import twilightforest.entity.projectile.*;
import twilightforest.item.ItemTFTransformPowder;
import twilightforest.item.TFItems;
import twilightforest.util.TFEntityNames;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFEntities {

	public static final EntitySpawnPlacementRegistry.PlacementType ON_ICE = EntitySpawnPlacementRegistry.PlacementType.create("TF_ON_ICE", (world, pos, entityType) -> {

		BlockState state = world.getBlockState(pos.down());
		Block block = state.getBlock();
		Material material = state.getMaterial();
		BlockPos up = pos.up();

		return (material == Material.ICE || material == Material.PACKED_ICE) && block != Blocks.BEDROCK && block != Blocks.BARRIER && WorldEntitySpawner.isSpawnableSpace(world, pos, world.getBlockState(pos), world.getFluidState(pos)) && WorldEntitySpawner.isSpawnableSpace(world, up, world.getBlockState(up), world.getFluidState(up));
	});

	public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, TwilightForestMod.ID);

	public static final RegistryObject<EntityType<EntityTFBoar>> wild_boar = make(TFEntityNames.WILD_BOAR, EntityTFBoar::new, EntityClassification.CREATURE, 0.9F, 0.9F);
	public static final RegistryObject<EntityType<EntityTFBighorn>> bighorn_sheep = make(TFEntityNames.BIGHORN_SHEEP, EntityTFBighorn::new, EntityClassification.CREATURE, 0.9F, 1.3F);
	public static final RegistryObject<EntityType<EntityTFDeer>> deer = make(TFEntityNames.DEER, EntityTFDeer::new, EntityClassification.CREATURE, 0.7F, 2.3F);

	public static final RegistryObject<EntityType<EntityTFRedcap>> redcap = make(TFEntityNames.REDCAP, EntityTFRedcap::new, EntityClassification.MONSTER, 0.9F, 1.4F);
	public static final RegistryObject<EntityType<EntityTFSwarmSpider>> swarm_spider = make(TFEntityNames.SWARM_SPIDER, EntityTFSwarmSpider::new, EntityClassification.MONSTER, 0.8F, 0.4F);
	public static final RegistryObject<EntityType<EntityTFNaga>> naga = make(TFEntityNames.NAGA, EntityTFNaga::new, EntityClassification.MONSTER, 1.75f, 3.0f);
	public static final RegistryObject<EntityType<EntityTFSkeletonDruid>> skeleton_druid = make(TFEntityNames.SKELETON_DRUID, EntityTFSkeletonDruid::new, EntityClassification.MONSTER, 0.6F, 1.99F);
	public static final RegistryObject<EntityType<EntityTFHostileWolf>> hostile_wolf = make(TFEntityNames.HOSTILE_WOLF, EntityTFHostileWolf::new, EntityClassification.MONSTER, 0.6F, 0.85F);
	public static final RegistryObject<EntityType<EntityTFWraith>> wraith = make(TFEntityNames.WRAITH, EntityTFWraith::new, EntityClassification.MONSTER);
	public static final RegistryObject<EntityType<EntityTFHedgeSpider>> hedge_spider = make(TFEntityNames.HEDGE_SPIDER, EntityTFHedgeSpider::new, EntityClassification.MONSTER, 1.4F, 0.9F);
	public static final RegistryObject<EntityType<EntityTFHydra>> hydra = make(TFEntityNames.HYDRA, EntityTFHydra::new, EntityClassification.MONSTER, 16F, 12F);
	public static final RegistryObject<EntityType<EntityTFLich>> lich = make(TFEntityNames.LICH, EntityTFLich::new, EntityClassification.MONSTER, 1.1F, 2.5F);
	public static final RegistryObject<EntityType<EntityTFPenguin>> penguin = make(TFEntityNames.PENGUIN, EntityTFPenguin::new, EntityClassification.CREATURE, 0.5F, 0.9F);
	public static final RegistryObject<EntityType<EntityTFLichMinion>> lich_minion = make(TFEntityNames.LICH_MINION, EntityTFLichMinion::new, EntityClassification.MONSTER, 0.6F, 1.95F);
	public static final RegistryObject<EntityType<EntityTFLoyalZombie>> loyal_zombie = make(TFEntityNames.LOYAL_ZOMBIE, EntityTFLoyalZombie::new, EntityClassification.MONSTER);
	public static final RegistryObject<EntityType<EntityTFTinyBird>> tiny_bird = make(TFEntityNames.TINY_BIRD, EntityTFTinyBird::new, EntityClassification.CREATURE, 0.3F, 0.3F);
	public static final RegistryObject<EntityType<EntityTFSquirrel>> squirrel = make(TFEntityNames.SQUIRREL, EntityTFSquirrel::new, EntityClassification.CREATURE, 0.3F, 0.5F);
	public static final RegistryObject<EntityType<EntityTFBunny>> bunny = make(TFEntityNames.BUNNY, EntityTFBunny::new, EntityClassification.CREATURE, 0.3F, 0.6F);
	public static final RegistryObject<EntityType<EntityTFRaven>> raven = make(TFEntityNames.RAVEN, EntityTFRaven::new, EntityClassification.CREATURE, 0.3F, 0.5F);
	public static final RegistryObject<EntityType<EntityTFQuestRam>> quest_ram = make(TFEntityNames.QUEST_RAM, EntityTFQuestRam::new, EntityClassification.CREATURE, 1.25F, 2.9F);
	public static final RegistryObject<EntityType<EntityTFKobold>> kobold = make(TFEntityNames.KOBOLD, EntityTFKobold::new, EntityClassification.MONSTER, 0.8F, 1.1F);
	public static final RegistryObject<EntityType<EntityTFMosquitoSwarm>> mosquito_swarm = make(TFEntityNames.MOSQUITO_SWARM, EntityTFMosquitoSwarm::new, EntityClassification.MONSTER, 0.7F, 1.9F);
	public static final RegistryObject<EntityType<EntityTFDeathTome>> death_tome = make(TFEntityNames.DEATH_TOME, EntityTFDeathTome::new, EntityClassification.MONSTER);
	public static final RegistryObject<EntityType<EntityTFMinotaur>> minotaur = make(TFEntityNames.MINOTAUR, EntityTFMinotaur::new, EntityClassification.MONSTER);
	public static final RegistryObject<EntityType<EntityTFMinoshroom>> minoshroom = make(TFEntityNames.MINOSHROOM, EntityTFMinoshroom::new, EntityClassification.MONSTER, 1.49F, 2.9F);
	public static final RegistryObject<EntityType<EntityTFFireBeetle>> fire_beetle = make(TFEntityNames.FIRE_BEETLE, EntityTFFireBeetle::new, EntityClassification.MONSTER, 1.1F, 1.75F);
	public static final RegistryObject<EntityType<EntityTFSlimeBeetle>> slime_beetle = make(TFEntityNames.SLIME_BEETLE, EntityTFSlimeBeetle::new, EntityClassification.MONSTER, 0.9F, 1.75F);
	public static final RegistryObject<EntityType<EntityTFPinchBeetle>> pinch_beetle = make(TFEntityNames.PINCH_BEETLE, EntityTFPinchBeetle::new, EntityClassification.MONSTER, 1.2F, 1.1F);
	public static final RegistryObject<EntityType<EntityTFMazeSlime>> maze_slime = make(TFEntityNames.MAZE_SLIME, EntityTFMazeSlime::new, EntityClassification.MONSTER, 2.04F, 2.04F);
	public static final RegistryObject<EntityType<EntityTFRedcapSapper>> redcap_sapper = make(TFEntityNames.REDCAP_SAPPER, EntityTFRedcapSapper::new, EntityClassification.MONSTER, 0.9F, 1.4F);
	public static final RegistryObject<EntityType<EntityTFMistWolf>> mist_wolf = make(TFEntityNames.MIST_WOLF, EntityTFMistWolf::new, EntityClassification.MONSTER, 1.4F, 1.9F);
	public static final RegistryObject<EntityType<EntityTFKingSpider>> king_spider = make(TFEntityNames.KING_SPIDER, EntityTFKingSpider::new, EntityClassification.MONSTER, 1.6F, 1.6F);
	public static final RegistryObject<EntityType<EntityTFMobileFirefly>> firefly = make(TFEntityNames.FIREFLY, EntityTFMobileFirefly::new, EntityClassification.CREATURE, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<EntityTFMiniGhast>> mini_ghast = make(TFEntityNames.MINI_GHAST, EntityTFMiniGhast::new, EntityClassification.MONSTER, 1.1F, 1.5F);
	public static final RegistryObject<EntityType<EntityTFTowerGhast>> tower_ghast = make(TFEntityNames.TOWER_GHAST, EntityTFTowerGhast::new, EntityClassification.MONSTER, 4.0F, 6.0F);
	public static final RegistryObject<EntityType<EntityTFTowerGolem>> tower_golem = make(TFEntityNames.TOWER_GOLEM, EntityTFTowerGolem::new, EntityClassification.MONSTER, 1.4F, 2.9F);
	public static final RegistryObject<EntityType<EntityTFTowerTermite>> tower_termite = make(TFEntityNames.TOWER_TERMITE, EntityTFTowerTermite::new, EntityClassification.MONSTER, 0.3F, 0.7F);
	public static final RegistryObject<EntityType<EntityTFTowerBroodling>> tower_broodling = make(TFEntityNames.TOWER_BROODLING, EntityTFTowerBroodling::new, EntityClassification.MONSTER, 0.3F, 0.7F);
	public static final RegistryObject<EntityType<EntityTFUrGhast>> ur_ghast = make(TFEntityNames.UR_GHAST, EntityTFUrGhast::new, EntityClassification.MONSTER, 14.0F, 18.0F);
	public static final RegistryObject<EntityType<EntityTFBlockGoblin>> blockchain_goblin = make(TFEntityNames.BLOCKCHAIN_GOBLIN, EntityTFBlockGoblin::new, EntityClassification.MONSTER, 0.9F, 1.4F);
	public static final RegistryObject<EntityType<EntityTFGoblinKnightUpper>> goblin_knight_upper = make(TFEntityNames.GOBLIN_KNIGHT_UPPER, EntityTFGoblinKnightUpper::new, EntityClassification.MONSTER, 1.1F, 1.3F);
	public static final RegistryObject<EntityType<EntityTFGoblinKnightLower>> goblin_knight_lower = make(TFEntityNames.GOBLIN_KNIGHT_LOWER, EntityTFGoblinKnightLower::new, EntityClassification.MONSTER, 0.7F, 1.1F);
	public static final RegistryObject<EntityType<EntityTFHelmetCrab>> helmet_crab = make(TFEntityNames.HELMET_CRAB, EntityTFHelmetCrab::new, EntityClassification.MONSTER, 0.8F, 1.1F);
	public static final RegistryObject<EntityType<EntityTFKnightPhantom>> knight_phantom = make(TFEntityNames.KNIGHT_PHANTOM, EntityTFKnightPhantom::new, EntityClassification.MONSTER, 1.5F, 3.0F);
	public static final RegistryObject<EntityType<EntityTFYeti>> yeti = make(TFEntityNames.YETI, EntityTFYeti::new, EntityClassification.MONSTER, 1.4F, 2.4F);
	public static final RegistryObject<EntityType<EntityTFYetiAlpha>> yeti_alpha = make(TFEntityNames.YETI_ALPHA, EntityTFYetiAlpha::new, EntityClassification.MONSTER, 3.8F, 5.0F);
	public static final RegistryObject<EntityType<EntityTFWinterWolf>> winter_wolf = make(TFEntityNames.WINTER_WOLF, EntityTFWinterWolf::new, EntityClassification.MONSTER, 1.4F, 1.9F);
	public static final RegistryObject<EntityType<EntityTFSnowGuardian>> snow_guardian = make(TFEntityNames.SNOW_GUARDIAN, EntityTFSnowGuardian::new, EntityClassification.MONSTER, 0.6F, 1.8F);
	public static final RegistryObject<EntityType<EntityTFIceShooter>> stable_ice_core = make(TFEntityNames.STABLE_ICE_CORE, EntityTFIceShooter::new, EntityClassification.MONSTER, 0.8F, 1.8F);
	public static final RegistryObject<EntityType<EntityTFIceExploder>> unstable_ice_core = make(TFEntityNames.UNSTABLE_ICE_CORE, EntityTFIceExploder::new, EntityClassification.MONSTER, 0.8F, 1.8F);
	public static final RegistryObject<EntityType<EntityTFSnowQueen>> snow_queen = make(TFEntityNames.SNOW_QUEEN, EntityTFSnowQueen::new, EntityClassification.MONSTER, 0.7F, 2.2F);
	public static final RegistryObject<EntityType<EntityTFTroll>> troll = make(TFEntityNames.TROLL, EntityTFTroll::new, EntityClassification.MONSTER, 1.4F, 2.4F);
	public static final RegistryObject<EntityType<EntityTFGiantMiner>> giant_miner = make(TFEntityNames.GIANT_MINER, EntityTFGiantMiner::new, EntityClassification.MONSTER, 2.4F, 7.2F);
	public static final RegistryObject<EntityType<EntityTFArmoredGiant>> armored_giant = make(TFEntityNames.ARMORED_GIANT, EntityTFArmoredGiant::new, EntityClassification.MONSTER, 2.4F, 7.2F);
	public static final RegistryObject<EntityType<EntityTFIceCrystal>> ice_crystal = make(TFEntityNames.ICE_CRYSTAL, EntityTFIceCrystal::new, EntityClassification.MONSTER, 0.6F, 1.8F);
	public static final RegistryObject<EntityType<EntityTFHarbingerCube>> harbinger_cube = make(TFEntityNames.HARBINGER_CUBE, EntityTFHarbingerCube::new, EntityClassification.MONSTER, 1.9F, 2.4F);
	public static final RegistryObject<EntityType<EntityTFAdherent>> adherent = make(TFEntityNames.ADHERENT, EntityTFAdherent::new, EntityClassification.MONSTER, 0.8F, 2.2F);
	public static final RegistryObject<EntityType<EntityTFRovingCube>> roving_cube = make(TFEntityNames.ROVING_CUBE, EntityTFRovingCube::new, EntityClassification.MONSTER, 1.2F, 2.1F);
	public static final RegistryObject<EntityType<EntityTFCastleGuardian>> castle_guardian = make(TFEntityNames.CASTLE_GUARDIAN, EntityTFCastleGuardian::new, EntityClassification.MONSTER, 1.8F, 2.4F);

	public static final RegistryObject<EntityType<EntityTFHydraHead>> hydra_head = make(TFEntityNames.HYDRA_HEAD, makeCastedBuilder(EntityTFHydraHead.class, EntityTFHydraHead::new, EntityClassification.MONSTER).size(3F, 3F).setTrackingRange(150).setShouldReceiveVelocityUpdates(false));

	public static final RegistryObject<EntityType<EntityTFNatureBolt>> nature_bolt = make(TFEntityNames.NATURE_BOLT, makeCastedBuilder(EntityTFNatureBolt.class, EntityTFNatureBolt::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150).setUpdateInterval(5));
	public static final RegistryObject<EntityType<EntityTFLichBolt>> lich_bolt = make(TFEntityNames.LICH_BOLT, makeCastedBuilder(EntityTFLichBolt.class, EntityTFLichBolt::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150).setUpdateInterval(2));
	public static final RegistryObject<EntityType<EntityTFTwilightWandBolt>> wand_bolt = make(TFEntityNames.WAND_BOLT, makeCastedBuilder(EntityTFTwilightWandBolt.class, EntityTFTwilightWandBolt::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150).setUpdateInterval(5));
	public static final RegistryObject<EntityType<EntityTFTomeBolt>> tome_bolt = make(TFEntityNames.TOME_BOLT, makeCastedBuilder(EntityTFTomeBolt.class, EntityTFTomeBolt::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150).setUpdateInterval(5));
	public static final RegistryObject<EntityType<EntityTFHydraMortar>> hydra_mortar = make(TFEntityNames.HYDRA_MORTAR, makeCastedBuilder(EntityTFHydraMortar.class, EntityTFHydraMortar::new, EntityClassification.MISC).size(0.75F, 0.75F).setTrackingRange(150));
	public static final RegistryObject<EntityType<EntityTFLichBomb>> lich_bomb = make(TFEntityNames.LICH_BOMB, makeCastedBuilder(EntityTFLichBomb.class, EntityTFLichBomb::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150));
	public static final RegistryObject<EntityType<EntityTFMoonwormShot>> moonworm_shot = make(TFEntityNames.MOONWORM_SHOT, makeCastedBuilder(EntityTFMoonwormShot.class, EntityTFMoonwormShot::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150));
	public static final RegistryObject<EntityType<EntityTFSlimeProjectile>> slime_blob = make(TFEntityNames.SLIME_BLOB, makeCastedBuilder(EntityTFSlimeProjectile.class, EntityTFSlimeProjectile::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150));
	public static final RegistryObject<EntityType<EntityTFCharmEffect>> charm_effect = make(TFEntityNames.CHARM_EFFECT, EntityTFCharmEffect::new, EntityClassification.MISC, 0.25F, 0.25F);
	public static final RegistryObject<EntityType<EntityTFThrownWep>> thrown_wep = make(TFEntityNames.THROWN_WEP, EntityTFThrownWep::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<EntityTFFallingIce>> falling_ice = make(TFEntityNames.FALLING_ICE, EntityTFFallingIce::new, EntityClassification.MISC, 2.98F, 2.98F);
	public static final RegistryObject<EntityType<EntityTFIceBomb>> thrown_ice = make(TFEntityNames.THROWN_ICE, makeCastedBuilder(EntityTFIceBomb.class, EntityTFIceBomb::new, EntityClassification.MISC).size(1.0F, 1.0F).setUpdateInterval(2));
	public static final RegistryObject<EntityType<EntitySeekerArrow>> seeker_arrow = make(TFEntityNames.SEEKER_ARROW, makeCastedBuilder(EntitySeekerArrow.class, EntitySeekerArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).setTrackingRange(150).setUpdateInterval(1));
	public static final RegistryObject<EntityType<EntityIceArrow>> ice_arrow = make(TFEntityNames.ICE_ARROW, makeCastedBuilder(EntityIceArrow.class, EntityIceArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).setTrackingRange(150).setUpdateInterval(1));
	public static final RegistryObject<EntityType<EntityTFIceSnowball>> ice_snowball = make(TFEntityNames.ICE_SNOWBALL, makeCastedBuilder(EntityTFIceSnowball.class, EntityTFIceSnowball::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150));
	public static final RegistryObject<EntityType<EntityTFChainBlock>> chain_block = make(TFEntityNames.CHAIN_BLOCK, makeCastedBuilder(EntityTFChainBlock.class, EntityTFChainBlock::new, EntityClassification.MISC).size(0.6F, 0.6F).setUpdateInterval(1));
	public static final RegistryObject<EntityType<EntityTFCubeOfAnnihilation>> cube_of_annihilation = make(TFEntityNames.CUBE_OF_ANNIHILATION, makeCastedBuilder(EntityTFCubeOfAnnihilation.class, EntityTFCubeOfAnnihilation::new, EntityClassification.MISC).size(1F, 1F).setUpdateInterval(1));
	public static final RegistryObject<EntityType<EntityTFSlideBlock>> slider = make(TFEntityNames.SLIDER, makeCastedBuilder(EntityTFSlideBlock.class, EntityTFSlideBlock::new, EntityClassification.MISC).size(0.98F, 0.98F).setUpdateInterval(1));
	public static final RegistryObject<EntityType<EntityTFBoggard>> boggard = make(TFEntityNames.BOGGARD, EntityTFBoggard::new, EntityClassification.MONSTER, 0.8F, 1.1F);
	public static final RegistryObject<EntityType<EntityTFRisingZombie>> rising_zombie = make(TFEntityNames.RISING_ZOMBIE, EntityTFRisingZombie::new, EntityClassification.MONSTER, 0.6F, 1.95F);
	public static final RegistryObject<EntityType<EntityTFProtectionBox>> protection_box = make(TFEntityNames.PROTECTION_BOX, makeCastedBuilder(EntityTFProtectionBox.class, EntityTFProtectionBox::new, EntityClassification.MISC).disableSerialization().disableSummoning().size(0, 0));

	private static <E extends Entity> RegistryObject<EntityType<E>> make(ResourceLocation id, EntityType.IFactory<E> factory, EntityClassification classification, float width, float height) {
		return make(id, makeBuilder(factory, classification).size(width, height));
	}

	private static <E extends Entity> RegistryObject<EntityType<E>> make(ResourceLocation id, EntityType.IFactory<E> factory, EntityClassification classification) {
		return ENTITIES.register(id.getPath(), () -> makeBuilderAndBuild(id, factory, classification));
	}

	private static <E extends Entity> RegistryObject<EntityType<E>> make(ResourceLocation id, EntityType.Builder<E> builder) {
		return ENTITIES.register(id.getPath(), () -> builder.build(id.toString()));
	}

	private static <E extends Entity> EntityType<E> makeBuilderAndBuild(ResourceLocation id, EntityType.IFactory<E> factory, EntityClassification classification) {
		return makeBuilderAndBuild(id.toString(), factory, classification);
	}

	private static <E extends Entity> EntityType<E> makeBuilderAndBuild(String id, EntityType.IFactory<E> factory, EntityClassification classification) {
		return makeBuilder(factory, classification).build(id);
	}

	private static <E extends Entity> EntityType.Builder<E> makeCastedBuilder(@SuppressWarnings("unused") Class<E> cast, EntityType.IFactory<E> factory, EntityClassification classification) {
		return makeBuilder(factory, classification);
	}

	private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.IFactory<E> factory, EntityClassification classification) {
		return EntityType.Builder.create(factory, classification).
				size(0.6F, 1.8F).
				setTrackingRange(80).
				setUpdateInterval(3).
				setShouldReceiveVelocityUpdates(true);
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
		((ItemTFTransformPowder) TFItems.transformation_powder.get()).initTransformations();
		// TODO: this is only here still for the spawnegg colors
		/*		helper.registerEntity(TFEntityNames.WILD_BOAR, EntityTFBoar.class, EntityTFBoar::new, 0x83653b, 0xffefca);
				helper.registerEntity(TFEntityNames.BIGHORN_SHEEP, EntityTFBighorn.class, EntityTFBighorn::new, 0xdbceaf, 0xd7c771);
				helper.registerEntity(TFEntityNames.DEER, EntityTFDeer.class, EntityTFDeer::new, 0x7b4d2e, 0x4b241d);

				helper.registerEntity(TFEntityNames.REDCAP, EntityTFRedcap.class, EntityTFRedcap::new, 0x3b3a6c, 0xab1e14);
				helper.registerEntity(TFEntityNames.SWARM_SPIDER, EntityTFSwarmSpider.class, EntityTFSwarmSpider::new, 0x32022e, 0x17251e);
				helper.registerEntity(TFEntityNames.NAGA, EntityTFNaga.class, EntityTFNaga::new, 0xa4d316, 0x1b380b, 150, 1, true);
				helper.registerEntity(TFEntityNames.SKELETON_DRUID, EntityTFSkeletonDruid.class, EntityTFSkeletonDruid::new, 0xa3a3a3, 0x2a3b17);
				helper.registerEntity(TFEntityNames.HOSTILE_WOLF, EntityTFHostileWolf.class, EntityTFHostileWolf::new, 0xd7d3d3, 0xab1e14);
				helper.registerEntity(TFEntityNames.WRAITH, EntityTFWraith.class, EntityTFWraith::new, 0x505050, 0x838383);
				helper.registerEntity(TFEntityNames.HEDGE_SPIDER, EntityTFHedgeSpider.class, EntityTFHedgeSpider::new, 0x235f13, 0x562653);
				helper.registerEntity(TFEntityNames.HYDRA, EntityTFHydra.class, EntityTFHydra::new, 0x142940, 0x29806b);
				helper.registerEntity(TFEntityNames.LICH, EntityTFLich.class, EntityTFLich::new, 0xaca489, 0x360472);
				helper.registerEntity(TFEntityNames.PENGUIN, EntityTFPenguin.class, EntityTFPenguin::new, 0x12151b, 0xf9edd2);
				helper.registerEntity(TFEntityNames.LICH_MINION, EntityTFLichMinion.class, EntityTFLichMinion::new);
				helper.registerEntity(TFEntityNames.LOYAL_ZOMBIE, EntityTFLoyalZombie.class, EntityTFLoyalZombie::new);
				helper.registerEntity(TFEntityNames.TINY_BIRD, EntityTFTinyBird.class, EntityTFTinyBird::new, 0x33aadd, 0x1188ee);
				helper.registerEntity(TFEntityNames.SQUIRREL, EntityTFSquirrel.class, EntityTFSquirrel::new, 0x904f12, 0xeeeeee);
				helper.registerEntity(TFEntityNames.BUNNY, EntityTFBunny.class, EntityTFBunny::new, 0xfefeee, 0xccaa99);
				helper.registerEntity(TFEntityNames.RAVEN, EntityTFRaven.class, EntityTFRaven::new, 0x000011, 0x222233);
				helper.registerEntity(TFEntityNames.QUEST_RAM, EntityTFQuestRam.class, EntityTFQuestRam::new, 0xfefeee, 0x33aadd);
				helper.registerEntity(TFEntityNames.KOBOLD, EntityTFKobold.class, EntityTFKobold::new, 0x372096, 0x895d1b);
				helper.registerEntity(TFEntityNames.MOSQUITO_SWARM, EntityTFMosquitoSwarm.class, EntityTFMosquitoSwarm::new, 0x080904, 0x2d2f21);
				helper.registerEntity(TFEntityNames.DEATH_TOME, EntityTFDeathTome.class, EntityTFDeathTome::new, 0x774e22, 0xdbcdbe);
				helper.registerEntity(TFEntityNames.MINOTAUR, EntityTFMinotaur.class, EntityTFMinotaur::new, 0x3f3024, 0xaa7d66);
				helper.registerEntity(TFEntityNames.MINOSHROOM, EntityTFMinoshroom.class, EntityTFMinoshroom::new, 0xa81012, 0xaa7d66);
				helper.registerEntity(TFEntityNames.FIRE_BEETLE, EntityTFFireBeetle.class, EntityTFFireBeetle::new, 0x1d0b00, 0xcb6f25);
				helper.registerEntity(TFEntityNames.SLIME_BEETLE, EntityTFSlimeBeetle.class, EntityTFSlimeBeetle::new, 0x0c1606, 0x60a74c);
				helper.registerEntity(TFEntityNames.PINCH_BEETLE, EntityTFPinchBeetle.class, EntityTFPinchBeetle::new, 0xbc9327, 0x241609);
				helper.registerEntity(TFEntityNames.MAZE_SLIME, EntityTFMazeSlime.class, EntityTFMazeSlime::new, 0xa3a3a3, 0x2a3b17);
				helper.registerEntity(TFEntityNames.REDCAP_SAPPER, EntityTFRedcapSapper.class, EntityTFRedcapSapper::new, 0x575d21, 0xab1e14);
				helper.registerEntity(TFEntityNames.MIST_WOLF, EntityTFMistWolf.class, EntityTFMistWolf::new, 0x3a1411, 0xe2c88a);
				helper.registerEntity(TFEntityNames.KING_SPIDER, EntityTFKingSpider.class, EntityTFKingSpider::new, 0x2c1a0e, 0xffc017);
				helper.registerEntity(TFEntityNames.FIREFLY, EntityTFMobileFirefly.class, EntityTFMobileFirefly::new, 0xa4d316, 0xbaee02);
				helper.registerEntity(TFEntityNames.MINI_GHAST, EntityTFMiniGhast.class, EntityTFMiniGhast::new, 0xbcbcbc, 0xa74343);
				helper.registerEntity(TFEntityNames.TOWER_GHAST, EntityTFTowerGhast.class, EntityTFTowerGhast::new, 0xbcbcbc, 0xb77878);
				helper.registerEntity(TFEntityNames.TOWER_GOLEM, EntityTFTowerGolem.class, EntityTFTowerGolem::new, 0x6b3d20, 0xe2ddda);
				helper.registerEntity(TFEntityNames.TOWER_TERMITE, EntityTFTowerTermite.class, EntityTFTowerTermite::new, 0x5d2b21, 0xaca03a);
				helper.registerEntity(TFEntityNames.TOWER_BROODLING, EntityTFTowerBroodling.class, EntityTFTowerBroodling::new, 0x343c14, 0xbaee02);
				helper.registerEntity(TFEntityNames.UR_GHAST, EntityTFUrGhast.class, EntityTFUrGhast::new, 0xbcbcbc, 0xb77878);
				helper.registerEntity(TFEntityNames.BLOCKCHAIN_GOBLIN, EntityTFBlockGoblin.class, EntityTFBlockGoblin::new, 0xd3e7bc, 0x1f3fff);
				helper.registerEntity(TFEntityNames.GOBLIN_KNIGHT_UPPER, EntityTFGoblinKnightUpper.class, EntityTFGoblinKnightUpper::new);
				helper.registerEntity(TFEntityNames.GOBLIN_KNIGHT_LOWER, EntityTFGoblinKnightLower.class, EntityTFGoblinKnightLower::new, 0x566055, 0xd3e7bc);
				helper.registerEntity(TFEntityNames.HELMET_CRAB, EntityTFHelmetCrab.class, EntityTFHelmetCrab::new, 0xfb904b, 0xd3e7bc);
				helper.registerEntity(TFEntityNames.KNIGHT_PHANTOM, EntityTFKnightPhantom.class, EntityTFKnightPhantom::new, 0xa6673b, 0xd3e7bc);
				helper.registerEntity(TFEntityNames.YETI, EntityTFYeti.class, EntityTFYeti::new, 0xdedede, 0x4675bb);
				helper.registerEntity(TFEntityNames.YETI_ALPHA, EntityTFYetiAlpha.class, EntityTFYetiAlpha::new, 0xcdcdcd, 0x29486e);
				helper.registerEntity(TFEntityNames.WINTER_WOLF, EntityTFWinterWolf.class, EntityTFWinterWolf::new, 0xdfe3e5, 0xb2bcca);
				helper.registerEntity(TFEntityNames.SNOW_GUARDIAN, EntityTFSnowGuardian.class, EntityTFSnowGuardian::new, 0xd3e7bc, 0xfefefe);
				helper.registerEntity(TFEntityNames.STABLE_ICE_CORE, EntityTFIceShooter.class, EntityTFIceShooter::new, 0xa1bff3, 0x7000f8);
				helper.registerEntity(TFEntityNames.UNSTABLE_ICE_CORE, EntityTFIceExploder.class, EntityTFIceExploder::new, 0x9aacf5, 0x9b0fa5);
				helper.registerEntity(TFEntityNames.SNOW_QUEEN, EntityTFSnowQueen.class, EntityTFSnowQueen::new, 0xb1b2d4, 0x87006e);
				helper.registerEntity(TFEntityNames.TROLL, EntityTFTroll.class, EntityTFTroll::new, 0x9ea98f, 0xb0948e);
				helper.registerEntity(TFEntityNames.GIANT_MINER, EntityTFGiantMiner.class, EntityTFGiantMiner::new, 0x211b52, 0x9a9a9a);
				helper.registerEntity(TFEntityNames.ARMORED_GIANT, EntityTFArmoredGiant.class, EntityTFArmoredGiant::new, 0x239391, 0x9a9a9a);
				helper.registerEntity(TFEntityNames.ICE_CRYSTAL, EntityTFIceCrystal.class, EntityTFIceCrystal::new, 0xdce9fe, 0xadcafb);
				helper.registerEntity(TFEntityNames.HARBINGER_CUBE, EntityTFHarbingerCube.class, EntityTFHarbingerCube::new, 0x00000a, 0x8b0000);
				helper.registerEntity(TFEntityNames.ADHERENT, EntityTFAdherent.class, EntityTFAdherent::new, 0x0a0000, 0x00008b);
				helper.registerEntity(TFEntityNames.ROVING_CUBE, EntityTFRovingCube.class, EntityTFRovingCube::new, 0x0a0000, 0x00009b);
				helper.registerEntity(TFEntityNames.CASTLE_GUARDIAN, EntityTFCastleGuardian.class, EntityTFCastleGuardian::new, 80, 3, true);

				helper.registerEntity(TFEntityNames.HYDRA_HEAD, EntityTFHydraHead.class, EntityTFHydraHead::new, 150, 3, false);

				helper.registerEntity(TFEntityNames.BOGGARD, EntityTFBoggard.class, EntityTFBoggard::new);
				helper.registerEntity(TFEntityNames.RISING_ZOMBIE, EntityTFRisingZombie.class, EntityTFRisingZombie::new);*/

		// this place is now spawn placement registry
		EntitySpawnPlacementRegistry.register(wild_boar.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::func_223316_b);
		EntitySpawnPlacementRegistry.register(bighorn_sheep.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::func_223316_b);
		EntitySpawnPlacementRegistry.register(deer.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::func_223316_b);

		EntitySpawnPlacementRegistry.register(redcap.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(skeleton_druid.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFSkeletonDruid::skeletonDruidSpawnHandler);
		EntitySpawnPlacementRegistry.register(wraith.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFWraith::getCanSpawnHere);
		EntitySpawnPlacementRegistry.register(hydra.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(lich.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(penguin.get(), ON_ICE, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::func_223316_b);
		EntitySpawnPlacementRegistry.register(lich_minion.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(loyal_zombie.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(tiny_bird.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::func_223316_b);
		EntitySpawnPlacementRegistry.register(squirrel.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::func_223316_b);
		EntitySpawnPlacementRegistry.register(bunny.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::func_223316_b);
		EntitySpawnPlacementRegistry.register(raven.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::func_223316_b);
		EntitySpawnPlacementRegistry.register(quest_ram.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::func_223316_b);
		EntitySpawnPlacementRegistry.register(kobold.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(mosquito_swarm.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFMosquitoSwarm::canSpawn);
		EntitySpawnPlacementRegistry.register(death_tome.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(minotaur.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(minoshroom.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(fire_beetle.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(slime_beetle.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(pinch_beetle.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(mist_wolf.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFHostileWolf::getCanSpawnHere);
		EntitySpawnPlacementRegistry.register(firefly.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFMobileFirefly::getCanSpawnHere);
		EntitySpawnPlacementRegistry.register(mini_ghast.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFMiniGhast::canSpawnHere);
		EntitySpawnPlacementRegistry.register(tower_golem.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(tower_termite.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(tower_ghast.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFTowerGhast::ghastSpawnHandler);
		EntitySpawnPlacementRegistry.register(ur_ghast.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(blockchain_goblin.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(goblin_knight_upper.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(goblin_knight_lower.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(helmet_crab.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(knight_phantom.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(naga.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(swarm_spider.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFSwarmSpider::getCanSpawnHere);
		EntitySpawnPlacementRegistry.register(king_spider.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(tower_broodling.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFSwarmSpider::getCanSpawnHere);
		EntitySpawnPlacementRegistry.register(hedge_spider.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFHedgeSpider::canSpawn);
		EntitySpawnPlacementRegistry.register(redcap_sapper.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(maze_slime.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFMazeSlime::getCanSpawnHere);
		EntitySpawnPlacementRegistry.register(yeti.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFYeti::yetiSnowyForestSpawnHandler);
		EntitySpawnPlacementRegistry.register(yeti_alpha.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(winter_wolf.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFWinterWolf::canSpawnHere);
		EntitySpawnPlacementRegistry.register(snow_guardian.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(stable_ice_core.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(unstable_ice_core.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(snow_queen.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(troll.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(giant_miner.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(armored_giant.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(ice_crystal.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(harbinger_cube.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(adherent.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(roving_cube.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::func_223325_c);
		EntitySpawnPlacementRegistry.register(rising_zombie.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);

		EntitySpawnPlacementRegistry.register(castle_guardian.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerEntityRenderer() {
		RenderingRegistry.registerEntityRenderingHandler(wild_boar.get(), m -> new RenderTFBoar(m, new ModelTFBoar()));
		RenderingRegistry.registerEntityRenderingHandler(bighorn_sheep.get(), m -> new RenderTFBighorn(m, new ModelTFBighorn(), new ModelTFBighornFur(), 0.7F));
		RenderingRegistry.registerEntityRenderingHandler(deer.get(), m -> new RenderTFDeer<>(m, new ModelTFDeer<>(), 0.7F));

		RenderingRegistry.registerEntityRenderingHandler(redcap.get(), m -> new RenderTFBiped<>(m, new ModelTFRedcap<>(), 0.4F, "redcap.png"));
		RenderingRegistry.registerEntityRenderingHandler(skeleton_druid.get(), m -> new RenderTFBiped<>(m, new ModelTFSkeletonDruid<>(), 0.5F, "skeletondruid.png"));
		RenderingRegistry.registerEntityRenderingHandler(hostile_wolf.get(), WolfRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(wraith.get(), m -> new RenderTFWraith<>(m, new ModelTFWraith<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(hydra.get(), m -> new RenderTFHydra<>(m, new ModelTFHydra<>(), 4.0F));
		RenderingRegistry.registerEntityRenderingHandler(lich.get(), m -> new RenderTFLich<>(m, new ModelTFLich<>(), 0.6F));
		RenderingRegistry.registerEntityRenderingHandler(penguin.get(), m -> new RenderTFBird<>(m, new ModelTFPenguin<>(), 0.375F, "penguin.png"));
		RenderingRegistry.registerEntityRenderingHandler(lich_minion.get(), m -> new RenderTFBiped<>(m, new ModelTFLichMinion<>(), 0.5F, "textures/entity/zombie/zombie.png"));
		RenderingRegistry.registerEntityRenderingHandler(loyal_zombie.get(), m -> new RenderTFBiped<>(m, new ModelTFLoyalZombie(), 0.5F, "textures/entity/zombie/zombie.png"));
		RenderingRegistry.registerEntityRenderingHandler(tiny_bird.get(), m -> new RenderTFTinyBird<>(m, new ModelTFTinyBird<>(), 1.0F));
		RenderingRegistry.registerEntityRenderingHandler(squirrel.get(), m -> new RenderTFGenericMob<>(m, new ModelTFSquirrel<>(), 1.0F, "squirrel2.png"));
		RenderingRegistry.registerEntityRenderingHandler(bunny.get(), m -> new RenderTFBunny<>(m, new ModelTFBunny<>(), 1.0F));
		RenderingRegistry.registerEntityRenderingHandler(raven.get(), m -> new RenderTFBird<>(m, new ModelTFRaven<>(), 1.0F, "raven.png"));
		RenderingRegistry.registerEntityRenderingHandler(quest_ram.get(), manager -> new RenderTFQuestRam<>(manager, new ModelTFQuestRam<>()));
		RenderingRegistry.registerEntityRenderingHandler(kobold.get(), m -> new RenderTFKobold<>(m, new ModelTFKobold<>(), 0.4F, "kobold.png"));
		RenderingRegistry.registerEntityRenderingHandler(boggard.get(), m -> new RenderTFBiped<>(m, new ModelTFLoyalZombie(), 0.625F, "kobold.png"));
		RenderingRegistry.registerEntityRenderingHandler(mosquito_swarm.get(), m -> new RenderTFGenericMob<>(m, new ModelTFMosquitoSwarm<>(), 0.0F, "mosquitoswarm.png"));
		RenderingRegistry.registerEntityRenderingHandler(death_tome.get(), m -> new RenderTFGenericMob<>(m, new ModelTFDeathTome(), 0.3F, "textures/entity/enchanting_table_book.png"));
		RenderingRegistry.registerEntityRenderingHandler(minotaur.get(), m -> new RenderTFBiped<>(m, new ModelTFMinotaur<>(), 0.625F, "minotaur.png"));
		RenderingRegistry.registerEntityRenderingHandler(minoshroom.get(), m -> new RenderTFMinoshroom<>(m, new ModelTFMinoshroom<>(), 0.625F));
		RenderingRegistry.registerEntityRenderingHandler(fire_beetle.get(), m -> new RenderTFGenericMob<>(m, new ModelTFFireBeetle<>(), 0.8F, "firebeetle.png"));
		RenderingRegistry.registerEntityRenderingHandler(slime_beetle.get(), m -> new RenderTFSlimeBeetle<>(m, new ModelTFSlimeBeetle<>(), 0.6F));
		RenderingRegistry.registerEntityRenderingHandler(pinch_beetle.get(), m -> new RenderTFGenericMob<>(m, new ModelTFPinchBeetle<>(), 0.6F, "pinchbeetle.png"));
		RenderingRegistry.registerEntityRenderingHandler(mist_wolf.get(), RenderTFMistWolf::new);
		RenderingRegistry.registerEntityRenderingHandler(firefly.get(), RenderTFMobileFirefly::new);
		RenderingRegistry.registerEntityRenderingHandler(mini_ghast.get(), m -> new RenderTFGhast<>(m, new ModelTFGhast<>(), 0.625F));
		RenderingRegistry.registerEntityRenderingHandler(tower_golem.get(), m -> new RenderTFTowerGolem<>(m, new ModelTFTowerGolem<>(), 0.75F));
		RenderingRegistry.registerEntityRenderingHandler(tower_termite.get(), m -> new RenderTFGenericMob<>(m, new SilverfishModel<>(), 0.3F, "towertermite.png"));
		RenderingRegistry.registerEntityRenderingHandler(tower_ghast.get(), m -> new RenderTFTowerGhast<>(m, new ModelTFGhast<>(), 3.0F));
		RenderingRegistry.registerEntityRenderingHandler(ur_ghast.get(), m -> new RenderTFUrGhast<>(m, new ModelTFTowerBoss<>(), 8.0F, 24F));
		RenderingRegistry.registerEntityRenderingHandler(blockchain_goblin.get(), m -> new RenderTFBlockGoblin<>(m, new ModelTFBlockGoblin<>(), 0.4F));
		//RenderingRegistry.registerEntityRenderingHandler(EntityTFGoblinChain.class, m -> new RenderTFSpikeBlock<>(m, new ModelTFGoblinChain()));
		//RenderingRegistry.registerEntityRenderingHandler(EntityTFSpikeBlock.class, m -> new RenderTFSpikeBlock<>(m, new ModelTFSpikeBlock()));
		RenderingRegistry.registerEntityRenderingHandler(goblin_knight_upper.get(), m -> new RenderTFGoblinKnightUpper<>(m, new ModelTFGoblinKnightUpper<>(), 0.625F));
		RenderingRegistry.registerEntityRenderingHandler(goblin_knight_lower.get(), m -> new RenderTFBiped<>(m, new ModelTFGoblinKnightLower<>(), 0.625F, "doublegoblin.png"));
		RenderingRegistry.registerEntityRenderingHandler(helmet_crab.get(), m -> new RenderTFGenericMob<>(m, new ModelTFHelmetCrab<>(), 0.625F, "helmetcrab.png"));
		RenderingRegistry.registerEntityRenderingHandler(knight_phantom.get(), m -> new RenderTFKnightPhantom<>(m, new ModelTFKnightPhantom2<>(), 0.625F));
		RenderingRegistry.registerEntityRenderingHandler(naga.get(), m -> new RenderTFNaga<>(m, new ModelTFNaga<>(), 1.45F));
		//RenderingRegistry.registerEntityRenderingHandler(EntityTFNagaSegment.class, m -> new RenderTFNagaSegment(m, new ModelTFNaga()));
		RenderingRegistry.registerEntityRenderingHandler(swarm_spider.get(), RenderTFSwarmSpider::new);
		RenderingRegistry.registerEntityRenderingHandler(king_spider.get(), RenderTFKingSpider::new);
		RenderingRegistry.registerEntityRenderingHandler(tower_broodling.get(), RenderTFTowerBroodling::new);
		RenderingRegistry.registerEntityRenderingHandler(hedge_spider.get(), RenderTFHedgeSpider::new);
		RenderingRegistry.registerEntityRenderingHandler(redcap_sapper.get(), m -> new RenderTFBiped<>(m, new ModelTFRedcap<>(), 0.4F, "redcapsapper.png"));
		RenderingRegistry.registerEntityRenderingHandler(maze_slime.get(), m -> new RenderTFMazeSlime(m, 0.625F));
		RenderingRegistry.registerEntityRenderingHandler(yeti.get(), m -> new RenderTFBiped<>(m, new ModelTFYeti<>(), 0.625F, "yeti2.png"));
		RenderingRegistry.registerEntityRenderingHandler(protection_box.get(), RenderTFProtectionBox::new);
		RenderingRegistry.registerEntityRenderingHandler(yeti_alpha.get(), m -> new RenderTFBiped<>(m, new ModelTFYetiAlpha<>(), 1.75F, "yetialpha.png"));
		RenderingRegistry.registerEntityRenderingHandler(winter_wolf.get(), RenderTFWinterWolf::new);
		RenderingRegistry.registerEntityRenderingHandler(snow_guardian.get(), m -> new RenderTFSnowGuardian<>(m, new ModelTFSnowGuardian<>()));
		RenderingRegistry.registerEntityRenderingHandler(stable_ice_core.get(), m -> new RenderTFIceShooter<>(m, new ModelTFIceShooter<>()));
		RenderingRegistry.registerEntityRenderingHandler(unstable_ice_core.get(), m -> new RenderTFIceExploder<>(m, new ModelTFIceExploder<>()));
		RenderingRegistry.registerEntityRenderingHandler(snow_queen.get(), m -> new RenderTFSnowQueen<>(m, new ModelTFSnowQueen<>()));
		//RenderingRegistry.registerEntityRenderingHandler(EntityTFSnowQueenIceShield.class, RenderTFSnowQueenIceShield::new);
		RenderingRegistry.registerEntityRenderingHandler(troll.get(), m -> new RenderTFBiped<>(m, new ModelTFTroll<>(), 0.625F, "troll.png"));
		RenderingRegistry.registerEntityRenderingHandler(giant_miner.get(), RenderTFGiant::new);
		RenderingRegistry.registerEntityRenderingHandler(ice_crystal.get(), RenderTFIceCrystal::new);
		RenderingRegistry.registerEntityRenderingHandler(chain_block.get(), m -> new RenderTFChainBlock<>(m, new ModelTFSpikeBlock()));
		RenderingRegistry.registerEntityRenderingHandler(cube_of_annihilation.get(), RenderTFCubeOfAnnihilation::new);
		RenderingRegistry.registerEntityRenderingHandler(harbinger_cube.get(), RenderTFHarbingerCube::new);
		RenderingRegistry.registerEntityRenderingHandler(adherent.get(), m -> new RenderTFAdherent<>(m, new ModelTFAdherent<>(), 0.625F, "adherent.png"));
		RenderingRegistry.registerEntityRenderingHandler(roving_cube.get(), RenderTFRovingCube::new);
		RenderingRegistry.registerEntityRenderingHandler(rising_zombie.get(), m -> new RenderTFBiped<>(m, new ModelTFRisingZombie<>(), 0.5F, "textures/entity/zombie/zombie.png"));

		RenderingRegistry.registerEntityRenderingHandler(castle_guardian.get(), m -> new RenderTFCastleGuardian<>(m, new ModelTFCastleGuardian<>(), 2.0F, "finalcastle/castle_guardian.png"));

		// projectiles
		RenderingRegistry.registerEntityRenderingHandler(nature_bolt.get(), m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(lich_bolt.get(), m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(wand_bolt.get(), m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(tome_bolt.get(), m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(hydra_mortar.get(), RenderTFHydraMortar::new);
		RenderingRegistry.registerEntityRenderingHandler(slime_blob.get(), m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(moonworm_shot.get(), RenderTFMoonwormShot::new);
		RenderingRegistry.registerEntityRenderingHandler(charm_effect.get(), m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(lich_bomb.get(), m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(thrown_wep.get(), RenderTFThrownWep::new);
		RenderingRegistry.registerEntityRenderingHandler(falling_ice.get(), RenderTFFallingIce::new);
		RenderingRegistry.registerEntityRenderingHandler(thrown_ice.get(), RenderTFThrownIce::new);
		RenderingRegistry.registerEntityRenderingHandler(ice_snowball.get(), m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(slider.get(), RenderTFSlideBlock::new);
		RenderingRegistry.registerEntityRenderingHandler(seeker_arrow.get(), RenderDefaultArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(ice_arrow.get(), RenderDefaultArrow::new);

		// I guess the hydra gets its own section
		RenderingRegistry.registerEntityRenderingHandler(hydra_head.get(), m -> new RenderTFHydraHead<>(m, new ModelTFHydraHead<>(), 1.0F));
		//RenderingRegistry.registerEntityRenderingHandler(EntityTFHydraNeck.class, m -> new RenderTFGenericLiving<>(m, new ModelTFHydraNeck(), 1.0F, "hydra4.png"));
	}
}
