package twilightforest.biomes;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;
import twilightforest.world.feature.TFGenHangingLamps;
import twilightforest.world.feature.TFGenLampposts;
import twilightforest.world.feature.TFGenTallGrass;
import twilightforest.world.TFWorld;

import java.util.Random;

public class TFBiomeFireflyForest extends TFBiomeBase {

	private static final int LAMPPOST_CHANCE = 4;

	private final WorldGenerator tfGenHangingLamps = new TFGenHangingLamps();
	private final WorldGenerator tfGenLampposts = new TFGenLampposts(TFBlocks.firefly_jar.getDefaultState());
	private final WorldGenerator worldGenMushgloom = new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MUSHGLOOM));

	public TFBiomeFireflyForest(Builder props) {
		super(props);

		this.decorator.flowersPerChunk = 4;
		this.decorator.grassPerChunk = 1;

		this.getTFBiomeDecorator().setTreesPerChunk(2);
	}

	//TODO: Move to feature decorator
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
			int ry = TFWorld.getGroundLevel(world, rx, rz);
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
			int ry = TFWorld.getGroundLevel(world, rx, rz);

			this.tfGenLampposts.generate(world, rand, new BlockPos(rx, ry, rz));
		}

		// extra pumpkins (should they be lit?)
		if (rand.nextInt(32) == 0) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			int ry = TFWorld.getGroundLevel(world, rx, rz);

			new WorldGenPumpkin().generate(world, rand, new BlockPos(rx, ry, rz));
		}
	}
}
