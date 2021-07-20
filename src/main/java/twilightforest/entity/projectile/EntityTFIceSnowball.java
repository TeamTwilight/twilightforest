package twilightforest.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.TFEntities;
import twilightforest.util.TFDamageSources;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityTFIceSnowball extends EntityTFThrowable implements IRendersAsItem {

	private static final int DAMAGE = 2;

	public EntityTFIceSnowball(EntityType<? extends EntityTFIceSnowball> type, World world) {
		super(type, world);
	}

	public EntityTFIceSnowball(World world, LivingEntity thrower) {
		super(TFEntities.ice_snowball, world, thrower);
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

	public void makeTrail() {
		for (int i = 0; i < 2; i++) {
			double dx = getPosX() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dy = getPosY() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dz = getPosZ() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			world.addParticle(ParticleTypes.ITEM_SNOWBALL, dx, dy, dz, 0.0D, 0.0D, 0.0D);
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
			for (int j = 0; j < 8; ++j) {
				this.world.addParticle(ParticleTypes.ITEM_SNOWBALL, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result instanceof EntityRayTraceResult) {
			Entity target = ((EntityRayTraceResult)result).getEntity();
			if (!world.isRemote && target instanceof LivingEntity) {
				target.attackEntityFrom(TFDamageSources.SNOWBALL_FIGHT(this, (LivingEntity) this.getShooter()), DAMAGE);
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
			this.world.setEntityState(this, (byte) 3);
			this.remove();
		}
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.SNOWBALL);
	}
}
