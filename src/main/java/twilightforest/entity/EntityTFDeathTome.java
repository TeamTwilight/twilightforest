package twilightforest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import twilightforest.TwilightForestMod;

public class EntityTFDeathTome extends EntityMob implements IRangedAttackMob {
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/death_tome");
	public static final ResourceLocation HURT_LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/death_tome_hurt");

	public EntityTFDeathTome(World world) {
		super(world);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(4, new EntityAIAttackRanged(this, 1, 60, 10));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0F));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		for (int i = 0; i < 1; ++i) {
			this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * (this.height - 0.75D) + 0.5D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width,
					0, 0.5, 0);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource src, float damage) {
		if (src.isFireDamage()) {
			damage *= 2;
		}

		if (super.attackEntityFrom(src, damage)) {
			if (!world.isRemote) {
				LootContext ctx = new LootContext.Builder((WorldServer) world)
						.withDamageSource(src)
						.withLootedEntity(this)
						.build();

				for (ItemStack stack : world.getLootTableManager().getLootTableFromLocation(HURT_LOOT_TABLE).generateLootForPools(world.rand, ctx)) {
					entityDropItem(stack, 1.0F);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		EntityThrowable projectile = new EntityTFTomeBolt(this.world, this);
		double tx = target.posX - this.posX;
		double ty = target.posY + target.getEyeHeight() - 1.100000023841858D - projectile.posY;
		double tz = target.posZ - this.posZ;
		float heightOffset = MathHelper.sqrt(tx * tx + tz * tz) * 0.2F;
		projectile.shoot(tx, ty + heightOffset, tz, 0.6F, 6.0F);
		this.world.spawnEntity(projectile);
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {} // todo 1.12
}
