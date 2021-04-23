package twilightforest.structures.courtyard;

import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import twilightforest.block.BlockTFNagastoneStairs;
import twilightforest.block.TFBlocks;
import twilightforest.structures.RandomizedTemplateProcessor;
import twilightforest.structures.TFStructureProcessors;

import javax.annotation.Nullable;
import java.util.Random;

public class CourtyardWallTemplateProcessor extends RandomizedTemplateProcessor {

	public static final Codec<CourtyardWallTemplateProcessor> codecWallProcessor = Codec.FLOAT.fieldOf("integrity").orElse(1.0F).xmap(CourtyardWallTemplateProcessor::new, (obj) -> obj.integrity).codec();

	public CourtyardWallTemplateProcessor(float integrity) {
        super(integrity);
    }

	@Override
	protected IStructureProcessorType<?> getType() {
		return TFStructureProcessors.COURTYARD_WALL;
	}

	@Nullable
	@Override
	public Template.BlockInfo process(IWorldReader worldIn, BlockPos pos, BlockPos piecepos, Template.BlockInfo oldInfo, Template.BlockInfo newInfo, PlacementSettings placementSettingsIn, @Nullable Template template) {
		Random random = placementSettingsIn.getRandom(newInfo.pos);

		if (shouldPlaceBlock(random)) {
			BlockState state = newInfo.state;
			Block block = state.getBlock();

			if (state == Blocks.STONE_BRICKS.getDefaultState())
				return random.nextBoolean() ? newInfo : new Template.BlockInfo(newInfo.pos, random.nextInt(2) == 0 ? Blocks.CRACKED_STONE_BRICKS.getDefaultState() : Blocks.MOSSY_STONE_BRICKS.getDefaultState(), null);

			if (state == Blocks.SMOOTH_STONE_SLAB.getDefaultState())
				return random.nextBoolean() ? newInfo : new Template.BlockInfo(newInfo.pos, Blocks.COBBLESTONE_SLAB.getDefaultState(), null);

			if (block == TFBlocks.etched_nagastone.get())
				return random.nextBoolean() ? newInfo : new Template.BlockInfo(newInfo.pos, translateState(state, randomBlock(random, TFBlocks.etched_nagastone_mossy.get(), TFBlocks.etched_nagastone_weathered.get()), DirectionalBlock.FACING), null);

			if (block == TFBlocks.nagastone_pillar.get())
				return random.nextBoolean() ? newInfo : new Template.BlockInfo(newInfo.pos, translateState(state, randomBlock(random, TFBlocks.nagastone_pillar_mossy.get(), TFBlocks.nagastone_pillar_weathered.get()), RotatedPillarBlock.AXIS), null);

			if (block == TFBlocks.nagastone_stairs_left.get())
				return random.nextBoolean() ? newInfo : new Template.BlockInfo(newInfo.pos, translateState(state, randomBlock(random, TFBlocks.nagastone_stairs_mossy_left.get(), TFBlocks.nagastone_stairs_weathered_left.get()), StairsBlock.FACING, StairsBlock.HALF, StairsBlock.SHAPE), null);

			if (block == TFBlocks.nagastone_stairs_right.get())
				return random.nextBoolean() ? newInfo : new Template.BlockInfo(newInfo.pos, translateState(state, randomBlock(random, TFBlocks.nagastone_stairs_mossy_right.get(), TFBlocks.nagastone_stairs_weathered_right.get()), StairsBlock.FACING, StairsBlock.HALF, StairsBlock.SHAPE), null);

			return newInfo;
		}

		return null;
	}
}
