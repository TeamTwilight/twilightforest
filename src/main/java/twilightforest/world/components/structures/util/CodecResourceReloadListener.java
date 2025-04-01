package twilightforest.world.components.structures.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//TODO this class may be redundant now that SimpleJsonResourceReloadListener uses codecs. Look into it
@Deprecated
public abstract class CodecResourceReloadListener<T> extends SimpleJsonResourceReloadListener<JsonElement> {
	protected final Gson gson;
	private final Codec<T> codec;

	public CodecResourceReloadListener(Codec<T> codec, String directory) {
		this(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(), codec, directory);
	}

	public CodecResourceReloadListener(Gson gson, Codec<T> codec, String directory) {
		super(ExtraCodecs.JSON, FileToIdConverter.json(directory));

		this.gson = gson;
		this.codec = codec;
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
		List<Map.Entry<ResourceLocation, JsonElement>> nonTwilight = new ArrayList<>();

		for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
			ResourceLocation location = entry.getKey();

			// FIXME separate Stalactite entry from structure-specific stalactite jsons
			//  The StalactiteReloadListener requires this. When that is fixed, then this class can be deleted.
			if (location.getPath().contains("entries"))
				continue;

			if (TwilightForestMod.ID.equals(location.getNamespace())) {
				JsonElement jsonElement = entry.getValue();
				this.deserialize(manager, location, jsonElement);
			} else {
				nonTwilight.add(entry);
			}
		}

		for (Map.Entry<ResourceLocation, JsonElement> entry : nonTwilight) {
			ResourceLocation location = entry.getKey();
			JsonElement jsonElement = entry.getValue();
			this.deserialize(manager, location, jsonElement);
		}
	}

	protected void deserialize(ResourceManager manager, ResourceLocation location, JsonElement jsonElement) {
		try {
			Optional<T> checkFile = this.codec.parse(JsonOps.INSTANCE, jsonElement).result();
			if (checkFile.isPresent()) {
				this.forLocation(manager, location, checkFile.get());
			} else {
				TwilightForestMod.LOGGER.error("Listener '{}' failed to load resource {}", this.getName(), location);
			}
		} catch (Exception e) {
			TwilightForestMod.LOGGER.error("Listener '{}' couldn't read element {}", this.getName(), location, e);
		}
	}

	protected abstract void forLocation(ResourceManager manager, ResourceLocation location, T element);
}
