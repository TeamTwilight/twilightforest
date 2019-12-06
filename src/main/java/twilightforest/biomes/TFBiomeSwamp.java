package twilightforest.biomes;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.GrassColors;
import net.minecraft.world.World;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TFBiomeSwamp extends TFBiomeBase {

	private static final int MONSTER_SPAWN_RATE = 20;

	private final Random monsterRNG = new Random(53439L);

	private final WorldGenerator vinesGen = new TFGenVines();
	private final WorldGenerator hugeLilyPadGen = new TFGenHugeLilyPad();
	private final WorldGenerator hugeWaterLilyGen = new TFGenHugeWaterLily();

	public TFBiomeSwamp(Builder props) {
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
		this.spawnableMonsterList.add(new SpawnListEntry(EntityType.CREEPER, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityType.ZOMBIE, 10, 4, 4));
	}

    //TODO: Move to feature decorator
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random random) {
		if (random.nextInt(3) == 0) {
			return new WorldGenShrub(
					Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE),
					Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, false)
			);
		} else {
			return SWAMP_FEATURE;
		}
	}

    //TODO: Move to feature decorator
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

    //TODO: Move to feature decorator
	@Override
	public void decorate(World world, Random random, BlockPos pos) {
		super.decorate(world, random, pos);

		for (int i = 0; i < 50; i++) {
			int x = pos.getX() + random.nextInt(16) + 8;
			int y = TFWorld.SEALEVEL + 128;
			int z = pos.getZ() + random.nextInt(16) + 8;
			vinesGen.generate(world, random, new BlockPos(x, y, z));
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
	public int getGrassColor(BlockPos pos) {
		double temperature = (double) MathHelper.clamp(this.getTemperature(pos), 0.0F, 1.0F);
		double humidity = (double) MathHelper.clamp(this.getDownfall(), 0.0F, 1.0F);
		return ((GrassColors.get(temperature, humidity) & 0xFEFEFE) + 0x4E0E4E) / 2;
	}

	@Override
	public int getFoliageColor(BlockPos pos) {
		double temperature = (double) MathHelper.clamp(this.getTemperature(pos), 0.0F, 1.0F);
		double humidity = (double) MathHelper.clamp(this.getDownfall(), 0.0F, 1.0F);
		return ((FoliageColors.get(temperature, humidity) & 0xFEFEFE) + 0x4E0E4E) / 2;
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
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[]{ TwilightForestMod.prefix("progress_lich") };
	}

	@Override
	public void enforceProgression(PlayerEntity player, World world) {
		if (!world.isRemote && player.ticksExisted % 60 == 0) {
			EffectInstance currentHunger = player.getActivePotionEffect(Effects.HUNGER);

			int hungerLevel = currentHunger != null ? currentHunger.getAmplifier() + 1 : 1;

			player.addPotionEffect(new EffectInstance(Effects.HUNGER, 100, hungerLevel));

			trySpawnHintMonster(player, world);
		}
	}

	@Override
	protected TFFeature getContainedFeature() {
		return TFFeature.LABYRINTH;
	}
}
