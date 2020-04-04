package twilightforest.tileentity.spawner;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.text.StringTextComponent;
import twilightforest.entity.EntityTFKobold;
import twilightforest.entity.TFEntities;
import twilightforest.tileentity.TFTileEntities;

public class TileEntityTFFinalBossSpawner extends TileEntityTFBossSpawner<EntityTFKobold> {

	public TileEntityTFFinalBossSpawner() {
		super(TFTileEntities.FINAL_BOSS_SPAWNER.get(), TFEntities.kobold.get());
	}

	@Override
	protected void initializeCreature(EntityTFKobold myCreature) {
		super.initializeCreature(myCreature);
		myCreature.setCustomName(new StringTextComponent("Final Boss"));
		myCreature.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1024);
		myCreature.setHealth(myCreature.getMaxHealth());
	}
}