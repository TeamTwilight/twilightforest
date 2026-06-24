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
import twilightforest.init.custom.DwarfRabbitVariants;

import java.util.List;
import java.util.Optional;

public record DwarfRabbitVariant(Identifier texture, Optional<HolderSet<Biome>> spawnBiomes) {
	public static final Codec<DwarfRabbitVariant> DIRECT_CODEC = RecordCodecBuilder.create(
		p_332779_ -> p_332779_.group(
				Identifier.CODEC.fieldOf("texture").forGetter(DwarfRabbitVariant::texture),
				RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("biomes").forGetter(DwarfRabbitVariant::spawnBiomes)
			)
			.apply(p_332779_, DwarfRabbitVariant::new)
	);
	public static final Codec<Holder<DwarfRabbitVariant>> CODEC = RegistryFileCodec.create(TFRegistries.Keys.DWARF_RABBIT_VARIANT, DIRECT_CODEC);

	public DwarfRabbitVariant(Identifier texture) {
		this(texture, Optional.empty());
	}

	public static Holder<DwarfRabbitVariant> getVariant(RegistryAccess access, Holder<Biome> currentBiome, RandomSource random) {
		Registry<DwarfRabbitVariant> registry = access.lookupOrThrow(TFRegistries.Keys.DWARF_RABBIT_VARIANT);
		List<Holder.Reference<DwarfRabbitVariant>> validBunnies = registry.filterElements(variant -> variant.spawnBiomes().isEmpty() || variant.spawnBiomes().get().contains(currentBiome)).listElements().toList();
		return validBunnies.isEmpty() ? registry.getOrThrow(DwarfRabbitVariants.BROWN) : validBunnies.get(random.nextInt(validBunnies.size()));
	}

	public static Holder<DwarfRabbitVariant> getRandomCommonVariant(RegistryAccess access, RandomSource random) {
		Registry<DwarfRabbitVariant> registry = access.lookupOrThrow(TFRegistries.Keys.DWARF_RABBIT_VARIANT);
		List<Holder.Reference<DwarfRabbitVariant>> validBunnies = registry.filterElements(variant -> variant.spawnBiomes().isEmpty()).listElements().toList();
		return validBunnies.isEmpty() ? registry.getOrThrow(DwarfRabbitVariants.BROWN) : validBunnies.get(random.nextInt(validBunnies.size()));
	}
}
