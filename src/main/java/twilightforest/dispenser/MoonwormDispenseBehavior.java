package twilightforest.dispenser;

import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;

//[VanillaCopy] of ProjectileDispenseBehavior, but it damages the moonworm queen instead of using it up every shot
public abstract class MoonwormDispenseBehavior extends DefaultDispenseItemBehavior {

    boolean fired = false;

    @Override
    public ItemStack execute(BlockSource source, ItemStack stack) {
        Level world = source.getLevel();
        Position iposition = DispenserBlock.getDispensePosition(source);
        Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
        if(!world.isClientSide) {
            if(!(stack.getMaxDamage() == stack.getDamageValue() + 2)) {
                Projectile projectileentity = this.getProjectileEntity(world, iposition, stack);
                projectileentity.shoot(direction.getStepX(), (float) direction.getStepY() + 0.1F, direction.getStepZ(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
                world.addFreshEntity(projectileentity);
                if (stack.hurt(2, world.random, null)) {
                    stack.setCount(0);
                }
                fired = true;
            }
        }
        return stack;
    }

    protected void playSound(BlockSource source) {
        if(fired) {
            source.getLevel().playSound(null, source.x(), source.y(), source.z(), TFSounds.MOONWORM_SQUISH, SoundSource.NEUTRAL, 1, 1);
            fired = false;
        } else {
            source.getLevel().levelEvent(1001, source.getPos(), 0);
        }
    }

    protected abstract Projectile getProjectileEntity(Level worldIn, Position position, ItemStack stackIn);

    //bigger inaccuracy is a good thing in this case, right?
    protected float getProjectileInaccuracy() {
        return 18.0F;
    }

    protected float getProjectileVelocity() {
        return 1.1F;
    }
}
