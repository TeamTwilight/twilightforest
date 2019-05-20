package twilightforest.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

public class BlockTFPillar extends BlockRotatedPillar implements ModelRegisterCallback {

	protected BlockTFPillar(Material material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y));
	}

}
