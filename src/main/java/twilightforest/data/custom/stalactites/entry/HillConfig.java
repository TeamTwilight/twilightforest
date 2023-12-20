package twilightforest.data.custom.stalactites.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Locale;

public record HillConfig(HillType type, List<ResourceLocation> baseStalactites, List<ResourceLocation> oreStalactites, List<ResourceLocation> stalagmites, float oreChance, float stalactiteChance, float stalagmiteChance, boolean replace) {
	public static final Codec<HillConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			HillType.CODEC.fieldOf("type").forGetter(HillConfig::type),
			ResourceLocation.CODEC.listOf().fieldOf("base_stalactites").forGetter(HillConfig::baseStalactites),
			ResourceLocation.CODEC.listOf().fieldOf("ore_stalactites").forGetter(HillConfig::oreStalactites),
			ResourceLocation.CODEC.listOf().fieldOf("stalagmites").forGetter(HillConfig::stalagmites),
			Codec.floatRange(0.0F, 1.0F).fieldOf("ore_chance").forGetter(HillConfig::oreChance),
			Codec.floatRange(0.0F, 1.0F).fieldOf("stalactite_chance").forGetter(HillConfig::stalactiteChance),
			Codec.floatRange(0.0F, 1.0F).fieldOf("stalagmite_chance").forGetter(HillConfig::stalagmiteChance),
			Codec.BOOL.fieldOf("replace").forGetter(HillConfig::replace)
	).apply(instance, HillConfig::new));

	public boolean shouldDoAStalactite(RandomSource rand) {
		return rand.nextFloat() < this.stalactiteChance();
	}

	public boolean shouldDoAStalagmite(RandomSource rand) {
		return rand.nextFloat() < this.stalagmiteChance();
	}

	public enum HillType implements StringRepresentable {
		SMALL_HOLLOW_HILL,
		MEDIUM_HOLLOW_HILL,
		LARGE_HOLLOW_HILL,
		YETI_CAVE,
		HYDRA_LAIR,
		TROLL_CAVES;

		public static final StringRepresentable.EnumCodec<HillType> CODEC = StringRepresentable.fromEnum(HillType::values);

		@Override
		public String getSerializedName() {
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
}
