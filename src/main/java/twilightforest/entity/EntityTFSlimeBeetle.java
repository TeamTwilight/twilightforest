package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.entity.projectile.EntityTFSlimeProjectile;

public class EntityTFSlimeBeetle extends MonsterEntity implements IRangedAttackMob {

	public EntityTFSlimeBeetle(EntityType<? extends EntityTFSlimeBeetle> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 3.0F, 1.25F, 2.0F));
		this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1, 30, 10));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 25.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 4);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.SLIME_BEETLE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.SLIME_BEETLE_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		playSound(TFSounds.SLIME_BEETLE_STEP, 0.15F, 1.0F);
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return 0.25F;
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ARTHROPOD;
	}

	@Override
	public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
		ThrowableEntity projectile = new EntityTFSlimeProjectile(TFEntities.slime_blob, this.world, this);
		playSound(TFSounds.SLIME_BEETLE_SQUISH_SMALL, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		double tx = target.getPosX() - this.getPosX();
		double ty = target.getPosY() + target.getEyeHeight() - 1.100000023841858D - projectile.getPosY();
		double tz = target.getPosZ() - this.getPosZ();
		float heightOffset = MathHelper.sqrt(tx * tx + tz * tz) * 0.2F;
		projectile.shoot(tx, ty + heightOffset, tz, 0.6F, 6.0F);
		this.world.addEntity(projectile);
	}
}
