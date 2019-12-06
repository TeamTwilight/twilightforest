package twilightforest.biomes;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;
import twilightforest.entity.EntityTFTroll;
import twilightforest.world.feature.TFGenTallGrass;
import twilightforest.world.feature.TFGenTrollRoots;

import java.util.Random;

public class TFBiomeHighlands extends TFBiomeBase {

	private final WorldGenAbstractTree taigaGen1 = new WorldGenTaiga1();
	private final WorldGenAbstractTree taigaGen2 = new WorldGenTaiga2(false);
	private final WorldGenAbstractTree megaPineGen1 = new WorldGenMegaPineTree(false, false);
	private final WorldGenAbstractTree megaPineGen2 = new WorldGenMegaPineTree(false, true);

	private final WorldGenerator genBoulder = new WorldGenBlockBlob(Blocks.MOSSY_COBBLESTONE, 0);
	private final WorldGenerator genTrollRoots = new TFGenTrollRoots();
	private final WorldGenerator worldGenMushgloom = new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MUSHGLOOM));

	public TFBiomeHighlands(Builder props) {
		super(props);

		getTFBiomeDecorator().hasCanopy = false;

		this.decorator.grassPerChunk = 7;
		this.decorator.deadBushPerChunk = 1;
		this.decorator.generateFalls = false;

		undergroundMonsterList.clear();
		undergroundMonsterList.add(new SpawnListEntry(EntityType.SKELETON, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.CREEPER, 1, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.SLIME, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityTFTroll.class, 10, 4, 4));
	}

    //TODO: Move to feature decorator
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

    //TODO: Move to feature decorator
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random random) {
		return random.nextInt(5) > 0
				? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN)
				: new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
	}

    //TODO: Move to SurfaceBuilderConfig?
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

    //TODO: Move to feature decorator
	@Override
	public void decorate(World world, Random rand, BlockPos pos) {

		// boulders
		int maxBoulders = rand.nextInt(2);
		for (int i = 0; i < maxBoulders; ++i) {
			int dx = pos.getX() + rand.nextInt(16) + 8;
			int dz = pos.getZ() + rand.nextInt(16) + 8;
			int dy = world.getHeight(dx, dz);
			genBoulder.generate(world, rand, new BlockPos(dx, dy, dz));
		}

		// giant ferns
		DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);
		for (int i = 0; i < 7; ++i) {
			int dx = pos.getX() + rand.nextInt(16) + 8;
			int dz = pos.getZ() + rand.nextInt(16) + 8;
			int dy = rand.nextInt(world.getHeight(dx, dz) + 32);
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
                //TwilightForestMod.prefix("progress_hydra"),
                //TwilightForestMod.prefix("progress_ur_ghast"),
                //TwilightForestMod.prefix("progress_glacier")
				TwilightForestMod.prefix("progress_merge")
		};
	}

	@Override
	public void enforceProgression(PlayerEntity player, World world) {
		if (!world.isRemote && player.ticksExisted % 5 == 0) {
			player.attackEntityFrom(DamageSource.MAGIC, 0.5F);
			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1.0F, 1.0F);
			trySpawnHintMonster(player, world);
		}
	}

	@Override
	protected TFFeature getContainedFeature() {
		return TFFeature.TROLL_CAVE;
	}
}
