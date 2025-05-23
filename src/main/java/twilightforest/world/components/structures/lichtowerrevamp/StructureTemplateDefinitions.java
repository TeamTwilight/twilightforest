package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class StructureTemplateDefinitions extends SimpleJsonResourceReloadListener<StructureTemplateDefinition> {
	public static final StructureTemplateDefinitions INSTANCE = new StructureTemplateDefinitions(); // TODO Autowired

	private final Map<ResourceLocation, SimpleWeightedRandomList<ResourceLocation>> templatePools = new HashMap<>();

	public static final String DIRECTORY = "twilight/template_definition";

	StructureTemplateDefinitions() {
		super(StructureTemplateDefinition.CODEC, FileToIdConverter.json(DIRECTORY));
	}

	@Override
	protected void apply(Map<ResourceLocation, StructureTemplateDefinition> map, ResourceManager manager, ProfilerFiller profiler) {
		this.templatePools.clear();

		final Map<ResourceLocation, SimpleWeightedRandomList.Builder<ResourceLocation>> rawTemplatePools = new HashMap<>();

		for(Map.Entry<ResourceLocation, StructureTemplateDefinition> mapEntry : map.entrySet()) {

			// Ensures that the order of elements stays deterministic between sessions, as Map/Set sorting are undefined behavior
			List<Map.Entry<ResourceLocation, Integer>> sorted = mapEntry.getValue().poolWeights().entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();

			for(Map.Entry<ResourceLocation, Integer> poolToRegisterWeight : sorted) {
				ResourceLocation templatePoolId = poolToRegisterWeight.getKey();
				Integer templateWeight = poolToRegisterWeight.getValue();

				SimpleWeightedRandomList.Builder<ResourceLocation> pool = rawTemplatePools.computeIfAbsent(templatePoolId, k -> SimpleWeightedRandomList.builder());

				pool.add(mapEntry.getKey(), templateWeight);
			}
		}

		for(Map.Entry<ResourceLocation, SimpleWeightedRandomList.Builder<ResourceLocation>> rawTemplatePool : rawTemplatePools.entrySet()) {
			this.templatePools.put(rawTemplatePool.getKey(), rawTemplatePool.getValue().build());
		}

		rawTemplatePools.clear();
	}

	@Nullable
	public ResourceLocation rollTemplatePool(RandomSource random, ResourceLocation templatePoolId) {
		SimpleWeightedRandomList<ResourceLocation> templatePool = this.templatePools.get(templatePoolId);
		return templatePool == null ? null : templatePool.getRandomValue(random).orElse(null);
	}

	// https://en.wikipedia.org/wiki/Reservoir_sampling
	public Iterable<ResourceLocation> shuffledTemplatePool(RandomSource random, ResourceLocation templatePoolId) {
		SimpleWeightedRandomList<ResourceLocation> templatePool = this.templatePools.get(templatePoolId);

		if (templatePool == null)
			return List.of();

		Map<ResourceLocation, Double> reservoirSampled = new HashMap<>();
		for (WeightedEntry.Wrapper<ResourceLocation> entry : templatePool.unwrap()) {
			double rand = random.nextDouble();
			reservoirSampled.put(entry.data(), -Math.log(rand) / entry.getWeight().asInt());
		}

		return reservoirSampled.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).collect(Collectors.toList());
	}
}
