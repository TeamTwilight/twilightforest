package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TFSounds;

public class EntityTFHelmetCrab extends MonsterEntity {

	public EntityTFHelmetCrab(EntityType<? extends EntityTFHelmetCrab> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.28F));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(6, new RandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 13.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.28D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D)
				.createMutableAttribute(Attributes.ARMOR, 6.0D);
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getHeight() * 0.4F;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.HELMET_CRAB_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.HELMET_CRAB_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		this.playSound(TFSounds.HELMET_CRAB_STEP, 0.15F, 1.0F);
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ARTHROPOD;
	}
}
