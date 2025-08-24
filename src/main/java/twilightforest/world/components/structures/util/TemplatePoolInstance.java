package twilightforest.world.components.structures.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

import java.util.Optional;

public record TemplatePoolInstance(Weight weight) implements WeightedEntry {
	private static final Codec<TemplatePoolInstance> CODEC_DIRECT = Codec.withAlternative(RecordCodecBuilder.create(instance -> instance.group(
		Weight.CODEC.fieldOf("weight").forGetter(TemplatePoolInstance::weight)
	).apply(instance, TemplatePoolInstance::new)), Codec.INT, TemplatePoolInstance::defaultsWithWeight);

	public static final Codec<TemplatePoolInstance> CODEC = new Codec<>() {
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
				return DataResult.error(() -> "TemplatePoolInstance CODEC_COMPACT deserialization problem:\n" + parse + "\n\n from data:\n" + input);
			}
			return DataResult.success(Pair.of(templatePoolInstance.get(), input));
		}
	};

	public static TemplatePoolInstance defaultsWithWeight(int weight) {
		return new TemplatePoolInstance(Weight.of(weight));
	}

	@Override
	public Weight getWeight() {
		return this.weight;
	}

	// TODO what other data? Processors? Terrain adjustment? Beardifier?
}
