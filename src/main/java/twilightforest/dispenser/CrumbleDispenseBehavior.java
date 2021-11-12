package twilightforest.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.item.CrumbleHornItem;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class CrumbleDispenseBehavior extends DefaultDispenseItemBehavior {

    boolean fired = false;
    private final List<Pair<Predicate<BlockState>, UnaryOperator<BlockState>>> crumbleTransforms = CrumbleHornItem.crumbleTransforms;
    private final List<Predicate<BlockState>> harvestedStates = CrumbleHornItem.harvestedStates;

    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        boolean crumbled = false;
        boolean harvested = false;
        Level world = source.getLevel();
        BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
        BlockState blockstate = world.getBlockState(blockpos);
        if(!world.isClientSide) {
            if (!(stack.getMaxDamage() == stack.getDamageValue() + 1)) {
                for (Pair<Predicate<BlockState>, UnaryOperator<BlockState>> transform : crumbleTransforms) {
                    if (transform.getLeft().test(blockstate)) {
                        world.setBlock(blockpos, transform.getRight().apply(blockstate), 3);
                        crumbled = true;
                    }
                }

                if (crumbled) {
                    world.levelEvent(2001, blockpos, Block.getId(blockstate));
                    if (stack.hurt(1, world.random, null)) {
                        stack.setCount(0);
                    }
                    fired = true;
                }

                for (Predicate<BlockState> predicate : harvestedStates) {
                    if (predicate.test(blockstate)) {
                        world.destroyBlock(blockpos, true);
                        harvested = true;
                    }
                }

                if (harvested) {
                    if (stack.hurt(1, world.random, null)) {
                        stack.setCount(0);
                    }
                    fired = true;
                }
                return stack;
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
