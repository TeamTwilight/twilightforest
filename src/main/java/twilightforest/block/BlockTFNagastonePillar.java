package twilightforest.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

/**
 * Created by Drullkus on 12/12/17.
 */
public class BlockTFNagastonePillar extends BlockRotatedPillar implements ModelRegisterCallback {
    protected BlockTFNagastonePillar() {
        super(Material.ROCK);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(TFItems.creativeTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        ModelUtils.registerToState(this, 0, this.getDefaultState());
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this));
    }
}
