package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;

public class RisingZombie extends Monster {

	private static final EntityDataAccessor<Integer> RISING_TICKS = SynchedEntityData.defineId(RisingZombie.class, EntityDataSerializers.INT);

	public RisingZombie(EntityType<? extends RisingZombie> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(RISING_TICKS, 0);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (RISING_TICKS.equals(key)) {
			this.refreshDimensions();
		}
		super.onSyncedDataUpdated(key);
	}

	@Override
	protected EntityDimensions getDefaultDimensions(Pose pose) {
		return super.getDefaultDimensions(pose).scale(1.0F, this.getRisingTicks() / 130.0F);
	}

	@Override
	public boolean isInvisible() {
		return this.getRisingTicks() == 0;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.getRisingTicks() > 0 && !this.isDeadOrDying()) {
			BlockPos pos = this.blockPosition().below();
			BlockState state = this.level().getBlockState(pos);
			if (!this.level().isClientSide()) {
				this.getEntityData().set(RISING_TICKS, this.getRisingTicks() + 1);
				if (this.getRisingTicks() % 10 == 0 && this.getRisingTicks() < 130) {
					this.level().playSound(null, this.blockPosition(), state.getSoundType(this.level(), pos, null).getBreakSound(), SoundSource.BLOCKS, 1.0F, this.getRandom().nextFloat() * 0.15F + 0.7F);
				}
			} else {
				if (!this.level().isEmptyBlock(this.blockPosition().below())) {
					double px = this.getX() + this.getRandom().nextDouble() - 0.5D;
					double py = this.getY() + this.getRandom().nextDouble() * 0.2D + 0.075D;
					double pz = this.getZ() + this.getRandom().nextDouble() - 0.5D;
					for (int i = 0, amount = this.getRandom().nextInt(10) + 5; i < amount; i++) {
						double ox = this.getRandom().nextDouble() * 0.1D - 0.05D;
						double oz = this.getRandom().nextDouble() * 0.1D - 0.05D;
						double motionX = this.getRandom().nextDouble() * 0.2D - 0.1D;
						double motionY = this.getRandom().nextDouble() * 0.25D + 0.1D;
						double motionZ = this.getRandom().nextDouble() * 0.2D - 0.1D;
						this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), px + ox, py, pz + oz, motionX, motionY, motionZ);
					}
				}
			}
		} else if (this.tickCount % 10 == 0) {
			var player = this.level().getNearestPlayer(this, this.getAttributeValue(Attributes.FOLLOW_RANGE) / 2);
			if (player != null && this.isLookingInMyDirection(player, 0.5D, false, true, this.getEyeY(), this.getY() + 0.5D * (double)this.getScale(), (this.getEyeY() + this.getY()) / 2.0D)) {
				this.getEntityData().set(RISING_TICKS, 1);
			}
		}

		if (!this.level().isClientSide() && this.getRisingTicks() >= 130) {
			var zombie = this.convertTo(EntityType.ZOMBIE, true);
			zombie.setHealth(this.getHealth());
			zombie.setYRot(this.yRotO = this.getYRot());
		}
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source.is(DamageTypes.IN_WALL);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ZOMBIE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ZOMBIE_DEATH;
	}

	//TODO replace with LivingEntity.isLookingAtMe in 1.21.4+
	public boolean isLookingInMyDirection(Player player, double width, boolean useLength, boolean checkAir, double... offsets) {
		Vec3 vec3 = player.getViewVector(1.0F).normalize();

		for (double yOffs : offsets) {
			Vec3 vec31 = new Vec3(this.getX() - player.getX(), yOffs - player.getEyeY(), this.getZ() - player.getZ());
			double d1 = vec31.length();
			vec31 = vec31.normalize();
			double d2 = vec3.dot(vec31);
			if (d2 > 1.0 - width / (useLength ? d1 : 1.0D) && this.hasLineOfSight(player, checkAir ? ClipContext.Block.VISUAL : ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, yOffs)) {
				return true;
			}
		}

		return false;
	}

	public boolean hasLineOfSight(Player player, ClipContext.Block blockClip, ClipContext.Fluid fluidClip, double yOffs) {
		if (player.level() != this.level()) {
			return false;
		} else {
			Vec3 vec3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
			Vec3 vec31 = new Vec3(player.getX(), yOffs, player.getZ());
			return !(vec31.distanceTo(vec3) > 128.0) && this.level().clip(new ClipContext(vec3, vec31, blockClip, fluidClip, this)).getType() == HitResult.Type.MISS;
		}
	}

	public int getRisingTicks() {
		return this.getEntityData().get(RISING_TICKS);
	}

	@Override
	protected boolean isImmobile() {
		return true;
	}

	@Override
	public void knockback(double strength, double xRatio, double zRatio) {

	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void pushEntities() {

	}

	@Override
	protected void doPush(Entity entity) {

	}

	@Override
	protected boolean isAffectedByFluids() {
		return false;
	}

	@Override
	public boolean isPushedByFluid(FluidType type) {
		return false;
	}

	@Override
	public boolean canUsePortal(boolean force) {
		return false;
	}
}
