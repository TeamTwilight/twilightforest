package twilightforest.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

/**
 * Created by Drullkus on 12/12/17.
 */
public class BlockTFNagastonePillar extends BlockTFDirectionalRotatedPillar implements ModelRegisterCallback {

    protected BlockTFNagastonePillar() {
        super(Material.ROCK);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(TFItems.creativeTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y).withProperty(REVERSED, false));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        ModelUtils.registerToState(this, 0, this.getDefaultState());
    }
}
