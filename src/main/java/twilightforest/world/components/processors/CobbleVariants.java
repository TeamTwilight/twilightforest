package twilightforest.world.components.processors;

import com.mojang.serialization.Codec;
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
import twilightforest.util.FeaturePlacers;

public final class CobbleVariants extends StructureProcessor {
	public static final CobbleVariants INSTANCE = new CobbleVariants();
	public static final Codec<CobbleVariants> CODEC = Codec.unit(() -> INSTANCE);

	private CobbleVariants() {
    }

	@Override
	public StructureTemplate.StructureBlockInfo process(LevelReader worldReaderIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo p_215194_3_, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
		RandomSource random = settings.getRandom(modifiedBlockInfo.pos());

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 2);

		BlockState state = modifiedBlockInfo.state();
		Block block = state.getBlock();

		if (block == Blocks.COBBLESTONE && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), null);

		if (block == Blocks.COBBLESTONE_STAIRS && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_STAIRS), null);

		if (block == Blocks.COBBLESTONE_SLAB && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_SLAB), null);

		if (block == Blocks.COBBLESTONE_WALL && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_WALL), null);

		return modifiedBlockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.COBBLE_VARIANTS.get();
	}
}
