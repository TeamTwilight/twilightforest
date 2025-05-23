package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import twilightforest.entity.monster.Redcap;

import java.util.EnumSet;

public class RedcapLightTNTGoal extends RedcapBaseGoal {

	private final float pursueSpeed;
	private int delay;
	private BlockPos tntPos = null;

	public RedcapLightTNTGoal(Redcap hostEntity, float speed) {
		super(hostEntity);
		this.pursueSpeed = speed;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (!EventHooks.canEntityGrief(getServerLevel(this.redcap), this.redcap)) {
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
		return this.redcap.level().getBlockState(this.tntPos).is(Blocks.TNT);
	}

	@Override
	public void start() {
		this.redcap.setItemSlot(EquipmentSlot.MAINHAND, this.redcap.heldFlint);
	}

	@Override
	public void stop() {
		this.redcap.getNavigation().stop();
		this.redcap.setItemSlot(EquipmentSlot.MAINHAND, this.redcap.heldPick);
		this.delay = 20;
		this.tntPos = null;
	}

	@Override
	public void tick() {
		this.redcap.getLookControl().setLookAt(this.tntPos.getX(), this.tntPos.getY(), this.tntPos.getZ(), 30.0F, this.redcap.getMaxHeadXRot());

		if (this.redcap.distanceToSqr(Vec3.atLowerCornerOf(this.tntPos)) < 2.4D * 2.4D) {
			redcap.playAmbientSound();

			Blocks.TNT.onCaughtFire(Blocks.TNT.defaultBlockState(), this.redcap.level(), this.tntPos, Direction.UP, this.redcap);
			this.redcap.swing(InteractionHand.MAIN_HAND);
			this.redcap.level().setBlock(this.tntPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
			this.redcap.gameEvent(GameEvent.PRIME_FUSE);
			this.redcap.getNavigation().stop();
		} else {
			this.redcap.getNavigation().moveTo(this.tntPos.getX(), this.tntPos.getY(), this.tntPos.getZ(), this.pursueSpeed);
		}
	}
}
