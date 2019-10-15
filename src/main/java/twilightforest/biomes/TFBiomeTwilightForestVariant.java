package twilightforest.biomes;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;
import twilightforest.block.BlockTFLeaves;
import twilightforest.block.BlockTFLog;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.LeavesVariant;
import twilightforest.enums.PlantVariant;
import twilightforest.enums.WoodVariant;
import twilightforest.world.feature.TFGenTallGrass;

import java.util.Random;

public class TFBiomeTwilightForestVariant extends TFBiomeBase {

	public TFBiomeTwilightForestVariant(BiomeProperties props) {
		super(props);

		getTFBiomeDecorator().setTreesPerChunk(25);
		getTFBiomeDecorator().setGrassPerChunk(15);
		getTFBiomeDecorator().setFlowersPerChunk(8);
	}

	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random random) {
		if (random.nextInt(5) == 0) {
			return new WorldGenShrub(
					TFBlocks.twilight_log.getDefaultState().withProperty(BlockTFLog.VARIANT, WoodVariant.OAK),
					TFBlocks.twilight_leaves.getDefaultState().withProperty(BlockTFLeaves.VARIANT, LeavesVariant.OAK).withProperty(BlockLeaves.CHECK_DECAY, false)
			);
		} else if (random.nextInt(10) == 0) {
			return new WorldGenBigTree(false);
		} else {
			return TREE_FEATURE;
		}
	}

	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random random) {
		if (random.nextInt(4) != 0) {
			return new WorldGenTallGrass(BlockTallGrass.EnumType.FERN);
		} else if (random.nextBoolean()) {
			return new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MAYAPPLE));
		} else {
			return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
		}
	}

	@Override
	public void decorate(World world, Random random, BlockPos pos) {

		DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);
		for (int i = 0; i < 7; ++i) {
			int rx = pos.getX() + random.nextInt(16) + 8;
			int rz = pos.getZ() + random.nextInt(16) + 8;
			int ry = random.nextInt(world.getHeight(rx, rz) + 32);
			DOUBLE_PLANT_GENERATOR.generate(world, random, new BlockPos(rx, ry, rz));
		}

		super.decorate(world, random, pos);
	}
}
