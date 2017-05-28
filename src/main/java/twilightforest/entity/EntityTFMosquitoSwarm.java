package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TFAchievementPage;
import twilightforest.TFSounds;
import twilightforest.biomes.TFBiomes;

public class EntityTFMosquitoSwarm extends EntityMob {

	public EntityTFMosquitoSwarm(World par1World) {
		super(par1World);

        setSize(.7F, 1.9F);
        this.stepHeight = 2.1f;
	}

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 0, true, false, null));
    }

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }

    @Override
	protected SoundEvent getAmbientSound()
    {
        return TFSounds.MOSQUITO;
    }

    @Override
    public boolean attackEntityAsMob(Entity par1Entity)
    {
        if (super.attackEntityAsMob(par1Entity))
        {
            if (par1Entity instanceof EntityLivingBase)
            {
                int duration;
                switch (world.getDifficulty()) {
                    case EASY: duration = 7; break;
                    default:
                    case NORMAL: duration = 15; break;
                    case HARD: duration = 30; break;
                }

                ((EntityLivingBase) par1Entity).addPotionEffect(new PotionEffect(MobEffects.HUNGER, duration * 20, 0));
            }

            return true;
        }
        else
        {
            return false;
        }
    }

	@Override
	public boolean getCanSpawnHere()
    {
		if (world.getBiome(new BlockPos(this)) == TFBiomes.tfSwamp) {
			// don't check light level
	        return world.checkNoEntityCollision(getEntityBoundingBox()) && world.getCollisionBoxes(this, getEntityBoundingBox()).size() == 0;
		} else {
			return super.getCanSpawnHere();
		}
    }

    @Override
	public int getMaxSpawnedInChunk()
    {
        return 1;
    }

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);
		if (par1DamageSource.getSourceOfDamage() instanceof EntityPlayer) {
			((EntityPlayer)par1DamageSource.getSourceOfDamage()).addStat(TFAchievementPage.twilightHunter);
		}
	}
}
