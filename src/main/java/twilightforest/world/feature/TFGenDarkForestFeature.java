package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import twilightforest.world.TFGenerationSettings;

import java.util.Random;

//Places single block features in patches around the dark forest
public class TFGenDarkForestFeature extends Feature<BlockClusterFeatureConfig> {
    public TFGenDarkForestFeature(Codec<BlockClusterFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BlockClusterFeatureConfig config) {
        boolean foundDirt = false;
        Material materialUnder;

        if(pos.getY() <= 40) {
            for (int dy = pos.getY(); dy >= TFGenerationSettings.SEALEVEL; dy--) {
                materialUnder = reader.getBlockState(new BlockPos(pos.getX(), dy - 1, pos.getZ())).getMaterial();
                if ((materialUnder == Material.ORGANIC || materialUnder == Material.EARTH) && reader.getBlockState(pos) == Blocks.AIR.getDefaultState()) {
                    foundDirt = true;
                    pos = new BlockPos(pos.getX(), dy, pos.getZ());
                    break;
                } else if (materialUnder == Material.ROCK || materialUnder == Material.SAND) {
                    break;
                }
            }
        }

        if (!foundDirt) {
            return false;
        }

        //RandomPatchFeature placement logic
        BlockState blockstate = config.stateProvider.getBlockState(rand, pos);
        int i = 0;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        for(int j = 0; j < config.tryCount; ++j) {
            blockpos$mutable.setAndOffset(pos, rand.nextInt(config.xSpread + 1) - rand.nextInt(config.xSpread + 1), rand.nextInt(config.ySpread + 1) - rand.nextInt(config.ySpread + 1), rand.nextInt(config.zSpread + 1) - rand.nextInt(config.zSpread + 1));
            BlockPos blockpos1 = blockpos$mutable.down();
            BlockState blockstate1 = reader.getBlockState(blockpos1);
            if ((reader.isAirBlock(blockpos$mutable) || config.isReplaceable && reader.getBlockState(blockpos$mutable).getMaterial().isReplaceable()) && blockstate.isValidPosition(reader, blockpos$mutable) && (config.whitelist.isEmpty() || config.whitelist.contains(blockstate1.getBlock())) && !config.blacklist.contains(blockstate1) && (!config.requiresWater || reader.getFluidState(blockpos1.west()).isTagged(FluidTags.WATER) || reader.getFluidState(blockpos1.east()).isTagged(FluidTags.WATER) || reader.getFluidState(blockpos1.north()).isTagged(FluidTags.WATER) || reader.getFluidState(blockpos1.south()).isTagged(FluidTags.WATER))) {
                config.blockPlacer.place(reader, blockpos$mutable, blockstate, rand);
                ++i;
            }
        }
        return i > 0;
    }
}
