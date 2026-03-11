package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.world.components.processors.NagastoneVariants;
import twilightforest.world.components.structures.TFStructureComponentTemplate;

public abstract class NagaCourtyardHedgeAbstractComponent extends TFStructureComponentTemplate {

	private final ResourceLocation HEDGE;
	private final ResourceLocation HEDGE_BIG;

	private StructureTemplate templateBig;

	public NagaCourtyardHedgeAbstractComponent(StructurePieceSerializationContext ctx, StructurePieceType piece, CompoundTag nbt, ResourceLocation hedge, ResourceLocation hedgeBig) {
		super(ctx, piece, nbt);
		this.HEDGE = hedge;
		this.HEDGE_BIG = hedgeBig;
	}

	@SuppressWarnings("WeakerAccess")
	public NagaCourtyardHedgeAbstractComponent(StructureTemplateManager manager, StructurePieceType type, int i, int x, int y, int z, Rotation rotation, ResourceLocation hedge, ResourceLocation hedgeBig) {
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
		placeSettings.setBoundingBox(structureBoundingBox).clearProcessors();
		if (TEMPLATE == null) // FIXME: this should never be null in the first place
			LAZY_TEMPLATE_LOADER.run();
		TEMPLATE.placeInWorld(world, rotatedPosition, rotatedPosition, placeSettings.clearProcessors().addProcessor(NagastoneVariants.INSTANCE), random, 18);
		templateBig.placeInWorld(world, rotatedPosition, rotatedPosition, placeSettings.addProcessor(BlockIgnoreProcessor.AIR).addProcessor(new BlockRotProcessor(CourtyardMain.HEDGE_FLOOF)), random, 18);
	}
}
