package twilightforest.forge;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

/**
 * Curios, en son absence.
 *
 * <p>Le code de compatibilite tierce est hors du build tant que les artefacts Forge correspondants
 * ne sont pas rebranches, mais quatre fichiers du coeur l'interrogent. Ce bouchon repond « rien
 * d'equipe », ce qui est exact : sans Curios, aucun objet n'est porte dans ses emplacements.</p>
 *
 * <p><b>A remplacer</b> par le vrai pont quand Curios pour Forge sera ajoute aux dependances.</p>
 */
public final class CuriosStub {
	private CuriosStub() {}

	public static boolean isCurioEquipped(LivingEntity entity, Predicate<ItemStack> filter) {
		return false;
	}

	public static boolean isCurioEquippedAndVisible(LivingEntity entity, Predicate<ItemStack> filter) {
		return false;
	}

	public static boolean findAndConsumeCurio(Item item, Player player) {
		return false;
	}
}
