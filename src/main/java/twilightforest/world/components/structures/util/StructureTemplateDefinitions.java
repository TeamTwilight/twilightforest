package twilightforest.world.components.structures.util;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public final class StructureTemplateDefinitions extends SimpleJsonResourceReloadListener<@NotNull StructureTemplateDefinition> {
	public static final StructureTemplateDefinitions INSTANCE = new StructureTemplateDefinitions(); //TODO Autowired

	private final Map<Identifier, WeightedList<@NotNull Identifier>> templatePools = new HashMap<>();

	public static final String DIRECTORY = "twilight/template_definition";

	public StructureTemplateDefinitions() {
		super(StructureTemplateDefinition.CODEC, FileToIdConverter.json(DIRECTORY));
	}

	@Override
	public void apply(Map<Identifier, StructureTemplateDefinition> map, ResourceManager manager, ProfilerFiller profiler) {
		this.templatePools.clear();

		final Map<Identifier, WeightedList.Builder<@NotNull Identifier>> rawTemplatePools = new HashMap<>();

		for (Map.Entry<Identifier, StructureTemplateDefinition> rawTemplatePool : map.entrySet()) {
			// Ensures that the order of elements stays deterministic between sessions, as Sets are not implicitly ordered
			List<Map.Entry<Identifier, Integer>> sortedTemplateWeights = rawTemplatePool.getValue().poolWeights().entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();

			for (Map.Entry<Identifier, Integer> templateIdWeight : sortedTemplateWeights) {
				Identifier templatePoolId = templateIdWeight.getKey();
				int weight = templateIdWeight.getValue();

				WeightedList.Builder<@NotNull Identifier> pool = rawTemplatePools.computeIfAbsent(templatePoolId, k -> WeightedList.builder());

				pool.add(rawTemplatePool.getKey(), weight);
			}

			for(Map.Entry<Identifier, WeightedList.Builder<@NotNull Identifier>> rawPool : rawTemplatePools.entrySet()) {
				this.templatePools.put(rawPool.getKey(), rawPool.getValue().build());
			}
		}

		rawTemplatePools.clear();
	}

	@Nullable
	public Identifier rollTemplatePool(RandomSource random, Identifier templatePoolId) {
		WeightedList<@NotNull Identifier> templatePool = this.templatePools.get(templatePoolId);
		return templatePool == null ? null : templatePool.getRandom(random).orElse(null);
	}

	// https://en.wikipedia.org/wiki/Reservoir_sampling
	public Iterable<Identifier> getShuffledSequence(RandomSource random, Identifier templatePoolId) {
		WeightedList<@NotNull Identifier> templatePool = this.templatePools.get(templatePoolId);

		if (templatePool == null)
			return Collections.emptyList();

		Map<Identifier, Double> reservoirSampled = new HashMap<>();
		for (Weighted<@NotNull Identifier> entry : templatePool.unwrap()) {
			double rand = random.nextDouble();
			reservoirSampled.put(entry.value(), -Math.log(rand) / entry.weight());
		}

		return reservoirSampled.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).collect(Collectors.toList());
	}

	// TODO initializeStubFromPool to return GenerationStub
}
