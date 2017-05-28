package twilightforest.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFIronwoodAxe extends ItemAxe implements ModelRegisterCallback {

	protected ItemTFIronwoodAxe(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial, par2EnumToolMaterial.getDamageVsEntity(), -3.2F);
		this.setCreativeTab(TFItems.creativeTab);
	}

    @Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
    	ItemStack istack = new ItemStack(par1, 1, 0);
    	istack.addEnchantment(Enchantments.FORTUNE, 1);
        par3List.add(istack);
    }
}
