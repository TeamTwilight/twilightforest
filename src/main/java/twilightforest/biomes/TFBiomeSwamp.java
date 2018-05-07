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
import twilightforest.world.TFGenHugeLilyPad;
import twilightforest.world.TFGenTallGrass;
import twilightforest.world.TFGenVines;
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


		((TFBiomeDecorator) decorator).hasCanopy = false;
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
	public WorldGenerator getRandomWorldGenForGrass(Random par1Random) {
		if (par1Random.nextInt(4) == 0) {
			return new WorldGenTallGrass(BlockTallGrass.EnumType.FERN);
		} else if (par1Random.nextInt(4) == 0) {
			return new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MAYAPPLE));
		} else {
			return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
		}
	}

	@Override
	public void decorate(World par1World, Random par2Random, BlockPos pos) {
		super.decorate(par1World, par2Random, pos);

		for (int i = 0; i < 50; i++) {
			int j = pos.getX() + par2Random.nextInt(16) + 8;
			int l = TFWorld.SEALEVEL + 128;
			int k = pos.getZ() + par2Random.nextInt(16) + 8;
			worldgenvines.generate(par1World, par2Random, new BlockPos(j, l, k));
		}
		for (int i = 0; i < 25; i++) {
			int x = pos.getX() + par2Random.nextInt(15) + 8;
			int y = TFWorld.SEALEVEL;
			int z = pos.getZ() + par2Random.nextInt(15) + 8;
			hugeLilyPadGen.generate(par1World, par2Random, new BlockPos(x, y, z));
		}
		for (int i = 0; i < 2; i++) {
			int x = pos.getX() + par2Random.nextInt(16) + 8;
			int y = TFWorld.SEALEVEL;
			int z = pos.getZ() + par2Random.nextInt(16) + 8;
			hugeWaterLilyGen.generate(par1World, par2Random, new BlockPos(x, y, z));
		}
	}

	@Override
	public int getGrassColorAtPos(BlockPos pos) {
		double var1 = (double) MathHelper.clamp(this.getFloatTemperature(pos), 0.0F, 1.0F);
		double var3 = (double) MathHelper.clamp(this.getRainfall(), 0.0F, 1.0F);
		return ((ColorizerGrass.getGrassColor(var1, var3) & 0xFEFEFE) + 0x4E0E4E) / 2;
	}

	@Override
	public int getFoliageColorAtPos(BlockPos pos) {
		double var1 = (double) MathHelper.clamp(this.getFloatTemperature(pos), 0.0F, 1.0F);
		double var3 = (double) MathHelper.clamp(this.getRainfall(), 0.0F, 1.0F);
		return ((ColorizerFoliage.getFoliageColor(var1, var3) & 0xFEFEFE) + 0x4E0E4E) / 2;
	}


	@Override
	public List<SpawnListEntry> getSpawnableList(EnumCreatureType par1EnumCreatureType) {
		// if is is monster, then only give it the real list 1/MONSTER_SPAWN_RATE of the time
		if (par1EnumCreatureType == EnumCreatureType.MONSTER) {
			return monsterRNG.nextInt(MONSTER_SPAWN_RATE) == 0 ? this.spawnableMonsterList : Lists.newArrayList();
		} else {
			return par1EnumCreatureType == EnumCreatureType.CREATURE ? this.spawnableCreatureList : (par1EnumCreatureType == EnumCreatureType.WATER_CREATURE ? this.spawnableWaterCreatureList : (par1EnumCreatureType == EnumCreatureType.AMBIENT ? this.spawnableCaveCreatureList : null));
		}
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
				TFFeature.labyrinth.trySpawnHintMonster(world, player);
			}
		}
	}
}
