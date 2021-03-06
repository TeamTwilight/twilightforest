package twilightforest.structures.courtyard;

import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import twilightforest.block.TFBlocks;
import twilightforest.structures.RandomizedTemplateProcessor;
import twilightforest.structures.TFStructureProcessors;

import javax.annotation.Nullable;
import java.util.Random;

public class CourtyardTerraceTemplateProcessor extends RandomizedTemplateProcessor {

	public static final Codec<CourtyardTerraceTemplateProcessor> codecTerraceProcessor = Codec.FLOAT.fieldOf("integrity").orElse(1.0F).xmap(CourtyardTerraceTemplateProcessor::new, (obj) -> obj.integrity).codec();

    public CourtyardTerraceTemplateProcessor(float integrity) {
        super(integrity);
    }

	@Override
	protected IStructureProcessorType<?> getType() {
		return TFStructureProcessors.COURTYARD_TERRACE;
	}

    @Nullable
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos pos, BlockPos piecepos, Template.BlockInfo oldinfo, Template.BlockInfo newinfo, PlacementSettings settings, @Nullable Template template) {
        Random random = settings.getRandom(newinfo.pos);

        if (!shouldPlaceBlock(random))
            return null;

        boolean shouldMakeNewBlockInfo = false;
        BlockState state = newinfo.state;

        final BlockState SMOOTHBRICK_SLAB_STATE = Blocks.STONE_BRICK_SLAB.getDefaultState();
        final BlockState SMOOTHBRICK_STATE = Blocks.STONE_BRICKS.getDefaultState();

        if (state == Blocks.SANDSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE)) {
            BlockState stateCheck = world.getBlockState(newinfo.pos);
            if (stateCheck == SMOOTHBRICK_SLAB_STATE)
                return new Template.BlockInfo(newinfo.pos, SMOOTHBRICK_SLAB_STATE, null);
            else if (stateCheck.getMaterial() == Material.AIR)
                return null;
            else
                state = SMOOTHBRICK_STATE;

            shouldMakeNewBlockInfo = true;
        }

        if (state.getBlock() == Blocks.SANDSTONE_SLAB) {
            BlockState stateCheck = world.getBlockState(newinfo.pos);

            if (stateCheck.getMaterial() == Material.AIR)
                return null;
            else
                return new Template.BlockInfo(newinfo.pos, SMOOTHBRICK_SLAB_STATE, null);
        }

        Block block = state.getBlock();

        if (state == Blocks.STONE_BRICKS.getDefaultState())
            return random.nextBoolean() ? (shouldMakeNewBlockInfo ? new Template.BlockInfo(newinfo.pos, state, null) : newinfo) :
                    new Template.BlockInfo(newinfo.pos, random.nextInt(2) == 0 ? Blocks.CRACKED_STONE_BRICKS.getDefaultState() : Blocks.MOSSY_STONE_BRICKS.getDefaultState(), null);

        if (state == Blocks.SMOOTH_STONE_SLAB.getDefaultState())
            return random.nextBoolean() ? newinfo : new Template.BlockInfo(newinfo.pos, Blocks.COBBLESTONE_SLAB.getDefaultState(), null);

        if (block == TFBlocks.etched_nagastone.get())
            return random.nextBoolean() ? newinfo : new Template.BlockInfo(newinfo.pos, translateState(state, randomBlock(random, TFBlocks.etched_nagastone_mossy.get(), TFBlocks.etched_nagastone_weathered.get()), DirectionalBlock.FACING), null);

        if (block == TFBlocks.nagastone_pillar.get())
            return random.nextBoolean() ? newinfo : new Template.BlockInfo(newinfo.pos, translateState(state, randomBlock(random, TFBlocks.nagastone_pillar_mossy.get(), TFBlocks.nagastone_pillar_weathered.get()), RotatedPillarBlock.AXIS), null);

        if (block == TFBlocks.nagastone_stairs_left.get())
            return random.nextBoolean() ? newinfo : new Template.BlockInfo(newinfo.pos, translateState(state, randomBlock(random, TFBlocks.nagastone_stairs_mossy_left.get(), TFBlocks.nagastone_stairs_weathered_left.get()), StairsBlock.FACING, StairsBlock.HALF, StairsBlock.SHAPE), null);

        if (block == TFBlocks.nagastone_stairs_right.get())
            return random.nextBoolean() ? newinfo : new Template.BlockInfo(newinfo.pos, translateState(state, randomBlock(random, TFBlocks.nagastone_stairs_mossy_right.get(), TFBlocks.nagastone_stairs_weathered_right.get()), StairsBlock.FACING, StairsBlock.HALF, StairsBlock.SHAPE), null);

        return newinfo;
    }
}
