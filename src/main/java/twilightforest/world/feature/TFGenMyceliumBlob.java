package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.AbstractSphereReplaceConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;

import java.util.Random;

public class TFGenMyceliumBlob extends AbstractSphereReplaceConfig {

	public TFGenMyceliumBlob(Codec<SphereReplaceConfig> configIn) {
		super(configIn);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, SphereReplaceConfig config) {

		boolean flag = false;
		int i = config.radius.func_242259_a(random);

		for(int j = pos.getX() - i; j <= pos.getX() + i; ++j) {
			for(int k = pos.getZ() - i; k <= pos.getZ() + i; ++k) {
				int l = j - pos.getX();
				int i1 = k - pos.getZ();
				if (l * l + i1 * i1 <= i * i) {
					for(int j1 = pos.getY() - config.field_242809_d; j1 <= pos.getY() + config.field_242809_d; ++j1) {
						BlockPos blockpos = new BlockPos(j, j1, k);
						Block block = world.getBlockState(blockpos).getBlock();

						for(BlockState blockstate : config.targets) {
							if (blockstate.isIn(block) && world.isAirBlock(pos.up())) {
								world.setBlockState(blockpos, config.state, 2);
								flag = true;
								break;
							}
						}
					}
				}
			}
		}

		return flag;
	}
}
