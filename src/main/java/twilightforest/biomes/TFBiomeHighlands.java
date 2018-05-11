package twilightforest.biomes;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;
import twilightforest.entity.EntityTFTroll;
import twilightforest.world.TFGenTallGrass;
import twilightforest.world.TFGenTrollRoots;

import java.util.Random;


public class TFBiomeHighlands extends TFBiomeBase {
	private static final WorldGenTaiga1 taigaGen1 = new WorldGenTaiga1();
	private static final WorldGenTaiga2 taigaGen2 = new WorldGenTaiga2(false);
	private static final WorldGenMegaPineTree megaPineGen1 = new WorldGenMegaPineTree(false, false);
	private static final WorldGenMegaPineTree megaPineGen2 = new WorldGenMegaPineTree(false, true);
	private static final WorldGenBlockBlob genBoulder = new WorldGenBlockBlob(Blocks.MOSSY_COBBLESTONE, 0);
	private static final TFGenTrollRoots genTrollRoots = new TFGenTrollRoots();
	private static final TFGenTallGrass worldGenMushgloom = new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MUSHGLOOM));


	public TFBiomeHighlands(BiomeProperties props) {
		super(props);

		((TFBiomeDecorator) decorator).hasCanopy = false;

		this.decorator.grassPerChunk = 7;
		this.decorator.deadBushPerChunk = 1;
		this.decorator.generateFalls = false;

		undergroundMonsterList.clear();
		undergroundMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityCreeper.class, 1, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntitySlime.class, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityTFTroll.class, 10, 4, 4));
	}

	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random random) {
		if (random.nextInt(4) == 0) {
			return taigaGen1;
		} else if (random.nextInt(10) == 0) {
			return taigaGen2;
		} else if (random.nextInt(3) == 0) {
			return megaPineGen1;
		} else if (random.nextInt(13) == 0) {
			return megaPineGen2;
		} else {
			return birchGen;
		}
	}

	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random par1Random) {
		return par1Random.nextInt(5) > 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
	}

	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal) {
		this.topBlock = Blocks.GRASS.getDefaultState();
		this.fillerBlock = Blocks.DIRT.getDefaultState();

		if (noiseVal > 1.75D) {
			this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
		} else if (noiseVal > -0.95D) {
			this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
		}

		this.genTwilightBiomeTerrain(world, rand, primer, x, z, noiseVal);
	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {
		int dx, dy, dz;

		// boulders
		int maxBoulders = rand.nextInt(2);
		for (int i = 0; i < maxBoulders; ++i) {
			dx = pos.getX() + rand.nextInt(16) + 8;
			dz = pos.getZ() + rand.nextInt(16) + 8;
			genBoulder.generate(world, rand, world.getHeight(new BlockPos(dx, 0, dz)));
		}

		// giant ferns
		DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);
		for (int i = 0; i < 7; ++i) {
			dx = pos.getX() + rand.nextInt(16) + 8;
			dz = pos.getZ() + rand.nextInt(16) + 8;
			dy = rand.nextInt(world.getHeight(new BlockPos(dx, 0, dz)).getY() + 32);
			DOUBLE_PLANT_GENERATOR.generate(world, rand, new BlockPos(dx, dy, dz));
		}

		// mushglooms
		for (int i = 0; i < 1; ++i) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			int ry = rand.nextInt(64);
			// mushglooms
			worldGenMushgloom.generate(world, rand, new BlockPos(rx, ry, rz));
		}

		// generate roots
		for (int i = 0; i < 24; ++i) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int ry = 64;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			genTrollRoots.generate(world, rand, new BlockPos(rx, ry, rz));
		}

		super.decorate(world, rand, pos);
	}

	@Override
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[] {
                //new ResourceLocation(TwilightForestMod.ID, "progress_hydra"),
                //new ResourceLocation(TwilightForestMod.ID, "progress_ur_ghast"),
                //new ResourceLocation(TwilightForestMod.ID, "progress_glacier")
                new ResourceLocation(TwilightForestMod.ID, "progress_merge")
		};
	}

	@Override
	public void enforceProgession(EntityPlayer player, World world) {
		if (!world.isRemote && player.ticksExisted % 5 == 0) {
			player.attackEntityFrom(DamageSource.MAGIC, 0.5F);
			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1.0F, 1.0F);

			// hint monster?
			if (world.rand.nextInt(4) == 0) TFFeature.trollCave.trySpawnHintMonster(world, player);
		}
	}
}
