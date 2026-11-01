package twilightforest.forge;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

/**
 * Les etiquettes de convention pour les types de degats.
 *
 * <p>NeoForge les fournit sous {@code Tags.DamageTypes} ; Forge n'a pas cette famille. Ce sont de
 * simples etiquettes de l'espace de noms commun {@code c:}, celui que les mods se partagent : les
 * redeclarer ici donne exactement les memes cles, et reste compatible avec les autres mods qui les
 * utilisent.</p>
 */
public final class CommonDamageTypeTags {
	private CommonDamageTypeTags() {}

	public static final TagKey<DamageType> IS_PHYSICAL = tag("is_physical");
	public static final TagKey<DamageType> IS_MAGIC = tag("is_magic");
	public static final TagKey<DamageType> IS_ENVIRONMENT = tag("is_environment");
	public static final TagKey<DamageType> IS_TECHNICAL = tag("is_technical");

	private static TagKey<DamageType> tag(String name) {
		return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("c", name));
	}
}
