package twilightforest.dispenser;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LevelEvent;
import twilightforest.util.TFItemStackUtils;

//[VanillaCopy] of ProjectileDispenseBehavior, but it damages the stack instead of using it up every shot
public abstract class DamageableStackDispenseBehavior extends DefaultDispenseItemBehavior {

	private boolean fired = false;

	@Override
	public ItemStack execute(BlockSource source, ItemStack stack) {
		ServerLevel level = source.level();
		Position pos = DispenserBlock.getDispensePosition(source);
		Direction direction = source.state().getValue(DispenserBlock.FACING);
		if (stack.getMaxDamage() >= stack.getDamageValue() + this.getDamageAmount()) {
			Projectile projectileentity = this.getProjectileEntity(level, pos, stack);
			projectileentity.shoot(direction.getStepX(), (float) direction.getStepY() + 0.1F, direction.getStepZ(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
			level.addFreshEntity(projectileentity);
			TFItemStackUtils.hurtButDontBreak(stack, 1, level, null);
			this.fired = true;
		}
		return stack;
	}

	@Override
	protected void playSound(BlockSource source) {
		if (this.fired) {
			source.level().playSound(null, source.center().x(), source.center().y(), source.center().z(), this.getFiredSound(), SoundSource.NEUTRAL, 1.0F, 1.0F);
			this.fired = false;
		} else {
			source.level().levelEvent(LevelEvent.SOUND_DISPENSER_FAIL, source.pos(), 0);
		}
	}

	protected abstract Projectile getProjectileEntity(Level level, Position position, ItemStack stack);

	protected abstract int getDamageAmount();

	protected abstract SoundEvent getFiredSound();

	//bigger inaccuracy is a good thing in this case, right?
	protected float getProjectileInaccuracy() {
		return 18.0F;
	}

	protected float getProjectileVelocity() {
		return 1.1F;
	}
}
