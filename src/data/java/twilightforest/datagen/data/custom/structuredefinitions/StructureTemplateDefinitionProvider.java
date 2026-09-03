package twilightforest.datagen.data.custom.structuredefinitions;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.util.StructureTemplateDefinition;
import twilightforest.world.components.structures.util.StructureTemplateDefinitions;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;
import twilightforest.world.components.structures.util.TemplatePoolInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class StructureTemplateDefinitionProvider extends JsonCodecProvider<StructureTemplateDefinition> {
	private final Map<Identifier, Map<Identifier, TemplatePoolInstance>> poolsForTemplateWeights = new HashMap<>();

	private final String name;

	public StructureTemplateDefinitionProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, String name) {
		super(output, PackOutput.Target.DATA_PACK, StructureTemplateDefinitions.DIRECTORY, StructureTemplateDefinition.CODEC, lookupProvider, modId);
		this.name = name;
	}

	protected abstract void generatePools(HolderLookup.Provider provider);

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		return this.lookupProvider.thenCompose(provider -> {
			this.generatePools(provider);

			for(Map.Entry<Identifier, Map<Identifier, TemplatePoolInstance>> poolWeightsForTemplate : this.poolsForTemplateWeights.entrySet()) {
				Identifier templateId = poolWeightsForTemplate.getKey();

				this.unconditional(templateId, new StructureTemplateDefinition(poolWeightsForTemplate.getValue()));
			}

			return super.run(cache);
		});
	}

	@Override
	protected void gather() {
	}

	protected void addToAllPools(String roomId, int weight, Identifier... poolIds) {
		for(Identifier poolId : poolIds) {
			this.add(roomId, poolId, weight);
		}
	}

	protected void addToAllPools(String roomId, TemplatePoolInstance weight, Identifier... poolIds) {
		for(Identifier poolId : poolIds) {
			this.add(roomId, poolId, weight);
		}
	}

	protected void addAllTemplatesToPool(Identifier poolId, int weight, String... roomIds) {
		for(String roomId : roomIds) {
			this.add(roomId, poolId, weight);
		}
	}

	protected void add(String roomId, Identifier poolId, int weight) {
		this.add(TwilightForestMod.prefix(roomId), poolId, weight);
	}

	protected void addEmpty(Identifier poolId, int weight) {
		this.add(TwilightForestMod.prefix("_empty"), poolId, weight);
	}

	protected void add(Identifier templateId, Identifier poolId, int weight) {
		this.add(templateId, poolId, TemplatePoolInstance.defaultsWithWeight(weight));
	}

	protected void add(String roomId, Identifier poolId, TemplatePoolInstance poolData) {
		this.add(TwilightForestMod.prefix(roomId), poolId, poolData);
	}

	protected void add(Identifier templateId, Identifier poolId, TemplatePoolInstance poolData) {
		Map<Identifier, TemplatePoolInstance> poolWeightsForTemplate = this.poolsForTemplateWeights.computeIfAbsent(templateId, k -> new HashMap<>());

		poolWeightsForTemplate.put(poolId, poolData);
	}

	protected TemplatePoolInstance weightedPathTemplate(int weight, @Nullable Holder<TemplateMarkerHandlerList> markerHandlers, @Nullable Map<String, String> poolAliases) {
		// TODO register and reference instead
		Holder<StructureProcessorList> projectionProcessorHolder = Holder.direct(new StructureProcessorList(List.of(new GravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -2))));
		return new TemplatePoolInstance(
			weight,
			Optional.of(projectionProcessorHolder),
			StructureTemplatePool.Projection.RIGID,
			TerrainAdjustment.NONE,
			Optional.of(new TemplatePoolInstance.HeightAdjustment(Heightmap.Types.WORLD_SURFACE_WG, 2, Optional.of(0))),
			false,
			Optional.ofNullable(markerHandlers),
			Optional.empty(),
			poolAliases == null ? Map.of() : poolAliases
		);
	}

	protected TemplatePoolInstance weightedRigidTemplate(int weight, int beardifierGroundDelta, @Nullable Integer groundJunctionDiffLimit, @Nullable Holder<TemplateMarkerHandlerList> markerHandlers, @Nullable TemplatePoolInstance.ChooseRandomProcessors randomizedProcessors, @Nullable Map<String, String> poolAliases) {
		return new TemplatePoolInstance(
				weight,
			Optional.empty(),
			StructureTemplatePool.Projection.RIGID,
			TerrainAdjustment.BEARD_BOX,
			Optional.of(new TemplatePoolInstance.HeightAdjustment(Heightmap.Types.WORLD_SURFACE_WG, beardifierGroundDelta, Optional.ofNullable(groundJunctionDiffLimit))),
			true,
			Optional.ofNullable(markerHandlers),
			Optional.ofNullable(randomizedProcessors),
			poolAliases == null ? Map.of() : poolAliases
		);
	}

	protected Holder.Reference<TemplateMarkerHandlerList> getMarkers(HolderLookup.Provider provider, ResourceKey<TemplateMarkerHandlerList> key) {
		return Holder.Reference.createStandAlone(provider.lookupOrThrow(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST), key);
	}

	@Override
	public String getName() {
		return String.format("%s generator for %s in %s", this.directory, this.name, this.modid);
	}
}
