package twilightforest.biomes;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
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
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFKingSpider;
import twilightforest.entity.EntityTFKobold;
import twilightforest.entity.EntityTFMistWolf;
import twilightforest.entity.EntityTFSkeletonDruid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TFBiomeDarkForest extends TFBiomeBase {

	private static final int MONSTER_SPAWN_RATE = 20;

	private final Random monsterRNG = new Random(53439L);

	public TFBiomeDarkForest(BiomeProperties props) {
		super(props);

		getTFBiomeDecorator().canopyPerChunk = 5.5F;

		getTFBiomeDecorator().setTreesPerChunk(10);
		getTFBiomeDecorator().setGrassPerChunk(-99);
		getTFBiomeDecorator().setFlowersPerChunk(-99);
		getTFBiomeDecorator().setMushroomsPerChunk(2);
		getTFBiomeDecorator().setDeadBushPerChunk(10);

		this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 1, 1, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 5, 1, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 5, 1, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityTFMistWolf.class, 10, 1, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityTFSkeletonDruid.class, 10, 1, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityTFKingSpider.class, 10, 1, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityTFKobold.class, 10, 4, 8));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityWitch.class, 1, 1, 1));

		this.decorator.generateFalls = false;
	}

	@Override
	public TFBiomeDecorator createBiomeDecorator() {
		return new TFDarkForestBiomeDecorator();
	}

	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random random) {
		if (random.nextInt(5) == 0) {
			return new WorldGenShrub(
					Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE),
					Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, false)
			);
		} else if (random.nextInt(8) == 0) {
			return this.birchGen;
		} else {
			return TREE_FEATURE;
		}
	}

	@Override
	public int getGrassColorAtPos(BlockPos pos) {
		double temperature = (double) MathHelper.clamp(this.getTemperature(pos), 0.0F, 1.0F);
		double humidity = (double) MathHelper.clamp(this.getRainfall(), 0.0F, 1.0F);
		return ((ColorizerGrass.getGrassColor(temperature, humidity) & 0xFEFEFE) + 0x1E0E4E) / 2;
	}

	@Override
	public int getFoliageColorAtPos(BlockPos pos) {
		double temperature = (double) MathHelper.clamp(this.getTemperature(pos), 0.0F, 1.0F);
		double humidity = (double) MathHelper.clamp(this.getRainfall(), 0.0F, 1.0F);
		return ((ColorizerFoliage.getFoliageColor(temperature, humidity) & 0xFEFEFE) + 0x1E0E4E) / 2;
	}

	@Override
	public List<SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
		// if it is monster, then only give it the real list 1/MONSTER_SPAWN_RATE of the time
		if (creatureType == EnumCreatureType.MONSTER) {
			return monsterRNG.nextInt(MONSTER_SPAWN_RATE) == 0 ? this.spawnableMonsterList : new ArrayList<>();
		}
		return super.getSpawnableList(creatureType);
	}

	@Override
	public boolean isHighHumidity() {
		return true;
	}

	@Override
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[]{ TwilightForestMod.prefix("progress_lich") };
	}

	@Override
	public void enforceProgression(EntityPlayer player, World world) {
		if (!world.isRemote && player.ticksExisted % 60 == 0) {
			player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0));
			trySpawnHintMonster(player, world);
		}
	}

	@Override
	protected TFFeature getContainedFeature() {
		return TFFeature.KNIGHT_STRONGHOLD;
	}
}
