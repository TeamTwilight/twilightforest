package twilightforest.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFSteeleafAxe extends ItemAxe implements ModelRegisterCallback {

	protected ItemTFSteeleafAxe(Item.ToolMaterial material) {
		super(material, material.getDamageVsEntity(), -3.0f);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			ItemStack istack = new ItemStack(this);
			istack.addEnchantment(Enchantments.EFFICIENCY, 2);
			list.add(istack);
		}
	}
}
