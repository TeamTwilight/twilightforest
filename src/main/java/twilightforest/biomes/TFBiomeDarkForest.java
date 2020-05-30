package twilightforest.biomes;

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
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.entity.*;

import java.util.Random;

public class TFBiomeDarkForest extends TFBiomeBase {

	private static final int MONSTER_SPAWN_RATE = 20;

	private final Random monsterRNG = new Random(53439L);

	public TFBiomeDarkForest(Builder props) {
		super(props);
	}

	@Override
	public void addFeatures() {
		super.addFeatures();

		TFBiomeDecorator.addOres(this);
		TFBiomeDecorator.addClayDisks(this, 1);
		TFBiomeDecorator.addLakes(this);
		TFBiomeDecorator.addSprings(this);
		TFBiomeDecorator.addWoodRoots(this);
		TFBiomeDecorator.addPlantRoots(this);
		TFBiomeDecorator.addDarkTrees(this);
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.DARK_FOREST_TREES_CONFIG, 10);
		TFBiomeDecorator.addTorchberries(this);
		TFBiomeDecorator.addMushroomsDark(this);
		TFBiomeDecorator.addMushrooms(this);
		TFBiomeDecorator.addDeadBushes(this, 2);
		TFBiomeDecorator.addForestGrass(this, 10);
		TFBiomeDecorator.addPumpkins(this, 32);
	}

	@Override
	public void addSpawns() {
		super.addSpawns();

		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.ENDERMAN, 1, 1, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.ZOMBIE, 5, 1, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.SKELETON, 5, 1, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TFEntities.mist_wolf.get(), 10, 1, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TFEntities.skeleton_druid.get(), 10, 1, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TFEntities.king_spider.get(), 10, 1, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TFEntities.kobold.get(), 10, 4, 8));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.WITCH, 1, 1, 1));
	}

	@Override
	public int getGrassColorAt(double p_225528_1_, double p_225528_3_) {
		double temperature = (double) MathHelper.clamp(this.getDefaultTemperature(), 0.0F, 1.0F);
		double humidity = (double) MathHelper.clamp(this.getDownfall(), 0.0F, 1.0F);
		return ((GrassColors.get(temperature, humidity) & 0xFEFEFE) + 0x1E0E4E) / 2;
	}

	@Override
	public int getFoliageColor() {
		double temperature = (double) MathHelper.clamp(this.getDefaultTemperature(), 0.0F, 1.0F);
		double humidity = (double) MathHelper.clamp(this.getDownfall(), 0.0F, 1.0F);
		return ((FoliageColors.get(temperature, humidity) & 0xFEFEFE) + 0x1E0E4E) / 2;
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
	public boolean isHighHumidity() {
		return true;
	}

	@Override
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[]{ TwilightForestMod.prefix("progress_lich") };
	}

	@Override
	public void enforceProgression(PlayerEntity player, World world) {
		if (!world.isRemote && player.ticksExisted % 60 == 0) {
			player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 0));
			trySpawnHintMonster(player, world);
		}
	}

//	@Override
//	protected TFFeature getContainedFeature() {
//		return TFFeature.KNIGHT_STRONGHOLD;
//	}
}
