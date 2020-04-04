package twilightforest.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFMobileFirefly extends AmbientEntity {
	private BlockPos spawnPosition;

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/mobile_firefly");

	public EntityTFMobileFirefly(EntityType<? extends EntityTFMobileFirefly> type, World world) {
		super(type, world);
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
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
	}

	@Override
	public void tick() {
		super.tick();

		Vec3d motion = getMotion();
		this.setMotion(motion.x, motion.y * 0.6000000238418579D, motion.z);
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();

		// [VanillaCopy] direct from last half of EntityBat.updateAITasks
		if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
			this.spawnPosition = null;
		}

		// TODO: True adds 0.5
		if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((double) ((int) this.getX()), (double) ((int) this.getY()), (double) ((int) this.getZ()), false) < 4.0D) {
			this.spawnPosition = new BlockPos((int) this.getX() + this.rand.nextInt(7) - this.rand.nextInt(7), (int) this.getY() + this.rand.nextInt(6) - 2, (int) this.getZ() + this.rand.nextInt(7) - this.rand.nextInt(7));
		}

		double d0 = (double) this.spawnPosition.getX() + 0.5D - this.getX();
		double d1 = (double) this.spawnPosition.getY() + 0.1D - this.getY();
		double d2 = (double) this.spawnPosition.getZ() + 0.5D - this.getZ();
		this.setMotion(this.getMotion().add(new Vec3d(
				(Math.signum(d0) * 0.5D - this.getMotion().getX()) * 0.10000000149011612D,
				(Math.signum(d1) * 0.699999988079071D - this.getMotion().getY()) * 0.10000000149011612D,
				(Math.signum(d2) * 0.5D - this.getMotion().getZ()) * 0.10000000149011612D
		)));
		float f = (float) (MathHelper.atan2(this.getMotion().getZ(), this.getMotion().getX()) * (180D / Math.PI)) - 90.0F;
		float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
		this.moveForward = 0.5F;
		this.rotationYaw += f1;
		// End copy
	}

	@Override
	public boolean bypassesSteppingEffects() {
		return false;
	}

	@Override
	public boolean handleFallDamage(float dist, float mult) {
		return false;
	}

	@Override
	protected void updateFallState(double y, boolean onGround, BlockState state, BlockPos pos) {
	}

	@Override
	public boolean doesEntityNotTriggerPressurePlate() {
		return true;
	}

	// [VanillaCopy] EntityBat.getCanSpawnHere. Edits noted.
	// TODO: These are now moved to spawn predicates
/*	@Override
	public boolean getCanSpawnHere() {
		BlockPos blockpos = new BlockPos(this.getX(), this.getBoundingBox().minY, this.getZ());

		return blockpos.getY() < this.world.getSeaLevel()
				&& !this.rand.nextBoolean()
				&& this.world.getLightFromNeighbors(blockpos) <= this.rand.nextInt(4)
				&& super.getCanSpawnHere();
	}*/

	//TODO: I believe this is done via the Renderer now
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public int getBrightnessForRender() {
//		return 15728880;
//	}

	public float getGlowBrightness() {
		return (float) Math.sin(this.ticksExisted / 7.0) + 1F;
	}

	@Override
	public float getBrightness() {
		return getGlowBrightness();
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
