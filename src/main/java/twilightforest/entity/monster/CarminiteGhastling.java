package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFSounds;

import java.util.Objects;

public class CarminiteGhastling extends CarminiteGhastguard {

	private boolean isMinion = false;

	public CarminiteGhastling(EntityType<? extends CarminiteGhastling> type, Level world) {
		super(type, world);
		this.wanderFactor = 4.0F;

		if (this.isMinion() && this.getAttribute(Attributes.MAX_HEALTH) != null) {
			Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(6);
			this.setHealth(this.getMaxHealth());
		}
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 16;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return CarminiteGhastguard.registerAttributes()
			.add(Attributes.MAX_HEALTH, 10)
			.add(Attributes.FOLLOW_RANGE, 16.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.CARMINITE_GHASTLING_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.CARMINITE_GHASTLING_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.CARMINITE_GHASTLING_DEATH.get();
	}

	@Override
	public SoundEvent getFireSound() {
		return TFSounds.CARMINITE_GHASTLING_SHOOT.get();
	}

	@Override
	public SoundEvent getWarnSound() {
		return TFSounds.CARMINITE_GHASTLING_WARN.get();
	}

	// Loosely based on EnderMan.isLookingAtMe
	@Override
	public boolean shouldAttack(LivingEntity living) {
		ItemStack helmet = living.getItemBySlot(EquipmentSlot.HEAD);
		if (!helmet.isEmpty() && helmet.is(Items.PUMPKIN)) {
			return false;
		} else if (living.distanceTo(this) <= 3.5F) {
			return living.hasLineOfSight(this);
		} else {
			Vec3 vec3d = living.getViewVector(1.0F).normalize();
			Vec3 vec3d1 = new Vec3(this.getX() - living.getX(), this.getBoundingBox().minY + this.getEyeHeight() - (living.getY() + living.getEyeHeight()), this.getZ() - living.getZ());
			double d0 = vec3d1.length();
			vec3d1 = vec3d1.normalize();
			double d1 = vec3d.dot(vec3d1);
			return d1 > 1.0D - 0.025D / d0 && living.hasLineOfSight(this);
		}
	}

	//This does not factor into whether the entity is a Minion or not. However, since it is spawned via MOB_SUMMONED, it will always spawn if that is the SpawnReason
	public static boolean checkGhastlingSpawnRules(EntityType<CarminiteGhastling> entity, ServerLevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && (reason == EntitySpawnReason.MOB_SUMMONED || Monster.isDarkEnoughToSpawn(world, pos, random)) && checkMobSpawnRules(entity, world, reason, pos, random);
	}

	public void makeBossMinion() {
		this.wanderFactor = 0.005F;
		this.isMinion = true;

		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(6);
		this.setHealth(this.getMaxHealth());
	}

	public boolean isMinion() {
		return this.isMinion;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putBoolean("isMinion", this.isMinion);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.getBoolean("isMinion")) {
			this.makeBossMinion();
		}
	}

	//prevents ghastlings from triggering the traps on accident
	@Override
	public boolean isIgnoringBlockTriggers() {
		return true;
	}
}
