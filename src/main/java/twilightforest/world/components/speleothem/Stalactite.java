package twilightforest.world.components.speleothem;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Objects;

public record Stalactite(Either<List<Pair<Block, Integer>>, Block> ores, float sizeVariation, int maxLength, int weight) {

	public static final Codec<Stalactite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.either(Codec.pair(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").codec(), Codec.INT.fieldOf("weight").codec()).listOf(), BuiltInRegistries.BLOCK.byNameCodec()).fieldOf("ores").forGetter(o -> o.ores),
		Codec.FLOAT.fieldOf("size_variation").forGetter(o -> o.sizeVariation),
		Codec.INT.fieldOf("max_length").forGetter(o -> o.maxLength),
		Codec.INT.fieldOf("weight").forGetter(o -> o.weight)
	).apply(instance, Stalactite::new));

	@Override
	public String toString() {
		return String.format("Stalactite: Ore:%s, Length:%s, Variation:%s, Weight:%s", this.ores(), this.maxLength(), this.sizeVariation(), this.weight());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.ores(), this.maxLength(), this.sizeVariation(), this.weight());
	}
}
