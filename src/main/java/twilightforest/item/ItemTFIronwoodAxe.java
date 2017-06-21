package twilightforest.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFIronwoodAxe extends ItemAxe implements ModelRegisterCallback {

	protected ItemTFIronwoodAxe(Item.ToolMaterial material) {
		super(material, material.getDamageVsEntity(), -3.2F);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
		ItemStack istack = new ItemStack(item);
		istack.addEnchantment(Enchantments.FORTUNE, 1);
		list.add(istack);
	}
}
