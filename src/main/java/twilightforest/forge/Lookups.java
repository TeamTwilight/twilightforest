package twilightforest.forge;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * Acces aux entrees de registre a partir d'une cle.
 *
 * <p>NeoForge ajoute {@code holderOrThrow} sur {@code HolderLookup.Provider} ; le jeu ne l'a pas et
 * demande de passer par le registre concerne. La cle sait de quel registre elle vient, donc le
 * detour se fait ici une fois pour toutes.</p>
 */
public final class Lookups {
	private Lookups() {}

	public static <T> Holder.Reference<T> holder(HolderLookup.Provider provider, ResourceKey<T> key) {
		ResourceKey<? extends Registry<T>> registry = ResourceKey.createRegistryKey(key.registry());
		return provider.lookupOrThrow(registry).getOrThrow(key);
	}
}
