package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
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
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);

		// add warning for [WIP] tag
		if (stack.getDisplayName().contains("[WIP]")) {
			// TODO 1.10 localize these messages.
			tooltip.add("This block is a work in progress");
			tooltip.add("and may have bugs or unintended");
			tooltip.add("effects that may damage your world.");
			tooltip.add("Use with caution.");
		}
		// add warning for [NYI] tag
		if (stack.getDisplayName().contains("[NYI]")) {
			tooltip.add("This block has effects");
			tooltip.add("that are not yet implemented.");
		}
	}
}
