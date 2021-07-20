package twilightforest.dispenser;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import twilightforest.TFSounds;

//[VanillaCopy] of ProjectileDispenseBehavior, but it damages the moonworm queen instead of using it up every shot
public abstract class MoonwormDispenseBehavior extends DefaultDispenseItemBehavior {

    boolean fired = false;

    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        World world = source.getWorld();
        IPosition iposition = DispenserBlock.getDispensePosition(source);
        Direction direction = source.getBlockState().get(DispenserBlock.FACING);
        if(!world.isRemote) {
            if(!(stack.getMaxDamage() == stack.getDamage() + 2)) {
                ProjectileEntity projectileentity = this.getProjectileEntity(world, iposition, stack);
                projectileentity.shoot((double) direction.getXOffset(), (double) ((float) direction.getYOffset() + 0.1F), (double) direction.getZOffset(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
                world.addEntity(projectileentity);
                if (stack.attemptDamageItem(2, world.rand, (ServerPlayerEntity) null)) {
                    stack.setCount(0);
                }
                fired = true;
            }
        }
        return stack;
    }

    protected void playDispenseSound(IBlockSource source) {
        if(fired) {
            source.getWorld().playSound(null, source.getX(), source.getY(), source.getZ(), TFSounds.MOONWORM_SQUISH, SoundCategory.NEUTRAL, 1, 1);
            fired = false;
        } else {
            source.getWorld().playEvent(1001, source.getBlockPos(), 0);
        }
    }

    protected abstract ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn);

    //bigger inaccuracy is a good thing in this case, right?
    protected float getProjectileInaccuracy() {
        return 18.0F;
    }

    protected float getProjectileVelocity() {
        return 1.1F;
    }
}
