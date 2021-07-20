package twilightforest.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.block.TFBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CrumbleDispenseBehavior extends DefaultDispenseItemBehavior {

    boolean fired = false;
    private final List<Pair<Predicate<BlockState>, UnaryOperator<BlockState>>> crumbleTransforms = new ArrayList<>();
    private final List<Predicate<BlockState>> harvestedStates = new ArrayList<>();

    @Override
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        this.addCrumbleTransforms();
        boolean crumbled = false;
        boolean harvested = false;
        World world = source.getWorld();
        BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
        BlockState blockstate = world.getBlockState(blockpos);
        if(!world.isRemote) {
            if (!(stack.getMaxDamage() == stack.getDamage() + 1)) {
                for (Pair<Predicate<BlockState>, UnaryOperator<BlockState>> transform : crumbleTransforms) {
                    if (transform.getLeft().test(blockstate)) {
                        world.setBlockState(blockpos, transform.getRight().apply(blockstate), 3);
                        crumbled = true;
                    }
                }

                if (crumbled) {
                    world.playEvent(2001, blockpos, Block.getStateId(blockstate));
                    if (stack.attemptDamageItem(1, world.rand, (ServerPlayerEntity) null)) {
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
                    if (stack.attemptDamageItem(1, world.rand, (ServerPlayerEntity) null)) {
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
    protected void playDispenseSound(IBlockSource source) {
        if (fired) {
            super.playDispenseSound(source);
            fired = false;
        } else {
            source.getWorld().playEvent(1001, source.getBlockPos(), 0);
        }
    }

    private void addCrumbleTransforms() {
        addCrumble(() -> Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS::getDefaultState);
        addCrumble(() -> Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS::getDefaultState);
        addCrumble(() -> Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.BLACKSTONE::getDefaultState);
        addCrumble(() -> Blocks.NETHER_BRICKS, Blocks.CRACKED_NETHER_BRICKS::getDefaultState);
        addCrumble(TFBlocks.maze_stone_brick, () -> TFBlocks.maze_stone_cracked.get().getDefaultState());
        addCrumble(TFBlocks.underbrick, () -> TFBlocks.underbrick_cracked.get().getDefaultState());
        addCrumble(TFBlocks.tower_wood, () -> TFBlocks.tower_wood_cracked.get().getDefaultState());
        addCrumble(TFBlocks.deadrock, () -> TFBlocks.deadrock_cracked.get().getDefaultState());
        addCrumble(TFBlocks.castle_brick, () -> TFBlocks.castle_brick_cracked.get().getDefaultState());
        addCrumble(TFBlocks.nagastone_pillar, () -> TFBlocks.nagastone_pillar_weathered.get().getDefaultState());
        addCrumble(TFBlocks.etched_nagastone, () -> TFBlocks.etched_nagastone_weathered.get().getDefaultState());
        addCrumble(() -> Blocks.STONE, Blocks.COBBLESTONE::getDefaultState);
        addCrumble(() -> Blocks.COBBLESTONE, Blocks.GRAVEL::getDefaultState);
        addCrumble(() -> Blocks.SANDSTONE, Blocks.SAND::getDefaultState);
        addCrumble(() -> Blocks.RED_SANDSTONE, Blocks.RED_SAND::getDefaultState);
        addCrumble(() -> Blocks.GRASS_BLOCK, Blocks.DIRT::getDefaultState);
        addCrumble(() -> Blocks.MYCELIUM, Blocks.DIRT::getDefaultState);
        addCrumble(() -> Blocks.PODZOL, Blocks.DIRT::getDefaultState);
        addCrumble(() -> Blocks.COARSE_DIRT, Blocks.DIRT::getDefaultState);
        addCrumble(() -> Blocks.CRIMSON_NYLIUM, Blocks.NETHERRACK::getDefaultState);
        addCrumble(() -> Blocks.WARPED_NYLIUM, Blocks.NETHERRACK::getDefaultState);
        addCrumble(() -> Blocks.QUARTZ_BLOCK, Blocks.SAND::getDefaultState);
        addHarvest(() -> Blocks.GRAVEL);
        addHarvest(() -> Blocks.DIRT);
        addHarvest(() -> Blocks.SAND);
        addHarvest(() -> Blocks.RED_SAND);
        addHarvest(() -> Blocks.CLAY);
        addHarvest(() -> Blocks.ANDESITE);
        addHarvest(() -> Blocks.GRANITE);
        addHarvest(() -> Blocks.DIORITE);
    }

    private void addCrumble(Supplier<Block> block, Supplier<BlockState> result) {
        addCrumble(state -> state.getBlock() == block.get(), state -> result.get());
    }

    private void addCrumble(Predicate<BlockState> test, UnaryOperator<BlockState> transform) {
        crumbleTransforms.add(Pair.of(test, transform));
    }

    private void addHarvest(Supplier<Block> block) {
        addHarvest(state -> state.getBlock() == block.get());
    }

    private void addHarvest(Predicate<BlockState> test) {
        harvestedStates.add(test);
    }
}
