package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.trollcave.CloudCastleComponent;
import twilightforest.world.components.structures.util.ConfigurableSpawns;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.ProgressionStructure;

import java.util.Optional;

public class GiantHouseStructure extends ProgressionStructure implements ConfigurableSpawns {
	public static final MapCodec<GiantHouseStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
		progressionCodec(instance)
			.and(ControlledSpawningConfig.CODEC.fieldOf(ControlledSpawns.CODEC_NAME).forGetter(ConfigurableSpawns::getConfig))
			.apply(instance, GiantHouseStructure::new)
	);

	private final ControlledSpawningConfig controlledSpawningConfig;

	public GiantHouseStructure(AdvancementLockConfig advancementLockConfig, Optional<HintConfig> hintConfig, Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings, ControlledSpawningConfig controlledSpawningConfig) {
		super(advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, structureSettings);

		this.controlledSpawningConfig = controlledSpawningConfig;
	}

	@Override
	protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		// add cloud castle
		return new CloudCastleComponent(1, x, y + 168, z);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.GIANT_HOUSE.get();
	}

	@Override
	public ControlledSpawningConfig getConfig() {
		return this.controlledSpawningConfig;
	}

}
