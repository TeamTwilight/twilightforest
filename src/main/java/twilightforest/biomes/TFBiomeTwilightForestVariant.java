package twilightforest.biomes;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.PlantVariant;
import twilightforest.world.TFGenTallGrass;

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
			new WorldGenShrub(
					Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE),
					Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK));
		} else if (random.nextInt(10) == 0) {
			return new WorldGenBigTree(false);
		}

		return TREE_FEATURE;
	}

	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random par1Random) {
		if (par1Random.nextInt(4) != 0) {
			return new WorldGenTallGrass(BlockTallGrass.EnumType.FERN);
		} else if (par1Random.nextBoolean()) {
			return new TFGenTallGrass(TFBlocks.plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MAYAPPLE));
		} else {
			return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
		}
	}

	@Override
	public void decorate(World par1World, Random par2Random, BlockPos pos) {
		DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);

		for (int i = 0; i < 7; ++i) {
			int rx = pos.getX() + par2Random.nextInt(16) + 8;
			int rz = pos.getZ() + par2Random.nextInt(16) + 8;
			int ry = par2Random.nextInt(par1World.getHeight(new BlockPos(rx, 0, rz)).getY() + 32);
			DOUBLE_PLANT_GENERATOR.generate(par1World, par2Random, new BlockPos(rx, ry, rz));
		}

		super.decorate(par1World, par2Random, pos);
	}


}
