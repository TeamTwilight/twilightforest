package twilightforest.world.components.structures.util;

import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.FrontAndTop;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.world.components.structures.TwilightJigsawPiece;

import java.util.*;
import java.util.stream.Collectors;

public class StructureTemplateDefinitions extends CodecResourceReloadListener<StructureTemplateDefinition> {
	public static final StructureTemplateDefinitions INSTANCE = new StructureTemplateDefinitions(); // TODO Autowired

	private final Map<ResourceLocation, Map<ResourceLocation, TemplatePoolInstance>> rawTemplatePools = new HashMap<>();
	private final Map<ResourceLocation, WeightedRandomList<PoolEntry>> templatePools = new HashMap<>();

	public static final String DIRECTORY = "twilight/template_definition";

	private StructureTemplateDefinitions() {
		super(DIRECTORY, StructureTemplateDefinition.CODEC);
	}

	@Override
	protected void forLocation(ResourceManager manager, ResourceLocation templateName, StructureTemplateDefinition templateDefinition) {
		for(Map.Entry<ResourceLocation, TemplatePoolInstance> poolToRegisterWeight : templateDefinition.poolWeights().entrySet()) {
			ResourceLocation templatePoolId = poolToRegisterWeight.getKey();
			TemplatePoolInstance templatePoolInstance = poolToRegisterWeight.getValue();

			Map<ResourceLocation, TemplatePoolInstance> pool = this.rawTemplatePools.computeIfAbsent(templatePoolId, k -> new HashMap<>());

			pool.put(templateName, templatePoolInstance);
		}
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
		this.rawTemplatePools.clear();
		this.templatePools.clear();

		super.apply(map, manager, profiler);

		for(Map.Entry<ResourceLocation, Map<ResourceLocation, TemplatePoolInstance>> rawTemplatePool : this.rawTemplatePools.entrySet()) {
			ArrayList<PoolEntry> poolBuilder = new ArrayList<>();

			// Ensures that the order of elements stays deterministic between sessions, as Sets are not implicitly ordered
			List<Map.Entry<ResourceLocation, TemplatePoolInstance>> sortedTemplateWeights = rawTemplatePool.getValue().entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();
			for (Map.Entry<ResourceLocation, TemplatePoolInstance> templateIdWeight : sortedTemplateWeights) {
				poolBuilder.add(new PoolEntry(templateIdWeight.getKey(), templateIdWeight.getValue()));
			}

			ResourceLocation templatePoolId = rawTemplatePool.getKey();
			this.templatePools.put(templatePoolId, WeightedRandomList.create(poolBuilder));
		}

		this.rawTemplatePools.clear();
	}

	@Nullable
	private ResourceLocation rollTemplatePool(RandomSource random, ResourceLocation templatePoolId) {
		WeightedRandomList<PoolEntry> templatePool = this.templatePools.get(templatePoolId);
		return templatePool == null ? null : templatePool.getRandom(random).map(PoolEntry::templateLocation).orElse(null);
	}

	// https://en.wikipedia.org/wiki/Reservoir_sampling
	private Iterable<ResourceLocation> shuffledTemplatePool(RandomSource random, ResourceLocation templatePoolId) {
		WeightedRandomList<PoolEntry> templatePool = this.templatePools.get(templatePoolId);

		if (templatePool == null)
			return List.of();

		Map<ResourceLocation, Double> reservoirSampled = new HashMap<>();
		for (PoolEntry entry : templatePool.unwrap()) {
			double rand = random.nextDouble();
			reservoirSampled.put(entry.templateLocation(), -Math.log(rand) / entry.getWeight().asInt());
		}

		return reservoirSampled.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).collect(Collectors.toList());
	}

	@Nullable // TODO Autowired
	public static ResourceLocation getRandomTemplate(RandomSource random, ResourceLocation poolId) {
		return INSTANCE.rollTemplatePool(random, poolId);
	}

	// TODO Autowired
	public static Iterable<ResourceLocation> getShuffledSequence(RandomSource random, ResourceLocation poolId) {
		return INSTANCE.shuffledTemplatePool(random, poolId);
	}

	// TODO initializeStubFromPool

	@Nullable
	public TwilightJigsawPiece initializeTemplateFromPool(ResourceLocation templatePool, BlockPos parentJunctionPos, FrontAndTop parentOrientation, String selectName, RandomSource rand, int genDepth, StructureTemplateManager structureManager) {
		ResourceLocation templateId = this.rollTemplatePool(rand, templatePool);
		JigsawPlaceContext placeContext = JigsawPlaceContext.pickPlaceableJunction(parentJunctionPos, BlockPos.ZERO, parentOrientation, structureManager, templateId, selectName, rand);

		if (templateId == null || placeContext == null)
			return null;

		return TwilightJigsawPiece.defaultForTemplate(genDepth, structureManager, templateId, placeContext);
	}

	private record PoolEntry(ResourceLocation templateLocation, TemplatePoolInstance instance) implements WeightedEntry {
		@Override
		public Weight getWeight() {
			return this.instance.getWeight();
		}
	}
}
