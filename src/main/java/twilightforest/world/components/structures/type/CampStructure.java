package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFStructureTypes;
import twilightforest.util.WorldUtil;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.world.components.structures.TwilightJigsawPiece;

import java.util.Optional;

public class CampStructure extends Structure {

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
			TwilightJigsawPiece twilightJigsawPiece = TwilightJigsawPiece.initializeTemplateFromPool(TwilightForestMod.prefix("camp/structure_start"), freePosition, oriented, "twilightforest:camp/structure_start", random, 0, context.structureTemplateManager());

			structurePiecesBuilder.addPiece(twilightJigsawPiece);

			twilightJigsawPiece.addChildren(twilightJigsawPiece, structurePiecesBuilder, context.random());
		}));
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.CAMP.value();
	}

	public static CampStructure buildStructureConfig(BootstrapContext<Structure> context) {
		return new CampStructure(new StructureSettings(
			context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_CAMP_BIOMES)
		));
	}

}
