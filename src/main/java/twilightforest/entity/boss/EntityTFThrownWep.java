package twilightforest.entity.boss;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.projectile.EntityTFThrowable;
import twilightforest.item.TFItems;
import twilightforest.util.TFDamageSources;

public class EntityTFThrownWep extends EntityTFThrowable {

	private static final DataParameter<ItemStack> DATA_ITEMSTACK = EntityDataManager.createKey(EntityTFThrownWep.class, DataSerializers.ITEMSTACK);
	private static final DataParameter<Float> DATA_VELOCITY = EntityDataManager.createKey(EntityTFThrownWep.class, DataSerializers.FLOAT);

	private float projectileDamage = 6;

	public EntityTFThrownWep(EntityType<? extends EntityTFThrownWep> type, World world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	public EntityTFThrownWep(EntityType<? extends EntityTFThrownWep> type, World world) {
		super(type, world);
	}

	public EntityTFThrownWep setDamage(float damage) {
		projectileDamage = damage;
		return this;
	}

	@Override
	protected void registerData() {
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

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result instanceof EntityRayTraceResult) {
			if (((EntityRayTraceResult)result).getEntity() instanceof EntityTFKnightPhantom || ((EntityRayTraceResult)result).getEntity() == this.getShooter()) {
				return;
			}

			if (!world.isRemote) {
				if (((EntityRayTraceResult)result).getEntity() != null) {
					((EntityRayTraceResult)result).getEntity().attackEntityFrom(this.getItem().getItem() == TFItems.knightmetal_pickaxe.get() ? TFDamageSources.THROWN_PICKAXE : TFDamageSources.THROWN_AXE, projectileDamage);
				}
				world.setEntityState(this, (byte) 3);
				remove();
			}
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
