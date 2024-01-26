package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

//Places single block features in patches around the dark forest
public class DarkForestFeature extends Feature<RandomPatchConfiguration> {
    public DarkForestFeature(Codec<RandomPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<RandomPatchConfiguration> ctx) {
        WorldGenLevel reader = ctx.level();
        BlockPos pos = ctx.origin();
        RandomSource rand = ctx.random();
        RandomPatchConfiguration config = ctx.config();

        boolean foundDirt = false;

        if (pos.getY() <= 40) {
            int seaLevel = ctx.chunkGenerator().getSeaLevel();
            for (int dy = pos.getY(); dy >= seaLevel; dy--) {
                BlockState state = reader.getBlockState(new BlockPos(pos.getX(), dy - 1, pos.getZ()));
                if (state.is(BlockTags.DIRT) && reader.getBlockState(pos).canBeReplaced()) {
                    foundDirt = true;
                    pos = new BlockPos(pos.getX(), dy, pos.getZ());
                    break;
                } else if (state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.SAND)) {
                    break;
                }
            }
        }

        if (!foundDirt) {
            return false;
        }

        //RandomPatchFeature placement logic
        int i = 0;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int j = config.xzSpread() + 1;
        int k = config.ySpread() + 1;

        for(int l = 0; l < config.tries(); ++l) {
            blockpos$mutableblockpos.setWithOffset(pos, rand.nextInt(j) - rand.nextInt(j), rand.nextInt(k) - rand.nextInt(k), rand.nextInt(j) - rand.nextInt(j));
            if (config.feature().value().place(reader, ctx.chunkGenerator(), rand, blockpos$mutableblockpos)) {
                ++i;
            }
        }

        return i > 0;
    }
}
