package twilightforest.biomes;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;
import twilightforest.world.feature.TFGenCanopyOak;
import twilightforest.world.feature.TFGenNoTree;
import twilightforest.world.feature.TFGenTallGrass;

import java.util.Random;

public class TFBiomeOakSavanna extends TFBiomeBase {

	public TFBiomeOakSavanna(BiomeProperties props) {
		super(props);

		getTFBiomeDecorator().canopyTreeGen = new TFGenCanopyOak();
		getTFBiomeDecorator().alternateCanopyChance = 0.8F;
		getTFBiomeDecorator().alternateCanopyGen = new TFGenNoTree();

		this.decorator.treesPerChunk = 1;
		this.decorator.flowersPerChunk = 4;
		this.decorator.grassPerChunk = 20;
	}

	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random random) {
		if (random.nextInt(10) == 0) {
			return new WorldGenBigTree(false);
		} else {
			return TREE_FEATURE;
		}
	}

	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random random) {
		if (random.nextInt(10) == 0) {
			return new WorldGenTallGrass(BlockTallGrass.EnumType.FERN);
		} else if (random.nextInt(10) == 0) {
			return new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MAYAPPLE));
		} else {
			return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
		}
	}

	@Override
	public void decorate(World world, Random random, BlockPos pos) {
		DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);

		for (int k = 0; k < 7; ++k) {
			int l = pos.getX() + random.nextInt(16) + 8;
			int i1 = pos.getZ() + random.nextInt(16) + 8;
			int j1 = random.nextInt(world.getHeight(new BlockPos(l, 0, i1)).getY() + 32);
			DOUBLE_PLANT_GENERATOR.generate(world, random, new BlockPos(l, j1, i1));
		}

		super.decorate(world, random, pos);
	}
}
