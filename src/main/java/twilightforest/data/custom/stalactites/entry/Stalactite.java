package twilightforest.data.custom.stalactites.entry;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record Stalactite(Either<List<Pair<Block, Integer>>, Block> ores, float sizeVariation, int maxLength, int weight) {

	@Nullable
	private static StalactiteReloadListener STALACTITE_CONFIG;

	public static final Codec<Stalactite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ExtraCodecs.either(Codec.pair(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").codec(), Codec.INT.fieldOf("weight").codec()).listOf(), BuiltInRegistries.BLOCK.byNameCodec()).fieldOf("ores").forGetter(o -> o.ores),
			Codec.FLOAT.fieldOf("size_variation").forGetter(o -> o.sizeVariation),
			Codec.INT.fieldOf("max_length").forGetter(o -> o.maxLength),
			Codec.INT.fieldOf("weight").forGetter(o -> o.weight)
	).apply(instance, Stalactite::new));

	public static void reloadStalactites(AddReloadListenerEvent event) {
		STALACTITE_CONFIG = new StalactiteReloadListener();
		event.addListener(STALACTITE_CONFIG);
	}

	public static StalactiteReloadListener getStalactiteConfig() {
		if (STALACTITE_CONFIG == null)
			throw new IllegalStateException("Can't retrieve Stalactites yet!");
		return STALACTITE_CONFIG;
	}
}
