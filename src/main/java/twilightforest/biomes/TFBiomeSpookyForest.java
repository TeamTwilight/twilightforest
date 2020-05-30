package twilightforest.biomes;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import twilightforest.entity.TFEntities;

public class TFBiomeSpookyForest extends TFBiomeBase {

	public TFBiomeSpookyForest(Builder props) {
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
		TFBiomeDecorator.addTorchberries(this);
		TFBiomeDecorator.addCanopyDead(this);
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.NORMAL_TREES_CONFIG, 2);
		TFBiomeDecorator.addPumpkins(this, 2);
		TFBiomeDecorator.addGrassWithFern(this, 4);
		TFBiomeDecorator.addFlowers(this, 1);
		TFBiomeDecorator.addMushgloom(this, 1);
		TFBiomeDecorator.addMushrooms(this);
		TFBiomeDecorator.addDeadBushes(this, 2);
		TFBiomeDecorator.addWebs(this);
		TFBiomeDecorator.addLamppost(this, Blocks.JACK_O_LANTERN.getDefaultState(), 2);
		TFBiomeDecorator.addFallenLeaves(this);
		TFBiomeDecorator.addGraveyards(this);
	}

	@Override
	public void addSpawns() {
		super.addSpawns();

		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.BAT, 20, 8, 8));

		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.SPIDER, 50, 1, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.SKELETON, 20, 1, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TFEntities.skeleton_druid.get(), 5, 1, 1));
	}

	@Override
	public int getGrassColorAt(double p_225528_1_, double p_225528_3_) {
		return 0xC45123;
	}

	@Override
	public int getFoliageColor() {
		return 0xFF8501;
	}
}
