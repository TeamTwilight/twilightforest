package twilightforest;

import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import twilightforest.entity.TFEntities;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public final class TFSounds {

	public static final SoundEvent KOBOLD_DEATH = createEvent("entity.kobold.die");
	public static final SoundEvent KOBOLD_AMBIENT = createEvent("entity.kobold.ambient");
	public static final SoundEvent KOBOLD_HURT = createEvent("entity.kobold.hurt");
	public static final SoundEvent KOBOLD_PARROT = createEvent("entity.kobold.parrot");
	public static final SoundEvent CICADA = createEvent("entity.cicada");
	public static final SoundEvent NAGA_HISS = createEvent("entity.naga.hiss");
	public static final SoundEvent NAGA_HURT = createEvent("entity.naga.hurt");
	public static final SoundEvent NAGA_RATTLE = createEvent("entity.naga.rattle");
	public static final SoundEvent NAGA_PARROT = createEvent("entity.naga.parrot");
	public static final SoundEvent RAVEN_CAW = createEvent("entity.raven.caw");
	public static final SoundEvent RAVEN_SQUAWK = createEvent("entity.raven.squawk");
	public static final SoundEvent REDCAP_DEATH = createEvent("entity.redcap.die");
	public static final SoundEvent REDCAP_AMBIENT = createEvent("entity.redcap.ambient");
	public static final SoundEvent REDCAP_HURT = createEvent("entity.redcap.hurt");
	public static final SoundEvent REDCAP_PARROT = createEvent("entity.redcap.parrot");
	public static final SoundEvent TINYBIRD_CHIRP = createEvent("entity.tinybird.chirp");
	public static final SoundEvent TINYBIRD_HURT = createEvent("entity.tinybird.hurt");
	public static final SoundEvent TINYBIRD_SONG = createEvent("entity.tinybird.song");
	public static final SoundEvent URGHAST_TRAP_ACTIVE = createEvent("entity.urghast.trapactive");
	public static final SoundEvent URGHAST_TRAP_ON = createEvent("entity.urghast.trapon");
	public static final SoundEvent URGHAST_TRAP_SPINDOWN = createEvent("entity.urghast.trapspindown");
	public static final SoundEvent URGHAST_TRAP_WARMUP = createEvent("entity.urghast.trapwarmup");
	public static final SoundEvent WRAITH = createEvent("entity.wraith.ambient");
	public static final SoundEvent WRAITH_PARROT = createEvent("entity.wraith.parrot");
	public static final SoundEvent HYDRA_HURT = createEvent("entity.hydra.hurt");
	public static final SoundEvent HYDRA_DEATH = createEvent("entity.hydra.death");
	public static final SoundEvent HYDRA_GROWL = createEvent("entity.hydra.growl");
	public static final SoundEvent HYDRA_ROAR = createEvent("entity.hydra.roar");
	public static final SoundEvent HYDRA_WARN = createEvent("entity.hydra.warn");
	public static final SoundEvent HYDRA_PARROT = createEvent("entity.hydra.parrot");
	public static final SoundEvent MOSQUITO = createEvent("entity.mosquito.ambient");
	public static final SoundEvent MOSQUITO_PARROT = createEvent("entity.mosquito.parrot");
	public static final SoundEvent ICE_CORE_AMBIENT = createEvent("entity.ice.noise");
	public static final SoundEvent ICE_CORE_HURT = createEvent("entity.ice.hurt");
	public static final SoundEvent ICE_CORE_DEATH = createEvent("entity.ice.death");
	public static final SoundEvent ICE_CORE_PARROT = createEvent("entity.ice.parrot");
	public static final SoundEvent MINOTAUR_PARROT = createEvent("entity.minotaur.parrot");
	public static final SoundEvent ALPHAYETI_ALERT = createEvent("entity.alphayeti.alert");
	public static final SoundEvent ALPHAYETI_DIE = createEvent("entity.alphayeti.die");
	public static final SoundEvent ALPHAYETI_GRAB = createEvent("entity.alphayeti.grab");
	public static final SoundEvent ALPHAYETI_GROWL = createEvent("entity.alphayeti.growl");
	public static final SoundEvent ALPHAYETI_HURT = createEvent("entity.alphayeti.hurt");
	public static final SoundEvent ALPHAYETI_PANT = createEvent("entity.alphayeti.pant");
	public static final SoundEvent ALPHAYETI_ROAR = createEvent("entity.alphayeti.roar");
	public static final SoundEvent ALPHAYETI_THROW = createEvent("entity.alphayeti.throw");
	public static final SoundEvent ALPHAYETI_PARROT = createEvent("entity.alphayeti.parrot");
	public static final SoundEvent DEER_DEATH = createEvent("entity.deer.death");
	public static final SoundEvent DEER_HURT = createEvent("entity.deer.hurt");
	public static final SoundEvent DEER_IDLE = createEvent("entity.deer.idle");
	public static final SoundEvent MISTWOLF_TARGET = createEvent("entity.mistwolf.target");
	public static final SoundEvent MISTWOLF_HURT = createEvent("entity.mistwolf.hurt");
	public static final SoundEvent MISTWOLF_IDLE = createEvent("entity.mistwolf.idle");
	public static final SoundEvent MISTWOLF_PARROT = createEvent("entity.mistwolf.parrot");
	public static final SoundEvent TOME_DEATH = createEvent("entity.tome.death");
	public static final SoundEvent TOME_HURT = createEvent("entity.tome.hurt");
	public static final SoundEvent TOME_IDLE = createEvent("entity.tome.idle");
	public static final SoundEvent TOME_PARROT = createEvent("entity.tome.parrot");
	public static final SoundEvent BLOCKCHAIN_AMBIENT = createEvent("entity.blockchain.ambient");
	public static final SoundEvent BLOCKCHAIN_HURT = createEvent("entity.blockchain.hurt");
	public static final SoundEvent BLOCKCHAIN_DEATH = createEvent("entity.blockchain.death");
	public static final SoundEvent FIRE_BEETLE_STEP = createEvent("entity.firebeetle.step");
	public static final SoundEvent FIRE_BEETLE_HURT = createEvent("entity.firebeetle.hurt");
	public static final SoundEvent FIRE_BEETLE_DEATH = createEvent("entity.firebeetle.death");
	public static final SoundEvent FIRE_BEETLE_SHOOT = createEvent("entity.firebeetle.shoot");
	public static final SoundEvent HEDGE_SPIDER_STEP = createEvent("entity.hedgespider.step");
	public static final SoundEvent HEDGE_SPIDER_HURT = createEvent("entity.hedgespider.hurt");
	public static final SoundEvent HEDGE_SPIDER_DEATH = createEvent("entity.hedgespider.death");
	public static final SoundEvent HEDGE_SPIDER_AMBIENT = createEvent("entity.hedgespider.ambient");
	public static final SoundEvent HELMET_CRAB_STEP = createEvent("entity.helmetcrab.step");
	public static final SoundEvent HELMET_CRAB_HURT = createEvent("entity.helmetcrab.hurt");
	public static final SoundEvent HELMET_CRAB_DEATH = createEvent("entity.helmetcrab.death");
	public static final SoundEvent HOSTILE_WOLF_TARGET = createEvent("entity.hostilewolf.target");
	public static final SoundEvent HOSTILE_WOLF_HURT = createEvent("entity.hostilewolf.hurt");
	public static final SoundEvent HOSTILE_WOLF_IDLE = createEvent("entity.hostilewolf.idle");
	public static final SoundEvent KING_SPIDER_STEP = createEvent("entity.kingspider.step");
	public static final SoundEvent KING_SPIDER_HURT = createEvent("entity.kingspider.hurt");
	public static final SoundEvent KING_SPIDER_DEATH = createEvent("entity.kingspider.death");
	public static final SoundEvent KING_SPIDER_AMBIENT = createEvent("entity.kingspider.ambient");
	public static final SoundEvent LOYAL_ZOMBIE_STEP = createEvent("entity.loyalzombie.step");
	public static final SoundEvent LOYAL_ZOMBIE_HURT = createEvent("entity.loyalzombie.hurt");
	public static final SoundEvent LOYAL_ZOMBIE_DEATH = createEvent("entity.loyalzombie.death");
	public static final SoundEvent LOYAL_ZOMBIE_AMBIENT = createEvent("entity.loyalzombie.ambient");
	public static final SoundEvent MAZE_SLIME_HURT = createEvent("entity.mazeslime.hurt");
	public static final SoundEvent MAZE_SLIME_DEATH = createEvent("entity.mazeslime.death");
	public static final SoundEvent MAZE_SLIME_SQUISH = createEvent("entity.mazeslime.ambient");
	public static final SoundEvent MAZE_SLIME_HURT_SMALL = createEvent("entity.mazeslimesmall.hurt");
	public static final SoundEvent MAZE_SLIME_DEATH_SMALL = createEvent("entity.mazeslimesmall.death");
	public static final SoundEvent MAZE_SLIME_SQUISH_SMALL = createEvent("entity.mazeslimesmall.ambient");
	public static final SoundEvent GHASTLING_HURT = createEvent("entity.ghastling.hurt");
	public static final SoundEvent GHASTLING_DEATH = createEvent("entity.ghastling.death");
	public static final SoundEvent GHASTLING_AMBIENT = createEvent("entity.ghastling.ambient");
	public static final SoundEvent GHASTGUARD_HURT = createEvent("entity.ghastguard.hurt");
	public static final SoundEvent GHASTGUARD_DEATH = createEvent("entity.ghastguard.death");
	public static final SoundEvent GHASTGUARD_AMBIENT = createEvent("entity.ghastguard.ambient");
	public static final SoundEvent MINOTAUR_STEP = createEvent("entity.minotaur.step");
	public static final SoundEvent MINOTAUR_HURT = createEvent("entity.minotaur.hurt");
	public static final SoundEvent MINOTAUR_DEATH = createEvent("entity.minotaur.death");
	public static final SoundEvent MINOTAUR_AMBIENT = createEvent("entity.minotaur.ambient");
	public static final SoundEvent MINOTAUR_ATTACK = createEvent("entity.minotaur.attack");
	public static final SoundEvent MINOSHROOM_STEP = createEvent("entity.minoshroom.step");
	public static final SoundEvent MINOSHROOM_HURT = createEvent("entity.minoshroom.hurt");
	public static final SoundEvent MINOSHROOM_DEATH = createEvent("entity.minoshroom.death");
	public static final SoundEvent MINOSHROOM_AMBIENT = createEvent("entity.minoshroom.ambient");
	public static final SoundEvent MINOSHROOM_ATTACK = createEvent("entity.minoshroom.attack");
	public static final SoundEvent PINCH_BEETLE_STEP = createEvent("entity.pinchbeetle.step");
	public static final SoundEvent PINCH_BEETLE_HURT = createEvent("entity.pinchbeetle.hurt");
	public static final SoundEvent PINCH_BEETLE_DEATH = createEvent("entity.pinchbeetle.death");
	public static final SoundEvent SKELETON_DRUID_STEP = createEvent("entity.druid.step");
	public static final SoundEvent SKELETON_DRUID_HURT = createEvent("entity.druid.hurt");
	public static final SoundEvent SKELETON_DRUID_DEATH = createEvent("entity.druid.death");
	public static final SoundEvent SKELETON_DRUID_AMBIENT = createEvent("entity.druid.ambient");
	public static final SoundEvent SKELETON_DRUID_SHOOT = createEvent("entity.druid.shoot");
	public static final SoundEvent SLIME_BEETLE_STEP = createEvent("entity.slimebeetle.step");
	public static final SoundEvent SLIME_BEETLE_HURT = createEvent("entity.slimebeetle.hurt");
	public static final SoundEvent SLIME_BEETLE_DEATH = createEvent("entity.slimebeetle.death");
	public static final SoundEvent SLIME_BEETLE_SQUISH_SMALL = createEvent("entity.slimebeetle.squish");
	public static final SoundEvent ICE_GUARDIAN_AMBIENT = createEvent("entity.iceguardian.ambient");
	public static final SoundEvent ICE_GUARDIAN_HURT = createEvent("entity.iceguardian.hurt");
	public static final SoundEvent ICE_GUARDIAN_DEATH = createEvent("entity.iceguardian.death");
	public static final SoundEvent SWARM_SPIDER_STEP = createEvent("entity.swarmspider.step");
	public static final SoundEvent SWARM_SPIDER_HURT = createEvent("entity.swarmspider.hurt");
	public static final SoundEvent SWARM_SPIDER_DEATH = createEvent("entity.swarmspider.death");
	public static final SoundEvent SWARM_SPIDER_AMBIENT = createEvent("entity.swarmspider.ambient");
	public static final SoundEvent BROODLING_STEP = createEvent("entity.broodling.step");
	public static final SoundEvent BROODLING_HURT = createEvent("entity.broodling.hurt");
	public static final SoundEvent BROODLING_DEATH = createEvent("entity.broodling.death");
	public static final SoundEvent BROODLING_AMBIENT = createEvent("entity.broodling.ambient");
	public static final SoundEvent CARMINITE_GOLEM_STEP = createEvent("entity.carminitegolem.step");
	public static final SoundEvent CARMINITE_GOLEM_HURT = createEvent("entity.carminitegolem.hurt");
	public static final SoundEvent CARMINITE_GOLEM_DEATH = createEvent("entity.carminitegolem.death");
	public static final SoundEvent CARMINITE_GOLEM_ATTACK = createEvent("entity.carminitegolem.attack");
	public static final SoundEvent SNOW_QUEEN_HURT = createEvent("entity.snowqueen.hurt");
	public static final SoundEvent SNOW_QUEEN_DEATH = createEvent("entity.snowqueen.death");
	public static final SoundEvent SNOW_QUEEN_AMBIENT = createEvent("entity.snowqueen.ambient");
	public static final SoundEvent TERMITE_STEP = createEvent("entity.termite.step");
	public static final SoundEvent TERMITE_HURT = createEvent("entity.termite.hurt");
	public static final SoundEvent TERMITE_DEATH = createEvent("entity.termite.death");
	public static final SoundEvent TERMITE_AMBIENT = createEvent("entity.termite.ambient");
	public static final SoundEvent WINTER_WOLF_HURT = createEvent("entity.winterwolf.hurt");
	public static final SoundEvent WINTER_WOLF_SHOOT = createEvent("entity.winterwolf.shoot");
	public static final SoundEvent WINTER_WOLF_IDLE = createEvent("entity.winterwolf.idle");
	public static final SoundEvent LICH_AMBIENT = createEvent("entity.lich.ambient");
	public static final SoundEvent LICH_HURT = createEvent("entity.lich.hurt");
	public static final SoundEvent LICH_DEATH = createEvent("entity.lich.death");
	public static final SoundEvent LICH_TELEPORT = createEvent("entity.lich.teleport");
	public static final SoundEvent LICH_SHOOT = createEvent("entity.lich.shoot");
	public static final SoundEvent MINION_AMBIENT = createEvent("entity.minion.ambient");
	public static final SoundEvent MINION_HURT = createEvent("entity.minion.hurt");
	public static final SoundEvent MINION_DEATH = createEvent("entity.minion.death");
	public static final SoundEvent MINION_STEP = createEvent("entity.minion.step");
	public static final SoundEvent URGHAST_HURT = createEvent("entity.urghast.hurt");
	public static final SoundEvent URGHAST_DEATH = createEvent("entity.urghast.death");
	public static final SoundEvent URGHAST_AMBIENT = createEvent("entity.urghast.ambient");
	public static final SoundEvent YETI_DIE = createEvent("entity.yeti.die");
	public static final SoundEvent YETI_GRAB = createEvent("entity.yeti.grab");
	public static final SoundEvent YETI_GROWL = createEvent("entity.yeti.growl");
	public static final SoundEvent YETI_HURT = createEvent("entity.yeti.hurt");
	public static final SoundEvent YETI_THROW = createEvent("entity.yeti.throw");
	public static final SoundEvent QUEST_RAM_HURT = createEvent("entity.quest.hurt");
	public static final SoundEvent QUEST_RAM_DEATH = createEvent("entity.quest.death");
	public static final SoundEvent QUEST_RAM_AMBIENT = createEvent("entity.quest.ambient");
	public static final SoundEvent QUEST_RAM_STEP = createEvent("entity.quest.step");
	public static final SoundEvent BIGHORN_HURT = createEvent("entity.bighorn.hurt");
	public static final SoundEvent BIGHORN_DEATH = createEvent("entity.bighorn.death");
	public static final SoundEvent BIGHORN_AMBIENT = createEvent("entity.bighorn.ambient");
	public static final SoundEvent BIGHORN_STEP = createEvent("entity.bighorn.step");
	public static final SoundEvent BOAR_HURT = createEvent("entity.boar.hurt");
	public static final SoundEvent BOAR_DEATH = createEvent("entity.boar.death");
	public static final SoundEvent BOAR_AMBIENT = createEvent("entity.boar.ambient");
	public static final SoundEvent BOAR_STEP = createEvent("entity.boar.step");
	
	public static final SoundEvent SLIDER = createEvent("random.slider");

	public static final SoundEvent MUSIC = createEvent("music.bg");

	private static SoundEvent createEvent(String sound) {
		ResourceLocation name = TwilightForestMod.prefix(sound);
		return new SoundEvent(name).setRegistryName(name);
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(
				KOBOLD_DEATH,
				KOBOLD_AMBIENT,
				KOBOLD_HURT,
				KOBOLD_PARROT,
				CICADA,
				NAGA_HISS,
				NAGA_HURT,
				NAGA_RATTLE,
				NAGA_PARROT,
				RAVEN_CAW,
				RAVEN_SQUAWK,
				REDCAP_DEATH,
				REDCAP_AMBIENT,
				REDCAP_HURT,
				REDCAP_PARROT,
				TINYBIRD_CHIRP,
				TINYBIRD_HURT,
				TINYBIRD_SONG,
				URGHAST_TRAP_ACTIVE,
				URGHAST_TRAP_ON,
				URGHAST_TRAP_SPINDOWN,
				URGHAST_TRAP_WARMUP,
				WRAITH,
				WRAITH_PARROT,
				HYDRA_DEATH,
				HYDRA_GROWL,
				HYDRA_HURT,
				HYDRA_ROAR,
				HYDRA_WARN,
				HYDRA_PARROT,
				MOSQUITO,
				MOSQUITO_PARROT,
				ICE_CORE_AMBIENT,
				ICE_CORE_DEATH,
				ICE_CORE_HURT,
				ICE_CORE_PARROT,
				MINOTAUR_PARROT,
				ALPHAYETI_ALERT,
				ALPHAYETI_DIE,
				ALPHAYETI_GRAB,
				ALPHAYETI_GROWL,
				ALPHAYETI_HURT,
				ALPHAYETI_PANT,
				ALPHAYETI_ROAR,
				ALPHAYETI_THROW,
				ALPHAYETI_PARROT,
				DEER_DEATH,
				DEER_HURT,
				DEER_IDLE,
				MISTWOLF_TARGET,
				MISTWOLF_HURT,
				MISTWOLF_IDLE,
				MISTWOLF_PARROT,
				TOME_DEATH,
				TOME_HURT,
				TOME_IDLE,
				TOME_PARROT,
				BLOCKCHAIN_AMBIENT,
				BLOCKCHAIN_HURT,
				BLOCKCHAIN_DEATH,
				FIRE_BEETLE_STEP,
				FIRE_BEETLE_HURT,
				FIRE_BEETLE_DEATH,
				FIRE_BEETLE_SHOOT,
				HEDGE_SPIDER_STEP,
				HEDGE_SPIDER_HURT,
				HEDGE_SPIDER_DEATH,
				HEDGE_SPIDER_AMBIENT,
				HELMET_CRAB_STEP,
				HELMET_CRAB_HURT,
				HELMET_CRAB_DEATH,
				HOSTILE_WOLF_TARGET,
				HOSTILE_WOLF_HURT,
				HOSTILE_WOLF_IDLE,
				KING_SPIDER_STEP,
				KING_SPIDER_HURT,
				KING_SPIDER_DEATH,
				KING_SPIDER_AMBIENT,
				LOYAL_ZOMBIE_STEP,
				LOYAL_ZOMBIE_HURT,
				LOYAL_ZOMBIE_DEATH,
				LOYAL_ZOMBIE_AMBIENT,
				MAZE_SLIME_HURT,
				MAZE_SLIME_DEATH,
				MAZE_SLIME_SQUISH,
				MAZE_SLIME_HURT_SMALL,
				MAZE_SLIME_DEATH_SMALL,
				MAZE_SLIME_SQUISH_SMALL,
				GHASTLING_HURT,
				GHASTLING_DEATH,
				GHASTLING_AMBIENT,
				GHASTGUARD_HURT,
				GHASTGUARD_DEATH,
				GHASTGUARD_AMBIENT,
				MINOTAUR_STEP,
				MINOTAUR_HURT,
				MINOTAUR_DEATH,
				MINOTAUR_AMBIENT,
				MINOTAUR_ATTACK,
				MINOSHROOM_STEP,
				MINOSHROOM_HURT,
				MINOSHROOM_DEATH,
				MINOSHROOM_AMBIENT,
				MINOSHROOM_ATTACK,
				PINCH_BEETLE_STEP,
				PINCH_BEETLE_HURT,
				PINCH_BEETLE_DEATH,
				SKELETON_DRUID_STEP,
				SKELETON_DRUID_HURT,
				SKELETON_DRUID_DEATH,
				SKELETON_DRUID_AMBIENT,
				SKELETON_DRUID_SHOOT,
				SLIME_BEETLE_STEP,
				SLIME_BEETLE_HURT,
				SLIME_BEETLE_DEATH,
				SLIME_BEETLE_SQUISH_SMALL,
				ICE_GUARDIAN_AMBIENT,
				ICE_GUARDIAN_HURT,
				ICE_GUARDIAN_DEATH,
				SWARM_SPIDER_STEP,
				SWARM_SPIDER_HURT,
				SWARM_SPIDER_DEATH,
				SWARM_SPIDER_AMBIENT,
				BROODLING_STEP,
				BROODLING_HURT,
				BROODLING_DEATH,
				BROODLING_AMBIENT,
				CARMINITE_GOLEM_STEP,
				CARMINITE_GOLEM_HURT,
				CARMINITE_GOLEM_DEATH,
				CARMINITE_GOLEM_ATTACK,
				SNOW_QUEEN_HURT,
				SNOW_QUEEN_DEATH,
				SNOW_QUEEN_AMBIENT,
				TERMITE_STEP,
				TERMITE_HURT,
				TERMITE_DEATH,
				TERMITE_AMBIENT ,
				WINTER_WOLF_HURT,
				WINTER_WOLF_SHOOT,
				WINTER_WOLF_IDLE,
				LICH_AMBIENT,
				LICH_HURT,
				LICH_DEATH,
				LICH_TELEPORT,
				LICH_SHOOT,
				MINION_AMBIENT,
				MINION_HURT,
				MINION_DEATH,
				MINION_STEP,
				URGHAST_HURT,
				URGHAST_DEATH,
				URGHAST_AMBIENT,
				QUEST_RAM_HURT,
				QUEST_RAM_AMBIENT,
				QUEST_RAM_DEATH,
				QUEST_RAM_STEP,
				BIGHORN_HURT,
				BIGHORN_AMBIENT,
				BIGHORN_DEATH,
				BIGHORN_STEP,
				BOAR_HURT,
				BOAR_AMBIENT,
				BOAR_DEATH,
				BOAR_STEP,
				SLIDER,
				MUSIC
		);

		registerParrotSounds();
	}

	private static void registerParrotSounds() {

		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.kobold, KOBOLD_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.redcap, REDCAP_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.redcap_sapper, REDCAP_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.blockchain_goblin, REDCAP_PARROT);
//		ParrotEntity.IMITATION_SOUND_EVENTS.put(EntityTFBoggard.class, REDCAP_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.wraith, WRAITH_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.mosquito_swarm, MOSQUITO_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.unstable_ice_core, ICE_CORE_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.stable_ice_core, ICE_CORE_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.snow_guardian, ICE_CORE_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.loyal_zombie, SoundEvents.ENTITY_PARROT_IMITATE_ZOMBIE);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.minotaur, MINOTAUR_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.king_spider, SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.hedge_spider, SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.swarm_spider, SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.tower_broodling, SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.mist_wolf, MISTWOLF_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.winter_wolf, MISTWOLF_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.skeleton_druid, SoundEvents.ENTITY_PARROT_IMITATE_SKELETON);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.tower_ghast, SoundEvents.ENTITY_PARROT_IMITATE_GHAST);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.mini_ghast, SoundEvents.ENTITY_PARROT_IMITATE_GHAST);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.tower_termite, SoundEvents.ENTITY_PARROT_IMITATE_SILVERFISH);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.death_tome, TOME_PARROT);

		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.hydra, HYDRA_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.knight_phantom, WRAITH_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.lich, SoundEvents.ENTITY_PARROT_IMITATE_BLAZE);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.minoshroom, MINOTAUR_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.naga, NAGA_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.snow_queen, ICE_CORE_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.ur_ghast, SoundEvents.ENTITY_PARROT_IMITATE_GHAST);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.yeti_alpha, ALPHAYETI_PARROT);
	}

	private TFSounds() {}

}
