package twilightforest.world.components.structures.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import twilightforest.util.WorldUtil;
import twilightforest.util.jigsaw.JigsawPlaceContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record TemplatePoolInstance(Weight weight, Optional<Holder<StructureProcessorList>> processors, StructureTemplatePool.Projection projection, TerrainAdjustment terrainAdjustment, Optional<HeightAdjustment> heightAdjustment, boolean ignoreWorldWaterlog) implements WeightedEntry {
	private static final Codec<TemplatePoolInstance> CODEC_DIRECT = Codec.withAlternative(RecordCodecBuilder.create(instance -> instance.group(
		Weight.CODEC.fieldOf("weight").forGetter(TemplatePoolInstance::weight),
		StructureProcessorType.LIST_CODEC.optionalFieldOf("processors").forGetter(TemplatePoolInstance::processors),
		StructureTemplatePool.Projection.CODEC.optionalFieldOf("projection", StructureTemplatePool.Projection.RIGID).forGetter(TemplatePoolInstance::projection),
		TerrainAdjustment.CODEC.optionalFieldOf("terrain_adaptation", TerrainAdjustment.NONE).forGetter(TemplatePoolInstance::terrainAdjustment),
		HeightAdjustment.CODEC.optionalFieldOf("height_adjustment").forGetter(TemplatePoolInstance::heightAdjustment),
		Codec.BOOL.optionalFieldOf("ignore_world_waterlog", false).forGetter(TemplatePoolInstance::ignoreWorldWaterlog)
	).apply(instance, TemplatePoolInstance::new)), Codec.INT, TemplatePoolInstance::defaultsWithWeight);

	public static final Codec<TemplatePoolInstance> CODEC = new TemplatePoolInstanceCodec();

	public static TemplatePoolInstance defaultsWithWeight(int weight) {
		return new TemplatePoolInstance(
			Weight.of(weight),
			Optional.empty(),
			StructureTemplatePool.Projection.RIGID,
			TerrainAdjustment.NONE,
			Optional.empty(),
			false
		);
	}

	@Override
	public Weight getWeight() {
		return this.weight;
	}

	@SuppressWarnings("OptionalIsPresent")
	public JigsawPlaceContext adjustContextForTerrain(JigsawPlaceContext placeContext, Structure.GenerationContext generationContext) {
		if (this.heightAdjustment.isEmpty())
			return placeContext;

		return this.heightAdjustment.get().adjustForTerrain(placeContext, generationContext);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || this.getClass() != o.getClass()) return false;
		TemplatePoolInstance that = (TemplatePoolInstance) o;
		return Objects.equals(this.weight(), that.weight())
			&& this.terrainAdjustment() == that.terrainAdjustment()
			&& ((this.processors.isEmpty() && that.processors.isEmpty()) || ((this.processors.isPresent() && that.processors.isPresent()) && Objects.equals(this.processors().get().value().list(), that.processors().get().value().list())))
			&& this.projection() == that.projection();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.weight(), this.processors().map(Holder::value).map(StructureProcessorList::list), this.projection(), this.terrainAdjustment());
	}

	public record HeightAdjustment(Heightmap.Types heightType, int yOffset, Optional<Integer> groundJunctionDiffLimit) {
		public static final Codec<HeightAdjustment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(HeightAdjustment::heightType),
			Codec.INT.optionalFieldOf("y_offset", 0).forGetter(HeightAdjustment::yOffset),
			Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("ground_junction_diff_clamp").forGetter(HeightAdjustment::groundJunctionDiffLimit)
		).apply(instance, HeightAdjustment::new));

		public JigsawPlaceContext adjustForTerrain(JigsawPlaceContext placeContext, Structure.GenerationContext generationContext) {
			BoundingBox box = placeContext.makeBoundingBox(generationContext.structureTemplateManager());
			int yAdjusted = this.clampYLevel(generationContext, placeContext, box);
			return new JigsawPlaceContext(
				placeContext.templatePos().atY(yAdjusted),
				placeContext.placementSettings().copy(),
				placeContext.seedJigsaw(),
				List.copyOf(placeContext.spareJigsaws()),
				placeContext.templateLocation()
			);
		}

		private int clampYLevel(Structure.GenerationContext generationContext, JigsawPlaceContext placeContext, BoundingBox box) {
			if (this.groundJunctionDiffLimit.isEmpty()) {
				return this.yOffset + WorldUtil.adjustForTerrain(generationContext, box.minX(), box.minZ(), box.maxX(), box.maxZ(), 2, this.heightType);
			}

			int templateY = placeContext.templatePos().getY();
			int variantLimit = this.groundJunctionDiffLimit.get();
			if (variantLimit == 0) {
				return templateY;
			}

			int yTerrain = WorldUtil.adjustForTerrain(generationContext, box.minX(), box.minZ(), box.maxX(), box.maxZ(), 2, this.heightType);
			return Mth.clamp(this.yOffset + yTerrain, templateY - variantLimit, templateY + variantLimit);
		}
	}

	private static class TemplatePoolInstanceCodec implements Codec<TemplatePoolInstance> {
		@Override
		public <T> DataResult<T> encode(TemplatePoolInstance input, DynamicOps<T> ops, T prefix) {
			int weight = input.weight.asInt();
			if (input.equals(TemplatePoolInstance.defaultsWithWeight(weight))) {
				return DataResult.success(ops.createInt(weight));
			}

			return CODEC_DIRECT.encodeStart(ops, input);
		}

		@SuppressWarnings("OptionalIsPresent")
		@Override
		public <T> DataResult<Pair<TemplatePoolInstance, T>> decode(DynamicOps<T> ops, T input) {
			DataResult<TemplatePoolInstance> parse = CODEC_DIRECT.parse(ops, input);
			Optional<TemplatePoolInstance> templatePoolInstance = parse.resultOrPartial();
			if (templatePoolInstance.isEmpty()) {
				return DataResult.error(() -> "TemplatePoolInstance.CODEC deserialization problem:\n" + parse + "\n\n from data:\n" + input);
			}
			return DataResult.success(Pair.of(templatePoolInstance.get(), input));
		}
	}
}
