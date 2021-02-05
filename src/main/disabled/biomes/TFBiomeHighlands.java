package twilightforest.worldgen.biomes;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;

public class TFBiomeHighlands extends TFBiomeBase {

	public TFBiomeHighlands(Builder props) {
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
		TFBiomeDecorator.addTorchberries(this);
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.HIGHLANDS_TREES_CONFIG, 10);
		TFBiomeDecorator.addFernWithGrass(this, 7);
		TFBiomeDecorator.addTallFerns(this, 7);
		TFBiomeDecorator.addDeadBushes(this, 16);
		TFBiomeDecorator.addMossyBoulders(this);
		TFBiomeDecorator.addTrollRoots(this);
		TFBiomeDecorator.addFlowers(this, 2);
		TFBiomeDecorator.addMushgloom(this, 1);
		TFBiomeDecorator.addMushrooms(this);
	}

	@Override
	public void addSpawns() {
		super.addSpawns();

		undergroundMonsterList.clear();
		undergroundMonsterList.add(new SpawnListEntry(EntityType.SKELETON, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.CREEPER, 1, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.SLIME, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(TFEntities.troll, 10, 4, 4));
	}

	@Override
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[] {
                //TwilightForestMod.prefix("progress_hydra"),
                //TwilightForestMod.prefix("progress_ur_ghast"),
                //TwilightForestMod.prefix("progress_glacier")
				TwilightForestMod.prefix("progress_merge")
		};
	}

	@Override
	public void enforceProgression(PlayerEntity player, World world) {
		if (!world.isRemote && player.ticksExisted % 5 == 0) {
			player.attackEntityFrom(DamageSource.MAGIC, 0.5F);
			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1.0F, 1.0F);
			trySpawnHintMonster(player, world);
		}
	}

//	@Override
//	protected TFFeature getContainedFeature() {
//		return TFFeature.TROLL_CAVE;
//	}
}
