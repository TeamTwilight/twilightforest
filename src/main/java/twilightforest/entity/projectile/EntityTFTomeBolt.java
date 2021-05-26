package twilightforest.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.util.TFDamageSources;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityTFTomeBolt extends EntityTFThrowable implements IRendersAsItem {

	public EntityTFTomeBolt(EntityType<? extends EntityTFTomeBolt> type, World world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	public EntityTFTomeBolt(EntityType<? extends EntityTFTomeBolt> type, World world) {
		super(type, world);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	@Override
	protected float getGravityVelocity() {
		return 0.003F;
	}

	private void makeTrail() {
		for (int i = 0; i < 5; i++) {
			double dx = getPosX() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dy = getPosY() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dz = getPosZ() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			world.addParticle(ParticleTypes.CRIT, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			IParticleData particle = new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Items.PAPER));
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(particle, false, this.getPosX(), this.getPosY(), this.getPosZ(), rand.nextGaussian() * 0.05D, rand.nextDouble() * 0.2D, rand.nextGaussian() * 0.05D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result instanceof EntityRayTraceResult) {
				EntityRayTraceResult entityRay = ((EntityRayTraceResult) result);
				if (entityRay.getEntity() instanceof LivingEntity
						&& entityRay.getEntity().attackEntityFrom(TFDamageSources.LOST_WORDS(this, (LivingEntity)this.func_234616_v_()), 6)) {
					// inflict move slowdown
					int duration = world.getDifficulty() == Difficulty.PEACEFUL ? 3 : world.getDifficulty() == Difficulty.NORMAL ? 7 : 9;
					((LivingEntity) entityRay.getEntity()).addPotionEffect(new EffectInstance(Effects.SLOWNESS, duration * 20, 1));
				}
			}

			this.world.setEntityState(this, (byte) 3);
			this.remove();
		}
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.PAPER);
	}
}
