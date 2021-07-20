package twilightforest.entity.ai;

import net.minecraft.block.Blocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.entity.EntityTFRedcap;

import java.util.EnumSet;

public class EntityAITFRedcapLightTNT extends EntityAITFRedcapBase {

	private float pursueSpeed;
	private int delay;
	private BlockPos tntPos = null;

	public EntityAITFRedcapLightTNT(EntityTFRedcap hostEntity, float speed) {
		super(hostEntity);
		this.pursueSpeed = speed;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
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
		this.redcap.setItemStackToSlot(EquipmentSlotType.MAINHAND, redcap.heldFlint);
	}

	@Override
	public void resetTask() {
		this.redcap.getNavigator().clearPath();
		this.redcap.setItemStackToSlot(EquipmentSlotType.MAINHAND, redcap.heldPick);
		this.delay = 20;
		this.tntPos = null;
	}

	@Override
	public void tick() {
		this.redcap.getLookController().setLookPosition(tntPos.getX(), tntPos.getY(), tntPos.getZ(), 30.0F, this.redcap.getVerticalFaceSpeed());

		if (this.redcap.getDistanceSq(Vector3d.copy(tntPos)) < 2.4D * 2.4D) {
			redcap.playAmbientSound();

			Blocks.TNT.catchFire(Blocks.TNT.getDefaultState(), redcap.world, tntPos, Direction.UP, redcap);
			redcap.swingArm(Hand.MAIN_HAND);
			redcap.world.setBlockState(tntPos, Blocks.AIR.getDefaultState(), 2);
			this.redcap.getNavigator().clearPath();
		} else {
			this.redcap.getNavigator().tryMoveToXYZ(tntPos.getX(), tntPos.getY(), tntPos.getZ(), this.pursueSpeed);
		}
	}
}
