package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.common.Tags;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDamageTypes;
import twilightforest.tags.TFDamageTypeTags;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGenerator extends KeyTagProvider<DamageType> {

	public DamageTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, Registries.DAMAGE_TYPE, future, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFDamageTypes.GHAST_TEAR, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.DAMAGES_HELMET, Tags.DamageTypes.IS_PHYSICAL, DamageTypeTags.AVOIDS_GUARDIAN_THORNS);
		this.tag(TFDamageTypes.HYDRA_BITE, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.HYDRA_FIRE, DamageTypeTags.IS_FIRE, DamageTypeTags.IGNITES_ARMOR_STANDS, DamageTypeTags.AVOIDS_GUARDIAN_THORNS);
		this.tag(TFDamageTypes.HYDRA_MORTAR, DamageTypeTags.IS_FIRE, DamageTypeTags.IGNITES_ARMOR_STANDS, DamageTypeTags.AVOIDS_GUARDIAN_THORNS);
		this.tag(TFDamageTypes.LICH_BOLT, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, DamageTypeTags.WITCH_RESISTANT_TO, TFDamageTypeTags.BREAKS_LICH_SHIELDS, Tags.DamageTypes.IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		this.tag(TFDamageTypes.LICH_BOMB, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, DamageTypeTags.WITCH_RESISTANT_TO, Tags.DamageTypes.IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR, DamageTypeTags.IS_EXPLOSION);
		this.tag(TFDamageTypes.CHILLING_BREATH, Tags.DamageTypes.IS_MAGIC);
		this.tag(TFDamageTypes.SQUISH, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.THROWN_AXE, DamageTypeTags.IS_PROJECTILE, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.THROWN_PICKAXE, DamageTypeTags.IS_PROJECTILE, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.THORNS, Tags.DamageTypes.IS_ENVIRONMENT, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.KNIGHTMETAL, Tags.DamageTypes.IS_ENVIRONMENT);
		this.tag(TFDamageTypes.FIERY, DamageTypeTags.IS_FIRE, Tags.DamageTypes.IS_ENVIRONMENT, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.FIRE_JET, DamageTypeTags.IS_FIRE, Tags.DamageTypes.IS_ENVIRONMENT, Tags.DamageTypes.IS_PHYSICAL, DamageTypeTags.IGNITES_ARMOR_STANDS);
		this.tag(TFDamageTypes.REACTOR, Tags.DamageTypes.IS_ENVIRONMENT, Tags.DamageTypes.IS_MAGIC);
		this.tag(TFDamageTypes.SLIDER, Tags.DamageTypes.IS_ENVIRONMENT, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.THROWN_BLOCK, DamageTypeTags.DAMAGES_HELMET, DamageTypeTags.IS_PROJECTILE, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.AXING, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.SLAM, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.NO_ANGER, Tags.DamageTypes.IS_PHYSICAL, DamageTypeTags.AVOIDS_GUARDIAN_THORNS);
		this.tag(TFDamageTypes.YEETED, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_FALL, Tags.DamageTypes.IS_PHYSICAL, DamageTypeTags.AVOIDS_GUARDIAN_THORNS);
		this.tag(TFDamageTypes.ANT, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.HAUNT, Tags.DamageTypes.IS_MAGIC);
		this.tag(TFDamageTypes.CLAMPED, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.SCORCHED, DamageTypeTags.IS_FIRE, DamageTypeTags.IGNITES_ARMOR_STANDS);
		this.tag(TFDamageTypes.FROZEN, Tags.DamageTypes.IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		this.tag(TFDamageTypes.SPIKED, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.LEAF_BRAIN, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, DamageTypeTags.WITCH_RESISTANT_TO, Tags.DamageTypes.IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		this.tag(TFDamageTypes.LOST_WORDS, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, DamageTypeTags.WITCH_RESISTANT_TO, Tags.DamageTypes.IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		this.tag(TFDamageTypes.SCHOOLED, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, DamageTypeTags.WITCH_RESISTANT_TO, Tags.DamageTypes.IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		this.tag(TFDamageTypes.SNOWBALL_FIGHT, DamageTypeTags.IS_PROJECTILE, Tags.DamageTypes.IS_MAGIC);
		this.tag(TFDamageTypes.TWILIGHT_SCEPTER, DamageTypeTags.IS_PROJECTILE, TFDamageTypeTags.BREAKS_LICH_SHIELDS, Tags.DamageTypes.IS_MAGIC);
		this.tag(TFDamageTypes.LIFEDRAIN, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, Tags.DamageTypes.IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		this.tag(TFDamageTypes.EXPIRED, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_RESISTANCE, DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL, DamageTypeTags.BYPASSES_INVULNERABILITY, Tags.DamageTypes.IS_TECHNICAL, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		this.tag(TFDamageTypes.FALLING_ICE, DamageTypeTags.BYPASSES_ENCHANTMENTS, Tags.DamageTypes.IS_ENVIRONMENT, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.MOONWORM, Tags.DamageTypes.IS_PHYSICAL);
		this.tag(TFDamageTypes.ACID_RAIN, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.WITCH_RESISTANT_TO, Tags.DamageTypes.IS_ENVIRONMENT, Tags.DamageTypes.IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		this.tag(TFDamageTypes.OMINOUS_FIRE, Tags.DamageTypes.IS_MAGIC, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.WITHER_IMMUNE_TO, DamageTypeTags.NO_KNOCKBACK, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES);

		this.tag(DamageTypes.MAGIC, TFDamageTypeTags.BREAKS_LICH_SHIELDS);
		this.tag(DamageTypes.INDIRECT_MAGIC, TFDamageTypeTags.BREAKS_LICH_SHIELDS);
		this.tag(DamageTypes.SONIC_BOOM, TFDamageTypeTags.BREAKS_LICH_SHIELDS);
	}

	@SafeVarargs
	private void tag(ResourceKey<DamageType> type, TagKey<DamageType>... tags) {
		for (TagKey<DamageType> key : tags) {
			tag(key).add(type);
		}
	}
}
