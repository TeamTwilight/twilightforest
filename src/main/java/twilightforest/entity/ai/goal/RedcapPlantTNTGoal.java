package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.event.EventHooks;
import twilightforest.entity.monster.Redcap;

public class RedcapPlantTNTGoal extends RedcapBaseGoal {

	public RedcapPlantTNTGoal(Redcap redcap) {
		super(redcap);
	}

	@Override
	public boolean canUse() {
		LivingEntity attackTarget = this.redcap.getTarget();
		return attackTarget != null
			&& !this.redcap.heldTNT.isEmpty()
			&& this.redcap.distanceToSqr(attackTarget) < 25
			&& !this.isTargetLookingAtMe(attackTarget)
			&& EventHooks.canEntityGrief(getServerLevel(this.redcap), this.redcap)
			&& !this.isLitTNTNearby(8)
			&& this.findBlockTNTNearby(5) == null;
	}

	@Override
	public void start() {
		BlockPos entityPos = new BlockPos(this.redcap.blockPosition());

		this.redcap.setItemSlot(EquipmentSlot.MAINHAND, this.redcap.heldTNT);

		if (this.redcap.level().isEmptyBlock(entityPos)) {
			this.redcap.heldTNT.shrink(1);
			this.redcap.playAmbientSound();
			this.redcap.level().setBlockAndUpdate(entityPos, Blocks.TNT.defaultBlockState());
			this.redcap.gameEvent(GameEvent.BLOCK_PLACE);
		}
	}

	@Override
	public void stop() {
		this.redcap.setItemSlot(EquipmentSlot.MAINHAND, this.redcap.heldPick);
	}
}
