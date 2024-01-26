package twilightforest.world.components.structures;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.FeaturePlacers;
import twilightforest.world.components.processors.StoneBricksVariants;
import twilightforest.world.components.processors.TargetedRotProcessor;


public class QuestGrove extends TwilightTemplateStructurePiece {
	private static final TargetedRotProcessor MOSSY_BRICK_DECAY = new TargetedRotProcessor(ImmutableSet.of(Blocks.MOSSY_STONE_BRICKS.defaultBlockState()), 0.5f);

	public QuestGrove(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.TFQuestGrove.get(), compoundTag, ctx, readSettings(compoundTag).addProcessor(StoneBricksVariants.INSTANCE));
	}

	public QuestGrove(StructureTemplateManager structureManager, BlockPos templatePosition) {
		super(TFStructurePieceTypes.TFQuestGrove.get(), 0, structureManager, TwilightForestMod.prefix("quest_grove"), makeSettings(Rotation.NONE).addProcessor(MOSSY_BRICK_DECAY).addProcessor(StoneBricksVariants.INSTANCE), templatePosition);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureFeatureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
		this.placePieceAdjusted(level, structureFeatureManager, chunkGenerator, random, boundingBox, chunkPos, pos, -2);
	}

	@Override
	protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor levelAccessor, RandomSource random, BoundingBox boundingBox) {
		if (!boundingBox.isInside(pos)) return;

		if ("quest_ram".equals(name)) {
			FeaturePlacers.placeEntity(TFEntities.QUEST_RAM.get(), pos, levelAccessor);
		} else if ("dispenser".equals(name)) {
			TFLootTables.QUEST_GROVE.generateLootContainer(levelAccessor, pos, Blocks.DROPPER.defaultBlockState().setValue(DispenserBlock.FACING, this.placeSettings.getRotation().rotate(Direction.NORTH)), 16 | 4 | 2, random.nextLong());
		}
	}
}
