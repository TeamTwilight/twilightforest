package twilightforest.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFIronwoodSword extends ItemSword implements ModelRegisterCallback {

	public ItemTFIronwoodSword(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}

    @Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
    	ItemStack istack = new ItemStack(par1, 1, 0);
    	istack.addEnchantment(Enchantments.KNOCKBACK, 1);
        par3List.add(istack);
    }
}
