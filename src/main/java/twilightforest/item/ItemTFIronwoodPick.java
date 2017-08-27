package twilightforest.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFIronwoodPick extends ItemPickaxe implements ModelRegisterCallback {

	protected ItemTFIronwoodPick(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			ItemStack istack = new ItemStack(this);
			istack.addEnchantment(Enchantments.EFFICIENCY, 1);
			list.add(istack);
		}
	}
}
