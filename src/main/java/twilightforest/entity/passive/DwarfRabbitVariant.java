package twilightforest.entity.passive;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import twilightforest.TFRegistries;
import twilightforest.init.custom.DwarfRabbitVariants;
import twilightforest.init.custom.TinyBirdVariants;

import java.util.List;
import java.util.Optional;

public record DwarfRabbitVariant(ResourceLocation texture, Optional<HolderSet<Biome>> spawnBiomes) {
	public static final Codec<DwarfRabbitVariant> DIRECT_CODEC = RecordCodecBuilder.create(
		p_332779_ -> p_332779_.group(
				ResourceLocation.CODEC.fieldOf("texture").forGetter(DwarfRabbitVariant::texture),
				RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("biomes").forGetter(DwarfRabbitVariant::spawnBiomes)
			)
			.apply(p_332779_, DwarfRabbitVariant::new)
	);
	public static final Codec<Holder<DwarfRabbitVariant>> CODEC = RegistryFileCodec.create(TFRegistries.Keys.DWARF_RABBIT_VARIANT, DIRECT_CODEC);

	public DwarfRabbitVariant(ResourceLocation texture) {
		this(texture, Optional.empty());
	}

	public static Holder<DwarfRabbitVariant> getVariant(RegistryAccess access, Holder<Biome> currentBiome, RandomSource random) {
		Registry<DwarfRabbitVariant> registry = access.lookupOrThrow(TFRegistries.Keys.DWARF_RABBIT_VARIANT);
		List<Holder.Reference<DwarfRabbitVariant>> validBunnies = registry.listElements().filter(variant -> variant.value().spawnBiomes().isEmpty() || variant.value().spawnBiomes().get().contains(currentBiome)).toList();
		return validBunnies.isEmpty() ? registry.getOrThrow(DwarfRabbitVariants.BROWN) : validBunnies.get(random.nextInt(validBunnies.size()));
	}

	public static Holder<DwarfRabbitVariant> getRandomCommonVariant(RegistryAccess access, RandomSource random) {
		Registry<DwarfRabbitVariant> registry = access.lookupOrThrow(TFRegistries.Keys.DWARF_RABBIT_VARIANT);
		List<Holder.Reference<DwarfRabbitVariant>> validBunnies = registry.listElements().filter(variant -> variant.value().spawnBiomes().isEmpty()).toList();
		return validBunnies.isEmpty() ? registry.getOrThrow(DwarfRabbitVariants.BROWN) : validBunnies.get(random.nextInt(validBunnies.size()));
	}
}
