package twilightforest.enums;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;

public enum BlockLoggingEnum implements StringRepresentable {
    AIR      (Blocks.AIR,      Fluids.EMPTY),
    WATER    (Blocks.WATER,    Fluids.WATER),
    LAVA     (Blocks.LAVA,     Fluids.LAVA),
    OBSIDIAN (Blocks.OBSIDIAN, Fluids.EMPTY),
    STONE    (Blocks.STONE,    Fluids.EMPTY),
    BASALT   (Blocks.BASALT,   Fluids.EMPTY);

    public static final EnumProperty<BlockLoggingEnum> MULTILOGGED = EnumProperty.create("multilogged", BlockLoggingEnum.class);

    private final Block block;
    private final Fluid fluid;
    private final String name;

    BlockLoggingEnum(Block block, Fluid fluid) {
        this.block = block;
        this.fluid = fluid;
        this.name = name().toLowerCase(Locale.ROOT);

        if (fluid != Fluids.EMPTY && block == Blocks.AIR) {
            Ref.FLUIDS.put(fluid, this);
        }
        if(fluid == Fluids.EMPTY && block != Blocks.AIR) {
            Ref.BLOCKS.put(block, this);
        }
    }

    public static BlockLoggingEnum getFromFluid(Fluid fluid) {
        return Ref.FLUIDS.getOrDefault(fluid, AIR);
    }

    public static BlockLoggingEnum getFromBlock(Block block) {
        return Ref.BLOCKS.getOrDefault(block, AIR);
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public Block getBlock() {
        return block;
    }

    public interface IMultiLoggable extends BucketPickup, LiquidBlockContainer {
        @Override
        default ItemStack pickupBlock(@Nullable Player player, LevelAccessor world, BlockPos pos, BlockState state) {
            Fluid stateFluid = state.getValue(MULTILOGGED).fluid;

            if (stateFluid != Fluids.EMPTY) {
                world.setBlock(pos, state.setValue(MULTILOGGED, AIR), 3);
            }

            return new ItemStack(stateFluid.getBucket());
        }

        @Override
        default boolean canPlaceLiquid(@Nullable Player player, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
            return state.hasProperty(MULTILOGGED) && Ref.FLUIDS.containsKey(fluid) && !fluid.equals(state.getValue(MULTILOGGED).fluid) && state.getValue(MULTILOGGED) == AIR;
        }

        @Override
        default boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
            Fluid stateFluid = state.getValue(MULTILOGGED).fluid;

            if (stateFluid != fluidState.getType() && Ref.FLUIDS.containsKey(fluidState.getType())) {
                if (!world.isClientSide()) {
                    world.setBlock(pos, state.setValue(MULTILOGGED, Ref.FLUIDS.get(fluidState.getType())), 3);
                    world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
                }
                return true;
            }
            return false;
        }
    }

    private static class Ref {
        private final static HashMap<Fluid, BlockLoggingEnum> FLUIDS = new HashMap<>();
        private final static HashMap<Block, BlockLoggingEnum> BLOCKS = new HashMap<>();
    }
}
