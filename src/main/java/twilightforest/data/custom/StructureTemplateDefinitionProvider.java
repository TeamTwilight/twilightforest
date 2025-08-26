package twilightforest.data.custom;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.util.StructureTemplateDefinition;
import twilightforest.world.components.structures.util.StructureTemplateDefinitions;
import twilightforest.world.components.structures.util.TemplatePoolInstance;

import java.util.HashMap;
import java.util.Map;
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

	@Override
	public String getName() {
		return String.format("%s generator for %s in %s", this.directory, this.name, this.modid);
	}
}
