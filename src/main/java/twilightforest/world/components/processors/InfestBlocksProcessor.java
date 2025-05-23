package twilightforest.world.components.processors;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;
import twilightforest.util.features.FeaturePlacers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class InfestBlocksProcessor extends StructureProcessor {
	public static final InfestBlocksProcessor INSTANCE = new InfestBlocksProcessor();
	public static final MapCodec<InfestBlocksProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

	// TODO Convert to DataMap
	private static final Supplier<Map<Block, BlockState>> CONVERSIONS = Suppliers.memoize(() -> Util.make(new HashMap<>(), map -> {
		map.put(Blocks.STONE, Blocks.INFESTED_STONE.defaultBlockState());
		map.put(Blocks.COBBLESTONE, Blocks.INFESTED_COBBLESTONE.defaultBlockState());
		map.put(Blocks.STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS.defaultBlockState());
		map.put(Blocks.MOSSY_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS.defaultBlockState());
		map.put(Blocks.CRACKED_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS.defaultBlockState());
		map.put(Blocks.CHISELED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS.defaultBlockState());
	}));

	private InfestBlocksProcessor() {
	}

	@Override
	public StructureTemplate.StructureBlockInfo process(LevelReader worldReaderIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo originalBlock, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
		RandomSource random = settings.getRandom(modifiedBlockInfo.pos().below(-10));

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 2);

		var replacement = CONVERSIONS.get().get(modifiedBlockInfo.state().getBlock());

		if (replacement == null || random.nextFloat() > 1/12f)
			return modifiedBlockInfo;

		return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), replacement, null);
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.INFEST_BLOCKS.get();
	}
}
