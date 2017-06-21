package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntitySeekerArrow extends EntityArrow {
	private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(EntitySeekerArrow.class, DataSerializers.VARINT);
	private static final double seekDistance = 5.0;

	public EntitySeekerArrow(World par1World) {
		super(par1World);
	}

	public EntitySeekerArrow(World world, EntityPlayer player) {
		super(world, player);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(TARGET, -1);
	}

	@Override
	public void onUpdate() {
		if (isThisArrowFlying()) {
			if (!world.isRemote) {
				updateTarget();
			}

			if (world.isRemote && !inGround) {
				for (int k = 0; k < 4; ++k) {
					this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.motionX * (double) k / 4.0D, this.posY + this.motionY * (double) k / 4.0D, this.posZ + this.motionZ * (double) k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
				}
			}

			// TODO this is a bit wonky
			if (getTarget() != null) {
				Entity target = getTarget();
				Vec3d targetVec = new Vec3d(this.posX - target.posX, this.posY - (target.posY + target.getEyeHeight()), this.posZ - target.posZ);
				targetVec = targetVec.normalize();

				Vec3d courseVec = new Vec3d(this.motionX * seekDistance, this.motionY * seekDistance, this.motionZ * seekDistance);
				courseVec = courseVec.normalize();

				double dotProduct = courseVec.dotProduct(targetVec);
				//System.out.println("target vec compared to course vec= " + dotProduct);

				if (dotProduct < 0) {

					// match current speed
					float currentSpeed = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);

					currentSpeed *= 1.0;

					targetVec = targetVec.scale(currentSpeed);

					// adjust current heading
					double dx = MathHelper.clamp(targetVec.xCoord, -2.0, 2.0);
					double dy = MathHelper.clamp(targetVec.yCoord, -1.0, 1.0);
					double dz = MathHelper.clamp(targetVec.zCoord, -2.0, 2.0);

					this.motionX -= dx;
					this.motionY -= dy;
					this.motionZ -= dz;
				} else if (!world.isRemote) {
					setTarget(null);
				}

				this.motionY += 0.045F;
			}
		}

		super.onUpdate();
	}

	private void updateTarget() {
		if (getTarget() != null && getTarget().isDead) {
			setTarget(null);
		}

		if (getTarget() == null) {
			AxisAlignedBB targetBB = new AxisAlignedBB(lastTickPosX, lastTickPosY, lastTickPosZ, lastTickPosX, lastTickPosY, lastTickPosZ);

			// add two possible courses to our selection box
			Vec3d courseVec = new Vec3d(this.motionX * seekDistance, this.motionY * seekDistance, this.motionZ * seekDistance).rotateYaw((float) (Math.PI / 6F));
			targetBB = targetBB.addCoord(courseVec.xCoord, courseVec.yCoord, courseVec.zCoord);

			courseVec = new Vec3d(this.motionX * seekDistance, this.motionY * seekDistance, this.motionZ * seekDistance).rotateYaw(-(float) (Math.PI / 6F));
			targetBB = targetBB.addCoord(courseVec.xCoord, courseVec.yCoord, courseVec.zCoord).expand(0, 3, 0);

			double closestDot = 1;

			for (EntityLivingBase living : this.world.getEntitiesWithinAABB(EntityLivingBase.class, targetBB)) {
				if (!(living instanceof EntityPlayer)) {
					courseVec = new Vec3d(this.motionX, this.motionY, this.motionZ).normalize();
					Vec3d targetVec = new Vec3d(this.posX - living.posX, this.posY - (living.posY + (double) living.getEyeHeight()), this.posZ - living.posZ);

					//double d0 = targetVec.lengthVector(); // do we need this?
					targetVec = targetVec.normalize();
					double dot = courseVec.dotProduct(targetVec);

					if (dot < closestDot) {
						setTarget(living);
						closestDot = dot;
					}
				}
			}
		}
	}

	private Entity getTarget() {
		return world.getEntityByID(dataManager.get(TARGET));
	}

	private void setTarget(Entity e) {
		dataManager.set(TARGET, e == null ? -1 : e.getEntityId());
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(Items.ARROW);
	}

	private boolean isThisArrowFlying() {
		return MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ) > 1.0;
	}

}
