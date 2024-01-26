package twilightforest.data.custom.stalactites.entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import twilightforest.TwilightForestMod;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class StalactiteReloadListener extends SimpleJsonResourceReloadListener {

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	public static final String STALACTITE_DIRECTORY = "twilight/stalactites";

	public static final Map<String, SpeleothemVarietyConfig> HILL_CONFIGS = new HashMap<>();
	public static final Map<String, List<Stalactite>> STALACTITES_PER_HILL = new HashMap<>();
	public static final Map<String, List<Stalactite>> ORE_STALACTITES_PER_HILL = new HashMap<>();
	public static final Map<String, List<Stalactite>> STALAGMITES_PER_HILL = new HashMap<>();

	public StalactiteReloadListener() {
		super(GSON, STALACTITE_DIRECTORY);
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
		List<Map.Entry<ResourceLocation, JsonElement>> nonTwilight = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            ResourceLocation location = entry.getKey();

			if (location.getPath().contains("entries"))
				continue;

			if (TwilightForestMod.ID.equals(location.getNamespace())) {
				JsonElement jsonElement = entry.getValue();
				this.forLocation(manager, location, jsonElement);
			} else {
				nonTwilight.add(entry);
			}
        }

		for (Map.Entry<ResourceLocation, JsonElement> entry : nonTwilight) {
			ResourceLocation location = entry.getKey();
			JsonElement jsonElement = entry.getValue();
			this.forLocation(manager, location, jsonElement);
		}
	}

	private void forLocation(ResourceManager manager, ResourceLocation location, JsonElement jsonElement) {
        try {
            Optional<SpeleothemVarietyConfig> checkFile = SpeleothemVarietyConfig.CODEC.parse(JsonOps.INSTANCE, jsonElement).result();
            if (checkFile.isPresent()) {
                SpeleothemVarietyConfig config = checkFile.get();
                if (!HILL_CONFIGS.containsKey(config.type()) || config.replace()) {
                    HILL_CONFIGS.put(config.type(), config);
                    if (config.replace()) {
                        TwilightForestMod.LOGGER.info("Stalactite Config {} wiped by {}", config.type(), location.getNamespace());
                    }
                }

                this.populateList(manager, config, config.baseStalactites(), STALACTITES_PER_HILL);
                this.populateList(manager, config, config.oreStalactites(), ORE_STALACTITES_PER_HILL);
                this.populateList(manager, config, config.stalagmites(), STALAGMITES_PER_HILL);
            }
        } catch (Exception e) {
            TwilightForestMod.LOGGER.error("Couldn't read Hollow Hill list {}", location, e);
        }
    }

	private void populateList(ResourceManager manager, SpeleothemVarietyConfig config, List<ResourceLocation> rawEntries, Map<String, List<Stalactite>> stalactiteDict) {
		List<Stalactite> stalactitesForType = stalactiteDict.computeIfAbsent(config.type(), k -> new ArrayList<>());

		if (config.replace()) stalactitesForType.clear();

		for (ResourceLocation rl : rawEntries) {
			rl = new ResourceLocation(rl.getNamespace(), String.format("%s/%s.json", STALACTITE_DIRECTORY, rl.getPath()));
			Optional<Resource> stalRes = manager.getResource(rl);
			if (stalRes.isPresent()) {
				try {
					Reader stalReader = stalRes.get().openAsReader();
					JsonObject stalObject = GsonHelper.fromJson(GSON, stalReader, JsonObject.class);
					Stalactite stalactite = Stalactite.CODEC.parse(JsonOps.INSTANCE, stalObject).resultOrPartial(TwilightForestMod.LOGGER::error).orElseThrow();
					stalactitesForType.add(stalactite);
					TwilightForestMod.LOGGER.debug("Loaded Stalactite {} for config {}", rl, config.type());
				} catch (RuntimeException | IOException e) {
					TwilightForestMod.LOGGER.error("Failed to parse stalactite entry {} in file {}", rl, config, e);
				}
			} else {
				TwilightForestMod.LOGGER.error("Could not find stalactite entry for {}", rl);
			}
		}
	}
}
