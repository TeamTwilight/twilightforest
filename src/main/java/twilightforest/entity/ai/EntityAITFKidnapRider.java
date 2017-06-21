package twilightforest.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAITFKidnapRider extends EntityAIBase {
	private EntityCreature theEntityCreature;
	private float speed;
	private double randPosX;
	private double randPosY;
	private double randPosZ;

	public EntityAITFKidnapRider(EntityCreature par1EntityCreature, float par2) {
		this.theEntityCreature = par1EntityCreature;
		this.speed = par2;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (this.theEntityCreature.getRidingEntity() == null || this.theEntityCreature.getRNG().nextInt(5) > 0) {
			return false;
		} else {
			Vec3d var1 = RandomPositionGenerator.findRandomTarget(this.theEntityCreature, 5, 4);

			if (var1 == null) {
				return false;
			} else {
				this.randPosX = var1.xCoord;
				this.randPosY = var1.yCoord;
				this.randPosZ = var1.zCoord;
				return true;
			}
		}
	}

	@Override
	public void startExecuting() {
		this.theEntityCreature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !this.theEntityCreature.getNavigator().noPath();
	}
}
