package twilightforest.world.components.structures.util;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public abstract class ControlledSpawningStructure extends ProgressionStructure implements ConfigurableSpawns {
    protected static <S extends ControlledSpawningStructure> Products.P5<RecordCodecBuilder.Mu<S>, ControlledSpawningConfig, AdvancementLockConfig, HintConfig, DecorationConfig, StructureSettings> controlledSpawningCodec(RecordCodecBuilder.Instance<S> instance) {
        return instance.group(
                ControlledSpawningConfig.FLAT_CODEC.forGetter(ControlledSpawningStructure::getConfig)
        ).and(progressionCodec(instance));
    }

    protected final ControlledSpawningConfig controlledSpawningConfig;

    public ControlledSpawningStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(advancementLockConfig, hintConfig, decorationConfig, structureSettings);
        this.controlledSpawningConfig = controlledSpawningConfig;
    }

    @Override
    public ControlledSpawningConfig getConfig() {
        return this.controlledSpawningConfig;
    }
}
