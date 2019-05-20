package twilightforest.enums;

import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;

import javax.annotation.Nullable;
import java.util.Locale;

public enum StructureWoodVariant implements IStringSerializable {

    OAK          ( Blocks  .PLANKS             , Blocks  .OAK_STAIRS         , Blocks  .WOODEN_SLAB       , Blocks  .DOUBLE_WOODEN_SLAB     , Blocks  .WOODEN_BUTTON      , Blocks  .OAK_FENCE         , Blocks  .OAK_FENCE_GATE     , Blocks  .WOODEN_PRESSURE_PLATE ),
    SPRUCE       ( Blocks  .PLANKS             , Blocks  .SPRUCE_STAIRS      , Blocks  .WOODEN_SLAB       , Blocks  .DOUBLE_WOODEN_SLAB     , Blocks  .WOODEN_BUTTON      , Blocks  .SPRUCE_FENCE      , Blocks  .SPRUCE_FENCE_GATE  , Blocks  .WOODEN_PRESSURE_PLATE ),
    BIRCH        ( Blocks  .PLANKS             , Blocks  .BIRCH_STAIRS       , Blocks  .WOODEN_SLAB       , Blocks  .DOUBLE_WOODEN_SLAB     , Blocks  .WOODEN_BUTTON      , Blocks  .BIRCH_FENCE       , Blocks  .BIRCH_FENCE_GATE   , Blocks  .WOODEN_PRESSURE_PLATE ),
    JUNGLE       ( Blocks  .PLANKS             , Blocks  .JUNGLE_STAIRS      , Blocks  .WOODEN_SLAB       , Blocks  .DOUBLE_WOODEN_SLAB     , Blocks  .WOODEN_BUTTON      , Blocks  .JUNGLE_FENCE      , Blocks  .JUNGLE_FENCE_GATE  , Blocks  .WOODEN_PRESSURE_PLATE ),
    ACACIA       ( Blocks  .PLANKS             , Blocks  .ACACIA_STAIRS      , Blocks  .WOODEN_SLAB       , Blocks  .DOUBLE_WOODEN_SLAB     , Blocks  .WOODEN_BUTTON      , Blocks  .ACACIA_FENCE      , Blocks  .ACACIA_FENCE_GATE  , Blocks  .WOODEN_PRESSURE_PLATE ),
    DARK_OAK     ( Blocks  .PLANKS             , Blocks  .DARK_OAK_STAIRS    , Blocks  .WOODEN_SLAB       , Blocks  .DOUBLE_WOODEN_SLAB     , Blocks  .WOODEN_BUTTON      , Blocks  .DARK_OAK_FENCE    , Blocks  .DARK_OAK_FENCE_GATE, Blocks  .WOODEN_PRESSURE_PLATE ),

    TWILIGHT_OAK ( TFBlocks.twilight_oak_planks, TFBlocks.twilight_oak_stairs, TFBlocks.twilight_oak_slab , TFBlocks.twilight_oak_doubleslab, TFBlocks.twilight_oak_button, TFBlocks.twilight_oak_fence, TFBlocks.twilight_oak_gate  , TFBlocks.twilight_oak_plate    ),
    CANOPY       ( TFBlocks.canopy_planks      , TFBlocks.canopy_stairs      , TFBlocks.canopy_slab       , TFBlocks.canopy_doubleslab      , TFBlocks.canopy_button      , TFBlocks.canopy_fence      , TFBlocks.canopy_gate        , TFBlocks.canopy_plate          ),
    MANGROVE     ( TFBlocks.mangrove_planks    , TFBlocks.mangrove_stairs    , TFBlocks.mangrove_slab     , TFBlocks.mangrove_doubleslab    , TFBlocks.mangrove_button    , TFBlocks.mangrove_fence    , TFBlocks.mangrove_gate      , TFBlocks.mangrove_plate        ),
    DARK         ( TFBlocks.dark_planks        , TFBlocks.dark_stairs        , TFBlocks.dark_slab         , TFBlocks.dark_doubleslab        , TFBlocks.dark_button        , TFBlocks.dark_fence        , TFBlocks.dark_gate          , TFBlocks.dark_plate            ),
    TIME         ( TFBlocks.time_planks        , TFBlocks.time_stairs        , TFBlocks.time_slab         , TFBlocks.time_doubleslab        , TFBlocks.time_button        , TFBlocks.time_fence        , TFBlocks.time_gate          , TFBlocks.time_plate            ),
    TRANS        ( TFBlocks.trans_planks       , TFBlocks.trans_stairs       , TFBlocks.trans_slab        , TFBlocks.trans_doubleslab       , TFBlocks.trans_button       , TFBlocks.trans_fence       , TFBlocks.trans_gate         , TFBlocks.trans_plate           ),
    MINE         ( TFBlocks.mine_planks        , TFBlocks.mine_stairs        , TFBlocks.mine_slab         , TFBlocks.mine_doubleslab        , TFBlocks.mine_button        , TFBlocks.mine_fence        , TFBlocks.mine_gate          , TFBlocks.mine_plate            ),
    SORT         ( TFBlocks.sort_planks        , TFBlocks.sort_stairs        , TFBlocks.sort_slab         , TFBlocks.sort_doubleslab        , TFBlocks.sort_button        , TFBlocks.sort_fence        , TFBlocks.sort_gate          , TFBlocks.sort_plate            );

    StructureWoodVariant(
            Block planks    ,
            Block stairs    ,
            Block slab      ,
            Block doubleSlab,
            Block button    ,
            Block fence     ,
            Block gate      ,
            Block plate
    ) {
        this.planks     = planks    ;
        this.stairs     = stairs    ;
        this.slab       = slab      ;
        this.doubleSlab = doubleSlab;
        this.button     = button    ;
        this.fence      = fence     ;
        this.gate       = gate      ;
        this.plate      = plate     ;
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    private final Block planks    ;
    private final Block stairs    ;
    private final Block slab      ;
    private final Block doubleSlab;
    private final Block button    ;
    private final Block fence     ;
    private final Block gate      ;
    private final Block plate     ;

    @Nullable
    public static BlockPlanks.EnumType getTypeFromBlockState(IBlockState stateIn) {
        Block block = stateIn.getBlock();

        if (!"minecraft".equals(block.getRegistryName().getNamespace())) return null;

        switch (getWoodShapeFromBlock(block)) {
            case BLOCK:
                if (stateIn.getBlock() instanceof BlockPlanks)
                    return stateIn.getValue(BlockPlanks.VARIANT);
                else
                    return null;
            case SLAB:
            case DOUBLESLAB:
                if (stateIn.getBlock() instanceof BlockWoodSlab)
                    return stateIn.getValue(BlockPlanks.VARIANT);
                else
                    return null;
            case STAIRS:
                if (block == Blocks.OAK_STAIRS )
                    return BlockPlanks.EnumType.OAK;
                else if (block == Blocks.SPRUCE_STAIRS )
                    return BlockPlanks.EnumType.SPRUCE;
                else if (block == Blocks.BIRCH_STAIRS )
                    return BlockPlanks.EnumType.BIRCH;
                else if (block == Blocks.JUNGLE_STAIRS )
                    return BlockPlanks.EnumType.JUNGLE;
                else if (block == Blocks.ACACIA_STAIRS )
                    return BlockPlanks.EnumType.ACACIA;
                else if (block == Blocks.DARK_OAK_STAIRS )
                    return BlockPlanks.EnumType.DARK_OAK;
                else
                    return null;
            case FENCE:
                if (block == Blocks.OAK_FENCE )
                    return BlockPlanks.EnumType.OAK;
                else if (block == Blocks.SPRUCE_FENCE )
                    return BlockPlanks.EnumType.SPRUCE;
                else if (block == Blocks.BIRCH_FENCE )
                    return BlockPlanks.EnumType.BIRCH;
                else if (block == Blocks.JUNGLE_FENCE )
                    return BlockPlanks.EnumType.JUNGLE;
                else if (block == Blocks.ACACIA_FENCE )
                    return BlockPlanks.EnumType.ACACIA;
                else if (block == Blocks.DARK_OAK_FENCE )
                    return BlockPlanks.EnumType.DARK_OAK;
                else
                    return null;
            case GATE:
                if (block == Blocks.OAK_FENCE_GATE )
                    return BlockPlanks.EnumType.OAK;
                else if (block == Blocks.SPRUCE_FENCE_GATE )
                    return BlockPlanks.EnumType.SPRUCE;
                else if (block == Blocks.BIRCH_FENCE_GATE )
                    return BlockPlanks.EnumType.BIRCH;
                else if (block == Blocks.JUNGLE_FENCE_GATE )
                    return BlockPlanks.EnumType.JUNGLE;
                else if (block == Blocks.ACACIA_FENCE_GATE )
                    return BlockPlanks.EnumType.ACACIA;
                else if (block == Blocks.DARK_OAK_FENCE_GATE )
                    return BlockPlanks.EnumType.DARK_OAK;
                else
                    return null;
            case BUTTON:
            case PLATE:
                return BlockPlanks.EnumType.OAK;
            default:
                return null;
        }
    }

    public static IBlockState modifyBlockWithType(IBlockState stateIn, StructureWoodVariant target) {
        // get all data from stateIn
        Block block = stateIn.getBlock();
        WoodShapes shape = getWoodShapeFromBlock(block);

        ResourceLocation blockRegName = block.getRegistryName();

        if (blockRegName == null) return stateIn;

        if ("minecraft".equals(blockRegName.getNamespace()) && block instanceof BlockPlanks) {
            shape = WoodShapes.BLOCK;
        }

        if (shape == WoodShapes.INVALID || !("minecraft".equals(blockRegName.getNamespace()) || TwilightForestMod.ID.equals(blockRegName.getNamespace()))) {
            return stateIn; // Can't deal with this.
        }

        switch (shape) {
            case BLOCK:
                switch (target) {
                    case OAK:
                        return target.planks.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK);
                    case SPRUCE:
                        return target.planks.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
                    case BIRCH:
                        return target.planks.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
                    case JUNGLE:
                        return target.planks.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE);
                    case ACACIA:
                        return target.planks.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA);
                    case DARK_OAK:
                        return target.planks.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK);
                    default:
                        return target.planks.getDefaultState();
                }
            case STAIRS:
                return transferStateKeys(stateIn, target.stairs.getDefaultState(), BlockStairs.FACING, BlockStairs.HALF, BlockStairs.SHAPE);
            case SLAB:
                switch (target) {
                    case OAK:
                        return transferStateKey(stateIn, target.slab.getDefaultState(), BlockSlab.HALF).withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.OAK);
                    case SPRUCE:
                        return transferStateKey(stateIn, target.slab.getDefaultState(), BlockSlab.HALF).withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE);
                    case BIRCH:
                        return transferStateKey(stateIn, target.slab.getDefaultState(), BlockSlab.HALF).withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.BIRCH);
                    case JUNGLE:
                        return transferStateKey(stateIn, target.slab.getDefaultState(), BlockSlab.HALF).withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.JUNGLE);
                    case ACACIA:
                        return transferStateKey(stateIn, target.slab.getDefaultState(), BlockSlab.HALF).withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA);
                    case DARK_OAK:
                        return transferStateKey(stateIn, target.slab.getDefaultState(), BlockSlab.HALF).withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.DARK_OAK);
                    default:
                        return transferStateKey(stateIn, target.slab.getDefaultState(), BlockSlab.HALF);
                }
            case DOUBLESLAB:
                switch (target) {
                    case OAK:
                        return target.doubleSlab.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.OAK);
                    case SPRUCE:
                        return target.doubleSlab.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE);
                    case BIRCH:
                        return target.doubleSlab.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.BIRCH);
                    case JUNGLE:
                        return target.doubleSlab.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.JUNGLE);
                    case ACACIA:
                        return target.doubleSlab.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA);
                    case DARK_OAK:
                        return target.doubleSlab.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.DARK_OAK);
                    default:
                        return target.doubleSlab.getDefaultState();
                }
            case FENCE:
                return transferStateKeys(stateIn, target.fence .getDefaultState(), BlockFence.NORTH, BlockFence.EAST, BlockFence.WEST, BlockFence.SOUTH);
            case GATE:
                return transferStateKeys(stateIn, target.gate  .getDefaultState(), BlockFenceGate.FACING, BlockFenceGate.OPEN, BlockFenceGate.POWERED, BlockFenceGate.IN_WALL);
            case BUTTON:
                return transferStateKeys(stateIn, target.button.getDefaultState(), BlockButton.FACING, BlockButton.POWERED);
            case PLATE:
                return transferStateKey (stateIn, target.plate .getDefaultState(), BlockPressurePlate.POWERED);
            default:
                return stateIn; // Can't deal with this.
        }
    }

    public static IBlockState transferStateKeys(IBlockState stateIn, IBlockState stateOut, IProperty<?>... properties) {
        for (IProperty<?> property : properties) {
            stateOut = transferStateKey(stateIn, stateOut, property);
        }
        return stateOut;
    }

    public static <T extends Comparable<T>> IBlockState transferStateKey(IBlockState stateIn, IBlockState stateOut, IProperty<T> property) {
        return stateOut.withProperty(property, stateIn.getValue(property));
    }

    public static WoodShapes getWoodShapeFromBlock(Block b) {
        if (b instanceof BlockTF || b instanceof BlockPlanks)
            return WoodShapes.BLOCK;
        if (b instanceof BlockStairs  ) return WoodShapes.STAIRS;
        if (b instanceof BlockSlab    ) {
            if (((BlockSlab) b).isDouble()) return WoodShapes.DOUBLESLAB;
            else                            return WoodShapes.SLAB;
        }
        if (b instanceof BlockButton         ) return WoodShapes.BUTTON;
        if (b instanceof BlockFence          ) return WoodShapes.FENCE;
        if (b instanceof BlockFenceGate      ) return WoodShapes.GATE;
        if (b instanceof BlockPressurePlate  ) return WoodShapes.PLATE;

        return WoodShapes.INVALID;
    }

    public enum WoodShapes {
        BLOCK,
        STAIRS,
        SLAB,
        DOUBLESLAB,
        BUTTON,
        FENCE,
        GATE,
        PLATE,
        INVALID
    }
}
