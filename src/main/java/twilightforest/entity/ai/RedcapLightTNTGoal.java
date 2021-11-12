package twilightforest.entity.ai;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.entity.monster.Redcap;

import java.util.EnumSet;

public class RedcapLightTNTGoal extends RedcapBaseGoal {

	private float pursueSpeed;
	private int delay;
	private BlockPos tntPos = null;

	public RedcapLightTNTGoal(Redcap hostEntity, float speed) {
		super(hostEntity);
		this.pursueSpeed = speed;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (!ForgeEventFactory.getMobGriefingEvent(redcap.level, redcap)) {
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
	public boolean canContinueToUse() {
		return redcap.level.getBlockState(tntPos).getBlock() == Blocks.TNT;
	}

	@Override
	public void start() {
		this.redcap.setItemSlot(EquipmentSlot.MAINHAND, redcap.heldFlint);
	}

	@Override
	public void stop() {
		this.redcap.getNavigation().stop();
		this.redcap.setItemSlot(EquipmentSlot.MAINHAND, redcap.heldPick);
		this.delay = 20;
		this.tntPos = null;
	}

	@Override
	public void tick() {
		this.redcap.getLookControl().setLookAt(tntPos.getX(), tntPos.getY(), tntPos.getZ(), 30.0F, this.redcap.getMaxHeadXRot());

		if (this.redcap.distanceToSqr(Vec3.atLowerCornerOf(tntPos)) < 2.4D * 2.4D) {
			redcap.playAmbientSound();

			Blocks.TNT.catchFire(Blocks.TNT.defaultBlockState(), redcap.level, tntPos, Direction.UP, redcap);
			redcap.swing(InteractionHand.MAIN_HAND);
			redcap.level.setBlock(tntPos, Blocks.AIR.defaultBlockState(), 2);
			this.redcap.getNavigation().stop();
		} else {
			this.redcap.getNavigation().moveTo(tntPos.getX(), tntPos.getY(), tntPos.getZ(), this.pursueSpeed);
		}
	}
}
