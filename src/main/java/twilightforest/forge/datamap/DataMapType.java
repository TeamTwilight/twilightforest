package twilightforest.forge.datamap;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Une table de donnees attachee aux entrees d'un registre.
 *
 * <p>NeoForge appelle cela une « data map » : un fichier de datapack associe une valeur a chaque
 * entree d'un registre, sans toucher a l'entree elle-meme. Le mod s'en sert pour la corne
 * d'effritement, la poudre de transformation, le feu inquietant et la couleur des cartes.</p>
 *
 * <p>Forge n'a rien d'equivalent. La forme est redonnee ici, avec le meme format de fichier, pour
 * que les donnees produites par la generation restent utilisables telles quelles.</p>
 *
 * @param <R> le type du registre concerne
 * @param <V> le type de la valeur associee
 */
public final class DataMapType<R, V> {
	private final ResourceLocation id;
	private final ResourceKey<Registry<R>> registry;
	private final Codec<V> codec;
	private final Map<ResourceLocation, V> values = new HashMap<>();
	/**
	 * Entrees designees par un tag ({@code "#minecraft:coal_ores"}), comme NeoForge le permet. Elles
	 * sont resolues a la lecture, quand les tags sont charges ; l'ordre des fichiers est garde.
	 */
	private final Map<TagKey<R>, V> tagValues = new LinkedHashMap<>();

	private DataMapType(Builder<R, V> builder) {
		this.id = builder.id;
		this.registry = builder.registry;
		this.codec = builder.codec;
	}

	public static <R, V> Builder<R, V> builder(ResourceLocation id, ResourceKey<Registry<R>> registry, Codec<V> codec) {
		return new Builder<>(id, registry, codec);
	}

	public ResourceLocation id() {
		return this.id;
	}

	public ResourceKey<Registry<R>> registry() {
		return this.registry;
	}

	public Codec<V> codec() {
		return this.codec;
	}

	/** L'entree directe prime ; a defaut, le premier tag qui contient l'entree. */
	@Nullable
	V lookup(Holder<R> holder) {
		V direct = holder.unwrapKey().map(key -> this.values.get(key.location())).orElse(null);
		if (direct != null) return direct;
		for (Map.Entry<TagKey<R>, V> tag : this.tagValues.entrySet()) {
			if (holder.is(tag.getKey())) return tag.getValue();
		}
		return null;
	}

	void clear() {
		this.values.clear();
		this.tagValues.clear();
	}

	void put(ResourceLocation entry, V value) {
		this.values.put(entry, value);
	}

	void putTag(TagKey<R> tag, V value) {
		this.tagValues.put(tag, value);
	}

	/**
	 * Le constructeur fluide de NeoForge.
	 *
	 * <p>{@code synced} est accepte puis ignore : la synchronisation vers le client suppose la
	 * couche reseau et n'est pas branchee. Les tables restent donc lues cote serveur.</p>
	 */
	public static final class Builder<R, V> {
		private final ResourceLocation id;
		private final ResourceKey<Registry<R>> registry;
		private final Codec<V> codec;

		private Builder(ResourceLocation id, ResourceKey<Registry<R>> registry, Codec<V> codec) {
			this.id = id;
			this.registry = registry;
			this.codec = codec;
		}

		public Builder<R, V> synced(Codec<V> networkCodec, boolean mandatory) {
			return this;
		}

		public DataMapType<R, V> build() {
			return DataMaps.declare(new DataMapType<>(this));
		}
	}
}
