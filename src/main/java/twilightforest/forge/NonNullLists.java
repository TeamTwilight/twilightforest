package twilightforest.forge;

import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;

import java.util.List;
import java.util.function.Function;

/**
 * Les fabriques de {@code NonNullList} que NeoForge ajoute et que le jeu n'a pas.
 *
 * <p>Le jeu ne propose que {@code create}, {@code createWithCapacity}, {@code withSize} et
 * {@code of} — cette derniere exigeant une valeur par defaut. NeoForge y ajoute un {@code copyOf}
 * qui reprend une liste telle quelle, et un {@code codecOf} qui lit directement une liste non
 * nulle. Les deux se rendent sans peine.</p>
 */
public final class NonNullLists {
	private NonNullLists() {
	}

	/** Une liste non nulle reprenant le contenu d'une autre, dans l'ordre. */
	public static <E> NonNullList<E> copyOf(List<E> items) {
		NonNullList<E> list = NonNullList.createWithCapacity(items.size());
		list.addAll(items);
		return list;
	}

	/** Le codec d'une liste non nulle, bati sur celui de ses elements. */
	public static <E> Codec<NonNullList<E>> codecOf(Codec<E> elementCodec) {
		return elementCodec.listOf().xmap(NonNullLists::copyOf, Function.identity());
	}
}
