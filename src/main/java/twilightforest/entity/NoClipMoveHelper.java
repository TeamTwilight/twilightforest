package twilightforest.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

public class NoClipMoveHelper extends EntityMoveHelper {
	private final EntityLiving parentEntity;
	private int courseChangeCooldown;

	public NoClipMoveHelper(EntityLiving entity) {
		super(entity);
		this.parentEntity = entity;
	}

	@Override
	public void onUpdateMoveHelper() {
		if (this.action == EntityMoveHelper.Action.MOVE_TO) {
			double d0 = this.posX - this.parentEntity.posX;
			double d1 = this.posY - this.parentEntity.posY;
			double d2 = this.posZ - this.parentEntity.posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;

			if (this.courseChangeCooldown-- <= 0) {
				this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
				d3 = (double) MathHelper.sqrt(d3);

				this.parentEntity.motionX += (d0 / d3 * 0.1D) * speed;
				this.parentEntity.motionY += (d1 / d3 * 0.1D) * speed;
				this.parentEntity.motionZ += (d2 / d3 * 0.1D) * speed;
			}
		}
	}
}
