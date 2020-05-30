package twilightforest.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;
import twilightforest.potions.TFPotions;

/**
 * @author Ben
 */
public class TFBiomeGlacier extends TFBiomeBase {

	public TFBiomeGlacier(Builder props) {
		super(props);
	}

	@Override
	public void addFeatures() {
		super.addFeatures();

		TFBiomeDecorator.addWoodRoots(this);
		TFBiomeDecorator.addOres(this);
		TFBiomeDecorator.addClayDisks(this, 1);
		TFBiomeDecorator.addLakes(this);
		TFBiomeDecorator.addPlantRoots(this);
		TFBiomeDecorator.addTorchberries(this);
		TFBiomeDecorator.addPenguins(this);
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.GLACIER_TREES_CONFIG, 1);
		TFBiomeDecorator.addMushrooms(this);
	}

	@Override
	public void addSpawns() {
		super.addSpawns();

		getSpawns(EntityClassification.CREATURE).clear();
		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(TFEntities.penguin.get(), 10, 4, 4));
	}

	@Override
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[]{ TwilightForestMod.prefix("progress_yeti") };
	}

	@Override
	public void enforceProgression(PlayerEntity player, World world) {
		if (!world.isRemote && player.ticksExisted % 60 == 0) {
			player.addPotionEffect(new EffectInstance(TFPotions.frosty.get(), 100, 3));
		}
		trySpawnHintMonster(player, world);
	}

//	@Override
//	protected TFFeature getContainedFeature() {
//		return TFFeature.ICE_TOWER;
//	}
}
