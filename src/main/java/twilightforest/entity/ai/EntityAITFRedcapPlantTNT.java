package twilightforest.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import twilightforest.entity.EntityTFRedcap;

public class EntityAITFRedcapPlantTNT extends EntityAITFRedcapBase {
	public EntityAITFRedcapPlantTNT(EntityTFRedcap entityTFRedcap) {
		super(entityTFRedcap);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase attackTarget = this.entityObj.getAttackTarget();
		return attackTarget != null
				&& !entityObj.heldTNT.isEmpty()
				&& entityObj.getDistanceSq(attackTarget) < 25
				&& !isTargetLookingAtMe(attackTarget)
				&& !isLitTNTNearby(8)
				&& findBlockTNTNearby(5) == null;
	}

	@Override
	public void startExecuting() {
		BlockPos entityPos = new BlockPos(entityObj);

		this.entityObj.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, entityObj.heldTNT);

		if (this.entityObj.world.isAirBlock(entityPos)) {
			entityObj.heldTNT.shrink(1);
			entityObj.playLivingSound();
			entityObj.world.setBlockState(entityPos, Blocks.TNT.getDefaultState());
		}
	}

	@Override
	public void resetTask() {
		this.entityObj.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, entityObj.heldPick);
	}
}
