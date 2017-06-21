package twilightforest.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nonnull;

public class ItemTFIronwoodPick extends ItemPickaxe implements ModelRegisterCallback {

	protected ItemTFIronwoodPick(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public void getSubItems(@Nonnull Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
		ItemStack istack = new ItemStack(item);
		istack.addEnchantment(Enchantments.EFFICIENCY, 1);
		list.add(istack);
	}
}
