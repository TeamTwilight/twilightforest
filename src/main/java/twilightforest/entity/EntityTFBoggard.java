package twilightforest.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.MeleeAttackGoal;
import net.minecraft.entity.ai.HurtByTargetGoal;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.LookAtGoal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFChargeAttack;

public class EntityTFBoggard extends EntityMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/boggard");

	public EntityTFBoggard(World world) {
		super(world);
		setSize(0.8F, 1.1F);
	}

	@Override
	protected void registerGoals() {
		this.tasks.addTask(0, new SwimGoal(this));
//        this.tasks.addTask(1, new EntityAITFRedcapShy(this, this.moveSpeed));
		this.tasks.addTask(2, new EntityAITFChargeAttack(this, 2.0F, false));
		this.tasks.addTask(3, new MeleeAttackGoal(this, 1.0F, false));
		this.tasks.addTask(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.tasks.addTask(7, new LookAtGoal(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new LookRandomlyGoal(this));
		this.targetTasks.addTask(1, new HurtByTargetGoal(this, false));
		this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityPlayer.class, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.REDCAP_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.REDCAP_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.REDCAP_AMBIENT;
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
