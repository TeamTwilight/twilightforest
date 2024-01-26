package twilightforest.world.components.processors;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;
import twilightforest.util.FeaturePlacers;

public final class StoneBricksVariants extends StructureProcessor {
	public static final StoneBricksVariants INSTANCE = new StoneBricksVariants();
	public static final Codec<StoneBricksVariants> CODEC = Codec.unit(() -> INSTANCE);

	private StoneBricksVariants() {
    }

	@Override
	public StructureTemplate.StructureBlockInfo process(LevelReader worldReaderIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo originalBlock, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
		RandomSource random = settings.getRandom(modifiedBlockInfo.pos());

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 3);

		BlockState state = modifiedBlockInfo.state();

		if (state.is(Blocks.STONE_BRICKS) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), random.nextBoolean() ? Blocks.MOSSY_STONE_BRICKS.defaultBlockState() : Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), null);

		if (state.is(Blocks.STONE_BRICK_STAIRS) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_STONE_BRICK_STAIRS), null);

		if (state.is(Blocks.STONE_BRICK_SLAB) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_STONE_BRICK_SLAB), null);

		if (state.is(Blocks.STONE_BRICK_WALL) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_STONE_BRICK_WALL), null);

		return modifiedBlockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.STONE_BRICK_VARIANTS.get();
	}
}
