package twilightforest.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import twilightforest.block.TFBlocks;

public class CreativeTabTwilightForest extends CreativeTabs {

	public CreativeTabTwilightForest(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(TFBlocks.fireflyJar);
	}
}
