package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.MeleeAttackGoal;
import net.minecraft.entity.ai.HurtByTargetGoal;
import net.minecraft.entity.ai.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.biomes.TFBiomes;

public class EntityTFMosquitoSwarm extends EntityMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/mosquito_swarm");

	public EntityTFMosquitoSwarm(World world) {
		super(world);

		setSize(.7F, 1.9F);
		this.stepHeight = 2.1f;
	}

	@Override
	protected void registerGoals() {
		this.tasks.addTask(0, new SwimGoal(this));
		this.tasks.addTask(3, new MeleeAttackGoal(this, 1.0D, false));
		this.tasks.addTask(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.targetTasks.addTask(1, new HurtByTargetGoal(this, true));
		this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MOSQUITO;
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (super.attackEntityAsMob(entity)) {
			if (entity instanceof EntityLivingBase) {
				int duration;
				switch (world.getDifficulty()) {
					case EASY:
						duration = 7;
						break;
					default:
					case NORMAL:
						duration = 15;
						break;
					case HARD:
						duration = 30;
						break;
				}

				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.HUNGER, duration * 20, 0));
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean getCanSpawnHere() {
		if (world.getBiome(new BlockPos(this)) == TFBiomes.tfSwamp) {
			// don't check light level
			return world.checkNoEntityCollision(getEntityBoundingBox()) && world.getCollisionBoxes(this, getEntityBoundingBox()).size() == 0;
		} else {
			return super.getCanSpawnHere();
		}
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 1;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
