package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class TFGenWebs extends Feature<NoneFeatureConfiguration> {

	public TFGenWebs(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	private static boolean isValidMaterial(Material material) {
		return material == Material.LEAVES || material == Material.WOOD;
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> config) {
		WorldGenLevel world = config.level();
		ChunkGenerator generator = config.chunkGenerator();
		BlockPos pos = config.origin().above(config.random().nextInt(world.getMaxBuildHeight() - config.origin().getY()));
		while (pos.getY() > config.origin().getY() && world.isEmptyBlock(pos))
			pos = pos.below();

		if (!isValidMaterial(world.getBlockState(pos).getMaterial()))
			return false;

		do {
			if (world.isEmptyBlock(pos.below())) {
				world.setBlock(pos.below(), Blocks.COBWEB.defaultBlockState(), 16 | 2);
				return true;
			}
			pos = pos.below();
		} while (pos.getY() > config.origin().getY() && isValidMaterial(world.getBlockState(pos).getMaterial()));

		return false;
	}
}
