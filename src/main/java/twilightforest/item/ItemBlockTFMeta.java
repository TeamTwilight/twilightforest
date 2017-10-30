package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockTFMeta extends ItemBlock {
	private boolean appendNumber = true;

	public ItemBlockTFMeta(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	public ItemBlockTFMeta setAppend(boolean doAppend) {
		this.appendNumber = doAppend;
		return this;
	}

	@Override
	public int getMetadata(int i) {
		return i;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if (appendNumber) {
			int meta = itemstack.getItemDamage();
			return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append(meta).toString();
		} else return super.getUnlocalizedName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);

		// add warning for [WIP] tag
		if (stack.getDisplayName().contains("[WIP]")) {
			tooltip.add(I18n.format("twilightforest.misc.wip0"));
			tooltip.add(I18n.format("twilightforest.misc.wip1"));
		}
		// add warning for [NYI] tag
		if (stack.getDisplayName().contains("[NYI]")) {
			tooltip.add(I18n.format("twilightforest.misc.nyi"));
		}
	}
}
