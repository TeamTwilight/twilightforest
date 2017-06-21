package twilightforest.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

public class BlockTFAuroraPillar extends BlockRotatedPillar implements ModelRegisterCallback {

	protected BlockTFAuroraPillar() {
		super(Material.PACKED_ICE);
		this.setCreativeTab(TFItems.creativeTab);
		this.setHardness(2.0F);
		this.setResistance(10.0F);
	}

}
