package twilightforest.world.components.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public record StructureClearingConfig(boolean occupiesSurface, boolean occupiesUnderground, int additionalClearance) implements DecoratorConfiguration {
    public static final Codec<StructureClearingConfig> CODEC = RecordCodecBuilder.<StructureClearingConfig>create(instance -> instance.group(
            Codec.BOOL.fieldOf("occupies_surface").forGetter(StructureClearingConfig::occupiesSurface),
            Codec.BOOL.fieldOf("occupies_underground").forGetter(StructureClearingConfig::occupiesUnderground),
            Codec.INT.fieldOf("additional_clearance").forGetter(StructureClearingConfig::additionalClearance)
    ).apply(instance, StructureClearingConfig::new)).flatXmap(StructureClearingConfig::validate, StructureClearingConfig::validate);

    private static DataResult<StructureClearingConfig> validate(StructureClearingConfig config) {
        return config.occupiesSurface() || config.occupiesUnderground() ? DataResult.success(config) : DataResult.error("Feature Decorator cannot occupy neither surface nor underground");
    }
}
