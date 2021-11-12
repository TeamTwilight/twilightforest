package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TFGenFallenLeaves extends Feature<NoneFeatureConfiguration> {

	public TFGenFallenLeaves(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	private final BlockState state = TFBlocks.FALLEN_LEAVES.get().defaultBlockState();

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel worldIn = ctx.level();
		ChunkGenerator generator = ctx.chunkGenerator();
		BlockPos position = ctx.origin();
		Random rand = ctx.random();

		do {
			BlockState state = worldIn.getBlockState(position.below());
			if (worldIn.isEmptyBlock(position) && (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.DIRT))
				break;
			position = position.below();
		} while (position.getY() > generator.getSpawnHeight(worldIn));

		for (int x = 0; x < 5; x++)
			for (int z = 0; z < 5; z++) {
				if (rand.nextInt(3) != 0)
					continue;
				boolean flag = false;
				int y = 2;
				do {
					BlockState state = worldIn.getBlockState(position.offset(x, y, z).below());
					if (worldIn.isEmptyBlock(position.offset(x, y, z)) && (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.DIRT)) {
						flag = true;
						break;
					}
					y--;
				} while (y >= -2);
				if (!flag)
					continue;
				BlockPos pos = position.offset(x, y, z);
				if (((BushBlock) state.getBlock()).canSurvive(state, worldIn, pos))
					worldIn.setBlock(pos, state, 16 | 2);
			}

		return true;
	}

}
