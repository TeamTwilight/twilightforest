package twilightforest.entity.monster;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;
import twilightforest.entity.TFEntities;

import javax.annotation.Nullable;

public class KingSpider extends Spider {

	public KingSpider(EntityType<? extends KingSpider> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		//this.goalSelector.addGoal(1, new EntityAITFChargeAttack(this, 0.4F));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Spider.createAttributes()
				.add(Attributes.MAX_HEALTH, 30.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.35D)
				.add(Attributes.ATTACK_DAMAGE, 6.0D);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
	      return TFSounds.KING_SPIDER_AMBIENT;
	   }

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	      return TFSounds.KING_SPIDER_HURT;
	   }

	@Override
	protected SoundEvent getDeathSound() {
	      return TFSounds.KING_SPIDER_DEATH;
	   }

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
	      this.playSound(TFSounds.KING_SPIDER_STEP, 0.15F, 1.0F);
	   }

	@Override
	public boolean onClimbable() {
		// let's not climb
		return false;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingData, @Nullable CompoundTag dataTag) {
		livingData = super.finalizeSpawn(worldIn, difficulty, reason, livingData, dataTag);

		// will always have a dryad riding the spider or whatever is riding the spider
		SkeletonDruid druid = TFEntities.SKELETON_DRUID.create(level);
		druid.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
		druid.finalizeSpawn(worldIn, difficulty, MobSpawnType.JOCKEY, null, null);
		Entity lastRider = this;
		while (!lastRider.getPassengers().isEmpty())
			lastRider = lastRider.getPassengers().get(0);
		druid.startRiding(lastRider);

		return livingData;
	}

	@Override
	public double getPassengersRidingOffset() {
		return this.getBbHeight() * 0.75D;
	}
}
