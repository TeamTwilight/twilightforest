package twilightforest.biomes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.EntityTFPenguin;
import twilightforest.potions.TFPotions;
import twilightforest.world.feature.TFGenPenguins;
import twilightforest.world.TFWorld;

import java.util.Random;

/**
 * @author Ben
 */
public class TFBiomeGlacier extends TFBiomeBase {

	public TFBiomeGlacier(Builder props) {
		super(props);

		getTFBiomeDecorator().setTreesPerChunk(1);
		getTFBiomeDecorator().setGrassPerChunk(0);
		getTFBiomeDecorator().hasCanopy = false;

		spawnableCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(EntityTFPenguin.class, 10, 4, 4));
	}

    //TODO: Move to feature decorator
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random random) {
		if (random.nextInt(3) == 0) {
			return new WorldGenTaiga1();
		} else {
			return new WorldGenTaiga2(true);
		}
	}

	@Override
	public boolean getEnableSnow() {
		return true;
	}

	/**
	 * Required for actual snow?
	 */
	@Override
	public boolean canRain() {
		return false;
	}

    //TODO: Move to feature decorator
	@Override
	public void decorate(World world, Random random, BlockPos pos) {
		super.decorate(world, random, pos);

		WorldGenerator penguins = new TFGenPenguins();
		if (random.nextInt(4) == 0) {
			int x = pos.getX() + random.nextInt(16) + 8;
			int y = TFWorld.SEALEVEL;
			int z = pos.getZ() + random.nextInt(16) + 8;
			penguins.generate(world, random, new BlockPos(x, y, z));
		}
	}

	@Override
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[]{ TwilightForestMod.prefix("progress_yeti") };
	}

	@Override
	public void enforceProgression(PlayerEntity player, World world) {
		if (!world.isRemote && player.ticksExisted % 60 == 0) {
			player.addPotionEffect(new EffectInstance(TFPotions.frosty, 100, 3));
		}
		trySpawnHintMonster(player, world);
	}

	@Override
	protected TFFeature getContainedFeature() {
		return TFFeature.ICE_TOWER;
	}
}
