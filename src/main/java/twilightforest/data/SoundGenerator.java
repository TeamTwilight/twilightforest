package twilightforest.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import twilightforest.TwilightForestMod;
import twilightforest.data.helpers.TFSoundProvider;
import twilightforest.init.TFSounds;

public class SoundGenerator extends TFSoundProvider {

	public SoundGenerator(PackOutput output, ExistingFileHelper helper) {
		super(output, helper);
	}

	@Override
	public void registerSounds() {
		this.generateExistingSoundWithSubtitle(TFSounds.ACID_RAIN_BURNS, SoundEvents.FIRE_EXTINGUISH);
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_ALERT, "mob/alpha_yeti/alert", 1);
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_DEATH, "mob/alpha_yeti/death", 1);
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_GRAB, "mob/alpha_yeti/grab", 1);
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_GROWL, "mob/alpha_yeti/growl", 3);
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_HURT, "mob/alpha_yeti/hurt", 3);
		this.generateExistingSoundWithSubtitle(TFSounds.ALPHA_YETI_ICE, SoundEvents.ARROW_SHOOT);
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_PANT, "mob/alpha_yeti/pant", 3);
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_ROAR, "mob/alpha_yeti/roar", 1);
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_THROW, "mob/alpha_yeti/throw", 1);

		this.generateExistingSoundWithSubtitle(TFSounds.BIGHORN_SHEEP_AMBIENT, SoundEvents.SHEEP_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.BIGHORN_SHEEP_DEATH, SoundEvents.SHEEP_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.BIGHORN_SHEEP_HURT, SoundEvents.SHEEP_HURT);
		this.makeStepSound(TFSounds.BIGHORN_SHEEP_STEP, SoundEvents.SHEEP_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.BLOCKCHAIN_GOBLIN_AMBIENT, "mob/redcap/redcap", 6);
		this.generateNewSoundWithSubtitle(TFSounds.BLOCKCHAIN_GOBLIN_DEATH, "mob/redcap/die", 3);
		this.generateNewSoundWithSubtitle(TFSounds.BLOCKCHAIN_GOBLIN_HURT, "mob/redcap/hurt", 4);

		this.generateExistingSoundWithSubtitle(TFSounds.BOAR_AMBIENT, SoundEvents.PIG_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.BOAR_DEATH, SoundEvents.PIG_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.BOAR_HURT, SoundEvents.PIG_HURT);
		this.makeStepSound(TFSounds.BOAR_STEP, SoundEvents.PIG_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_BROODLING_AMBIENT, SoundEvents.SPIDER_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_BROODLING_DEATH, SoundEvents.SPIDER_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_BROODLING_HURT, SoundEvents.SPIDER_HURT);
		this.makeStepSound(TFSounds.CARMINITE_BROODLING_STEP, SoundEvents.SPIDER_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTGUARD_AMBIENT, SoundEvents.GHAST_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTGUARD_DEATH, SoundEvents.GHAST_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTGUARD_HURT, SoundEvents.GHAST_HURT);
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTGUARD_SHOOT, SoundEvents.GHAST_SHOOT);
		this.generateSoundWithCustomSubtitle(TFSounds.CARMINITE_GHASTGUARD_WARN, SoundEvents.GHAST_WARN, "subtitles.twilightforest.entity.carminite_ghastguard.shoot");

		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTLING_AMBIENT, SoundEvents.GHAST_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTLING_DEATH, SoundEvents.GHAST_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTLING_HURT, SoundEvents.GHAST_HURT);
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTLING_SHOOT, SoundEvents.GHAST_SHOOT);
		this.generateSoundWithCustomSubtitle(TFSounds.CARMINITE_GHASTLING_WARN, SoundEvents.GHAST_WARN, "subtitles.twilightforest.entity.carminite_ghastling.shoot");

		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GOLEM_ATTACK, SoundEvents.IRON_GOLEM_ATTACK);
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GOLEM_DEATH, SoundEvents.IRON_GOLEM_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GOLEM_HURT, SoundEvents.IRON_GOLEM_HURT);
		this.makeStepSound(TFSounds.CARMINITE_GOLEM_STEP, SoundEvents.IRON_GOLEM_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.DEATH_TOME_AMBIENT, "mob/tome/idle", 2);
		this.generateNewSoundWithSubtitle(TFSounds.DEATH_TOME_DEATH, "mob/tome/death", 1);
		this.generateNewSoundWithSubtitle(TFSounds.DEATH_TOME_HURT, "mob/tome/hurt", 3);

		this.generateNewSoundWithSubtitle(TFSounds.DEER_AMBIENT, "mob/deer/idle", 3);
		this.generateNewSoundWithSubtitle(TFSounds.DEER_DEATH, "mob/deer/death", 1);
		this.generateNewSoundWithSubtitle(TFSounds.DEER_HURT, "mob/deer/hurt", 2);

		this.generateExistingSoundWithSubtitle(TFSounds.DWARF_RABBIT_AMBIENT, SoundEvents.RABBIT_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.DWARF_RABBIT_DEATH, SoundEvents.RABBIT_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.DWARF_RABBIT_HURT, SoundEvents.RABBIT_HURT);

		this.generateExistingSoundWithSubtitle(TFSounds.FIRE_BEETLE_DEATH, SoundEvents.SPIDER_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.FIRE_BEETLE_HURT, SoundEvents.SPIDER_HURT);
		this.generateExistingSoundWithSubtitle(TFSounds.FIRE_BEETLE_SHOOT, SoundEvents.GHAST_SHOOT);
		this.makeStepSound(TFSounds.FIRE_BEETLE_STEP, SoundEvents.SPIDER_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.GOBLIN_KNIGHT_AMBIENT, "mob/redcap/redcap", 6);
		this.generateNewSoundWithSubtitle(TFSounds.GOBLIN_KNIGHT_DEATH, "mob/redcap/die", 3);
		this.generateNewSoundWithSubtitle(TFSounds.GOBLIN_KNIGHT_HURT, "mob/redcap/hurt", 4);

		this.generateNewSoundWithSubtitle(TFSounds.GOBLIN_KNIGHT_MUFFLED_AMBIENT, "mob/redcap/muffled/redcap", 6);
		this.generateNewSoundWithSubtitle(TFSounds.GOBLIN_KNIGHT_MUFFLED_DEATH, "mob/redcap/muffled/die", 3);
		this.generateNewSoundWithSubtitle(TFSounds.GOBLIN_KNIGHT_MUFFLED_HURT, "mob/redcap/muffled/hurt", 4);

		this.generateExistingSoundWithSubtitle(TFSounds.HEDGE_SPIDER_AMBIENT, SoundEvents.SPIDER_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.HEDGE_SPIDER_DEATH, SoundEvents.SPIDER_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.HEDGE_SPIDER_HURT, SoundEvents.SPIDER_HURT);
		this.makeStepSound(TFSounds.HEDGE_SPIDER_STEP, SoundEvents.SPIDER_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.HELMET_CRAB_DEATH, SoundEvents.SPIDER_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.HELMET_CRAB_HURT, SoundEvents.SPIDER_HURT);
		this.makeStepSound(TFSounds.HELMET_CRAB_STEP, SoundEvents.SPIDER_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.HOSTILE_WOLF_AMBIENT, "mob/mist_wolf/idle", 3);
		this.generateExistingSoundWithSubtitle(TFSounds.HOSTILE_WOLF_DEATH, SoundEvents.WOLF_DEATH);
		this.generateNewSoundWithSubtitle(TFSounds.HOSTILE_WOLF_HURT, "mob/mist_wolf/hurt", 2);
		this.generateNewSoundWithSubtitle(TFSounds.HOSTILE_WOLF_TARGET, "mob/mist_wolf/target", 1);

		this.generateNewSoundWithSubtitle(TFSounds.HYDRA_DEATH, "mob/hydra/death", 1);
		this.generateNewSoundWithSubtitle(TFSounds.HYDRA_GROWL, "mob/hydra/growl", 3);
		this.generateNewSoundWithSubtitle(TFSounds.HYDRA_HURT, "mob/hydra/hurt", 4);
		this.generateNewSoundWithSubtitle(TFSounds.HYDRA_ROAR, "mob/hydra/roar", 2);
		this.generateExistingSoundWithSubtitle(TFSounds.HYDRA_SHOOT, SoundEvents.GHAST_SHOOT);
		this.generateExistingSoundWithSubtitle(TFSounds.HYDRA_SHOOT_FIRE, SoundEvents.GHAST_SHOOT);
		this.generateNewSoundWithSubtitle(TFSounds.HYDRA_WARN, "mob/hydra/warn", 1);

		this.generateNewSoundWithSubtitle(TFSounds.ICE_CORE_AMBIENT, "mob/ice/crackle", 2);
		this.generateNewSoundWithSubtitle(TFSounds.ICE_CORE_DEATH, "mob/ice/death", 2);
		this.generateNewSoundWithSubtitle(TFSounds.ICE_CORE_HURT, "mob/ice/hurt", 2);
		this.generateExistingSoundWithSubtitle(TFSounds.ICE_CORE_SHOOT, SoundEvents.SNOWBALL_THROW);

		this.generateExistingSoundWithSubtitle(TFSounds.KING_SPIDER_AMBIENT, SoundEvents.SPIDER_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.KING_SPIDER_DEATH, SoundEvents.SPIDER_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.KING_SPIDER_HURT, SoundEvents.SPIDER_HURT);
		this.makeStepSound(TFSounds.KING_SPIDER_STEP, SoundEvents.SPIDER_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.KNIGHT_PHANTOM_AMBIENT, "mob/wraith/wraith", 4);
		this.generateNewSoundWithSubtitle(TFSounds.KNIGHT_PHANTOM_DEATH, "mob/wraith/wraith", 4);
		this.generateNewSoundWithSubtitle(TFSounds.KNIGHT_PHANTOM_HURT, "mob/wraith/wraith", 4);
		this.generateExistingSoundWithSubtitle(TFSounds.KNIGHT_PHANTOM_THROW_AXE, SoundEvents.ARROW_SHOOT);
		this.generateExistingSoundWithSubtitle(TFSounds.KNIGHT_PHANTOM_THROW_PICK, SoundEvents.ARROW_SHOOT);

		this.generateNewSoundWithSubtitle(TFSounds.KOBOLD_AMBIENT, "mob/kobold/ambient", 6);
		this.generateNewSoundWithSubtitle(TFSounds.KOBOLD_DEATH, "mob/kobold/death", 3);
		this.generateNewSoundWithSubtitle(TFSounds.KOBOLD_HURT, "mob/kobold/hurt", 3);
		this.generateExistingSoundWithSubtitle(TFSounds.KOBOLD_MUNCH, SoundEvents.GENERIC_EAT);

		this.generateExistingSoundWithSubtitle(TFSounds.LICH_AMBIENT, SoundEvents.BLAZE_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.LICH_CLONE_HURT, SoundEvents.FIRE_EXTINGUISH);
		this.generateExistingSoundWithSubtitle(TFSounds.LICH_DEATH, SoundEvents.BLAZE_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.LICH_HURT, SoundEvents.BLAZE_HURT);
		this.generateExistingSoundWithSubtitle(TFSounds.LICH_POP_MOB, SoundEvents.CHICKEN_EGG);
		this.generateExistingSoundWithSubtitle(TFSounds.LICH_SHOOT, SoundEvents.GHAST_SHOOT);
		this.generateExistingSoundWithSubtitle(TFSounds.LICH_TELEPORT, SoundEvents.CHORUS_FRUIT_TELEPORT);

		this.generateExistingSoundWithSubtitle(TFSounds.LOYAL_ZOMBIE_AMBIENT, SoundEvents.ZOMBIE_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.LOYAL_ZOMBIE_DEATH, SoundEvents.ZOMBIE_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.LOYAL_ZOMBIE_HURT, SoundEvents.ZOMBIE_HURT);
		this.makeStepSound(TFSounds.LOYAL_ZOMBIE_STEP, SoundEvents.ZOMBIE_STEP);
		this.generateExistingSoundWithSubtitle(TFSounds.LOYAL_ZOMBIE_SUMMON, SoundEvents.ITEM_PICKUP);

		this.generateExistingSoundWithSubtitle(TFSounds.MAZE_SLIME_DEATH, SoundEvents.SLIME_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.MAZE_SLIME_HURT, SoundEvents.SLIME_HURT);
		this.generateExistingSoundWithSubtitle(TFSounds.MAZE_SLIME_SQUISH, SoundEvents.SLIME_SQUISH);

		this.generateSoundWithCustomSubtitle(TFSounds.MAZE_SLIME_DEATH_SMALL, SoundEvents.SLIME_DEATH_SMALL, "subtitles.twilightforest.entity.maze_slime.death");
		this.generateSoundWithCustomSubtitle(TFSounds.MAZE_SLIME_HURT_SMALL, SoundEvents.SLIME_HURT_SMALL, "subtitles.twilightforest.entity.maze_slime.hurt");
		this.generateSoundWithCustomSubtitle(TFSounds.MAZE_SLIME_SQUISH_SMALL, SoundEvents.SLIME_SQUISH_SMALL, "subtitles.twilightforest.entity.maze_slime.squish");

		this.generateExistingSoundWithSubtitle(TFSounds.MINION_AMBIENT, SoundEvents.ZOMBIE_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.MINION_DEATH, SoundEvents.ZOMBIE_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.MINION_HURT, SoundEvents.ZOMBIE_HURT);
		this.makeStepSound(TFSounds.MINION_STEP, SoundEvents.ZOMBIE_STEP);
		this.generateExistingSoundWithSubtitle(TFSounds.MINION_SUMMON, SoundEvents.ITEM_PICKUP);

		this.generateExistingSoundWithSubtitle(TFSounds.MINOSHROOM_AMBIENT, SoundEvents.COW_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.MINOSHROOM_ATTACK, SoundEvents.IRON_GOLEM_ATTACK);
		this.generateExistingSoundWithSubtitle(TFSounds.MINOSHROOM_DEATH, SoundEvents.COW_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.MINOSHROOM_HURT, SoundEvents.COW_HURT);
		this.generateExistingSoundWithSubtitle(TFSounds.MINOSHROOM_SLAM, SoundEvents.LIGHTNING_BOLT_IMPACT);
		this.makeStepSound(TFSounds.MINOSHROOM_STEP, SoundEvents.COW_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.MINOTAUR_AMBIENT, SoundEvents.COW_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.MINOTAUR_ATTACK, SoundEvents.IRON_GOLEM_ATTACK);
		this.generateExistingSoundWithSubtitle(TFSounds.MINOTAUR_DEATH, SoundEvents.COW_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.MINOTAUR_HURT, SoundEvents.COW_HURT);
		this.makeStepSound(TFSounds.MINOTAUR_STEP, SoundEvents.COW_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.MIST_WOLF_AMBIENT, "mob/mist_wolf/idle", 3);
		this.generateExistingSoundWithSubtitle(TFSounds.MIST_WOLF_DEATH, SoundEvents.WOLF_DEATH);
		this.generateNewSoundWithSubtitle(TFSounds.MIST_WOLF_HURT, "mob/mist_wolf/hurt", 2);
		this.generateNewSoundWithSubtitle(TFSounds.MIST_WOLF_TARGET, "mob/mist_wolf/target", 1);

		this.generateNewSoundWithSubtitle(TFSounds.MOSQUITO, "mob/mosquito/animals132", 1);

		this.generateNewSoundWithSubtitle(TFSounds.NAGA_HISS, "mob/naga/hiss", 3);
		this.generateNewSoundWithSubtitle(TFSounds.NAGA_HURT, "mob/naga/hurt", 3);
		this.generateNewSoundWithSubtitle(TFSounds.NAGA_RATTLE, "mob/naga/rattle", 2);

		this.generateExistingSoundWithSubtitle(TFSounds.PINCH_BEETLE_DEATH, SoundEvents.SPIDER_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.PINCH_BEETLE_HURT, SoundEvents.SPIDER_HURT);
		this.makeStepSound(TFSounds.PINCH_BEETLE_STEP, SoundEvents.SPIDER_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.QUEST_RAM_AMBIENT, SoundEvents.SHEEP_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.QUEST_RAM_DEATH, SoundEvents.SHEEP_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.QUEST_RAM_HURT, SoundEvents.SHEEP_HURT);
		this.makeStepSound(TFSounds.QUEST_RAM_STEP, SoundEvents.SHEEP_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.RAVEN_CAW, "mob/raven/caw", 2);
		this.generateNewSoundWithSubtitle(TFSounds.RAVEN_SQUAWK, "mob/raven/squawk", 2);

		this.generateNewSoundWithSubtitle(TFSounds.REDCAP_AMBIENT, "mob/redcap/redcap", 6);
		this.generateNewSoundWithSubtitle(TFSounds.REDCAP_DEATH, "mob/redcap/die", 3);
		this.generateNewSoundWithSubtitle(TFSounds.REDCAP_HURT, "mob/redcap/hurt", 4);

		this.generateExistingSoundWithSubtitle(TFSounds.SHIELD_ADD, SoundEvents.CHICKEN_EGG);
		this.generateExistingSoundWithSubtitle(TFSounds.SHIELD_BREAK, SoundEvents.ITEM_BREAK);
		this.generateExistingSoundWithSubtitle(TFSounds.SHIELD_BLOCK, SoundEvents.TRIDENT_HIT);

		this.generateExistingSoundWithSubtitle(TFSounds.SKELETON_DRUID_AMBIENT, SoundEvents.STRAY_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.SKELETON_DRUID_DEATH, SoundEvents.STRAY_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.SKELETON_DRUID_HURT, SoundEvents.STRAY_HURT);
		this.generateExistingSoundWithSubtitle(TFSounds.SKELETON_DRUID_SHOOT, SoundEvents.GHAST_SHOOT);
		this.makeStepSound(TFSounds.SKELETON_DRUID_STEP, SoundEvents.SKELETON_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.SLIME_BEETLE_DEATH, SoundEvents.SPIDER_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.SLIME_BEETLE_HURT, SoundEvents.SPIDER_HURT);
		this.generateExistingSoundWithSubtitle(TFSounds.SLIME_BEETLE_SQUISH, SoundEvents.SLIME_SQUISH_SMALL);
		this.makeStepSound(TFSounds.SLIME_BEETLE_STEP, SoundEvents.SPIDER_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.SNOW_GUARDIAN_AMBIENT, "mob/ice/crackle", 2);
		this.generateNewSoundWithSubtitle(TFSounds.SNOW_GUARDIAN_DEATH, "mob/ice/death", 2);
		this.generateNewSoundWithSubtitle(TFSounds.SNOW_GUARDIAN_HURT, "mob/ice/hurt", 2);

		this.generateNewSoundWithSubtitle(TFSounds.SNOW_QUEEN_AMBIENT, "mob/ice/crackle", 2);
		this.generateExistingSoundWithSubtitle(TFSounds.SNOW_QUEEN_ATTACK, SoundEvents.IRON_GOLEM_ATTACK);
		this.generateExistingSoundWithSubtitle(TFSounds.SNOW_QUEEN_BREAK, SoundEvents.ITEM_BREAK);
		this.generateNewSoundWithSubtitle(TFSounds.SNOW_QUEEN_DEATH, "mob/ice/death", 2);
		this.generateNewSoundWithSubtitle(TFSounds.SNOW_QUEEN_HURT, "mob/ice/hurt", 2);

		this.generateExistingSoundWithSubtitle(TFSounds.SWARM_SPIDER_AMBIENT, SoundEvents.SPIDER_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.SWARM_SPIDER_DEATH, SoundEvents.SPIDER_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.SWARM_SPIDER_HURT, SoundEvents.SPIDER_HURT);
		this.makeStepSound(TFSounds.SWARM_SPIDER_STEP, SoundEvents.SPIDER_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.TEAR_BREAK, SoundEvents.GLASS_BREAK);

		this.generateNewSoundWithSubtitle(TFSounds.TINY_BIRD_CHIRP, "mob/tiny_bird/chirp", 3);
		this.generateNewSoundWithSubtitle(TFSounds.TINY_BIRD_HURT, "mob/tiny_bird/hurt", 2);
		this.generateNewSoundWithSubtitle(TFSounds.TINY_BIRD_SONG, "mob/tiny_bird/song", 2);
		this.generateExistingSoundWithSubtitle(TFSounds.TINY_BIRD_TAKEOFF, SoundEvents.BAT_TAKEOFF);

		this.generateExistingSoundWithSubtitle(TFSounds.TOWERWOOD_BORER_AMBIENT, SoundEvents.SILVERFISH_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.TOWERWOOD_BORER_DEATH, SoundEvents.SILVERFISH_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.TOWERWOOD_BORER_HURT, SoundEvents.SILVERFISH_HURT);
		this.makeStepSound(TFSounds.TOWERWOOD_BORER_STEP, SoundEvents.SILVERFISH_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.TROLL_THROWS_ROCK, SoundEvents.ARROW_SHOOT);

		this.generateExistingSoundWithSubtitle(TFSounds.UR_GHAST_AMBIENT, SoundEvents.GHAST_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.UR_GHAST_DEATH, SoundEvents.GHAST_DEATH);
		this.generateExistingSoundWithSubtitle(TFSounds.UR_GHAST_HURT, SoundEvents.GHAST_HURT);
		this.generateExistingSoundWithSubtitle(TFSounds.UR_GHAST_SHOOT, SoundEvents.GHAST_SHOOT);
		this.generateExistingSoundWithSubtitle(TFSounds.UR_GHAST_TANTRUM, SoundEvents.GHAST_HURT);
		this.generateSoundWithCustomSubtitle(TFSounds.UR_GHAST_WARN, SoundEvents.GHAST_WARN, "subtitles.twilightforest.entity.ur_ghast.shoot");

		this.generateNewSoundWithSubtitle(TFSounds.WINTER_WOLF_AMBIENT, "mob/mist_wolf/idle", 3);
		this.generateExistingSoundWithSubtitle(TFSounds.WINTER_WOLF_DEATH, SoundEvents.WOLF_DEATH);
		this.generateNewSoundWithSubtitle(TFSounds.WINTER_WOLF_HURT, "mob/mist_wolf/hurt", 2);
		this.generateExistingSoundWithSubtitle(TFSounds.WINTER_WOLF_SHOOT, SoundEvents.GHAST_SHOOT);
		this.generateNewSoundWithSubtitle(TFSounds.WINTER_WOLF_TARGET, "mob/mist_wolf/target", 1);

		this.generateNewSoundWithSubtitle(TFSounds.WRAITH_AMBIENT, "mob/wraith/wraith", 4);
		this.generateNewSoundWithSubtitle(TFSounds.WRAITH_DEATH, "mob/wraith/wraith", 4);
		this.generateNewSoundWithSubtitle(TFSounds.WRAITH_HURT, "mob/wraith/wraith", 4);

		this.generateNewSoundWithSubtitle(TFSounds.YETI_DEATH, "mob/alpha_yeti/death", 1);
		this.generateNewSoundWithSubtitle(TFSounds.YETI_GRAB, "mob/alpha_yeti/grab", 1);
		this.generateNewSoundWithSubtitle(TFSounds.YETI_GROWL, "mob/alpha_yeti/growl", 3);
		this.generateNewSoundWithSubtitle(TFSounds.YETI_HURT, "mob/alpha_yeti/hurt", 3);
		this.generateNewSoundWithSubtitle(TFSounds.YETI_THROW, "mob/alpha_yeti/throw", 1);


		this.generateParrotSound(TFSounds.ALPHA_YETI_PARROT, TFSounds.ALPHA_YETI_GROWL.get());
		this.generateParrotSound(TFSounds.CARMINITE_GOLEM_PARROT, TFSounds.CARMINITE_GOLEM_HURT.get());
		this.generateParrotSound(TFSounds.DEATH_TOME_PARROT, TFSounds.DEATH_TOME_AMBIENT.get());
		this.generateParrotSound(TFSounds.HOSTILE_WOLF_PARROT, TFSounds.HOSTILE_WOLF_AMBIENT.get());
		this.generateParrotSound(TFSounds.HYDRA_PARROT, TFSounds.HYDRA_GROWL.get());
		this.generateParrotSound(TFSounds.ICE_CORE_PARROT, TFSounds.ICE_CORE_AMBIENT.get());
		this.generateParrotSound(TFSounds.KOBOLD_PARROT, TFSounds.KOBOLD_AMBIENT.get());
		this.generateParrotSound(TFSounds.MINOTAUR_PARROT, TFSounds.MINOTAUR_AMBIENT.get());
		this.generateParrotSound(TFSounds.MOSQUITO_PARROT, TFSounds.MOSQUITO.get());
		this.generateParrotSound(TFSounds.NAGA_PARROT, TFSounds.NAGA_HISS.get());
		this.generateParrotSound(TFSounds.REDCAP_PARROT, TFSounds.REDCAP_AMBIENT.get());
		this.generateParrotSound(TFSounds.WRAITH_PARROT, TFSounds.WRAITH_AMBIENT.get());


		this.generateNewSoundWithSubtitle(TFSounds.BEANSTALK_GROWTH, "random/beanstalk_grow", 1);
		this.generateExistingSoundWithSubtitle(TFSounds.BLOCK_ANNIHILATED, SoundEvents.FIRE_EXTINGUISH);
		this.generateExistingSoundWithSubtitle(TFSounds.BOSS_CHEST_APPEAR, SoundEvents.EVOKER_PREPARE_SUMMON);
		this.generateExistingSoundWithSubtitle(TFSounds.BUG_SQUISH, SoundEvents.SLIME_SQUISH_SMALL);
		this.generateExistingSoundWithSubtitle(TFSounds.BUILDER_CREATE, SoundEvents.COMPARATOR_CLICK);
		this.generateExistingSoundWithSubtitle(TFSounds.BUILDER_OFF, SoundEvents.COMPARATOR_CLICK);
		this.generateExistingSoundWithSubtitle(TFSounds.BUILDER_ON, SoundEvents.COMPARATOR_CLICK);
		this.generateExistingSoundWithSubtitle(TFSounds.BUILDER_REPLACE, SoundEvents.ITEM_PICKUP);
		this.generateNewSoundWithSubtitle(TFSounds.CASKET_CLOSE, "random/casket/close", 1);
		this.generateExistingSoundWithSubtitle(TFSounds.CASKET_LOCKED, SoundEvents.CHEST_LOCKED);
		this.generateNewSoundWithSubtitle(TFSounds.CASKET_OPEN, "random/casket/open", 1);
		this.generateNewSoundWithSubtitle(TFSounds.CASKET_REPAIR, "random/casket/repair", 1);
		this.generateNewSoundWithSubtitle(TFSounds.CICADA, "mob/cicada", 2);
		this.generateExistingSoundWithSubtitle(TFSounds.DOOR_ACTIVATED, SoundEvents.COMPARATOR_CLICK);
		this.generateExistingSoundWithSubtitle(TFSounds.DOOR_REAPPEAR, SoundEvents.FIRE_EXTINGUISH);
		this.generateExistingSoundWithSubtitle(TFSounds.DOOR_VANISH, SoundEvents.FIRE_EXTINGUISH);
		this.generateExistingSoundWithSubtitle(TFSounds.GHAST_TRAP_AMBIENT, SoundEvents.NOTE_BLOCK_HARP.value());
		this.generateNewSoundWithSubtitle(TFSounds.GHAST_TRAP_ON, "mob/ur_ghast/trap_on", 5);
		this.generateNewSoundWithSubtitle(TFSounds.GHAST_TRAP_SPINDOWN, "mob/ur_ghast/trap_spin_down", 1);
		this.generateNewSoundWithSubtitle(TFSounds.GHAST_TRAP_WARMUP, "mob/ur_ghast/trap_warmup", 1);
		this.generateExistingSoundWithSubtitle(TFSounds.JET_ACTIVE, SoundEvents.GHAST_SHOOT);
		this.generateExistingSoundWithSubtitle(TFSounds.JET_POP, SoundEvents.LAVA_POP);
		this.generateExistingSoundWithSubtitle(TFSounds.JET_START, SoundEvents.COMPARATOR_CLICK);
		this.generateExistingSoundWithSubtitle(TFSounds.LOCKED_VANISHING_BLOCK, SoundEvents.COMPARATOR_CLICK);
		this.generateExistingSoundWithSubtitle(TFSounds.PEDESTAL_ACTIVATE, SoundEvents.ZOMBIE_INFECT);
		this.generateExistingSoundWithSubtitle(TFSounds.PICKED_TORCHBERRIES, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES);
		this.generateExistingSoundWithSubtitle(TFSounds.PORTAL_WHOOSH, SoundEvents.PORTAL_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.REACTOR_AMBIENT, SoundEvents.PORTAL_AMBIENT);
		this.generateExistingSoundWithSubtitle(TFSounds.REAPPEAR_BLOCK, SoundEvents.ITEM_PICKUP);
		this.generateExistingSoundWithSubtitle(TFSounds.REAPPEAR_POOF, SoundEvents.ITEM_PICKUP);
		this.generateNewSoundWithSubtitle(TFSounds.SLIDER, "random/creakgo2", 1);
		this.generateExistingSoundWithSubtitle(TFSounds.SMOKER_START, SoundEvents.COMPARATOR_CLICK);
		this.generateExistingSoundWithSubtitle(TFSounds.TIME_CORE, SoundEvents.COMPARATOR_CLICK);
		this.generateExistingSoundWithSubtitle(TFSounds.TRANSFORMATION_CORE, SoundEvents.NOTE_BLOCK_HARP.value());
		this.generateExistingSound(TFSounds.UNCRAFTING_TABLE_ACTIVATE, SoundEvents.END_PORTAL_SPAWN, false);
		this.generateExistingSoundWithSubtitle(TFSounds.UNLOCK_VANISHING_BLOCK, SoundEvents.COMPARATOR_CLICK);
		this.generateExistingSoundWithSubtitle(TFSounds.VANISHING_BLOCK, SoundEvents.ITEM_PICKUP);
		this.generateNewSoundWithSubtitle(TFSounds.WROUGHT_IRON_FENCE_EXTENDED, "random/casket/repair", 1);

		this.generateExistingSoundWithSubtitle(TFSounds.BLOCK_AND_CHAIN_COLLIDE, SoundEvents.ANVIL_LAND);
		this.generateExistingSoundWithSubtitle(TFSounds.BLOCK_AND_CHAIN_FIRED, SoundEvents.ARROW_SHOOT);
		this.generateExistingSoundWithSubtitle(TFSounds.BLOCK_AND_CHAIN_HIT, SoundEvents.IRON_GOLEM_ATTACK);
		this.generateExistingSoundWithSubtitle(TFSounds.BRITTLE_FLASK_BREAK, SoundEvents.GLASS_BREAK);
		this.generateExistingSoundWithSubtitle(TFSounds.BRITTLE_FLASK_CRACK, SoundEvents.GLASS_BREAK);
		this.generateExistingSoundWithSubtitle(TFSounds.CHARM_KEEP, SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
		this.generateExistingSoundWithSubtitle(TFSounds.CHARM_LIFE, SoundEvents.TOTEM_USE);
		this.generateNewSoundMC(TFSounds.FAN_WHOOSH, "random/breath", 1, true);
		this.generateExistingSoundWithSubtitle(TFSounds.FLASK_FILL, SoundEvents.BREWING_STAND_BREW);
		this.generateExistingSoundWithSubtitle(TFSounds.GLASS_SWORD_BREAK, SoundEvents.GLASS_BREAK);
		this.generateExistingSoundWithSubtitle(TFSounds.ICE_BOMB_FIRED, SoundEvents.ARROW_SHOOT);
		this.generateExistingSoundWithSubtitle(TFSounds.KNIGHTMETAL_EQUIP, SoundEvents.ARMOR_EQUIP_NETHERITE);
		this.generateExistingSoundWithSubtitle(TFSounds.LAMP_BURN, SoundEvents.GHAST_SHOOT);
		this.generateExistingSoundWithSubtitle(TFSounds.MAGNET_GRAB, SoundEvents.CHORUS_FRUIT_TELEPORT);
		this.generateExistingSoundWithSubtitle(TFSounds.METAL_SHIELD_SHATTERS, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR);
		this.generateExistingSoundWithSubtitle(TFSounds.MOONWORM_SQUISH, SoundEvents.SLIME_SQUISH_SMALL);
		this.generateNewSoundMC(TFSounds.ORE_METER_CLEAR, "block/crafter/craft", 1, true);
		this.generateNewSoundMC(TFSounds.ORE_METER_CRACKLE, "ambient/nether/basalt_deltas/click", 5, true);
		this.generateExistingSoundWithSubtitle(TFSounds.ORE_METER_TARGET_BLOCK, SoundEvents.LODESTONE_COMPASS_LOCK);
		this.generateExistingSoundWithSubtitle(TFSounds.POWDER_USE, SoundEvents.ZOMBIE_VILLAGER_CURE);
		this.generateExistingSoundWithSubtitle(TFSounds.SCEPTER_DRAIN, SoundEvents.GENERIC_BIG_FALL);
		this.generateExistingSoundWithSubtitle(TFSounds.SCEPTER_PEARL, SoundEvents.FIRECHARGE_USE);
		this.generateExistingSoundWithSubtitle(TFSounds.SCEPTER_USE, SoundEvents.FLINTANDSTEEL_USE);
		this.generateExistingSoundWithSubtitle(TFSounds.WOOD_SHIELD_SHATTERS, SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR);

		this.makeMusicDisc(TFSounds.MUSIC_DISC_RADIANCE, "radiance");
		this.makeMusicDisc(TFSounds.MUSIC_DISC_STEPS, "steps");
		this.makeMusicDisc(TFSounds.MUSIC_DISC_SUPERSTITIOUS, "superstitious");
		this.makeMusicDisc(TFSounds.MUSIC_DISC_HOME, "home");
		this.makeMusicDisc(TFSounds.MUSIC_DISC_WAYFARER, "wayfarer");
		this.makeMusicDisc(TFSounds.MUSIC_DISC_FINDINGS, "findings");
		this.makeMusicDisc(TFSounds.MUSIC_DISC_MAKER, "maker");
		this.makeMusicDisc(TFSounds.MUSIC_DISC_THREAD, "thread");
		this.makeMusicDisc(TFSounds.MUSIC_DISC_MOTION, "motion");

		this.add(TFSounds.MUSIC, SoundDefinition.definition().with(
				SoundDefinition.Sound.sound(new ResourceLocation(TwilightForestMod.ID, "music/superstitious"), SoundDefinition.SoundType.SOUND).stream().volume(0.5F),
				SoundDefinition.Sound.sound(new ResourceLocation(TwilightForestMod.ID, "music/steps"), SoundDefinition.SoundType.SOUND).stream().volume(0.5F),
				SoundDefinition.Sound.sound(new ResourceLocation(TwilightForestMod.ID, "music/radiance"), SoundDefinition.SoundType.SOUND).stream().volume(0.5F),
				SoundDefinition.Sound.sound(new ResourceLocation(TwilightForestMod.ID, "music/home"), SoundDefinition.SoundType.SOUND).stream().volume(0.5F),
				SoundDefinition.Sound.sound(new ResourceLocation(TwilightForestMod.ID, "music/wayfarer"), SoundDefinition.SoundType.SOUND).stream().volume(0.5F),
				SoundDefinition.Sound.sound(new ResourceLocation(TwilightForestMod.ID, "music/findings"), SoundDefinition.SoundType.SOUND).stream().volume(0.5F),
				SoundDefinition.Sound.sound(new ResourceLocation(TwilightForestMod.ID, "music/maker"), SoundDefinition.SoundType.SOUND).stream().volume(0.5F),
				SoundDefinition.Sound.sound(new ResourceLocation(TwilightForestMod.ID, "music/thread"), SoundDefinition.SoundType.SOUND).stream().volume(0.5F),
				SoundDefinition.Sound.sound(new ResourceLocation(TwilightForestMod.ID, "music/motion"), SoundDefinition.SoundType.SOUND).stream().volume(0.5F)));
	}
}
