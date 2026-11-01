package twilightforest.forge;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * Designation des modeles charges en plus de ceux du jeu.
 *
 * <p>NeoForge a une variante « standalone » qui charge un modele a son chemin exact. Forge n'a pas
 * cette constante, mais il traite de la meme facon tout ce qui passe par
 * {@code ModelEvent.RegisterAdditional} : son {@code ModelBakery} charge
 * {@code models/<chemin de l'identifiant>.json}, quelle que soit la variante, et range le modele
 * sous l'identifiant complet donne. Il suffit donc de garder le chemin tel quel ; la variante ne
 * sert qu'a distinguer ces modeles de ceux d'un bloc ou d'un objet de meme nom.</p>
 */
public final class TFModels {
	private TFModels() {}

	public static ModelResourceLocation standalone(ResourceLocation path) {
		return new ModelResourceLocation(path, "standalone");
	}
}
