package twilightforest.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.compat.ModdedEntityTagGenerator;
import twilightforest.init.TFEntities;

import java.util.concurrent.CompletableFuture;

public class EntityTagGenerator extends ModdedEntityTagGenerator {
	public static final TagKey<EntityType<?>> BOSSES = create(TwilightForestMod.prefix("bosses"));
	public static final TagKey<EntityType<?>> LICH_POPPABLES = create(TwilightForestMod.prefix("lich_poppables"));
	public static final TagKey<EntityType<?>> LIFEDRAIN_DROPS_NO_FLESH = create(TwilightForestMod.prefix("lifedrain_drops_no_flesh"));
	public static final TagKey<EntityType<?>> RIDES_OBSTRUCT_SNATCHING = create(TwilightForestMod.prefix("rides_obstruct_snatching"));
	public static final TagKey<EntityType<?>> DONT_KILL_BUGS = create(TwilightForestMod.prefix("dont_kill_bugs"));
	public static final TagKey<EntityType<?>> SORTABLE_ENTITIES = create(TwilightForestMod.prefix("sortable_entities"));

	public EntityTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);
		tag(EntityTypeTags.SKELETONS).add(TFEntities.SKELETON_DRUID.get());
		tag(EntityTypeTags.ARROWS).add(TFEntities.ICE_ARROW.get(), TFEntities.SEEKER_ARROW.get());
		tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(TFEntities.FIRE_BEETLE.get());
		tag(EntityTypeTags.FROG_FOOD).add(TFEntities.MAZE_SLIME.get());

		tag(BOSSES).add(
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

		tag(EntityTypeTags.IMPACT_PROJECTILES).add(
				TFEntities.NATURE_BOLT.get(),
				TFEntities.LICH_BOLT.get(),
				TFEntities.WAND_BOLT.get(),
				TFEntities.LICH_BOMB.get(),
				TFEntities.MOONWORM_SHOT.get(),
				TFEntities.SLIME_BLOB.get(),
				TFEntities.THROWN_WEP.get(),
				TFEntities.THROWN_ICE.get(),
				TFEntities.FALLING_ICE.get(),
				TFEntities.ICE_SNOWBALL.get()
		);

		tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(
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

		tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(
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
		).addTag(BOSSES);

		tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(
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

		tag(LICH_POPPABLES).addTag(EntityTypeTags.SKELETONS).add(EntityType.ZOMBIE, EntityType.ENDERMAN, EntityType.SPIDER, EntityType.CREEPER, TFEntities.SWARM_SPIDER.get());

		tag(LIFEDRAIN_DROPS_NO_FLESH).addTag(EntityTypeTags.SKELETONS).addTag(EntityTypeTags.FROG_FOOD).add(
				EntityType.BLAZE,
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
		tag(RIDES_OBSTRUCT_SNATCHING).add(TFEntities.PINCH_BEETLE.get(), TFEntities.YETI.get(), TFEntities.ALPHA_YETI.get());

		tag(DONT_KILL_BUGS).add(TFEntities.MOONWORM_SHOT.get());

		tag(SORTABLE_ENTITIES).add(
				EntityType.CHEST_MINECART,
				EntityType.HOPPER_MINECART,
				EntityType.LLAMA,
				EntityType.TRADER_LLAMA,
				EntityType.DONKEY,
				EntityType.MULE);

		tag(Tags.EntityTypes.BOSSES).addTag(BOSSES);
	}

	private static TagKey<EntityType<?>> create(ResourceLocation rl) {
		return TagKey.create(Registries.ENTITY_TYPE, rl);
	}

	@Override
	public String getName() {
		return "Twilight Forest Entity Tags";
	}
}
