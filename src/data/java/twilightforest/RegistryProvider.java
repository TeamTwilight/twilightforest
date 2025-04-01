package twilightforest;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.beanification.PostConstruct;
import twilightforest.bootstrap.IBootstrap;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
public class RegistryProvider {

	@Directory(IBootstrap.class)
	private List<IBootstrap> bootstraps;

	private RegistrySetBuilder builder = new RegistrySetBuilder();

	@Nullable
	private DatapackBuiltinEntriesProvider value;

	@PostConstruct
	private void setup() {
		bootstraps.forEach(bootstrap -> builder = bootstrap.bootstrap(builder));
	}

	public CompletableFuture<HolderLookup.Provider> retrieve(GatherDataEvent event) {
		if (value == null) {
			value = new DatapackBuiltinEntriesProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), builder, Set.of("minecraft", TwilightForestMod.ID));
			event.getGenerator().addProvider(true, value);
		}
		return value.getRegistryProvider();
	}

	public HolderLookup.Provider join() {
		return Objects.requireNonNull(value).getRegistryProvider().join();
	}

	public <T> Optional<ResourceKey<T>> findKey(ResourceKey<Registry<T>> registry, T t) {
		return findKeyFrom(join(), registry, t);
	}

	public <T> Optional<ResourceKey<T>> findKeyFrom(HolderLookup.Provider provider, ResourceKey<Registry<T>> registry, T t) {
		return provider.lookupOrThrow(registry).listElements()
			.filter(b -> b.value() == t)
			.findAny().orElseThrow()
			.unwrapKey();
	}

}
