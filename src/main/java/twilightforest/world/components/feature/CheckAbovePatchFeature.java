package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;

//[VanillaCopy] of BaseDiskFeature, but we add a check to make sure the block above is air
public class CheckAbovePatchFeature extends Feature<DiskConfiguration> {

	public CheckAbovePatchFeature(Codec<DiskConfiguration> pCodec) {
		super(pCodec);
	}

	@Override
	public boolean place(FeaturePlaceContext<DiskConfiguration> ctx) {
		DiskConfiguration config = ctx.config();
		BlockPos blockpos = ctx.origin();
		WorldGenLevel level = ctx.level();
		boolean flag = false;
		int i = blockpos.getY();
		int j = i + config.halfHeight;
		int k = i - config.halfHeight - 1;
		boolean flag1 = config.state.getBlock() instanceof FallingBlock;
		int l = config.radius.sample(ctx.random());

		for(int i1 = blockpos.getX() - l; i1 <= blockpos.getX() + l; ++i1) {
			for(int j1 = blockpos.getZ() - l; j1 <= blockpos.getZ() + l; ++j1) {
				int k1 = i1 - blockpos.getX();
				int l1 = j1 - blockpos.getZ();
				if (k1 * k1 + l1 * l1 <= l * l) {
					boolean flag2 = false;

					for(int i2 = j; i2 >= k; --i2) {
						BlockPos blockpos1 = new BlockPos(i1, i2, j1);
						BlockState blockstate = level.getBlockState(blockpos1);
						Block block = blockstate.getBlock();
						boolean flag3 = false;
						if (i2 > k) {
							for(BlockState blockstate1 : config.targets) {
								//TF: add a check to make sure the block above is air or replaceable
								if (blockstate1.is(block) && (level.getBlockState(blockpos1.above()).isAir() || level.getBlockState(blockpos1.above()).getMaterial().isReplaceable())) {
									level.setBlock(blockpos1, config.state, 2);
									this.markAboveForPostProcessing(level, blockpos1);
									flag = true;
									flag3 = true;
									break;
								}
							}
						}

						if (flag1 && flag2 && blockstate.isAir()) {
							BlockState blockstate2 = config.state.is(Blocks.RED_SAND) ? Blocks.RED_SANDSTONE.defaultBlockState() : Blocks.SANDSTONE.defaultBlockState();
							level.setBlock(new BlockPos(i1, i2 + 1, j1), blockstate2, 2);
						}

						flag2 = flag3;
					}
				}
			}
		}
		return flag;
	}
}
