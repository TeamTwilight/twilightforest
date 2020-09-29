package twilightforest.enums;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import java.util.HashMap;
import java.util.Locale;

public enum BlockLoggingEnum implements IStringSerializable {
    AIR(Blocks.AIR, Fluids.EMPTY),
    WATER(Blocks.WATER, Fluids.WATER),
    LAVA(Blocks.LAVA, Fluids.LAVA),
    OBSIDIAN(Blocks.OBSIDIAN, Fluids.EMPTY);

    public static final EnumProperty<BlockLoggingEnum> MULTILOGGED = EnumProperty.create("multilogged", BlockLoggingEnum.class);

    private final Block block;
    private final Fluid fluid;
    private final String name;

    BlockLoggingEnum(Block block, Fluid fluid) {
        this.block = block;
        this.fluid = fluid;
        this.name = name().toLowerCase(Locale.ROOT);

        if (fluid != Fluids.EMPTY) {
            Ref.FLUIDS.put(fluid, this);
        }
    }

    public static BlockLoggingEnum getFromFluid(Fluid fluid) {
        return Ref.FLUIDS.getOrDefault(fluid, AIR);
    }

    @Override
    public String getString() {
        return name;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public interface IMultiLoggable extends IBucketPickupHandler, ILiquidContainer {
        @Override
        default Fluid pickupFluid(IWorld world, BlockPos pos, BlockState state) {
            Fluid stateFluid = state.get(MULTILOGGED).fluid;

            if (stateFluid != Fluids.EMPTY) {
                world.setBlockState(pos, state.with(MULTILOGGED, AIR), 3);
            }

            return stateFluid;
        }

        @Override
        default boolean canContainFluid(IBlockReader world, BlockPos pos, BlockState state, Fluid fluid) {
            return state.hasProperty(MULTILOGGED) && Ref.FLUIDS.containsKey(fluid) && !fluid.equals(state.get(MULTILOGGED).fluid);
        }

        @Override
        default boolean receiveFluid(IWorld world, BlockPos pos, BlockState state, FluidState fluidState) {
            Fluid stateFluid = state.get(MULTILOGGED).fluid;

            if (stateFluid != fluidState.getFluid() && Ref.FLUIDS.containsKey(fluidState.getFluid())) {
                if (!world.isRemote()) {
                    if (stateFluid != Fluids.EMPTY) { // TODO Fix this... if Mojang ever adds a third Liquid
                        world.setBlockState(pos, state.with(MULTILOGGED, OBSIDIAN), 3);
                    } else {
                        world.setBlockState(pos, state.with(MULTILOGGED, Ref.FLUIDS.get(fluidState.getFluid())), 3);
                        world.getPendingFluidTicks().scheduleTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
                    }
                }

                return true;
            } else {
                return false;
            }
        }
    }

    private static class Ref {
        private final static HashMap<Fluid, BlockLoggingEnum> FLUIDS = new HashMap<>();
    }
}
