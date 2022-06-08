package twilightforest;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.entity.TFEntities;

public final class TFSounds {
	
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TwilightForestMod.ID);

	public static final RegistryObject<SoundEvent> ACID_RAIN_BURNS = createEvent("entity.twilightforest.acid_rain");
	public static final RegistryObject<SoundEvent> ALPHAYETI_ALERT = createEvent("entity.twilightforest.alphayeti.alert");
	public static final RegistryObject<SoundEvent> ALPHAYETI_DEATH = createEvent("entity.twilightforest.alphayeti.death");
	public static final RegistryObject<SoundEvent> ALPHAYETI_GRAB = createEvent("entity.twilightforest.alphayeti.grab");
	public static final RegistryObject<SoundEvent> ALPHAYETI_GROWL = createEvent("entity.twilightforest.alphayeti.growl");
	public static final RegistryObject<SoundEvent> ALPHAYETI_HURT = createEvent("entity.twilightforest.alphayeti.hurt");
	public static final RegistryObject<SoundEvent> ALPHAYETI_ICE = createEvent("entity.twilightforest.alphayeti.ice");
	public static final RegistryObject<SoundEvent> ALPHAYETI_PANT = createEvent("entity.twilightforest.alphayeti.pant");
	public static final RegistryObject<SoundEvent> ALPHAYETI_ROAR = createEvent("entity.twilightforest.alphayeti.roar");
	public static final RegistryObject<SoundEvent> ALPHAYETI_THROW = createEvent("entity.twilightforest.alphayeti.throw");
	public static final RegistryObject<SoundEvent> BIGHORN_AMBIENT = createEvent("entity.twilightforest.bighorn.ambient");
	public static final RegistryObject<SoundEvent> BIGHORN_DEATH = createEvent("entity.twilightforest.bighorn.death");
	public static final RegistryObject<SoundEvent> BIGHORN_HURT = createEvent("entity.twilightforest.bighorn.hurt");
	public static final RegistryObject<SoundEvent> BIGHORN_STEP = createEvent("entity.twilightforest.bighorn.step");
	public static final RegistryObject<SoundEvent> BLOCKCHAIN_AMBIENT = createEvent("entity.twilightforest.blockchain.ambient");
	public static final RegistryObject<SoundEvent> BLOCKCHAIN_COLLIDE = createEvent("item.twilightforest.blockchain.collide");
	public static final RegistryObject<SoundEvent> BLOCKCHAIN_DEATH = createEvent("entity.twilightforest.blockchain.death");
	public static final RegistryObject<SoundEvent> BLOCKCHAIN_FIRED = createEvent("item.twilightforest.blockchain.fire");
	public static final RegistryObject<SoundEvent> BLOCKCHAIN_HIT = createEvent("item.twilightforest.blockchain.hit");
	public static final RegistryObject<SoundEvent> BLOCKCHAIN_HURT = createEvent("entity.twilightforest.blockchain.hurt");
	public static final RegistryObject<SoundEvent> BLOCK_ANNIHILATED = createEvent("block.twilightforest.generic.annihilation");
	public static final RegistryObject<SoundEvent> BOAR_AMBIENT = createEvent("entity.twilightforest.boar.ambient");
	public static final RegistryObject<SoundEvent> BOAR_DEATH = createEvent("entity.twilightforest.boar.death");
	public static final RegistryObject<SoundEvent> BOAR_HURT = createEvent("entity.twilightforest.boar.hurt");
	public static final RegistryObject<SoundEvent> BOAR_STEP = createEvent("entity.twilightforest.boar.step");
	public static final RegistryObject<SoundEvent> BRITTLE_FLASK_BREAK = createEvent("item.twilightforest.flask.break");
	public static final RegistryObject<SoundEvent> BRITTLE_FLASK_CRACK = createEvent("item.twilightforest.flask.crack");
	public static final RegistryObject<SoundEvent> BROODLING_AMBIENT = createEvent("entity.twilightforest.broodling.ambient");
	public static final RegistryObject<SoundEvent> BROODLING_DEATH = createEvent("entity.twilightforest.broodling.death");
	public static final RegistryObject<SoundEvent> BROODLING_HURT = createEvent("entity.twilightforest.broodling.hurt");
	public static final RegistryObject<SoundEvent> BROODLING_STEP = createEvent("entity.twilightforest.broodling.step");
	public static final RegistryObject<SoundEvent> BUILDER_OFF = createEvent("block.twilightforest.builder.off");
	public static final RegistryObject<SoundEvent> BUILDER_ON = createEvent("block.twilightforest.builder.on");
	public static final RegistryObject<SoundEvent> BUILDER_REPLACE = createEvent("block.twilightforest.builder.replace");
	public static final RegistryObject<SoundEvent> BUILDER_CREATE = createEvent("block.twilightforest.builder.create");
	public static final RegistryObject<SoundEvent> BUG_SQUISH = createEvent("block.twilightforest.bug.squish");
	public static final RegistryObject<SoundEvent> CARMINITE_GOLEM_ATTACK = createEvent("entity.twilightforest.carminitegolem.attack");
	public static final RegistryObject<SoundEvent> CARMINITE_GOLEM_DEATH = createEvent("entity.twilightforest.carminitegolem.death");
	public static final RegistryObject<SoundEvent> CARMINITE_GOLEM_HURT = createEvent("entity.twilightforest.carminitegolem.hurt");
	public static final RegistryObject<SoundEvent> CARMINITE_GOLEM_STEP = createEvent("entity.twilightforest.carminitegolem.step");
	public static final RegistryObject<SoundEvent> CASKET_OPEN = createEvent("block.twilightforest.casket.open");
	public static final RegistryObject<SoundEvent> CASKET_CLOSE = createEvent("block.twilightforest.casket.close");
	public static final RegistryObject<SoundEvent> CASKET_LOCKED = createEvent("block.twilightforest.casket.locked");
	public static final RegistryObject<SoundEvent> CASKET_REPAIR = createEvent("block.twilightforest.casket.repair");
	public static final RegistryObject<SoundEvent> CHARM_KEEP = createEvent("item.twilightforest.charm.keep");
	public static final RegistryObject<SoundEvent> CHARM_LIFE = createEvent("item.twilightforest.charm.life");
	public static final RegistryObject<SoundEvent> CICADA = createEvent("entity.twilightforest.cicada");
	public static final RegistryObject<SoundEvent> CICADA_FLYING = createEvent("entity.twilightforest.cicada_flying");
	public static final RegistryObject<SoundEvent> DEER_DEATH = createEvent("entity.twilightforest.deer.death");
	public static final RegistryObject<SoundEvent> DEER_HURT = createEvent("entity.twilightforest.deer.hurt");
	public static final RegistryObject<SoundEvent> DEER_AMBIENT = createEvent("entity.twilightforest.deer.ambient");
	public static final RegistryObject<SoundEvent> DWARF_DEATH = createEvent("entity.twilightforest.dwarf_rabbit.death");
	public static final RegistryObject<SoundEvent> DWARF_HURT = createEvent("entity.twilightforest.dwarf_rabbit.hurt");
	public static final RegistryObject<SoundEvent> DWARF_AMBIENT = createEvent("entity.twilightforest.dwarf_rabbit.ambient");
	public static final RegistryObject<SoundEvent> DOOR_ACTIVATED = createEvent("block.twilightforest.door.activate");
	public static final RegistryObject<SoundEvent> DOOR_REAPPEAR = createEvent("block.twilightforest.door.reappear");
	public static final RegistryObject<SoundEvent> DOOR_VANISH = createEvent("block.twilightforest.door.vanish");
	public static final RegistryObject<SoundEvent> FAN_WOOSH = createEvent("item.twilightforest.fan.woosh");
	public static final RegistryObject<SoundEvent> FIRE_BEETLE_DEATH = createEvent("entity.twilightforest.firebeetle.death");
	public static final RegistryObject<SoundEvent> FIRE_BEETLE_HURT = createEvent("entity.twilightforest.firebeetle.hurt");
	public static final RegistryObject<SoundEvent> FIRE_BEETLE_SHOOT = createEvent("entity.twilightforest.firebeetle.shoot");
	public static final RegistryObject<SoundEvent> FIRE_BEETLE_STEP = createEvent("entity.twilightforest.firebeetle.step");
	public static final RegistryObject<SoundEvent> FLASK_FILL = createEvent("item.twilightforest.flask.fill");
	public static final RegistryObject<SoundEvent> GHASTGUARD_AMBIENT = createEvent("entity.twilightforest.ghastguard.ambient");
	public static final RegistryObject<SoundEvent> GHASTGUARD_DEATH = createEvent("entity.twilightforest.ghastguard.death");
	public static final RegistryObject<SoundEvent> GHASTGUARD_HURT = createEvent("entity.twilightforest.ghastguard.hurt");
	public static final RegistryObject<SoundEvent> GHASTGUARD_SHOOT = createEvent("entity.twilightforest.ghastguard.shoot");
	public static final RegistryObject<SoundEvent> GHASTGUARD_WARN = createEvent("entity.twilightforest.ghastguard.warn");
	public static final RegistryObject<SoundEvent> GHASTLING_AMBIENT = createEvent("entity.twilightforest.ghastling.ambient");
	public static final RegistryObject<SoundEvent> GHASTLING_DEATH = createEvent("entity.twilightforest.ghastling.death");
	public static final RegistryObject<SoundEvent> GHASTLING_HURT = createEvent("entity.twilightforest.ghastling.hurt");
	public static final RegistryObject<SoundEvent> GHASTLING_SHOOT = createEvent("entity.twilightforest.ghastling.shoot");
	public static final RegistryObject<SoundEvent> GHASTLING_WARN = createEvent("entity.twilightforest.ghastling.warn");
	public static final RegistryObject<SoundEvent> GHAST_TRAP_ACTIVE = createEvent("block.twilightforest.ghast_trap.active");
	public static final RegistryObject<SoundEvent> GHAST_TRAP_AMBIENT = createEvent("block.twilightforest.ghast_trap.ambient");
	public static final RegistryObject<SoundEvent> GHAST_TRAP_ON = createEvent("block.twilightforest.ghast_trap.on");
	public static final RegistryObject<SoundEvent> GHAST_TRAP_SPINDOWN = createEvent("block.twilightforest.ghast_trap.spindown");
	public static final RegistryObject<SoundEvent> GHAST_TRAP_WARMUP = createEvent("block.twilightforest.ghast_trap.warmup");
	public static final RegistryObject<SoundEvent> GLASS_SWORD_BREAK = createEvent("item.twilightforest.glasssword.break");
	public static final RegistryObject<SoundEvent> GOBLIN_KNIGHT_AMBIENT = createEvent("entity.twilightforest.goblinKnight.ambient");
	public static final RegistryObject<SoundEvent> GOBLIN_KNIGHT_DEATH = createEvent("entity.twilightforest.goblinKnight.death");
	public static final RegistryObject<SoundEvent> GOBLIN_KNIGHT_HURT = createEvent("entity.twilightforest.goblinKnight.hurt");
	public static final RegistryObject<SoundEvent> GOBLIN_KNIGHT_MUFFLED_AMBIENT = createEvent("entity.twilightforest.goblinknight.muffled.ambient");
	public static final RegistryObject<SoundEvent> GOBLIN_KNIGHT_MUFFLED_DEATH = createEvent("entity.twilightforest.goblinknight.muffled.death");
	public static final RegistryObject<SoundEvent> GOBLIN_KNIGHT_MUFFLED_HURT = createEvent("entity.twilightforest.goblinknight.muffled.hurt");
	public static final RegistryObject<SoundEvent> HEDGE_SPIDER_AMBIENT = createEvent("entity.twilightforest.hedgespider.ambient");
	public static final RegistryObject<SoundEvent> HEDGE_SPIDER_DEATH = createEvent("entity.twilightforest.hedgespider.death");
	public static final RegistryObject<SoundEvent> HEDGE_SPIDER_HURT = createEvent("entity.twilightforest.hedgespider.hurt");
	public static final RegistryObject<SoundEvent> HEDGE_SPIDER_STEP = createEvent("entity.twilightforest.hedgespider.step");
	public static final RegistryObject<SoundEvent> HELMET_CRAB_DEATH = createEvent("entity.twilightforest.helmetcrab.death");
	public static final RegistryObject<SoundEvent> HELMET_CRAB_HURT = createEvent("entity.twilightforest.helmetcrab.hurt");
	public static final RegistryObject<SoundEvent> HELMET_CRAB_STEP = createEvent("entity.twilightforest.helmetcrab.step");
	public static final RegistryObject<SoundEvent> HOSTILE_WOLF_DEATH = createEvent("entity.twilightforest.hostilewolf.death");
	public static final RegistryObject<SoundEvent> HOSTILE_WOLF_HURT = createEvent("entity.twilightforest.hostilewolf.hurt");
	public static final RegistryObject<SoundEvent> HOSTILE_WOLF_AMBIENT = createEvent("entity.twilightforest.hostilewolf.ambient");
	public static final RegistryObject<SoundEvent> HOSTILE_WOLF_TARGET = createEvent("entity.twilightforest.hostilewolf.target");
	public static final RegistryObject<SoundEvent> HYDRA_DEATH = createEvent("entity.twilightforest.hydra.death");
	public static final RegistryObject<SoundEvent> HYDRA_GROWL = createEvent("entity.twilightforest.hydra.growl");
	public static final RegistryObject<SoundEvent> HYDRA_HURT = createEvent("entity.twilightforest.hydra.hurt");
	public static final RegistryObject<SoundEvent> HYDRA_ROAR = createEvent("entity.twilightforest.hydra.roar");
	public static final RegistryObject<SoundEvent> HYDRA_SHOOT = createEvent("entity.twilightforest.hydra.shoot");
	public static final RegistryObject<SoundEvent> HYDRA_SHOOT_FIRE = createEvent("entity.twilightforest.hydra.shoot_fire");
	public static final RegistryObject<SoundEvent> HYDRA_WARN = createEvent("entity.twilightforest.hydra.warn");
	public static final RegistryObject<SoundEvent> ICEBOMB_FIRED = createEvent("item.twilightforest.icebomb.fired");
	public static final RegistryObject<SoundEvent> ICE_CORE_AMBIENT = createEvent("entity.twilightforest.ice.noise");
	public static final RegistryObject<SoundEvent> ICE_CORE_DEATH = createEvent("entity.twilightforest.ice.death");
	public static final RegistryObject<SoundEvent> ICE_CORE_HURT = createEvent("entity.twilightforest.ice.hurt");
	public static final RegistryObject<SoundEvent> ICE_CORE_SHOOT = createEvent("entity.twilightforest.ice.shoot");
	public static final RegistryObject<SoundEvent> ICE_GUARDIAN_AMBIENT = createEvent("entity.twilightforest.iceguardian.ambient");
	public static final RegistryObject<SoundEvent> ICE_GUARDIAN_DEATH = createEvent("entity.twilightforest.iceguardian.death");
	public static final RegistryObject<SoundEvent> ICE_GUARDIAN_HURT = createEvent("entity.twilightforest.iceguardian.hurt");
	public static final RegistryObject<SoundEvent> JET_ACTIVE = createEvent("block.twilightforest.jet.active");
	public static final RegistryObject<SoundEvent> JET_POP = createEvent("block.twilightforest.jet.pop");
	public static final RegistryObject<SoundEvent> JET_START = createEvent("block.twilightforest.jet.start");
	public static final RegistryObject<SoundEvent> KING_SPIDER_AMBIENT = createEvent("entity.twilightforest.kingspider.ambient");
	public static final RegistryObject<SoundEvent> KING_SPIDER_DEATH = createEvent("entity.twilightforest.kingspider.death");
	public static final RegistryObject<SoundEvent> KING_SPIDER_HURT = createEvent("entity.twilightforest.kingspider.hurt");
	public static final RegistryObject<SoundEvent> KING_SPIDER_STEP = createEvent("entity.twilightforest.kingspider.step");
	public static final RegistryObject<SoundEvent> KNIGHTMETAL_EQUIP = createEvent("item.twilightforest.armor.equip_knightmetal");
	public static final RegistryObject<SoundEvent> KOBOLD_AMBIENT = createEvent("entity.twilightforest.kobold.ambient");
	public static final RegistryObject<SoundEvent> KOBOLD_DEATH = createEvent("entity.twilightforest.kobold.death");
	public static final RegistryObject<SoundEvent> KOBOLD_HURT = createEvent("entity.twilightforest.kobold.hurt");
	public static final RegistryObject<SoundEvent> KOBOLD_MUNCH = createEvent("entity.twilightforest.kobold.munch");
	public static final RegistryObject<SoundEvent> LAMP_BURN = createEvent("item.twilightforest.lamp.burn");
	public static final RegistryObject<SoundEvent> LICH_AMBIENT = createEvent("entity.twilightforest.lich.ambient");
	public static final RegistryObject<SoundEvent> LICH_CLONE_HURT = createEvent("entity.twilightforest.lichclone.hurt");
	public static final RegistryObject<SoundEvent> LICH_DEATH = createEvent("entity.twilightforest.lich.death");
	public static final RegistryObject<SoundEvent> LICH_HURT = createEvent("entity.twilightforest.lich.hurt");
	public static final RegistryObject<SoundEvent> LICH_POP_MOB = createEvent("entity.twilightforest.lich.pop_mob");
	public static final RegistryObject<SoundEvent> LICH_SHOOT = createEvent("entity.twilightforest.lich.shoot");
	public static final RegistryObject<SoundEvent> LICH_TELEPORT = createEvent("entity.twilightforest.lich.teleport");
	public static final RegistryObject<SoundEvent> LOCKED_VANISHING_BLOCK = createEvent("block.twilightforest.vanish.locked");
	public static final RegistryObject<SoundEvent> LOYAL_ZOMBIE_AMBIENT = createEvent("entity.twilightforest.loyalzombie.ambient");
	public static final RegistryObject<SoundEvent> LOYAL_ZOMBIE_DEATH = createEvent("entity.twilightforest.loyalzombie.death");
	public static final RegistryObject<SoundEvent> LOYAL_ZOMBIE_HURT = createEvent("entity.twilightforest.loyalzombie.hurt");
	public static final RegistryObject<SoundEvent> LOYAL_ZOMBIE_STEP = createEvent("entity.twilightforest.loyalzombie.step");
	public static final RegistryObject<SoundEvent> LOYAL_ZOMBIE_SUMMON = createEvent("entity.twilightforest.loyalzombie.summon");
	public static final RegistryObject<SoundEvent> MAGNET_GRAB = createEvent("item.twilightforest.magnet.grab");
	public static final RegistryObject<SoundEvent> MAZE_SLIME_DEATH = createEvent("entity.twilightforest.mazeslime.death");
	public static final RegistryObject<SoundEvent> MAZE_SLIME_DEATH_SMALL = createEvent("entity.twilightforest.mazeslimesmall.death");
	public static final RegistryObject<SoundEvent> MAZE_SLIME_HURT = createEvent("entity.twilightforest.mazeslime.hurt");
	public static final RegistryObject<SoundEvent> MAZE_SLIME_HURT_SMALL = createEvent("entity.twilightforest.mazeslimesmall.hurt");
	public static final RegistryObject<SoundEvent> MAZE_SLIME_SQUISH = createEvent("entity.twilightforest.mazeslime.squish");
	public static final RegistryObject<SoundEvent> MAZE_SLIME_SQUISH_SMALL = createEvent("entity.twilightforest.mazeslimesmall.squish");
	public static final RegistryObject<SoundEvent> METAL_SHIELD_SHATTERS = createEvent("item.twilightforest.knightmetal_shield.shatter");
	public static final RegistryObject<SoundEvent> MINION_AMBIENT = createEvent("entity.twilightforest.minion.ambient");
	public static final RegistryObject<SoundEvent> MINION_DEATH = createEvent("entity.twilightforest.minion.death");
	public static final RegistryObject<SoundEvent> MINION_HURT = createEvent("entity.twilightforest.minion.hurt");
	public static final RegistryObject<SoundEvent> MINION_STEP = createEvent("entity.twilightforest.minion.step");
	public static final RegistryObject<SoundEvent> MINION_SUMMON = createEvent("entity.twilightforest.minion.summon");
	public static final RegistryObject<SoundEvent> MINOSHROOM_AMBIENT = createEvent("entity.twilightforest.minoshroom.ambient");
	public static final RegistryObject<SoundEvent> MINOSHROOM_ATTACK = createEvent("entity.twilightforest.minoshroom.attack");
	public static final RegistryObject<SoundEvent> MINOSHROOM_DEATH = createEvent("entity.twilightforest.minoshroom.death");
	public static final RegistryObject<SoundEvent> MINOSHROOM_HURT = createEvent("entity.twilightforest.minoshroom.hurt");
	public static final RegistryObject<SoundEvent> MINOSHROOM_SLAM = createEvent("entity.twilightforest.minoshroom.slam");
	public static final RegistryObject<SoundEvent> MINOSHROOM_STEP = createEvent("entity.twilightforest.minoshroom.step");
	public static final RegistryObject<SoundEvent> MINOTAUR_AMBIENT = createEvent("entity.twilightforest.minotaur.ambient");
	public static final RegistryObject<SoundEvent> MINOTAUR_ATTACK = createEvent("entity.twilightforest.minotaur.attack");
	public static final RegistryObject<SoundEvent> MINOTAUR_DEATH = createEvent("entity.twilightforest.minotaur.death");
	public static final RegistryObject<SoundEvent> MINOTAUR_HURT = createEvent("entity.twilightforest.minotaur.hurt");
	public static final RegistryObject<SoundEvent> MINOTAUR_STEP = createEvent("entity.twilightforest.minotaur.step");
	public static final RegistryObject<SoundEvent> MISTWOLF_DEATH = createEvent("entity.twilightforest.mistwolf.death");
	public static final RegistryObject<SoundEvent> MISTWOLF_HURT = createEvent("entity.twilightforest.mistwolf.hurt");
	public static final RegistryObject<SoundEvent> MISTWOLF_AMBIENT = createEvent("entity.twilightforest.mistwolf.ambient");
	public static final RegistryObject<SoundEvent> MISTWOLF_TARGET = createEvent("entity.twilightforest.mistwolf.target");
	public static final RegistryObject<SoundEvent> MOONWORM_SQUISH = createEvent("item.twilightforest.moonworm.squish");
	public static final RegistryObject<SoundEvent> MOSQUITO = createEvent("entity.twilightforest.mosquito.ambient");
	public static final RegistryObject<SoundEvent> NAGA_HISS = createEvent("entity.twilightforest.naga.hiss");
	public static final RegistryObject<SoundEvent> NAGA_HURT = createEvent("entity.twilightforest.naga.hurt");
	public static final RegistryObject<SoundEvent> NAGA_RATTLE = createEvent("entity.twilightforest.naga.rattle");
	public static final RegistryObject<SoundEvent> PEDESTAL_ACTIVATE = createEvent("block.twilightforest.pedestal.activate");
	public static final RegistryObject<SoundEvent> PHANTOM_AMBIENT = createEvent("entity.twilightforest.phantom.ambient");
	public static final RegistryObject<SoundEvent> PHANTOM_DEATH = createEvent("entity.twilightforest.phantom.death");
	public static final RegistryObject<SoundEvent> PHANTOM_HURT = createEvent("entity.twilightforest.phantom.hurt");
	public static final RegistryObject<SoundEvent> PHANTOM_THROW_AXE = createEvent("entity.twilightforest.phantom.axe");
	public static final RegistryObject<SoundEvent> PHANTOM_THROW_PICK = createEvent("entity.twilightforest.phantom.pick");
	public static final RegistryObject<SoundEvent> PICKED_TORCHBERRIES = createEvent("block.twilightforest.torchberry.harvest");
	public static final RegistryObject<SoundEvent> PINCH_BEETLE_DEATH = createEvent("entity.twilightforest.pinchbeetle.death");
	public static final RegistryObject<SoundEvent> PINCH_BEETLE_HURT = createEvent("entity.twilightforest.pinchbeetle.hurt");
	public static final RegistryObject<SoundEvent> PINCH_BEETLE_STEP = createEvent("entity.twilightforest.pinchbeetle.step");
	public static final RegistryObject<SoundEvent> PORTAL_WOOSH = createEvent("block.twilightforest.portal.woosh");
	public static final RegistryObject<SoundEvent> POWDER_USE = createEvent("item.twilightforest.powder.use");
	public static final RegistryObject<SoundEvent> QUEST_RAM_AMBIENT = createEvent("entity.twilightforest.quest.ambient");
	public static final RegistryObject<SoundEvent> QUEST_RAM_DEATH = createEvent("entity.twilightforest.quest.death");
	public static final RegistryObject<SoundEvent> QUEST_RAM_HURT = createEvent("entity.twilightforest.quest.hurt");
	public static final RegistryObject<SoundEvent> QUEST_RAM_STEP = createEvent("entity.twilightforest.quest.step");
	public static final RegistryObject<SoundEvent> RAVEN_CAW = createEvent("entity.twilightforest.raven.caw");
	public static final RegistryObject<SoundEvent> RAVEN_SQUAWK = createEvent("entity.twilightforest.raven.squawk");
	public static final RegistryObject<SoundEvent> REACTOR_AMBIENT = createEvent("block.twilightforest.reactor.ambient");
	public static final RegistryObject<SoundEvent> REAPPEAR_BLOCK = createEvent("block.twilightforest.reappear.reappear");
	public static final RegistryObject<SoundEvent> REAPPEAR_POOF = createEvent("block.twilightforest.reappear.vanish");
	public static final RegistryObject<SoundEvent> REDCAP_AMBIENT = createEvent("entity.twilightforest.redcap.ambient");
	public static final RegistryObject<SoundEvent> REDCAP_DEATH = createEvent("entity.twilightforest.redcap.death");
	public static final RegistryObject<SoundEvent> REDCAP_HURT = createEvent("entity.twilightforest.redcap.hurt");
	public static final RegistryObject<SoundEvent> SCEPTER_DRAIN = createEvent("item.twilightforest.scepter.drain");
	public static final RegistryObject<SoundEvent> SCEPTER_PEARL = createEvent("item.twilightforest.scepter.pearl");
	public static final RegistryObject<SoundEvent> SCEPTER_USE = createEvent("item.twilightforest.scepter.use");
	public static final RegistryObject<SoundEvent> SHIELD_ADD = createEvent("entity.twilightforest.shield.add");
	public static final RegistryObject<SoundEvent> SHIELD_BREAK = createEvent("entity.twilightforest.shield.break");
	public static final RegistryObject<SoundEvent> SKELETON_DRUID_AMBIENT = createEvent("entity.twilightforest.druid.ambient");
	public static final RegistryObject<SoundEvent> SKELETON_DRUID_DEATH = createEvent("entity.twilightforest.druid.death");
	public static final RegistryObject<SoundEvent> SKELETON_DRUID_HURT = createEvent("entity.twilightforest.druid.hurt");
	public static final RegistryObject<SoundEvent> SKELETON_DRUID_SHOOT = createEvent("entity.twilightforest.druid.shoot");
	public static final RegistryObject<SoundEvent> SKELETON_DRUID_STEP = createEvent("entity.twilightforest.druid.step");
	public static final RegistryObject<SoundEvent> SLIDER = createEvent("block.twilightforest.slider");
	public static final RegistryObject<SoundEvent> SLIME_BEETLE_DEATH = createEvent("entity.twilightforest.slimebeetle.death");
	public static final RegistryObject<SoundEvent> SLIME_BEETLE_HURT = createEvent("entity.twilightforest.slimebeetle.hurt");
	public static final RegistryObject<SoundEvent> SLIME_BEETLE_SQUISH_SMALL = createEvent("entity.twilightforest.slimebeetle.squish");
	public static final RegistryObject<SoundEvent> SLIME_BEETLE_STEP = createEvent("entity.twilightforest.slimebeetle.step");
	public static final RegistryObject<SoundEvent> SMOKER_START = createEvent("block.twilightforest.smoker.start");
	public static final RegistryObject<SoundEvent> SNOW_QUEEN_AMBIENT = createEvent("entity.twilightforest.snowqueen.ambient");
	public static final RegistryObject<SoundEvent> SNOW_QUEEN_ATTACK = createEvent("entity.twilightforest.snowqueen.attack");
	public static final RegistryObject<SoundEvent> SNOW_QUEEN_BREAK = createEvent("entity.twilightforest.snowqueen.break");
	public static final RegistryObject<SoundEvent> SNOW_QUEEN_DEATH = createEvent("entity.twilightforest.snowqueen.death");
	public static final RegistryObject<SoundEvent> SNOW_QUEEN_HURT = createEvent("entity.twilightforest.snowqueen.hurt");
	public static final RegistryObject<SoundEvent> SWARM_SPIDER_AMBIENT = createEvent("entity.twilightforest.swarmspider.ambient");
	public static final RegistryObject<SoundEvent> SWARM_SPIDER_DEATH = createEvent("entity.twilightforest.swarmspider.death");
	public static final RegistryObject<SoundEvent> SWARM_SPIDER_HURT = createEvent("entity.twilightforest.swarmspider.hurt");
	public static final RegistryObject<SoundEvent> SWARM_SPIDER_STEP = createEvent("entity.twilightforest.swarmspider.step");
	public static final RegistryObject<SoundEvent> TEAR_BREAK = createEvent("entity.twilightforest.tear.break");
	public static final RegistryObject<SoundEvent> TERMITE_AMBIENT = createEvent("entity.twilightforest.termite.ambient");
	public static final RegistryObject<SoundEvent> TERMITE_DEATH = createEvent("entity.twilightforest.termite.death");
	public static final RegistryObject<SoundEvent> TERMITE_HURT = createEvent("entity.twilightforest.termite.hurt");
	public static final RegistryObject<SoundEvent> TERMITE_STEP = createEvent("entity.twilightforest.termite.step");
	public static final RegistryObject<SoundEvent> TIME_CORE = createEvent("block.twilightforest.core.time");
	public static final RegistryObject<SoundEvent> TINYBIRD_CHIRP = createEvent("entity.twilightforest.tinybird.chirp");
	public static final RegistryObject<SoundEvent> TINYBIRD_HURT = createEvent("entity.twilightforest.tinybird.hurt");
	public static final RegistryObject<SoundEvent> TINYBIRD_SONG = createEvent("entity.twilightforest.tinybird.song");
	public static final RegistryObject<SoundEvent> TINYBIRD_TAKEOFF = createEvent("entity.twilightforest.tinybird.takeoff");
	public static final RegistryObject<SoundEvent> TOME_DEATH = createEvent("entity.twilightforest.tome.death");
	public static final RegistryObject<SoundEvent> TOME_HURT = createEvent("entity.twilightforest.tome.hurt");
	public static final RegistryObject<SoundEvent> TOME_AMBIENT = createEvent("entity.twilightforest.tome.ambient");
	public static final RegistryObject<SoundEvent> TRANSFORMATION_CORE = createEvent("block.twilightforest.core.transformation");
	public static final RegistryObject<SoundEvent> TROLL_THROWS_ROCK = createEvent("entity.twilightforest.troll.throw_rock");
	public static final RegistryObject<SoundEvent> UNCRAFTING_TABLE_ACTIVATE = createEvent("block.twilightforest.uncrafting_table.activate");
	public static final RegistryObject<SoundEvent> UNLOCK_VANISHING_BLOCK = createEvent("block.twilightforest.vanish.unlock");
	public static final RegistryObject<SoundEvent> URGHAST_AMBIENT = createEvent("entity.twilightforest.urghast.ambient");
	public static final RegistryObject<SoundEvent> URGHAST_DEATH = createEvent("entity.twilightforest.urghast.death");
	public static final RegistryObject<SoundEvent> URGHAST_HURT = createEvent("entity.twilightforest.urghast.hurt");
	public static final RegistryObject<SoundEvent> URGHAST_SHOOT = createEvent("entity.twilightforest.urghast.shoot");
	public static final RegistryObject<SoundEvent> URGHAST_WARN = createEvent("entity.twilightforest.urghast.warn");
	public static final RegistryObject<SoundEvent> VANISHING_BLOCK = createEvent("block.twilightforest.vanish.vanish");
	public static final RegistryObject<SoundEvent> WINTER_WOLF_DEATH = createEvent("entity.twilightforest.winterwolf.death");
	public static final RegistryObject<SoundEvent> WINTER_WOLF_HURT = createEvent("entity.twilightforest.winterwolf.hurt");
	public static final RegistryObject<SoundEvent> WINTER_WOLF_AMBIENT = createEvent("entity.twilightforest.winterwolf.ambient");
	public static final RegistryObject<SoundEvent> WINTER_WOLF_SHOOT = createEvent("entity.twilightforest.winterwolf.shoot");
	public static final RegistryObject<SoundEvent> WINTER_WOLF_TARGET = createEvent("entity.twilightforest.winterwolf.target");
	public static final RegistryObject<SoundEvent> WOOD_SHIELD_SHATTERS = createEvent("item.twilightforest.shield.shatter");
	public static final RegistryObject<SoundEvent> WRAITH_AMBIENT = createEvent("entity.twilightforest.wraith.ambient");
	public static final RegistryObject<SoundEvent> WRAITH_DEATH = createEvent("entity.twilightforest.wraith.death");
	public static final RegistryObject<SoundEvent> WRAITH_HURT = createEvent("entity.twilightforest.wraith.hurt");
	public static final RegistryObject<SoundEvent> YETI_DEATH = createEvent("entity.twilightforest.yeti.death");
	public static final RegistryObject<SoundEvent> YETI_GRAB = createEvent("entity.twilightforest.yeti.grab");
	public static final RegistryObject<SoundEvent> YETI_GROWL = createEvent("entity.twilightforest.yeti.growl");
	public static final RegistryObject<SoundEvent> YETI_HURT = createEvent("entity.twilightforest.yeti.hurt");
	public static final RegistryObject<SoundEvent> YETI_THROW = createEvent("entity.twilightforest.yeti.throw");
	
	//Parrot sounds
	public static final RegistryObject<SoundEvent> ALPHAYETI_PARROT = createEvent("entity.twilightforest.alphayeti.parrot");
	public static final RegistryObject<SoundEvent> CARMINITE_GOLEM_PARROT = createEvent("entity.twilightforest.carminitegolem.parrot");
	public static final RegistryObject<SoundEvent> HOSTILE_WOLF_PARROT = createEvent("entity.twilightforest.hostilewolf.parrot");
	public static final RegistryObject<SoundEvent> HYDRA_PARROT = createEvent("entity.twilightforest.hydra.parrot");
	public static final RegistryObject<SoundEvent> ICE_CORE_PARROT = createEvent("entity.twilightforest.icecore.parrot");
	public static final RegistryObject<SoundEvent> KOBOLD_PARROT = createEvent("entity.twilightforest.kobold.parrot");
	public static final RegistryObject<SoundEvent> MINOTAUR_PARROT = createEvent("entity.twilightforest.minotaur.parrot");
	public static final RegistryObject<SoundEvent> MOSQUITO_PARROT = createEvent("entity.twilightforest.mosquito.parrot");
	public static final RegistryObject<SoundEvent> NAGA_PARROT = createEvent("entity.twilightforest.naga.parrot");
	public static final RegistryObject<SoundEvent> REDCAP_PARROT = createEvent("entity.twilightforest.redcap.parrot");
	public static final RegistryObject<SoundEvent> TOME_PARROT = createEvent("entity.twilightforest.tome.parrot");
	public static final RegistryObject<SoundEvent> WRAITH_PARROT = createEvent("entity.twilightforest.wraith.parrot");

	public static final RegistryObject<SoundEvent> MUSIC = createEvent("music.bg");

	public static final RegistryObject<SoundEvent> MUSIC_DISC_RADIANCE = createEvent("music_disc.twilightforest.radiance");
	public static final RegistryObject<SoundEvent> MUSIC_DISC_STEPS = createEvent("music_disc.twilightforest.steps");
	public static final RegistryObject<SoundEvent> MUSIC_DISC_SUPERSTITIOUS = createEvent("music_disc.twilightforest.superstitious");
	public static final RegistryObject<SoundEvent> MUSIC_DISC_HOME = createEvent("music_disc.twilightforest.home");
	public static final RegistryObject<SoundEvent> MUSIC_DISC_WAYFARER = createEvent("music_disc.twilightforest.wayfarer");
	public static final RegistryObject<SoundEvent> MUSIC_DISC_FINDINGS = createEvent("music_disc.twilightforest.findings");
	public static final RegistryObject<SoundEvent> MUSIC_DISC_MAKER = createEvent("music_disc.twilightforest.maker");
	public static final RegistryObject<SoundEvent> MUSIC_DISC_THREAD = createEvent("music_disc.twilightforest.thread");
	public static final RegistryObject<SoundEvent> MUSIC_DISC_MOTION = createEvent("music_disc.twilightforest.motion");

	private static RegistryObject<SoundEvent> createEvent(String sound) {
		return SOUNDS.register(sound, () -> new SoundEvent(TwilightForestMod.prefix(sound)));
	}

	public static void registerParrotSounds() {
		Parrot.MOB_SOUND_MAP.put(TFEntities.ALPHA_YETI.get(), ALPHAYETI_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.BLOCKCHAIN_GOBLIN.get(), REDCAP_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.CARMINITE_BROODLING.get(), SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.CARMINITE_GOLEM.get(), CARMINITE_GOLEM_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.FIRE_BEETLE.get(), SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.CARMINITE_GHASTLING.get(), SoundEvents.PARROT_IMITATE_GHAST);
		Parrot.MOB_SOUND_MAP.put(TFEntities.CARMINITE_GHASTGUARD.get(), SoundEvents.PARROT_IMITATE_GHAST);
		Parrot.MOB_SOUND_MAP.put(TFEntities.HEDGE_SPIDER.get(), SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.HELMET_CRAB.get(), SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.HOSTILE_WOLF.get(), HOSTILE_WOLF_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.HYDRA.get(), HYDRA_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.STABLE_ICE_CORE.get(), ICE_CORE_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.KING_SPIDER.get(), SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.KOBOLD.get(), KOBOLD_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.LICH.get(), SoundEvents.PARROT_IMITATE_BLAZE);
		Parrot.MOB_SOUND_MAP.put(TFEntities.MAZE_SLIME.get(), SoundEvents.PARROT_IMITATE_SLIME);
		Parrot.MOB_SOUND_MAP.put(TFEntities.LICH_MINION.get(), SoundEvents.PARROT_IMITATE_ZOMBIE);
		Parrot.MOB_SOUND_MAP.put(TFEntities.MINOSHROOM.get(), MINOTAUR_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.MINOTAUR.get(), MINOTAUR_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.MIST_WOLF.get(), HOSTILE_WOLF_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.MOSQUITO_SWARM.get(), MOSQUITO_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.NAGA.get(), NAGA_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.KNIGHT_PHANTOM.get(), WRAITH_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.PINCH_BEETLE.get(), SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.REDCAP.get(), REDCAP_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.REDCAP_SAPPER.get(), REDCAP_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.SKELETON_DRUID.get(), SoundEvents.PARROT_IMITATE_SKELETON);
		Parrot.MOB_SOUND_MAP.put(TFEntities.SLIME_BEETLE.get(), SoundEvents.PARROT_IMITATE_SLIME);
		Parrot.MOB_SOUND_MAP.put(TFEntities.SNOW_GUARDIAN.get(), ICE_CORE_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.SNOW_QUEEN.get(), ICE_CORE_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.SWARM_SPIDER.get(), SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.TOWERWOOD_BORER.get(), SoundEvents.PARROT_IMITATE_SILVERFISH);
		Parrot.MOB_SOUND_MAP.put(TFEntities.DEATH_TOME.get(), TOME_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.UR_GHAST.get(), SoundEvents.PARROT_IMITATE_GHAST);
		Parrot.MOB_SOUND_MAP.put(TFEntities.WINTER_WOLF.get(), HOSTILE_WOLF_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.WRAITH.get(), WRAITH_PARROT.get());
		Parrot.MOB_SOUND_MAP.put(TFEntities.YETI.get(), ALPHAYETI_PARROT.get());
	}
}
