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
import twilightforest.beans.Component;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.world.components.structures.TwilightJigsawPiece;

import java.util.*;
import java.util.stream.Collectors;

@Component
public final class StructureTemplateDefinitions extends CodecResourceReloadListener<StructureTemplateDefinition> {

	private final Map<ResourceLocation, Map<ResourceLocation, TemplatePoolInstance>> rawTemplatePools = new HashMap<>();
	private final Map<ResourceLocation, WeightedRandomList<TemplatePoolEntry>> templatePools = new HashMap<>();

	public static final String DIRECTORY = "twilight/template_definition";

	public StructureTemplateDefinitions() {
		super(DIRECTORY, StructureTemplateDefinition.CODEC);
	}

	@Override
	protected void forLocation(ResourceManager manager, ResourceLocation templateName, StructureTemplateDefinition templateDefinition) {
		for (Map.Entry<ResourceLocation, TemplatePoolInstance> poolToRegisterWeight : templateDefinition.poolWeights().entrySet()) {
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

		for (Map.Entry<ResourceLocation, Map<ResourceLocation, TemplatePoolInstance>> rawTemplatePool : this.rawTemplatePools.entrySet()) {
			ArrayList<TemplatePoolEntry> poolBuilder = new ArrayList<>();

			// Ensures that the order of elements stays deterministic between sessions, as Sets are not implicitly ordered
			List<Map.Entry<ResourceLocation, TemplatePoolInstance>> sortedTemplateWeights = rawTemplatePool.getValue().entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();
			for (Map.Entry<ResourceLocation, TemplatePoolInstance> templateIdWeight : sortedTemplateWeights) {
				poolBuilder.add(new TemplatePoolEntry(templateIdWeight.getKey(), templateIdWeight.getValue()));
			}

			ResourceLocation templatePoolId = rawTemplatePool.getKey();
			this.templatePools.put(templatePoolId, WeightedRandomList.create(poolBuilder));
		}

		this.rawTemplatePools.clear();
	}

	private Optional<TemplatePoolEntry> getRandomEntry(RandomSource random, ResourceLocation templatePoolId) {
		WeightedRandomList<TemplatePoolEntry> templatePool = this.templatePools.get(templatePoolId);
		return templatePool == null ? Optional.empty() : templatePool.getRandom(random);
	}

	@Nullable
	public ResourceLocation getRandomTemplate(RandomSource random, ResourceLocation templatePoolId) {
		return this.getRandomEntry(random, templatePoolId).map(TemplatePoolEntry::templateLocation).orElse(null);
	}

	// https://en.wikipedia.org/wiki/Reservoir_sampling
	public Iterable<ResourceLocation> getShuffledSequence(RandomSource random, ResourceLocation templatePoolId) {
		WeightedRandomList<TemplatePoolEntry> templatePool = this.templatePools.get(templatePoolId);

		if (templatePool == null)
			return Collections.emptyList();

		Map<ResourceLocation, Double> reservoirSampled = new HashMap<>();
		for (TemplatePoolEntry entry : templatePool.unwrap()) {
			double rand = random.nextDouble();
			reservoirSampled.put(entry.templateLocation(), -Math.log(rand) / entry.getWeight().asInt());
		}

		return reservoirSampled.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).collect(Collectors.toList());
	}

	// TODO initializeStubFromPool to return GenerationStub

	@Nullable
	public TwilightJigsawPiece initializeTemplateFromPool(ResourceLocation templatePool, BlockPos parentJunctionPos, FrontAndTop parentOrientation, String selectName, RandomSource rand, int genDepth, StructureTemplateManager structureManager) {
		Optional<TemplatePoolEntry> entryOptional = this.getRandomEntry(rand, templatePool);

		if (entryOptional.isEmpty())
			return null;

		TemplatePoolEntry templateEntry = entryOptional.get();
		JigsawPlaceContext placeContext = JigsawPlaceContext.pickPlaceableJunction(parentJunctionPos, BlockPos.ZERO, parentOrientation, structureManager, templateEntry.templateLocation, selectName, rand);

		if (placeContext == null)
			return null;

		return TwilightJigsawPiece.defaultForTemplate(genDepth, structureManager, templateEntry.templateLocation, placeContext, templateEntry.instance);
	}

	private record TemplatePoolEntry(ResourceLocation templateLocation, TemplatePoolInstance instance) implements WeightedEntry {
		@Override
		public Weight getWeight() {
			return this.instance.getWeight();
		}
	}
}
