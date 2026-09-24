package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.icetower.IceTowerMainComponent;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Optional;

public class AuroraPalaceStructure extends ControlledSpawningStructure {
	public static final MapCodec<AuroraPalaceStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
		controlledSpawningCodec(instance).apply(instance, AuroraPalaceStructure::new)
	);

	public AuroraPalaceStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, Optional<HintConfig> hintConfig, Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, structureSettings);
	}

	@Override
	protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return new IceTowerMainComponent(random, 0, x, y, z);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.AURORA_PALACE.get();
	}

}
