package twilightforest.forge;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/**
 * L'acces par emplacement au contenu d'un conteneur, que Forge ne fournit pas.
 *
 * <p>{@code ItemContainerContents} est une classe du jeu, mais son API publique ne donne que des
 * flux : {@code stream()}, {@code nonEmptyStream()}, {@code copyOne()}. NeoForge y ajoute
 * {@code getSlots()} et {@code getStackInSlot(int)} par une interface d'extension ; Forge n'a pas
 * d'equivalent.</p>
 *
 * <p>Les deux methodes se rendent depuis {@code stream()}, qui parcourt exactement les memes
 * emplacements — de zero jusqu'au dernier non vide — donc dans le meme ordre et avec le meme
 * compte.</p>
 */
public final class ItemContainerContentsAccess {
	private ItemContainerContentsAccess() {
	}

	/** Le nombre d'emplacements retenus, dernier emplacement non vide compris. */
	public static int getSlots(ItemContainerContents contents) {
		return (int) contents.stream().count();
	}

	/** La pile d'un emplacement, ou une pile vide si l'indice deborde. */
	public static ItemStack getStackInSlot(ItemContainerContents contents, int index) {
		if (index < 0) {
			return ItemStack.EMPTY;
		}
		return contents.stream().skip(index).findFirst().orElse(ItemStack.EMPTY);
	}
}
