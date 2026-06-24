package twilightforest.datagen.data.custom;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.lichtowerrevamp.StructureTemplateDefinition;
import twilightforest.world.components.structures.lichtowerrevamp.StructureTemplateDefinitions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class StructureTemplateDefinitionProvider extends JsonCodecProvider<StructureTemplateDefinition> {
	private final Map<Identifier, Map<Identifier, Integer>> poolsForTemplateWeights = new HashMap<>();

	public StructureTemplateDefinitionProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
		super(output, PackOutput.Target.DATA_PACK, StructureTemplateDefinitions.DIRECTORY, StructureTemplateDefinition.CODEC, lookupProvider, modId);
	}

	protected abstract void generatePools();

	@Override
	protected void gather() {
		this.generatePools();

		for(Map.Entry<Identifier, Map<Identifier, Integer>> poolWeightsForTemplate : this.poolsForTemplateWeights.entrySet()) {
			Identifier templateId = poolWeightsForTemplate.getKey();

			this.unconditional(templateId, new StructureTemplateDefinition(poolWeightsForTemplate.getValue()));
		}
	}

	protected void addToAllPools(String roomId, int weight, Identifier... poolIds) {
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

	protected void add(Identifier templateId, Identifier poolId, int weight) {
		Map<Identifier, Integer> poolWeightsForTemplate = this.poolsForTemplateWeights.computeIfAbsent(templateId, k -> new HashMap<>());

		poolWeightsForTemplate.put(poolId, weight);
	}
}
