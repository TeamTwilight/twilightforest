package twilightforest.data.custom.stalactites.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.List;

public record SpeleothemVarietyConfig(String type, List<ResourceLocation> baseStalactites, List<ResourceLocation> oreStalactites, List<ResourceLocation> stalagmites, float oreChance, float stalactiteChance, float stalagmiteChance, boolean replace) {
	public static final Codec<SpeleothemVarietyConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.xmap(String::toLowerCase, String::toLowerCase).fieldOf("type").forGetter(SpeleothemVarietyConfig::type),
			ResourceLocation.CODEC.listOf().fieldOf("base_stalactites").forGetter(SpeleothemVarietyConfig::baseStalactites),
			ResourceLocation.CODEC.listOf().fieldOf("ore_stalactites").forGetter(SpeleothemVarietyConfig::oreStalactites),
			ResourceLocation.CODEC.listOf().fieldOf("stalagmites").forGetter(SpeleothemVarietyConfig::stalagmites),
			Codec.floatRange(0.0F, 1.0F).fieldOf("ore_chance").forGetter(SpeleothemVarietyConfig::oreChance),
			Codec.floatRange(0.0F, 1.0F).fieldOf("stalactite_chance").forGetter(SpeleothemVarietyConfig::stalactiteChance),
			Codec.floatRange(0.0F, 1.0F).fieldOf("stalagmite_chance").forGetter(SpeleothemVarietyConfig::stalagmiteChance),
			Codec.BOOL.fieldOf("replace").forGetter(SpeleothemVarietyConfig::replace)
	).apply(instance, SpeleothemVarietyConfig::new));

	public boolean shouldDoAStalactite(RandomSource rand) {
		return rand.nextFloat() < this.stalactiteChance();
	}

	public boolean shouldDoAStalagmite(RandomSource rand) {
		return rand.nextFloat() < this.stalagmiteChance();
	}
}
