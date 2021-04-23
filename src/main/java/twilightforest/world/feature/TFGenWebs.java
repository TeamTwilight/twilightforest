package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.material.Material;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import java.util.Random;

public class TFGenWebs extends Feature<NoFeatureConfig> {

	public TFGenWebs(Codec<NoFeatureConfig> config) {
		super(config);
	}

	private static boolean isValidMaterial(Material material) {
		return material == Material.LEAVES || material == Material.WOOD;
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
		while (pos.getY() > generator.getGroundHeight() && world.isAirBlock(pos))
			pos = pos.down();

		if (!isValidMaterial(world.getBlockState(pos).getMaterial()))
			return false;

		do {
			if (world.isAirBlock(pos.down())) {
				world.setBlockState(pos.down(), Blocks.COBWEB.getDefaultState(), 16 | 2);
				return true;
			}
			pos = pos.down();
		} while (pos.getY() > generator.getGroundHeight() && isValidMaterial(world.getBlockState(pos).getMaterial()));

		return false;
	}
}
