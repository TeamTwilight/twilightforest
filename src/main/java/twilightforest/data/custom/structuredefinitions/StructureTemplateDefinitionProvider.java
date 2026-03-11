package twilightforest.data.custom.structuredefinitions;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.random.Weight;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
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
	private final Map<ResourceLocation, Map<ResourceLocation, TemplatePoolInstance>> poolsForTemplateWeights = new HashMap<>();

	private final String name;

	public StructureTemplateDefinitionProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, ExistingFileHelper existingFileHelper, String name) {
		super(output, PackOutput.Target.DATA_PACK, StructureTemplateDefinitions.DIRECTORY, PackType.SERVER_DATA, StructureTemplateDefinition.CODEC, lookupProvider, modId, existingFileHelper);
		this.name = name;
	}

	protected abstract void generatePools();

	@Override
	protected void gather() {
		this.generatePools();

		for(Map.Entry<ResourceLocation, Map<ResourceLocation, TemplatePoolInstance>> poolWeightsForTemplate : this.poolsForTemplateWeights.entrySet()) {
			ResourceLocation templateId = poolWeightsForTemplate.getKey();

			this.unconditional(templateId, new StructureTemplateDefinition(poolWeightsForTemplate.getValue()));
		}
	}

	protected void addToAllPools(String roomId, int weight, ResourceLocation... poolIds) {
		for(ResourceLocation poolId : poolIds) {
			this.add(roomId, poolId, weight);
		}
	}

	protected void addToAllPools(String roomId, TemplatePoolInstance weight, ResourceLocation... poolIds) {
		for(ResourceLocation poolId : poolIds) {
			this.add(roomId, poolId, weight);
		}
	}

	protected void addAllTemplatesToPool(ResourceLocation poolId, int weight, String... roomIds) {
		for(String roomId : roomIds) {
			this.add(roomId, poolId, weight);
		}
	}

	protected void add(String roomId, ResourceLocation poolId, int weight) {
		this.add(TwilightForestMod.prefix(roomId), poolId, weight);
	}

	protected void addEmpty(ResourceLocation poolId, int weight) {
		this.add(TwilightForestMod.prefix("_empty"), poolId, weight);
	}

	protected void add(ResourceLocation templateId, ResourceLocation poolId, int weight) {
		this.add(templateId, poolId, TemplatePoolInstance.defaultsWithWeight(weight));
	}

	protected void add(String roomId, ResourceLocation poolId, TemplatePoolInstance poolData) {
		this.add(TwilightForestMod.prefix(roomId), poolId, poolData);
	}

	protected void add(ResourceLocation templateId, ResourceLocation poolId, TemplatePoolInstance poolData) {
		Map<ResourceLocation, TemplatePoolInstance> poolWeightsForTemplate = this.poolsForTemplateWeights.computeIfAbsent(templateId, k -> new HashMap<>());

		poolWeightsForTemplate.put(poolId, poolData);
	}

	protected TemplatePoolInstance weightedPathTemplate(int weight, @Nullable Holder<TemplateMarkerHandlerList> markerHandlers, @Nullable Map<String, String> poolAliases) {
		// TODO register and reference instead
		Holder<StructureProcessorList> projectionProcessorHolder = Holder.direct(new StructureProcessorList(List.of(new GravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -2))));
		return new TemplatePoolInstance(
			Weight.of(weight),
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
			Weight.of(weight),
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

	@SuppressWarnings("unchecked")
	protected Holder.Reference<TemplateMarkerHandlerList> getMarkers(HolderLookup.Provider provider, ResourceKey<TemplateMarkerHandlerList> key) {
		return Holder.Reference.createStandAlone((HolderOwner<TemplateMarkerHandlerList>) provider.asGetterLookup().lookupOrThrow(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST), key);
	}

	@Override
	public String getName() {
		return String.format("%s generator for %s in %s", this.directory, this.name, this.modid);
	}
}
