package twilightforest.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.HoeItem;

public class ItemTFIronwoodHoe extends HoeItem {

	public ItemTFIronwoodHoe(IItemTier material, Properties props) {
		super(material, 1, material.getEfficiency(), props);
	}
}
