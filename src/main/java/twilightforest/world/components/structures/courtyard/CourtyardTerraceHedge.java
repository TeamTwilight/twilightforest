package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;

public class CourtyardTerraceHedge extends CourtyardTerrace {
	private final Identifier hedgeBig = TwilightForestMod.prefix("courtyard/terrace_hedge_big");
	private StructureTemplate templateBig;

	public CourtyardTerraceHedge(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFNCHe.value(), ctx, nbt);

		this.templateBig = ctx.structureTemplateManager().getOrCreate(this.hedgeBig);
	}

	public CourtyardTerraceHedge(int i, int x, int y, int z, Rotation rotation, StructureTemplateManager structureManager) {
		super(TFStructurePieceTypes.TFNCHe.value(), i, x, y, z, rotation, structureManager, TwilightForestMod.prefix("courtyard/terrace_hedge"));

		this.templateBig = structureManager.getOrCreate(this.hedgeBig);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		super.postProcess(level, structureManager, chunkGen, random, chunkBounds, chunkPos, structureBottomCenter);
		this.templateBig.placeInWorld(level, this.templatePosition, structureBottomCenter, this.placeSettings.copy().clearProcessors().addProcessor(BlockIgnoreProcessor.AIR).addProcessor(new BlockRotProcessor(CourtyardMain.HEDGE_FLOOF)), random, Block.UPDATE_ALL);
	}
}
