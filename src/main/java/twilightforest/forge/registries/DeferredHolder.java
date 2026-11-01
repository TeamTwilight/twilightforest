package twilightforest.forge.registries;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Le {@code DeferredHolder} de NeoForge, rendu au-dessus du {@code RegistryObject} de Forge.
 *
 * <p>Le mod s'appuie sur ce type a plus de mille endroits, et surtout sur le fait qu'il est un
 * {@link Holder} : c'est ce qui permet de passer une entree de registre directement aux API vanilla
 * qui en attendent un. Le {@code RegistryObject} de Forge, lui, n'est qu'un
 * {@link java.util.function.Supplier}.
 * Ecrire cet adaptateur coute quelques centaines de lignes ; convertir les 341 fichiers concernes
 * en aurait coute des milliers, et aurait eloigne le portage de l'original a chaque mise a jour
 * amont.</p>
 *
 * @param <R> le type du registre
 * @param <T> le type concret de l'entree, forcement un {@code R}
 */
public class DeferredHolder<R, T extends R> implements Holder<R> {
	/**
	 * Ce dont le detenteur a besoin, quelle que soit la nature du registre derriere.
	 *
	 * <p>La plupart des entrees vivent dans un registre que Forge connait, et sont donc servies par
	 * un {@code RegistryObject}. Six registres du mod n'y sont pas : Forge ne sait pas creer de
	 * registre de forme vanilla, ils sont donc tenus par le mod lui-meme. Cette interface permet aux
	 * deux de se presenter pareil.</p>
	 */
	protected interface Source<T> {
		T get();

		ResourceLocation getId();

		ResourceKey<?> getKey();

		Optional<? extends Holder<T>> getHolder();

		boolean isPresent();
	}

	protected final Source<T> source;

	protected DeferredHolder(Source<T> source) {
		this.source = source;
	}

	public static <R, T extends R> DeferredHolder<R, T> of(RegistryObject<T> object) {
		return new DeferredHolder<>(sourceOf(object));
	}

	/** La source adossee a un registre que Forge connait, partagee avec les variantes typees. */
	protected static <T> Source<T> sourceOf(RegistryObject<T> object) {
		return new Source<T>() {
			@Override
			public T get() {
				return object.get();
			}

			@Override
			public ResourceLocation getId() {
				return object.getId();
			}

			@Override
			public ResourceKey<?> getKey() {
				return object.getKey();
			}

			@Override
			public Optional<? extends Holder<T>> getHolder() {
				return object.getHolder();
			}

			@Override
			public boolean isPresent() {
				return object.isPresent();
			}
		};
	}

	/**
	 * Le detenteur d'une entree rangee dans un registre que le mod tient lui-meme.
	 *
	 * <p>La recherche est faite a chaque appel plutot que retenue : l'entree n'existe pas encore au
	 * moment ou le detenteur est cree, exactement comme pour un registre de Forge.</p>
	 */
	public static <R, T extends R> DeferredHolder<R, T> of(Registry<R> registry, ResourceLocation id) {
		ResourceKey<R> key = ResourceKey.create(registry.key(), id);
		return new DeferredHolder<>(new Source<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T get() {
				R value = registry.get(id);
				if (value == null) {
					throw new IllegalStateException("Aucune entree de registre pour " + id);
				}
				return (T) value;
			}

			@Override
			public ResourceLocation getId() {
				return id;
			}

			@Override
			public ResourceKey<?> getKey() {
				return key;
			}

			@SuppressWarnings("unchecked")
			@Override
			public Optional<? extends Holder<T>> getHolder() {
				return (Optional<? extends Holder<T>>) (Optional<?>) registry.getHolder(key);
			}

			@Override
			public boolean isPresent() {
				return registry.get(id) != null;
			}
		});
	}

	/**
	 * L'entree, dans son type concret.
	 *
	 * <p>{@code Holder} etend {@code Supplier}, dont le {@code get()} rend un {@code R} ; on le
	 * redefinit en {@code T}, ce que Java autorise puisque {@code T} est un {@code R}.</p>
	 */
	@Override
	public T get() {
		return this.source.get();
	}

	public ResourceLocation getId() {
		return this.source.getId();
	}

	/**
	 * La cle du registre.
	 *
	 * <p>Forge la type sur l'entree concrete, NeoForge sur le registre. L'un et l'autre designent la
	 * meme chose et le parametre de type disparait a l'execution : le transtypage est sans effet
	 * reel.</p>
	 */
	@SuppressWarnings("unchecked")
	public ResourceKey<R> getKey() {
		return (ResourceKey<R>) this.source.getKey();
	}

	/**
	 * Le {@link Holder} du registre, resolu a la demande.
	 *
	 * <p>Meme remarque sur le transtypage : l'objet enregistre est un {@code T}, donc un {@code R}.
	 * Seule l'etiquette de type change.</p>
	 */
	@SuppressWarnings("unchecked")
	protected Holder<R> holder() {
		return (Holder<R>) this.source.getHolder().orElseThrow(() -> new IllegalStateException(
				"Aucune entree de registre pour " + this.source.getId()));
	}

	@Override
	public T value() {
		return this.get();
	}

	@Override
	public boolean isBound() {
		return this.source.isPresent();
	}

	// Les comparaisons par identifiant n'ont pas besoin que l'entree soit resolue : les repondre
	// ici evite de forcer la resolution pendant le chargement, quand le registre est encore ouvert.
	@Override
	public boolean is(ResourceLocation id) {
		return id.equals(this.source.getId());
	}

	@Override
	public boolean is(ResourceKey<R> key) {
		return key.equals(this.getKey());
	}

	@Override
	public boolean is(Predicate<ResourceKey<R>> predicate) {
		return predicate.test(this.getKey());
	}

	@Override
	public boolean is(Holder<R> other) {
		return other.unwrapKey().map(this::is).orElse(false);
	}

	@Override
	public Optional<ResourceKey<R>> unwrapKey() {
		return Optional.of(this.getKey());
	}

	@Override
	public Kind kind() {
		return Kind.REFERENCE;
	}

	// Le reste demande le vrai Holder : les etiquettes et la serialisation dependent du registre.
	@Override
	public boolean is(TagKey<R> tag) {
		return this.holder().is(tag);
	}

	@Override
	public Stream<TagKey<R>> tags() {
		return this.holder().tags();
	}

	@Override
	public Either<ResourceKey<R>, R> unwrap() {
		return this.holder().unwrap();
	}

	@Override
	public boolean canSerializeIn(HolderOwner<R> owner) {
		return this.holder().canSerializeIn(owner);
	}

	@Override
	public String toString() {
		return "DeferredHolder{" + this.source.getId() + "}";
	}
}
