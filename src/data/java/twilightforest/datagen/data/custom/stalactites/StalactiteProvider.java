package twilightforest.datagen.data.custom.stalactites;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.spelothem.SpeleothemVarietyConfig;
import twilightforest.world.components.spelothem.Stalactite;
import twilightforest.world.components.spelothem.StalactiteReloadListener;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class StalactiteProvider implements DataProvider {

	private final PackOutput generator;
	private final String modid;
	private final PackOutput.PathProvider entryPath;
	protected final List<HillInformation> builder = new ArrayList<>();

	public StalactiteProvider(PackOutput generator, String modid) {
		this.generator = generator;
		this.modid = modid;
		this.entryPath = generator.createPathProvider(PackOutput.Target.DATA_PACK, StalactiteReloadListener.STALACTITE_DIRECTORY);
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		List<SpeleothemVarietyConfig> configs = new ArrayList<>();
		Map<Identifier, Stalactite> map = Maps.newHashMap();

		ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

		this.builder.clear();
		this.createStalactites();
		this.builder.forEach(info -> {
			configs.add(info.config());
			this.checkForIncorrectEntries(map, info.baseStalactites());
			this.checkForIncorrectEntries(map, info.oreStalactites());
			this.checkForIncorrectEntries(map, info.stalagmites());
		});

		map.forEach((identifier, stalactite) -> {
			Path path = this.entryPath.json(identifier);
			futuresBuilder.add(DataProvider.saveStable(output, Stalactite.CODEC.encodeStart(JsonOps.INSTANCE, stalactite).resultOrPartial(TwilightForestMod.LOGGER::error).orElseThrow(), path));
		});
		configs.forEach(hillConfig -> {
			Path hillPath = this.generator.getOutputFolder().resolve(String.format("data/%s/%s/%s.json", this.modid, StalactiteReloadListener.STALACTITE_DIRECTORY, hillConfig.type()));
			futuresBuilder.add(DataProvider.saveStable(output, SpeleothemVarietyConfig.CODEC.encodeStart(JsonOps.INSTANCE, hillConfig).resultOrPartial(TwilightForestMod.LOGGER::error).orElseThrow(), hillPath));
		});
		return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
	}

	//checks for improper duplicate entries in the map. This will prevent you from registering multiple stalactites under the same name that have different properties.
	private void checkForIncorrectEntries(Map<Identifier, Stalactite> insertMap, Map<Identifier, Stalactite> entries) {
		for (Map.Entry<Identifier, Stalactite> entry : entries.entrySet()) {
			if (insertMap.containsKey(entry.getKey()) && !insertMap.get(entry.getKey()).toString().equals(entry.getValue().toString())) {
				throw new IllegalArgumentException("A stalactite with the name " + entry.getKey() + " already exists!");
			}
			insertMap.put(entry.getKey(), entry.getValue());
		}
	}

	protected abstract void createStalactites();

	public Identifier makeStalactiteName(String name) {
		return Identifier.fromNamespaceAndPath(this.modid, "entries/" + name);
	}

	public Stalactite buildStalactite(Block ore, float sizeVariation, int maxLength, int weight) {
		return new Stalactite(Either.right(ore), sizeVariation, maxLength, weight);
	}

	public Stalactite buildStalactite(List<Pair<Block, Integer>> ores, float sizeVariation, int maxLength, int weight) {
		return new Stalactite(Either.left(ores), sizeVariation, maxLength, weight);
	}

	protected void buildConfig(HillBuilder builder) {
		this.builder.add(builder.build());
	}

	@Override
	public String getName() {
		return this.modid + " Hollow Hill Stalactites";
	}

	public static class HillBuilder {

		private final SpeleothemVarietyConfig config;
		private final Map<Identifier, Stalactite> baseStalactites = new HashMap<>();
		private final Map<Identifier, Stalactite> oreStalactites = new HashMap<>();
		private final Map<Identifier, Stalactite> stalagmites = new HashMap<>();

		public HillBuilder(String type, float stalactiteChance, float stalagmiteChance, float oreChance) {
			this(type, stalactiteChance, stalagmiteChance, oreChance, false);
		}

		public HillBuilder(String type, float stalactitePlaceTries, float stalagmitePlaceTries, float oreChance, boolean replace) {
			this.config = new SpeleothemVarietyConfig(type, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), oreChance, stalactitePlaceTries, stalagmitePlaceTries, replace);
		}

		public HillBuilder addBaseStalactite(Identifier name, Stalactite stalactite) {
			this.config.baseStalactites().add(name);
			this.baseStalactites.put(name, stalactite);
			return this;
		}

		public HillBuilder addOreStalactite(Identifier name, Stalactite stalactite) {
			this.config.oreStalactites().add(name);
			this.oreStalactites.put(name, stalactite);
			return this;
		}

		public HillBuilder addStalagmite(Identifier name, Stalactite stalactite) {
			this.config.stalagmites().add(name);
			this.stalagmites.put(name, stalactite);
			return this;
		}

		public HillInformation build() {
			if (this.baseStalactites.isEmpty() && this.oreStalactites.isEmpty() && this.config.stalactiteChance() > 0) {
				throw new IllegalArgumentException("HillBuilder must define at least one stalactite type when placement chance is set above 0.");
			}
			if (this.stalagmites.isEmpty() && this.config.stalagmiteChance() > 0) {
				throw new IllegalArgumentException("HillBuilder must define at least one stalagmite type when placement chance is set above 0.");
			}
			return new HillInformation(this.config, this.baseStalactites, this.oreStalactites, this.stalagmites);
		}
	}

	private record HillInformation(SpeleothemVarietyConfig config, Map<Identifier, Stalactite> baseStalactites, Map<Identifier, Stalactite> oreStalactites, Map<Identifier, Stalactite> stalagmites) {
	}
}
