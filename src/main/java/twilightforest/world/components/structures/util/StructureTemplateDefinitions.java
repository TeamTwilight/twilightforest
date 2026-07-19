package twilightforest.world.components.structures.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.FrontAndTop;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Component;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.world.components.structures.TwilightJigsawPiece;

import java.util.*;
import java.util.stream.Collectors;

@Component
public final class StructureTemplateDefinitions extends CodecResourceReloadListener<StructureTemplateDefinition> {

	private final Map<Identifier, Map<Identifier, TemplatePoolInstance>> rawTemplatePools = new HashMap<>();
	private final Map<Identifier, WeightedList<TemplatePoolEntry>> templatePools = new HashMap<>();

	public static final String DIRECTORY = "twilight/template_definition";

	public StructureTemplateDefinitions() {
		super(DIRECTORY, StructureTemplateDefinition.CODEC);
	}

	@Override
	protected void forLocation(ResourceManager manager, Identifier templateName, StructureTemplateDefinition templateDefinition) {
		for (Map.Entry<Identifier, TemplatePoolInstance> poolToRegisterWeight : templateDefinition.poolWeights().entrySet()) {
			Identifier templatePoolId = poolToRegisterWeight.getKey();
			TemplatePoolInstance templatePoolInstance = poolToRegisterWeight.getValue();

			Map<Identifier, TemplatePoolInstance> pool = this.rawTemplatePools.computeIfAbsent(templatePoolId, k -> new HashMap<>());

			pool.put(templateName, templatePoolInstance);
		}
	}

	@Override
	protected void afterApply(ResourceManager manager, ProfilerFiller profiler) {
		this.templatePools.clear();

		for (Map.Entry<Identifier, Map<Identifier, TemplatePoolInstance>> rawTemplatePool : rawTemplatePools.entrySet()) {
            WeightedList.Builder<TemplatePoolEntry> builder = WeightedList.builder();

			// Ensures that the order of elements stays deterministic between sessions, as Sets are not implicitly ordered
			List<Map.Entry<Identifier, TemplatePoolInstance>> sortedTemplateWeights = rawTemplatePool.getValue().entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();
			for (Map.Entry<Identifier, TemplatePoolInstance> templatePoolEntry : sortedTemplateWeights) {
				builder.add(new TemplatePoolEntry(templatePoolEntry.getKey(), templatePoolEntry.getValue()), templatePoolEntry.getValue().weight());
			}

			this.templatePools.put(rawTemplatePool.getKey(), builder.build());
		}

		this.rawTemplatePools.clear();
	}

	private Optional<TemplatePoolEntry> getRandomEntry(RandomSource random, Identifier templatePoolId) {
		WeightedList<TemplatePoolEntry> templatePool = this.templatePools.get(templatePoolId);
		return templatePool == null ? Optional.empty() : templatePool.getRandom(random);
	}

	@Nullable
	public Identifier getRandomTemplate(RandomSource random, Identifier templatePoolId) {
		return this.getRandomEntry(random, templatePoolId).map(TemplatePoolEntry::templateId).orElse(null);
	}

	// https://en.wikipedia.org/wiki/Reservoir_sampling
	public Iterable<Identifier> getShuffledSequence(RandomSource random, Identifier templatePoolId) {
		WeightedList<TemplatePoolEntry> templatePool = this.templatePools.get(templatePoolId);

		if (templatePool == null)
			return Collections.emptyList();

		Map<Identifier, Double> reservoirSampled = new HashMap<>();
		for (Weighted<TemplatePoolEntry> entry : templatePool.unwrap()) {
			double rand = random.nextDouble();
			reservoirSampled.put(entry.value().templateId(), -Math.log(rand) / entry.weight());
		}

		return reservoirSampled.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).collect(Collectors.toList());
	}

	// TODO initializeStubFromPool to return GenerationStub

	@Deprecated // Prefer below initializeTemplateFromPool
	@Nullable
	public TwilightJigsawPiece initializeTemplateFromPool(Identifier templatePool, BlockPos parentJunctionPos, FrontAndTop parentOrientation, String selectName, RandomSource rand, int genDepth, StructureTemplateManager structureManager) {
		Optional<TemplatePoolEntry> entryOptional = this.getRandomEntry(rand, templatePool);

		if (entryOptional.isEmpty())
			return null;

		TemplatePoolEntry templateEntry = entryOptional.get();
		JigsawPlaceContext placeContext = JigsawPlaceContext.pickPlaceableJunction(parentJunctionPos, BlockPos.ZERO, parentOrientation, structureManager, templateEntry.templateId, selectName, rand);

		if (placeContext == null)
			return null;

		return TwilightJigsawPiece.defaultForTemplate(genDepth, structureManager, templateEntry.templateId, placeContext, templateEntry.instance, templateEntry.instance.chooseRandomProcessors(rand));
	}

	@Nullable
	public TwilightJigsawPiece initializeTemplateFromPool(Identifier templatePool, BlockPos parentJunctionPos, FrontAndTop parentOrientation, String selectName, Structure.GenerationContext generationContext, int genDepth, boolean parentProjectsTerrain) {
		RandomSource random = generationContext.random();
		Optional<TemplatePoolEntry> entryOptional = this.getRandomEntry(random, templatePool);
		if (entryOptional.isEmpty())
			return null;

		TemplatePoolEntry templateEntry = entryOptional.get();
		JigsawPlaceContext placeContext = JigsawPlaceContext.pickPlaceableJunction(parentJunctionPos, BlockPos.ZERO, parentOrientation, generationContext.structureTemplateManager(), templateEntry.templateId, selectName, random);

		if (placeContext == null)
			return null;
		return TwilightJigsawPiece.defaultForTemplate(genDepth, generationContext.structureTemplateManager(), templateEntry.templateId, templateEntry.instance.adjustContextForTerrain(placeContext, generationContext, parentProjectsTerrain), templateEntry.instance, templateEntry.instance.chooseRandomProcessors(random));
	}

	@Override
	public String getListenerName() {
		return "template_definitions";
	}

	private record TemplatePoolEntry(Identifier templateId, TemplatePoolInstance instance) {}
}
