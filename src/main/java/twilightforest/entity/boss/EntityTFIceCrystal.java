package twilightforest.entity.boss;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.MeleeAttackGoal;
import net.minecraft.entity.ai.HurtByTargetGoal;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.LookAtGoal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFIceMob;

public class EntityTFIceCrystal extends EntityTFIceMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/ice_crystal");

	private int crystalAge;
	private int maxCrystalAge = -1;

	public EntityTFIceCrystal(World world) {
		super(world);
		this.setSize(0.6F, 1.8F);

		//this.setCurrentItemOrArmor(0, new ItemStack(TFItems.iceSword));
	}

	@Override
	protected void registerGoals() {
		this.tasks.addTask(0, new SwimGoal(this));
		this.tasks.addTask(1, new MeleeAttackGoal(this, 1.0D, false));
		this.tasks.addTask(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.tasks.addTask(3, new LookAtGoal(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new LookRandomlyGoal(this));
		this.targetTasks.addTask(1, new HurtByTargetGoal(this, true));
		this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
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
	protected SoundEvent getAmbientSound() {
		return TFSounds.ICE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.ICE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ICE_DEATH;
	}

	public void setToDieIn30Seconds() {
		this.maxCrystalAge = 600;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (!world.isRemote) {
			this.crystalAge++;
			if (this.maxCrystalAge > 0 && this.crystalAge >= this.maxCrystalAge) {
				this.setDead();
			}
		}
	}

}
