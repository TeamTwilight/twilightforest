package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.client.ModelRegisterCallback;

import java.util.Random;

public abstract class BlockTFSlab<T extends Enum<T>> extends BlockSlab implements ModelRegisterCallback {

    private final Comparable<T> propertyValue;

    BlockTFSlab(Material material, MapColor mapColor, T propertyValue) {
        super(material, mapColor);
        this.propertyValue = propertyValue;

        if (this.isDouble()) this.setDefaultState(this.blockState.getBaseState().withProperty(this.getVariantProperty(), propertyValue));
        else this.setDefaultState(this.blockState.getBaseState().withProperty(this.getVariantProperty(), propertyValue).withProperty(HALF, EnumBlockHalf.BOTTOM));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, this.getVariantProperty()) : new BlockStateContainer(this, this.getVariantProperty(), HALF);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(getSingle());
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(getSingle()));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.isDouble() ? this.getDefaultState() : this.getDefaultState().withProperty(HALF, meta == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return this.isDouble() ? 0 : state.getValue(HALF) == EnumBlockHalf.BOTTOM ? 0 : 1;
    }

    @Override
    public String getTranslationKey(int meta) {
        return this.getTranslationKey();
    }

    @Override
    public abstract boolean isDouble();

    @Override
    public abstract IProperty<T> getVariantProperty();

    @Override
    public Comparable<T> getTypeForItem(ItemStack stack) {
        return propertyValue;
    }

    protected abstract Block getSingle();

    @Override
    public Block setSoundType(SoundType sound) {
        return super.setSoundType(sound);
    }
}
