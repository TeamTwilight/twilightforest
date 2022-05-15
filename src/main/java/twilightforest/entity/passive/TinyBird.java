package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import twilightforest.TFSounds;
import twilightforest.entity.ai.TinyBirdFlyGoal;

public class TinyBird extends Bird {

	private static final EntityDataAccessor<Byte> DATA_BIRDTYPE = SynchedEntityData.defineId(TinyBird.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> DATA_BIRDFLAGS = SynchedEntityData.defineId(TinyBird.class, EntityDataSerializers.BYTE);

	// [VanillaCopy] EntityBat field
	private BlockPos spawnPosition;
	private int currentFlightTime;

	public TinyBird(EntityType<? extends TinyBird> type, Level world) {
		super(type, world);
		setBirdType(random.nextInt(4));
		setIsBirdLanded(true);
		setAge(0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.5F));
		this.goalSelector.addGoal(2, new TinyBirdFlyGoal(this));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0F, SEEDS, true));
		this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Cat.class, 8.0F, 1.0D, 1.25D));
		this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Ocelot.class, 8.0F, 1.0D, 1.25D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_BIRDTYPE, (byte) 0);
		entityData.define(DATA_BIRDFLAGS, (byte) 0);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 1.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.20000001192092896D);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("BirdType", this.getBirdType());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setBirdType(compound.getInt("BirdType"));
	}

	public int getBirdType() {
		return entityData.get(DATA_BIRDTYPE);
	}

	public void setBirdType(int type) {
		entityData.set(DATA_BIRDTYPE, (byte) type);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.TINYBIRD_CHIRP;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.TINYBIRD_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.TINYBIRD_HURT;
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getBbHeight() * 0.7F;
	}

	@Override
	public boolean removeWhenFarAway(double p_213397_1_) {
		return false;
	}

	@Override
	public float getWalkTargetValue(BlockPos pos) {
		// prefer standing on leaves
		Material underMaterial = this.level.getBlockState(pos.below()).getMaterial();
		if (underMaterial == Material.LEAVES) {
			return 200.0F;
		}
		if (underMaterial == Material.WOOD) {
			return 15.0F;
		}
		if (underMaterial == Material.GRASS) {
			return 9.0F;
		}
		// default to just preferring lighter areas
		return this.level.getMaxLocalRawBrightness(pos) - 0.5F;
	}

	@Override
	public void tick() {
		super.tick();
		// while we are flying, try to level out somewhat
		if (!this.isBirdLanded()) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0F, 0.6D, 1.0F));
		}
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

		BlockPos blockpos = this.blockPosition();
		if (this.isBirdLanded()) {
			this.currentFlightTime = 0;

			boolean flag = this.isSilent();
			if (this.isSpooked() || this.isInWater() || level.containsAnyLiquid(getBoundingBox()) || (this.random.nextInt(200) == 0 && !this.isLandableBlock(this.blockPosition().below()))) {
				this.setIsBirdLanded(false);
				if (!flag) {
					this.playSound(TFSounds.TINYBIRD_TAKEOFF, 0.05F, this.getVoicePitch());
				}
			}
		} else {
			this.currentFlightTime++;

			// [VanillaCopy] Modified version of last half of Bat.customServerAiStep(). Edits noted
			if (this.spawnPosition != null && (!this.level.isEmptyBlock(this.spawnPosition) || this.spawnPosition.getY() <= this.level.getMinBuildHeight())) {
				this.spawnPosition = null;
			}

			//TF - no drowning birds
			if (isInWater() || level.containsAnyLiquid(getBoundingBox())) {
				currentFlightTime = 0; // reset timer for MAX FLIGHT :v
				this.setDeltaMovement(this.getDeltaMovement().x(), 0.1F, this.getDeltaMovement().z());
			}

			if (this.spawnPosition == null || this.random.nextInt(30) == 0 || this.spawnPosition.closerToCenterThan(this.position(), 2.0D)) {
				// TF - modify shift factor of Y
				int yTarget = this.currentFlightTime < 100 ? 2 : 4;
				this.spawnPosition = new BlockPos(
						this.getX() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7),
						this.getY() + (double)this.random.nextInt(6) - yTarget,
						this.getZ() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7));
			}

			double d2 = (double)this.spawnPosition.getX() + 0.5D - this.getX();
			double d0 = (double)this.spawnPosition.getY() + 0.1D - this.getY();
			double d1 = (double)this.spawnPosition.getZ() + 0.5D - this.getZ();
			Vec3 vec3 = this.getDeltaMovement();
			Vec3 vec31 = vec3.add((Math.signum(d2) * 0.5D - vec3.x) * (double)0.1F, (Math.signum(d0) * (double)0.7F - vec3.y) * (double)0.1F, (Math.signum(d1) * 0.5D - vec3.z) * (double)0.1F);
			this.setDeltaMovement(vec31);
			float f = (float)(Mth.atan2(vec31.z, vec31.x) * (double)(180F / (float)Math.PI)) - 90.0F;
			float f1 = Mth.wrapDegrees(f - this.getYRot());
			this.zza = 0.5F;
			this.setYRot(this.getYRot() + f1);
			// TF - change chance 100 -> 10; change check to isLandable
			if (this.random.nextInt(10) == 0 && this.isLandableBlock(this.blockPosition().below())) {
				//TF - land the bird
				this.setIsBirdLanded(true);
				this.setDeltaMovement(this.getDeltaMovement().multiply(1.0F, 0.0F, 1.0F));
			}
			// End copy
		}
	}

	public boolean isSpooked() {
		if (this.hurtTime > 0) return true;
		Player closestPlayer = this.level.getNearestPlayer(this, 4.0D);
		return closestPlayer != null
				&& !SEEDS.test(closestPlayer.getMainHandItem())
				&& !SEEDS.test(closestPlayer.getOffhandItem());
	}

	public boolean isLandableBlock(BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		return !state.isAir()
				&& (state.is(BlockTags.LEAVES) || state.isFaceSturdy(level, pos, Direction.UP));
	}

	@Override
	public boolean isBirdLanded() {
		return (entityData.get(DATA_BIRDFLAGS) & 1) != 0;
	}

	public void setIsBirdLanded(boolean landed) {
		byte flags = entityData.get(DATA_BIRDFLAGS);
		entityData.set(DATA_BIRDFLAGS, (byte) (landed ? flags | 1 : flags & ~1));
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void doPush(Entity entity) {
	}

	@Override
	public boolean isIgnoringBlockTriggers() {
		return true;
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	protected void pushEntities() {
	}

	@Override
	public boolean isBaby() {
		return false;
	}
}
