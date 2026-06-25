package twilightforest.world.components.structures.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class CodecResourceReloadListener<T> extends SimpleJsonResourceReloadListener<JsonElement> {
	protected final Gson gson;
	private final Codec<T> codec;

	public CodecResourceReloadListener(String directory, Codec<T> codec) {
		this(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(), directory, codec);
	}

	public CodecResourceReloadListener(Gson gson, String directory, Codec<T> codec) {
		super(ExtraCodecs.JSON, FileToIdConverter.json(directory));

		this.gson = gson;
		this.codec = codec;
	}

	protected DynamicOps<JsonElement> initDynamicOps() {
		try {
			if (ServerLifecycleHooks.getCurrentServer() != null) {
				return RegistryOps.create(JsonOps.INSTANCE, ServerLifecycleHooks.getCurrentServer().registryAccess());
			}
		} catch (Exception e) {
			TwilightForestMod.LOGGER.warn("Failed to get registry access for reload listener {}, falling back to JsonOps", this.getName(), e);
		}
		return JsonOps.INSTANCE;
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
		List<Map.Entry<Identifier, JsonElement>> nonTwilight = new ArrayList<>();

		for (Map.Entry<Identifier, JsonElement> entry : map.entrySet()) {
			Identifier location = entry.getKey();

			if (location.getPath().contains("entries"))
				continue;

			if (TwilightForestMod.ID.equals(location.getNamespace())) {
				JsonElement jsonElement = entry.getValue();
				this.deserialize(manager, location, jsonElement);
			} else {
				nonTwilight.add(entry);
			}
		}

		for (Map.Entry<Identifier, JsonElement> entry : nonTwilight) {
			Identifier location = entry.getKey();
			JsonElement jsonElement = entry.getValue();
			this.deserialize(manager, location, jsonElement);
		}
	}

	protected void deserialize(ResourceManager manager, Identifier location, JsonElement jsonElement) {
		try {
			Optional<T> checkFile = this.codec.parse(this.initDynamicOps(), jsonElement).result();
			if (checkFile.isPresent()) {
				this.forLocation(manager, location, checkFile.get());
			} else {
				TwilightForestMod.LOGGER.error("Listener '{}' failed to load resource {}", this.getName(), location);
			}
		} catch (Exception e) {
			TwilightForestMod.LOGGER.error("Listener '{}' couldn't read element {}", this.getName(), location, e);
		}
	}

	protected abstract void forLocation(ResourceManager manager, Identifier location, T element);
}
