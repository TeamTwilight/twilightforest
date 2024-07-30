package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFStructureProcessors;

public final class SoftReplaceProcessor extends StructureProcessor {
	public static final SoftReplaceProcessor INSTANCE = new SoftReplaceProcessor();
	public static final MapCodec<SoftReplaceProcessor> CODEC = MapCodec.unit(INSTANCE);

	private SoftReplaceProcessor() {
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos piecePos, StructureTemplate.StructureBlockInfo originalInfo, StructureTemplate.StructureBlockInfo modifiedInfo, StructurePlaceSettings placeSettings, @Nullable StructureTemplate template) {
		BlockState blockAt = level.getBlockState(modifiedInfo.pos());

		boolean isReplaceableAt = blockAt.canBeReplaced() || blockAt.is(BlockTagGenerator.WORLDGEN_REPLACEABLES);

		if (isReplaceableAt) {
			return modifiedInfo;
		}

		// Replace partial blocks such as slabs or fences, if the replacement is a solid block
		if (!this.isFullBlock(blockAt) && this.isFullBlock(modifiedInfo.state())) {
			return modifiedInfo;
		}

		return null;
	}

	private boolean isFullBlock(BlockState state) {
		// the BlockState#isSolid() is not reliable in checking for a full block
		Block block = state.getBlock();
		return !(block instanceof FenceBlock || block instanceof WallBlock || block instanceof SlabBlock || block instanceof StairBlock);
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.SOFT_REPLACE.value();
	}
}
