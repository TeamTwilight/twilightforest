package twilightforest.biomes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

public class TFBiomeFireSwamp extends TFBiomeBase {

	public TFBiomeFireSwamp(Builder props) {
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
		TFBiomeDecorator.addExtraPoolsLava(this, 0.125F);
		TFBiomeDecorator.addMangroves(this, 3);
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.SWAMP_TREES_CONFIG, 3);
		TFBiomeDecorator.addTorchberries(this);
		TFBiomeDecorator.addJetsAndSmokers(this);
		TFBiomeDecorator.addGrassWithFern(this, 2);
		TFBiomeDecorator.addFlowers(this, 2);
		TFBiomeDecorator.addMushroomsSometimes(this);
		TFBiomeDecorator.addDeadBushes(this, 3);
		TFBiomeDecorator.addVines(this, 20);
		TFBiomeDecorator.addReeds(this, 4);
		TFBiomeDecorator.addWaterlilies(this, 6);
	}

	@Override
	public int getGrassColorAt(double p_225528_1_, double p_225528_3_) {
		return 0x572e23;
	}

	@Override
	public int getFoliageColor() {
		return 0x64260f;
	}

	@Override
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[]{ TwilightForestMod.prefix("progress_labyrinth") };
	}

	@Override
	public void enforceProgression(PlayerEntity player, World world) {
		if (!world.isRemote && player.ticksExisted % 60 == 0) {
			player.setFire(8);
		}
		trySpawnHintMonster(player, world);
	}

//	@Override
//	protected TFFeature getContainedFeature() {
//		return TFFeature.HYDRA_LAIR;
//	}
}