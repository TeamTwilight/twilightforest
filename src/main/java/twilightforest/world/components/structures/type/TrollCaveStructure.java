package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.init.TFStructureTypes;
import twilightforest.init.custom.StructureSpeleothemConfigs;
import twilightforest.world.components.structures.StructureSpeleothemConfig;
import twilightforest.world.components.structures.trollcave.TrollCaveMainComponent;
import twilightforest.world.components.structures.util.ConfigurableSpawns;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.ProgressionStructure;

import java.util.Optional;

public class TrollCaveStructure extends ProgressionStructure implements ConfigurableSpawns {
	public static final MapCodec<TrollCaveStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
		.group(
			ControlledSpawns.ControlledSpawningConfig.CODEC.fieldOf(ControlledSpawns.CODEC_NAME).forGetter(ConfigurableSpawns::getConfig),
			StructureSpeleothemConfigs.CODEC.fieldOf("speleothem_config").forGetter(s -> s.speleothemConfig)
		)
		.and(progressionCodec(instance))
		.apply(instance, TrollCaveStructure::new)
	);

	private final ControlledSpawningConfig controlledSpawningConfig;
	private final Holder.Reference<StructureSpeleothemConfig> speleothemConfig;

	public TrollCaveStructure(ControlledSpawningConfig controlledSpawningConfig, Holder<StructureSpeleothemConfig> speleothemConfig, AdvancementLockConfig advancementLockConfig, Optional<HintConfig> hintConfig, Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, structureSettings);

		this.controlledSpawningConfig = controlledSpawningConfig;
		this.speleothemConfig = (Holder.Reference<StructureSpeleothemConfig>) speleothemConfig;
	}

	@Override
	protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return new TrollCaveMainComponent(TFStructurePieceTypes.TFTCMai.get(), 0, x, y + 11, z, this.speleothemConfig);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.TROLL_CAVE.get();
	}

	@Override
	public ControlledSpawningConfig getConfig() {
		return this.controlledSpawningConfig;
	}

}
