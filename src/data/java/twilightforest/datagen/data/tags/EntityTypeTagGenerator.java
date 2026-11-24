package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityTypes;
import net.neoforged.neoforge.common.Tags;
import twilightforest.datagen.data.tags.compat.ModdedEntityTypeTagGenerator;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.init.TFEntities;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EntityTypeTagGenerator extends ModdedEntityTypeTagGenerator {

	public EntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);
		this.tag(EntityTypeTags.SKELETONS).addAll(List.of(TFEntities.SKELETON_DRUID.key(), TFEntities.LICH.key(), TFEntities.KNIGHT_PHANTOM.key()));
		this.tag(EntityTypeTags.ZOMBIES).addAll(List.of(TFEntities.LICH_MINION.key(), TFEntities.LOYAL_ZOMBIE.key(), TFEntities.RISING_ZOMBIE.key()));
		this.tag(EntityTypeTags.UNDEAD).add(TFEntities.WRAITH.key());
		this.tag(EntityTypeTags.BURN_IN_DAYLIGHT).add(TFEntities.SKELETON_DRUID.key());
		this.tag(EntityTypeTags.ARROWS).addAll(List.of(TFEntities.ICE_ARROW.key(), TFEntities.SEEKER_ARROW.key()));
		this.tag(EntityTypeTags.IMPACT_PROJECTILES).addAll(List.of(
			TFEntities.NATURE_BOLT.key(),
			TFEntities.LICH_BOLT.key(),
			TFEntities.WAND_BOLT.key(),
			TFEntities.LICH_BOMB.key(),
			TFEntities.MOONWORM_SHOT.key(),
			TFEntities.SLIME_BLOB.key(),
			TFEntities.THROWN_WEP.key(),
			TFEntities.THROWN_ICE.key(),
			TFEntities.FALLING_ICE.key(),
			TFEntities.ICE_SNOWBALL.key(),
			TFEntities.CHAIN_BLOCK.key()));

		this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).addAll(List.of(
			TFEntities.PENGUIN.key(),
			TFEntities.STABLE_ICE_CORE.key(),
			TFEntities.UNSTABLE_ICE_CORE.key(),
			TFEntities.SNOW_GUARDIAN.key(),
			TFEntities.ICE_CRYSTAL.key(),
			TFEntities.RAVEN.key(),
			TFEntities.SQUIRREL.key(),
			TFEntities.DWARF_RABBIT.key(),
			TFEntities.TINY_BIRD.key(),
			TFEntities.KOBOLD.key(),
			TFEntities.DEATH_TOME.key(),
			TFEntities.MOSQUITO_SWARM.key(),
			TFEntities.TOWERWOOD_BORER.key()
		));

		this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).addAll(List.of(
			TFEntities.PENGUIN.key(),
			TFEntities.STABLE_ICE_CORE.key(),
			TFEntities.UNSTABLE_ICE_CORE.key(),
			TFEntities.SNOW_GUARDIAN.key(),
			TFEntities.ICE_CRYSTAL.key(),
			TFEntities.WRAITH.key(),
			TFEntities.KNIGHT_PHANTOM.key(),
			TFEntities.WINTER_WOLF.key(),
			TFEntities.YETI.key()
		)).addTag(TFEntityTypeTags.BOSSES);

		this.tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(TFEntities.FIRE_BEETLE.key());
		this.tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER).addTag(TFEntityTypeTags.BOSSES);
		this.tag(EntityTypeTags.FROG_FOOD).add(TFEntities.MAZE_SLIME.key());
		this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).addAll(List.of(
			TFEntities.NAGA.key(),
			TFEntities.SQUIRREL.key(),
			TFEntities.WRAITH.key(),
			TFEntities.CARMINITE_GOLEM.key(),
			TFEntities.DEATH_TOME.key(),
			TFEntities.UR_GHAST.key(),
			TFEntities.CARMINITE_GHASTLING.key(),
			TFEntities.KNIGHT_PHANTOM.key(),
			TFEntities.SNOW_QUEEN.key(),
			TFEntities.PENGUIN.key(),
			TFEntities.RAVEN.key(),
			TFEntities.SNOW_GUARDIAN.key(),
			TFEntities.STABLE_ICE_CORE.key(),
			TFEntities.MOSQUITO_SWARM.key(),
			TFEntities.UNSTABLE_ICE_CORE.key(),
			TFEntities.ICE_CRYSTAL.key(),
			TFEntities.CARMINITE_GHASTGUARD.key(),
			TFEntities.TINY_BIRD.key()));

		this.tag(EntityTypeTags.NON_CONTROLLING_RIDER).add(TFEntities.MAZE_SLIME.key());
		this.tag(EntityTypeTags.ARTHROPOD).addAll(List.of(
			TFEntities.CARMINITE_BROODLING.key(),
			TFEntities.FIRE_BEETLE.key(),
			TFEntities.HEDGE_SPIDER.key(),
			TFEntities.HELMET_CRAB.key(),
			TFEntities.KING_SPIDER.key(),
			TFEntities.PINCH_BEETLE.key(),
			TFEntities.SLIME_BEETLE.key(),
			TFEntities.SWARM_SPIDER.key(),
			TFEntities.TOWERWOOD_BORER.key(),
			TFEntities.MOSQUITO_SWARM.key()
		));

		this.tag(EntityTypeTags.REDIRECTABLE_PROJECTILE).addAll(List.of(TFEntities.HYDRA_MORTAR.key(), TFEntities.LICH_BOLT.key()));
		this.tag(EntityTypeTags.IMMUNE_TO_INFESTED).add(TFEntities.TOWERWOOD_BORER.key());
		this.tag(EntityTypeTags.IMMUNE_TO_OOZING).add(TFEntities.MAZE_SLIME.key());
		this.tag(EntityTypeTags.BOAT).addAll(List.of(
			TFEntities.TWILIGHT_OAK_BOAT.key(),
			TFEntities.CANOPY_BOAT.key(),
			TFEntities.MANGROVE_BOAT.key(),
			TFEntities.DARK_BOAT.key(),
			TFEntities.TIME_BOAT.key(),
			TFEntities.TRANSFORMATION_BOAT.key(),
			TFEntities.MINING_BOAT.key(),
			TFEntities.SORTING_BOAT.key()));

		this.tag(EntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS).addAll(List.of(
			TFEntities.DEER.key(),
			TFEntities.TINY_BIRD.key(),
			TFEntities.RAVEN.key(),
			TFEntities.BOAR.key(),
			TFEntities.BIGHORN_SHEEP.key(),
			TFEntities.DWARF_RABBIT.key(),
			TFEntities.PENGUIN.key(),
			TFEntities.SQUIRREL.key()
		));

		this.tag(EntityTypeTags.CANNOT_BE_PUSHED_ONTO_BOATS).addAll(List.of(
			TFEntities.ADHERENT.key(),
			TFEntities.CARMINITE_GHASTLING.key(),
			TFEntities.CARMINITE_GHASTGUARD.key(),
			TFEntities.DEATH_TOME.key(),
			TFEntities.MOSQUITO_SWARM.key(),
			TFEntities.QUEST_RAM.key(),
			TFEntities.RISING_ZOMBIE.key(),
			TFEntities.SNOW_GUARDIAN.key(),
			TFEntities.WRAITH.key()
		)).addTag(TFEntityTypeTags.BOSSES);

		this.tag(TFEntityTypeTags.BOSSES).addAll(List.of(
			TFEntities.NAGA.key(),
			TFEntities.LICH.key(),
			TFEntities.MINOSHROOM.key(),
			TFEntities.HYDRA.key(),
			TFEntities.KNIGHT_PHANTOM.key(),
			TFEntities.UR_GHAST.key(),
			TFEntities.ALPHA_YETI.key(),
			TFEntities.SNOW_QUEEN.key(),
			TFEntities.PLATEAU_BOSS.key()
		));

		this.tag(TFEntityTypeTags.LICH_POPPABLES)
			.addTag(EntityTypeTags.SKELETONS)
			.addAll(List.of(EntityTypes.ZOMBIE.builtInRegistryHolder().key(), EntityTypes.ENDERMAN.builtInRegistryHolder().key(), EntityTypes.SPIDER.builtInRegistryHolder().key(), EntityTypes.CREEPER.builtInRegistryHolder().key(), TFEntities.SWARM_SPIDER.key()))
			.remove(Tags.EntityTypes.BOSSES);

		this.tag(TFEntityTypeTags.LIFEDRAIN_DROPS_NO_FLESH).addTag(EntityTypeTags.SKELETONS).addTag(EntityTypeTags.FROG_FOOD).addAll(List.of(
			EntityTypes.BLAZE.builtInRegistryHolder().key(),
			EntityTypes.BREEZE.builtInRegistryHolder().key(),
			EntityTypes.IRON_GOLEM.builtInRegistryHolder().key(),
			EntityTypes.PHANTOM.builtInRegistryHolder().key(),
			EntityTypes.SHULKER.builtInRegistryHolder().key(),
			EntityTypes.SKELETON_HORSE.builtInRegistryHolder().key(),
			EntityTypes.SNOW_GOLEM.builtInRegistryHolder().key(),
			EntityTypes.VEX.builtInRegistryHolder().key(),
			EntityTypes.WITHER.builtInRegistryHolder().key(),
			TFEntities.CARMINITE_GOLEM.key(),
			TFEntities.DEATH_TOME.key(),
			TFEntities.ICE_CRYSTAL.key(),
			TFEntities.KNIGHT_PHANTOM.key(),
			TFEntities.LICH.key(),
			TFEntities.MOSQUITO_SWARM.key(),
			TFEntities.SNOW_GUARDIAN.key(),
			TFEntities.STABLE_ICE_CORE.key(),
			TFEntities.UNSTABLE_ICE_CORE.key(),
			TFEntities.WRAITH.key()));

		// These entities forcefully take players from the entity they're riding
		this.tag(TFEntityTypeTags.RIDES_OBSTRUCT_SNATCHING).addAll(List.of(TFEntities.PINCH_BEETLE.key(), TFEntities.YETI.key(), TFEntities.ALPHA_YETI.key()));
		this.tag(TFEntityTypeTags.DONT_KILL_BUGS).add(TFEntities.MOONWORM_SHOT.key());
		this.tag(TFEntityTypeTags.SORTABLE_ENTITIES).addAll(List.of(
			EntityTypes.CHEST_MINECART.builtInRegistryHolder().key(),
			EntityTypes.HOPPER_MINECART.builtInRegistryHolder().key(),
			EntityTypes.LLAMA.builtInRegistryHolder().key(),
			EntityTypes.TRADER_LLAMA.builtInRegistryHolder().key(),
			EntityTypes.DONKEY.builtInRegistryHolder().key(),
			EntityTypes.MULE.builtInRegistryHolder().key()));

		this.tag(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES).addAll(List.of(
			TFEntities.NAGA.key(),
			TFEntities.LICH.key(),
			TFEntities.MINOSHROOM.key(),
			TFEntities.HYDRA.key(),
			TFEntities.UR_GHAST.key(),
			TFEntities.ALPHA_YETI.key(),
			TFEntities.SNOW_QUEEN.key(),
			TFEntities.PLATEAU_BOSS.key()
		));

		this.tag(Tags.EntityTypes.BOSSES).addTag(TFEntityTypeTags.BOSSES);
		this.tag(TFEntityTypeTags.LICH_DEFLECTS_PHASE_2).addAll(List.of(TFEntities.WAND_BOLT.key(), TFEntities.LICH_BOLT.key(), TFEntities.LICH_BOMB.key()));

		this.tag(TFEntityTypeTags.HEDGE_DAMAGE_IMMUNE).addAll(List.of(
			EntityTypes.SPIDER.builtInRegistryHolder().key(),
			EntityTypes.CAVE_SPIDER.builtInRegistryHolder().key(),
			TFEntities.HEDGE_SPIDER.key(),
			TFEntities.SWARM_SPIDER.key(),
			TFEntities.KING_SPIDER.key(),
			TFEntities.CARMINITE_BROODLING.key(),
			EntityTypes.ITEM.builtInRegistryHolder().key()
		));

		this.tag(TFEntityTypeTags.PROGRESSION_LOCK_VULNERABLE).add(TFEntities.KOBOLD.key());
	}


	@Override
	public String getName() {
		return "Twilight Forest Entity Tags";
	}
}
