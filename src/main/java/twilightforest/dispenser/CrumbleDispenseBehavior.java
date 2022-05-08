package twilightforest.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.item.recipe.TFRecipes;

public class CrumbleDispenseBehavior extends DefaultDispenseItemBehavior {

    boolean fired = false;

    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        Level world = source.getLevel();
        BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
        BlockState state = world.getBlockState(pos);
        if(!world.isClientSide) {
            if (!(stack.getMaxDamage() == stack.getDamageValue() + 1)) {
                world.getRecipeManager().getAllRecipesFor(TFRecipes.CRUMBLE_RECIPE.get()).forEach(recipe -> {
                    if(recipe.getInput().is(state.getBlock())) {
                        if (recipe.getResult().is(Blocks.AIR)) {
                            world.removeBlock(pos, true);
                            world.levelEvent(2001, pos, Block.getId(state));
                        } else {
                            world.setBlock(pos, recipe.getResult().getBlock().withPropertiesOf(state), 3);
                        }
                        stack.hurt(1, world.random, null);
                        fired = true;
                    }
                });
            }
        }
        return stack;
    }

    @Override
    protected void playSound(BlockSource source) {
        if (fired) {
            super.playSound(source);
            fired = false;
        } else {
            source.getLevel().levelEvent(1001, source.getPos(), 0);
        }
    }

}
