package twilightforest.biomes;

import net.minecraft.block.BlockDoublePlant;
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
					TFBlocks.twilight_log.getDefaultState().withProperty(BlockTFLog.VARIANT, WoodVariant.OAK),
					TFBlocks.twilight_leaves.getDefaultState().withProperty(BlockTFLeaves.VARIANT, LeavesVariant.OAK));
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
			return new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MAYAPPLE));
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
