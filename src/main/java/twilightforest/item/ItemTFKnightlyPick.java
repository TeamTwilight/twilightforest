package twilightforest.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.IItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTFKnightlyPick extends PickaxeItem {

	protected ItemTFKnightlyPick(IItemTier material, Properties props) {
		super(material, 1, -2.8F, props);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flags) {
		super.addInformation(stack, world, list, flags);
		list.add(new TranslationTextComponent(getTranslationKey() + ".tooltip"));
	}
}
