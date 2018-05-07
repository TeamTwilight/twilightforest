package twilightforest.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import twilightforest.block.TFBlocks;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nonnull;

public class ItemTFMazebreakerPick extends ItemPickaxe implements ModelRegisterCallback {

	protected ItemTFMazebreakerPick(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			ItemStack istack = new ItemStack(this);
			istack.addEnchantment(Enchantments.EFFICIENCY, 4);
			istack.addEnchantment(Enchantments.UNBREAKING, 3);
			istack.addEnchantment(Enchantments.FORTUNE, 2);
			list.add(istack);
		}
	}

	@Override
	public float getStrVsBlock(@Nonnull ItemStack stack, IBlockState state) {
		float strVsBlock = super.getStrVsBlock(stack, state);
		return state.getBlock() == TFBlocks.maze_stone ? strVsBlock * 16F : strVsBlock;
	}
}
