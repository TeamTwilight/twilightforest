package twilightforest.biomes;

import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import twilightforest.TFConfig;
import twilightforest.TFFeature;
import twilightforest.features.GenDruidHut;
import twilightforest.world.feature.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

//TODO: BiomeDecorator no longer exists. Turn into default decoration class akin to DefaultBiomeFeatures?
public class TFBiomeDecorator extends BiomeDecorator {

	WorldGenerator canopyTreeGen = new TFGenCanopyTree();
	WorldGenerator alternateCanopyGen = new TFGenCanopyMushroom();

	//private final WorldGenerator hollowTreeGen = new TFGenHollowTree();
	private final WorldGenerator myceliumBlobGen = new TFGenMyceliumBlob(5);
	private final WorldGenerator extraLakeGen = new WorldGenLakes(Blocks.WATER);
	private final WorldGenerator extraLavaPoolGen = new WorldGenLakes(Blocks.LAVA);
	private final WorldGenerator mangroveTreeGen = new TFGenMangroveTree();
	private final WorldGenerator plantRootGen = new TFGenPlantRoots();
	private final WorldGenerator woodRootGen = new TFGenWoodRoots();
	private final WorldGenerator caveWaterGen = new WorldGenLiquids(Blocks.FLOWING_WATER);
	private final WorldGenerator torchBerryGen = new TFGenTorchBerries();

	public float canopyPerChunk = TFConfig.performance.canopyCoverage;
	public boolean hasCanopy = true;
	public float alternateCanopyChance = 0f;
	public int myceliumPerChunk = 0;
	public int mangrovesPerChunk = 0;
	public int lakesPerChunk = 0;
	public float lavaPoolChance = 0f;

	private static final List<RuinEntry> ruinList = new ArrayList<>();

	static {
		// make list of ruins
		addRuin(TFGenStoneCircle::new      , TFConfig.dimension.worldGenWeights.stoneCircleWeight    );
		addRuin(TFGenWell::new             , TFConfig.dimension.worldGenWeights.wellWeight           );
		addRuin(TFGenOutsideStalagmite::new, TFConfig.dimension.worldGenWeights.stalagmiteWeight     );
		addRuin(TFGenFoundation::new       , TFConfig.dimension.worldGenWeights.foundationWeight     );
		addRuin(TFGenMonolith::new         , TFConfig.dimension.worldGenWeights.monolithWeight       );
		addRuin(TFGenGroveRuins::new       , TFConfig.dimension.worldGenWeights.groveRuinsWeight     );
		addRuin(TFGenHollowStump::new      , TFConfig.dimension.worldGenWeights.hollowStumpWeight    );
		addRuin(TFGenFallenHollowLog::new  , TFConfig.dimension.worldGenWeights.fallenHollowLogWeight);
		addRuin(TFGenFallenSmallLog::new   , TFConfig.dimension.worldGenWeights.fallenSmallLogWeight );

		addRuin(GenDruidHut::new           , TFConfig.dimension.worldGenWeights.druidHutWeight       );
	}

	private static void addRuin(Supplier<TFGenerator> generator, int weight) {
		if (weight > 0) {
			ruinList.add(new RuinEntry(generator.get(), weight));
		}
	}

	private static class RuinEntry extends WeightedRandom.Item {

		public final TFGenerator generator;

		RuinEntry(TFGenerator generator, int weight) {
			super(weight);
			this.generator = generator;
		}
	}

	@Override
	public void decorate(World world, Random rand, Biome biome, BlockPos pos) {

		// check for features
		TFFeature nearFeature = TFFeature.getNearestFeature(pos.getX() >> 4, pos.getZ() >> 4, world);

		if (!nearFeature.areChunkDecorationsEnabled) {
			// no normal decorations here, these parts supply their own decorations.
			decorateUnderground(world, rand, pos);
			decorateOnlyOres(world, rand, pos);
		} else {
//    		// hollow trees!
//	    	if (rand.nextInt(24) == 0) {
//		        int rx = mapX + rand.nextInt(16) + 8;
//		        int rz = mapZ + rand.nextInt(16) + 8;
//		        int ry = world.getHeightValue(rx, rz);
//	    		hollowTreeGen.generate(world, rand, rx, ry, rz);
//	    	}

			// regular decorations
			super.decorate(world, rand, biome, pos);
		}
	}

	@Override
	protected void genDecorations(Biome biome, World world, Random randomGenerator) {
		// random features!
		if (randomGenerator.nextInt(6) == 0) {
			int rx = chunkPos.getX() + randomGenerator.nextInt(14) + 8;
			int rz = chunkPos.getZ() + randomGenerator.nextInt(14) + 8;
			TFGenerator rf = randomFeature(randomGenerator);
			if (rf != null) {
				rf.generate(world, randomGenerator, new BlockPos(rx, world.getHeight(rx, rz), rz));
			}
		}

		if (this.hasCanopy) {
			//TODO: is there a better place for this? We want to load this value once the config file is loaded.
			this.canopyPerChunk = TFConfig.performance.canopyCoverage;

			// add canopy trees
			int nc = (int) canopyPerChunk + ((randomGenerator.nextFloat() < (canopyPerChunk - (int) canopyPerChunk)) ? 1 : 0);
			for (int i = 0; i < nc; i++) {
				int rx = chunkPos.getX() + randomGenerator.nextInt(16) + 8;
				int rz = chunkPos.getZ() + randomGenerator.nextInt(16) + 8;
				BlockPos genPos = new BlockPos(rx, world.getHeight(rx, rz), rz);
				if (alternateCanopyChance > 0 && randomGenerator.nextFloat() < alternateCanopyChance) {
					alternateCanopyGen.generate(world, randomGenerator, genPos);
				} else {
					canopyTreeGen.generate(world, randomGenerator, genPos);
				}
			}
		}

		// mangrove trees
		for (int i = 0; i < mangrovesPerChunk; i++) {
			int rx = chunkPos.getX() + randomGenerator.nextInt(16) + 8;
			int rz = chunkPos.getZ() + randomGenerator.nextInt(16) + 8;
			mangroveTreeGen.generate(world, randomGenerator, new BlockPos(rx, world.getHeight(rx, rz), rz));
		}
		// add extra lakes for swamps
		for (int i = 0; i < lakesPerChunk; i++) {
			int rx = chunkPos.getX() + randomGenerator.nextInt(16) + 8;
			int rz = chunkPos.getZ() + randomGenerator.nextInt(16) + 8;
			extraLakeGen.generate(world, randomGenerator, new BlockPos(rx, world.getHeight(rx, rz), rz));
		}

		// add extra lava for fire swamps
		if (randomGenerator.nextFloat() < lavaPoolChance) {
			int rx = chunkPos.getX() + randomGenerator.nextInt(16) + 8;
			int rz = chunkPos.getZ() + randomGenerator.nextInt(16) + 8;
			extraLavaPoolGen.generate(world, randomGenerator, new BlockPos(rx, world.getHeight(rx, rz), rz));
		}

		// mycelium blobs
		for (int i = 0; i < myceliumPerChunk; i++) {
			int rx = chunkPos.getX() + randomGenerator.nextInt(16) + 8;
			int rz = chunkPos.getZ() + randomGenerator.nextInt(16) + 8;
			myceliumBlobGen.generate(world, randomGenerator, new BlockPos(rx, world.getHeight(rx, rz), rz));
		}

		super.genDecorations(biome, world, randomGenerator);
		decorateUnderground(world, randomGenerator, chunkPos);
	}

	/**
	 * Generate the Twilight Forest underground decorations
	 */
	protected void decorateUnderground(World world, Random rand, BlockPos pos) {
		// generate roots
		for (int i = 0; i < 12; ++i) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int ry = 64;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			plantRootGen.generate(world, rand, new BlockPos(rx, ry, rz));
		}

		// generate roots
		for (int i = 0; i < 20; ++i) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int ry = rand.nextInt(64);
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			woodRootGen.generate(world, rand, new BlockPos(rx, ry, rz));
		}

		// extra underground water sources
		if (this.generateFalls) {
			for (int i = 0; i < 50; ++i) {
				int rx = pos.getX() + rand.nextInt(16) + 8;
				int ry = rand.nextInt(24) + 4;
				int rz = pos.getZ() + rand.nextInt(16) + 8;
				caveWaterGen.generate(world, rand, new BlockPos(rx, ry, rz));
			}
		}

		// torch berries are almost guaranteed to spawn so we don't need many
		for (int i = 0; i < 3; ++i) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int ry = 64;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			torchBerryGen.generate(world, rand, new BlockPos(rx, ry, rz));
		}
	}

	/**
	 * Generates ores only
	 */
	public void decorateOnlyOres(World world, Random rand, BlockPos pos) {
		this.chunkPos = pos;
		if (this.chunkProviderSettings == null) {
			this.chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(world.getWorldInfo().getGeneratorOptions()).build();
			this.chunkPos = pos;
			this.dirtGen = new WorldGenMinable(Blocks.DIRT.getDefaultState(), this.chunkProviderSettings.dirtSize);
			this.gravelOreGen = new WorldGenMinable(Blocks.GRAVEL.getDefaultState(), this.chunkProviderSettings.gravelSize);
			this.graniteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
			this.dioriteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
			this.andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
			this.coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), this.chunkProviderSettings.coalSize);
			this.ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), this.chunkProviderSettings.ironSize);
			this.goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), this.chunkProviderSettings.goldSize);
			this.redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), this.chunkProviderSettings.redstoneSize);
			this.diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), this.chunkProviderSettings.diamondSize);
			this.lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), this.chunkProviderSettings.lapisSize);
		}
		this.generateOres(world, rand);
	}

	/**
	 * Gets a random feature suitable for the current biome.
	 */
	@Nullable
	public TFGenerator randomFeature(Random rand) {
		return ruinList.isEmpty() ? null : WeightedRandom.getRandomItem(rand, ruinList).generator;
	}

	public void setTreesPerChunk(int treesPerChunk) {
		this.treesPerChunk = treesPerChunk;
	}

	public void setBigMushroomsPerChunk(int bigMushroomsPerChunk) {
		this.bigMushroomsPerChunk = bigMushroomsPerChunk;
	}

	public void setClayPerChunk(int clayPerChunk) {
		this.clayPerChunk = clayPerChunk;
	}

	public void setDeadBushPerChunk(int deadBushPerChunk) {
		this.deadBushPerChunk = deadBushPerChunk;
	}

	public void setMushroomsPerChunk(int mushroomsPerChunk) {
		this.mushroomsPerChunk = mushroomsPerChunk;
	}

	public void setFlowersPerChunk(int flowersPerChunk) {
		this.flowersPerChunk = flowersPerChunk;
	}

	public void setReedsPerChunk(int reedsPerChunk) {
		this.reedsPerChunk = reedsPerChunk;
	}

	public void setWaterlilyPerChunk(int waterlilyPerChunk) {
		this.waterlilyPerChunk = waterlilyPerChunk;
	}

	public void setGrassPerChunk(int grassPerChunk) {
		this.grassPerChunk = grassPerChunk;
	}
}
