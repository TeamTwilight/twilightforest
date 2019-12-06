package twilightforest.entity;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFAdherent extends MobEntity implements IRangedAttackMob, ITFCharger {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/adherent");

	private static final DataParameter<Boolean> CHARGE_FLAG = EntityDataManager.createKey(EntityTFAdherent.class, DataSerializers.BOOLEAN);

	public EntityTFAdherent(World world) {
		super(world);
		this.setSize(0.8F, 2.2F);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(2, new RestrictSunGoal(this));
		this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(CHARGE_FLAG, false);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	@Override
	public void attackEntityWithRangedAttack(LivingEntity attackTarget, float extraDamage) {
		EntityTFNatureBolt natureBolt = new EntityTFNatureBolt(this.world, this);
		playSound(SoundEvents.ENTITY_GHAST_SHOOT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));

		// [VanillaCopy] adapted from EntitySnowman, with lower velocity and inaccuracy calculation
		double d0 = attackTarget.posY + (double) attackTarget.getEyeHeight() - 1.100000023841858D;
		double d1 = attackTarget.posX - this.posX;
		double d2 = d0 - natureBolt.posY;
		double d3 = attackTarget.posZ - this.posZ;
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
		natureBolt.shoot(d1, d2 + (double) f, d3, 0.6F, 10 - this.world.getDifficulty().getId() * 4);

		this.world.addEntity(natureBolt);

	}

	@Override
	public void setSwingingArms(boolean swingingArms) {} // todo 1.12

	@Override
	public boolean isCharging() {
		return dataManager.get(CHARGE_FLAG);
	}

	@Override
	public void setCharging(boolean flag) {
		dataManager.set(CHARGE_FLAG, flag);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
