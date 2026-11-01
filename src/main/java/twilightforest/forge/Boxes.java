package twilightforest.forge;

import twilightforest.forge.Boxes;
import net.minecraft.world.phys.AABB;

/** Boites englobantes utilitaires. */
public final class Boxes {
	private Boxes() {}

	/**
	 * Une boite qui contient tout.
	 *
	 * <p>NeoForge expose `Boxes.INFINITE` ; le jeu ne l'a pas. Les particules qui s'en servent veulent
	 * simplement ne jamais etre eliminees par le test de visibilite.</p>
	 */
	public static final AABB INFINITE = new AABB(
			Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
			Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
}
