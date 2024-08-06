package twilightforest.world.components.structures.util;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

import java.util.Optional;

public abstract class ControlledSpawningStructure extends ProgressionStructure implements ConfigurableSpawns {
	protected static <S extends ControlledSpawningStructure> Products.P7<RecordCodecBuilder.Mu<S>, ControlledSpawningConfig, AdvancementLockConfig, HintConfig, DecorationConfig, Boolean, Optional<Holder<MapDecorationType>>, StructureSettings> controlledSpawningCodec(RecordCodecBuilder.Instance<S> instance) {
		return instance.group(
			ControlledSpawningConfig.FLAT_CODEC.forGetter(ControlledSpawningStructure::getConfig)
		).and(progressionCodec(instance));
	}

	protected final ControlledSpawningConfig controlledSpawningConfig;

	public ControlledSpawningStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, structureSettings);
		this.controlledSpawningConfig = controlledSpawningConfig;
	}

	@Override
	public ControlledSpawningConfig getConfig() {
		return this.controlledSpawningConfig;
	}
}
