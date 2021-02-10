package twilightforest.worldgen.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.GrassColors;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;

import java.util.Random;

public class TFBiomeSwamp extends TFBiomeBase {

	private static final int MONSTER_SPAWN_RATE = 20;

	private final Random monsterRNG = new Random(53439L);

	public TFBiomeSwamp(Builder props) {
		super(props);
	}

	@Override
	public void addFeatures() {
		super.addFeatures();

		TFBiomeDecorator.addWoodRoots(this);
		TFBiomeDecorator.addOres(this);
		TFBiomeDecorator.addClayDisks(this, 1);
		TFBiomeDecorator.addLakes(this);
		TFBiomeDecorator.addRuins(this);
		TFBiomeDecorator.addSprings(this);
		TFBiomeDecorator.addPlantRoots(this);
		TFBiomeDecorator.addExtraPoolsWater(this, 2);
		TFBiomeDecorator.addMangroves(this, 3);
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.SWAMP_TREES_CONFIG, 2);
		TFBiomeDecorator.addTorchberries(this);
		TFBiomeDecorator.addTwilightGrass(this, TFBiomeDecorator.SWAMP_GRASS_CONFIG, 2);
		TFBiomeDecorator.addFlowers(this, 2);
		TFBiomeDecorator.addMushroomsSometimes(this);
		TFBiomeDecorator.addDeadBushes(this, 16);
		TFBiomeDecorator.addVines(this, 50);
		TFBiomeDecorator.addWaterlilies(this, 20);
		TFBiomeDecorator.addHugeLilyPads(this);
		TFBiomeDecorator.addHugeWaterLiles(this);
		TFBiomeDecorator.addReeds(this, 10);
	}

	@Override
	public void addSpawns() {
		super.addSpawns();

		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TFEntities.mosquito_swarm, 10, 1, 1));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.CREEPER, 10, 4, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.ZOMBIE, 10, 4, 4));
	}

	@Override
	public int getGrassColor(double x, double z) {
		double temperature = (double) MathHelper.clamp(this.getDefaultTemperature(), 0.0F, 1.0F);
		double humidity = (double) MathHelper.clamp(this.getDownfall(), 0.0F, 1.0F);
		return ((GrassColors.get(temperature, humidity) & 0xFEFEFE) + 0x4E0E4E) / 2;
	}

	@Override
	public int getFoliageColor() {
		double temperature = (double) MathHelper.clamp(this.getDefaultTemperature(), 0.0F, 1.0F);
		double humidity = (double) MathHelper.clamp(this.getDownfall(), 0.0F, 1.0F);
		return ((FoliageColors.get(temperature, humidity) & 0xFEFEFE) + 0x4E0E4E) / 2;
	}

	//TODO: Figure this out
//	@Override
//	public List<SpawnListEntry> getSpawns(EntityClassification creatureType) {
//		// if it is monster, then only give it the real list 1/MONSTER_SPAWN_RATE of the time
//		if (creatureType == EntityClassification.MONSTER) {
//			return monsterRNG.nextInt(MONSTER_SPAWN_RATE) == 0 ? this.spawnableMonsterList : new ArrayList<>();
//		}
//		return super.getSpawns(creatureType);
//	}

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

//	@Override
//	protected TFFeature getContainedFeature() {
//		return TFFeature.LABYRINTH;
//	}
}
