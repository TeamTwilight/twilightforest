package twilightforest.forge;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

/**
 * Construction d'une source de degats a partir de sa cle.
 *
 * <p>La fabrique {@code DamageSources.source} du jeu est privee ; NeoForge l'ouvre par
 * transformateur d'acces. Plutot que d'elargir l'acces, on construit la source directement : le
 * resultat est le meme.</p>
 */
public final class TFDamage {
	private TFDamage() {}

	public static DamageSource source(Entity context, ResourceKey<DamageType> key) {
		return new DamageSource(context.level().registryAccess()
				.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key));
	}
}
