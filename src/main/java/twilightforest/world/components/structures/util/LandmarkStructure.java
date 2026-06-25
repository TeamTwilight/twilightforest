package twilightforest.world.components.structures.util;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.structures.TFStructureComponentTemplate;
import twilightforest.TwilightForestMod;

import java.util.Comparator;
import java.util.Optional;

// Landmark structure without progression lock; Hollow Hills/Hedge Maze/Naga Courtyard/Quest Grove
public abstract class LandmarkStructure extends Structure implements DecorationClearance {
	private static final Logger LOGGER = LoggerFactory.getLogger(LandmarkStructure.class);

	protected static <S extends LandmarkStructure> Products.P3<RecordCodecBuilder.Mu<S>, Optional<DecorationConfig>, Boolean, Optional<Holder<MapDecorationType>>> landmarkCodecNoSettings(RecordCodecBuilder.Instance<S> instance) {
		return instance.group(
			DecorationConfig.CODEC.optionalFieldOf(DecorationClearance.CODEC_NAME).forGetter(s -> s.decorationConfig),
			Codec.BOOL.optionalFieldOf("center_in_chunk", true).forGetter(s -> s.centerInChunk),
			BuiltInRegistries.MAP_DECORATION_TYPE.holderByNameCodec().optionalFieldOf("structure_icon").forGetter(s -> s.structureIcon)
		);
	}

	protected static <S extends LandmarkStructure> Products.P4<RecordCodecBuilder.Mu<S>, Optional<DecorationConfig>, Boolean, Optional<Holder<MapDecorationType>>, StructureSettings> landmarkCodec(RecordCodecBuilder.Instance<S> instance) {
		return instance.group(
			DecorationConfig.CODEC.optionalFieldOf(DecorationClearance.CODEC_NAME).forGetter(s -> s.decorationConfig),
			Codec.BOOL.optionalFieldOf("center_in_chunk", true).forGetter(s -> s.centerInChunk),
			BuiltInRegistries.MAP_DECORATION_TYPE.holderByNameCodec().optionalFieldOf("structure_icon").forGetter(s -> s.structureIcon),
			Structure.settingsCodec(instance)
		);
	}

	protected final Optional<DecorationConfig> decorationConfig;
	protected final boolean centerInChunk;
	protected Optional<Holder<MapDecorationType>> structureIcon;

	public LandmarkStructure(Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(structureSettings);
		this.decorationConfig = decorationConfig;
		this.centerInChunk = centerInChunk;
		this.structureIcon = structureIcon;
	}

	protected Structure.GenerationStub getStructurePieceGenerationStubFunction(StructurePiece startingPiece, GenerationContext context, int x, int y, int z) {
		return new GenerationStub(new BlockPos(x, y, z), structurePiecesBuilder -> {
			this.generateFromStartingPiece(startingPiece, context, structurePiecesBuilder);

			structurePiecesBuilder.pieces.sort(Comparator.comparing(piece -> piece instanceof SortablePiece sortable ? sortable.getSortKey() : 0));

			structurePiecesBuilder.pieces.stream()
				.filter(TFStructureComponentTemplate.class::isInstance)
				.map(TFStructureComponentTemplate.class::cast)
				.forEach(t -> t.LAZY_TEMPLATE_LOADER.run());
		});
	}

	protected void generateFromStartingPiece(StructurePiece startingPiece, GenerationContext context, StructurePiecesBuilder structurePiecesBuilder) {
		structurePiecesBuilder.addPiece(startingPiece);
		startingPiece.addChildren(startingPiece, structurePiecesBuilder, context.random());
	}

	// TODO Refactor findGenerationPoint to merge usecases for getFirstPiece and getStructurePieceGenerationStubFunction
	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();
		int x = (chunkPos.x() << 4) + (this.centerInChunk ? 7 : 0);
		int z = (chunkPos.z() << 4) + (this.centerInChunk ? 7 : 0);
		int y = this.adjustForTerrain(context, x, z);

		TwilightForestMod.LOGGER.error("TF-LandmarkStructure: findGenerationPoint for {} at ({},{}), adjustForTerrain returned y={}, seaLevel={}", this.getClass().getSimpleName(), x, z, y, context.chunkGenerator().getSeaLevel());

		return Optional
			.ofNullable(this.getFirstPiece(context, RandomSource.create(context.seed() + chunkPos.x() * 25117L + chunkPos.z() * 151121L), chunkPos, x, y, z))
			.map(piece -> this.getStructurePieceGenerationStubFunction(piece, context, x, y, z));
	}

	public final Optional<Holder<MapDecorationType>> getMapIcon() {
		return this.structureIcon;
	}

	@Nullable
	protected abstract StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z);

	@Override
	public boolean isSurfaceDecorationsAllowed() {
		return this.decorationConfig.map(DecorationConfig::surfaceDecorations).orElse(true);
	}

	@Override
	public boolean isUndergroundDecoAllowed() {
		return this.decorationConfig.map(DecorationConfig::undergroundDecorations).orElse(true);
	}

	@Override
	public boolean isGrassDecoAllowed() {
		return this.decorationConfig.map(DecorationConfig::vegetation).orElse(true);
	}

	@Override
	public boolean shouldAdjustToTerrain() {
		return this.decorationConfig.map(DecorationConfig::adjustElevation).orElse(false);
	}

	@Override
	public float chunkClearanceRadius() {
		return this.decorationConfig.map(DecorationConfig::chunkClearanceRadius).orElse(1.0F);
	}

	@Override
	public Optional<GenerationStub> findValidGenerationPoint(GenerationContext context) {
		if (!(context.biomeSource() instanceof TFBiomeProvider twilightBiomeProvider)) {
			LOGGER.debug("[TF-STRUCTURE] BiomeSource is not TFBiomeProvider, using super.findValidGenerationPoint");
			return super.findValidGenerationPoint(context);
		}

		ChunkPos chunkPos = context.chunkPos();
		// set biomeX and biomeZ to center of the biome-grid tile.
		// Otherwise some tightly-fitting biomes like Highlands vs Thornlands may fail the Troll-Clouds structure generation
		int biomeX = (Math.round(chunkPos.x() / 16F) << 6) + 2;
		int biomeZ = (Math.round(chunkPos.z() / 16F) << 6) + 2;

		Holder<Biome> biomeAt = twilightBiomeProvider.getMainBiome(biomeX, biomeZ);
		boolean validBiome = context.validBiome().test(biomeAt);

		biomeAt.unwrapKey().ifPresent(biomeKey -> {
			LOGGER.debug("[TF-STRUCTURE] Chunk [{}, {}], biome at center: {}, valid: {}", chunkPos.x(), chunkPos.z(), biomeKey.identifier(), validBiome);
		});

		if (!validBiome) {
			LOGGER.debug("[TF-STRUCTURE] Biome check failed for chunk [{}, {}], skipping structure generation", chunkPos.x(), chunkPos.z());
		}

		return validBiome ? this.findGenerationPoint(context) : Optional.empty();
	}
}
