package twilightforest.forge.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Le {@code DeferredRegister} de NeoForge, rendu au-dessus de celui de Forge.
 *
 * <p>Les deux portent le meme nom et rendent le meme service ; ils ne different que par le type
 * rendu a l'enregistrement — {@link DeferredHolder} d'un cote, {@code RegistryObject} de l'autre.
 * Cette classe fait le pont, et les variantes {@link Blocks} et {@link Items} rendent les types
 * specialises que NeoForge fournit.</p>
 */
public class DeferredRegister<T> {
	protected final net.minecraftforge.registries.DeferredRegister<T> delegate;
	protected final String modId;

	protected DeferredRegister(net.minecraftforge.registries.DeferredRegister<T> delegate, String modId) {
		this.delegate = delegate;
		this.modId = modId;
	}

	/**
	 * Les registres que le mod tient lui-meme, faute que Forge sache en creer de forme vanilla.
	 *
	 * <p>{@link twilightforest.TFRegistries} les declare ici a son initialisation ; toute demande de
	 * registre differe portant l'une de ces cles est alors servie par {@link Standalone} au lieu de
	 * passer par Forge, qui ne les connait pas.</p>
	 */
	private static final Map<ResourceLocation, Registry<?>> STANDALONE = new HashMap<>();

	public static <T> void declareStandalone(Registry<T> registry) {
		STANDALONE.put(registry.key().location(), registry);
	}

	@SuppressWarnings("unchecked")
	public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> key, String modId) {
		// Les registres autonomes sont declares par l'initialiseur statique de TFRegistries. Un appel
		// avec TFRegistries.Keys.X ne le declenche pas (classe imbriquee) : selon l'ordre de
		// chargement, la cle n'etait pas encore connue et le registre partait chez Forge, qui
		// l'ignore (« Unable to find registry with key »).
		twilightforest.TFRegistries.bootstrap();
		Registry<?> standalone = STANDALONE.get(key.location());
		if (standalone != null) {
			return new Standalone<>((Registry<T>) standalone, modId);
		}
		return new DeferredRegister<>(net.minecraftforge.registries.DeferredRegister.create(key, modId), modId);
	}

	public static <T> DeferredRegister<T> create(Registry<T> registry, String modId) {
		return create(registry.key(), modId);
	}

	public static <T> DeferredRegister<T> create(ResourceLocation registryName, String modId) {
		return new DeferredRegister<>(
				net.minecraftforge.registries.DeferredRegister.create(registryName, modId), modId);
	}

	public static Blocks createBlocks(String modId) {
		return new Blocks(modId);
	}

	public static Items createItems(String modId) {
		return new Items(modId);
	}

	public static DeferredRegister<DataComponentType<?>> createDataComponents(String modId) {
		return create(Registries.DATA_COMPONENT_TYPE, modId);
	}

	public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> supplier) {
		return DeferredHolder.of(this.delegate.register(name, supplier));
	}

	/**
	 * La variante de NeoForge qui donne son identifiant a la fabrique, pratique pour les objets qui
	 * ont besoin de connaitre leur propre nom.
	 */
	public <I extends T> DeferredHolder<T, I> register(String name, Function<ResourceLocation, ? extends I> factory) {
		return this.register(name, () -> factory.apply(ResourceLocation.fromNamespaceAndPath(this.modId, name)));
	}

	public void register(IEventBus bus) {
		this.delegate.register(bus);
	}

	public ResourceKey<? extends Registry<T>> getRegistryKey() {
		return this.delegate.getRegistryKey();
	}

	public ResourceLocation getRegistryName() {
		return this.delegate.getRegistryName();
	}

	public TagKey<T> createTagKey(String name) {
		return this.delegate.createTagKey(name);
	}

	public Collection<DeferredHolder<T, ? extends T>> getEntries() {
		return this.delegate.getEntries().stream()
				.map(o -> (DeferredHolder<T, ? extends T>) DeferredHolder.<T, T>of(o))
				.collect(Collectors.toList());
	}

	/**
	 * Declare qu'un ancien identifiant designe la meme entree qu'un nouveau.
	 *
	 * <p>NeoForge le gere sur le registre differe ; Forge passe par son evenement de correspondances
	 * manquantes. Sans branchement, cette methode ne fait rien : les mondes crees avant un
	 * renommage perdront l'entree concernee au lieu d'etre migres.</p>
	 *
	 * <p><b>A brancher</b> sur {@code MissingMappingsEvent}.</p>
	 */
	public void addAlias(ResourceLocation from, ResourceLocation to) {
	}

	/**
	 * Le registre differe d'un registre que le mod tient lui-meme.
	 *
	 * <p>Forge n'a pas d'equivalent a {@code NewRegistryEvent.register(Registry)} de NeoForge : son
	 * evenement ne sait produire qu'un {@code IForgeRegistry}, et le pont vers un registre de forme
	 * vanilla — {@code RegistryBuilder.hasWrapper()}, {@code ForgeRegistry.getWrapperOrThrow()} —
	 * est de portee paquet. Les six registres de repartition de codecs du mod sont donc de simples
	 * {@code MappedRegistry} que le mod cree et remplit.</p>
	 *
	 * <p>Les entrees sont retenues puis ecrites d'un coup au moment ou le mod remet son registre au
	 * bus, comme le fait Forge a son evenement d'enregistrement.</p>
	 */
	public static class Standalone<T> extends DeferredRegister<T> {
		private final Registry<T> registry;
		private final List<Runnable> pending = new ArrayList<>();
		private final List<DeferredHolder<T, ? extends T>> entries = new ArrayList<>();

		private Standalone(Registry<T> registry, String modId) {
			super(null, modId);
			this.registry = registry;
		}

		@Override
		public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> supplier) {
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(this.modId, name);
			this.pending.add(() -> Registry.register(this.registry, id, supplier.get()));
			DeferredHolder<T, I> holder = DeferredHolder.of(this.registry, id);
			this.entries.add(holder);
			return holder;
		}

		@Override
		public void register(IEventBus bus) {
			this.pending.forEach(Runnable::run);
			this.pending.clear();
		}

		@Override
		public ResourceKey<? extends Registry<T>> getRegistryKey() {
			return this.registry.key();
		}

		@Override
		public ResourceLocation getRegistryName() {
			return this.registry.key().location();
		}

		@Override
		public TagKey<T> createTagKey(String name) {
			return TagKey.create(this.registry.key(), ResourceLocation.fromNamespaceAndPath(this.modId, name));
		}

		@Override
		public Collection<DeferredHolder<T, ? extends T>> getEntries() {
			return List.copyOf(this.entries);
		}
	}

	/** Les blocs, dont l'enregistrement rend un {@link DeferredBlock}. */
	public static class Blocks extends DeferredRegister<Block> {
		private Blocks(String modId) {
			super(net.minecraftforge.registries.DeferredRegister.create(Registries.BLOCK, modId), modId);
		}

		@Override
		public <I extends Block> DeferredBlock<I> register(String name, Supplier<? extends I> supplier) {
			return new DeferredBlock<>(this.delegate.register(name, supplier));
		}

		@Override
		public <I extends Block> DeferredBlock<I> register(String name, Function<ResourceLocation, ? extends I> factory) {
			return this.register(name, () -> factory.apply(ResourceLocation.fromNamespaceAndPath(this.modId, name)));
		}
	}

	/** Les objets, dont l'enregistrement rend un {@link DeferredItem}. */
	public static class Items extends DeferredRegister<Item> {
		private Items(String modId) {
			super(net.minecraftforge.registries.DeferredRegister.create(Registries.ITEM, modId), modId);
		}

		@Override
		public <I extends Item> DeferredItem<I> register(String name, Supplier<? extends I> supplier) {
			return new DeferredItem<>(this.delegate.register(name, supplier));
		}

		@Override
		public <I extends Item> DeferredItem<I> register(String name, Function<ResourceLocation, ? extends I> factory) {
			return this.register(name, () -> factory.apply(ResourceLocation.fromNamespaceAndPath(this.modId, name)));
		}
	}
}