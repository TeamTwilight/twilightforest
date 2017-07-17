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

    public final IBlockState stoneSlab = getSlab(Blocks.STONE_SLAB);
    public final IBlockState stoneSlabTop = getSlabTop(Blocks.STONE_SLAB);
    public final IBlockState stoneSlabDouble = getBlock(Blocks.DOUBLE_STONE_SLAB);

    public IBlockState birchSlab = getSlab(Blocks.WOODEN_SLAB).withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
    public IBlockState birchSlabTop = getSlabTop(Blocks.WOODEN_SLAB).withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
    public IBlockState birchPlanks = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);

    public <T extends Block> IBlockState getBlock(T type) {
        return type.getDefaultState();
    }

    private <T extends Block> IBlockState getSlabType(T type, BlockSlab.EnumBlockHalf side) {
        return type.getDefaultState().withProperty(BlockSlab.HALF, side);
    }

    public <T extends Block> IBlockState getSlab(T type) {
        return getSlabType(type, BlockSlab.EnumBlockHalf.BOTTOM);
    }

    public <T extends Block> IBlockState getSlabTop(T type) {
        return getSlabType(type, BlockSlab.EnumBlockHalf.TOP);
    }

    public IBlockState randomPlant(int i) {
        if(i < 4) return randomSapling(i);
        else return randomMushroom(i-4);
    }

    public IBlockState randomSapling(int i) {
        return Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.values()[i]);
    }

    public IBlockState randomMushroom(int i) {
        if(i == 0) return Blocks.RED_MUSHROOM.getDefaultState();
        else return Blocks.BROWN_MUSHROOM.getDefaultState();
    }


}
