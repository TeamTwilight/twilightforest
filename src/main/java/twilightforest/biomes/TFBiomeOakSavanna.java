package twilightforest.biomes;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;
import twilightforest.world.feature.TFGenCanopyOak;
import twilightforest.world.feature.TFGenNoTree;
import twilightforest.world.feature.TFGenTallGrass;

import java.util.Random;

public class TFBiomeOakSavanna extends TFBiomeBase {

	public TFBiomeOakSavanna(Builder props) {
		super(props);

		getTFBiomeDecorator().canopyTreeGen = new TFGenCanopyOak();
		getTFBiomeDecorator().alternateCanopyChance = 0.8F;
		getTFBiomeDecorator().alternateCanopyGen = new TFGenNoTree();

		this.decorator.treesPerChunk = 1;
		this.decorator.flowersPerChunk = 4;
		this.decorator.grassPerChunk = 20;
	}

    //TODO: Move to feature decorator
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random random) {
		if (random.nextInt(10) == 0) {
			return new WorldGenBigTree(false);
		} else {
			return TREE_FEATURE;
		}
	}

    //TODO: Move to feature decorator
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

    //TODO: Move to feature decorator
	@Override
	public void decorate(World world, Random random, BlockPos pos) {

		DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);
		for (int i = 0; i < 7; ++i) {
			int x = pos.getX() + random.nextInt(16) + 8;
			int z = pos.getZ() + random.nextInt(16) + 8;
			int y = random.nextInt(world.getHeight(x, z) + 32);
			DOUBLE_PLANT_GENERATOR.generate(world, random, new BlockPos(x, y, z));
		}

		super.decorate(world, random, pos);
	}
}
