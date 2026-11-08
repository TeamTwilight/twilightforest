package twilightforest.entity.ai.control;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.monster.CarminiteGhastguard;

/**
 * [VanillaCopy] of {@link net.minecraft.world.entity.monster.Ghast.GhastMoveControl} with adjustments noted.
 */
public class GhastguardMoveControl extends MoveControl {
	// TF: Removed redundant ghast field
	private int floatDuration;
	// TF: Removed the careful field
	// TF: Removed the shouldBeStopped field

	public GhastguardMoveControl(CarminiteGhastguard ghast) {
		super(ghast);
	}

	// TF: Removed happy ghast logic and simplified method
	public void tick() {
		// TF: Dampen motion while engaged with a target
		if (this.mob.getTarget() != null) {
			this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.75D));
			return;
		}

		if (this.operation == Operation.MOVE_TO && this.floatDuration-- <= 0) {
			this.floatDuration = this.floatDuration + this.mob.getRandom().nextInt(20) + 20; // TF: Shorter interval between movement
			Vec3 travel = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
			if (this.canReach(travel)) {
				this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(travel.normalize().scale(this.mob.getAttributeValue(Attributes.FLYING_SPEED) * 5.0D / 3.0D)));
			} else {
				this.operation = Operation.WAIT;
			}
		}
	}

	// TF: Removed happy ghast logic and simplified method
	private boolean canReach(Vec3 travel) {
		AABB aabb = this.mob.getBoundingBox();
		AABB aabbAtDestination = aabb.move(travel);
		Vec3 start = this.mob.position();
		Vec3 end = start.add(travel);
		return BlockGetter.forEachBlockIntersectedBetween(start, end, aabbAtDestination, (blockPos, _) -> aabb.intersects(blockPos) || this.blockTraversalPossible(this.mob.level(), start, end, blockPos));
	}

	// TF: Removed happy ghast logic and simplified method
	private boolean blockTraversalPossible(BlockGetter level, Vec3 start, Vec3 end, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (state.isAir())
			return true;

		return !this.mob.collidedWithShapeMovingFrom(start, end, state.getCollisionShape(level, pos).move(new Vec3(pos)).toAabbs());
	}
}
