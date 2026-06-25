package twilightforest.datagen.data.custom.stalactites;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import twilightforest.world.components.speleothem.SpeleothemVarietyConfig;
import twilightforest.world.components.speleothem.Stalactite;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class StalactiteProvider implements DataProvider {

	private final PackOutput generator;
	private final String modid;
	protected final List<HillInformation> builder = new ArrayList<>();

	public StalactiteProvider(PackOutput generator, String modid) {
		this.generator = generator;
		this.modid = modid;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		this.builder.clear();
		this.createStalactites();

		List<CompletableFuture<?>> futures = new ArrayList<>();

		for (HillInformation hillInfo : this.builder) {
			Path configPath = this.generator.getOutputFolder(PackOutput.Target.DATA_PACK)
				.resolve(this.modid)
				.resolve("twilight/stalactites")
				.resolve(hillInfo.config.type() + ".json");

			futures.add(DataProvider.saveStable(output, SpeleothemVarietyConfig.CODEC, hillInfo.config, configPath));

			futures.addAll(this.saveStalactites(output, hillInfo.baseStalactites()));
			futures.addAll(this.saveStalactites(output, hillInfo.oreStalactites()));
			futures.addAll(this.saveStalactites(output, hillInfo.stalagmites()));
		}

		return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]));
	}

	private List<CompletableFuture<?>> saveStalactites(CachedOutput output, Map<Identifier, Stalactite> stalactites) {
		List<CompletableFuture<?>> futures = new ArrayList<>();

		for (Map.Entry<Identifier, Stalactite> entry : stalactites.entrySet()) {
			Path stalactitePath = this.generator.getOutputFolder(PackOutput.Target.DATA_PACK)
				.resolve(this.modid)
				.resolve("twilight/stalactites")
				.resolve(entry.getKey().getPath() + ".json");

			futures.add(DataProvider.saveStable(output, Stalactite.CODEC, entry.getValue(), stalactitePath));
		}

		return futures;
	}

	@Override
	public String getName() {
		return this.modid + " Hollow Hill Stalactites";
	}

	protected abstract void createStalactites();

	public Identifier makeStalactiteName(String name) {
		return Identifier.fromNamespaceAndPath(this.modid, "entries/" + name);
	}

	public Stalactite buildStalactite(Block ore, float sizeVariation, int maxLength, int weight) {
		return new Stalactite(Either.right(ore), sizeVariation, maxLength, weight);
	}

	protected void buildConfig(HillBuilder builder) {
		this.builder.add(builder.build());
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
			return new HillInformation(this.config, this.baseStalactites, this.oreStalactites, this.stalagmites);
		}
	}

	protected record HillInformation(SpeleothemVarietyConfig config, Map<Identifier, Stalactite> baseStalactites, Map<Identifier, Stalactite> oreStalactites, Map<Identifier, Stalactite> stalagmites) {}
}
