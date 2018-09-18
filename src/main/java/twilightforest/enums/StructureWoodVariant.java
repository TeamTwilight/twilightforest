package twilightforest.enums;

import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;

import java.util.Locale;
import java.util.function.Supplier;

public enum StructureWoodVariant implements IStringSerializable {
    OAK          ( "minecraft"         , () -> Blocks  .PLANKS             , () -> Blocks  .OAK_STAIRS         , () -> Blocks  .WOODEN_SLAB            , () -> Blocks  .DOUBLE_WOODEN_SLAB, () -> Blocks  .WOODEN_BUTTON      , () -> Blocks  .OAK_FENCE         , () -> Blocks  .OAK_FENCE_GATE     , () -> Blocks  .WOODEN_PRESSURE_PLATE ),
    SPRUCE       ( "minecraft"         , () -> Blocks  .PLANKS             , () -> Blocks  .SPRUCE_STAIRS      , () -> Blocks  .WOODEN_SLAB            , () -> Blocks  .DOUBLE_WOODEN_SLAB, () -> Blocks  .WOODEN_BUTTON      , () -> Blocks  .SPRUCE_FENCE      , () -> Blocks  .SPRUCE_FENCE_GATE  , () -> Blocks  .WOODEN_PRESSURE_PLATE ),
    BIRCH        ( "minecraft"         , () -> Blocks  .PLANKS             , () -> Blocks  .BIRCH_STAIRS       , () -> Blocks  .WOODEN_SLAB            , () -> Blocks  .DOUBLE_WOODEN_SLAB, () -> Blocks  .WOODEN_BUTTON      , () -> Blocks  .BIRCH_FENCE       , () -> Blocks  .BIRCH_FENCE_GATE   , () -> Blocks  .WOODEN_PRESSURE_PLATE ),
    JUNGLE       ( "minecraft"         , () -> Blocks  .PLANKS             , () -> Blocks  .JUNGLE_STAIRS      , () -> Blocks  .WOODEN_SLAB            , () -> Blocks  .DOUBLE_WOODEN_SLAB, () -> Blocks  .WOODEN_BUTTON      , () -> Blocks  .JUNGLE_FENCE      , () -> Blocks  .JUNGLE_FENCE_GATE  , () -> Blocks  .WOODEN_PRESSURE_PLATE ),
    ACACIA       ( "minecraft"         , () -> Blocks  .PLANKS             , () -> Blocks  .ACACIA_STAIRS      , () -> Blocks  .WOODEN_SLAB            , () -> Blocks  .DOUBLE_WOODEN_SLAB, () -> Blocks  .WOODEN_BUTTON      , () -> Blocks  .ACACIA_FENCE      , () -> Blocks  .ACACIA_FENCE_GATE  , () -> Blocks  .WOODEN_PRESSURE_PLATE ),
    DARK_OAK     ( "minecraft"         , () -> Blocks  .PLANKS             , () -> Blocks  .DARK_OAK_STAIRS    , () -> Blocks  .WOODEN_SLAB            , () -> Blocks  .DOUBLE_WOODEN_SLAB, () -> Blocks  .WOODEN_BUTTON      , () -> Blocks  .DARK_OAK_FENCE    , () -> Blocks  .DARK_OAK_FENCE_GATE, () -> Blocks  .WOODEN_PRESSURE_PLATE ),

    TWILIGHT_OAK ( TwilightForestMod.ID, () -> TFBlocks.twilight_oak_planks, () -> TFBlocks.twilight_oak_stairs, () -> TFBlocks.twilight_oak_doubleslab, () -> TFBlocks.twilight_oak_slab , () -> TFBlocks.twilight_oak_button, () -> TFBlocks.twilight_oak_fence, () -> TFBlocks.twilight_oak_gate  , () -> TFBlocks.twilight_oak_plate    ),
    CANOPY       ( TwilightForestMod.ID, () -> TFBlocks.canopy_planks      , () -> TFBlocks.canopy_stairs      , () -> TFBlocks.canopy_doubleslab      , () -> TFBlocks.canopy_slab       , () -> TFBlocks.canopy_button      , () -> TFBlocks.canopy_fence      , () -> TFBlocks.canopy_gate        , () -> TFBlocks.canopy_plate          ),
    MANGROVE     ( TwilightForestMod.ID, () -> TFBlocks.mangrove_planks    , () -> TFBlocks.mangrove_stairs    , () -> TFBlocks.mangrove_doubleslab    , () -> TFBlocks.mangrove_slab     , () -> TFBlocks.mangrove_button    , () -> TFBlocks.mangrove_fence    , () -> TFBlocks.mangrove_gate      , () -> TFBlocks.mangrove_plate        ),
    DARK         ( TwilightForestMod.ID, () -> TFBlocks.dark_planks        , () -> TFBlocks.dark_stairs        , () -> TFBlocks.dark_doubleslab        , () -> TFBlocks.dark_slab         , () -> TFBlocks.dark_button        , () -> TFBlocks.dark_fence        , () -> TFBlocks.dark_gate          , () -> TFBlocks.dark_plate            ),
    TIME         ( TwilightForestMod.ID, () -> TFBlocks.time_planks        , () -> TFBlocks.time_stairs        , () -> TFBlocks.time_doubleslab        , () -> TFBlocks.time_slab         , () -> TFBlocks.time_button        , () -> TFBlocks.time_fence        , () -> TFBlocks.time_gate          , () -> TFBlocks.time_plate            ),
    TRANS        ( TwilightForestMod.ID, () -> TFBlocks.trans_planks       , () -> TFBlocks.trans_stairs       , () -> TFBlocks.trans_doubleslab       , () -> TFBlocks.trans_slab        , () -> TFBlocks.trans_button       , () -> TFBlocks.trans_fence       , () -> TFBlocks.trans_gate         , () -> TFBlocks.trans_plate           ),
    MINE         ( TwilightForestMod.ID, () -> TFBlocks.mine_planks        , () -> TFBlocks.mine_stairs        , () -> TFBlocks.mine_doubleslab        , () -> TFBlocks.mine_slab         , () -> TFBlocks.mine_button        , () -> TFBlocks.mine_fence        , () -> TFBlocks.mine_gate          , () -> TFBlocks.mine_plate            ),
    SORT         ( TwilightForestMod.ID, () -> TFBlocks.sort_planks        , () -> TFBlocks.sort_stairs        , () -> TFBlocks.sort_doubleslab        , () -> TFBlocks.sort_slab         , () -> TFBlocks.sort_button        , () -> TFBlocks.sort_fence        , () -> TFBlocks.sort_gate          , () -> TFBlocks.sort_plate            );

    StructureWoodVariant(
            String          nameSpace ,
            Supplier<Block> planks    ,
            Supplier<Block> stairs    ,
            Supplier<Block> slab      ,
            Supplier<Block> doubleSlab,
            Supplier<Block> button    ,
            Supplier<Block> fence     ,
            Supplier<Block> gate      ,
            Supplier<Block> plate
    ) {
        this.nameSpace  = nameSpace ;
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

    private final String          nameSpace ;
    private final Supplier<Block> planks    ;
    private final Supplier<Block> stairs    ;
    private final Supplier<Block> slab      ;
    private final Supplier<Block> doubleSlab;
    private final Supplier<Block> button    ;
    private final Supplier<Block> fence     ;
    private final Supplier<Block> gate      ;
    private final Supplier<Block> plate     ;

    public static IBlockState modifyBlockWithType(IBlockState stateIn, StructureWoodVariant target) {
        // get all data from stateIn
        Block block = stateIn.getBlock();
        WoodShapes shape = getWoodShapeFromBlock(block);

        ResourceLocation blockRegName = block.getRegistryName();

        if (blockRegName == null) return stateIn;

        if ("minecraft".equals(blockRegName.getNamespace()) && block instanceof BlockPlanks) {
            shape = WoodShapes.BLOCK;
        }

        if (shape == WoodShapes.INVALID || !TwilightForestMod.ID.equals(blockRegName.getNamespace())) {
            return stateIn; // Can't deal with this.
        }

        switch (shape) {
            case BLOCK:
                switch (target) {
                    case OAK:
                        return target.planks.get().getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK);
                    case SPRUCE:
                        return target.planks.get().getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
                    case BIRCH:
                        return target.planks.get().getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
                    case JUNGLE:
                        return target.planks.get().getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE);
                    case ACACIA:
                        return target.planks.get().getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA);
                    case DARK_OAK:
                        return target.planks.get().getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK);
                    default:
                        return target.planks.get().getDefaultState();
                }
            case STAIRS:
                return transferStateKeys(stateIn, target.stairs.get().getDefaultState(), BlockStairs.FACING, BlockStairs.HALF, BlockStairs.SHAPE);
            case SLAB:
                switch (target) {
                    case OAK:
                        return transferStateKey(stateIn, target.slab.get().getDefaultState(), BlockSlab.HALF).withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.OAK);
                    case SPRUCE:
                        return transferStateKey(stateIn, target.slab.get().getDefaultState(), BlockSlab.HALF).withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE);
                    case BIRCH:
                        return transferStateKey(stateIn, target.slab.get().getDefaultState(), BlockSlab.HALF).withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.BIRCH);
                    case JUNGLE:
                        return transferStateKey(stateIn, target.slab.get().getDefaultState(), BlockSlab.HALF).withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.JUNGLE);
                    case ACACIA:
                        return transferStateKey(stateIn, target.slab.get().getDefaultState(), BlockSlab.HALF).withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA);
                    case DARK_OAK:
                        return transferStateKey(stateIn, target.slab.get().getDefaultState(), BlockSlab.HALF).withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.DARK_OAK);
                    default:
                        return transferStateKey(stateIn, target.slab.get().getDefaultState(), BlockSlab.HALF);
                }
            case DOUBLESLAB:
                switch (target) {
                    case OAK:
                        return target.doubleSlab.get().getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.OAK);
                    case SPRUCE:
                        return target.doubleSlab.get().getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE);
                    case BIRCH:
                        return target.doubleSlab.get().getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.BIRCH);
                    case JUNGLE:
                        return target.doubleSlab.get().getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.JUNGLE);
                    case ACACIA:
                        return target.doubleSlab.get().getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA);
                    case DARK_OAK:
                        return target.doubleSlab.get().getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.DARK_OAK);
                    default:
                        return target.doubleSlab.get().getDefaultState();
                }
            case FENCE:
                return transferStateKeys(stateIn, target.fence.get().getDefaultState(), BlockFence.NORTH, BlockFence.EAST, BlockFence.WEST, BlockFence.SOUTH);
            case GATE:
                return transferStateKeys(stateIn, target.gate.get().getDefaultState(), BlockFenceGate.FACING, BlockFenceGate.OPEN, BlockFenceGate.POWERED, BlockFenceGate.IN_WALL);
            case BUTTON:
                return transferStateKeys(stateIn, target.button.get().getDefaultState(), BlockButton.FACING, BlockButton.POWERED);
            case PLATE:
                return transferStateKey(stateIn, target.plate.get().getDefaultState(), BlockPressurePlate.POWERED);
            default:
                return stateIn; // Can't deal with this.
        }
    }

    @SuppressWarnings("unchecked")
    public static IBlockState transferStateKeys(IBlockState stateIn, IBlockState stateOut, IProperty... properties) {
        for (IProperty property : properties) {
            stateOut = transferStateKey(stateIn, stateOut, property);
        }

        return stateOut;
    }

    public static <T extends Comparable<T>> IBlockState transferStateKey(IBlockState stateIn, IBlockState stateOut, IProperty<T> property) {
        return stateOut.withProperty(property, stateIn.getValue(property));
    }

    public static WoodShapes getWoodShapeFromBlock(Block b) {
        if (b instanceof BlockTF      ) return WoodShapes.BLOCK;
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
