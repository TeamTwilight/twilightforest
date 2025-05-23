package twilightforest.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.boss.UrGhast;

import java.util.EnumSet;

//[VanillaCopy] of Ghast.GhastLookGoal
public class UrGhastLookGoal extends Goal {
	private final UrGhast ghast;

	public UrGhastLookGoal(UrGhast pGhast) {
		this.ghast = pGhast;
		this.setFlags(EnumSet.of(Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return true;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		if (this.ghast.getTarget() == null) {
			Vec3 vec3 = this.ghast.getDeltaMovement();
			this.ghast.setYRot(-((float) Mth.atan2(vec3.x(), vec3.z())) * (180.0F / (float) Math.PI));
			this.ghast.yBodyRot = this.ghast.getYRot();
		} else {
			LivingEntity livingentity = this.ghast.getTarget();
			if (livingentity.distanceToSqr(this.ghast) < 4096.0) {
				double d1 = livingentity.getX() - this.ghast.getX();
				double d2 = livingentity.getZ() - this.ghast.getZ();
				this.ghast.setYRot(-((float) Mth.atan2(d1, d2)) * (180.0F / (float) Math.PI));
				this.ghast.yBodyRot = this.ghast.getYRot();
			}
		}
	}
}
