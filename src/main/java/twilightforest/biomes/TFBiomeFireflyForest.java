package twilightforest.biomes;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;
import twilightforest.world.TFGenHangingLamps;
import twilightforest.world.TFGenLampposts;
import twilightforest.world.TFGenTallGrass;
import twilightforest.world.TFWorld;

import java.util.Random;

public class TFBiomeFireflyForest extends TFBiomeBase {

	private static final int LAMPPOST_CHANCE = 4;
	private TFGenHangingLamps tfGenHangingLamps;
	private TFGenLampposts tfGenLampposts;
	private TFGenTallGrass worldGenMushgloom;

	public TFBiomeFireflyForest(BiomeProperties props) {
		super(props);

		this.worldGenMushgloom = new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MUSHGLOOM));
		this.tfGenHangingLamps = new TFGenHangingLamps();
		this.tfGenLampposts = new TFGenLampposts();

		this.decorator.flowersPerChunk = 4;
		this.decorator.grassPerChunk = 1;

		this.getTFBiomeDecorator().setTreesPerChunk(2);

	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {
		int flowerCycles = rand.nextInt(3) - 1;
		// Handle mods not staying in the class hierarchy when replacing vanilla
		if (Biomes.MUTATED_FOREST instanceof BiomeForest) {
			((BiomeForest) Biomes.MUTATED_FOREST).addDoublePlants(world, rand, pos, flowerCycles);
		}

		super.decorate(world, rand, pos);

		if (rand.nextInt(24) == 0) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			int ry = getGroundLevel(world, new BlockPos(rx, 0, rz));
			// mushglooms
			this.worldGenMushgloom.generate(world, rand, new BlockPos(rx, ry, rz));
		}

		// hanging lamps
		for (int i = 0; i < 30; i++) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			int ry = TFWorld.SEALEVEL + rand.nextInt(TFWorld.CHUNKHEIGHT - TFWorld.SEALEVEL);

			this.tfGenHangingLamps.generate(world, rand, new BlockPos(rx, ry, rz));
		}

		if (rand.nextInt(LAMPPOST_CHANCE) == 0) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			int ry = getGroundLevel(world, new BlockPos(rx, 0, rz));

			this.tfGenLampposts.generate(world, rand, new BlockPos(rx, ry, rz));
		}

		// extra pumpkins (should they be lit?)
		if (rand.nextInt(32) == 0) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			int ry = getGroundLevel(world, new BlockPos(rx, 0, rz));
			(new WorldGenPumpkin()).generate(world, rand, new BlockPos(rx, ry, rz));
		}

	}

	private int getGroundLevel(World world, BlockPos pos) {
		// go from sea level up.  If we get grass, return that, otherwise return the last dirt, stone or gravel we got
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		int lastDirt = TFWorld.SEALEVEL;
		for (int y = TFWorld.SEALEVEL; y < TFWorld.CHUNKHEIGHT - 1; y++) {
			Block block = chunk.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).getBlock();
			// grass = return immediately
			if (block == Blocks.GRASS) {
				return y + 1;
			} else if (block == Blocks.DIRT || block == Blocks.STONE || block == Blocks.GRAVEL) {
				lastDirt = y + 1;
			}
		}

		return lastDirt;
	}
}
