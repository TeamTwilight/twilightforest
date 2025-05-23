package twilightforest.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import twilightforest.TwilightForestMod;

public class TFDamageTypeTags {

	public static final TagKey<DamageType> BREAKS_LICH_SHIELDS = create("breaks_lich_shields");

	private static TagKey<DamageType> create(String name) {
		return TagKey.create(Registries.DAMAGE_TYPE, TwilightForestMod.prefix(name));
	}
}
