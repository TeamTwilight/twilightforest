package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDataMaps;
import twilightforest.init.TFStructureProcessors;

public final class InfestBlocksProcessor extends StructureProcessor {
	public static final InfestBlocksProcessor INSTANCE = new InfestBlocksProcessor();
	public static final MapCodec<InfestBlocksProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

	private InfestBlocksProcessor() {
	}

	@Override
	public StructureTemplate.StructureBlockInfo process(LevelReader worldReaderIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo originalBlock, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
		RandomSource random = settings.getRandom(modifiedBlockInfo.pos().below(-10));

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 2);

		BlockState replacement = modifiedBlockInfo.state()
			.typeHolder()
			.getData(TFDataMaps.INFESTED_CONVERSION);

		if (replacement == null || random.nextFloat() > 1/12f)
			return modifiedBlockInfo;

		return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), replacement, null);
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.INFEST_BLOCKS.get();
	}
}
