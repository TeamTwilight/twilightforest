package twilightforest.world.components.structures.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public record TemplatePoolInstance(Weight weight, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection, TerrainAdjustment terrainAdjustment) implements WeightedEntry {
	private static final Codec<TemplatePoolInstance> CODEC_DIRECT = Codec.withAlternative(RecordCodecBuilder.create(instance -> instance.group(
		Weight.CODEC.fieldOf("weight").forGetter(TemplatePoolInstance::weight),
		StructureProcessorType.LIST_CODEC.fieldOf("processors").forGetter(TemplatePoolInstance::processors),
		StructureTemplatePool.Projection.CODEC.fieldOf("projection").forGetter(TemplatePoolInstance::projection),
		TerrainAdjustment.CODEC.fieldOf("terrain_adaptation").forGetter(TemplatePoolInstance::terrainAdjustment)
	).apply(instance, TemplatePoolInstance::new)), Codec.INT, TemplatePoolInstance::defaultsWithWeight);

	public static final Codec<TemplatePoolInstance> CODEC = new TemplatePoolInstanceCodec();

	public static TemplatePoolInstance defaultsWithWeight(int weight) {
		return new TemplatePoolInstance(
			Weight.of(weight),
			Holder.direct(new StructureProcessorList(Collections.emptyList())),
			StructureTemplatePool.Projection.RIGID,
			TerrainAdjustment.NONE
		);
	}

	@Override
	public Weight getWeight() {
		return this.weight;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || this.getClass() != o.getClass()) return false;
		TemplatePoolInstance that = (TemplatePoolInstance) o;
		return Objects.equals(this.weight(), that.weight())
			&& this.terrainAdjustment() == that.terrainAdjustment()
			&& Objects.equals(this.processors().value().list(), that.processors().value().list())
			&& this.projection() == that.projection();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.weight(), this.processors().value().list(), this.projection(), this.terrainAdjustment());
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
