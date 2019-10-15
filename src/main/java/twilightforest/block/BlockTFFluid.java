package twilightforest.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;

public class BlockTFFluid extends BlockFluidClassic implements ModelRegisterCallback {

    public BlockTFFluid(Fluid fluid, Material material) {
        super(fluid, material);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        ModelLoader.setCustomStateMapper(this, new SingleStateMapper(new ModelResourceLocation(getRegistryName(), "fluid")));
    }
}
