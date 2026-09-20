package twilightforest.world.components.speleothem;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.util.CodecResourceReloadListener;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class StalactiteReloadListener extends CodecResourceReloadListener<SpeleothemVarietyConfig> {
	public final static StalactiteReloadListener INSTANCE = new StalactiteReloadListener(); // TODO Autowired

	public static final String STALACTITE_DIRECTORY = "twilight/stalactites";

	public static volatile Map<String, SpeleothemVarietyConfig> HILL_CONFIGS = Map.of();
	public static volatile Map<String, List<Stalactite>> STALACTITES_PER_HILL = Map.of();
	public static volatile Map<String, List<Stalactite>> ORE_STALACTITES_PER_HILL = Map.of();
	public static volatile Map<String, List<Stalactite>> STALAGMITES_PER_HILL = Map.of();

	private Map<String, SpeleothemVarietyConfig> loadingHillConfigs = Map.of();
	private Map<String, List<Stalactite>> loadingStalactites = Map.of();
	private Map<String, List<Stalactite>> loadingOreStalactites = Map.of();
	private Map<String, List<Stalactite>> loadingStalagmites = Map.of();

	public StalactiteReloadListener() {
		super(STALACTITE_DIRECTORY, SpeleothemVarietyConfig.CODEC);
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
		loadingHillConfigs = new HashMap<>();
		loadingStalactites = new HashMap<>();
		loadingOreStalactites = new HashMap<>();
		loadingStalagmites = new HashMap<>();

		super.apply(map, manager, profiler);

		HILL_CONFIGS = loadingHillConfigs;
		STALACTITES_PER_HILL = loadingStalactites;
		ORE_STALACTITES_PER_HILL = loadingOreStalactites;
		STALAGMITES_PER_HILL = loadingStalagmites;
	}

	@Override
	protected void forLocation(ResourceManager manager, Identifier location, SpeleothemVarietyConfig config) {
		if (!loadingHillConfigs.containsKey(config.type()) || config.replace()) {
			loadingHillConfigs.put(config.type(), config);
			if (config.replace()) {
				TwilightForestMod.LOGGER.info("Stalactite Config {} wiped by {}", config.type(), location.getNamespace());
			}
		}

		this.populateList(manager, config, config.baseStalactites(), loadingStalactites);
		this.populateList(manager, config, config.oreStalactites(), loadingOreStalactites);
		this.populateList(manager, config, config.stalagmites(), loadingStalagmites);
	}

	private void populateList(ResourceManager manager, SpeleothemVarietyConfig config, List<Identifier> rawEntries, Map<String, List<Stalactite>> stalactiteDict) {
		List<Stalactite> stalactitesForType = stalactiteDict.computeIfAbsent(config.type(), k -> new ArrayList<>());

		if (config.replace()) stalactitesForType.clear();

		for (Identifier rl : rawEntries) {
			rl = Identifier.fromNamespaceAndPath(rl.getNamespace(), String.format("%s/%s.json", STALACTITE_DIRECTORY, rl.getPath()));
			Optional<Resource> stalRes = manager.getResource(rl);
			if (stalRes.isPresent()) {
				try {
					Reader stalReader = stalRes.get().openAsReader();
					JsonObject stalObject = GsonHelper.fromJson(this.gson, stalReader, JsonObject.class);
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

	@Override
	public String getListenerName() {
		return "stalactites";
	}
}
