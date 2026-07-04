package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.util.ProgressionStructure;

import java.util.Map;
import java.util.Optional;

public class ProgressionWrappedStructure extends ProgressionStructure {

	public static final MapCodec<ProgressionWrappedStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(Structure.DIRECT_CODEC.fieldOf("wrapped").forGetter(o -> o.wrappedStructure))
			.and(progressionCodecNoSettings(instance))
			.apply(instance, ProgressionWrappedStructure::new)
	);

	private final Structure wrappedStructure;

	public ProgressionWrappedStructure(Structure wrappedStructure, AdvancementLockConfig advancementLockConfig, Optional<HintConfig> hintConfig, Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon) {
		super(advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, new StructureSettings(HolderSet.empty()));
		this.wrappedStructure = wrappedStructure;
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		return this.wrappedStructure.findGenerationPoint(context);
	}

	//not used since we override findGenerationPoint
	@Override
	protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return null;
	}

	@Override
	public HolderSet<Biome> biomes() {
		return this.wrappedStructure.biomes();
	}

	@Override
	public Map<MobCategory, StructureSpawnOverride> spawnOverrides() {
		return this.wrappedStructure.spawnOverrides();
	}

	@Override
	public GenerationStep.Decoration step() {
		return this.wrappedStructure.step();
	}

	@Override
	public TerrainAdjustment terrainAdaptation() {
		return this.wrappedStructure.terrainAdaptation();
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.PROGRESSION_WRAPPED.get();
	}
}
