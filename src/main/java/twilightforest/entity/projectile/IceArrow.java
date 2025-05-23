package twilightforest.entity.projectile;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.init.TFEntities;

public class IceArrow extends TFArrow {

	public IceArrow(EntityType<? extends IceArrow> type, Level world) {
		super(type, world);
	}

	public IceArrow(Level world, @Nullable LivingEntity shooter, ItemStack stack, ItemStack weapon) {
		super(TFEntities.ICE_ARROW.get(), world, shooter, stack, weapon);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide() && !this.isInGround()) {
			BlockState stateId = Blocks.SNOW.defaultBlockState();
			for (int i = 0; i < 4; ++i) {
				this.level().addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, stateId), this.getX() + this.getDeltaMovement().x() * i / 4.0D, this.getY() + this.getDeltaMovement().y() * i / 4.0D, this.getZ() + this.getDeltaMovement().z() * i / 4.0D, -this.getDeltaMovement().x(), -this.getDeltaMovement().y() + 0.2D, -this.getDeltaMovement().z());
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.level().isClientSide() && result.getEntity() instanceof LivingEntity living) {
			ApplyFrostedEffect.doChillAuraEffect(living, 200, 2, true);
		}
	}
}
