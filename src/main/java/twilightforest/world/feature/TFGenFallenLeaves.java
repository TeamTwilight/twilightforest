package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BushBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TFGenFallenLeaves extends Feature<NoFeatureConfig> {

	public TFGenFallenLeaves(Codec<NoFeatureConfig> config) {
		super(config);
	}

	private final BlockState state = TFBlocks.fallen_leaves.get().getDefaultState();

	@Override
	public boolean generate(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos position, NoFeatureConfig config) {
		do {
			BlockState state = worldIn.getBlockState(position.down());
			if (worldIn.isAirBlock(position) && (state.getMaterial() == Material.ORGANIC || state.getMaterial() == Material.EARTH))
				break;
			position = position.down();
		} while (position.getY() > generator.getGroundHeight());

		for (int x = 0; x < 5; x++)
			for (int z = 0; z < 5; z++) {
				if (rand.nextInt(3) != 0)
					continue;
				boolean flag = false;
				int y = 2;
				do {
					BlockState state = worldIn.getBlockState(position.add(x, y, z).down());
					if (worldIn.isAirBlock(position.add(x, y, z)) && (state.getMaterial() == Material.ORGANIC || state.getMaterial() == Material.EARTH)) {
						flag = true;
						break;
					}
					y--;
				} while (y >= -2);
				if (!flag)
					continue;
				BlockPos pos = position.add(x, y, z);
				if (((BushBlock) state.getBlock()).isValidPosition(state, worldIn, pos))
					worldIn.setBlockState(pos, state, 16 | 2);
			}

		return true;
	}

}
