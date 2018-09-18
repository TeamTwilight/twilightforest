package twilightforest.biomes;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;
import twilightforest.entity.EntityTFMosquitoSwarm;
import twilightforest.world.feature.TFGenHugeLilyPad;
import twilightforest.world.feature.TFGenHugeWaterLily;
import twilightforest.world.feature.TFGenTallGrass;
import twilightforest.world.feature.TFGenVines;
import twilightforest.world.TFWorld;

import java.util.List;
import java.util.Random;

public class TFBiomeSwamp extends TFBiomeBase {

	private static final int MONSTER_SPAWN_RATE = 20;
	private Random monsterRNG = new Random(53439L);

	private TFGenVines worldgenvines = new TFGenVines();
	private WorldGenerator hugeLilyPadGen = new TFGenHugeLilyPad();
	private WorldGenerator hugeWaterLilyGen = new TFGenHugeWaterLily();

	public TFBiomeSwamp(BiomeProperties props) {
		super(props);

		getTFBiomeDecorator().setDeadBushPerChunk(1);
		getTFBiomeDecorator().setMushroomsPerChunk(8);
		getTFBiomeDecorator().setReedsPerChunk(10);
		getTFBiomeDecorator().setClayPerChunk(1);
		getTFBiomeDecorator().setTreesPerChunk(2);
		getTFBiomeDecorator().setWaterlilyPerChunk(20);


		getTFBiomeDecorator().hasCanopy = false;
		getTFBiomeDecorator().lakesPerChunk = 2;
		getTFBiomeDecorator().mangrovesPerChunk = 3;

		this.spawnableMonsterList.add(new SpawnListEntry(EntityTFMosquitoSwarm.class, 10, 1, 1));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 10, 4, 4));
	}

	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random random) {
		if (random.nextInt(3) == 0) {
			new WorldGenShrub(
					Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE),
					Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK));
		}

		return SWAMP_FEATURE;
	}

	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random random) {
		if (random.nextInt(4) == 0) {
			return new WorldGenTallGrass(BlockTallGrass.EnumType.FERN);
		} else if (random.nextInt(4) == 0) {
			return new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MAYAPPLE));
		} else {
			return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
		}
	}

	@Override
	public void decorate(World world, Random random, BlockPos pos) {
		super.decorate(world, random, pos);

		for (int i = 0; i < 50; i++) {
			int j = pos.getX() + random.nextInt(16) + 8;
			int l = TFWorld.SEALEVEL + 128;
			int k = pos.getZ() + random.nextInt(16) + 8;
			worldgenvines.generate(world, random, new BlockPos(j, l, k));
		}
		for (int i = 0; i < 25; i++) {
			int x = pos.getX() + random.nextInt(15) + 8;
			int y = TFWorld.SEALEVEL;
			int z = pos.getZ() + random.nextInt(15) + 8;
			hugeLilyPadGen.generate(world, random, new BlockPos(x, y, z));
		}
		for (int i = 0; i < 2; i++) {
			int x = pos.getX() + random.nextInt(16) + 8;
			int y = TFWorld.SEALEVEL;
			int z = pos.getZ() + random.nextInt(16) + 8;
			hugeWaterLilyGen.generate(world, random, new BlockPos(x, y, z));
		}
	}

	@Override
	public int getGrassColorAtPos(BlockPos pos) {
		double temperature = (double) MathHelper.clamp(this.getTemperature(pos), 0.0F, 1.0F);
		double humidity = (double) MathHelper.clamp(this.getRainfall(), 0.0F, 1.0F);
		return ((ColorizerGrass.getGrassColor(temperature, humidity) & 0xFEFEFE) + 0x4E0E4E) / 2;
	}

	@Override
	public int getFoliageColorAtPos(BlockPos pos) {
		double temperature = (double) MathHelper.clamp(this.getTemperature(pos), 0.0F, 1.0F);
		double humidity = (double) MathHelper.clamp(this.getRainfall(), 0.0F, 1.0F);
		return ((ColorizerFoliage.getFoliageColor(temperature, humidity) & 0xFEFEFE) + 0x4E0E4E) / 2;
	}

	@Override
	public List<SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
		// if it is monster, then only give it the real list 1/MONSTER_SPAWN_RATE of the time
		if (creatureType == EnumCreatureType.MONSTER) {
			return monsterRNG.nextInt(MONSTER_SPAWN_RATE) == 0 ? this.spawnableMonsterList : Lists.newArrayList();
		}
		return super.getSpawnableList(creatureType);
	}

	@Override
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[]{ new ResourceLocation(TwilightForestMod.ID, "progress_lich") };
	}

	@Override
	public void enforceProgession(EntityPlayer player, World world) {
		if (!world.isRemote && player.ticksExisted % 60 == 0) {
			PotionEffect currentHunger = player.getActivePotionEffect(MobEffects.HUNGER);

			int hungerLevel = currentHunger != null ? currentHunger.getAmplifier() + 1 : 1;

			player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 100, hungerLevel));

			// hint monster?
			if (world.rand.nextInt(4) == 0) {
				TFFeature.LABYRINTH.trySpawnHintMonster(world, player);
			}
		}
	}
}
