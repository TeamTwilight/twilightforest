package twilightforest.entity.projectile;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import twilightforest.init.TFDamageTypes;

public class TomeBolt extends TFThrowable implements ItemSupplier {

	public TomeBolt(EntityType<? extends TomeBolt> type, Level world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	public TomeBolt(EntityType<? extends TomeBolt> type, Level world) {
		super(type, world);
	}

	@Override
	public void tick() {
		super.tick();
		this.makeTrail(ParticleTypes.CRIT, 5);
	}

	@Override
	protected double getDefaultGravity() {
		return 0.003F;
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == EntityEvent.DEATH) {
			ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.PAPER));
			for (int i = 0; i < 8; ++i) {
				this.level().addParticle(particle, false, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, this.random.nextDouble() * 0.2D, this.random.nextGaussian() * 0.05D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (result.getEntity() instanceof LivingEntity living) {
			if (result.getEntity().hurt(TFDamageTypes.getIndirectEntityDamageSource(this.level(), this.random.nextBoolean() ? TFDamageTypes.LOST_WORDS : TFDamageTypes.SCHOOLED, this, this.getOwner()), 3)) {
				// inflict move slowdown
				int duration = this.level().getDifficulty() == Difficulty.EASY ? 2 : this.level().getDifficulty() == Difficulty.NORMAL ? 6 : 8;
				living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration * 20, 1));
			}
		}
	}

	@Override
	protected void onHit(HitResult result) {
		if (this.getOwner() != null && result instanceof BlockHitResult blockHitResult &&
			this.getOwner().blockPosition().equals(blockHitResult.getBlockPos()) &&
			this.level().getBlockState(blockHitResult.getBlockPos()).getBlock() instanceof LecternBlock) {
			return;
		}
		super.onHit(result);
		if (!this.level().isClientSide()) {
			this.level().broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.PAPER);
	}
}
