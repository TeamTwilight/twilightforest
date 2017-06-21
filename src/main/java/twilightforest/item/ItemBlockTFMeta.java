package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockTFMeta extends ItemBlock {

	public ItemBlockTFMeta(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int i) {
		return i;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		int meta = itemstack.getItemDamage();
		return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append(meta).toString();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);

		// add warning for [WIP] tag
		if (par1ItemStack.getDisplayName().contains("[WIP]")) {
			// TODO 1.10 localize these messages.
			par3List.add("This block is a work in progress");
			par3List.add("and may have bugs or unintended");
			par3List.add("effects that may damage your world.");
			par3List.add("Use with caution.");
		}
		// add warning for [NYI] tag
		if (par1ItemStack.getDisplayName().contains("[NYI]")) {
			par3List.add("This block has effects");
			par3List.add("that are not yet implemented.");
		}
	}
}
