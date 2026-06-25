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

	public static final Map<String, SpeleothemVarietyConfig> HILL_CONFIGS = new HashMap<>();
	public static final Map<String, List<Stalactite>> STALACTITES_PER_HILL = new HashMap<>();
	public static final Map<String, List<Stalactite>> ORE_STALACTITES_PER_HILL = new HashMap<>();
	public static final Map<String, List<Stalactite>> STALAGMITES_PER_HILL = new HashMap<>();

	public StalactiteReloadListener() {
		super(STALACTITE_DIRECTORY, SpeleothemVarietyConfig.CODEC);
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
		HILL_CONFIGS.clear();
		STALACTITES_PER_HILL.clear();
		ORE_STALACTITES_PER_HILL.clear();
		STALAGMITES_PER_HILL.clear();

		super.apply(map, manager, profiler);
	}

	@Override
	protected void forLocation(ResourceManager manager, Identifier location, SpeleothemVarietyConfig config) {
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
}
