package twilightforest.structures;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

/**
 * Created by Joseph on 7/16/2017.
 */
public class StructureTFHelper {

    public static final IBlockState stoneSlab = getSlab(Blocks.STONE_SLAB);
    public static final IBlockState stoneSlabTop = getSlabTop(Blocks.STONE_SLAB);
    public static final IBlockState stoneSlabDouble = Blocks.DOUBLE_STONE_SLAB.getDefaultState();

    public static final IBlockState birchSlab = getSlab(Blocks.WOODEN_SLAB).withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
    public static final IBlockState birchSlabTop = getSlabTop(Blocks.WOODEN_SLAB).withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
    public static final IBlockState birchPlanks = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);


    private static IBlockState getSlabType(Block type, BlockSlab.EnumBlockHalf side) {
        return type.getDefaultState().withProperty(BlockSlab.HALF, side);
    }


    public static IBlockState getSlab(Block type) {
        return getSlabType(type, BlockSlab.EnumBlockHalf.BOTTOM);
    }

    public static IBlockState getSlabTop(Block type) {
        return getSlabType(type, BlockSlab.EnumBlockHalf.TOP);
    }

    public static IBlockState randomPlant(int i) {
        if(i < 4) return randomSapling(i);
        else return randomMushroom(i-4);
    }

    public static IBlockState randomSapling(int i) {
        return Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.values()[i]);
    }

    public static IBlockState randomMushroom(int i) {
        if(i == 0) return Blocks.RED_MUSHROOM.getDefaultState();
        else return Blocks.BROWN_MUSHROOM.getDefaultState();
    }


}
