package twilightforest.entity.passive;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTFMobileFirefly extends EntityAmbientCreature {
	private BlockPos spawnPosition;

	public EntityTFMobileFirefly(World world) {
		super(world);
		this.setSize(0.5F, 0.5F);
	}

	@Override
	protected float getSoundVolume() {
		return 0.1F;
	}

	@Override
	protected float getSoundPitch() {
		return super.getSoundPitch() * 0.95F;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_BAT_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_BAT_DEATH;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void collideWithEntity(Entity entity) {
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.motionY *= 0.6000000238418579D;
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();

		// [VanillaCopy] direct from last half of EntityBat.updateAITasks
		if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
			this.spawnPosition = null;
		}

		if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((double) ((int) this.posX), (double) ((int) this.posY), (double) ((int) this.posZ)) < 4.0D) {
			this.spawnPosition = new BlockPos((int) this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int) this.posY + this.rand.nextInt(6) - 2, (int) this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
		}

		double d0 = (double) this.spawnPosition.getX() + 0.5D - this.posX;
		double d1 = (double) this.spawnPosition.getY() + 0.1D - this.posY;
		double d2 = (double) this.spawnPosition.getZ() + 0.5D - this.posZ;
		this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.10000000149011612D;
		this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
		this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.10000000149011612D;
		float f = (float) (MathHelper.atan2(this.motionZ, this.motionX) * (180D / Math.PI)) - 90.0F;
		float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
		this.moveForward = 0.5F;
		this.rotationYaw += f1;
		// End copy
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public void fall(float dist, float mult) {
	}

	@Override
	protected void updateFallState(double y, boolean onGround, IBlockState state, BlockPos pos) {
	}

	@Override
	public boolean doesEntityNotTriggerPressurePlate() {
		return true;
	}

	// [VanillaCopy] EntityBat.getCanSpawnHere. Edits noted.
	@Override
	public boolean getCanSpawnHere() {
		BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

		return blockpos.getY() < this.world.getSeaLevel()
				&& !this.rand.nextBoolean()
				&& this.world.getLightFromNeighbors(blockpos) <= this.rand.nextInt(4)
				&& super.getCanSpawnHere();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender() {
		return 15728880;
	}

	public float getGlowBrightness() {
		return (float) Math.sin(this.ticksExisted / 7.0) + 1F;
	}
}
