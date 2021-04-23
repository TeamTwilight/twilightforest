package twilightforest.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.IItemTier;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTFKnightlyAxe extends AxeItem {

	protected ItemTFKnightlyAxe(IItemTier material, Properties props) {
		super(material, 6F, material.getEfficiency() * 0.05f - 3.4f, props);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltips, flags);
		tooltips.add(new TranslationTextComponent(getTranslationKey() + ".tooltip"));
	}
}
