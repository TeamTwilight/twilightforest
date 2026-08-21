package twilightforest.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import twilightforest.TwilightForestMod;

public class TFEntityTypeTags {

	public static final TagKey<EntityType<?>> BOSSES = create("bosses");
	public static final TagKey<EntityType<?>> LICH_POPPABLES = create("lich_poppables");
	public static final TagKey<EntityType<?>> LIFEDRAIN_DROPS_NO_FLESH = create("lifedrain_drops_no_flesh");
	public static final TagKey<EntityType<?>> RIDES_OBSTRUCT_SNATCHING = create("rides_obstruct_snatching");
	public static final TagKey<EntityType<?>> DONT_KILL_BUGS = create("dont_kill_bugs");
	public static final TagKey<EntityType<?>> SORTABLE_ENTITIES = create("sortable_entities");
	public static final TagKey<EntityType<?>> MULTIPLAYER_INCLUSIVE_ENTITIES = create("multiplayer_inclusive_entities");
	public static final TagKey<EntityType<?>> LICH_DEFLECTS_PHASE_2 = create("lich_deflects_phase_2");

	public static final TagKey<EntityType<?>> AC_RESISTS_ACID = create("alexscaves", "resists_acid");
	public static final TagKey<EntityType<?>> AC_RESISTS_MAGNETS = create("alexscaves", "resists_magnets");
	public static final TagKey<EntityType<?>> AC_RESISTS_TREMORSAURUS_ROAR = create("alexscaves", "resists_tremorsaurus_roar");

	public static final TagKey<EntityType<?>> AETHER_DEFLECTABLE_PROJECTILES = create("aether", "deflectable_projectiles");
	public static final TagKey<EntityType<?>> AETHER_FIRE_MOB = create("aether", "fire_mob");
	public static final TagKey<EntityType<?>> AETHER_PIGS = create("aether", "pigs");

	public static final TagKey<EntityType<?>> AN_JAR_BLACKLIST = create("ars_nouveau", "jar_blacklist");
	public static final TagKey<EntityType<?>> AN_JAR_RELEASE_BLACKLIST = create("ars_nouveau", "jar_release_blacklist");

	public static final TagKey<EntityType<?>> IE_SHADER_BLACKLIST = create("immersiveengineering", "shaderbag/blacklist");

	private static TagKey<EntityType<?>> create(String tagName) {
		return TagKey.create(Registries.ENTITY_TYPE, TwilightForestMod.prefix(tagName));
	}


	private static TagKey<EntityType<?>> create(String modid, String tagName) {
		return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(modid, tagName));
	}
}
