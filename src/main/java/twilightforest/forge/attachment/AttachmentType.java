package twilightforest.forge.attachment;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Une donnee attachee a une entite, sur le modele des attachements de NeoForge.
 *
 * <p>Forge n'a pas d'equivalent : il propose les capabilities, dont le modele est different — un
 * fournisseur par objet, pas une donnee nommee. Le mod declare vingt-trois attachements et les lit
 * a cent quarante endroits, presque tous sur des entites. Plutot que de convertir chaque usage en
 * capability, on redonne ici la forme d'origine et on la range dans une seule capability portee par
 * l'entite ({@link Attachments}).</p>
 *
 * <p>L'identifiant sert de cle dans le NBT sauvegarde. Il est passe explicitement, la ou NeoForge le
 * tirait du registre.</p>
 *
 * @param <T> le type de la donnee attachee
 */
public final class AttachmentType<T> {
	private final String id;
	private final Supplier<T> defaultValue;
	@Nullable
	private final Codec<T> codec;
	@Nullable
	private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
	private final boolean copyOnDeath;

	private AttachmentType(Builder<T> builder) {
		this.id = builder.id;
		this.defaultValue = builder.defaultValue;
		this.codec = builder.codec;
		this.streamCodec = builder.streamCodec;
		this.copyOnDeath = builder.copyOnDeath;
	}

	public static <T> Builder<T> builder(String id, Supplier<T> defaultValue) {
		return new Builder<>(id, defaultValue);
	}

	public String id() {
		return this.id;
	}

	public T defaultValue() {
		return this.defaultValue.get();
	}

	/** Le codec de sauvegarde, ou {@code null} pour un attachement qui ne survit pas a la session. */
	@Nullable
	public Codec<T> codec() {
		return this.codec;
	}

	/**
	 * Le codec de synchronisation vers le client.
	 *
	 * <p>Un type qui en a un est envoye aux clients par {@link Attachments} (voir
	 * {@link SyncAttachmentPacket}).</p>
	 */
	@Nullable
	public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
		return this.streamCodec;
	}

	/** Vrai si la donnee doit suivre le joueur a travers sa mort. */
	public boolean copyOnDeath() {
		return this.copyOnDeath;
	}

	@Override
	public String toString() {
		return "AttachmentType{" + this.id + "}";
	}

	public static final class Builder<T> {
		private final String id;
		private final Supplier<T> defaultValue;
		@Nullable
		private Codec<T> codec;
		@Nullable
		private StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
		private boolean copyOnDeath;

		private Builder(String id, Supplier<T> defaultValue) {
			this.id = id;
			this.defaultValue = defaultValue;
		}

		public Builder<T> serialize(Codec<T> codec) {
			this.codec = codec;
			return this;
		}

		public Builder<T> sync(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
			this.streamCodec = streamCodec;
			return this;
		}

		public Builder<T> copyOnDeath() {
			this.copyOnDeath = true;
			return this;
		}

		public AttachmentType<T> build() {
			return new AttachmentType<>(this);
		}
	}
}
