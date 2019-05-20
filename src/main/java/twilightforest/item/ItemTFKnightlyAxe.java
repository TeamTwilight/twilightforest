package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTFKnightlyAxe extends ItemAxe implements ModelRegisterCallback {

	protected ItemTFKnightlyAxe(Item.ToolMaterial material) {
		super(material, 6F + material.getAttackDamage(), material.getEfficiency() * 0.05f - 3.4f);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltips, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltips, flags);
		tooltips.add(I18n.format(getTranslationKey() + ".tooltip"));
	}
}
