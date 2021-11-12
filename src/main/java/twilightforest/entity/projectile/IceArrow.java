package twilightforest.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import twilightforest.entity.TFEntities;
import twilightforest.potions.TFPotions;

public class IceArrow extends TFArrow {

	public IceArrow(EntityType<? extends IceArrow> type, Level world) {
		super(type, world);
	}

	public IceArrow(Level world, Entity shooter) {
		super(TFEntities.ICE_ARROW, world, shooter);
	}

	@Override
	public void tick() {
		super.tick();
		if (level.isClientSide && !inGround) {
			BlockState stateId = Blocks.SNOW.defaultBlockState();
			for (int i = 0; i < 4; ++i) {
				this.level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, stateId), this.getX() + this.getDeltaMovement().x() * i / 4.0D, this.getY() + this.getDeltaMovement().y() * i / 4.0D, this.getZ() + this.getDeltaMovement().z() * i / 4.0D, -this.getDeltaMovement().x(), -this.getDeltaMovement().y() + 0.2D, -this.getDeltaMovement().z());
			}
		}
	}

	@Override
	protected void onHit(HitResult ray) {
		super.onHit(ray);
		if (ray instanceof EntityHitResult) {
			if (!level.isClientSide && ((EntityHitResult)ray).getEntity() instanceof LivingEntity) {
				int chillLevel = 2;
				((LivingEntity) ((EntityHitResult)ray).getEntity()).addEffect(new MobEffectInstance(TFPotions.frosty.get(), 20 * 10, chillLevel));
			}
		}
	}
}
