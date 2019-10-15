package twilightforest.tileentity.spawner;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import twilightforest.entity.EntityTFKobold;
import twilightforest.entity.boss.EntityTFYetiAlpha;

public class TileEntityTFFinalBossSpawner extends TileEntityTFBossSpawner {

	public TileEntityTFFinalBossSpawner() {
		super(EntityList.getKey(EntityTFKobold.class));
	}

	@Override
	protected void initializeCreature(EntityLiving myCreature) {
		super.initializeCreature(myCreature);
		myCreature.setCustomNameTag("Final Boss");
		myCreature.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1024);
		myCreature.setHealth(myCreature.getMaxHealth());
	}
}
