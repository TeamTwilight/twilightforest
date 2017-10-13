package twilightforest.entity.ai;

import net.minecraft.block.BlockTNT;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import twilightforest.entity.EntityTFRedcap;

public class EntityAITFRedcapLightTNT extends EntityAITFRedcapBase {
	private float pursueSpeed;
	private int delay;
	private BlockPos tntPos = null;

	public EntityAITFRedcapLightTNT(EntityTFRedcap hostEntity, float speed) {
		super(hostEntity);
		this.pursueSpeed = speed;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (this.delay > 0) {
			--this.delay;
			return false;
		}

		BlockPos nearbyTNT = this.findBlockTNTNearby(8);
		if (nearbyTNT != null) {
			this.tntPos = nearbyTNT;
			return true;
		}

		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return entityObj.world.getBlockState(tntPos).getBlock() == Blocks.TNT;
	}

	@Override
	public void startExecuting() {
		this.entityObj.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, entityObj.heldFlint);
	}

	@Override
	public void resetTask() {
		this.entityObj.getNavigator().clearPath();
		this.entityObj.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, entityObj.heldPick);
		this.delay = 20;
		this.tntPos = null;
	}

	@Override
	public void updateTask() {
		this.entityObj.getLookHelper().setLookPosition(tntPos.getX(), tntPos.getY(), tntPos.getZ(), 30.0F, this.entityObj.getVerticalFaceSpeed());

		if (this.entityObj.getDistanceSq(tntPos) < 2.4D * 2.4D) {
			entityObj.playLivingSound();

			Blocks.TNT.onBlockDestroyedByPlayer(entityObj.world, tntPos, Blocks.TNT.getDefaultState().withProperty(BlockTNT.EXPLODE, true));
			entityObj.swingArm(EnumHand.MAIN_HAND);
			entityObj.world.setBlockState(tntPos, Blocks.AIR.getDefaultState(), 2);
			this.entityObj.getNavigator().clearPath();
		} else {
			this.entityObj.getNavigator().tryMoveToXYZ(tntPos.getX(), tntPos.getY(), tntPos.getZ(), this.pursueSpeed);
		}
	}

}
