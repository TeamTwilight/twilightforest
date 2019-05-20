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
		super(material, 6F + material.getAttackDamage(), material.getEfficiency() * 0.05f - 3.4f);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			ItemStack istack = new ItemStack(this);
			istack.addEnchantment(Enchantments.FORTUNE, 1);
			list.add(istack);
		}
	}
}
