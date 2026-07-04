package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructureTypes;
import twilightforest.tags.TFBiomeTags;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.util.DecorationClearance;

import java.util.Map;
import java.util.Optional;

public class CampStructure extends Structure implements DecorationClearance {

	public static final MapCodec<CampStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Structure.settingsCodec(instance)
	).apply(instance, CampStructure::new));

	protected CampStructure(StructureSettings settings) {
		super(settings);
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();
		WorldgenRandom random = context.random();

		int blockXCenter = Mth.lerpDiscrete(random.nextFloat(), chunkPos.getMinBlockX(), chunkPos.getMaxBlockX());
		int blockZCenter = Mth.lerpDiscrete(random.nextFloat(), chunkPos.getMinBlockZ(), chunkPos.getMaxBlockZ());
		int topFreeY = WorldUtil.adjustForTerrain(context, blockXCenter, blockZCenter, 12, 3);
		BlockPos freePosition = new BlockPos(blockXCenter, topFreeY, blockZCenter);

		Direction direction = Rotation.getRandom(random).rotate(Direction.SOUTH);
		FrontAndTop oriented = FrontAndTop.fromFrontAndTop(Direction.UP, direction);

		return Optional.of(new GenerationStub(freePosition, structurePiecesBuilder -> {
			Identifier templatePool = TwilightForestMod.prefix("camp/structure_start");
			// TODO Instead use StructureTemplateDefinitions.initializeStubFromPool
			TwilightJigsawPiece twilightJigsawPiece = TwilightJigsawPiece.initializeTemplateFromPool(templatePool, freePosition.mutable(), oriented, templatePool.toString(), random, 0, context.structureTemplateManager());

			if (twilightJigsawPiece == null) return;

			structurePiecesBuilder.addPiece(twilightJigsawPiece);

			twilightJigsawPiece.addJigsaws(twilightJigsawPiece, structurePiecesBuilder, context);
		}));
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.CAMP.value();
	}

	public static CampStructure buildStructureConfig(BootstrapContext<Structure> context) {
		return new CampStructure(new StructureSettings(
			context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_CAMP_BIOMES),
			Map.of(),
			GenerationStep.Decoration.SURFACE_STRUCTURES,
			TerrainAdjustment.BEARD_BOX
		));
	}

	@Override
	public float chunkClearanceRadius() {
		return 1;
	}

	@Override
	public boolean isSurfaceDecorationsAllowed() {
		return false;
	}

	@Override
	public boolean isUndergroundDecoAllowed() {
		return true;
	}

	@Override
	public boolean isGrassDecoAllowed() {
		return true;
	}

	@Override
	public boolean shouldAdjustToTerrain() {
		return false;
	}
}
