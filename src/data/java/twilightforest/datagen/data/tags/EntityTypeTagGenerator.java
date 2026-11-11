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
		this.tag(EntityTypeTags.SKELETONS).addAll(List.of(TFEntities.SKELETON_DRUID.getKey(), TFEntities.LICH.getKey(), TFEntities.KNIGHT_PHANTOM.getKey()));
		this.tag(EntityTypeTags.ZOMBIES).addAll(List.of(TFEntities.LICH_MINION.getKey(), TFEntities.LOYAL_ZOMBIE.getKey(), TFEntities.RISING_ZOMBIE.getKey()));
		this.tag(EntityTypeTags.UNDEAD).add(TFEntities.WRAITH.getKey());
		this.tag(EntityTypeTags.BURN_IN_DAYLIGHT).add(TFEntities.SKELETON_DRUID.getKey());
		this.tag(EntityTypeTags.ARROWS).addAll(List.of(TFEntities.ICE_ARROW.getKey(), TFEntities.SEEKER_ARROW.getKey()));
		this.tag(EntityTypeTags.IMPACT_PROJECTILES).addAll(List.of(
			TFEntities.NATURE_BOLT.getKey(),
			TFEntities.LICH_BOLT.getKey(),
			TFEntities.WAND_BOLT.getKey(),
			TFEntities.LICH_BOMB.getKey(),
			TFEntities.MOONWORM_SHOT.getKey(),
			TFEntities.SLIME_BLOB.getKey(),
			TFEntities.THROWN_WEP.getKey(),
			TFEntities.THROWN_ICE.getKey(),
			TFEntities.FALLING_ICE.getKey(),
			TFEntities.ICE_SNOWBALL.getKey(),
			TFEntities.CHAIN_BLOCK.getKey()));

		this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).addAll(List.of(
			TFEntities.PENGUIN.getKey(),
			TFEntities.STABLE_ICE_CORE.getKey(),
			TFEntities.UNSTABLE_ICE_CORE.getKey(),
			TFEntities.SNOW_GUARDIAN.getKey(),
			TFEntities.ICE_CRYSTAL.getKey(),
			TFEntities.RAVEN.getKey(),
			TFEntities.SQUIRREL.getKey(),
			TFEntities.DWARF_RABBIT.getKey(),
			TFEntities.TINY_BIRD.getKey(),
			TFEntities.KOBOLD.getKey(),
			TFEntities.DEATH_TOME.getKey(),
			TFEntities.MOSQUITO_SWARM.getKey(),
			TFEntities.TOWERWOOD_BORER.getKey()
		));

		this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).addAll(List.of(
			TFEntities.PENGUIN.getKey(),
			TFEntities.STABLE_ICE_CORE.getKey(),
			TFEntities.UNSTABLE_ICE_CORE.getKey(),
			TFEntities.SNOW_GUARDIAN.getKey(),
			TFEntities.ICE_CRYSTAL.getKey(),
			TFEntities.WRAITH.getKey(),
			TFEntities.KNIGHT_PHANTOM.getKey(),
			TFEntities.WINTER_WOLF.getKey(),
			TFEntities.YETI.getKey()
		)).addTag(TFEntityTypeTags.BOSSES);

		this.tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(TFEntities.FIRE_BEETLE.getKey());
		this.tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER).addTag(TFEntityTypeTags.BOSSES);
		this.tag(EntityTypeTags.FROG_FOOD).add(TFEntities.MAZE_SLIME.getKey());
		this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).addAll(List.of(
			TFEntities.NAGA.getKey(),
			TFEntities.SQUIRREL.getKey(),
			TFEntities.WRAITH.getKey(),
			TFEntities.CARMINITE_GOLEM.getKey(),
			TFEntities.DEATH_TOME.getKey(),
			TFEntities.UR_GHAST.getKey(),
			TFEntities.CARMINITE_GHASTLING.getKey(),
			TFEntities.KNIGHT_PHANTOM.getKey(),
			TFEntities.SNOW_QUEEN.getKey(),
			TFEntities.PENGUIN.getKey(),
			TFEntities.RAVEN.getKey(),
			TFEntities.SNOW_GUARDIAN.getKey(),
			TFEntities.STABLE_ICE_CORE.getKey(),
			TFEntities.MOSQUITO_SWARM.getKey(),
			TFEntities.UNSTABLE_ICE_CORE.getKey(),
			TFEntities.ICE_CRYSTAL.getKey(),
			TFEntities.CARMINITE_GHASTGUARD.getKey(),
			TFEntities.TINY_BIRD.getKey()));

		this.tag(EntityTypeTags.NON_CONTROLLING_RIDER).add(TFEntities.MAZE_SLIME.getKey());
		this.tag(EntityTypeTags.ARTHROPOD).addAll(List.of(
			TFEntities.CARMINITE_BROODLING.getKey(),
			TFEntities.FIRE_BEETLE.getKey(),
			TFEntities.HEDGE_SPIDER.getKey(),
			TFEntities.HELMET_CRAB.getKey(),
			TFEntities.KING_SPIDER.getKey(),
			TFEntities.PINCH_BEETLE.getKey(),
			TFEntities.SLIME_BEETLE.getKey(),
			TFEntities.SWARM_SPIDER.getKey(),
			TFEntities.TOWERWOOD_BORER.getKey(),
			TFEntities.MOSQUITO_SWARM.getKey()
		));

		this.tag(EntityTypeTags.REDIRECTABLE_PROJECTILE).addAll(List.of(TFEntities.HYDRA_MORTAR.getKey(), TFEntities.LICH_BOLT.getKey()));
		this.tag(EntityTypeTags.IMMUNE_TO_INFESTED).add(TFEntities.TOWERWOOD_BORER.getKey());
		this.tag(EntityTypeTags.IMMUNE_TO_OOZING).add(TFEntities.MAZE_SLIME.getKey());
		this.tag(EntityTypeTags.BOAT).addAll(List.of(
			TFEntities.TWILIGHT_OAK_BOAT.getKey(),
			TFEntities.CANOPY_BOAT.getKey(),
			TFEntities.MANGROVE_BOAT.getKey(),
			TFEntities.DARK_BOAT.getKey(),
			TFEntities.TIME_BOAT.getKey(),
			TFEntities.TRANSFORMATION_BOAT.getKey(),
			TFEntities.MINING_BOAT.getKey(),
			TFEntities.SORTING_BOAT.getKey()));

		this.tag(EntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS).addAll(List.of(
			TFEntities.DEER.getKey(),
			TFEntities.TINY_BIRD.getKey(),
			TFEntities.RAVEN.getKey(),
			TFEntities.BOAR.getKey(),
			TFEntities.BIGHORN_SHEEP.getKey(),
			TFEntities.DWARF_RABBIT.getKey(),
			TFEntities.PENGUIN.getKey(),
			TFEntities.SQUIRREL.getKey()
		));

		this.tag(EntityTypeTags.CANNOT_BE_PUSHED_ONTO_BOATS).addAll(List.of(
			TFEntities.ADHERENT.getKey(),
			TFEntities.CARMINITE_GHASTLING.getKey(),
			TFEntities.CARMINITE_GHASTGUARD.getKey(),
			TFEntities.DEATH_TOME.getKey(),
			TFEntities.MOSQUITO_SWARM.getKey(),
			TFEntities.QUEST_RAM.getKey(),
			TFEntities.RISING_ZOMBIE.getKey(),
			TFEntities.SNOW_GUARDIAN.getKey(),
			TFEntities.WRAITH.getKey()
		)).addTag(TFEntityTypeTags.BOSSES);

		this.tag(TFEntityTypeTags.BOSSES).addAll(List.of(
			TFEntities.NAGA.getKey(),
			TFEntities.LICH.getKey(),
			TFEntities.MINOSHROOM.getKey(),
			TFEntities.HYDRA.getKey(),
			TFEntities.KNIGHT_PHANTOM.getKey(),
			TFEntities.UR_GHAST.getKey(),
			TFEntities.ALPHA_YETI.getKey(),
			TFEntities.SNOW_QUEEN.getKey(),
			TFEntities.PLATEAU_BOSS.getKey()
		));

		this.tag(TFEntityTypeTags.LICH_POPPABLES)
			.addTag(EntityTypeTags.SKELETONS)
			.addAll(List.of(EntityTypes.ZOMBIE.builtInRegistryHolder().key(), EntityTypes.ENDERMAN.builtInRegistryHolder().key(), EntityTypes.SPIDER.builtInRegistryHolder().key(), EntityTypes.CREEPER.builtInRegistryHolder().key(), TFEntities.SWARM_SPIDER.getKey()))
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
			TFEntities.CARMINITE_GOLEM.getKey(),
			TFEntities.DEATH_TOME.getKey(),
			TFEntities.ICE_CRYSTAL.getKey(),
			TFEntities.KNIGHT_PHANTOM.getKey(),
			TFEntities.LICH.getKey(),
			TFEntities.MOSQUITO_SWARM.getKey(),
			TFEntities.SNOW_GUARDIAN.getKey(),
			TFEntities.STABLE_ICE_CORE.getKey(),
			TFEntities.UNSTABLE_ICE_CORE.getKey(),
			TFEntities.WRAITH.getKey()));

		// These entities forcefully take players from the entity they're riding
		this.tag(TFEntityTypeTags.RIDES_OBSTRUCT_SNATCHING).addAll(List.of(TFEntities.PINCH_BEETLE.getKey(), TFEntities.YETI.getKey(), TFEntities.ALPHA_YETI.getKey()));
		this.tag(TFEntityTypeTags.DONT_KILL_BUGS).add(TFEntities.MOONWORM_SHOT.getKey());
		this.tag(TFEntityTypeTags.SORTABLE_ENTITIES).addAll(List.of(
			EntityTypes.CHEST_MINECART.builtInRegistryHolder().key(),
			EntityTypes.HOPPER_MINECART.builtInRegistryHolder().key(),
			EntityTypes.LLAMA.builtInRegistryHolder().key(),
			EntityTypes.TRADER_LLAMA.builtInRegistryHolder().key(),
			EntityTypes.DONKEY.builtInRegistryHolder().key(),
			EntityTypes.MULE.builtInRegistryHolder().key()));

		this.tag(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES).addAll(List.of(
			TFEntities.NAGA.getKey(),
			TFEntities.LICH.getKey(),
			TFEntities.MINOSHROOM.getKey(),
			TFEntities.HYDRA.getKey(),
			TFEntities.UR_GHAST.getKey(),
			TFEntities.ALPHA_YETI.getKey(),
			TFEntities.SNOW_QUEEN.getKey(),
			TFEntities.PLATEAU_BOSS.getKey()
		));

		this.tag(Tags.EntityTypes.BOSSES).addTag(TFEntityTypeTags.BOSSES);
		this.tag(TFEntityTypeTags.LICH_DEFLECTS_PHASE_2).addAll(List.of(TFEntities.WAND_BOLT.getKey(), TFEntities.LICH_BOLT.getKey(), TFEntities.LICH_BOMB.getKey()));

		this.tag(TFEntityTypeTags.HEDGE_DAMAGE_IMMUNE).addAll(List.of(
			EntityTypes.SPIDER.builtInRegistryHolder().key(),
			EntityTypes.CAVE_SPIDER.builtInRegistryHolder().key(),
			TFEntities.HEDGE_SPIDER.getKey(),
			TFEntities.SWARM_SPIDER.getKey(),
			TFEntities.KING_SPIDER.getKey(),
			TFEntities.CARMINITE_BROODLING.getKey(),
			EntityTypes.ITEM.builtInRegistryHolder().key()
		));

		this.tag(TFEntityTypeTags.PROGRESSION_LOCK_VULNERABLE).add(TFEntities.KOBOLD.getKey());
	}


	@Override
	public String getName() {
		return "Twilight Forest Entity Tags";
	}
}
