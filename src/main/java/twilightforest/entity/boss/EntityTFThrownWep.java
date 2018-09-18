package twilightforest.entity.boss;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.entity.ITFProjectile;

public class EntityTFThrownWep extends EntityThrowable implements ITFProjectile {

	private static final DataParameter<ItemStack> DATA_ITEMSTACK = EntityDataManager.createKey(EntityTFThrownWep.class, DataSerializers.ITEM_STACK);
	private static final DataParameter<Float> DATA_VELOCITY = EntityDataManager.createKey(EntityTFThrownWep.class, DataSerializers.FLOAT);

	private float projectileDamage = 6;

	public EntityTFThrownWep(World world, EntityLivingBase thrower) {
		super(world, thrower);
		this.setSize(0.5F, 0.5F);
	}

	public EntityTFThrownWep(World world) {
		super(world);
		this.setSize(0.5F, 0.5F);
	}

	public EntityTFThrownWep setDamage(float damage) {
		projectileDamage = damage;
		return this;
	}

	@Override
	protected void entityInit() {
		dataManager.register(DATA_ITEMSTACK, ItemStack.EMPTY);
		dataManager.register(DATA_VELOCITY, 0.001F);
	}

	public EntityTFThrownWep setItem(ItemStack stack) {
		dataManager.set(DATA_ITEMSTACK, stack);
		return this;
	}

	public ItemStack getItem() {
		return dataManager.get(DATA_ITEMSTACK);
	}

	public EntityTFThrownWep setVelocity(float velocity) {
		dataManager.set(DATA_VELOCITY, velocity);
		return this;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.entityHit instanceof EntityTFKnightPhantom || result.entityHit == this.getThrower()) {
			return;
		}

		if (!world.isRemote) {
			if (result.entityHit != null) {
				result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), projectileDamage);
			}
			world.setEntityState(this, (byte) 3);
			setDead();
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public float getCollisionBorderSize() {
		return 1.0F;
	}

	@Override
	protected float getGravityVelocity() {
		return dataManager.get(DATA_VELOCITY);
	}
}
