package twilightforest.datagen.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.tags.TFEntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class ModdedEntityTypeTagGenerator extends EntityTypeTagsProvider {

	public ModdedEntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFEntityTypeTags.AC_RESISTS_ACID).add(TFEntities.HYDRA.get(), TFEntities.NAGA.get());
		this.tag(TFEntityTypeTags.AC_RESISTS_MAGNETS).addTag(TFEntityTypeTags.BOSSES);
		this.tag(TFEntityTypeTags.AC_RESISTS_TREMORSAURUS_ROAR).add(TFEntities.HYDRA.get(), TFEntities.UR_GHAST.get());

		this.tag(TFEntityTypeTags.AETHER_DEFLECTABLE_PROJECTILES).add(
			TFEntities.NATURE_BOLT.get(),
			TFEntities.LICH_BOLT.get(),
			TFEntities.WAND_BOLT.get(),
			TFEntities.SLIME_BLOB.get(),
			TFEntities.ICE_SNOWBALL.get());

		this.tag(TFEntityTypeTags.AETHER_FIRE_MOB).add(TFEntities.FIRE_BEETLE.get());
		this.tag(TFEntityTypeTags.AETHER_PIGS).add(TFEntities.BOAR.get());

		this.tag(TFEntityTypeTags.AN_JAR_BLACKLIST).addTag(TFEntityTypeTags.BOSSES);
		this.tag(TFEntityTypeTags.AN_JAR_RELEASE_BLACKLIST).addTag(TFEntityTypeTags.BOSSES);

		this.tag(TFEntityTypeTags.IE_SHADER_BLACKLIST).addTag(TFEntityTypeTags.BOSSES);
	}
}
