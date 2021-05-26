package twilightforest.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityTFSlimeProjectile extends EntityTFThrowable implements IRendersAsItem {

	public EntityTFSlimeProjectile(EntityType<? extends EntityTFSlimeProjectile> type, World world) {
		super(type, world);
	}

	public EntityTFSlimeProjectile(EntityType<? extends EntityTFSlimeProjectile> type, World world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	@Override
	protected float getGravityVelocity() {
		return 0.006F;
	}

	private void makeTrail() {
		for (int i = 0; i < 2; i++) {
			double dx = getPosX() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dy = getPosY() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dz = getPosZ() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			world.addParticle(ParticleTypes.ITEM_SLIME, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		super.attackEntityFrom(source, amount);
		die();
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(ParticleTypes.ITEM_SLIME, this.getPosX(), this.getPosY(), this.getPosZ(), rand.nextGaussian() * 0.05D, rand.nextDouble() * 0.2D, rand.nextGaussian() * 0.05D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		// only damage living things
		if (result instanceof EntityRayTraceResult) {
			Entity target = ((EntityRayTraceResult)result).getEntity();
			if (!world.isRemote && target instanceof LivingEntity) {
				target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), 8);
				//damage armor pieces
				if(target instanceof PlayerEntity) {
					for(ItemStack stack : target.getArmorInventoryList())
						stack.damageItem(rand.nextInt(1), ((PlayerEntity)target), (user) -> user.sendBreakAnimation(stack.getEquipmentSlot()));
				}
			}
		}

		die();
	}

	private void die() {
		if (!this.world.isRemote) {
			this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
			this.remove();
			this.world.setEntityState(this, (byte) 3);
		}
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.SLIME_BALL);
	}
}
