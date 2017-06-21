package twilightforest.block;

import net.minecraft.init.Blocks;
import twilightforest.item.TFItems;

public class BlockTFGiantObsidian extends BlockTFGiantBlock {

	protected BlockTFGiantObsidian() {
		super(Blocks.OBSIDIAN.getDefaultState());
		this.setHardness(50.0F * 64F * 64F);
		this.setResistance(2000.0F * 64F * 64F);
		this.setCreativeTab(TFItems.creativeTab);
	}
}
