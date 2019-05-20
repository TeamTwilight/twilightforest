package twilightforest.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.entity.EntityTFRedcap;

public class EntityAITFRedcapPlantTNT extends EntityAITFRedcapBase {

	public EntityAITFRedcapPlantTNT(EntityTFRedcap entityTFRedcap) {
		super(entityTFRedcap);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase attackTarget = this.redcap.getAttackTarget();
		return attackTarget != null
				&& !redcap.heldTNT.isEmpty()
				&& redcap.getDistanceSq(attackTarget) < 25
				&& !isTargetLookingAtMe(attackTarget)
				&& ForgeEventFactory.getMobGriefingEvent(redcap.world, redcap)
				&& !isLitTNTNearby(8)
				&& findBlockTNTNearby(5) == null;
	}

	@Override
	public void startExecuting() {
		BlockPos entityPos = new BlockPos(redcap);

		this.redcap.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, redcap.heldTNT);

		if (this.redcap.world.isAirBlock(entityPos)) {
			redcap.heldTNT.shrink(1);
			redcap.playLivingSound();
			redcap.world.setBlockState(entityPos, Blocks.TNT.getDefaultState());
		}
	}

	@Override
	public void resetTask() {
		this.redcap.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, redcap.heldPick);
	}
}
