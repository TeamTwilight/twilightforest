package twilightforest.world.components.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import twilightforest.init.TFFeatureModifiers;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.world.components.structures.UtilityPiece;
import twilightforest.world.components.structures.util.DecorationClearance;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class AvoidLandmarkModifier extends PlacementModifier {

	public static final MapCodec<AvoidLandmarkModifier> CODEC = RecordCodecBuilder.<AvoidLandmarkModifier>mapCodec(instance -> instance.group(
		Codec.BOOL.fieldOf("occupies_surface").forGetter(o -> o.occupiesSurface),
		Codec.BOOL.fieldOf("occupies_underground").forGetter(o -> o.occupiesUnderground),
		Codec.BOOL.fieldOf("occupies_vegetation").forGetter(o -> o.occupiesVegetation),
		Codec.INT.fieldOf("additional_clearance").forGetter(o -> o.additionalClearance),
		RegistryCodecs.homogeneousList(Registries.STRUCTURE).fieldOf("structures_allowed").forGetter(m -> m.structuresAllowed)
	).apply(instance, AvoidLandmarkModifier::new)).validate(AvoidLandmarkModifier::validate);

	private final boolean occupiesSurface;
	private final boolean occupiesUnderground;
	private final boolean occupiesVegetation;
	private final int additionalClearance;
	private final HolderSet<Structure> structuresAllowed;

	public AvoidLandmarkModifier(boolean occupiesSurface, boolean occupiesUnderground, int additionalClearance, HolderSet<Structure> structuresAllowed) {
		this(occupiesSurface, occupiesUnderground, false, additionalClearance, structuresAllowed);
	}

	public AvoidLandmarkModifier(boolean occupiesSurface, boolean occupiesUnderground, boolean occupiesVegetation, int additionalClearance, HolderSet<Structure> structuresAllowed) {
		this.occupiesSurface = occupiesSurface;
		this.occupiesUnderground = occupiesUnderground;
		this.occupiesVegetation = occupiesVegetation;
		this.additionalClearance = additionalClearance;
		this.structuresAllowed = structuresAllowed;
	}

	public static AvoidLandmarkModifier checkSurface() {
		return checkSurface(HolderSet.empty());
	}

	public static AvoidLandmarkModifier checkSurface(HolderSet<Structure> structuresAllowed) {
		return new AvoidLandmarkModifier(true, false, 0, structuresAllowed);
	}

	public static AvoidLandmarkModifier checkUnderground() {
		return new AvoidLandmarkModifier(false, true, 0, HolderSet.empty());
	}

	public static AvoidLandmarkModifier checkBoth() {
		return new AvoidLandmarkModifier(true, true, 0, HolderSet.empty());
	}

	public static AvoidLandmarkModifier checkVegetation() {
		return new AvoidLandmarkModifier(false, false, true, 0, HolderSet.empty());
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext worldDecoratingHelper, RandomSource random, BlockPos blockPos) {
		ChunkAccess chunk = worldDecoratingHelper.getLevel().getChunk(blockPos);
		Set<Map.Entry<Structure, LongSet>> structuresOverlappingChunk = chunk.getAllReferences().entrySet();

		for (Map.Entry<Structure, LongSet> startsForStructure : structuresOverlappingChunk)
			if (this.structureTypeBlocksFeaturePlacement(worldDecoratingHelper, blockPos, startsForStructure.getKey(), startsForStructure.getValue()))
				return Stream.empty();

		return Stream.of(blockPos);
	}

	private boolean structureTypeBlocksFeaturePlacement(PlacementContext worldDecoratingHelper, BlockPos blockPos, Structure structure, LongSet coordsForStarts) {
		if (this.allowedInsideStructure(structure)
			|| !(structure instanceof DecorationClearance decorationClearance)
			|| this.clearFromStructureZone(decorationClearance))
			return false;

		for (long packedChunkCoord : coordsForStarts) {
			int startChunkX = ChunkPos.getX(packedChunkCoord);
			int startChunkZ = ChunkPos.getZ(packedChunkCoord);

			ChunkAccess startChunk = worldDecoratingHelper.getLevel().getChunk(startChunkX, startChunkZ);
			if (startChunk == null) {
				continue;
			}

			StructureStart startForStructure = startChunk.getStartForStructure(structure);

			if (startForStructure == null || !startForStructure.isValid())
				continue;

			BlockPos diff = blockPos.subtract(startForStructure.getBoundingBox().getCenter());
			if (this.placementIsBlocked(blockPos, startForStructure, diff, decorationClearance.chunkClearanceRadius())) {
				return true;
			}
		}

		return false;
	}

	private boolean allowedInsideStructure(Structure overlappingStructure) {
		boolean configAllowedOverlap = false;

		for (Holder<Structure> allowedStructure : this.structuresAllowed) {
			if (allowedStructure.isBound() && overlappingStructure.equals(allowedStructure.value())) {
				configAllowedOverlap = true;
			}
		}

		return configAllowedOverlap;
	}

	private boolean clearFromStructureZone(DecorationClearance decorationClearance) {
		boolean surfaceClear = !this.occupiesSurface || decorationClearance.isSurfaceDecorationsAllowed();
		boolean undergroundClear = !this.occupiesUnderground || decorationClearance.isUndergroundDecoAllowed();
		boolean vegetationClear = !this.occupiesVegetation || decorationClearance.isGrassDecoAllowed();

		return surfaceClear && undergroundClear && vegetationClear;
	}

	private boolean placementIsBlocked(BlockPos blockPos, StructureStart startForStructure, BlockPos featureDistanceXZ, float chunkClearanceRadius) {
		if (chunkClearanceRadius <= 0) {
			for (StructurePiece piece : startForStructure.getPieces()) {
				if (piece instanceof UtilityPiece utilityPiece && utilityPiece.allowFeatures) {
					continue;
				}

				if (BoundingBoxUtils.greatestAxalDistance(piece.getBoundingBox(), blockPos) <= this.additionalClearance) {
					return true;
				}
			}

			return false;
		}

		float size = chunkClearanceRadius * 16f + this.additionalClearance;
		return Math.abs(featureDistanceXZ.getX()) < size && Math.abs(featureDistanceXZ.getZ()) < size;
	}

	@Override
	public PlacementModifierType<?> type() {
		return TFFeatureModifiers.NO_STRUCTURE.get();
	}

	private static DataResult<AvoidLandmarkModifier> validate(AvoidLandmarkModifier config) {
		return config.occupiesSurface || config.occupiesUnderground || config.occupiesVegetation ? DataResult.success(config) : DataResult.error(() -> "Feature Decorator cannot have no occupancy");
	}
}
