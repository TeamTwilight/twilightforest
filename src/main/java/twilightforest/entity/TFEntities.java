package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.SilverfishModel;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.entity.*;
import twilightforest.client.renderer.entity.*;
import twilightforest.entity.boss.*;
import twilightforest.entity.passive.*;
import twilightforest.entity.projectile.*;
import twilightforest.item.ItemTFTransformPowder;
import twilightforest.item.TFItems;
import twilightforest.util.TFEntityNames;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFEntities {

	public static final EntitySpawnPlacementRegistry.PlacementType ON_ICE = EntitySpawnPlacementRegistry.PlacementType.create("TF_ON_ICE", (world, pos, entityType) -> {
		BlockState state = world.getBlockState(pos.down());
		Block block = state.getBlock();
		Material material = state.getMaterial();
		BlockPos up = pos.up();
		return (material == Material.ICE || material == Material.PACKED_ICE) && block != Blocks.BEDROCK && block != Blocks.BARRIER && WorldEntitySpawner.func_234968_a_(world, pos, world.getBlockState(pos), world.getFluidState(pos), entityType) && WorldEntitySpawner.func_234968_a_(world, up, world.getBlockState(up), world.getFluidState(up), entityType);
	});

	public static final EntitySpawnPlacementRegistry.PlacementType CLOUDS = EntitySpawnPlacementRegistry.PlacementType.create("CLOUD_DWELLERS", (world, pos, entityType) -> {
		BlockState state = world.getBlockState(pos.down());
		Block block = state.getBlock();
		BlockPos up = pos.up();
		return (block == TFBlocks.wispy_cloud.get() || block == TFBlocks.wispy_cloud.get()) && block != Blocks.BEDROCK && block != Blocks.BARRIER && WorldEntitySpawner.func_234968_a_(world, pos, world.getBlockState(pos), world.getFluidState(pos), entityType) && WorldEntitySpawner.func_234968_a_(world, up, world.getBlockState(up), world.getFluidState(up), entityType);
	});

	private static final List<EntityType<?>> ALL = new ArrayList<>();
	public static final EntityType<EntityTFBoar> wild_boar = make(TFEntityNames.WILD_BOAR, EntityTFBoar::new, EntityClassification.CREATURE, 0.9F, 0.9F);
	public static final EntityType<EntityTFBighorn> bighorn_sheep = make(TFEntityNames.BIGHORN_SHEEP, EntityTFBighorn::new, EntityClassification.CREATURE, 0.9F, 1.3F);
	public static final EntityType<EntityTFDeer> deer = make(TFEntityNames.DEER, EntityTFDeer::new, EntityClassification.CREATURE, 0.7F, 1.8F);
	public static final EntityType<EntityTFRedcap> redcap = make(TFEntityNames.REDCAP, EntityTFRedcap::new, EntityClassification.MONSTER, 0.9F, 1.4F);
	public static final EntityType<EntityTFSwarmSpider> swarm_spider = make(TFEntityNames.SWARM_SPIDER, EntityTFSwarmSpider::new, EntityClassification.MONSTER, 0.8F, 0.4F);
	public static final EntityType<EntityTFNaga> naga = make(TFEntityNames.NAGA, EntityTFNaga::new, EntityClassification.MONSTER, 1.75f, 3.0f);
	public static final EntityType<EntityTFSkeletonDruid> skeleton_druid = make(TFEntityNames.SKELETON_DRUID, EntityTFSkeletonDruid::new, EntityClassification.MONSTER, 0.6F, 1.99F);
	public static final EntityType<EntityTFHostileWolf> hostile_wolf = make(TFEntityNames.HOSTILE_WOLF, EntityTFHostileWolf::new, EntityClassification.MONSTER, 0.6F, 0.85F);
	public static final EntityType<EntityTFWraith> wraith = make(TFEntityNames.WRAITH, EntityTFWraith::new, EntityClassification.MONSTER);
	public static final EntityType<EntityTFHedgeSpider> hedge_spider = make(TFEntityNames.HEDGE_SPIDER, EntityTFHedgeSpider::new, EntityClassification.MONSTER, 1.4F, 0.9F);
	public static final EntityType<EntityTFHydra> hydra = make(TFEntityNames.HYDRA, EntityTFHydra::new, EntityClassification.MONSTER, 16F, 12F);
	public static final EntityType<EntityTFLich> lich = make(TFEntityNames.LICH, EntityTFLich::new, EntityClassification.MONSTER, 1.1F, 2.1F);
	public static final EntityType<EntityTFPenguin> penguin = make(TFEntityNames.PENGUIN, EntityTFPenguin::new, EntityClassification.CREATURE, 0.5F, 0.9F);
	public static final EntityType<EntityTFLichMinion> lich_minion = make(TFEntityNames.LICH_MINION, EntityTFLichMinion::new, EntityClassification.MONSTER, 0.6F, 1.95F);
	public static final EntityType<EntityTFLoyalZombie> loyal_zombie = make(TFEntityNames.LOYAL_ZOMBIE, EntityTFLoyalZombie::new, EntityClassification.MONSTER);
	public static final EntityType<EntityTFTinyBird> tiny_bird = make(TFEntityNames.TINY_BIRD, EntityTFTinyBird::new, EntityClassification.CREATURE, 0.3F, 0.3F);
	public static final EntityType<EntityTFSquirrel> squirrel = make(TFEntityNames.SQUIRREL, EntityTFSquirrel::new, EntityClassification.CREATURE, 0.3F, 0.5F);
	public static final EntityType<EntityTFBunny> bunny = make(TFEntityNames.BUNNY, EntityTFBunny::new, EntityClassification.CREATURE, 0.3F, 0.6F);
	public static final EntityType<EntityTFRaven> raven = make(TFEntityNames.RAVEN, EntityTFRaven::new, EntityClassification.CREATURE, 0.3F, 0.5F);
	public static final EntityType<EntityTFQuestRam> quest_ram = make(TFEntityNames.QUEST_RAM, EntityTFQuestRam::new, EntityClassification.CREATURE, 1.25F, 2.9F);
	public static final EntityType<EntityTFKobold> kobold = make(TFEntityNames.KOBOLD, EntityTFKobold::new, EntityClassification.MONSTER, 0.8F, 1.1F);
	public static final EntityType<EntityTFMosquitoSwarm> mosquito_swarm = make(TFEntityNames.MOSQUITO_SWARM, EntityTFMosquitoSwarm::new, EntityClassification.MONSTER, 0.7F, 1.9F);
	public static final EntityType<EntityTFDeathTome> death_tome = make(TFEntityNames.DEATH_TOME, EntityTFDeathTome::new, EntityClassification.MONSTER);
	public static final EntityType<EntityTFMinotaur> minotaur = make(TFEntityNames.MINOTAUR, EntityTFMinotaur::new, EntityClassification.MONSTER, 0.6F, 2.1F);
	public static final EntityType<EntityTFMinoshroom> minoshroom = make(TFEntityNames.MINOSHROOM, EntityTFMinoshroom::new, EntityClassification.MONSTER, 1.49F, 2.5F);
	public static final EntityType<EntityTFFireBeetle> fire_beetle = make(TFEntityNames.FIRE_BEETLE, EntityTFFireBeetle::new, EntityClassification.MONSTER, 1.1F, 0.5F);
	public static final EntityType<EntityTFSlimeBeetle> slime_beetle = make(TFEntityNames.SLIME_BEETLE, EntityTFSlimeBeetle::new, EntityClassification.MONSTER, 0.9F, 0.5F);
	public static final EntityType<EntityTFPinchBeetle> pinch_beetle = make(TFEntityNames.PINCH_BEETLE, EntityTFPinchBeetle::new, EntityClassification.MONSTER, 1.2F, 0.5F);
	public static final EntityType<EntityTFMazeSlime> maze_slime = make(TFEntityNames.MAZE_SLIME, EntityTFMazeSlime::new, EntityClassification.MONSTER, 2.04F, 2.04F);
	public static final EntityType<EntityTFRedcapSapper> redcap_sapper = make(TFEntityNames.REDCAP_SAPPER, EntityTFRedcapSapper::new, EntityClassification.MONSTER, 0.9F, 1.4F);
	public static final EntityType<EntityTFMistWolf> mist_wolf = make(TFEntityNames.MIST_WOLF, EntityTFMistWolf::new, EntityClassification.MONSTER, 1.4F, 1.9F);
	public static final EntityType<EntityTFKingSpider> king_spider = make(TFEntityNames.KING_SPIDER, EntityTFKingSpider::new, EntityClassification.MONSTER, 1.6F, 1.6F);
	public static final EntityType<EntityTFMiniGhast> mini_ghast = make(TFEntityNames.MINI_GHAST, EntityTFMiniGhast::new, EntityClassification.MONSTER, 1.1F, 1.5F);
	public static final EntityType<EntityTFTowerGhast> tower_ghast = make(TFEntityNames.TOWER_GHAST, EntityTFTowerGhast::new, EntityClassification.MONSTER, 4.0F, 6.0F);
	public static final EntityType<EntityTFTowerGolem> tower_golem = make(TFEntityNames.TOWER_GOLEM, EntityTFTowerGolem::new, EntityClassification.MONSTER, 1.4F, 2.9F);
	public static final EntityType<EntityTFTowerTermite> tower_termite = make(TFEntityNames.TOWER_TERMITE, EntityTFTowerTermite::new, EntityClassification.MONSTER, 0.4F, 0.3F);
	public static final EntityType<EntityTFTowerBroodling> tower_broodling = make(TFEntityNames.TOWER_BROODLING, EntityTFTowerBroodling::new, EntityClassification.MONSTER, 0.7F, 0.5F);
	public static final EntityType<EntityTFUrGhast> ur_ghast = make(TFEntityNames.UR_GHAST, EntityTFUrGhast::new, EntityClassification.MONSTER, 14.0F, 18.0F);
	public static final EntityType<EntityTFBlockGoblin> blockchain_goblin = make(TFEntityNames.BLOCKCHAIN_GOBLIN, EntityTFBlockGoblin::new, EntityClassification.MONSTER, 0.9F, 1.4F);
	public static final EntityType<EntityTFGoblinKnightUpper> goblin_knight_upper = make(TFEntityNames.GOBLIN_KNIGHT_UPPER, EntityTFGoblinKnightUpper::new, EntityClassification.MONSTER, 1.1F, 1.3F);
	public static final EntityType<EntityTFGoblinKnightLower> goblin_knight_lower = make(TFEntityNames.GOBLIN_KNIGHT_LOWER, EntityTFGoblinKnightLower::new, EntityClassification.MONSTER, 0.7F, 1.1F);
	public static final EntityType<EntityTFHelmetCrab> helmet_crab = make(TFEntityNames.HELMET_CRAB, EntityTFHelmetCrab::new, EntityClassification.MONSTER, 0.8F, 1.1F);
	public static final EntityType<EntityTFKnightPhantom> knight_phantom = make(TFEntityNames.KNIGHT_PHANTOM, EntityTFKnightPhantom::new, EntityClassification.MONSTER, 1.5F, 3.0F);
	public static final EntityType<EntityTFYeti> yeti = make(TFEntityNames.YETI, EntityTFYeti::new, EntityClassification.MONSTER, 1.4F, 2.4F);
	public static final EntityType<EntityTFYetiAlpha> yeti_alpha = make(TFEntityNames.YETI_ALPHA, EntityTFYetiAlpha::new, EntityClassification.MONSTER, 3.8F, 5.0F);
	public static final EntityType<EntityTFWinterWolf> winter_wolf = make(TFEntityNames.WINTER_WOLF, EntityTFWinterWolf::new, EntityClassification.MONSTER, 1.4F, 1.9F);
	public static final EntityType<EntityTFSnowGuardian> snow_guardian = make(TFEntityNames.SNOW_GUARDIAN, EntityTFSnowGuardian::new, EntityClassification.MONSTER, 0.6F, 1.8F);
	public static final EntityType<EntityStableIceCore> stable_ice_core = make(TFEntityNames.STABLE_ICE_CORE, EntityStableIceCore::new, EntityClassification.MONSTER, 0.8F, 1.8F);
	public static final EntityType<EntityUnstableIceCore> unstable_ice_core = make(TFEntityNames.UNSTABLE_ICE_CORE, EntityUnstableIceCore::new, EntityClassification.MONSTER, 0.8F, 1.8F);
	public static final EntityType<EntityTFSnowQueen> snow_queen = make(TFEntityNames.SNOW_QUEEN, EntityTFSnowQueen::new, EntityClassification.MONSTER, 0.7F, 2.2F);
	public static final EntityType<EntityTFTroll> troll = make(TFEntityNames.TROLL, EntityTFTroll::new, EntityClassification.MONSTER, 1.4F, 2.4F);
	public static final EntityType<EntityTFGiantMiner> giant_miner = make(TFEntityNames.GIANT_MINER, EntityTFGiantMiner::new, EntityClassification.MONSTER, 2.4F, 7.2F);
	public static final EntityType<EntityTFArmoredGiant> armored_giant = make(TFEntityNames.ARMORED_GIANT, EntityTFArmoredGiant::new, EntityClassification.MONSTER, 2.4F, 7.2F);
	public static final EntityType<EntityTFIceCrystal> ice_crystal = make(TFEntityNames.ICE_CRYSTAL, EntityTFIceCrystal::new, EntityClassification.MONSTER, 0.6F, 1.8F);
	public static final EntityType<EntityTFHarbingerCube> harbinger_cube = make(TFEntityNames.HARBINGER_CUBE, EntityTFHarbingerCube::new, EntityClassification.MONSTER, 1.9F, 2.4F);
	public static final EntityType<EntityTFAdherent> adherent = make(TFEntityNames.ADHERENT, EntityTFAdherent::new, EntityClassification.MONSTER, 0.8F, 2.2F);
	public static final EntityType<EntityTFRovingCube> roving_cube = make(TFEntityNames.ROVING_CUBE, EntityTFRovingCube::new, EntityClassification.MONSTER, 1.2F, 2.1F);
	//public static final EntityType<EntityTFCastleGuardian> castle_guardian = make(TFEntityNames.CASTLE_GUARDIAN, EntityTFCastleGuardian::new, EntityClassification.MONSTER, 1.8F, 2.4F);
	public static final EntityType<EntityTFPlateauBoss> plateau_boss = make(TFEntityNames.PLATEAU_BOSS, EntityTFPlateauBoss::new, EntityClassification.MONSTER, 1F, 1F);

	public static final EntityType<EntityTFNatureBolt> nature_bolt = build(TFEntityNames.NATURE_BOLT, makeCastedBuilder(EntityTFNatureBolt.class, EntityTFNatureBolt::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150).setUpdateInterval(5));
	public static final EntityType<EntityTFLichBolt> lich_bolt = build(TFEntityNames.LICH_BOLT, makeCastedBuilder(EntityTFLichBolt.class, EntityTFLichBolt::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150).setUpdateInterval(2));
	public static final EntityType<EntityTFTwilightWandBolt> wand_bolt = build(TFEntityNames.WAND_BOLT, makeCastedBuilder(EntityTFTwilightWandBolt.class, EntityTFTwilightWandBolt::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150).setUpdateInterval(5));
	public static final EntityType<EntityTFTomeBolt> tome_bolt = build(TFEntityNames.TOME_BOLT, makeCastedBuilder(EntityTFTomeBolt.class, EntityTFTomeBolt::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150).setUpdateInterval(5));
	public static final EntityType<EntityTFHydraMortar> hydra_mortar = build(TFEntityNames.HYDRA_MORTAR, makeCastedBuilder(EntityTFHydraMortar.class, EntityTFHydraMortar::new, EntityClassification.MISC).size(0.75F, 0.75F).setTrackingRange(150));
	public static final EntityType<EntityTFLichBomb> lich_bomb = build(TFEntityNames.LICH_BOMB, makeCastedBuilder(EntityTFLichBomb.class, EntityTFLichBomb::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150));
	public static final EntityType<EntityTFMoonwormShot> moonworm_shot = build(TFEntityNames.MOONWORM_SHOT, makeCastedBuilder(EntityTFMoonwormShot.class, EntityTFMoonwormShot::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150));
	public static final EntityType<EntityTFCicadaShot> cicada_shot = build(TFEntityNames.CICADA_SHOT, makeCastedBuilder(EntityTFCicadaShot.class, EntityTFCicadaShot::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150));
	public static final EntityType<EntityTFSlimeProjectile> slime_blob = build(TFEntityNames.SLIME_BLOB, makeCastedBuilder(EntityTFSlimeProjectile.class, EntityTFSlimeProjectile::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150));
	public static final EntityType<EntityTFCharmEffect> charm_effect = make(TFEntityNames.CHARM_EFFECT, EntityTFCharmEffect::new, EntityClassification.MISC, 0.25F, 0.25F);
	public static final EntityType<EntityTFThrownWep> thrown_wep = make(TFEntityNames.THROWN_WEP, EntityTFThrownWep::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final EntityType<EntityTFFallingIce> falling_ice = make(TFEntityNames.FALLING_ICE, EntityTFFallingIce::new, EntityClassification.MISC, 2.98F, 2.98F);
	public static final EntityType<EntityTFIceBomb> thrown_ice = build(TFEntityNames.THROWN_ICE, makeCastedBuilder(EntityTFIceBomb.class, EntityTFIceBomb::new, EntityClassification.MISC).size(1.0F, 1.0F).setUpdateInterval(2));
	public static final EntityType<EntitySeekerArrow> seeker_arrow = build(TFEntityNames.SEEKER_ARROW, makeCastedBuilder(EntitySeekerArrow.class, EntitySeekerArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).setTrackingRange(150).setUpdateInterval(1));
	public static final EntityType<EntityIceArrow> ice_arrow = build(TFEntityNames.ICE_ARROW, makeCastedBuilder(EntityIceArrow.class, EntityIceArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).setTrackingRange(150).setUpdateInterval(1));
	public static final EntityType<EntityTFIceSnowball> ice_snowball = build(TFEntityNames.ICE_SNOWBALL, makeCastedBuilder(EntityTFIceSnowball.class, EntityTFIceSnowball::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(150));
	public static final EntityType<EntityTFChainBlock> chain_block = build(TFEntityNames.CHAIN_BLOCK, makeCastedBuilder(EntityTFChainBlock.class, EntityTFChainBlock::new, EntityClassification.MISC).size(0.6F, 0.6F).setUpdateInterval(1));
	public static final EntityType<EntityTFCubeOfAnnihilation> cube_of_annihilation = build(TFEntityNames.CUBE_OF_ANNIHILATION, makeCastedBuilder(EntityTFCubeOfAnnihilation.class, EntityTFCubeOfAnnihilation::new, EntityClassification.MISC).size(1F, 1F).setUpdateInterval(1));
	public static final EntityType<EntityTFSlideBlock> slider = build(TFEntityNames.SLIDER, makeCastedBuilder(EntityTFSlideBlock.class, EntityTFSlideBlock::new, EntityClassification.MISC).size(0.98F, 0.98F).setUpdateInterval(1));
	//public static final EntityType<EntityTFBoggard> boggard = make(TFEntityNames.BOGGARD, EntityTFBoggard::new, EntityClassification.MONSTER, 0.8F, 1.1F);
	public static final EntityType<EntityTFRisingZombie> rising_zombie = make(TFEntityNames.RISING_ZOMBIE, EntityTFRisingZombie::new, EntityClassification.MONSTER, 0.6F, 1.95F);
	public static final EntityType<EntityTFProtectionBox> protection_box = build(TFEntityNames.PROTECTION_BOX, makeCastedBuilder(EntityTFProtectionBox.class, EntityTFProtectionBox::new, EntityClassification.MISC).disableSerialization().disableSummoning().size(0, 0));

	private static <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.IFactory<E> factory, EntityClassification classification, float width, float height) {
		return build(id, makeBuilder(factory, classification).size(width, height));
	}

	private static <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.IFactory<E> factory, EntityClassification classification) {
		return make(id, factory, classification, 0.6F, 1.8F);
	}

	@SuppressWarnings("unchecked")
	private static <E extends Entity> EntityType<E> build(ResourceLocation id, EntityType.Builder<E> builder) {
		boolean cache = SharedConstants.useDatafixers;
		SharedConstants.useDatafixers = false;
		EntityType<E> ret = (EntityType<E>) builder.build(id.toString()).setRegistryName(id);
		SharedConstants.useDatafixers = cache;
		ALL.add(ret);
		return ret;
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

	private static Item spawnEgg(EntityType<?> type, int color, int color2) {
		ResourceLocation eggId = new ResourceLocation(type.getRegistryName().getNamespace(), type.getRegistryName().getPath() + "_spawn_egg");
		return new SpawnEggItem(type, color, color2, TFItems.defaultBuilder()).setRegistryName(eggId);
	}

	@SubscribeEvent
	public static void registerSpawnEggs(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		r.register(spawnEgg(adherent, 0x0a0000, 0x00008b));
		r.register(spawnEgg(yeti_alpha, 0xcdcdcd, 0x29486e));
		r.register(spawnEgg(armored_giant, 0x239391, 0x9a9a9a));
		r.register(spawnEgg(bighorn_sheep, 0xdbceaf, 0xd7c771));
		r.register(spawnEgg(blockchain_goblin, 0xd3e7bc, 0x1f3fff));
		r.register(spawnEgg(tower_broodling, 0x343c14, 0xbaee02));
		r.register(spawnEgg(tower_ghast, 0xbcbcbc, 0xb77878));
		r.register(spawnEgg(mini_ghast, 0xbcbcbc, 0xa74343));
		r.register(spawnEgg(tower_golem, 0x6b3d20, 0xe2ddda));
		r.register(spawnEgg(troll, 0x9ea98f, 0xb0948e));
		r.register(spawnEgg(death_tome, 0x774e22, 0xdbcdbe));
		r.register(spawnEgg(deer, 0x7b4d2e, 0x4b241d));
		r.register(spawnEgg(bunny, 0xfefeee, 0xccaa99));
		r.register(spawnEgg(fire_beetle, 0x1d0b00, 0xcb6f25));
		r.register(spawnEgg(squirrel, 0x904f12, 0xeeeeee));
		r.register(spawnEgg(giant_miner, 0x211b52, 0x9a9a9a));
		r.register(spawnEgg(goblin_knight_lower, 0x566055, 0xd3e7bc));
		r.register(spawnEgg(harbinger_cube, 0x00000a, 0x8b0000));
		r.register(spawnEgg(hedge_spider, 0x235f13, 0x562653));
		r.register(spawnEgg(helmet_crab, 0xfb904b, 0xd3e7bc));
		r.register(spawnEgg(hostile_wolf, 0xd7d3d3, 0xab1e14));
		r.register(spawnEgg(hydra, 0x142940, 0x29806b));
		r.register(spawnEgg(ice_crystal, 0xdce9fe, 0xadcafb));
		r.register(spawnEgg(king_spider, 0x2c1a0e, 0xffc017));
		r.register(spawnEgg(knight_phantom, 0xa6673b, 0xd3e7bc));
		r.register(spawnEgg(kobold, 0x372096, 0x895d1b));
		r.register(spawnEgg(maze_slime, 0xa3a3a3, 0x2a3b17));
		r.register(spawnEgg(minoshroom, 0xa81012, 0xaa7d66));
		r.register(spawnEgg(minotaur, 0x3f3024, 0xaa7d66));
		r.register(spawnEgg(mist_wolf, 0x3a1411, 0xe2c88a));
		r.register(spawnEgg(mosquito_swarm, 0x080904, 0x2d2f21));
		r.register(spawnEgg(naga, 0xa4d316, 0x1b380b));
		r.register(spawnEgg(penguin, 0x12151b, 0xf9edd2));
		r.register(spawnEgg(pinch_beetle, 0xbc9327, 0x241609));
		r.register(spawnEgg(quest_ram, 0xfefeee, 0x33aadd));
		r.register(spawnEgg(raven, 0x000011, 0x222233));
		r.register(spawnEgg(redcap, 0x3b3a6c, 0xab1e14));
		r.register(spawnEgg(redcap_sapper, 0x575d21, 0xab1e14));
		r.register(spawnEgg(roving_cube, 0x0a0000, 0x00009b));
		r.register(spawnEgg(skeleton_druid, 0xa3a3a3, 0x2a3b17));
		r.register(spawnEgg(slime_beetle, 0x0c1606, 0x60a74c));
		r.register(spawnEgg(snow_guardian, 0xd3e7bc, 0xfefefe));
		r.register(spawnEgg(snow_queen, 0xb1b2d4, 0x87006e));
		r.register(spawnEgg(stable_ice_core, 0xa1bff3, 0x7000f8));
		r.register(spawnEgg(swarm_spider, 0x32022e, 0x17251e));
		r.register(spawnEgg(tiny_bird, 0x33aadd, 0x1188ee));
		r.register(spawnEgg(tower_termite, 0x5d2b21, 0xaca03a));
		r.register(spawnEgg(lich, 0xaca489, 0x360472));
		r.register(spawnEgg(unstable_ice_core, 0x9aacf5, 0x9b0fa5));
		r.register(spawnEgg(ur_ghast, 0xbcbcbc, 0xb77878));
		r.register(spawnEgg(wild_boar, 0x83653b, 0xffefca));
		r.register(spawnEgg(winter_wolf, 0xdfe3e5, 0xb2bcca));
		r.register(spawnEgg(wraith, 0x505050, 0x838383));
		r.register(spawnEgg(yeti, 0xdedede, 0x4675bb));
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
		evt.getRegistry().registerAll(ALL.toArray(new EntityType<?>[0]));
		((ItemTFTransformPowder) TFItems.transformation_powder.get()).initTransformations();

		EntitySpawnPlacementRegistry.register(wild_boar, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
		EntitySpawnPlacementRegistry.register(bighorn_sheep, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
		EntitySpawnPlacementRegistry.register(deer, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
		EntitySpawnPlacementRegistry.register(redcap, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(skeleton_druid, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFSkeletonDruid::skeletonDruidSpawnHandler);
		EntitySpawnPlacementRegistry.register(wraith, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFWraith::getCanSpawnHere);
		EntitySpawnPlacementRegistry.register(hydra, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(lich, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(penguin, ON_ICE, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFPenguin::canSpawn);
		EntitySpawnPlacementRegistry.register(lich_minion, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(loyal_zombie, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(tiny_bird, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
		EntitySpawnPlacementRegistry.register(squirrel, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
		EntitySpawnPlacementRegistry.register(bunny, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
		EntitySpawnPlacementRegistry.register(raven, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
		EntitySpawnPlacementRegistry.register(quest_ram, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
		EntitySpawnPlacementRegistry.register(kobold, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(mosquito_swarm, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFMosquitoSwarm::canSpawn);
		EntitySpawnPlacementRegistry.register(death_tome, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(minotaur, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(minoshroom, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(fire_beetle, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(slime_beetle, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(pinch_beetle, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(mist_wolf, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFHostileWolf::getCanSpawnHere);
		EntitySpawnPlacementRegistry.register(mini_ghast, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFMiniGhast::canSpawnHere);
		EntitySpawnPlacementRegistry.register(tower_golem, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(tower_termite, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(tower_ghast, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFTowerGhast::ghastSpawnHandler);
		EntitySpawnPlacementRegistry.register(ur_ghast, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(blockchain_goblin, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(goblin_knight_upper, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(goblin_knight_lower, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(helmet_crab, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(knight_phantom, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(naga, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(swarm_spider, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFSwarmSpider::getCanSpawnHere);
		EntitySpawnPlacementRegistry.register(king_spider, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(tower_broodling, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFSwarmSpider::getCanSpawnHere);
		EntitySpawnPlacementRegistry.register(hedge_spider, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFHedgeSpider::canSpawn);
		EntitySpawnPlacementRegistry.register(redcap_sapper, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(maze_slime, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFMazeSlime::getCanSpawnHere);
		EntitySpawnPlacementRegistry.register(yeti, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFYeti::yetiSnowyForestSpawnHandler);
		EntitySpawnPlacementRegistry.register(yeti_alpha, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(winter_wolf, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFWinterWolf::canSpawnHere);
		EntitySpawnPlacementRegistry.register(snow_guardian, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(stable_ice_core, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(unstable_ice_core, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(snow_queen, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(troll, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFTroll::canSpawn);
		EntitySpawnPlacementRegistry.register(giant_miner, CLOUDS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFGiantMiner::canSpawn);
		EntitySpawnPlacementRegistry.register(armored_giant, CLOUDS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTFGiantMiner::canSpawn);
		EntitySpawnPlacementRegistry.register(ice_crystal, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(harbinger_cube, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
		EntitySpawnPlacementRegistry.register(adherent, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(roving_cube, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
		EntitySpawnPlacementRegistry.register(rising_zombie, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);

		//EntitySpawnPlacementRegistry.register(castle_guardian, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
	}

	@SubscribeEvent
	public static void addEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(wild_boar, PigEntity.func_234215_eI_().create());
		event.put(bighorn_sheep, SheepEntity.registerAttributes().create());
		event.put(deer, EntityTFDeer.registerAttributes().create());
		event.put(redcap, EntityTFRedcap.registerAttributes().create());
		event.put(swarm_spider, EntityTFSwarmSpider.registerAttributes().create());
		event.put(naga, EntityTFNaga.registerAttributes().create());
		event.put(skeleton_druid, AbstractSkeletonEntity.registerAttributes().create());
		event.put(hostile_wolf, EntityTFHostileWolf.registerAttributes().create());
		event.put(wraith, EntityTFWraith.registerAttributes().create());
		event.put(hedge_spider, SpiderEntity.func_234305_eI_().create());
		event.put(hydra, EntityTFHydra.registerAttributes().create());
		event.put(lich, EntityTFLich.registerAttributes().create());
		event.put(penguin, EntityTFPenguin.registerAttributes().create());
		event.put(lich_minion, ZombieEntity.func_234342_eQ_().create());
		event.put(loyal_zombie, EntityTFLoyalZombie.registerAttributes().create());
		event.put(tiny_bird, EntityTFTinyBird.registerAttributes().create());
		event.put(squirrel, EntityTFSquirrel.registerAttributes().create());
		event.put(bunny, EntityTFBunny.registerAttributes().create());
		event.put(raven, EntityTFRaven.registerAttributes().create());
		event.put(quest_ram, EntityTFQuestRam.registerAttributes().create());
		event.put(kobold, EntityTFKobold.registerAttributes().create());
		event.put(mosquito_swarm, EntityTFMosquitoSwarm.registerAttributes().create());
		event.put(death_tome, EntityTFDeathTome.registerAttributes().create());
		event.put(minotaur, EntityTFMinotaur.registerAttributes().create());
		event.put(minoshroom, EntityTFMinoshroom.registerAttributes().create());
		event.put(fire_beetle, EntityTFFireBeetle.registerAttributes().create());
		event.put(slime_beetle, EntityTFSlimeBeetle.registerAttributes().create());
		event.put(pinch_beetle, EntityTFPinchBeetle.registerAttributes().create());
		event.put(maze_slime, EntityTFMazeSlime.registerAttributes().create());
		event.put(redcap_sapper, EntityTFRedcapSapper.registerAttributes().create());
		event.put(mist_wolf, EntityTFMistWolf.registerAttributes().create());
		event.put(king_spider, EntityTFKingSpider.registerAttributes().create());
		event.put(mini_ghast, EntityTFMiniGhast.registerAttributes().create());
		event.put(tower_ghast, EntityTFTowerGhast.registerAttributes().create());
		event.put(tower_golem, EntityTFTowerGolem.registerAttributes().create());
		event.put(tower_termite, EntityTFTowerTermite.registerAttributes().create());
		event.put(tower_broodling, EntityTFTowerBroodling.registerAttributes().create());
		event.put(ur_ghast, EntityTFUrGhast.registerAttributes().create());
		event.put(blockchain_goblin, EntityTFBlockGoblin.registerAttributes().create());
		event.put(goblin_knight_upper, EntityTFGoblinKnightUpper.registerAttributes().create());
		event.put(goblin_knight_lower, EntityTFGoblinKnightLower.registerAttributes().create());
		event.put(helmet_crab, EntityTFHelmetCrab.registerAttributes().create());
		event.put(knight_phantom, EntityTFKnightPhantom.registerAttributes().create());
		event.put(yeti, EntityTFYeti.registerAttributes().create());
		event.put(yeti_alpha, EntityTFYetiAlpha.registerAttributes().create());
		event.put(winter_wolf, EntityTFWinterWolf.registerAttributes().create());
		event.put(snow_guardian, EntityTFSnowGuardian.registerAttributes().create());
		event.put(stable_ice_core, EntityStableIceCore.registerAttributes().create());
		event.put(unstable_ice_core, EntityUnstableIceCore.registerAttributes().create());
		event.put(snow_queen, EntityTFSnowQueen.registerAttributes().create());
		event.put(troll, EntityTFTroll.registerAttributes().create());
		event.put(giant_miner, EntityTFGiantMiner.registerAttributes().create());
		event.put(armored_giant, EntityTFGiantMiner.registerAttributes().create());
		event.put(ice_crystal, EntityTFIceCrystal.registerAttributes().create());
		event.put(harbinger_cube, EntityTFHarbingerCube.registerAttributes().create());
		event.put(adherent, EntityTFAdherent.registerAttributes().create());
		event.put(roving_cube, EntityTFRovingCube.registerAttributes().create());
		//event.put(castle_guardian, MobEntity.func_233666_p_().create());
		event.put(plateau_boss, EntityTFPlateauBoss.registerAttributes().create());

		//event.put(boggard, EntityTFBoggard.registerAttributes().create());
		event.put(rising_zombie, ZombieEntity.func_234342_eQ_().create());
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerEntityRenderer() {
		RenderingRegistry.registerEntityRenderingHandler(wild_boar, m -> new RenderTFBoar(m, new ModelTFBoar<>()));
		RenderingRegistry.registerEntityRenderingHandler(bighorn_sheep, m -> new RenderTFBighorn(m, new ModelTFBighorn<>(), new ModelTFBighornFur(), 0.7F));
		RenderingRegistry.registerEntityRenderingHandler(deer, m -> new RenderTFGenericMob<>(m, new ModelTFDeer(), 0.7F, "wilddeer.png"));
		RenderingRegistry.registerEntityRenderingHandler(redcap, m -> new RenderTFBiped<>(m, new ModelTFRedcap<>(0.0F), new BipedModel<>(0.7F), new BipedModel<>(0.7F), 0.4F, "redcap.png"));
		RenderingRegistry.registerEntityRenderingHandler(skeleton_druid, m -> new RenderTFBiped<>(m, new ModelTFSkeletonDruid(), new ModelTFSkeletonDruid(), new ModelTFSkeletonDruid(), 0.5F, "skeletondruid.png"));
		RenderingRegistry.registerEntityRenderingHandler(hostile_wolf, WolfRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(wraith, m -> new RenderTFWraith(m, new ModelTFWraith(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(hydra, m -> new RenderTFHydra(m, new ModelTFHydra(), 4.0F));
		RenderingRegistry.registerEntityRenderingHandler(lich, m -> new RenderTFLich(m, new ModelTFLich(), 0.6F));
		RenderingRegistry.registerEntityRenderingHandler(penguin, m -> new RenderTFBird<>(m, new ModelTFPenguin(), 0.375F, "penguin.png"));
		RenderingRegistry.registerEntityRenderingHandler(lich_minion, m -> new RenderTFBiped<>(m, new ModelTFLichMinion(false), new ModelTFLichMinion(true), new ModelTFLichMinion(true), 0.5F, "textures/entity/zombie/zombie.png"));
		RenderingRegistry.registerEntityRenderingHandler(loyal_zombie, m -> new RenderTFBiped<>(m, new ModelTFLoyalZombie(false), new ModelTFLoyalZombie(true), new ModelTFLoyalZombie(true), 0.5F, "textures/entity/zombie/zombie.png"));
		RenderingRegistry.registerEntityRenderingHandler(tiny_bird, m -> new RenderTFTinyBird(m, new ModelTFTinyBird(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(squirrel, m -> new RenderTFGenericMob<>(m, new ModelTFSquirrel(), 0.3F, "squirrel2.png"));
		RenderingRegistry.registerEntityRenderingHandler(bunny, m -> new RenderTFBunny(m, new ModelTFBunny(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(raven, m -> new RenderTFBird<>(m, new ModelTFRaven(), 0.3F, "raven.png"));
		RenderingRegistry.registerEntityRenderingHandler(quest_ram, manager -> new RenderTFQuestRam(manager, new ModelTFQuestRam()));
		RenderingRegistry.registerEntityRenderingHandler(kobold, m -> new RenderTFKobold(m, new ModelTFKobold(), 0.4F, "kobold.png"));
		//RenderingRegistry.registerEntityRenderingHandler(boggard, m -> new RenderTFBiped<>(m, new BipedModel<>(0), 0.625F, "kobold.png"));
		RenderingRegistry.registerEntityRenderingHandler(mosquito_swarm, m -> new RenderTFGenericMob<>(m, new ModelTFMosquitoSwarm(), 0.0F, "mosquitoswarm.png"));
		RenderingRegistry.registerEntityRenderingHandler(death_tome, m -> new RenderTFGenericMob<>(m, new ModelTFDeathTome(), 0.3F, "textures/entity/enchanting_table_book.png"));
		RenderingRegistry.registerEntityRenderingHandler(minotaur, m -> new RenderTFBiped<>(m, new ModelTFMinotaur(), 0.625F, "minotaur.png"));
		RenderingRegistry.registerEntityRenderingHandler(minoshroom, m -> new RenderTFMinoshroom(m, new ModelTFMinoshroom(), 0.625F));
		RenderingRegistry.registerEntityRenderingHandler(fire_beetle, m -> new RenderTFGenericMob<>(m, new ModelTFFireBeetle(), 0.8F, "firebeetle.png"));
		RenderingRegistry.registerEntityRenderingHandler(slime_beetle, m -> new RenderTFSlimeBeetle(m, new ModelTFSlimeBeetle(), 0.6F));
		RenderingRegistry.registerEntityRenderingHandler(pinch_beetle, m -> new RenderTFGenericMob<>(m, new ModelTFPinchBeetle(), 0.6F, "pinchbeetle.png"));
		RenderingRegistry.registerEntityRenderingHandler(mist_wolf, RenderTFMistWolf::new);
		RenderingRegistry.registerEntityRenderingHandler(mini_ghast, m -> new RenderTFGhast<>(m, new ModelTFGhast<>(), 0.625F));
		RenderingRegistry.registerEntityRenderingHandler(tower_golem, m -> new RenderTFTowerGolem<>(m, new ModelTFTowerGolem<>(), 0.75F));
		RenderingRegistry.registerEntityRenderingHandler(tower_termite, m -> new RenderTFGenericMob<>(m, new SilverfishModel<>(), 0.3F, "towertermite.png"));
		RenderingRegistry.registerEntityRenderingHandler(tower_ghast, m -> new RenderTFTowerGhast<>(m, new ModelTFGhast<>(), 3.0F));
		RenderingRegistry.registerEntityRenderingHandler(ur_ghast, m -> new RenderTFUrGhast(m, new ModelTFTowerBoss(), 8.0F, 24F));
		RenderingRegistry.registerEntityRenderingHandler(blockchain_goblin, m -> new RenderTFBlockGoblin<>(m, new ModelTFBlockGoblin<>(), 0.4F));
		RenderingRegistry.registerEntityRenderingHandler(goblin_knight_upper, m -> new RenderTFGoblinKnightUpper(m, new ModelTFGoblinKnightUpper(), 0.625F));
		RenderingRegistry.registerEntityRenderingHandler(goblin_knight_lower, m -> new RenderTFBiped<>(m, new ModelTFGoblinKnightLower(), 0.625F, "doublegoblin.png"));
		RenderingRegistry.registerEntityRenderingHandler(helmet_crab, m -> new RenderTFGenericMob<>(m, new ModelTFHelmetCrab(), 0.625F, "helmetcrab.png"));
		RenderingRegistry.registerEntityRenderingHandler(knight_phantom, m -> new RenderTFKnightPhantom(m, new ModelTFKnightPhantom(), 0.625F));
		RenderingRegistry.registerEntityRenderingHandler(naga, m -> new RenderTFNaga<>(m, new ModelTFNaga<>(), 1.45F));
		RenderingRegistry.registerEntityRenderingHandler(swarm_spider, RenderTFSwarmSpider::new);
		RenderingRegistry.registerEntityRenderingHandler(king_spider, RenderTFKingSpider::new);
		RenderingRegistry.registerEntityRenderingHandler(tower_broodling, RenderTFTowerBroodling::new);
		RenderingRegistry.registerEntityRenderingHandler(hedge_spider, RenderTFHedgeSpider::new);
		RenderingRegistry.registerEntityRenderingHandler(redcap_sapper, m -> new RenderTFBiped<>(m, new ModelTFRedcap<>(0.0F), new BipedModel<>(0.75F), new BipedModel<>(0.75F), 0.4F, "redcapsapper.png"));
		RenderingRegistry.registerEntityRenderingHandler(maze_slime, m -> new RenderTFMazeSlime(m, 0.625F));
		RenderingRegistry.registerEntityRenderingHandler(yeti, m -> new RenderTFBiped<>(m, new ModelTFYeti<>(), 0.625F, "yeti2.png"));
		RenderingRegistry.registerEntityRenderingHandler(protection_box, RenderTFProtectionBox::new);
		RenderingRegistry.registerEntityRenderingHandler(yeti_alpha, m -> new RenderTFBiped<>(m, new ModelTFYetiAlpha(), 1.75F, "yetialpha.png"));
		RenderingRegistry.registerEntityRenderingHandler(winter_wolf, RenderTFWinterWolf::new);
		RenderingRegistry.registerEntityRenderingHandler(snow_guardian, m -> new RenderTFSnowGuardian(m, new ModelNoop<>()));
		RenderingRegistry.registerEntityRenderingHandler(stable_ice_core, m -> new RenderTFIceShooter(m, new ModelTFIceShooter()));
		RenderingRegistry.registerEntityRenderingHandler(unstable_ice_core, m -> new RenderTFIceExploder<>(m, new ModelTFIceExploder<>()));
		RenderingRegistry.registerEntityRenderingHandler(snow_queen, m -> new RenderTFSnowQueen(m, new ModelTFSnowQueen()));
		RenderingRegistry.registerEntityRenderingHandler(troll, m -> new RenderTFBiped<>(m, new ModelTFTroll(), 0.625F, "troll.png"));
		RenderingRegistry.registerEntityRenderingHandler(giant_miner, RenderTFGiant::new);
		RenderingRegistry.registerEntityRenderingHandler(armored_giant, RenderTFGiant::new);
		RenderingRegistry.registerEntityRenderingHandler(ice_crystal, RenderTFIceCrystal::new);
		RenderingRegistry.registerEntityRenderingHandler(chain_block, RenderTFChainBlock::new);
		RenderingRegistry.registerEntityRenderingHandler(cube_of_annihilation, RenderTFCubeOfAnnihilation::new);
		RenderingRegistry.registerEntityRenderingHandler(harbinger_cube, RenderTFHarbingerCube::new);
		RenderingRegistry.registerEntityRenderingHandler(adherent, RenderAdherent::new);
		RenderingRegistry.registerEntityRenderingHandler(roving_cube, RenderTFRovingCube::new);
		RenderingRegistry.registerEntityRenderingHandler(rising_zombie, m -> new RenderTFBiped<>(m, new ModelTFRisingZombie(false), new ModelTFRisingZombie(true), new ModelTFRisingZombie(true), 0.5F, "textures/entity/zombie/zombie.png"));
		//RenderingRegistry.registerEntityRenderingHandler(castle_guardian, m -> new RenderTFCastleGuardian(m, new ModelTFCastleGuardian(), 2.0F, "finalcastle/castle_guardian.png"));
		RenderingRegistry.registerEntityRenderingHandler(plateau_boss, RenderNoop::new);

		// projectiles
		RenderingRegistry.registerEntityRenderingHandler(nature_bolt, m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(lich_bolt, m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(wand_bolt, m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(tome_bolt, m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(hydra_mortar, RenderTFHydraMortar::new);
		RenderingRegistry.registerEntityRenderingHandler(slime_blob, m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(cicada_shot, RenderTFCicadaShot::new);
		RenderingRegistry.registerEntityRenderingHandler(moonworm_shot, RenderTFMoonwormShot::new);
		RenderingRegistry.registerEntityRenderingHandler(charm_effect, m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(lich_bomb, m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(thrown_wep, RenderTFThrownWep::new);
		RenderingRegistry.registerEntityRenderingHandler(falling_ice, RenderTFFallingIce::new);
		RenderingRegistry.registerEntityRenderingHandler(thrown_ice, RenderTFThrownIce::new);
		RenderingRegistry.registerEntityRenderingHandler(ice_snowball, m -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(slider, RenderTFSlideBlock::new);
		RenderingRegistry.registerEntityRenderingHandler(seeker_arrow, RenderDefaultArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(ice_arrow, RenderDefaultArrow::new);

	}
}
