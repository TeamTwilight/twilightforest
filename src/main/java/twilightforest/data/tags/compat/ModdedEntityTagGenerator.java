package twilightforest.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.init.TFEntities;

import java.util.concurrent.CompletableFuture;

public class ModdedEntityTagGenerator extends EntityTypeTagsProvider {

	public static final TagKey<EntityType<?>> AC_RESISTS_ACID = createTagFor("alexscaves", "resists_acid");
	public static final TagKey<EntityType<?>> AC_RESISTS_MAGNETS = createTagFor("alexscaves", "resists_magnets");
	public static final TagKey<EntityType<?>> AC_RESISTS_TREMORSAURUS_ROAR = createTagFor("alexscaves", "resists_tremorsaurus_roar");

	public static final TagKey<EntityType<?>> AETHER_DEFLECTABLE_PROJECTILES = createTagFor("aether", "deflectable_projectiles");
	public static final TagKey<EntityType<?>> AETHER_FIRE_MOB = createTagFor("aether", "fire_mob");
	public static final TagKey<EntityType<?>> AETHER_PIGS = createTagFor("aether", "pigs");

	public static final TagKey<EntityType<?>> AN_JAR_BLACKLIST = createTagFor("ars_nouveau", "jar_blacklist");
	public static final TagKey<EntityType<?>> AN_JAR_RELEASE_BLACKLIST = createTagFor("ars_nouveau", "jar_release_blacklist");

	public static final TagKey<EntityType<?>> IE_SHADER_BLACKLIST = createTagFor("immersiveengineering", "shaderbag/blacklist");

	public ModdedEntityTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, TwilightForestMod.ID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(AC_RESISTS_ACID).add(TFEntities.HYDRA.get(), TFEntities.NAGA.get());
		tag(AC_RESISTS_MAGNETS).addTag(EntityTagGenerator.BOSSES);
		tag(AC_RESISTS_TREMORSAURUS_ROAR).add(TFEntities.HYDRA.get(), TFEntities.UR_GHAST.get());

		tag(AETHER_DEFLECTABLE_PROJECTILES).add(
				TFEntities.NATURE_BOLT.get(),
				TFEntities.LICH_BOLT.get(),
				TFEntities.WAND_BOLT.get(),
				TFEntities.SLIME_BLOB.get(),
				TFEntities.ICE_SNOWBALL.get());

		tag(AETHER_FIRE_MOB).add(TFEntities.FIRE_BEETLE.get());
		tag(AETHER_PIGS).add(TFEntities.BOAR.get());

		tag(AN_JAR_BLACKLIST).addTag(EntityTagGenerator.BOSSES);
		tag(AN_JAR_RELEASE_BLACKLIST).addTag(EntityTagGenerator.BOSSES);

		tag(IE_SHADER_BLACKLIST).addTag(EntityTagGenerator.BOSSES);
	}

	private static TagKey<EntityType<?>> createTagFor(String modid, String tagName) {
		return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(modid, tagName));
	}

}
