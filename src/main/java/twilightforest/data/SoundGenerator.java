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
		this.generateExistingSoundWithSubtitle(TFSounds.ACID_RAIN_BURNS, SoundEvents.FIRE_EXTINGUISH, "Acid rain scalds");
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_ALERT, "mob/alpha_yeti/alert", 1, "Alpha Yeti takes notice");
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_DEATH, "mob/alpha_yeti/death", 1, "Alpha Yeti dies");
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_GRAB, "mob/alpha_yeti/grab", 1, "Alpha Yeti grabs");
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_GROWL, "mob/alpha_yeti/growl", 3, "Alpha Yeti growls");
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_HURT, "mob/alpha_yeti/hurt", 3, "Alpha Yeti hurts");
		this.generateExistingSoundWithSubtitle(TFSounds.ALPHA_YETI_ICE, SoundEvents.ARROW_SHOOT, "Alpha Yeti throws ice");
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_PANT, "mob/alpha_yeti/pant", 3, "Alpha Yeti pants");
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_ROAR, "mob/alpha_yeti/roar", 1, "Alpha Yeti roars");
		this.generateNewSoundWithSubtitle(TFSounds.ALPHA_YETI_THROW, "mob/alpha_yeti/throw", 1, "Alpha Yeti throws");

		this.generateExistingSoundWithSubtitle(TFSounds.BIGHORN_SHEEP_AMBIENT, SoundEvents.SHEEP_AMBIENT, "Bighorn Sheep bleats");
		this.generateExistingSoundWithSubtitle(TFSounds.BIGHORN_SHEEP_DEATH, SoundEvents.SHEEP_DEATH, "Bighorn Sheep dies");
		this.generateExistingSoundWithSubtitle(TFSounds.BIGHORN_SHEEP_HURT, SoundEvents.SHEEP_HURT, "Bighorn Sheep hurts");
		this.makeStepSound(TFSounds.BIGHORN_SHEEP_STEP, SoundEvents.SHEEP_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.BLOCKCHAIN_GOBLIN_AMBIENT, "mob/redcap/redcap", 6, "Block and Chain Goblin chuckles");
		this.generateNewSoundWithSubtitle(TFSounds.BLOCKCHAIN_GOBLIN_DEATH, "mob/redcap/die", 3, "Block and Chain Goblin dies");
		this.generateNewSoundWithSubtitle(TFSounds.BLOCKCHAIN_GOBLIN_HURT, "mob/redcap/hurt", 4, "Block and Chain Goblin screams");

		this.generateExistingSoundWithSubtitle(TFSounds.BOAR_AMBIENT, SoundEvents.PIG_AMBIENT, "Boar oinks");
		this.generateExistingSoundWithSubtitle(TFSounds.BOAR_DEATH, SoundEvents.PIG_DEATH, "Boar dies");
		this.generateExistingSoundWithSubtitle(TFSounds.BOAR_HURT, SoundEvents.PIG_HURT, "Boar hurts");
		this.makeStepSound(TFSounds.BOAR_STEP, SoundEvents.PIG_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_BROODLING_AMBIENT, SoundEvents.SPIDER_AMBIENT, "Carminite Broodling hisses");
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_BROODLING_DEATH, SoundEvents.SPIDER_DEATH, "Carminite Broodling dies");
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_BROODLING_HURT, SoundEvents.SPIDER_HURT, "Carminite Broodling hurts");
		this.makeStepSound(TFSounds.CARMINITE_BROODLING_STEP, SoundEvents.SPIDER_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTGUARD_AMBIENT, SoundEvents.GHAST_AMBIENT, "Carminite Ghastguard cries");
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTGUARD_DEATH, SoundEvents.GHAST_DEATH, "Carminite Ghastguard dies");
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTGUARD_HURT, SoundEvents.GHAST_HURT, "Carminite Ghastguard screams");
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTGUARD_SHOOT, SoundEvents.GHAST_SHOOT, "Carminite Ghastguard shoots");
		this.generateSoundWithExistingSubtitle(TFSounds.CARMINITE_GHASTGUARD_WARN, SoundEvents.GHAST_WARN, "subtitles.twilightforest.entity.carminite_ghastguard.shoot");

		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTLING_AMBIENT, SoundEvents.GHAST_AMBIENT, "Carminite Ghastling cries");
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTLING_DEATH, SoundEvents.GHAST_DEATH, "Carminite Ghastling dies");
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTLING_HURT, SoundEvents.GHAST_HURT, "Carminite Ghastling screams");
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GHASTLING_SHOOT, SoundEvents.GHAST_SHOOT, "Carminite Ghastling shoots");
		this.generateSoundWithExistingSubtitle(TFSounds.CARMINITE_GHASTLING_WARN, SoundEvents.GHAST_WARN, "subtitles.twilightforest.entity.carminite_ghastling.shoot");

		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GOLEM_ATTACK, SoundEvents.IRON_GOLEM_ATTACK, "Carminite Golem swings");
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GOLEM_DEATH, SoundEvents.IRON_GOLEM_DEATH, "Carminite Golem dies");
		this.generateExistingSoundWithSubtitle(TFSounds.CARMINITE_GOLEM_HURT, SoundEvents.IRON_GOLEM_HURT, "Carminite Golem hurts");
		this.makeStepSound(TFSounds.CARMINITE_GOLEM_STEP, SoundEvents.IRON_GOLEM_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.DEATH_TOME_AMBIENT, "mob/tome/idle", 2, "Death Tome flips pages");
		this.generateNewSoundWithSubtitle(TFSounds.DEATH_TOME_DEATH, "mob/tome/death", 1, "Death Tome falls apart");
		this.generateNewSoundWithSubtitle(TFSounds.DEATH_TOME_HURT, "mob/tome/hurt", 3, "Death Tome creases");

		this.generateNewSoundWithSubtitle(TFSounds.DEER_AMBIENT, "mob/deer/idle", 3, "Deer moos");
		this.generateNewSoundWithSubtitle(TFSounds.DEER_DEATH, "mob/deer/death", 1, "Deer dies");
		this.generateNewSoundWithSubtitle(TFSounds.DEER_HURT, "mob/deer/hurt", 2, "Deer hurts");

		this.generateExistingSoundWithSubtitle(TFSounds.DWARF_RABBIT_AMBIENT, SoundEvents.RABBIT_AMBIENT, "Dwarf Rabbit squeaks");
		this.generateExistingSoundWithSubtitle(TFSounds.DWARF_RABBIT_DEATH, SoundEvents.RABBIT_DEATH, "Dwarf Rabbit dies");
		this.generateExistingSoundWithSubtitle(TFSounds.DWARF_RABBIT_HURT, SoundEvents.RABBIT_HURT, "Dwarf Rabbit hurts");

		this.generateNewSoundWithSubtitle(TFSounds.FIRE_BEETLE_AMBIENT, "mob/fire_beetle/idle", 4, "Fire Beetle chitters");
		this.generateNewSoundWithSubtitle(TFSounds.FIRE_BEETLE_DEATH, "mob/fire_beetle/death", 1, "Fire Beetle dies");
		this.generateNewSoundWithSubtitle(TFSounds.FIRE_BEETLE_HURT, "mob/fire_beetle/hurt", 3, "Fire Beetle hurts");
		this.generateExistingSoundWithSubtitle(TFSounds.FIRE_BEETLE_SHOOT, SoundEvents.GHAST_SHOOT, "Fire Beetle spews flames");
		this.makeStepSound(TFSounds.FIRE_BEETLE_STEP, SoundEvents.SPIDER_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.GOBLIN_KNIGHT_AMBIENT, "mob/redcap/redcap", 6, "Goblin Knight chuckles");
		this.generateNewSoundWithSubtitle(TFSounds.GOBLIN_KNIGHT_DEATH, "mob/redcap/die", 3, "Goblin Knight groans in agony");
		this.generateNewSoundWithSubtitle(TFSounds.GOBLIN_KNIGHT_HURT, "mob/redcap/hurt", 4, "Goblin Knight screeches in pain");

		this.generateNewSoundWithSubtitle(TFSounds.GOBLIN_KNIGHT_MUFFLED_AMBIENT, "mob/redcap/muffled/redcap", 6, "Muffled Goblin Knight chuckles");
		this.generateNewSoundWithSubtitle(TFSounds.GOBLIN_KNIGHT_MUFFLED_DEATH, "mob/redcap/muffled/die", 3, "Muffled Goblin Knight groans in agony");
		this.generateNewSoundWithSubtitle(TFSounds.GOBLIN_KNIGHT_MUFFLED_HURT, "mob/redcap/muffled/hurt", 4, "Muffled Goblin Knight screeches in pain");

		this.generateExistingSoundWithSubtitle(TFSounds.HEDGE_SPIDER_AMBIENT, SoundEvents.SPIDER_AMBIENT, "Hedge Spider hisses");
		this.generateExistingSoundWithSubtitle(TFSounds.HEDGE_SPIDER_DEATH, SoundEvents.SPIDER_DEATH, "Hedge Spider dies");
		this.generateExistingSoundWithSubtitle(TFSounds.HEDGE_SPIDER_HURT, SoundEvents.SPIDER_HURT, "Hedge Spider hurts");
		this.makeStepSound(TFSounds.HEDGE_SPIDER_STEP, SoundEvents.SPIDER_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.HELMET_CRAB_AMBIENT, "mob/helmet_crab/idle", 3, "Helmet Crab snips");
		this.generateNewSoundWithSubtitle(TFSounds.HELMET_CRAB_DEATH, "mob/helmet_crab/hurt", 3, "Helmet Crab dies");
		this.generateNewSoundWithSubtitle(TFSounds.HELMET_CRAB_HURT, "mob/helmet_crab/hurt", 3, "Helmet Crab hurts");
		this.makeNewStepSound(TFSounds.HELMET_CRAB_STEP, "mob/helmet_crab/step", 6);

		this.generateNewSoundWithSubtitle(TFSounds.HOSTILE_WOLF_AMBIENT, "mob/mist_wolf/idle", 3, "Hostile Wolf growls");
		this.generateExistingSoundWithSubtitle(TFSounds.HOSTILE_WOLF_DEATH, SoundEvents.WOLF_DEATH, "Hostile Wolf dies");
		this.generateNewSoundWithSubtitle(TFSounds.HOSTILE_WOLF_HURT, "mob/mist_wolf/hurt", 2, "Hostile Wolf hurts");
		this.generateNewSoundWithSubtitle(TFSounds.HOSTILE_WOLF_TARGET, "mob/mist_wolf/target", 1, "Hostile Wolf takes notice");

		this.generateNewSoundWithSubtitle(TFSounds.HYDRA_DEATH, "mob/hydra/death", 1, "Hydra roars in defeat");
		this.generateNewSoundWithSubtitle(TFSounds.HYDRA_GROWL, "mob/hydra/growl", 3, "Hydra growls");
		this.generateNewSoundWithSubtitle(TFSounds.HYDRA_HURT, "mob/hydra/hurt", 4, "Hydra hurts");
		this.generateNewSoundWithSubtitle(TFSounds.HYDRA_ROAR, "mob/hydra/roar", 2, "Hydra roars");
		this.generateExistingSoundWithSubtitle(TFSounds.HYDRA_SHOOT, SoundEvents.GHAST_SHOOT, "Hydra shoots mortar");
		this.generateExistingSoundWithSubtitle(TFSounds.HYDRA_SHOOT_FIRE, SoundEvents.GHAST_SHOOT, "Hydra spews fire");
		this.generateNewSoundWithSubtitle(TFSounds.HYDRA_WARN, "mob/hydra/warn", 1, "Hydra prepares to bite");

		this.generateNewSoundWithSubtitle(TFSounds.ICE_CORE_AMBIENT, "mob/ice/crackle", 2, "Ice Core crackles");
		this.generateNewSoundWithSubtitle(TFSounds.ICE_CORE_DEATH, "mob/ice/death", 2, "Ice Core dies");
		this.generateNewSoundWithSubtitle(TFSounds.ICE_CORE_HURT, "mob/ice/hurt", 2, "Ice Core hurts");
		this.generateExistingSoundWithSubtitle(TFSounds.ICE_CORE_SHOOT, SoundEvents.SNOWBALL_THROW, "Ice Core shoots snowball");

		this.generateExistingSoundWithSubtitle(TFSounds.KING_SPIDER_AMBIENT, SoundEvents.SPIDER_AMBIENT, "King Spider hisses");
		this.generateExistingSoundWithSubtitle(TFSounds.KING_SPIDER_DEATH, SoundEvents.SPIDER_DEATH, "King Spider dies");
		this.generateExistingSoundWithSubtitle(TFSounds.KING_SPIDER_HURT, SoundEvents.SPIDER_HURT, "King Spider hurts");
		this.makeStepSound(TFSounds.KING_SPIDER_STEP, SoundEvents.SPIDER_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.KNIGHT_PHANTOM_AMBIENT, "mob/wraith/wraith", 4, "Knight Phantom gasps");
		this.generateNewSoundWithSubtitle(TFSounds.KNIGHT_PHANTOM_DEATH, "mob/wraith/wraith", 4, "Knight Phantom dies");
		this.generateNewSoundWithSubtitle(TFSounds.KNIGHT_PHANTOM_HURT, "mob/wraith/wraith", 4, "Knight Phantom hurts");
		this.generateExistingSoundWithSubtitle(TFSounds.KNIGHT_PHANTOM_THROW_AXE, SoundEvents.ARROW_SHOOT, "Knight Phantom throws axe");
		this.generateExistingSoundWithSubtitle(TFSounds.KNIGHT_PHANTOM_THROW_PICK, SoundEvents.ARROW_SHOOT, "Knight Phantom throws pickaxe");

		this.generateNewSoundWithSubtitle(TFSounds.KOBOLD_AMBIENT, "mob/kobold/ambient", 6, "Kobold grumbles");
		this.generateNewSoundWithSubtitle(TFSounds.KOBOLD_DEATH, "mob/kobold/death", 3, "Kobold dies");
		this.generateNewSoundWithSubtitle(TFSounds.KOBOLD_HURT, "mob/kobold/hurt", 3, "Kobold hurts");
		this.generateExistingSoundWithSubtitle(TFSounds.KOBOLD_MUNCH, SoundEvents.GENERIC_EAT, "Kobold munches");

		this.generateExistingSoundWithSubtitle(TFSounds.LICH_AMBIENT, SoundEvents.BLAZE_AMBIENT, "Lich breathes");
		this.generateExistingSoundWithSubtitle(TFSounds.LICH_CLONE_HURT, SoundEvents.FIRE_EXTINGUISH, "Lich clone ignores attack");
		this.generateExistingSoundWithSubtitle(TFSounds.LICH_DEATH, SoundEvents.BLAZE_DEATH, "Lich dies");
		this.generateExistingSoundWithSubtitle(TFSounds.LICH_HURT, SoundEvents.BLAZE_HURT, "Lich hurts");
		this.generateExistingSoundWithSubtitle(TFSounds.LICH_POP_MOB, SoundEvents.CHICKEN_EGG, "Lich absorbs mob");
		this.generateExistingSoundWithSubtitle(TFSounds.LICH_SHOOT, SoundEvents.GHAST_SHOOT, "Lich shoots");
		this.generateExistingSoundWithSubtitle(TFSounds.LICH_TELEPORT, SoundEvents.CHORUS_FRUIT_TELEPORT, "Lich teleports");

		this.generateExistingSoundWithSubtitle(TFSounds.LOYAL_ZOMBIE_AMBIENT, SoundEvents.ZOMBIE_AMBIENT, "Loyal Zombie groans");
		this.generateExistingSoundWithSubtitle(TFSounds.LOYAL_ZOMBIE_DEATH, SoundEvents.ZOMBIE_DEATH, "Loyal Zombie dies");
		this.generateExistingSoundWithSubtitle(TFSounds.LOYAL_ZOMBIE_HURT, SoundEvents.ZOMBIE_HURT, "Loyal Zombie hurts");
		this.makeStepSound(TFSounds.LOYAL_ZOMBIE_STEP, SoundEvents.ZOMBIE_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.MAZE_SLIME_DEATH, SoundEvents.SLIME_DEATH, "Maze Slime dies");
		this.generateExistingSoundWithSubtitle(TFSounds.MAZE_SLIME_HURT, SoundEvents.SLIME_HURT, "Maze Slime hurts");
		this.generateExistingSoundWithSubtitle(TFSounds.MAZE_SLIME_SQUISH, SoundEvents.SLIME_SQUISH, "Maze Slime squishes");

		this.generateSoundWithExistingSubtitle(TFSounds.MAZE_SLIME_DEATH_SMALL, SoundEvents.SLIME_DEATH_SMALL, "subtitles.twilightforest.entity.maze_slime.death");
		this.generateSoundWithExistingSubtitle(TFSounds.MAZE_SLIME_HURT_SMALL, SoundEvents.SLIME_HURT_SMALL, "subtitles.twilightforest.entity.maze_slime.hurt");
		this.generateSoundWithExistingSubtitle(TFSounds.MAZE_SLIME_SQUISH_SMALL, SoundEvents.SLIME_SQUISH_SMALL, "subtitles.twilightforest.entity.maze_slime.squish");

		this.generateExistingSoundWithSubtitle(TFSounds.MINION_AMBIENT, SoundEvents.ZOMBIE_AMBIENT, "Lich Minion groans");
		this.generateExistingSoundWithSubtitle(TFSounds.MINION_DEATH, SoundEvents.ZOMBIE_DEATH, "Lich Minion dies");
		this.generateExistingSoundWithSubtitle(TFSounds.MINION_HURT, SoundEvents.ZOMBIE_HURT, "Lich Minion hurts");
		this.makeStepSound(TFSounds.MINION_STEP, SoundEvents.ZOMBIE_STEP);
		this.generateExistingSoundWithSubtitle(TFSounds.MINION_SUMMON, SoundEvents.ITEM_PICKUP, "Lich summons new minion");

		this.generateExistingSoundWithSubtitle(TFSounds.MINOSHROOM_AMBIENT, SoundEvents.COW_AMBIENT, "Minoshroom moos");
		this.generateExistingSoundWithSubtitle(TFSounds.MINOSHROOM_ATTACK, SoundEvents.IRON_GOLEM_ATTACK, "Minoshroom attacks");
		this.generateExistingSoundWithSubtitle(TFSounds.MINOSHROOM_DEATH, SoundEvents.COW_DEATH, "Minoshroom dies");
		this.generateExistingSoundWithSubtitle(TFSounds.MINOSHROOM_HURT, SoundEvents.COW_HURT, "Minoshroom hurts");
		this.generateExistingSoundWithSubtitle(TFSounds.MINOSHROOM_SLAM, SoundEvents.LIGHTNING_BOLT_IMPACT, "Minoshroom slams ground");
		this.makeStepSound(TFSounds.MINOSHROOM_STEP, SoundEvents.COW_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.MINOTAUR_AMBIENT, SoundEvents.COW_AMBIENT, "Minotaur moos");
		this.generateExistingSoundWithSubtitle(TFSounds.MINOTAUR_ATTACK, SoundEvents.IRON_GOLEM_ATTACK, "Minotaur attacks");
		this.generateExistingSoundWithSubtitle(TFSounds.MINOTAUR_DEATH, SoundEvents.COW_DEATH, "Minotaur dies");
		this.generateExistingSoundWithSubtitle(TFSounds.MINOTAUR_HURT, SoundEvents.COW_HURT, "Minotaur hurts");
		this.makeStepSound(TFSounds.MINOTAUR_STEP, SoundEvents.COW_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.MIST_WOLF_AMBIENT, "mob/mist_wolf/idle", 3, "Mist Wolf growls");
		this.generateExistingSoundWithSubtitle(TFSounds.MIST_WOLF_DEATH, SoundEvents.WOLF_DEATH, "Mist Wolf dies");
		this.generateNewSoundWithSubtitle(TFSounds.MIST_WOLF_HURT, "mob/mist_wolf/hurt", 2, "Mist Wolf hurts");
		this.generateNewSoundWithSubtitle(TFSounds.MIST_WOLF_TARGET, "mob/mist_wolf/target", 1, "Mist Wolf takes notice");

		this.generateNewSoundWithSubtitle(TFSounds.MOSQUITO, "mob/mosquito/animals132", 1, "Mosquitoes buzz");

		this.generateNewSoundWithSubtitle(TFSounds.NAGA_HISS, "mob/naga/hiss", 3, "Naga hisses");
		this.generateNewSoundWithSubtitle(TFSounds.NAGA_HURT, "mob/naga/hurt", 3, "Naga hurts");
		this.generateNewSoundWithSubtitle(TFSounds.NAGA_RATTLE, "mob/naga/rattle", 2, "Naga rattles");

		this.generateNewSoundWithSubtitle(TFSounds.PENGUIN_AMBIENT, "mob/penguin/idle", 7, "Penguin honks");
		this.generateNewSoundWithSubtitle(TFSounds.PENGUIN_DEATH, "mob/penguin/death", 1, "Penguin dies");
		this.generateNewSoundWithSubtitle(TFSounds.PENGUIN_HURT, "mob/penguin/hurt", 1, "Penguin hurts");

		this.generateNewSoundWithSubtitle(TFSounds.PINCH_BEETLE_ATTACK, "mob/pinch_beetle/attack", 3, "Pinch Beetle clamps");
		this.generateNewSoundWithSubtitle(TFSounds.PINCH_BEETLE_AMBIENT, "mob/pinch_beetle/idle", 4, "Pinch Beetle chitters");
		this.generateNewSoundWithSubtitle(TFSounds.PINCH_BEETLE_DEATH, "mob/pinch_beetle/death", 1, "Pinch Beetle dies");
		this.generateNewSoundWithSubtitle(TFSounds.PINCH_BEETLE_HURT, "mob/pinch_beetle/hurt", 3, "Pinch Beetle hurts");
		this.makeStepSound(TFSounds.PINCH_BEETLE_STEP, SoundEvents.SPIDER_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.QUEST_RAM_AMBIENT, SoundEvents.SHEEP_AMBIENT, "Questing Ram bleats");
		this.generateExistingSoundWithSubtitle(TFSounds.QUEST_RAM_DEATH, SoundEvents.SHEEP_DEATH, "Questing Ram dies");
		this.generateExistingSoundWithSubtitle(TFSounds.QUEST_RAM_HURT, SoundEvents.SHEEP_HURT, "Questing Ram hurts");
		this.makeStepSound(TFSounds.QUEST_RAM_STEP, SoundEvents.SHEEP_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.RAVEN_CAW, "mob/raven/caw", 2, "Raven caws");
		this.generateNewSoundWithSubtitle(TFSounds.RAVEN_SQUAWK, "mob/raven/squawk", 2, "Raven squawks in pain");

		this.generateNewSoundWithSubtitle(TFSounds.REDCAP_AMBIENT, "mob/redcap/redcap", 6, "Redcap chuckles");
		this.generateNewSoundWithSubtitle(TFSounds.REDCAP_DEATH, "mob/redcap/die", 3, "Redcap groans in agony");
		this.generateNewSoundWithSubtitle(TFSounds.REDCAP_HURT, "mob/redcap/hurt", 4, "Redcap screeches in pain");

		this.generateNewSoundWithSubtitle(TFSounds.SHIELD_ADD, "random/scepter/shield_use", 1, "Fortification Shield spawns");
		this.generateNewSoundWithSubtitle(TFSounds.SHIELD_BREAK, "random/scepter/shield_break", 1, "Fortification Shield breaks");
		this.generateExistingSoundWithSubtitle(TFSounds.SHIELD_BLOCK, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, "Fortification Shield deflects", 0.5F, 1.0F);
		this.generateNewSoundWithSubtitle(TFSounds.SHIELD_EXPIRE, "random/scepter/shield_stop", 1, "Fortification Shield expires");

		this.generateExistingSoundWithSubtitle(TFSounds.SKELETON_DRUID_AMBIENT, SoundEvents.STRAY_AMBIENT, "Skeleton Druid rattles");
		this.generateExistingSoundWithSubtitle(TFSounds.SKELETON_DRUID_DEATH, SoundEvents.STRAY_DEATH, "Skeleton Druid dies");
		this.generateExistingSoundWithSubtitle(TFSounds.SKELETON_DRUID_HURT, SoundEvents.STRAY_HURT, "Skeleton Druid hurts");
		this.generateExistingSoundWithSubtitle(TFSounds.SKELETON_DRUID_SHOOT, SoundEvents.GHAST_SHOOT, "Skeleton Druid shoots");
		this.makeStepSound(TFSounds.SKELETON_DRUID_STEP, SoundEvents.SKELETON_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.SLIME_BEETLE_AMBIENT, "mob/slime_beetle/idle", 4, "Slime Beetle chitters");
		this.generateNewSoundWithSubtitle(TFSounds.SLIME_BEETLE_DEATH, "mob/slime_beetle/death", 1, "Slime Beetle dies");
		this.generateNewSoundWithSubtitle(TFSounds.SLIME_BEETLE_HURT, "mob/slime_beetle/hurt", 3, "Slime Beetle hurts");
		this.generateExistingSoundWithSubtitle(TFSounds.SLIME_BEETLE_SQUISH, SoundEvents.SLIME_SQUISH_SMALL, "Slime Beetle shoots");
		this.makeStepSound(TFSounds.SLIME_BEETLE_STEP, SoundEvents.SPIDER_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.SQUIRREL_AMBIENT, "mob/squirrel/idle", 5, "Squirrel chitters");
		this.generateNewSoundWithSubtitle(TFSounds.SQUIRREL_DEATH, "mob/squirrel/death", 1, "Squirrel dies");
		this.generateNewSoundWithSubtitle(TFSounds.SQUIRREL_HURT, "mob/squirrel/hurt", 1, "Squirrel hurts");

		this.generateNewSoundWithSubtitle(TFSounds.SNOW_GUARDIAN_AMBIENT, "mob/ice/crackle", 2, "Snow Guardian crackles");
		this.generateNewSoundWithSubtitle(TFSounds.SNOW_GUARDIAN_DEATH, "mob/ice/death", 2, "Snow Guardian dies");
		this.generateNewSoundWithSubtitle(TFSounds.SNOW_GUARDIAN_HURT, "mob/ice/hurt", 2, "Snow Guardian hurts");

		this.generateNewSoundWithSubtitle(TFSounds.SNOW_QUEEN_AMBIENT, "mob/ice/crackle", 2, "Snow Queen crackles");
		this.generateExistingSoundWithSubtitle(TFSounds.SNOW_QUEEN_ATTACK, SoundEvents.IRON_GOLEM_ATTACK, "Snow Queen attacks");
		this.generateExistingSoundWithSubtitle(TFSounds.SNOW_QUEEN_BREAK, SoundEvents.ITEM_BREAK, "Snow Queen deflects attack");
		this.generateNewSoundWithSubtitle(TFSounds.SNOW_QUEEN_DEATH, "mob/ice/death", 2, "Snow Queen dies");
		this.generateNewSoundWithSubtitle(TFSounds.SNOW_QUEEN_HURT, "mob/ice/hurt", 2, "Snow Queen hurts");

		this.generateExistingSoundWithSubtitle(TFSounds.SWARM_SPIDER_AMBIENT, SoundEvents.SPIDER_AMBIENT, "Swarm Spider hisses");
		this.generateExistingSoundWithSubtitle(TFSounds.SWARM_SPIDER_DEATH, SoundEvents.SPIDER_DEATH, "Swarm Spider dies");
		this.generateExistingSoundWithSubtitle(TFSounds.SWARM_SPIDER_HURT, SoundEvents.SPIDER_HURT, "Swarm Spider hurts");
		this.makeStepSound(TFSounds.SWARM_SPIDER_STEP, SoundEvents.SPIDER_STEP);

		this.generateExistingSoundWithSubtitle(TFSounds.TEAR_BREAK, SoundEvents.GLASS_BREAK, "Tear shatters");

		this.generateNewSoundWithSubtitle(TFSounds.TINY_BIRD_CHIRP, "mob/tiny_bird/chirp", 3, "Tiny Bird chirps");
		this.generateNewSoundWithSubtitle(TFSounds.TINY_BIRD_HURT, "mob/tiny_bird/hurt", 2, "Tiny Bird squeaks in pain");
		this.generateNewSoundWithSubtitle(TFSounds.TINY_BIRD_SONG, "mob/tiny_bird/song", 2, "Tiny Bird sings");
		this.generateExistingSoundWithSubtitle(TFSounds.TINY_BIRD_TAKEOFF, SoundEvents.BAT_TAKEOFF, "Bird takes off");

		this.generateExistingSoundWithSubtitle(TFSounds.TOWERWOOD_BORER_AMBIENT, SoundEvents.SILVERFISH_AMBIENT, "Towerwood Borer hisses");
		this.generateExistingSoundWithSubtitle(TFSounds.TOWERWOOD_BORER_DEATH, SoundEvents.SILVERFISH_DEATH, "Towerwood Borer dies");
		this.generateExistingSoundWithSubtitle(TFSounds.TOWERWOOD_BORER_HURT, SoundEvents.SILVERFISH_HURT, "Towerwood Borer hurts");
		this.makeStepSound(TFSounds.TOWERWOOD_BORER_STEP, SoundEvents.SILVERFISH_STEP);

		this.generateNewSoundWithSubtitle(TFSounds.TROLL_AMBIENT, "mob/troll/idle", 4, "Troll grumbles");
		this.generateNewSoundWithSubtitle(TFSounds.TROLL_DEATH, "mob/troll/death", 1, "Troll dies");
		this.generateNewSoundWithSubtitle(TFSounds.TROLL_GRABS_ROCK, "mob/troll/grab", 1, "Troll grabs rock");
		this.generateNewSoundWithSubtitle(TFSounds.TROLL_HURT, "mob/troll/hurt", 1, "Troll hurts");
		this.generateNewSoundWithSubtitle(TFSounds.TROLL_THROWS_ROCK, "mob/troll/throw", 1, "Troll throws rock");

		this.generateExistingSoundWithSubtitle(TFSounds.UR_GHAST_AMBIENT, SoundEvents.GHAST_AMBIENT, "Ur-Ghast cries");
		this.generateExistingSoundWithSubtitle(TFSounds.UR_GHAST_DEATH, SoundEvents.GHAST_DEATH, "Ur-Ghast dies");
		this.generateExistingSoundWithSubtitle(TFSounds.UR_GHAST_HURT, SoundEvents.GHAST_HURT, "Ur-Ghast screams");
		this.generateExistingSoundWithSubtitle(TFSounds.UR_GHAST_SHOOT, SoundEvents.GHAST_SHOOT, "Ur-Ghast shoots");
		this.generateExistingSoundWithSubtitle(TFSounds.UR_GHAST_TANTRUM, SoundEvents.GHAST_HURT, "Ur-Ghast wails");
		this.generateSoundWithExistingSubtitle(TFSounds.UR_GHAST_WARN, SoundEvents.GHAST_WARN, "subtitles.twilightforest.entity.ur_ghast.shoot");

		this.generateNewSoundWithSubtitle(TFSounds.WINTER_WOLF_AMBIENT, "mob/mist_wolf/idle", 3, "Winter Wolf growls");
		this.generateExistingSoundWithSubtitle(TFSounds.WINTER_WOLF_DEATH, SoundEvents.WOLF_DEATH, "Winter Wolf dies");
		this.generateNewSoundWithSubtitle(TFSounds.WINTER_WOLF_HURT, "mob/mist_wolf/hurt", 2, "Winter Wolf hurts");
		this.generateExistingSoundWithSubtitle(TFSounds.WINTER_WOLF_SHOOT, SoundEvents.GHAST_SHOOT, "Winter Wolf shoots");
		this.generateNewSoundWithSubtitle(TFSounds.WINTER_WOLF_TARGET, "mob/mist_wolf/target", 1, "Winter Wolf takes notice");

		this.generateNewSoundWithSubtitle(TFSounds.WRAITH_AMBIENT, "mob/wraith/wraith", 4, "Wraith gasps");
		this.generateNewSoundWithSubtitle(TFSounds.WRAITH_DEATH, "mob/wraith/wraith", 4, "Wraith dies");
		this.generateNewSoundWithSubtitle(TFSounds.WRAITH_HURT, "mob/wraith/wraith", 4, "Wraith hurts");

		this.generateNewSoundWithSubtitle(TFSounds.YETI_DEATH, "mob/alpha_yeti/death", 1, "Yeti dies");
		this.generateNewSoundWithSubtitle(TFSounds.YETI_GRAB, "mob/alpha_yeti/grab", 1, "Yeti grabs");
		this.generateNewSoundWithSubtitle(TFSounds.YETI_GROWL, "mob/alpha_yeti/growl", 3, "Yeti growls");
		this.generateNewSoundWithSubtitle(TFSounds.YETI_HURT, "mob/alpha_yeti/hurt", 3, "Yeti hurts");
		this.generateNewSoundWithSubtitle(TFSounds.YETI_THROW, "mob/alpha_yeti/throw", 1, "Yeti throws");


		this.generateParrotSound(TFSounds.ALPHA_YETI_PARROT, TFSounds.ALPHA_YETI_GROWL.get(), "Parrot coldly growls");
		this.generateParrotSound(TFSounds.CARMINITE_GOLEM_PARROT, TFSounds.CARMINITE_GOLEM_HURT.get(), "Parrot thunks");
		this.generateParrotSound(TFSounds.DEATH_TOME_PARROT, TFSounds.DEATH_TOME_AMBIENT.get(), "Parrot makes book noises");
		this.generateParrotSound(TFSounds.HOSTILE_WOLF_PARROT, TFSounds.HOSTILE_WOLF_AMBIENT.get(), "Parrot growls");
		this.generateParrotSound(TFSounds.HYDRA_PARROT, TFSounds.HYDRA_GROWL.get(), "Parrot roars");
		this.generateParrotSound(TFSounds.ICE_CORE_PARROT, TFSounds.ICE_CORE_AMBIENT.get(), "Parrot crackles");
		this.generateParrotSound(TFSounds.KOBOLD_PARROT, TFSounds.KOBOLD_AMBIENT.get(), "Parrot grumbles");
		this.generateParrotSound(TFSounds.MINOTAUR_PARROT, TFSounds.MINOTAUR_AMBIENT.get(), "Parrot moos angrily");
		this.generateParrotSound(TFSounds.MOSQUITO_PARROT, TFSounds.MOSQUITO.get(), "Parrot buzzes");
		this.generateParrotSound(TFSounds.NAGA_PARROT, TFSounds.NAGA_HISS.get(), "Parrot rattles");
		this.generateParrotSound(TFSounds.REDCAP_PARROT, TFSounds.REDCAP_AMBIENT.get(), "Parrot chuckles");
		this.generateParrotSound(TFSounds.WRAITH_PARROT, TFSounds.WRAITH_AMBIENT.get(), "Parrot gasps");


		this.generateNewSoundWithSubtitle(TFSounds.BEANSTALK_GROWTH, "random/beanstalk_grow", 1, "Ground rumbles");
		this.generateExistingSoundWithSubtitle(TFSounds.BLOCK_ANNIHILATED, SoundEvents.FIRE_EXTINGUISH, "Block dissolves");
		this.generateExistingSoundWithSubtitle(TFSounds.BOSS_CHEST_APPEAR, SoundEvents.EVOKER_PREPARE_SUMMON, "Boss Chest appears");
		this.generateExistingSoundWithSubtitle(TFSounds.BUG_SQUISH, SoundEvents.SLIME_SQUISH_SMALL, "Bug gets squished :(");
		this.generateExistingSoundWithSubtitle(TFSounds.BUILDER_CREATE, SoundEvents.COMPARATOR_CLICK, "Carminite Builder creates block");
		this.generateExistingSoundWithSubtitle(TFSounds.BUILDER_OFF, SoundEvents.COMPARATOR_CLICK, "Carminite Builder deactivates");
		this.generateExistingSoundWithSubtitle(TFSounds.BUILDER_ON, SoundEvents.COMPARATOR_CLICK, "Carminite Builder activates");
		this.generateExistingSoundWithSubtitle(TFSounds.BUILDER_REPLACE, SoundEvents.ITEM_PICKUP, "Built Block expires");
		this.generateNewSoundWithSubtitle(TFSounds.CASKET_CLOSE, "random/casket/close", 1, "Keepsake Casket closes");
		this.generateExistingSoundWithSubtitle(TFSounds.CASKET_LOCKED, SoundEvents.CHEST_LOCKED, "Keepsake Casket clicks");
		this.generateNewSoundWithSubtitle(TFSounds.CASKET_OPEN, "random/casket/open", 1, "Keepsake Casket opens");
		this.generateNewSoundWithSubtitle(TFSounds.CASKET_REPAIR, "random/casket/repair", 1, "Keepsake Casket repaired");
		this.generateNewSoundWithSubtitle(TFSounds.CICADA, "mob/cicada", 2, "Cicada screams");
		this.generateExistingSoundWithSubtitle(TFSounds.DOOR_ACTIVATED, SoundEvents.COMPARATOR_CLICK, "Castle Door clicks");
		this.generateExistingSoundWithSubtitle(TFSounds.DOOR_REAPPEAR, SoundEvents.FIRE_EXTINGUISH, "Castle Door reappears");
		this.generateExistingSoundWithSubtitle(TFSounds.DOOR_VANISH, SoundEvents.FIRE_EXTINGUISH, "Castle Door vanishes");
		this.generateExistingSoundWithSubtitle(TFSounds.GHAST_TRAP_AMBIENT, SoundEvents.NOTE_BLOCK_HARP.value(), "Ghast Trap dings");
		this.generateNewSoundWithSubtitle(TFSounds.GHAST_TRAP_ON, "mob/ur_ghast/trap_on", 5, "Ghast Trap buzzes");
		this.generateNewSoundWithSubtitle(TFSounds.GHAST_TRAP_SPINDOWN, "mob/ur_ghast/trap_spin_down", 1, "Ghast Trap shuts off");
		this.generateNewSoundWithSubtitle(TFSounds.GHAST_TRAP_WARMUP, "mob/ur_ghast/trap_warmup", 1, "Ghast Trap warms up");
		this.generateExistingSoundWithSubtitle(TFSounds.JET_ACTIVE, SoundEvents.GHAST_SHOOT, "Fire Jet flares");
		this.generateExistingSoundWithSubtitle(TFSounds.JET_POP, SoundEvents.LAVA_POP, "Fire Jet pops");
		this.generateExistingSoundWithSubtitle(TFSounds.JET_START, SoundEvents.COMPARATOR_CLICK, "Fire Jet activates");
		this.generateExistingSoundWithSubtitle(TFSounds.LOCKED_VANISHING_BLOCK, SoundEvents.COMPARATOR_CLICK, "Towerwood door clicks");
		this.generateExistingSoundWithSubtitle(TFSounds.PEDESTAL_ACTIVATE, SoundEvents.ZOMBIE_INFECT, "Trophy Pedestal accepts trophy");
		this.generateExistingSoundWithSubtitle(TFSounds.PICKED_TORCHBERRIES, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, "Torchberries pop");
		this.generateExistingSoundWithSubtitle(TFSounds.PORTAL_WHOOSH, SoundEvents.PORTAL_AMBIENT, "Twilight Forest Portal beckons");
		this.generateExistingSoundWithSubtitle(TFSounds.REACTOR_AMBIENT, SoundEvents.PORTAL_AMBIENT, "Carminite Reactor whooshes");
		this.generateExistingSoundWithSubtitle(TFSounds.REAPPEAR_BLOCK, SoundEvents.ITEM_PICKUP, "Reappearing Block reappears");
		this.generateExistingSoundWithSubtitle(TFSounds.REAPPEAR_POOF, SoundEvents.ITEM_PICKUP, "Reappearing Block disappears");
		this.generateNewSoundWithSubtitle(TFSounds.SLIDER, "random/creakgo2", 1, "Sliding Trap creaks");
		this.generateExistingSoundWithSubtitle(TFSounds.SMOKER_START, SoundEvents.COMPARATOR_CLICK, "Smoker activates");
		this.generateExistingSoundWithSubtitle(TFSounds.TIME_CORE, SoundEvents.COMPARATOR_CLICK, "Tree of Time ticks");
		this.generateExistingSoundWithSubtitle(TFSounds.TRANSFORMATION_CORE, SoundEvents.NOTE_BLOCK_HARP.value(), "Tree of Transformation hums");
		this.generateExistingSound(TFSounds.UNCRAFTING_TABLE_ACTIVATE, SoundEvents.END_PORTAL_SPAWN, null, 1.0F, 1.0F);
		this.generateExistingSoundWithSubtitle(TFSounds.UNLOCK_VANISHING_BLOCK, SoundEvents.COMPARATOR_CLICK, "Towerwood door unlocks");
		this.generateExistingSoundWithSubtitle(TFSounds.VANISHING_BLOCK, SoundEvents.ITEM_PICKUP, "Vanishing Block vanishes");
		this.generateNewSoundWithSubtitle(TFSounds.WROUGHT_IRON_FENCE_EXTENDED, "random/casket/repair", 1, "Iron clanks");

		this.generateExistingSoundWithSubtitle(TFSounds.BLOCK_AND_CHAIN_COLLIDE, SoundEvents.ANVIL_LAND, "Block and Chain hits block");
		this.generateExistingSoundWithSubtitle(TFSounds.BLOCK_AND_CHAIN_FIRED, SoundEvents.ARROW_SHOOT, "Block and Chain thrown");
		this.generateExistingSoundWithSubtitle(TFSounds.BLOCK_AND_CHAIN_HIT, SoundEvents.IRON_GOLEM_ATTACK, "Block and Chain hits entity");
		this.generateExistingSoundWithSubtitle(TFSounds.BRITTLE_FLASK_BREAK, SoundEvents.GLASS_BREAK, "Brittle Flask shatters");
		this.generateExistingSoundWithSubtitle(TFSounds.BRITTLE_FLASK_CRACK, SoundEvents.GLASS_BREAK, "Brittle Flask cracks");
		this.generateExistingSoundWithSubtitle(TFSounds.CHARM_KEEP, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, "Charm of Keeping returns items");
		this.generateExistingSoundWithSubtitle(TFSounds.CHARM_LIFE, SoundEvents.TOTEM_USE, "Charm of Keeping regenerates");
		this.generateNewSoundMC(TFSounds.FAN_WHOOSH, "random/breath", 1, "Peacock Feather Fan blows");
		this.generateExistingSoundWithSubtitle(TFSounds.FLASK_FILL, SoundEvents.BREWING_STAND_BREW, "Potion Flask fills");
		this.generateExistingSoundWithSubtitle(TFSounds.GLASS_SWORD_BREAK, SoundEvents.GLASS_BREAK, "Glass Sword shatters");
		this.generateExistingSoundWithSubtitle(TFSounds.ICE_BOMB_FIRED, SoundEvents.ARROW_SHOOT, "Ice Bomb thrown");
		this.generateExistingSoundWithSubtitle(TFSounds.KNIGHTMETAL_EQUIP, SoundEvents.ARMOR_EQUIP_NETHERITE.value(), "Knightmetal Armor clanks");
		this.generateExistingSoundWithSubtitle(TFSounds.LAMP_BURN, SoundEvents.GHAST_SHOOT, "Lamp of Cinders ignites area");
		this.generateExistingSoundWithSubtitle(TFSounds.MAGNET_GRAB, SoundEvents.CHORUS_FRUIT_TELEPORT, "Ore Magnet pulls up ore");
		this.generateExistingSoundWithSubtitle(TFSounds.METAL_SHIELD_SHATTERS, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, "Metal Shield shatters");
		this.generateExistingSoundWithSubtitle(TFSounds.MOONWORM_SQUISH, SoundEvents.SLIME_SQUISH_SMALL, "Moonworm fires");
		this.generateNewSoundMC(TFSounds.ORE_METER_CLEAR, "block/crafter/craft", 1, "Ore Meter wipes information");
		this.generateNewSoundMC(TFSounds.ORE_METER_CRACKLE, "ambient/nether/basalt_deltas/click", 5, "Ore Meter crackles");
		this.generateExistingSoundWithSubtitle(TFSounds.ORE_METER_TARGET_BLOCK, SoundEvents.LODESTONE_COMPASS_LOCK, "Ore Meter locks onto block");
		this.generateExistingSoundWithSubtitle(TFSounds.POWDER_USE, SoundEvents.ZOMBIE_VILLAGER_CURE, "Mob transforms");
		this.generateNewSoundWithSubtitle(TFSounds.LIFE_SCEPTER_DRAIN, "random/scepter/drain", 3, "Life Scepter drains");
		this.generateNewSoundWithSubtitle(TFSounds.TWILIGHT_SCEPTER_HIT, "random/scepter/twilight_hit", 3, "Twilight Pearl hits mob");
		this.generateNewSoundWithSubtitle(TFSounds.TWILIGHT_SCEPTER_USE, "random/scepter/twilight_use", 3, "Twilight Scepter throws pearl");
		this.generateExistingSoundWithSubtitle(TFSounds.WOOD_SHIELD_SHATTERS, SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, "Wooden Shield shatters");
		this.generateNewSoundWithSubtitle(TFSounds.ZOMBIE_SCEPTER_USE, "random/scepter/zombie", 1, "Loyal Zombie summoned");

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
