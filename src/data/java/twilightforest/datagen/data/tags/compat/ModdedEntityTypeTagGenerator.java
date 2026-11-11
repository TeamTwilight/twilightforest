package twilightforest.datagen.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.tags.TFEntityTypeTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModdedEntityTypeTagGenerator extends EntityTypeTagsProvider {

	public ModdedEntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFEntityTypeTags.AC_RESISTS_ACID).addAll(List.of(TFEntities.HYDRA.getKey(), TFEntities.NAGA.getKey()));
		this.tag(TFEntityTypeTags.AC_RESISTS_MAGNETS).addTag(TFEntityTypeTags.BOSSES);
		this.tag(TFEntityTypeTags.AC_RESISTS_TREMORSAURUS_ROAR).addAll(List.of(TFEntities.HYDRA.getKey(), TFEntities.UR_GHAST.getKey()));

		this.tag(TFEntityTypeTags.AETHER_DEFLECTABLE_PROJECTILES).addAll(List.of(
			TFEntities.NATURE_BOLT.getKey(),
			TFEntities.LICH_BOLT.getKey(),
			TFEntities.WAND_BOLT.getKey(),
			TFEntities.SLIME_BLOB.getKey(),
			TFEntities.ICE_SNOWBALL.getKey()));

		this.tag(TFEntityTypeTags.AETHER_FIRE_MOB).add(TFEntities.FIRE_BEETLE.getKey());
		this.tag(TFEntityTypeTags.AETHER_PIGS).add(TFEntities.BOAR.getKey());

		this.tag(TFEntityTypeTags.AN_JAR_BLACKLIST).addTag(TFEntityTypeTags.BOSSES);
		this.tag(TFEntityTypeTags.AN_JAR_RELEASE_BLACKLIST).addTag(TFEntityTypeTags.BOSSES);

		this.tag(TFEntityTypeTags.IE_SHADER_BLACKLIST).addTag(TFEntityTypeTags.BOSSES);
	}
}
