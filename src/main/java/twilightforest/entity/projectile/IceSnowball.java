package twilightforest.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.TFEntities;
import twilightforest.util.TFDamageSources;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class IceSnowball extends TFThrowable implements ItemSupplier {

	private static final int DAMAGE = 2;

	public IceSnowball(EntityType<? extends IceSnowball> type, Level world) {
		super(type, world);
	}

	public IceSnowball(Level world, LivingEntity thrower) {
		super(TFEntities.ICE_SNOWBALL, world, thrower);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	@Override
	protected float getGravity() {
		return 0.006F;
	}

	public void makeTrail() {
		for (int i = 0; i < 2; i++) {
			double dx = getX() + 0.5 * (random.nextDouble() - random.nextDouble());
			double dy = getY() + 0.5 * (random.nextDouble() - random.nextDouble());
			double dz = getZ() + 0.5 * (random.nextDouble() - random.nextDouble());
			level.addParticle(ParticleTypes.ITEM_SNOWBALL, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		super.hurt(source, amount);
		die();
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			for (int j = 0; j < 8; ++j) {
				this.level.addParticle(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHit(HitResult result) {
		if (result instanceof EntityHitResult) {
			Entity target = ((EntityHitResult)result).getEntity();
			if (!level.isClientSide && target instanceof LivingEntity) {
				target.hurt(TFDamageSources.snowballFight(this, (LivingEntity) this.getOwner()), DAMAGE);
				//damage armor pieces
				if(target instanceof Player) {
					for(ItemStack stack : target.getArmorSlots())
					stack.hurtAndBreak(random.nextInt(1), ((Player)target), (user) -> user.broadcastBreakEvent(stack.getEquipmentSlot()));
				}
			}
		}

		die();
	}

	private void die() {
		if (!this.level.isClientSide) {
			this.level.broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.SNOWBALL);
	}
}
