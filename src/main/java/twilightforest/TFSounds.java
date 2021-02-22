package twilightforest;

import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.entity.TFEntities;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public final class TFSounds {
	
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TwilightForestMod.ID);

	public static final SoundEvent ALPHAYETI_ALERT = createEvent("entity.alphayeti.alert");
	public static final SoundEvent ALPHAYETI_DEATH = createEvent("entity.alphayeti.death");
	public static final SoundEvent ALPHAYETI_GRAB = createEvent("entity.alphayeti.grab");
	public static final SoundEvent ALPHAYETI_GROWL = createEvent("entity.alphayeti.growl");
	public static final SoundEvent ALPHAYETI_HURT = createEvent("entity.alphayeti.hurt");
	public static final SoundEvent ALPHAYETI_ICE = createEvent("entity.alphayeti.ice");
	public static final SoundEvent ALPHAYETI_PANT = createEvent("entity.alphayeti.pant");
	public static final SoundEvent ALPHAYETI_ROAR = createEvent("entity.alphayeti.roar");
	public static final SoundEvent ALPHAYETI_THROW = createEvent("entity.alphayeti.throw");
	public static final SoundEvent BIGHORN_AMBIENT = createEvent("entity.bighorn.ambient");
	public static final SoundEvent BIGHORN_DEATH = createEvent("entity.bighorn.death");
	public static final SoundEvent BIGHORN_HURT = createEvent("entity.bighorn.hurt");
	public static final SoundEvent BIGHORN_STEP = createEvent("entity.bighorn.step");
	public static final SoundEvent BLOCKCHAIN_AMBIENT = createEvent("entity.blockchain.ambient");
	public static final SoundEvent BLOCKCHAIN_COLLIDE = createEvent("item.blockchain.collide");
	public static final SoundEvent BLOCKCHAIN_DEATH = createEvent("entity.blockchain.death");
	public static final SoundEvent BLOCKCHAIN_FIRED = createEvent("item.blockchain.fire");
	public static final SoundEvent BLOCKCHAIN_HIT = createEvent("item.blockchain.hit");
	public static final SoundEvent BLOCKCHAIN_HURT = createEvent("entity.blockchain.hurt");
	public static final SoundEvent BOAR_AMBIENT = createEvent("entity.boar.ambient");
	public static final SoundEvent BOAR_DEATH = createEvent("entity.boar.death");
	public static final SoundEvent BOAR_HURT = createEvent("entity.boar.hurt");
	public static final SoundEvent BOAR_STEP = createEvent("entity.boar.step");
	public static final SoundEvent BROODLING_AMBIENT = createEvent("entity.broodling.ambient");
	public static final SoundEvent BROODLING_DEATH = createEvent("entity.broodling.death");
	public static final SoundEvent BROODLING_HURT = createEvent("entity.broodling.hurt");
	public static final SoundEvent BROODLING_STEP = createEvent("entity.broodling.step");
	public static final SoundEvent BUILDER_OFF = createEvent("block.builder.off");
	public static final SoundEvent BUILDER_ON = createEvent("block.builder.on");
	public static final SoundEvent BUILDER_REPLACE = createEvent("block.builder.replace");
	public static final SoundEvent CARMINITE_GOLEM_ATTACK = createEvent("entity.carminitegolem.attack");
	public static final SoundEvent CARMINITE_GOLEM_DEATH = createEvent("entity.carminitegolem.death");
	public static final SoundEvent CARMINITE_GOLEM_HURT = createEvent("entity.carminitegolem.hurt");
	public static final SoundEvent CARMINITE_GOLEM_STEP = createEvent("entity.carminitegolem.step");
	public static final SoundEvent CHARM_KEEP = createEvent("item.charm.keep");
	public static final SoundEvent CHARM_LIFE = createEvent("item.charm.life");
	public static final SoundEvent CICADA = createEvent("entity.cicada");
	public static final SoundEvent DEER_DEATH = createEvent("entity.deer.death");
	public static final SoundEvent DEER_HURT = createEvent("entity.deer.hurt");
	public static final SoundEvent DEER_IDLE = createEvent("entity.deer.idle");
	public static final SoundEvent DOOR_ACTIVATED = createEvent("block.door.activate");
	public static final SoundEvent DOOR_REAPPEAR = createEvent("block.door.reappear");
	public static final SoundEvent DOOR_VANISH = createEvent("block.door.vanish");
	public static final SoundEvent FAN_WOOSH = createEvent("item.fan.woosh");
	public static final SoundEvent FIRE_BEETLE_DEATH = createEvent("entity.firebeetle.death");
	public static final SoundEvent FIRE_BEETLE_HURT = createEvent("entity.firebeetle.hurt");
	public static final SoundEvent FIRE_BEETLE_SHOOT = createEvent("entity.firebeetle.shoot");
	public static final SoundEvent FIRE_BEETLE_STEP = createEvent("entity.firebeetle.step");
	public static final SoundEvent GHASTGUARD_AMBIENT = createEvent("entity.ghastguard.ambient");
	public static final SoundEvent GHASTGUARD_DEATH = createEvent("entity.ghastguard.death");
	public static final SoundEvent GHASTGUARD_HURT = createEvent("entity.ghastguard.hurt");
	public static final SoundEvent GHASTLING_AMBIENT = createEvent("entity.ghastling.ambient");
	public static final SoundEvent GHASTLING_DEATH = createEvent("entity.ghastling.death");
	public static final SoundEvent GHASTLING_HURT = createEvent("entity.ghastling.hurt");
	public static final SoundEvent GLASS_SWORD_BREAK = createEvent("item.glasssword.break");
	public static final SoundEvent HEDGE_SPIDER_AMBIENT = createEvent("entity.hedgespider.ambient");
	public static final SoundEvent HEDGE_SPIDER_DEATH = createEvent("entity.hedgespider.death");
	public static final SoundEvent HEDGE_SPIDER_HURT = createEvent("entity.hedgespider.hurt");
	public static final SoundEvent HEDGE_SPIDER_STEP = createEvent("entity.hedgespider.step");
	public static final SoundEvent HELMET_CRAB_DEATH = createEvent("entity.helmetcrab.death");
	public static final SoundEvent HELMET_CRAB_HURT = createEvent("entity.helmetcrab.hurt");
	public static final SoundEvent HELMET_CRAB_STEP = createEvent("entity.helmetcrab.step");
	public static final SoundEvent HOSTILE_WOLF_DEATH = createEvent("entity.hostilewolf.death");
	public static final SoundEvent HOSTILE_WOLF_HURT = createEvent("entity.hostilewolf.hurt");
	public static final SoundEvent HOSTILE_WOLF_IDLE = createEvent("entity.hostilewolf.idle");
	public static final SoundEvent HOSTILE_WOLF_TARGET = createEvent("entity.hostilewolf.target");
	public static final SoundEvent HYDRA_DEATH = createEvent("entity.hydra.death");
	public static final SoundEvent HYDRA_GROWL = createEvent("entity.hydra.growl");
	public static final SoundEvent HYDRA_HURT = createEvent("entity.hydra.hurt");
	public static final SoundEvent HYDRA_ROAR = createEvent("entity.hydra.roar");
	public static final SoundEvent HYDRA_SHOOT = createEvent("entity.hydra.shoot");
	public static final SoundEvent HYDRA_WARN = createEvent("entity.hydra.warn");
	public static final SoundEvent ICEBOMB_FIRED = createEvent("item.icebomb.fired");
	public static final SoundEvent ICE_CORE_AMBIENT = createEvent("entity.ice.noise");
	public static final SoundEvent ICE_CORE_DEATH = createEvent("entity.ice.death");
	public static final SoundEvent ICE_CORE_HURT = createEvent("entity.ice.hurt");
	public static final SoundEvent ICE_CORE_SHOOT = createEvent("entity.ice.shoot");
	public static final SoundEvent ICE_GUARDIAN_AMBIENT = createEvent("entity.iceguardian.ambient");
	public static final SoundEvent ICE_GUARDIAN_DEATH = createEvent("entity.iceguardian.death");
	public static final SoundEvent ICE_GUARDIAN_HURT = createEvent("entity.iceguardian.hurt");
	public static final SoundEvent JET_ACTIVE = createEvent("te.jet.active");
	public static final SoundEvent JET_POP = createEvent("te.jet.pop");
	public static final SoundEvent JET_START = createEvent("te.jet.start");
	public static final SoundEvent KING_SPIDER_AMBIENT = createEvent("entity.kingspider.ambient");
	public static final SoundEvent KING_SPIDER_DEATH = createEvent("entity.kingspider.death");
	public static final SoundEvent KING_SPIDER_HURT = createEvent("entity.kingspider.hurt");
	public static final SoundEvent KING_SPIDER_STEP = createEvent("entity.kingspider.step");
	public static final SoundEvent KOBOLD_AMBIENT = createEvent("entity.kobold.ambient");
	public static final SoundEvent KOBOLD_DEATH = createEvent("entity.kobold.death");
	public static final SoundEvent KOBOLD_HURT = createEvent("entity.kobold.hurt");
	public static final SoundEvent LAMP_BURN = createEvent("item.lamp.burn");
	public static final SoundEvent LICH_AMBIENT = createEvent("entity.lich.ambient");
	public static final SoundEvent LICH_CLONE_HURT = createEvent("entity.lichclone.hurt");
	public static final SoundEvent LICH_DEATH = createEvent("entity.lich.death");
	public static final SoundEvent LICH_HURT = createEvent("entity.lich.hurt");
	public static final SoundEvent LICH_SHOOT = createEvent("entity.lich.shoot");
	public static final SoundEvent LICH_TELEPORT = createEvent("entity.lich.teleport");
	public static final SoundEvent LOCKED_VANISHING_BLOCK = createEvent("block.vanish.locked");
	public static final SoundEvent LOYAL_ZOMBIE_AMBIENT = createEvent("entity.loyalzombie.ambient");
	public static final SoundEvent LOYAL_ZOMBIE_DEATH = createEvent("entity.loyalzombie.death");
	public static final SoundEvent LOYAL_ZOMBIE_HURT = createEvent("entity.loyalzombie.hurt");
	public static final SoundEvent LOYAL_ZOMBIE_STEP = createEvent("entity.loyalzombie.step");
	public static final SoundEvent MAGNET_GRAB = createEvent("item.magnet.grab");
	public static final SoundEvent MAZE_SLIME_DEATH = createEvent("entity.mazeslime.death");
	public static final SoundEvent MAZE_SLIME_DEATH_SMALL = createEvent("entity.mazeslimesmall.death");
	public static final SoundEvent MAZE_SLIME_HURT = createEvent("entity.mazeslime.hurt");
	public static final SoundEvent MAZE_SLIME_HURT_SMALL = createEvent("entity.mazeslimesmall.hurt");
	public static final SoundEvent MAZE_SLIME_SQUISH = createEvent("entity.mazeslime.ambient");
	public static final SoundEvent MAZE_SLIME_SQUISH_SMALL = createEvent("entity.mazeslimesmall.ambient");
	public static final SoundEvent MINION_AMBIENT = createEvent("entity.minion.ambient");
	public static final SoundEvent MINION_DEATH = createEvent("entity.minion.death");
	public static final SoundEvent MINION_HURT = createEvent("entity.minion.hurt");
	public static final SoundEvent MINION_STEP = createEvent("entity.minion.step");
	public static final SoundEvent MINION_SUMMON = createEvent("entity.minion.summon");
	public static final SoundEvent MINOSHROOM_AMBIENT = createEvent("entity.minoshroom.ambient");
	public static final SoundEvent MINOSHROOM_ATTACK = createEvent("entity.minoshroom.attack");
	public static final SoundEvent MINOSHROOM_DEATH = createEvent("entity.minoshroom.death");
	public static final SoundEvent MINOSHROOM_HURT = createEvent("entity.minoshroom.hurt");
	public static final SoundEvent MINOSHROOM_STEP = createEvent("entity.minoshroom.step");
	public static final SoundEvent MINOTAUR_AMBIENT = createEvent("entity.minotaur.ambient");
	public static final SoundEvent MINOTAUR_ATTACK = createEvent("entity.minotaur.attack");
	public static final SoundEvent MINOTAUR_DEATH = createEvent("entity.minotaur.death");
	public static final SoundEvent MINOTAUR_HURT = createEvent("entity.minotaur.hurt");
	public static final SoundEvent MINOTAUR_STEP = createEvent("entity.minotaur.step");
	public static final SoundEvent MISTWOLF_DEATH = createEvent("entity.mistwolf.death");
	public static final SoundEvent MISTWOLF_HURT = createEvent("entity.mistwolf.hurt");
	public static final SoundEvent MISTWOLF_IDLE = createEvent("entity.mistwolf.idle");
	public static final SoundEvent MISTWOLF_TARGET = createEvent("entity.mistwolf.target");
	public static final SoundEvent MOONWORM_SQUISH = createEvent("item.moonworm.squish");
	public static final SoundEvent MOSQUITO = createEvent("entity.mosquito.ambient");
	public static final SoundEvent NAGA_HISS = createEvent("entity.naga.hiss");
	public static final SoundEvent NAGA_HURT = createEvent("entity.naga.hurt");
	public static final SoundEvent NAGA_RATTLE = createEvent("entity.naga.rattle");
	public static final SoundEvent PEDESTAL_ACTIVATE = createEvent("block.pedestal.activate");
	public static final SoundEvent PHANTOM_AMBIENT = createEvent("entity.phantom.ambient");
	public static final SoundEvent PHANTOM_DEATH = createEvent("entity.phantom.death");
	public static final SoundEvent PHANTOM_HURT = createEvent("entity.phantom.hurt");
	public static final SoundEvent PHANTOM_THROW_AXE = createEvent("entity.phantom.axe");
	public static final SoundEvent PHANTOM_THROW_PICK = createEvent("entity.phantom.pick");
	public static final SoundEvent PINCH_BEETLE_DEATH = createEvent("entity.pinchbeetle.death");
	public static final SoundEvent PINCH_BEETLE_HURT = createEvent("entity.pinchbeetle.hurt");
	public static final SoundEvent PINCH_BEETLE_STEP = createEvent("entity.pinchbeetle.step");
	public static final SoundEvent PORTAL_WOOSH = createEvent("block.portal.woosh");
	public static final SoundEvent POWDER_USE = createEvent("item.powder.use");
	public static final SoundEvent QUEST_RAM_AMBIENT = createEvent("entity.quest.ambient");
	public static final SoundEvent QUEST_RAM_DEATH = createEvent("entity.quest.death");
	public static final SoundEvent QUEST_RAM_HURT = createEvent("entity.quest.hurt");
	public static final SoundEvent QUEST_RAM_STEP = createEvent("entity.quest.step");
	public static final SoundEvent RAVEN_CAW = createEvent("entity.raven.caw");
	public static final SoundEvent RAVEN_SQUAWK = createEvent("entity.raven.squawk");
	public static final SoundEvent REACTOR_AMBIENT = createEvent("te.reactor.idle");
	public static final SoundEvent REAPPEAR_BLOCK = createEvent("block.vanish.reappear");
	public static final SoundEvent REDCAP_AMBIENT = createEvent("entity.redcap.ambient");
	public static final SoundEvent REDCAP_DEATH = createEvent("entity.redcap.death");
	public static final SoundEvent REDCAP_HURT = createEvent("entity.redcap.hurt");
	public static final SoundEvent SCEPTER_DRAIN = createEvent("item.scepter.drain");
	public static final SoundEvent SCEPTER_PEARL = createEvent("item.scepter.pearl");
	public static final SoundEvent SCEPTER_USE = createEvent("item.scepter.use");
	public static final SoundEvent SHIELD_ADD = createEvent("entity.shield.add");
	public static final SoundEvent SHIELD_BREAK = createEvent("entity.shield.break");
	public static final SoundEvent SKELETON_DRUID_AMBIENT = createEvent("entity.druid.ambient");
	public static final SoundEvent SKELETON_DRUID_DEATH = createEvent("entity.druid.death");
	public static final SoundEvent SKELETON_DRUID_HURT = createEvent("entity.druid.hurt");
	public static final SoundEvent SKELETON_DRUID_SHOOT = createEvent("entity.druid.shoot");
	public static final SoundEvent SKELETON_DRUID_STEP = createEvent("entity.druid.step");
	public static final SoundEvent SLIME_BEETLE_DEATH = createEvent("entity.slimebeetle.death");
	public static final SoundEvent SLIME_BEETLE_HURT = createEvent("entity.slimebeetle.hurt");
	public static final SoundEvent SLIME_BEETLE_SQUISH_SMALL = createEvent("entity.slimebeetle.squish");
	public static final SoundEvent SLIME_BEETLE_STEP = createEvent("entity.slimebeetle.step");
	public static final SoundEvent SMOKER_START = createEvent("te.smoker.start");
	public static final SoundEvent SNOW_QUEEN_AMBIENT = createEvent("entity.snowqueen.ambient");
	public static final SoundEvent SNOW_QUEEN_ATTACK = createEvent("entity.snowqueen.attack");
	public static final SoundEvent SNOW_QUEEN_BREAK = createEvent("entity.snowqueen.break");
	public static final SoundEvent SNOW_QUEEN_DEATH = createEvent("entity.snowqueen.death");
	public static final SoundEvent SNOW_QUEEN_HURT = createEvent("entity.snowqueen.hurt");
	public static final SoundEvent SWARM_SPIDER_AMBIENT = createEvent("entity.swarmspider.ambient");
	public static final SoundEvent SWARM_SPIDER_DEATH = createEvent("entity.swarmspider.death");
	public static final SoundEvent SWARM_SPIDER_HURT = createEvent("entity.swarmspider.hurt");
	public static final SoundEvent SWARM_SPIDER_STEP = createEvent("entity.swarmspider.step");
	public static final SoundEvent TEAR_BREAK = createEvent("entity.tear.break");
	public static final SoundEvent TERMITE_AMBIENT = createEvent("entity.termite.ambient");
	public static final SoundEvent TERMITE_DEATH = createEvent("entity.termite.death");
	public static final SoundEvent TERMITE_HURT = createEvent("entity.termite.hurt");
	public static final SoundEvent TERMITE_STEP = createEvent("entity.termite.step");
	public static final SoundEvent TIME_CORE = createEvent("block.core.time");
	public static final SoundEvent TINYBIRD_CHIRP = createEvent("entity.tinybird.chirp");
	public static final SoundEvent TINYBIRD_HURT = createEvent("entity.tinybird.hurt");
	public static final SoundEvent TINYBIRD_SONG = createEvent("entity.tinybird.song");
	public static final SoundEvent TOME_DEATH = createEvent("entity.tome.death");
	public static final SoundEvent TOME_HURT = createEvent("entity.tome.hurt");
	public static final SoundEvent TOME_IDLE = createEvent("entity.tome.idle");
	public static final SoundEvent TRANSFORMATION_CORE = createEvent("block.core.transformation");
	public static final SoundEvent UNLOCK_VANISHING_BLOCK = createEvent("block.vanish.unlock");
	public static final SoundEvent URGHAST_AMBIENT = createEvent("entity.urghast.ambient");
	public static final SoundEvent URGHAST_DEATH = createEvent("entity.urghast.death");
	public static final SoundEvent URGHAST_HURT = createEvent("entity.urghast.hurt");
	public static final SoundEvent URGHAST_TRAP_ACTIVE = createEvent("entity.urghast.trapactive");
	public static final SoundEvent URGHAST_TRAP_ON = createEvent("entity.urghast.trapon");
	public static final SoundEvent URGHAST_TRAP_SPINDOWN = createEvent("entity.urghast.trapspindown");
	public static final SoundEvent URGHAST_TRAP_WARMUP = createEvent("entity.urghast.trapwarmup");
	public static final SoundEvent VANISHING_BLOCK = createEvent("block.vanish.vanish");
	public static final SoundEvent WINTER_WOLF_DEATH = createEvent("entity.winterwolf.death");
	public static final SoundEvent WINTER_WOLF_HURT = createEvent("entity.winterwolf.hurt");
	public static final SoundEvent WINTER_WOLF_IDLE = createEvent("entity.winterwolf.idle");
	public static final SoundEvent WINTER_WOLF_SHOOT = createEvent("entity.winterwolf.shoot");
	public static final SoundEvent WINTER_WOLF_TARGET = createEvent("entity.winterwolf.target");
	public static final SoundEvent WRAITH_AMBIENT = createEvent("entity.wraith.ambient");
	public static final SoundEvent WRAITH_DEATH = createEvent("entity.wraith.death");
	public static final SoundEvent WRAITH_HURT = createEvent("entity.wraith.hurt");
	public static final SoundEvent YETI_DEATH = createEvent("entity.yeti.death");
	public static final SoundEvent YETI_GRAB = createEvent("entity.yeti.grab");
	public static final SoundEvent YETI_GROWL = createEvent("entity.yeti.growl");
	public static final SoundEvent YETI_HURT = createEvent("entity.yeti.hurt");
	public static final SoundEvent YETI_THROW = createEvent("entity.yeti.throw");
	
	//Parrot sounds
	public static final SoundEvent ALPHAYETI_PARROT = createEvent("entity.alphayeti.parrot");
	public static final SoundEvent CARMINITE_GOLEM_PARROT = createEvent("entity.carminitegolem.parrot");
	public static final SoundEvent HOSTILE_WOLF_PARROT = createEvent("entity.hostilewolf.parrot");
	public static final SoundEvent HYDRA_PARROT = createEvent("entity.hydra.parrot");
	public static final SoundEvent ICE_CORE_PARROT = createEvent("entity.icecore.parrot");
	public static final SoundEvent KOBOLD_PARROT = createEvent("entity.kobold.parrot");
	public static final SoundEvent MINOTAUR_PARROT = createEvent("entity.minotaur.parrot");
	public static final SoundEvent MOSQUITO_PARROT = createEvent("entity.mosquito.parrot");
	public static final SoundEvent NAGA_PARROT = createEvent("entity.naga.parrot");
	public static final SoundEvent REDCAP_PARROT = createEvent("entity.redcap.parrot");
	public static final SoundEvent TOME_PARROT = createEvent("entity.tome.parrot");
	public static final SoundEvent WRAITH_PARROT = createEvent("entity.wraith.parrot");

	public static final SoundEvent SLIDER = createEvent("random.slider");

	public static final SoundEvent MUSIC = createEvent("music.bg");

	private static SoundEvent createEvent(String sound) {
		ResourceLocation name = TwilightForestMod.prefix(sound);
		return new SoundEvent(name).setRegistryName(name);
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(
				ALPHAYETI_ALERT, ALPHAYETI_DEATH, ALPHAYETI_GRAB, ALPHAYETI_GROWL, ALPHAYETI_HURT, ALPHAYETI_ICE, ALPHAYETI_PANT, ALPHAYETI_ROAR, ALPHAYETI_THROW,
				BIGHORN_AMBIENT, BIGHORN_DEATH, BIGHORN_HURT, BIGHORN_STEP,
				BLOCKCHAIN_AMBIENT, BLOCKCHAIN_COLLIDE, BLOCKCHAIN_DEATH, BLOCKCHAIN_FIRED, BLOCKCHAIN_HIT, BLOCKCHAIN_HURT,
				BOAR_AMBIENT, BOAR_DEATH, BOAR_HURT, BOAR_STEP,
				BROODLING_AMBIENT, BROODLING_DEATH, BROODLING_HURT, BROODLING_STEP,
				BUILDER_OFF, BUILDER_ON, BUILDER_REPLACE,
				CARMINITE_GOLEM_ATTACK, CARMINITE_GOLEM_DEATH, CARMINITE_GOLEM_HURT, CARMINITE_GOLEM_STEP,
				CHARM_KEEP, CHARM_LIFE,
				CICADA,
				DEER_DEATH, DEER_HURT, DEER_IDLE,
				DOOR_ACTIVATED, DOOR_REAPPEAR, DOOR_VANISH,
				FAN_WOOSH,
				FIRE_BEETLE_DEATH, FIRE_BEETLE_HURT, FIRE_BEETLE_SHOOT, FIRE_BEETLE_STEP,
				GHASTGUARD_AMBIENT, GHASTGUARD_DEATH, GHASTGUARD_HURT,
				GHASTLING_AMBIENT, GHASTLING_DEATH, GHASTLING_HURT,
				GLASS_SWORD_BREAK,
				HEDGE_SPIDER_AMBIENT, HEDGE_SPIDER_DEATH, HEDGE_SPIDER_HURT, HEDGE_SPIDER_STEP,
				HELMET_CRAB_DEATH, HELMET_CRAB_HURT, HELMET_CRAB_STEP,
				HOSTILE_WOLF_DEATH, HOSTILE_WOLF_HURT, HOSTILE_WOLF_IDLE, HOSTILE_WOLF_TARGET,
				HYDRA_DEATH, HYDRA_GROWL, HYDRA_HURT, HYDRA_ROAR, HYDRA_SHOOT, HYDRA_WARN,
				ICEBOMB_FIRED,
				ICE_CORE_AMBIENT, ICE_CORE_DEATH, ICE_CORE_HURT, ICE_CORE_SHOOT,
				ICE_GUARDIAN_AMBIENT, ICE_GUARDIAN_DEATH, ICE_GUARDIAN_HURT,
				JET_ACTIVE, JET_POP, JET_START,
				KING_SPIDER_AMBIENT, KING_SPIDER_DEATH, KING_SPIDER_HURT, KING_SPIDER_STEP,
				KOBOLD_AMBIENT, KOBOLD_DEATH, KOBOLD_HURT,
				LAMP_BURN,
				LICH_AMBIENT, LICH_CLONE_HURT, LICH_DEATH, LICH_HURT, LICH_SHOOT, LICH_TELEPORT,
				LOCKED_VANISHING_BLOCK,
				LOYAL_ZOMBIE_AMBIENT, LOYAL_ZOMBIE_DEATH, LOYAL_ZOMBIE_HURT, LOYAL_ZOMBIE_STEP,
				MAGNET_GRAB,
				MAZE_SLIME_DEATH, MAZE_SLIME_DEATH_SMALL, MAZE_SLIME_HURT, MAZE_SLIME_HURT_SMALL, MAZE_SLIME_SQUISH, MAZE_SLIME_SQUISH_SMALL,
				MINION_AMBIENT, MINION_DEATH, MINION_HURT, MINION_STEP, MINION_SUMMON,
				MINOSHROOM_AMBIENT, MINOSHROOM_ATTACK, MINOSHROOM_DEATH, MINOSHROOM_HURT, MINOSHROOM_STEP,
				MINOTAUR_AMBIENT, MINOTAUR_ATTACK, MINOTAUR_DEATH, MINOTAUR_HURT, MINOTAUR_STEP,
				MISTWOLF_DEATH, MISTWOLF_HURT, MISTWOLF_IDLE, MISTWOLF_TARGET,
				MOONWORM_SQUISH,
				MOSQUITO,
				NAGA_HISS, NAGA_HURT, NAGA_RATTLE,
				PEDESTAL_ACTIVATE,
				PHANTOM_AMBIENT, PHANTOM_DEATH, PHANTOM_HURT, PHANTOM_THROW_AXE, PHANTOM_THROW_PICK,
				PINCH_BEETLE_DEATH, PINCH_BEETLE_HURT, PINCH_BEETLE_STEP,
				PORTAL_WOOSH,
				POWDER_USE,
				QUEST_RAM_AMBIENT, QUEST_RAM_DEATH, QUEST_RAM_HURT, QUEST_RAM_STEP,
				RAVEN_CAW, RAVEN_SQUAWK,
				REACTOR_AMBIENT,
				REAPPEAR_BLOCK,
				REDCAP_AMBIENT, REDCAP_DEATH, REDCAP_HURT,
				SCEPTER_DRAIN, SCEPTER_PEARL, SCEPTER_USE,
				SHIELD_ADD, SHIELD_BREAK,
				SKELETON_DRUID_AMBIENT, SKELETON_DRUID_DEATH, SKELETON_DRUID_HURT, SKELETON_DRUID_SHOOT, SKELETON_DRUID_STEP,
				SLIME_BEETLE_DEATH, SLIME_BEETLE_HURT, SLIME_BEETLE_SQUISH_SMALL, SLIME_BEETLE_STEP,
				SMOKER_START,
				SNOW_QUEEN_AMBIENT, SNOW_QUEEN_ATTACK, SNOW_QUEEN_BREAK, SNOW_QUEEN_DEATH, SNOW_QUEEN_HURT,
				SWARM_SPIDER_AMBIENT, SWARM_SPIDER_DEATH, SWARM_SPIDER_HURT, SWARM_SPIDER_STEP,
				TEAR_BREAK,
				TERMITE_AMBIENT, TERMITE_DEATH, TERMITE_HURT, TERMITE_STEP,
				TIME_CORE,
				TINYBIRD_CHIRP, TINYBIRD_HURT, TINYBIRD_SONG,
				TOME_DEATH, TOME_HURT, TOME_IDLE,
				TRANSFORMATION_CORE,
				UNLOCK_VANISHING_BLOCK,
				URGHAST_AMBIENT, URGHAST_DEATH, URGHAST_HURT,
				URGHAST_TRAP_ACTIVE, URGHAST_TRAP_ON, URGHAST_TRAP_SPINDOWN, URGHAST_TRAP_WARMUP,
				VANISHING_BLOCK,
				WINTER_WOLF_DEATH, WINTER_WOLF_HURT, WINTER_WOLF_IDLE, WINTER_WOLF_SHOOT, WINTER_WOLF_TARGET,
				WRAITH_AMBIENT, WRAITH_DEATH, WRAITH_HURT,
				YETI_DEATH, YETI_GRAB, YETI_GROWL, YETI_HURT, YETI_THROW,
				
				ALPHAYETI_PARROT,
				CARMINITE_GOLEM_PARROT,
				HOSTILE_WOLF_PARROT,
				HYDRA_PARROT,
				ICE_CORE_PARROT,
				KOBOLD_PARROT,
				MOSQUITO_PARROT,
				NAGA_PARROT,
				REDCAP_PARROT,
				TOME_PARROT,
				WRAITH_PARROT,
				
				SLIDER,
				MUSIC
		);

		registerParrotSounds();
	}

	private static void registerParrotSounds() {
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.yeti_alpha, ALPHAYETI_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.blockchain_goblin, REDCAP_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.tower_broodling, SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.tower_golem, CARMINITE_GOLEM_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.fire_beetle, SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.mini_ghast, SoundEvents.ENTITY_PARROT_IMITATE_GHAST);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.tower_ghast, SoundEvents.ENTITY_PARROT_IMITATE_GHAST);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.hedge_spider, SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.helmet_crab, SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.hostile_wolf, HOSTILE_WOLF_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.hydra, HYDRA_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.stable_ice_core, ICE_CORE_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.king_spider, SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.kobold, KOBOLD_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.lich, SoundEvents.ENTITY_PARROT_IMITATE_BLAZE);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.maze_slime, SoundEvents.ENTITY_PARROT_IMITATE_SLIME);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.lich_minion, SoundEvents.ENTITY_PARROT_IMITATE_ZOMBIE);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.minoshroom, MINOTAUR_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.minotaur, MINOTAUR_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.mist_wolf, HOSTILE_WOLF_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.mosquito_swarm, MOSQUITO_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.naga, NAGA_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.knight_phantom, WRAITH_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.pinch_beetle, SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.redcap, REDCAP_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.redcap_sapper, REDCAP_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.skeleton_druid, SoundEvents.ENTITY_PARROT_IMITATE_SKELETON);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.slime_beetle, SoundEvents.ENTITY_PARROT_IMITATE_SLIME);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.snow_guardian, ICE_CORE_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.snow_queen, ICE_CORE_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.swarm_spider, SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.tower_termite, SoundEvents.ENTITY_PARROT_IMITATE_SILVERFISH);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.death_tome, TOME_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.ur_ghast, SoundEvents.ENTITY_PARROT_IMITATE_GHAST);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.winter_wolf, HOSTILE_WOLF_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.wraith, WRAITH_PARROT);
		ParrotEntity.IMITATION_SOUND_EVENTS.put(TFEntities.yeti, ALPHAYETI_PARROT);
		
	}

	private TFSounds() {}

}
