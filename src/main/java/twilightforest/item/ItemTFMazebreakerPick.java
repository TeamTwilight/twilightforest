package twilightforest.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import twilightforest.block.TFBlocks;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFMazebreakerPick extends ItemPickaxe implements ModelRegisterCallback {

	protected ItemTFMazebreakerPick(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
    	ItemStack istack = new ItemStack(par1, 1, 0);
    	istack.addEnchantment(Enchantments.EFFICIENCY, 4);
    	istack.addEnchantment(Enchantments.UNBREAKING, 3);
    	istack.addEnchantment(Enchantments.FORTUNE, 2);
        par3List.add(istack);
    }

	@Override
	public float getStrVsBlock(ItemStack par1ItemStack, IBlockState state) {
		float strVsBlock = super.getStrVsBlock(par1ItemStack, state);
		// 16x strength vs mazestone
		return state.getBlock() == TFBlocks.mazestone ? strVsBlock * 16F : strVsBlock;
	}
}
