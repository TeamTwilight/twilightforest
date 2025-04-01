package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import twilightforest.datagen.data.tags.compat.ModdedEntityTypeTagGenerator;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.init.TFEntities;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagGenerator extends ModdedEntityTypeTagGenerator {

	public EntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);
		this.tag(EntityTypeTags.SKELETONS).add(TFEntities.SKELETON_DRUID.get(), TFEntities.LICH.get(), TFEntities.KNIGHT_PHANTOM.get());
		this.tag(EntityTypeTags.ZOMBIES).add(TFEntities.LICH_MINION.get(), TFEntities.LOYAL_ZOMBIE.get(), TFEntities.RISING_ZOMBIE.get());
		this.tag(EntityTypeTags.ARROWS).add(TFEntities.ICE_ARROW.get(), TFEntities.SEEKER_ARROW.get());
		this.tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(TFEntities.FIRE_BEETLE.get());
		this.tag(EntityTypeTags.FROG_FOOD).add(TFEntities.MAZE_SLIME.get());

		this.tag(TFEntityTypeTags.BOSSES).add(
			TFEntities.NAGA.get(),
			TFEntities.LICH.get(),
			TFEntities.MINOSHROOM.get(),
			TFEntities.HYDRA.get(),
			TFEntities.KNIGHT_PHANTOM.get(),
			TFEntities.UR_GHAST.get(),
			TFEntities.ALPHA_YETI.get(),
			TFEntities.SNOW_QUEEN.get(),
			TFEntities.PLATEAU_BOSS.get()
		);

		this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(
			TFEntities.NATURE_BOLT.get(),
			TFEntities.LICH_BOLT.get(),
			TFEntities.WAND_BOLT.get(),
			TFEntities.LICH_BOMB.get(),
			TFEntities.MOONWORM_SHOT.get(),
			TFEntities.SLIME_BLOB.get(),
			TFEntities.THROWN_WEP.get(),
			TFEntities.THROWN_ICE.get(),
			TFEntities.FALLING_ICE.get(),
			TFEntities.ICE_SNOWBALL.get(),
			TFEntities.CHAIN_BLOCK.get()
		);

		this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(
			TFEntities.PENGUIN.get(),
			TFEntities.STABLE_ICE_CORE.get(),
			TFEntities.UNSTABLE_ICE_CORE.get(),
			TFEntities.SNOW_GUARDIAN.get(),
			TFEntities.ICE_CRYSTAL.get()
		).add(
			TFEntities.RAVEN.get(),
			TFEntities.SQUIRREL.get(),
			TFEntities.DWARF_RABBIT.get(),
			TFEntities.TINY_BIRD.get(),
			TFEntities.KOBOLD.get(),
			TFEntities.DEATH_TOME.get(),
			TFEntities.MOSQUITO_SWARM.get(),
			TFEntities.TOWERWOOD_BORER.get()
		);

		this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(
			TFEntities.PENGUIN.get(),
			TFEntities.STABLE_ICE_CORE.get(),
			TFEntities.UNSTABLE_ICE_CORE.get(),
			TFEntities.SNOW_GUARDIAN.get(),
			TFEntities.ICE_CRYSTAL.get()
		).add(
			TFEntities.WRAITH.get(),
			TFEntities.KNIGHT_PHANTOM.get(),
			TFEntities.WINTER_WOLF.get(),
			TFEntities.YETI.get()
		).addTag(TFEntityTypeTags.BOSSES);

		this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(
			TFEntities.NAGA.get(),
			TFEntities.SQUIRREL.get(),
			TFEntities.WRAITH.get(),
			TFEntities.CARMINITE_GOLEM.get(),
			TFEntities.DEATH_TOME.get(),
			TFEntities.UR_GHAST.get(),
			TFEntities.CARMINITE_GHASTLING.get(),
			TFEntities.KNIGHT_PHANTOM.get(),
			TFEntities.SNOW_QUEEN.get(),
			TFEntities.PENGUIN.get(),
			TFEntities.RAVEN.get(),
			TFEntities.SNOW_GUARDIAN.get(),
			TFEntities.STABLE_ICE_CORE.get(),
			TFEntities.MOSQUITO_SWARM.get(),
			TFEntities.UNSTABLE_ICE_CORE.get(),
			TFEntities.ICE_CRYSTAL.get(),
			TFEntities.CARMINITE_GHASTGUARD.get(),
			TFEntities.TINY_BIRD.get());

		this.tag(TFEntityTypeTags.LICH_POPPABLES)
			.addTag(EntityTypeTags.SKELETONS)
			.add(EntityType.ZOMBIE, EntityType.ENDERMAN, EntityType.SPIDER, EntityType.CREEPER, TFEntities.SWARM_SPIDER.get())
			.remove(Tags.EntityTypes.BOSSES);

		this.tag(TFEntityTypeTags.LIFEDRAIN_DROPS_NO_FLESH).addTag(EntityTypeTags.SKELETONS).addTag(EntityTypeTags.FROG_FOOD).add(
			EntityType.BLAZE,
			EntityType.BREEZE,
			EntityType.IRON_GOLEM,
			EntityType.PHANTOM,
			EntityType.SHULKER,
			EntityType.SKELETON_HORSE,
			EntityType.SNOW_GOLEM,
			EntityType.VEX,
			EntityType.WITHER,
			TFEntities.CARMINITE_GOLEM.get(),
			TFEntities.DEATH_TOME.get(),
			TFEntities.ICE_CRYSTAL.get(),
			TFEntities.KNIGHT_PHANTOM.get(),
			TFEntities.LICH.get(),
			TFEntities.MOSQUITO_SWARM.get(),
			TFEntities.SNOW_GUARDIAN.get(),
			TFEntities.STABLE_ICE_CORE.get(),
			TFEntities.UNSTABLE_ICE_CORE.get(),
			TFEntities.WRAITH.get());

		// These entities forcefully take players from the entity they're riding
		this.tag(TFEntityTypeTags.RIDES_OBSTRUCT_SNATCHING).add(TFEntities.PINCH_BEETLE.get(), TFEntities.YETI.get(), TFEntities.ALPHA_YETI.get());

		this.tag(TFEntityTypeTags.DONT_KILL_BUGS).add(TFEntities.MOONWORM_SHOT.get());

		this.tag(TFEntityTypeTags.SORTABLE_ENTITIES).add(
			EntityType.CHEST_MINECART,
			EntityType.HOPPER_MINECART,
			EntityType.LLAMA,
			EntityType.TRADER_LLAMA,
			EntityType.DONKEY,
			EntityType.MULE);

		this.tag(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES).add(
			TFEntities.NAGA.get(),
			TFEntities.LICH.get(),
			TFEntities.MINOSHROOM.get(),
			TFEntities.HYDRA.get(),
			TFEntities.UR_GHAST.get(),
			TFEntities.ALPHA_YETI.get(),
			TFEntities.SNOW_QUEEN.get(),
			TFEntities.PLATEAU_BOSS.get()
		);

		this.tag(Tags.EntityTypes.BOSSES).addTag(TFEntityTypeTags.BOSSES);
		this.tag(EntityTypeTags.ARTHROPOD).add(
			TFEntities.CARMINITE_BROODLING.get(),
			TFEntities.FIRE_BEETLE.get(),
			TFEntities.HEDGE_SPIDER.get(),
			TFEntities.HELMET_CRAB.get(),
			TFEntities.KING_SPIDER.get(),
			TFEntities.PINCH_BEETLE.get(),
			TFEntities.SLIME_BEETLE.get(),
			TFEntities.SWARM_SPIDER.get(),
			TFEntities.TOWERWOOD_BORER.get());
		this.tag(EntityTypeTags.UNDEAD).add(TFEntities.WRAITH.get());
		this.tag(EntityTypeTags.IMMUNE_TO_OOZING).add(TFEntities.MAZE_SLIME.get());
		this.tag(EntityTypeTags.IMMUNE_TO_INFESTED).add(TFEntities.TOWERWOOD_BORER.get());
		this.tag(EntityTypeTags.REDIRECTABLE_PROJECTILE).add(TFEntities.HYDRA_MORTAR.get(), TFEntities.LICH_BOLT.get());
		this.tag(TFEntityTypeTags.LICH_DEFLECTS_PHASE_2).add(TFEntities.WAND_BOLT.get(), TFEntities.LICH_BOLT.get(), TFEntities.LICH_BOMB.get());
	}


	@Override
	public String getName() {
		return "Twilight Forest Entity Tags";
	}
}
