package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.util.FeatureUtil;

import java.util.Random;

public class TFGenStoneCircle extends Feature<NoFeatureConfig> {

	public TFGenStoneCircle(Codec<NoFeatureConfig> configIn) {
		super(configIn);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		if (!FeatureUtil.isAreaSuitable(world, rand, pos.add(-3, 0, -3), 6, 4, 6)) {
			return false;
		}

		BlockState mossyCobble = Blocks.MOSSY_COBBLESTONE.getDefaultState();

		// okay!  circle!
		for (int cy = 0; cy <= 2; cy++) {
			world.setBlockState(pos.add(-3, cy, 0), mossyCobble, 3);
			world.setBlockState(pos.add(3, cy, 0), mossyCobble, 3);
			world.setBlockState(pos.add(0, cy, -3), mossyCobble, 3);
			world.setBlockState(pos.add(0, cy, 3), mossyCobble, 3);
			world.setBlockState(pos.add(-2, cy, -2), mossyCobble, 3);
			world.setBlockState(pos.add(2, cy, -2), mossyCobble, 3);
			world.setBlockState(pos.add(-2, cy, 2), mossyCobble, 3);
			world.setBlockState(pos.add(2, cy, 2), mossyCobble, 3);
		}

		return true;
	}
}
