package twilightforest.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFSteeleafPick extends ItemPickaxe implements ModelRegisterCallback {

	protected ItemTFSteeleafPick(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
		ItemStack istack = new ItemStack(item);
		istack.addEnchantment(Enchantments.FORTUNE, 2);
		list.add(istack);
	}
}
