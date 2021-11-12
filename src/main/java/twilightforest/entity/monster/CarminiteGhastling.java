package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import twilightforest.TFSounds;

import java.util.Random;

public class CarminiteGhastling extends CarminiteGhastguard {

	private boolean isMinion = false;

	public CarminiteGhastling(EntityType<? extends CarminiteGhastling> type, Level world) {
		super(type, world);
		this.wanderFactor = 4.0F;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 16;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return CarminiteGhastguard.registerAttributes()
				.add(Attributes.MAX_HEALTH,/* this.isMinion ? 6 :*/ 10) //TODO
				.add(Attributes.FOLLOW_RANGE, 16.0D);
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
		return 0.5F;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
	      return TFSounds.GHASTLING_AMBIENT;
	   }

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	      return TFSounds.GHASTLING_HURT;
	   }

	@Override
	protected SoundEvent getDeathSound() {
	      return TFSounds.GHASTLING_DEATH;
	   }

	@Override
	public SoundEvent getFireSound() {
		return TFSounds.GHASTLING_SHOOT;
	}

	@Override
	public SoundEvent getWarnSound() {
		return TFSounds.GHASTLING_WARN;
	}

	// Loosely based on EntityEnderman.shouldAttackPlayer
	@Override
	protected boolean shouldAttack(LivingEntity living) {
		ItemStack helmet = living.getItemBySlot(EquipmentSlot.HEAD);
		if (!helmet.isEmpty() && helmet.getItem() == Item.byBlock(Blocks.PUMPKIN)) {
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
	public static boolean canSpawnHere(EntityType<CarminiteGhastling> entity, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && (reason == MobSpawnType.MOB_SUMMONED || Monster.isDarkEnoughToSpawn(world, pos, random)) && checkMobSpawnRules(entity, world, reason, pos, random);
	}

	public void makeBossMinion() {
		this.wanderFactor = 0.005F;
		this.isMinion = true;

		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(6);
		this.setHealth(6);
	}

	public boolean isMinion() {
		return isMinion;
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
			makeBossMinion();
		}
	}
}
