package twilightforest.entity.passive;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import twilightforest.TFRegistries;
import twilightforest.init.custom.TinyBirdVariants;

import java.util.List;
import java.util.Optional;

public record TinyBirdVariant(Identifier texture, Optional<HolderSet<Biome>> spawnBiomes) {
	public static final Codec<TinyBirdVariant> DIRECT_CODEC = RecordCodecBuilder.create(
		p_332779_ -> p_332779_.group(
				Identifier.CODEC.fieldOf("texture").forGetter(TinyBirdVariant::texture),
				RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("biomes").forGetter(TinyBirdVariant::spawnBiomes)
			)
			.apply(p_332779_, TinyBirdVariant::new)
	);
	public static final Codec<Holder<TinyBirdVariant>> CODEC = RegistryFileCodec.create(TFRegistries.Keys.TINY_BIRD_VARIANT, DIRECT_CODEC);

	public TinyBirdVariant(Identifier texture) {
		this(texture, Optional.empty());
	}

	public static Holder<TinyBirdVariant> getVariant(RegistryAccess access, Holder<Biome> currentBiome, RandomSource random) {
		Registry<TinyBirdVariant> registry = access.lookupOrThrow(TFRegistries.Keys.TINY_BIRD_VARIANT);
		List<Holder.Reference<TinyBirdVariant>> validBirds = registry.filterElements(variant -> variant.spawnBiomes().isEmpty() || variant.spawnBiomes().get().contains(currentBiome)).listElements().toList();
		return validBirds.isEmpty() ? registry.getOrThrow(TinyBirdVariants.RED) : validBirds.get(random.nextInt(validBirds.size()));
	}
}
