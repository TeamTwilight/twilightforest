package twilightforest.entity.ai;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.Redcap;

public abstract class RedcapBaseGoal extends Goal {

	protected final Redcap redcap;

	protected RedcapBaseGoal(Redcap entity) {
		this.redcap = entity;
	}

	/**
	 * Fairly straightforward.  Returns true in a 120 degree arc in front of the target's view.
	 */
	public boolean isTargetLookingAtMe(LivingEntity attackTarget) {
		// find angle of approach
		double dx = redcap.getX() - attackTarget.getX();
		double dz = redcap.getZ() - attackTarget.getZ();
		float angle = (float) ((Math.atan2(dz, dx) * 180D) / Math.PI) - 90F;

		float difference = Mth.abs((attackTarget.getYRot() - angle) % 360);

		return difference < 60 || difference > 300;
	}

	public BlockPos findBlockTNTNearby(int range) {
		BlockPos entityPos = new BlockPos(redcap.blockPosition());

		for (int x = -range; x <= range; x++) {
			for (int y = -range; y <= range; y++) {
				for (int z = -range; z <= range; z++) {
					if (redcap.level.getBlockState(entityPos.offset(x, y, z)).getBlock() == Blocks.TNT) {
						return entityPos.offset(x, y, z);
					}
				}
			}
		}

		return null;
	}

	public boolean isLitTNTNearby(int range) {
		AABB expandedBox = redcap.getBoundingBox().inflate(range, range, range);
		return !redcap.level.getEntitiesOfClass(PrimedTnt.class, expandedBox).isEmpty();
	}
}
