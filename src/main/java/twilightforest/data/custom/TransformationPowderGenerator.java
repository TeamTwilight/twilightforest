package twilightforest.data.custom;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import quek.undergarden.registry.UGEntityTypes;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;

public class TransformationPowderGenerator extends TransformationPowderProvider{

	public TransformationPowderGenerator(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, TwilightForestMod.ID, helper);
	}

	@Override
	public void registerTransforms() {
		addTwoWayTransform(TFEntities.MINOTAUR.get(), EntityType.ZOMBIFIED_PIGLIN);
		addTwoWayTransform(TFEntities.DEER.get(), EntityType.COW);
		addTwoWayTransform(TFEntities.BOAR.get(), EntityType.PIG);
		addTwoWayTransform(TFEntities.BIGHORN_SHEEP.get(), EntityType.SHEEP);
		addTwoWayTransform(TFEntities.DWARF_RABBIT.get(), EntityType.RABBIT);
		addTwoWayTransform(TFEntities.TINY_BIRD.get(), EntityType.PARROT);
		addTwoWayTransform(TFEntities.RAVEN.get(), EntityType.BAT);
		addTwoWayTransform(TFEntities.HOSTILE_WOLF.get(), EntityType.WOLF);
		addTwoWayTransform(TFEntities.PENGUIN.get(), EntityType.CHICKEN);
		addTwoWayTransform(TFEntities.HEDGE_SPIDER.get(), EntityType.SPIDER);
		addTwoWayTransform(TFEntities.SWARM_SPIDER.get(), EntityType.CAVE_SPIDER);
		addTwoWayTransform(TFEntities.WRAITH.get(), EntityType.VEX);
		addTwoWayTransform(TFEntities.SKELETON_DRUID.get(), EntityType.WITCH);
		addTwoWayTransform(TFEntities.CARMINITE_GHASTGUARD.get(), EntityType.GHAST);
		addTwoWayTransform(TFEntities.TOWERWOOD_BORER.get(), EntityType.SILVERFISH);
		addTwoWayTransform(TFEntities.MAZE_SLIME.get(), EntityType.SLIME);

		addTwoWayTransform(UGEntityTypes.GWIBLING.get(), EntityType.COD);
		addTwoWayTransform(UGEntityTypes.STONEBORN.get(), EntityType.VILLAGER);
		addTwoWayTransform(UGEntityTypes.ROTWALKER.get(), EntityType.ZOMBIE);
	}
}
