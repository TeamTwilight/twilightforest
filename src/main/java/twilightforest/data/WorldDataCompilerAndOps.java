package twilightforest.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.util.registry.WorldGenSettingsExport;
import net.minecraft.world.Dimension;
import net.minecraftforge.registries.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class WorldDataCompilerAndOps<Format> extends WorldGenSettingsExport<Format> implements IDataProvider {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    protected final DataGenerator generator;
    private final Function<Format, String> fileContentWriter;
    protected final DynamicRegistries dynamicRegistries;

    private DirectoryCache directoryCache;

    public WorldDataCompilerAndOps(DataGenerator generator, DynamicOps<Format> ops, Function<Format, String> fileContentWriter, DynamicRegistries dynamicRegistries) {
        super(ops, null);
        this.generator = generator;
        this.fileContentWriter = fileContentWriter;
        this.dynamicRegistries = dynamicRegistries;
    }

    protected <Resource> Resource getOrCreateInRegistry(Registry<Resource> registry, RegistryKey<Resource> registryKey, Supplier<Resource> resourceCreator) {
        Resource resourceSaved = getFromVanillaRegistryIllegally(registry, registryKey);

        if (resourceSaved == null) {
            //SimpleRegistry<Resource> simpleRegistry = new SimpleRegistry<>(registryKey, Lifecycle.experimental());

            resourceSaved = Registry.register(registry, registryKey.getLocation(), resourceCreator.get());
        }

        return resourceSaved;
    }

    @Override
    public void act(final DirectoryCache directoryCache) {
        this.directoryCache = directoryCache;

        getDimensions().forEach((rl, dimension) -> serialize(generator.getOutputFolder(), directoryCache, this, Registry.DIMENSION_KEY, rl, dimension, Dimension.CODEC));
    }

    protected abstract Map<ResourceLocation, Dimension> getDimensions();

    private final HashSet<Object> objectsSerializationCache = new HashSet<>();

    @SuppressWarnings("SameParameterValue")
    private <Resource> void serialize(Path root, DirectoryCache directoryCache, DynamicOps<Format> ops, RegistryKey<? extends Registry<Resource>> resourceType, ResourceLocation resourceLocation, Resource resource, Encoder<Resource> encoder) {
        if (objectsSerializationCache.contains(resource)) {
            LOGGER.debug("Avoiding duplicate serialization with " + resourceLocation);

            return;
        }

        objectsSerializationCache.add(resource);

        Optional<Format> output = ops.withEncoder(encoder).apply(resource).resultOrPartial(error -> LOGGER.error("Object not serialized within recursive serialization: " + error));

        if (output.isPresent()) {
            try {
                save(directoryCache, output.get(), makePath(root, resourceType, resourceLocation));
            } catch (IOException e) {
                LOGGER.error("Could not save resource `" + resourceLocation + "` (Resource Type `" + resourceType.getLocation() + "`)", e);
            }
        }
    }

    private static Path makePath(Path path, RegistryKey<?> key, ResourceLocation resc) {
        return path.resolve("data").resolve(resc.getNamespace()).resolve(key.getLocation().getPath()).resolve(resc.getPath() + ".json");
    }

    /** VanillaCopy: IDataProvider.save */
    @SuppressWarnings("UnstableApiUsage") // Mojang uses HASH_FUNCTION as well, hence the warning suppression
    private void save(DirectoryCache cache, Format dynamic, Path pathIn) throws IOException {
        String s = fileContentWriter.apply(dynamic);
        String s1 = HASH_FUNCTION.hashUnencodedChars(s).toString();
        if (!Objects.equals(cache.getPreviousHash(pathIn), s1) || !Files.exists(pathIn)) {
            Files.createDirectories(pathIn.getParent());

            try (BufferedWriter bufferedwriter = Files.newBufferedWriter(pathIn)) {
                bufferedwriter.write(s);
            }
        }

        cache.recordHash(pathIn, s1);
    }

    @SuppressWarnings({"unchecked", "rawtypes", "SameParameterValue"})
    @Nullable
    protected static <T> T getFromVanillaRegistryIllegally(Registry registry, RegistryKey<T> key) {
        return (T) registry.getValueForKey(key);
    }

    private static <Resource> Optional<ResourceLocation> getFromForgeRegistryIllegally(RegistryKey<? extends Registry<Resource>> registryKey, Resource resource) {
        if (resource instanceof IForgeRegistryEntry) {
            IForgeRegistryEntry<Resource> entry = (IForgeRegistryEntry) resource;
            ResourceLocation location = entry.getRegistryName();

            if (location != null) {
                return Optional.of(location);
            }

            IForgeRegistry forgeRegistry = RegistryManager.ACTIVE.getRegistry(registryKey.getLocation());

            return Optional.ofNullable(forgeRegistry.getKey(entry));
        }

        return Optional.empty();
    }

    @Override
    protected <Resource> DataResult<Format> encode(Resource resource, Format dynamic, RegistryKey<? extends Registry<Resource>> registryKey, Codec<Resource> codec) {
        Optional<ResourceLocation> instanceKey = Optional.empty();

        // Check "Local" Registry
        try {
            Registry<Resource> dynRegistry = dynamicRegistries.getRegistry(registryKey);

            instanceKey = dynRegistry != null ? dynRegistry.getOptionalKey(resource).map(RegistryKey::getLocation) : Optional.empty();
        } catch (Throwable t) {
            // Registry not supported, skip
        }

        // Check Vanilla Worldgen Registries
        if (!instanceKey.isPresent()) {
            Registry<Resource> registry = getFromVanillaRegistryIllegally(WorldGenRegistries.ROOT_REGISTRIES, registryKey);

            if (registry != null) {
                instanceKey = registry.getOptionalKey(resource).map(RegistryKey::getLocation);
            }
        }

        // Check Global Vanilla Registries
        if (!instanceKey.isPresent()) {
            Registry<Resource> registry = getFromVanillaRegistryIllegally(Registry.REGISTRY, registryKey);

            if (registry != null) {
                instanceKey = registry.getOptionalKey(resource).map(RegistryKey::getLocation);
            }
        }

        // Check Forge Registries
        if (!instanceKey.isPresent()) {
            instanceKey = getFromForgeRegistryIllegally(registryKey, resource);
        }

        // Four freaking registry locations to check... Let's see if we won a prize
        if (instanceKey.isPresent()) {
            if (TwilightForestMod.ID.equals(instanceKey.get().getNamespace())) // This avoids generating anything that belongs to Minecraft
                serialize(generator.getOutputFolder(), directoryCache, this, registryKey, instanceKey.get(), resource, codec);

            return ResourceLocation.CODEC.encode(instanceKey.get(), this.ops, dynamic);
        }

        // AND we turned out empty-handed. Inline the object begrudgingly instead.
        return codec.encode(resource, this, dynamic);
    }

    @Override
    public String getName() {
        return "Twilight World";
    }
}
