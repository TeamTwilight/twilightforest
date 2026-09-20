package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.world.components.processors.NagastoneVariants;
import twilightforest.world.components.structures.TFStructureComponentTemplate;

public abstract class NagaCourtyardHedgeAbstractComponent extends TFStructureComponentTemplate {

	private final Identifier HEDGE;
	private final Identifier HEDGE_BIG;

	private StructureTemplate templateBig;

	public NagaCourtyardHedgeAbstractComponent(StructurePieceSerializationContext ctx, StructurePieceType piece, CompoundTag nbt, Identifier hedge, Identifier hedgeBig) {
		super(ctx, piece, nbt);
		this.HEDGE = hedge;
		this.HEDGE_BIG = hedgeBig;
		setup(ctx.structureTemplateManager());
	}

	@SuppressWarnings("WeakerAccess")
	public NagaCourtyardHedgeAbstractComponent(StructureTemplateManager manager, StructurePieceType type, int i, int x, int y, int z, Rotation rotation, Identifier hedge, Identifier hedgeBig) {
		super(manager, type, i, x, y, z, rotation);
		this.HEDGE = hedge;
		this.HEDGE_BIG = hedgeBig;
	}

	@Override
	protected void loadTemplates(StructureTemplateManager templateManager) {
		TEMPLATE = templateManager.getOrCreate(HEDGE);
		templateBig = templateManager.getOrCreate(HEDGE_BIG);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox structureBoundingBox, ChunkPos chunkPosIn, BlockPos blockPos) {
		StructurePlaceSettings hedgeSettings = placeSettings.copy().setBoundingBox(structureBoundingBox).clearProcessors().addProcessor(NagastoneVariants.INSTANCE);
		StructurePlaceSettings hedgeBigSettings = hedgeSettings.copy().addProcessor(BlockIgnoreProcessor.AIR).addProcessor(new BlockRotProcessor(CourtyardMain.HEDGE_FLOOF));
		TEMPLATE.placeInWorld(world, rotatedPosition, rotatedPosition, hedgeSettings, random, 18);
		templateBig.placeInWorld(world, rotatedPosition, rotatedPosition, hedgeBigSettings, random, 18);
	}
}
