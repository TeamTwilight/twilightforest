package twilightforest.entity.ai;

import net.minecraft.block.BlockTNT;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.ForgeEventFactory;
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
		if (!ForgeEventFactory.getMobGriefingEvent(redcap.world, redcap)) {
			return false;
		}

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
		return redcap.world.getBlockState(tntPos).getBlock() == Blocks.TNT;
	}

	@Override
	public void startExecuting() {
		this.redcap.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, redcap.heldFlint);
	}

	@Override
	public void resetTask() {
		this.redcap.getNavigator().clearPath();
		this.redcap.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, redcap.heldPick);
		this.delay = 20;
		this.tntPos = null;
	}

	@Override
	public void updateTask() {
		this.redcap.getLookHelper().setLookPosition(tntPos.getX(), tntPos.getY(), tntPos.getZ(), 30.0F, this.redcap.getVerticalFaceSpeed());

		if (this.redcap.getDistanceSq(tntPos) < 2.4D * 2.4D) {
			redcap.playLivingSound();

			Blocks.TNT.onPlayerDestroy(redcap.world, tntPos, Blocks.TNT.getDefaultState().withProperty(BlockTNT.EXPLODE, true));
			redcap.swingArm(EnumHand.MAIN_HAND);
			redcap.world.setBlockState(tntPos, Blocks.AIR.getDefaultState(), 2);
			this.redcap.getNavigator().clearPath();
		} else {
			this.redcap.getNavigator().tryMoveToXYZ(tntPos.getX(), tntPos.getY(), tntPos.getZ(), this.pursueSpeed);
		}
	}
}
