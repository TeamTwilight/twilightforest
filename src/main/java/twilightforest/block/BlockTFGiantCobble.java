package twilightforest.block;

import net.minecraft.init.Blocks;
import twilightforest.item.TFItems;

public class BlockTFGiantCobble extends BlockTFGiantBlock {

	protected BlockTFGiantCobble() {
		super(Blocks.COBBLESTONE.getDefaultState());
		this.setHardness(2.0F * 64F);
		this.setResistance(10.0F);
		this.setCreativeTab(TFItems.creativeTab);
	}
}
