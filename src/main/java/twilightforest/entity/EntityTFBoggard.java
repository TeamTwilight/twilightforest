package twilightforest.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFChargeAttack;
import twilightforest.util.PlayerHelper;


public class EntityTFBoggard extends EntityMob {
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/boggard");

	public EntityTFBoggard(World world) {
		super(world);
		setSize(0.8F, 1.1F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
//        this.tasks.addTask(1, new EntityAITFRedcapShy(this, this.moveSpeed));
		this.tasks.addTask(2, new EntityAITFChargeAttack(this, 2.0F, false));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0F, false));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0F));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, false));
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

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (cause.getTrueSource() instanceof EntityPlayerMP) {
			// are we in a level 1 hill?
			int chunkX = MathHelper.floor(posX) >> 4;
			int chunkZ = MathHelper.floor(posZ) >> 4;
			if (TFFeature.getNearestFeature(chunkX, chunkZ, world) == TFFeature.SMALL_HILL) {
				PlayerHelper.grantCriterion((EntityPlayerMP) cause.getTrueSource(), new ResourceLocation(TwilightForestMod.ID, "hill1"), "boggard");
			}
		}
	}
}
