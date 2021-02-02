package twilightforest.data;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenSettingsExport;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class TwilightWorldProvider implements IDataProvider {
    public TwilightWorldProvider() {
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {

    }

    @Override
    public String getName() {
        return "Twilight World Provider";
    }

    private List<RegistryKey<?>> keys = ImmutableList.of(
            Registry.DIMENSION_KEY,

            // Exists in Dimension Codec
            Registry.DIMENSION_TYPE_KEY,
            Registry.CHUNK_GENERATOR_KEY,

            Registry.BIOME_KEY,

            // BiomeGenerationSettings - Level 1
            Registry.CONFIGURED_CARVER_KEY,
            Registry.CONFIGURED_FEATURE_KEY,
            Registry.CONFIGURED_STRUCTURE_FEATURE_KEY
    );

    private enum GenerationOrder { // I would've done this as a Map except there doesn't seem to be a way for me to link the generic types together
        BIOME(ForgeRegistries.BIOMES, Biome.BIOME_CODEC)
        ;

        <T extends IForgeRegistryEntry<T>> GenerationOrder(IForgeRegistry<T> registry, Codec<Supplier<T>> codec) {

        }
    }

    // Copy of Super, replaced dynamicRegistries
    private static class ModifiedWorldGenerationExport<Ops> extends WorldGenSettingsExport<Ops> {
        @SuppressWarnings("ConstantConditions")
        public ModifiedWorldGenerationExport(DynamicOps<Ops> ops) {
            super(ops, null);
        }

        @SuppressWarnings({"unchecked", "rawtypes", "SameParameterValue"})
        @Nullable
        private static <T> T getFromRegistryIllegally(Registry registry, RegistryKey<T> key) {
            return (T) registry.getValueForKey(key);
        }

        @Override
        protected <E> DataResult<Ops> encode(E registryObject, Ops dynOps, RegistryKey<? extends Registry<E>> registryKeyE, Codec<E> codec) {
            Registry<E> registry = getFromRegistryIllegally(Registry.REGISTRY, registryKeyE);

            if (registry != null) {
                Optional<RegistryKey<E>> instanceKey = registry.getOptionalKey(registryObject);

                if (instanceKey.isPresent())
                    return ResourceLocation.CODEC.encode(instanceKey.get().getLocation(), this.ops, dynOps);
            }

            return codec.encode(registryObject, this, dynOps);
        }
    }
}
