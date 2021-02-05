package twilightforest.worldgen.biomes;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;
import twilightforest.world.feature.TFGenTallGrass;
import twilightforest.world.TFWorld;

import java.util.Random;

public class TFDarkForestBiomeDecorator extends TFBiomeDecorator {

	private final Feature darkCanopyTreeGen = new TFGenDarkCanopyTree();
	private final Feature worldGenDeadBush = new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().with(BlockTFPlant.VARIANT, PlantVariant.DEADBUSH), 8);
	private final Feature worldGenForestGrass = new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().with(BlockTFPlant.VARIANT, PlantVariant.FORESTGRASS));
	private final Feature worldGenMushgloom = new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().with(BlockTFPlant.VARIANT, PlantVariant.MUSHGLOOM));

	@Override
	public void decorate(World world, Random rand, Biome biome, BlockPos pos) {
		// just decorate with what we need here
		TFFeature nearFeature = TFFeature.getNearestFeature(pos.getX() >> 4, pos.getZ() >> 4, world);
		if (nearFeature.areChunkDecorationsEnabled) {
			// add dark canopy trees
			int nc = (int) canopyPerChunk + ((rand.nextFloat() < (canopyPerChunk - (int) canopyPerChunk)) ? 1 : 0);
			for (int i = 0; i < nc; i++) {
				int rx = pos.getX() + rand.nextInt(16) + 8;
				int rz = pos.getZ() + rand.nextInt(16) + 8;
				darkCanopyTreeGen.generate(world, rand, new BlockPos(rx, world.getHeight(rx, rz), rz));
			}

			// regular trees
			for (int i = 0; i < this.treesPerChunk; ++i) {
				int rx = pos.getX() + rand.nextInt(16) + 8;
				int rz = pos.getZ() + rand.nextInt(16) + 8;
				int ry = TFWorld.getGroundLevel(world, rx, rz);
				Feature treeFeature = biome.getRandomTreeFeature(rand);
				treeFeature.setDecorationDefaults();
				treeFeature.generate(world, rand, new BlockPos(rx, ry, rz));
			}

			// dead bushes
			for (int i = 0; i < this.deadBushPerChunk; ++i) {
				int rx = pos.getX() + rand.nextInt(16) + 8;
				int rz = pos.getZ() + rand.nextInt(16) + 8;
				int ry = rand.nextInt(128);
				worldGenDeadBush.generate(world, rand, new BlockPos(rx, ry, rz));
			}

			// forest grass bushes
			for (int i = 0; i < this.deadBushPerChunk; ++i) {
				int rx = pos.getX() + rand.nextInt(16) + 8;
				int rz = pos.getZ() + rand.nextInt(16) + 8;
				int ry = rand.nextInt(128);
				worldGenForestGrass.generate(world, rand, new BlockPos(rx, ry, rz));
			}

			// mushrooms
			for (int i = 0; i < this.mushroomsPerChunk; ++i) {
				if (rand.nextInt(8) == 0) {
					int rx = pos.getX() + rand.nextInt(16) + 8;
					int rz = pos.getZ() + rand.nextInt(16) + 8;
					int ry = TFWorld.getGroundLevel(world, rx, rz);
					this.mushroomBrownGen.generate(world, rand, new BlockPos(rx, ry, rz));
				}
				if (rand.nextInt(16) == 0) {
					int rx = pos.getX() + rand.nextInt(16) + 8;
					int rz = pos.getZ() + rand.nextInt(16) + 8;
					int ry = TFWorld.getGroundLevel(world, rx, rz);
					this.mushroomRedGen.generate(world, rand, new BlockPos(rx, ry, rz));
				}
				if (rand.nextInt(24) == 0) {
					int rx = pos.getX() + rand.nextInt(16) + 8;
					int rz = pos.getZ() + rand.nextInt(16) + 8;
					int ry = TFWorld.getGroundLevel(world, rx, rz);
					// mushglooms
					worldGenMushgloom.generate(world, rand, new BlockPos(rx, ry, rz));
				}
			}
			if (rand.nextInt(4) == 0) {
				int rx = pos.getX() + rand.nextInt(16) + 8;
				int rz = pos.getZ() + rand.nextInt(16) + 8;
				int ry = rand.nextInt(128);
				this.mushroomBrownGen.generate(world, rand, new BlockPos(rx, ry, rz));
			}
			if (rand.nextInt(8) == 0) {
				int rx = pos.getX() + rand.nextInt(16) + 8;
				int rz = pos.getZ() + rand.nextInt(16) + 8;
				int ry = rand.nextInt(128);
				this.mushroomRedGen.generate(world, rand, new BlockPos(rx, ry, rz));
			}

			// pumpkins
			if (rand.nextInt(32) == 0) {
				int rx = pos.getX() + rand.nextInt(16) + 8;
				int rz = pos.getZ() + rand.nextInt(16) + 8;
				int ry = TFWorld.getGroundLevel(world, rx, rz);
				new PumpkinFeature().generate(world, rand, new BlockPos(rx, ry, rz));
			}

		}

		// do underground decorations
		decorateUnderground(world, rand, pos);
		decorateOnlyOres(world, rand, pos);
	}
}
