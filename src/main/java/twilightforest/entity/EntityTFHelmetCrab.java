package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.MeleeAttackGoal;
import net.minecraft.entity.ai.HurtByTargetGoal;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.LookAtGoal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFHelmetCrab extends EntityMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/helmet_crab");

	public EntityTFHelmetCrab(World world) {
		super(world);
		setSize(0.8F, 1.1F);
	}

	@Override
	protected void registerGoals() {
		this.tasks.addTask(0, new SwimGoal(this));
		this.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.28F));
		this.tasks.addTask(3, new MeleeAttackGoal(this, 1.0D, false));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(7, new LookAtGoal(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new LookRandomlyGoal(this));
		this.targetTasks.addTask(1, new HurtByTargetGoal(this, true));
		this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(13.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6.0D);
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.4F;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_SPIDER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SPIDER_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block block) {
		this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}
}
