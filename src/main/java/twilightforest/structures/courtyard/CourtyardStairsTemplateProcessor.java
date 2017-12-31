package twilightforest.structures.courtyard;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import twilightforest.block.BlockTFNagastoneStairs;
import twilightforest.block.TFBlocks;
import twilightforest.structures.RandomizedTemplateProcessor;

import javax.annotation.Nullable;

public class CourtyardStairsTemplateProcessor extends RandomizedTemplateProcessor {
    public CourtyardStairsTemplateProcessor(BlockPos pos, PlacementSettings settings) {
        super(pos, settings);
    }

    @Nullable
    @Override
    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfo) {
        if (shouldPlaceBlock()) {
            IBlockState state = blockInfo.blockState;
            Block block = state.getBlock();

            if (block == TFBlocks.nagastone_stairs)
                return random.nextBoolean() ? blockInfo : new Template.BlockInfo(pos, translateState(state, randomBlock(TFBlocks.nagastone_stairs_mossy, TFBlocks.nagastone_stairs_weathered), BlockTFNagastoneStairs.DIRECTION, BlockTFNagastoneStairs.FACING, BlockTFNagastoneStairs.HALF, BlockTFNagastoneStairs.SHAPE), null);

            return blockInfo;
        }

        return null;
    }
}