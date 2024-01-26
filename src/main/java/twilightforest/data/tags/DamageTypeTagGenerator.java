package twilightforest.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDamageTypes;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGenerator extends TagsProvider<DamageType> {

	public static final TagKey<DamageType> BREAKS_LICH_SHIELDS = create("breaks_lich_shields");

	public DamageTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
		super(output, Registries.DAMAGE_TYPE, future, TwilightForestMod.ID, helper);
	}

	protected void addTags(HolderLookup.Provider provider) {
		this.tag(DamageTypeTags.BYPASSES_ARMOR).add(TFDamageTypes.GHAST_TEAR, TFDamageTypes.SLAM, TFDamageTypes.LICH_BOLT, TFDamageTypes.LICH_BOMB, TFDamageTypes.LEAF_BRAIN, TFDamageTypes.LOST_WORDS, TFDamageTypes.SCHOOLED, TFDamageTypes.LIFEDRAIN, TFDamageTypes.EXPIRED, TFDamageTypes.YEETED, TFDamageTypes.ACID_RAIN);
		this.tag(DamageTypeTags.DAMAGES_HELMET).add(TFDamageTypes.GHAST_TEAR, TFDamageTypes.THROWN_BLOCK);
		this.tag(DamageTypeTags.IS_PROJECTILE).add(TFDamageTypes.TWILIGHT_SCEPTER, TFDamageTypes.LICH_BOLT, TFDamageTypes.LICH_BOMB, TFDamageTypes.THROWN_PICKAXE, TFDamageTypes.THROWN_AXE, TFDamageTypes.THROWN_BLOCK, TFDamageTypes.LEAF_BRAIN, TFDamageTypes.LOST_WORDS, TFDamageTypes.SCHOOLED, TFDamageTypes.SNOWBALL_FIGHT, TFDamageTypes.LIFEDRAIN);
		this.tag(DamageTypeTags.IS_FIRE).add(TFDamageTypes.HYDRA_MORTAR, TFDamageTypes.HYDRA_FIRE);
		this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(TFDamageTypes.EXPIRED);
		this.tag(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL).add(TFDamageTypes.EXPIRED);
		this.tag(DamageTypeTags.IS_FALL).add(TFDamageTypes.YEETED);
		this.tag(DamageTypeTags.NO_ANGER).add(TFDamageTypes.SLAM);
		this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(TFDamageTypes.EXPIRED);
		this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(TFDamageTypes.FALLING_ICE);
		this.tag(DamageTypeTags.WITCH_RESISTANT_TO).add(TFDamageTypes.LICH_BOMB, TFDamageTypes.LICH_BOLT, TFDamageTypes.LEAF_BRAIN, TFDamageTypes.LOST_WORDS, TFDamageTypes.SCHOOLED, TFDamageTypes.ACID_RAIN);
		this.tag(BREAKS_LICH_SHIELDS).add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.SONIC_BOOM, TFDamageTypes.LICH_BOLT, TFDamageTypes.TWILIGHT_SCEPTER);
	}

	private static TagKey<DamageType> create(String name) {
		return TagKey.create(Registries.DAMAGE_TYPE, TwilightForestMod.prefix(name));
	}
}
