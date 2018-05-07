package twilightforest.biomes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenerator;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;
import twilightforest.world.TFGenDarkCanopyTree;
import twilightforest.world.TFGenTallGrass;
import twilightforest.world.TFTreeGenerator;
import twilightforest.world.TFWorld;

import java.util.Random;

public class TFDarkForestBiomeDecorator extends TFBiomeDecorator {

	private TFTreeGenerator darkCanopyTreeGen;
	private TFGenTallGrass worldGenDeadBush;
	private TFGenTallGrass worldGenForestGrass;
	private TFGenTallGrass worldGenMushgloom;


	public TFDarkForestBiomeDecorator() {
		darkCanopyTreeGen = new TFGenDarkCanopyTree();
		worldGenDeadBush = new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.DEADBUSH), 8);
		worldGenForestGrass = new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.FORESTGRASS));
		worldGenMushgloom = new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MUSHGLOOM));
	}

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
				darkCanopyTreeGen.generate(world, rand, world.getHeight(new BlockPos(rx, 0, rz)));
			}

			// regular trees
			for (int i = 0; i < this.treesPerChunk; ++i) {
				int rx = pos.getX() + rand.nextInt(16) + 8;
				int rz = pos.getZ() + rand.nextInt(16) + 8;
				int ry = getGroundLevel(world, new BlockPos(rx, 0, rz));
				WorldGenerator var5 = biome.getRandomTreeFeature(rand);
				var5.setDecorationDefaults();
				var5.generate(world, rand, new BlockPos(rx, ry, rz));
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
					int ry = getGroundLevel(world, new BlockPos(rx, 0, rz));
					this.mushroomBrownGen.generate(world, rand, new BlockPos(rx, ry, rz));
				}
				if (rand.nextInt(16) == 0) {
					int rx = pos.getX() + rand.nextInt(16) + 8;
					int rz = pos.getZ() + rand.nextInt(16) + 8;
					int ry = getGroundLevel(world, new BlockPos(rx, 0, rz));
					this.mushroomRedGen.generate(world, rand, new BlockPos(rx, ry, rz));
				}
				if (rand.nextInt(24) == 0) {
					int rx = pos.getX() + rand.nextInt(16) + 8;
					int rz = pos.getZ() + rand.nextInt(16) + 8;
					int ry = getGroundLevel(world, new BlockPos(rx, 0, rz));
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
				int ry = getGroundLevel(world, new BlockPos(rx, 0, rz));
				(new WorldGenPumpkin()).generate(world, rand, new BlockPos(rx, ry, rz));
			}

		}

		// do underground decorations
		decorateUnderground(world, rand, pos);
		decorateOnlyOres(world, rand, pos);

	}

	private int getGroundLevel(World world, BlockPos pos) {
		// go from sea level up.  If we get grass, return that, otherwise return the last dirt, stone or gravel we got
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		int lastDirt = TFWorld.SEALEVEL;
		for (int y = TFWorld.SEALEVEL; y < TFWorld.CHUNKHEIGHT - 1; y++) {
			Block blockID = chunk.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).getBlock();
			// grass = return immediately
			if (blockID == Blocks.GRASS) {
				return y + 1;
			} else if (blockID == Blocks.DIRT || blockID == Blocks.STONE || blockID == Blocks.GRAVEL) {
				lastDirt = y + 1;
			}
		}

		return lastDirt;
	}

}
