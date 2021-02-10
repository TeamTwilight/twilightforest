package twilightforest.worldgen.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;
import twilightforest.potions.TFPotions;

import java.util.Random;

/**
 * @author Ben
 */
public class TFBiomeSnow extends TFBiomeBase {

	private static final int MONSTER_SPAWN_RATE = 10;

	private final Random monsterRNG = new Random(53439L);

	public TFBiomeSnow(Builder props) {
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
		TFBiomeDecorator.addPlantRoots(this);
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.SNOWY_TREES_CONFIG, 7);
		TFBiomeDecorator.addTorchberries(this);
		TFBiomeDecorator.addGrassWithFern(this, 1);
		TFBiomeDecorator.addFlowers(this, 2);
		TFBiomeDecorator.addMushrooms(this);
	}

	@Override
	public void addSpawns() {
		super.addSpawns();

		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TFEntities.yeti, 20, 4, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TFEntities.winter_wolf, 5, 1, 4));
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
			player.addPotionEffect(new EffectInstance(TFPotions.frosty.get(), 100, 2));
			trySpawnHintMonster(player, world);
		}
	}

//	@Override
//	protected TFFeature getContainedFeature() {
//		return TFFeature.YETI_CAVE;
//	}
}
