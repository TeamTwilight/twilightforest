package twilightforest.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.*;
import twilightforest.client.model.entity.legacy.*;
import twilightforest.client.renderer.entity.*;
import twilightforest.client.renderer.entity.legacy.*;
import twilightforest.compat.UndergardenCompat;
import twilightforest.entity.boss.*;
import twilightforest.entity.monster.*;
import twilightforest.entity.passive.*;
import twilightforest.entity.projectile.*;
import twilightforest.item.TFItems;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFEntities {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, TwilightForestMod.ID);
	public static final DeferredRegister<Item> SPAWN_EGGS = DeferredRegister.create(ForgeRegistries.ITEMS, TwilightForestMod.ID);


	public static final SpawnPlacements.Type ON_ICE = SpawnPlacements.Type.create("TF_ON_ICE", (world, pos, entityType) -> {
		BlockState state = world.getBlockState(pos.below());
		Block block = state.getBlock();
		Material material = state.getMaterial();
		BlockPos up = pos.above();
		return (material == Material.ICE || material == Material.ICE_SOLID) && block != Blocks.BEDROCK && block != Blocks.BARRIER && NaturalSpawner.isValidEmptySpawnBlock(world, pos, world.getBlockState(pos), world.getFluidState(pos), entityType) && NaturalSpawner.isValidEmptySpawnBlock(world, up, world.getBlockState(up), world.getFluidState(up), entityType);
	});

	public static final SpawnPlacements.Type CLOUDS = SpawnPlacements.Type.create("CLOUD_DWELLERS", (world, pos, entityType) -> {
		BlockState state = world.getBlockState(pos.below());
		Block block = state.getBlock();
		BlockPos up = pos.above();
		return (block == TFBlocks.WISPY_CLOUD.get() || block == TFBlocks.FLUFFY_CLOUD.get()) && block != Blocks.BEDROCK && block != Blocks.BARRIER && NaturalSpawner.isValidEmptySpawnBlock(world, pos, world.getBlockState(pos), world.getFluidState(pos), entityType) && NaturalSpawner.isValidEmptySpawnBlock(world, up, world.getBlockState(up), world.getFluidState(up), entityType);
	});

	public static final RegistryObject<EntityType<Adherent>> ADHERENT = make(TFEntityNames.ADHERENT, Adherent::new, MobCategory.MONSTER, 0.8F, 2.2F, 0x0a0000, 0x00008b);
	public static final RegistryObject<EntityType<AlphaYeti>> ALPHA_YETI = make(TFEntityNames.ALPHA_YETI, AlphaYeti::new, MobCategory.MONSTER, 3.8F, 5.0F, true, 0xcdcdcd, 0x29486e);
	public static final RegistryObject<EntityType<ArmoredGiant>> ARMORED_GIANT = make(TFEntityNames.ARMORED_GIANT, ArmoredGiant::new, MobCategory.MONSTER, 2.4F, 7.2F, 0x239391, 0x9a9a9a);
	public static final RegistryObject<EntityType<Bighorn>> BIGHORN_SHEEP = make(TFEntityNames.BIGHORN_SHEEP, Bighorn::new, MobCategory.CREATURE, 0.9F, 1.3F, 0xdbceaf, 0xd7c771);
	public static final RegistryObject<EntityType<BlockChainGoblin>> BLOCKCHAIN_GOBLIN = make(TFEntityNames.BLOCKCHAIN_GOBLIN, BlockChainGoblin::new, MobCategory.MONSTER, 0.9F, 1.4F, 0xd3e7bc, 0x1f3fff);
	public static final RegistryObject<EntityType<Boar>> BOAR = make(TFEntityNames.BOAR, Boar::new, MobCategory.CREATURE, 0.9F, 0.9F, 0x83653b, 0xffefca);

	//public static final RegistryObject<EntityType<Boggard>> BOGGARD = make(TFEntityNames.BOGGARD, Boggard::new, MobCategory.MONSTER, 0.8F, 1.1F);
	public static final RegistryObject<EntityType<TowerBroodling>> CARMINITE_BROODLING = make(TFEntityNames.CARMINITE_BROODLING, TowerBroodling::new, MobCategory.MONSTER, 0.7F, 0.5F, 0x343c14, 0xbaee02);
	public static final RegistryObject<EntityType<CarminiteGhastguard>> CARMINITE_GHASTGUARD = make(TFEntityNames.CARMINITE_GHASTGUARD, CarminiteGhastguard::new, MobCategory.MONSTER, 4.0F, 6.0F, 0xbcbcbc, 0xb77878);
	public static final RegistryObject<EntityType<CarminiteGhastling>> CARMINITE_GHASTLING = make(TFEntityNames.CARMINITE_GHASTLING, CarminiteGhastling::new, MobCategory.MONSTER, 1.1F, 1.5F, 0xbcbcbc, 0xa74343);
	public static final RegistryObject<EntityType<CarminiteGolem>> CARMINITE_GOLEM = make(TFEntityNames.CARMINITE_GOLEM, CarminiteGolem::new, MobCategory.MONSTER, 1.4F, 2.9F, 0x6b3d20, 0xe2ddda);
	//public static final RegistryObject<EntityType<CastleGuardian>> CASTLE_GUARDIAN = make(TFEntityNames.CASTLE_GUARDIAN, CastleGuardian::new, MobCategory.MONSTER, 1.8F, 2.4F);
	public static final RegistryObject<EntityType<ChainBlock>> CHAIN_BLOCK = build(TFEntityNames.CHAIN_BLOCK, makeCastedBuilder(ChainBlock.class, ChainBlock::new, 0.6F, 0.6F, 80, 1), true);
	public static final RegistryObject<EntityType<CharmEffect>> CHARM_EFFECT = make(TFEntityNames.CHARM_EFFECT, CharmEffect::new, MobCategory.MISC, 0.25F, 0.25F, 0, 0);
	public static final RegistryObject<EntityType<CicadaShot>> CICADA_SHOT = build(TFEntityNames.CICADA_SHOT, makeCastedBuilder(CicadaShot.class, CicadaShot::new, 0.25F, 0.25F, 150, 3), false);
	public static final RegistryObject<EntityType<CubeOfAnnihilation>> CUBE_OF_ANNIHILATION = build(TFEntityNames.CUBE_OF_ANNIHILATION, makeCastedBuilder(CubeOfAnnihilation.class, CubeOfAnnihilation::new, 1.0F, 1.0F, 80, 1), true);
	public static final RegistryObject<EntityType<DeathTome>> DEATH_TOME = make(TFEntityNames.DEATH_TOME, DeathTome::new, MobCategory.MONSTER, 0.6F, 1.8F, 0x774e22, 0xdbcdbe);
	public static final RegistryObject<EntityType<Deer>> DEER = make(TFEntityNames.DEER, Deer::new, MobCategory.CREATURE, 0.7F, 1.8F, 0x7b4d2e, 0x4b241d);
	public static final RegistryObject<EntityType<DwarfRabbit>> DWARF_RABBIT = make(TFEntityNames.DWARF_RABBIT, DwarfRabbit::new, MobCategory.CREATURE, 0.4F, 0.4F, 0xfefeee, 0xccaa99);
	public static final RegistryObject<EntityType<FallingIce>> FALLING_ICE = make(TFEntityNames.FALLING_ICE, FallingIce::new, MobCategory.MISC, 2.98F, 2.98F, true,0, 0);
	public static final RegistryObject<EntityType<FireBeetle>> FIRE_BEETLE = make(TFEntityNames.FIRE_BEETLE, FireBeetle::new, MobCategory.MONSTER, 1.1F, 0.5F, true, 0x1d0b00, 0xcb6f25);
	public static final RegistryObject<EntityType<GiantMiner>> GIANT_MINER = make(TFEntityNames.GIANT_MINER, GiantMiner::new, MobCategory.MONSTER, 2.4F, 7.2F, 0x211b52, 0x9a9a9a);
	public static final RegistryObject<EntityType<HarbingerCube>> HARBINGER_CUBE = make(TFEntityNames.HARBINGER_CUBE, HarbingerCube::new, MobCategory.MONSTER, 1.9F, 2.4F, true, 0x00000a, 0x8b0000);
	public static final RegistryObject<EntityType<HedgeSpider>> HEDGE_SPIDER = make(TFEntityNames.HEDGE_SPIDER, HedgeSpider::new, MobCategory.MONSTER, 1.4F, 0.9F, 0x235f13, 0x562653);
	public static final RegistryObject<EntityType<HelmetCrab>> HELMET_CRAB = make(TFEntityNames.HELMET_CRAB, HelmetCrab::new, MobCategory.MONSTER, 0.8F, 1.1F, 0xfb904b, 0xd3e7bc);
	public static final RegistryObject<EntityType<HostileWolf>> HOSTILE_WOLF = make(TFEntityNames.HOSTILE_WOLF, HostileWolf::new, MobCategory.MONSTER, 0.6F, 0.85F, 0xd7d3d3, 0xab1e14);
	public static final RegistryObject<EntityType<Hydra>> HYDRA = make(TFEntityNames.HYDRA, Hydra::new, MobCategory.MONSTER, 16.0F, 12.0F, true, 0x142940, 0x29806b);
	public static final RegistryObject<EntityType<HydraMortarHead>> HYDRA_MORTAR = build(TFEntityNames.HYDRA_MORTAR, makeCastedBuilder(HydraMortarHead.class, HydraMortarHead::new, 0.75F, 0.75F, 150, 3), true);
	public static final RegistryObject<EntityType<IceArrow>> ICE_ARROW = build(TFEntityNames.ICE_ARROW, makeCastedBuilder(IceArrow.class, IceArrow::new, 0.5F, 0.5F, 150, 1), false);
	public static final RegistryObject<EntityType<IceBomb>> THROWN_ICE = build(TFEntityNames.THROWN_ICE, makeCastedBuilder(IceBomb.class, IceBomb::new, 1.0F, 1.0F, 80, 2), true);
	public static final RegistryObject<EntityType<IceCrystal>> ICE_CRYSTAL = make(TFEntityNames.ICE_CRYSTAL, IceCrystal::new, MobCategory.MONSTER, 0.6F, 1.8F, 0xdce9fe, 0xadcafb);
	public static final RegistryObject<EntityType<IceSnowball>> ICE_SNOWBALL = build(TFEntityNames.ICE_SNOWBALL, makeCastedBuilder(IceSnowball.class, IceSnowball::new, 0.25F, 0.25F, 150, 3), false);
	public static final RegistryObject<EntityType<KingSpider>> KING_SPIDER = make(TFEntityNames.KING_SPIDER, KingSpider::new, MobCategory.MONSTER, 1.6F, 1.6F, 0x2c1a0e, 0xffc017);
	public static final RegistryObject<EntityType<KnightPhantom>> KNIGHT_PHANTOM = make(TFEntityNames.KNIGHT_PHANTOM, KnightPhantom::new, MobCategory.MONSTER, 1.5F, 3.0F, 0xa6673b, 0xd3e7bc);
	public static final RegistryObject<EntityType<Kobold>> KOBOLD = make(TFEntityNames.KOBOLD, Kobold::new, MobCategory.MONSTER, 0.8F, 1.1F, 0x372096, 0x895d1b);
	public static final RegistryObject<EntityType<Lich>> LICH = make(TFEntityNames.LICH, Lich::new, MobCategory.MONSTER, 1.1F, 2.1F, true, 0xaca489, 0x360472);
	public static final RegistryObject<EntityType<LichBolt>> LICH_BOLT = build(TFEntityNames.LICH_BOLT, makeCastedBuilder(LichBolt.class, LichBolt::new, 0.25F, 0.25F, 150, 2), false);
	public static final RegistryObject<EntityType<LichBomb>> LICH_BOMB = build(TFEntityNames.LICH_BOMB, makeCastedBuilder(LichBomb.class, LichBomb::new, 0.25F, 0.25F, 150, 3), false);
	public static final RegistryObject<EntityType<LichMinion>> LICH_MINION = make(TFEntityNames.LICH_MINION, LichMinion::new, MobCategory.MONSTER, 0.6F, 1.95F, 0, 0);
	public static final RegistryObject<EntityType<LowerGoblinKnight>> LOWER_GOBLIN_KNIGHT = make(TFEntityNames.LOWER_GOBLIN_KNIGHT, LowerGoblinKnight::new, MobCategory.MONSTER, 0.7F, 1.1F, 0x566055, 0xd3e7bc);
	public static final RegistryObject<EntityType<LoyalZombie>> LOYAL_ZOMBIE = make(TFEntityNames.LOYAL_ZOMBIE, LoyalZombie::new, MobCategory.MONSTER, 0.6F, 1.8F, 0, 0);
	public static final RegistryObject<EntityType<MazeSlime>> MAZE_SLIME = make(TFEntityNames.MAZE_SLIME, MazeSlime::new, MobCategory.MONSTER, 2.04F, 2.04F, 0xa3a3a3, 0x2a3b17);
	public static final RegistryObject<EntityType<Minoshroom>> MINOSHROOM = make(TFEntityNames.MINOSHROOM, Minoshroom::new, MobCategory.MONSTER, 1.49F, 2.5F, 0xa81012, 0xaa7d66);
	public static final RegistryObject<EntityType<Minotaur>> MINOTAUR = make(TFEntityNames.MINOTAUR, Minotaur::new, MobCategory.MONSTER, 0.6F, 2.1F, 0x3f3024, 0xaa7d66);
	public static final RegistryObject<EntityType<MistWolf>> MIST_WOLF = make(TFEntityNames.MIST_WOLF, MistWolf::new, MobCategory.MONSTER, 1.4F, 1.9F, 0x3a1411, 0xe2c88a);
	public static final RegistryObject<EntityType<MoonwormShot>> MOONWORM_SHOT = build(TFEntityNames.MOONWORM_SHOT, makeCastedBuilder(MoonwormShot.class, MoonwormShot::new, 0.25F, 0.25F, 150, 3), false);
	public static final RegistryObject<EntityType<MosquitoSwarm>> MOSQUITO_SWARM = make(TFEntityNames.MOSQUITO_SWARM, MosquitoSwarm::new, MobCategory.MONSTER, 0.7F, 1.9F, 0x080904, 0x2d2f21);
	public static final RegistryObject<EntityType<Naga>> NAGA = make(TFEntityNames.NAGA, Naga::new, MobCategory.MONSTER, 1.75f, 3.0f, true, 0xa4d316, 0x1b380b);
	public static final RegistryObject<EntityType<NatureBolt>> NATURE_BOLT = build(TFEntityNames.NATURE_BOLT, makeCastedBuilder(NatureBolt.class, NatureBolt::new, 0.25F, 0.25F, 150, 5), false);
	public static final RegistryObject<EntityType<Penguin>> PENGUIN = make(TFEntityNames.PENGUIN, Penguin::new, MobCategory.CREATURE, 0.5F, 0.9F, 0x12151b, 0xf9edd2);
	public static final RegistryObject<EntityType<PinchBeetle>> PINCH_BEETLE = make(TFEntityNames.PINCH_BEETLE, PinchBeetle::new, MobCategory.MONSTER, 1.2F, 0.5F, 0xbc9327, 0x241609);
	public static final RegistryObject<EntityType<PlateauBoss>> PLATEAU_BOSS = make(TFEntityNames.PLATEAU_BOSS, PlateauBoss::new, MobCategory.MONSTER, 1F, 1F, true, 0, 0);
	public static final RegistryObject<EntityType<ProtectionBox>> PROTECTION_BOX = build(TFEntityNames.PROTECTION_BOX, makeCastedBuilder(ProtectionBox.class, ProtectionBox::new, 0, 0, 80, 3).noSave().noSummon(), true);
	public static final RegistryObject<EntityType<QuestRam>> QUEST_RAM = make(TFEntityNames.QUEST_RAM, QuestRam::new, MobCategory.CREATURE, 1.25F, 2.9F, 0xfefeee, 0x33aadd);
	public static final RegistryObject<EntityType<Raven>> RAVEN = make(TFEntityNames.RAVEN, Raven::new, MobCategory.CREATURE, 0.3F, 0.5F, 0x000011, 0x222233);
	public static final RegistryObject<EntityType<Redcap>> REDCAP = make(TFEntityNames.REDCAP, Redcap::new, MobCategory.MONSTER, 0.9F, 1.4F, 0x3b3a6c, 0xab1e14);
	public static final RegistryObject<EntityType<RedcapSapper>> REDCAP_SAPPER = make(TFEntityNames.REDCAP_SAPPER, RedcapSapper::new, MobCategory.MONSTER, 0.9F, 1.4F, 0x575d21, 0xab1e14);
	public static final RegistryObject<EntityType<RisingZombie>> RISING_ZOMBIE = make(TFEntityNames.RISING_ZOMBIE, RisingZombie::new, MobCategory.MONSTER, 0.6F, 1.95F, 0, 0);
	public static final RegistryObject<EntityType<RovingCube>> ROVING_CUBE = make(TFEntityNames.ROVING_CUBE, RovingCube::new, MobCategory.MONSTER, 1.2F, 2.1F, 0x0a0000, 0x00009b);
	public static final RegistryObject<EntityType<SeekerArrow>> SEEKER_ARROW = build(TFEntityNames.SEEKER_ARROW, makeCastedBuilder(SeekerArrow.class, SeekerArrow::new, 0.5F, 0.5F, 150, 1), false);
	public static final RegistryObject<EntityType<SkeletonDruid>> SKELETON_DRUID = make(TFEntityNames.SKELETON_DRUID, SkeletonDruid::new, MobCategory.MONSTER, 0.6F, 1.99F, 0xa3a3a3, 0x2a3b17);

	public static final RegistryObject<EntityType<SlideBlock>> SLIDER = build(TFEntityNames.SLIDER, makeCastedBuilder(SlideBlock.class, SlideBlock::new, 0.98F, 0.98F, 80, 1), false);
	public static final RegistryObject<EntityType<SlimeBeetle>> SLIME_BEETLE = make(TFEntityNames.SLIME_BEETLE, SlimeBeetle::new, MobCategory.MONSTER, 0.9F, 0.5F, 0x0c1606, 0x60a74c);
	public static final RegistryObject<EntityType<SlimeProjectile>> SLIME_BLOB = build(TFEntityNames.SLIME_BLOB, makeCastedBuilder(SlimeProjectile.class, SlimeProjectile::new, 0.25F, 0.25F, 150, 3), false);
	public static final RegistryObject<EntityType<SnowGuardian>> SNOW_GUARDIAN = make(TFEntityNames.SNOW_GUARDIAN, SnowGuardian::new, MobCategory.MONSTER, 0.6F, 1.8F, 0xd3e7bc, 0xfefefe);
	public static final RegistryObject<EntityType<SnowQueen>> SNOW_QUEEN = make(TFEntityNames.SNOW_QUEEN, SnowQueen::new, MobCategory.MONSTER, 0.7F, 2.2F, true, 0xb1b2d4, 0x87006e);
	public static final RegistryObject<EntityType<Squirrel>> SQUIRREL = make(TFEntityNames.SQUIRREL, Squirrel::new, MobCategory.CREATURE, 0.3F, 0.5F, 0x904f12, 0xeeeeee);
	public static final RegistryObject<EntityType<StableIceCore>> STABLE_ICE_CORE = make(TFEntityNames.STABLE_ICE_CORE, StableIceCore::new, MobCategory.MONSTER, 0.8F, 1.8F, 0xa1bff3, 0x7000f8);
	public static final RegistryObject<EntityType<SwarmSpider>> SWARM_SPIDER = make(TFEntityNames.SWARM_SPIDER, SwarmSpider::new, MobCategory.MONSTER, 0.8F, 0.4F, 0x32022e, 0x17251e);
	public static final RegistryObject<EntityType<ThrownBlock>> THROWN_BLOCK = build(TFEntityNames.THROWN_BLOCK, makeCastedBuilder(ThrownBlock.class, ThrownBlock::new, 1.0F, 1.0F, 80, 2), true);
	public static final RegistryObject<EntityType<ThrownWep>> THROWN_WEP = make(TFEntityNames.THROWN_WEP, ThrownWep::new, MobCategory.MISC, 0.5F, 0.5F, 0, 0);
	public static final RegistryObject<EntityType<TinyBird>> TINY_BIRD = make(TFEntityNames.TINY_BIRD, TinyBird::new, MobCategory.CREATURE, 0.3F, 0.3F, 0x33aadd, 0x1188ee);
	public static final RegistryObject<EntityType<TomeBolt>> TOME_BOLT = build(TFEntityNames.TOME_BOLT, makeCastedBuilder(TomeBolt.class, TomeBolt::new, 0.25F, 0.25F, 150, 5), false);
	public static final RegistryObject<EntityType<TowerwoodBorer>> TOWERWOOD_BORER = make(TFEntityNames.TOWERWOOD_BORER, TowerwoodBorer::new, MobCategory.MONSTER, 0.4F, 0.3F, 0x5d2b21, 0xaca03a);
	public static final RegistryObject<EntityType<Troll>> TROLL = make(TFEntityNames.TROLL, Troll::new, MobCategory.MONSTER, 1.4F, 2.4F, 0x9ea98f, 0xb0948e);
	public static final RegistryObject<EntityType<TwilightWandBolt>> WAND_BOLT = build(TFEntityNames.WAND_BOLT, makeCastedBuilder(TwilightWandBolt.class, TwilightWandBolt::new, 0.25F, 0.25F, 150, 5), false);
	public static final RegistryObject<EntityType<UnstableIceCore>> UNSTABLE_ICE_CORE = make(TFEntityNames.UNSTABLE_ICE_CORE, UnstableIceCore::new, MobCategory.MONSTER, 0.8F, 1.8F, 0x9aacf5, 0x9b0fa5);
	public static final RegistryObject<EntityType<UpperGoblinKnight>> UPPER_GOBLIN_KNIGHT = make(TFEntityNames.UPPER_GOBLIN_KNIGHT, UpperGoblinKnight::new, MobCategory.MONSTER, 1.1F, 1.3F, 0, 0);
	public static final RegistryObject<EntityType<UrGhast>> UR_GHAST = make(TFEntityNames.UR_GHAST, UrGhast::new, MobCategory.MONSTER, 14.0F, 18.0F, true, 0xbcbcbc, 0xb77878);
	public static final RegistryObject<EntityType<WinterWolf>> WINTER_WOLF = make(TFEntityNames.WINTER_WOLF, WinterWolf::new, MobCategory.MONSTER, 1.4F, 1.9F, 0xdfe3e5, 0xb2bcca);
	public static final RegistryObject<EntityType<Wraith>> WRAITH = make(TFEntityNames.WRAITH, Wraith::new, MobCategory.MONSTER, 0.6F, 1.8F, true, 0x505050, 0x838383);
	public static final RegistryObject<EntityType<Yeti>> YETI = make(TFEntityNames.YETI, Yeti::new, MobCategory.MONSTER, 1.4F, 2.4F, 0xdedede, 0x4675bb);
	private static <E extends Entity> RegistryObject<EntityType<E>> make(ResourceLocation id, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, int primary, int secondary) {
		return make(id, factory, classification, width, height, false, primary, secondary);
	}

	private static <E extends Entity> RegistryObject<EntityType<E>> make(ResourceLocation id, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, boolean fireproof, int primary, int secondary) {
		return build(id, makeBuilder(factory, classification, width, height, 80, 3), fireproof, primary, secondary);
	}

	private static <E extends Entity> RegistryObject<EntityType<E>> build(ResourceLocation id, EntityType.Builder<E> builder, boolean fireProof) {
		return build(id, builder, fireProof, 0, 0);
	}

	@SuppressWarnings("unchecked")
	private static <E extends Entity> RegistryObject<EntityType<E>> build(ResourceLocation id, EntityType.Builder<E> builder, boolean fireproof, int primary, int secondary) {
		if(fireproof) builder.fireImmune();
		RegistryObject<EntityType<E>> ret = ENTITIES.register(id.getPath(), () -> builder.build(id.toString()));
		if(primary != 0 && secondary != 0) {
			SPAWN_EGGS.register(id.getPath() + "_spawn_egg", () -> new ForgeSpawnEggItem(() -> (EntityType<? extends Mob>) ret.get(), primary, secondary, TFItems.defaultBuilder()));
		}
		return ret;
	}

	private static <E extends Entity> EntityType.Builder<E> makeCastedBuilder(@SuppressWarnings("unused") Class<E> cast, EntityType.EntityFactory<E> factory, float width, float height, int range, int interval) {
		return makeBuilder(factory, MobCategory.MISC, width, height, range, interval);
	}

	private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, int range, int interval) {
		return EntityType.Builder.of(factory, classification).
				sized(width, height).
				setTrackingRange(range).
				setUpdateInterval(interval).
				setShouldReceiveVelocityUpdates(true);
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
		SpawnPlacements.register(BOAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(BIGHORN_SHEEP.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(DEER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(REDCAP.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(SKELETON_DRUID.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SkeletonDruid::skeletonDruidSpawnHandler);
		SpawnPlacements.register(WRAITH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Wraith::getCanSpawnHere);
		SpawnPlacements.register(HYDRA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(LICH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(PENGUIN.get(), ON_ICE, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Penguin::canSpawn);
		SpawnPlacements.register(LICH_MINION.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(LOYAL_ZOMBIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(TINY_BIRD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(SQUIRREL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(DWARF_RABBIT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(RAVEN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(QUEST_RAM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(KOBOLD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(MOSQUITO_SWARM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
		SpawnPlacements.register(DEATH_TOME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(MINOTAUR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(MINOSHROOM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(FIRE_BEETLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(SLIME_BEETLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(PINCH_BEETLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(MIST_WOLF.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HostileWolf::getCanSpawnHere);
		SpawnPlacements.register(CARMINITE_GHASTLING.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CarminiteGhastling::canSpawnHere);
		SpawnPlacements.register(CARMINITE_GOLEM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TOWERWOOD_BORER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(CARMINITE_GHASTGUARD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CarminiteGhastguard::ghastSpawnHandler);
		SpawnPlacements.register(UR_GHAST.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(BLOCKCHAIN_GOBLIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(UPPER_GOBLIN_KNIGHT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(LOWER_GOBLIN_KNIGHT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(HELMET_CRAB.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(KNIGHT_PHANTOM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(NAGA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(SWARM_SPIDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SwarmSpider::getCanSpawnHere);
		SpawnPlacements.register(KING_SPIDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(CARMINITE_BROODLING.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SwarmSpider::getCanSpawnHere);
		SpawnPlacements.register(HEDGE_SPIDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HedgeSpider::canSpawn);
		SpawnPlacements.register(REDCAP_SAPPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(MAZE_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MazeSlime::getCanSpawnHere);
		SpawnPlacements.register(YETI.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Yeti::yetiSnowyForestSpawnHandler);
		SpawnPlacements.register(ALPHA_YETI.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(WINTER_WOLF.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WinterWolf::canSpawnHere);
		SpawnPlacements.register(SNOW_GUARDIAN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(STABLE_ICE_CORE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(UNSTABLE_ICE_CORE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(SNOW_QUEEN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(TROLL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Troll::canSpawn);
		SpawnPlacements.register(GIANT_MINER.get(), CLOUDS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GiantMiner::canSpawn);
		SpawnPlacements.register(ARMORED_GIANT.get(), CLOUDS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GiantMiner::canSpawn);
		SpawnPlacements.register(ICE_CRYSTAL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(HARBINGER_CUBE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
		SpawnPlacements.register(ADHERENT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(ROVING_CUBE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
		SpawnPlacements.register(RISING_ZOMBIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);

		//EntitySpawnPlacementRegistry.register(CASTLE_GUARDIAN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
	}

	@SubscribeEvent
	public static void addEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(BOAR.get(), Pig.createAttributes().build());
		event.put(BIGHORN_SHEEP.get(), Sheep.createAttributes().build());
		event.put(DEER.get(), Deer.registerAttributes().build());
		event.put(REDCAP.get(), Redcap.registerAttributes().build());
		event.put(SWARM_SPIDER.get(), SwarmSpider.registerAttributes().build());
		event.put(NAGA.get(), Naga.registerAttributes().build());
		event.put(SKELETON_DRUID.get(), AbstractSkeleton.createAttributes().build());
		event.put(HOSTILE_WOLF.get(), HostileWolf.registerAttributes().build());
		event.put(WRAITH.get(), Wraith.registerAttributes().build());
		event.put(HEDGE_SPIDER.get(), Spider.createAttributes().build());
		event.put(HYDRA.get(), Hydra.registerAttributes().build());
		event.put(LICH.get(), Lich.registerAttributes().build());
		event.put(PENGUIN.get(), Penguin.registerAttributes().build());
		event.put(LICH_MINION.get(), Zombie.createAttributes().build());
		event.put(LOYAL_ZOMBIE.get(), LoyalZombie.registerAttributes().build());
		event.put(TINY_BIRD.get(), TinyBird.registerAttributes().build());
		event.put(SQUIRREL.get(), Squirrel.registerAttributes().build());
		event.put(DWARF_RABBIT.get(), DwarfRabbit.registerAttributes().build());
		event.put(RAVEN.get(), Raven.registerAttributes().build());
		event.put(QUEST_RAM.get(), QuestRam.registerAttributes().build());
		event.put(KOBOLD.get(), Kobold.registerAttributes().build());
		event.put(MOSQUITO_SWARM.get(), MosquitoSwarm.registerAttributes().build());
		event.put(DEATH_TOME.get(), DeathTome.registerAttributes().build());
		event.put(MINOTAUR.get(), Minotaur.registerAttributes().build());
		event.put(MINOSHROOM.get(), Minoshroom.registerAttributes().build());
		event.put(FIRE_BEETLE.get(), FireBeetle.registerAttributes().build());
		event.put(SLIME_BEETLE.get(), SlimeBeetle.registerAttributes().build());
		event.put(PINCH_BEETLE.get(), PinchBeetle.registerAttributes().build());
		event.put(MAZE_SLIME.get(), MazeSlime.registerAttributes().build());
		event.put(REDCAP_SAPPER.get(), RedcapSapper.registerAttributes().build());
		event.put(MIST_WOLF.get(), MistWolf.registerAttributes().build());
		event.put(KING_SPIDER.get(), KingSpider.registerAttributes().build());
		event.put(CARMINITE_GHASTLING.get(), CarminiteGhastling.registerAttributes().build());
		event.put(CARMINITE_GHASTGUARD.get(), CarminiteGhastguard.registerAttributes().build());
		event.put(CARMINITE_GOLEM.get(), CarminiteGolem.registerAttributes().build());
		event.put(TOWERWOOD_BORER.get(), TowerwoodBorer.registerAttributes().build());
		event.put(CARMINITE_BROODLING.get(), TowerBroodling.registerAttributes().build());
		event.put(UR_GHAST.get(), UrGhast.registerAttributes().build());
		event.put(BLOCKCHAIN_GOBLIN.get(), BlockChainGoblin.registerAttributes().build());
		event.put(UPPER_GOBLIN_KNIGHT.get(), UpperGoblinKnight.registerAttributes().build());
		event.put(LOWER_GOBLIN_KNIGHT.get(), LowerGoblinKnight.registerAttributes().build());
		event.put(HELMET_CRAB.get(), HelmetCrab.registerAttributes().build());
		event.put(KNIGHT_PHANTOM.get(), KnightPhantom.registerAttributes().build());
		event.put(YETI.get(), Yeti.registerAttributes().build());
		event.put(ALPHA_YETI.get(), AlphaYeti.registerAttributes().build());
		event.put(WINTER_WOLF.get(), WinterWolf.registerAttributes().build());
		event.put(SNOW_GUARDIAN.get(), SnowGuardian.registerAttributes().build());
		event.put(STABLE_ICE_CORE.get(), StableIceCore.registerAttributes().build());
		event.put(UNSTABLE_ICE_CORE.get(), UnstableIceCore.registerAttributes().build());
		event.put(SNOW_QUEEN.get(), SnowQueen.registerAttributes().build());
		event.put(TROLL.get(), Troll.registerAttributes().build());
		event.put(GIANT_MINER.get(), GiantMiner.registerAttributes().build());
		event.put(ARMORED_GIANT.get(), GiantMiner.registerAttributes().build());
		event.put(ICE_CRYSTAL.get(), IceCrystal.registerAttributes().build());
		event.put(HARBINGER_CUBE.get(), HarbingerCube.registerAttributes().build());
		event.put(ADHERENT.get(), Adherent.registerAttributes().build());
		event.put(ROVING_CUBE.get(), RovingCube.registerAttributes().build());
		//event.put(CASTLE_GUARDIAN.get(), Mob.createMobAttributes().create());
		event.put(PLATEAU_BOSS.get(), PlateauBoss.registerAttributes().build());

		//event.put(BOGGARD.get(), Boggard.registerAttributes().create());
		event.put(RISING_ZOMBIE.get(), Zombie.createAttributes().build());
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
		BooleanSupplier legacy = () -> Minecraft.getInstance().getResourcePackRepository().getSelectedIds().contains("builtin/twilight_forest_legacy_resources");
		event.registerEntityRenderer(BOAR.get(), m -> legacy.getAsBoolean() ? new LegacyBoarRenderer(m, new BoarLegacyModel<>(m.bakeLayer(TFModelLayers.LEGACY_BOAR))) : new BoarRenderer(m, new BoarModel<>(m.bakeLayer(TFModelLayers.BOAR))));
		event.registerEntityRenderer(BIGHORN_SHEEP.get(), m -> new BighornRenderer(m, new BighornModel<>(m.bakeLayer(legacy.getAsBoolean() ? TFModelLayers.LEGACY_BIGHORN_SHEEP : TFModelLayers.BIGHORN_SHEEP)), new BighornFurLayer(m.bakeLayer(TFModelLayers.BIGHORN_SHEEP_FUR)), 0.7F));
		event.registerEntityRenderer(DEER.get(), m -> new TFGenericMobRenderer<>(m, legacy.getAsBoolean() ? new DeerLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_DEER)) : new DeerModel(m.bakeLayer(TFModelLayers.DEER)), 0.7F, "wilddeer.png"));
		event.registerEntityRenderer(REDCAP.get(), m -> new TFBipedRenderer<>(m, legacy.getAsBoolean() ? new RedcapLegacyModel<>(m.bakeLayer(TFModelLayers.LEGACY_REDCAP)) : new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcap.png"));
		event.registerEntityRenderer(SKELETON_DRUID.get(), m -> new TFBipedRenderer<>(m, new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID)), new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID_INNER_ARMOR)), new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID_OUTER_ARMOR)), 0.5F, "skeletondruid.png"));
		event.registerEntityRenderer(HOSTILE_WOLF.get(), HostileWolfRenderer::new);
		event.registerEntityRenderer(WRAITH.get(), m -> new WraithRenderer(m, new WraithModel(m.bakeLayer(TFModelLayers.WRAITH)), 0.5F));
		event.registerEntityRenderer(HYDRA.get(), m -> legacy.getAsBoolean() ? new LegacyHydraRenderer(m, new HydraLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_HYDRA)), 4.0F) : new HydraRenderer(m, new HydraModel(m.bakeLayer(TFModelLayers.HYDRA)), 4.0F));
		event.registerEntityRenderer(LICH.get(), m -> new LichRenderer(m, new LichModel(m.bakeLayer(TFModelLayers.LICH)), 0.6F));
		event.registerEntityRenderer(PENGUIN.get(), m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), 0.375F, "penguin.png"));
		event.registerEntityRenderer(LICH_MINION.get(), m -> new TFBipedRenderer<>(m, new LichMinionModel(m.bakeLayer(TFModelLayers.LICH_MINION)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		event.registerEntityRenderer(LOYAL_ZOMBIE.get(), m -> new TFBipedRenderer<>(m, new LoyalZombieModel(m.bakeLayer(TFModelLayers.LOYAL_ZOMBIE)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		event.registerEntityRenderer(TINY_BIRD.get(), m -> legacy.getAsBoolean() ? new LegacyTinyBirdRenderer(m, new TinyBirdLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_TINY_BIRD)), 0.3F) :  new TinyBirdRenderer(m, new TinyBirdModel(m.bakeLayer(TFModelLayers.TINY_BIRD)), 0.3F));
		event.registerEntityRenderer(SQUIRREL.get(), m -> new TFGenericMobRenderer<>(m, legacy.getAsBoolean() ? new SquirrelLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_SQUIRREL)) : new SquirrelModel(m.bakeLayer(TFModelLayers.SQUIRREL)), 0.3F, "squirrel2.png"));
		event.registerEntityRenderer(DWARF_RABBIT.get(), m -> new BunnyRenderer(m, new BunnyModel(m.bakeLayer(TFModelLayers.BUNNY)), 0.3F));
		event.registerEntityRenderer(RAVEN.get(), m -> new BirdRenderer<>(m, legacy.getAsBoolean() ? new RavenLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_RAVEN)) : new RavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png"));
		event.registerEntityRenderer(QUEST_RAM.get(), m -> legacy.getAsBoolean() ? new LegacyQuestRamRenderer(m, new QuestRamLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_QUEST_RAM))) : new QuestRamRenderer(m, new QuestRamModel(m.bakeLayer(TFModelLayers.QUEST_RAM))));
		event.registerEntityRenderer(KOBOLD.get(), m -> legacy.getAsBoolean() ? new LegacyKoboldRenderer(m, new KoboldLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_KOBOLD)), 0.4F, "kobold.png") : new KoboldRenderer(m, new KoboldModel(m.bakeLayer(TFModelLayers.KOBOLD)), 0.4F, "kobold.png"));
		//event.registerEntityRenderer(BOGGARD.get(), m -> new RenderTFBiped<>(m, new BipedModel<>(0), 0.625F, "kobold.png"));
		event.registerEntityRenderer(MOSQUITO_SWARM.get(), m -> new TFGenericMobRenderer<>(m, new MosquitoSwarmModel(m.bakeLayer(TFModelLayers.MOSQUITO_SWARM)), 0.0F, "mosquitoswarm.png"));
		event.registerEntityRenderer(DEATH_TOME.get(), m -> new TFGenericMobRenderer<>(m, new DeathTomeModel(m.bakeLayer(TFModelLayers.DEATH_TOME)), 0.3F, "textures/entity/enchanting_table_book.png"));
		event.registerEntityRenderer(MINOTAUR.get(), m -> new TFBipedRenderer<>(m, legacy.getAsBoolean() ? new MinotaurLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_MINOTAUR)) : new MinotaurModel(m.bakeLayer(TFModelLayers.MINOTAUR)), 0.625F, "minotaur.png"));
		event.registerEntityRenderer(MINOSHROOM.get(), m -> legacy.getAsBoolean() ? new LegacyMinoshroomRenderer(m, new MinoshroomLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_MINOSHROOM)), 0.625F) : new MinoshroomRenderer(m, new MinoshroomModel(m.bakeLayer(TFModelLayers.MINOSHROOM)), 0.625F));
		event.registerEntityRenderer(FIRE_BEETLE.get(), m -> new TFGenericMobRenderer<>(m, legacy.getAsBoolean() ? new FireBeetleLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_FIRE_BEETLE)) : new FireBeetleModel(m.bakeLayer(TFModelLayers.FIRE_BEETLE)), 0.8F, "firebeetle.png"));
		event.registerEntityRenderer(SLIME_BEETLE.get(), m -> legacy.getAsBoolean() ? new LegacySlimeBeetleRenderer(m, new SlimeBeetleLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_SLIME_BEETLE)), 0.6F) : new SlimeBeetleRenderer(m, new SlimeBeetleModel(m.bakeLayer(TFModelLayers.SLIME_BEETLE)), 0.6F));
		event.registerEntityRenderer(PINCH_BEETLE.get(), m -> new TFGenericMobRenderer<>(m, legacy.getAsBoolean() ? new PinchBeetleLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_PINCH_BEETLE)) : new PinchBeetleModel(m.bakeLayer(TFModelLayers.PINCH_BEETLE)), 0.6F, "pinchbeetle.png"));
		event.registerEntityRenderer(MIST_WOLF.get(), MistWolfRenderer::new);
		event.registerEntityRenderer(CARMINITE_GHASTLING.get(), m -> new TFGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
		event.registerEntityRenderer(CARMINITE_GOLEM.get(), m -> new CarminiteGolemRenderer<>(m, new CarminiteGolemModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GOLEM)), 0.75F));
		event.registerEntityRenderer(TOWERWOOD_BORER.get(), m -> new TFGenericMobRenderer<>(m, new SilverfishModel<>(m.bakeLayer(ModelLayers.SILVERFISH)), 0.3F, "towertermite.png"));
		event.registerEntityRenderer(CARMINITE_GHASTGUARD.get(), m -> new CarminiteGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTGUARD)), 3.0F));
		event.registerEntityRenderer(UR_GHAST.get(), m -> legacy.getAsBoolean() ? new LegacyUrGhastRenderer(m, new UrGhastLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_UR_GHAST)), 8.0F, 24F) : new UrGhastRenderer(m, new UrGhastModel(m.bakeLayer(TFModelLayers.UR_GHAST)), 8.0F, 24F));
		event.registerEntityRenderer(BLOCKCHAIN_GOBLIN.get(), m -> legacy.getAsBoolean() ? new LegacyBlockChainGoblinRenderer<>(m, new BlockChainGoblinLegacyModel<>(m.bakeLayer(TFModelLayers.LEGACY_BLOCKCHAIN_GOBLIN)), 0.4F) : new BlockChainGoblinRenderer<>(m, new BlockChainGoblinModel<>(m.bakeLayer(TFModelLayers.BLOCKCHAIN_GOBLIN)), 0.4F));
		event.registerEntityRenderer(UPPER_GOBLIN_KNIGHT.get(), m -> legacy.getAsBoolean() ? new LegacyUpperGoblinKnightRenderer(m, new UpperGoblinKnightLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_UPPER_GOBLIN_KNIGHT)), 0.625F) : new UpperGoblinKnightRenderer(m, new UpperGoblinKnightModel(m.bakeLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT)), 0.625F));
		event.registerEntityRenderer(LOWER_GOBLIN_KNIGHT.get(), m -> new TFBipedRenderer<>(m, legacy.getAsBoolean() ? new LowerGoblinKnightLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_LOWER_GOBLIN_KNIGHT)) : new LowerGoblinKnightModel(m.bakeLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT)), 0.625F, "doublegoblin.png"));
		event.registerEntityRenderer(HELMET_CRAB.get(), m -> new TFGenericMobRenderer<>(m, legacy.getAsBoolean() ? new HelmetCrabLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_HELMET_CRAB)) : new HelmetCrabModel(m.bakeLayer(TFModelLayers.HELMET_CRAB)), 0.625F, "helmetcrab.png"));
		event.registerEntityRenderer(KNIGHT_PHANTOM.get(), m -> new KnightPhantomRenderer(m, new KnightPhantomModel(m.bakeLayer(TFModelLayers.KNIGHT_PHANTOM)), 0.625F));
		event.registerEntityRenderer(NAGA.get(), m -> legacy.getAsBoolean() ? new LegacyNagaRenderer<>(m, new NagaLegacyModel<>(m.bakeLayer(TFModelLayers.LEGACY_NAGA)), 1.45F) : new NagaRenderer<>(m, new NagaModel<>(m.bakeLayer(TFModelLayers.NAGA)), 1.45F));
		event.registerEntityRenderer(SWARM_SPIDER.get(), SwarmSpiderRenderer::new);
		event.registerEntityRenderer(KING_SPIDER.get(), KingSpiderRenderer::new);
		event.registerEntityRenderer(CARMINITE_BROODLING.get(), CarminiteBroodlingRenderer::new);
		event.registerEntityRenderer(HEDGE_SPIDER.get(), HedgeSpiderRenderer::new);
		event.registerEntityRenderer(REDCAP_SAPPER.get(), m -> new TFBipedRenderer<>(m, legacy.getAsBoolean() ? new RedcapLegacyModel<>(m.bakeLayer(TFModelLayers.LEGACY_REDCAP)) : new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcapsapper.png"));
		event.registerEntityRenderer(MAZE_SLIME.get(), m -> new MazeSlimeRenderer(m, 0.625F));
		event.registerEntityRenderer(YETI.get(), m -> new TFBipedRenderer<>(m, new YetiModel<>(m.bakeLayer(TFModelLayers.YETI)), 0.625F, "yeti2.png"));
		event.registerEntityRenderer(PROTECTION_BOX.get(), ProtectionBoxRenderer::new);
		event.registerEntityRenderer(ALPHA_YETI.get(), m -> new TFBipedRenderer<>(m, new AlphaYetiModel(m.bakeLayer(TFModelLayers.ALPHA_YETI)), 1.75F, "yetialpha.png"));
		event.registerEntityRenderer(WINTER_WOLF.get(), WinterWolfRenderer::new);
		event.registerEntityRenderer(SNOW_GUARDIAN.get(), m -> new SnowGuardianRenderer(m, new NoopModel<>(m.bakeLayer(TFModelLayers.NOOP))));
		event.registerEntityRenderer(STABLE_ICE_CORE.get(), m -> new StableIceCoreRenderer(m, new StableIceCoreModel(m.bakeLayer(TFModelLayers.STABLE_ICE_CORE))));
		event.registerEntityRenderer(UNSTABLE_ICE_CORE.get(), m -> new UnstableIceCoreRenderer<>(m, new UnstableIceCoreModel<>(m.bakeLayer(TFModelLayers.UNSTABLE_ICE_CORE))));
		event.registerEntityRenderer(SNOW_QUEEN.get(), m -> legacy.getAsBoolean() ? new LegacySnowQueenRenderer(m, new SnowQueenLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_SNOW_QUEEN))) : new SnowQueenRenderer(m, new SnowQueenModel(m.bakeLayer(TFModelLayers.SNOW_QUEEN))));
		event.registerEntityRenderer(TROLL.get(), m -> new TFBipedRenderer<>(m, legacy.getAsBoolean() ? new TrollLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_TROLL)) : new TrollModel(m.bakeLayer(TFModelLayers.TROLL)), 0.625F, "troll.png"));
		event.registerEntityRenderer(GIANT_MINER.get(), TFGiantRenderer::new);
		event.registerEntityRenderer(ARMORED_GIANT.get(), TFGiantRenderer::new);
		event.registerEntityRenderer(ICE_CRYSTAL.get(), IceCrystalRenderer::new);
		event.registerEntityRenderer(CHAIN_BLOCK.get(), BlockChainRenderer::new);
		event.registerEntityRenderer(CUBE_OF_ANNIHILATION.get(), CubeOfAnnihilationRenderer::new);
		event.registerEntityRenderer(HARBINGER_CUBE.get(), HarbingerCubeRenderer::new);
		event.registerEntityRenderer(ADHERENT.get(), AdherentRenderer::new);
		event.registerEntityRenderer(ROVING_CUBE.get(), RovingCubeRenderer::new);
		event.registerEntityRenderer(RISING_ZOMBIE.get(), m -> new TFBipedRenderer<>(m, new RisingZombieModel(m.bakeLayer(TFModelLayers.RISING_ZOMBIE)), new RisingZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new RisingZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		//event.registerEntityRenderer(CASTLE_GUARDIAN.get(), m -> new RenderTFCastleGuardian(m, new ModelTFCastleGuardian(), 2.0F, "finalcastle/castle_guardian.png"));
		event.registerEntityRenderer(PLATEAU_BOSS.get(), NoopRenderer::new);

		// projectiles
		event.registerEntityRenderer(NATURE_BOLT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(LICH_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/items/twilight_orb.png")));
		event.registerEntityRenderer(WAND_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/items/twilight_orb.png")));
		event.registerEntityRenderer(TOME_BOLT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(HYDRA_MORTAR.get(), HydraMortarRenderer::new);
		event.registerEntityRenderer(SLIME_BLOB.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(CICADA_SHOT.get(), CicadaShotRenderer::new);
		event.registerEntityRenderer(MOONWORM_SHOT.get(), MoonwormShotRenderer::new);
		event.registerEntityRenderer(CHARM_EFFECT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(LICH_BOMB.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(THROWN_WEP.get(), ThrownWepRenderer::new);
		event.registerEntityRenderer(FALLING_ICE.get(), FallingIceRenderer::new);
		event.registerEntityRenderer(THROWN_ICE.get(), ThrownIceRenderer::new);
		event.registerEntityRenderer(THROWN_BLOCK.get(), ThrownBlockRenderer::new);
		event.registerEntityRenderer(ICE_SNOWBALL.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(SLIDER.get(), SlideBlockRenderer::new);
		event.registerEntityRenderer(SEEKER_ARROW.get(), DefaultArrowRenderer::new);
		event.registerEntityRenderer(ICE_ARROW.get(), DefaultArrowRenderer::new);

		if(ModList.get().isLoaded("undergarden")) {
			UndergardenCompat.registerSlingshotRenders(event);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class BakedMultiPartRenderers {

		private static final Map<ResourceLocation, EntityRenderer<?>> renderers = new HashMap<>();

		public static void bakeMultiPartRenderers(EntityRendererProvider.Context context) {
			BooleanSupplier legacy = () -> Minecraft.getInstance().getResourcePackRepository().getSelectedIds().contains("builtin/twilight_forest_legacy_resources");
			renderers.put(TFPart.RENDERER, new NoopRenderer<>(context));
			renderers.put(HydraHead.RENDERER, legacy.getAsBoolean() ? new LegacyHydraHeadRenderer(context) : new HydraHeadRenderer(context));
			renderers.put(HydraNeck.RENDERER, legacy.getAsBoolean() ? new LegacyHydraNeckRenderer(context) : new HydraNeckRenderer(context));
			renderers.put(SnowQueenIceShield.RENDERER, new SnowQueenIceShieldLayer<>(context));
			renderers.put(NagaSegment.RENDERER, legacy.getAsBoolean() ? new LegacyNagaSegmentRenderer<>(context) : new NagaSegmentRenderer<>(context));
		}

		public static EntityRenderer<?> lookup(ResourceLocation location) {
			return renderers.get(location);
		}

	}
}
