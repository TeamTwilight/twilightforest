package twilightforest.biomes;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFWinterWolf;
import twilightforest.entity.EntityTFYeti;
import twilightforest.potions.TFPotions;
import twilightforest.world.feature.TFGenLargeWinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Ben
 */
public class TFBiomeSnow extends TFBiomeBase {

	private static final int MONSTER_SPAWN_RATE = 10;

	private final Random monsterRNG = new Random(53439L);

	public TFBiomeSnow(BiomeProperties props) {
		super(props);

		getTFBiomeDecorator().setTreesPerChunk(7);
		getTFBiomeDecorator().setGrassPerChunk(1);

		getTFBiomeDecorator().hasCanopy = false;
		getTFBiomeDecorator().generateFalls = false;

		this.spawnableMonsterList.add(new SpawnListEntry(EntityTFYeti.class, 20, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityTFWinterWolf.class, 5, 1, 4));
	}

	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random random) {
		if (random.nextInt(3) == 0) {
			return new WorldGenTaiga1();
		} else if (random.nextInt(8) == 0) {
			return new TFGenLargeWinter();
		} else {
			return new WorldGenTaiga2(true);
		}
	}

	@Override
	public boolean getEnableSnow() {
		return true;
	}

	@Override
	public boolean canRain() {
		return false;
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
	public void enforceProgression(EntityPlayer player, World world) {
		if (!world.isRemote && player.ticksExisted % 60 == 0) {
			player.addPotionEffect(new PotionEffect(TFPotions.frosty, 100, 2));

			// hint monster?
			if (world.rand.nextInt(4) == 0) {
				TFFeature.YETI_CAVE.trySpawnHintMonster(world, player);
			}
		}
	}
}
