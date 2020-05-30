package twilightforest.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;

public class TFBiomeFinalPlateau extends TFBiomeBase {

	public TFBiomeFinalPlateau(Builder props) {
		super(props);
	}

	@Override
	public void addFeatures() {
		super.addFeatures();

		TFBiomeDecorator.addClayDisks(this, 1);
		TFBiomeDecorator.addLakes(this);
		TFBiomeDecorator.addRuins(this);
		TFBiomeDecorator.addMushrooms(this);
	}

	@Override
	public void addSpawns() {
		super.addSpawns();

		// custom creature list.
		getSpawns(EntityClassification.CREATURE).clear();
		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(TFEntities.raven.get(), 10, 4, 4));
	}

	@Override
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[]{ TwilightForestMod.prefix("progress_troll") };
	}

	@Override
	public void enforceProgression(PlayerEntity player, World world) {
		if (!world.isRemote && player.ticksExisted % 5 == 0) {
			player.attackEntityFrom(DamageSource.MAGIC, 1.5F);
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1.0F, 1.0F);
			// TODO: change this when there's a book for the castle
			if (world.rand.nextInt(4) == 0) TFFeature.TROLL_CAVE.trySpawnHintMonster(world, player);
		}
	}

//	@Override
//	protected TFFeature getContainedFeature() {
//		return TFFeature.FINAL_CASTLE;
//	}
}
