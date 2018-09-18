package twilightforest.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.*;
import twilightforest.entity.finalcastle.EntityTFCastleGuardian;
import twilightforest.entity.passive.*;
import twilightforest.util.TFEntityNames;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFEntities {

	public static final EntityLiving.SpawnPlacementType ON_ICE = EnumHelper.addSpawnPlacementType("TF_ON_ICE", (world, pos) -> {
		Material material = world.getBlockState(pos.down()).getMaterial();
		return material == Material.ICE || material == Material.PACKED_ICE;
	});

	static {
		EntitySpawnPlacementRegistry.setPlacementType(EntityTFPenguin.class, ON_ICE);
	}

	private static class EntityRegistryHelper {

		private final IForgeRegistry<EntityEntry> registry;

		private int id = 0;

		EntityRegistryHelper(IForgeRegistry<EntityEntry> registry) {
			this.registry = registry;
		}

		private static String toString(ResourceLocation registryName) {
			return registryName.getNamespace() + "." + registryName.getPath();
		}

		final <T extends Entity> EntityEntryBuilder<T> builder(ResourceLocation registryName, Class<T> entity, Function<World, T> factory) {
			return EntityEntryBuilder.<T>create().id(registryName, id++).name(toString(registryName)).entity(entity).factory(factory);
		}

		final <T extends Entity> void registerEntity(ResourceLocation registryName, Class<T> entity, Function<World, T> factory, int backgroundEggColour, int foregroundEggColour) {
			registerEntity(registryName, entity, factory, backgroundEggColour, foregroundEggColour, 80, 3, true);
		}

		final <T extends Entity> void registerEntity(ResourceLocation registryName, Class<T> entity, Function<World, T> factory, int backgroundEggColour, int foregroundEggColour, int trackingRange, int updateInterval, boolean sendVelocityUpdates) {
			registry.register(builder(registryName, entity, factory).tracker(trackingRange, updateInterval, sendVelocityUpdates).egg(backgroundEggColour, foregroundEggColour).build());
		}

		final <T extends Entity> void registerEntity(ResourceLocation registryName, Class<T> entity, Function<World, T> factory) {
			registerEntity(registryName, entity, factory, 80, 3, true);
		}

		final <T extends Entity> void registerEntity(ResourceLocation registryName, Class<T> entity, Function<World, T> factory, int trackingRange, int updateInterval, boolean sendVelocityUpdates) {
			registry.register(builder(registryName, entity, factory).tracker(trackingRange, updateInterval, sendVelocityUpdates).build());
		}
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {

		EntityRegistryHelper helper = new EntityRegistryHelper(event.getRegistry());

		helper.registerEntity(TFEntityNames.WILD_BOAR, EntityTFBoar.class, EntityTFBoar::new, 0x83653b, 0xffefca);
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

		helper.registerEntity(TFEntityNames.NATURE_BOLT, EntityTFNatureBolt.class, EntityTFNatureBolt::new, 150, 5, true);
		helper.registerEntity(TFEntityNames.LICH_BOLT, EntityTFLichBolt.class, EntityTFLichBolt::new, 150, 2, true);
		helper.registerEntity(TFEntityNames.WAND_BOLT, EntityTFTwilightWandBolt.class, EntityTFTwilightWandBolt::new, 150, 5, true);
		helper.registerEntity(TFEntityNames.TOME_BOLT, EntityTFTomeBolt.class, EntityTFTomeBolt::new, 150, 5, true);
		helper.registerEntity(TFEntityNames.HYDRA_MORTAR, EntityTFHydraMortar.class, EntityTFHydraMortar::new, 150, 3, true);
		helper.registerEntity(TFEntityNames.LICH_BOMB, EntityTFLichBomb.class, EntityTFLichBomb::new, 150, 3, true);
		helper.registerEntity(TFEntityNames.MOONWORM_SHOT, EntityTFMoonwormShot.class, EntityTFMoonwormShot::new, 150, 3, true);
		helper.registerEntity(TFEntityNames.SLIME_BLOB, EntityTFSlimeProjectile.class, EntityTFSlimeProjectile::new, 150, 3, true);
		helper.registerEntity(TFEntityNames.CHARM_EFFECT, EntityTFCharmEffect.class, EntityTFCharmEffect::new, 80, 3, true);
		helper.registerEntity(TFEntityNames.THROWN_WEP, EntityTFThrownWep.class, EntityTFThrownWep::new, 80, 3, true);
		helper.registerEntity(TFEntityNames.FALLING_ICE, EntityTFFallingIce.class, EntityTFFallingIce::new, 80, 3, true);
		helper.registerEntity(TFEntityNames.THROWN_ICE, EntityTFIceBomb.class, EntityTFIceBomb::new, 80, 2, true);
		helper.registerEntity(TFEntityNames.SEEKER_ARROW, EntitySeekerArrow.class, EntitySeekerArrow::new, 150, 1, true);
		helper.registerEntity(TFEntityNames.ICE_ARROW, EntityIceArrow.class, EntityIceArrow::new, 150, 1, true);
		helper.registerEntity(TFEntityNames.ICE_SNOWBALL, EntityTFIceSnowball.class, EntityTFIceSnowball::new, 150, 3, true);
		helper.registerEntity(TFEntityNames.CHAIN_BLOCK, EntityTFChainBlock.class, EntityTFChainBlock::new, 80, 1, true);
		helper.registerEntity(TFEntityNames.CUBE_OF_ANNIHILATION, EntityTFCubeOfAnnihilation.class, EntityTFCubeOfAnnihilation::new, 80, 1, true);
		helper.registerEntity(TFEntityNames.SLIDER, EntityTFSlideBlock.class, EntityTFSlideBlock::new, 80, 1, true);
		helper.registerEntity(TFEntityNames.BOGGARD, EntityTFBoggard.class, EntityTFBoggard::new);
	}
}
