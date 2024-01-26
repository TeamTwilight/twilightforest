package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KnightmetalPickItem extends PickaxeItem {

	public KnightmetalPickItem(Tier material, Properties properties) {
		super(material, 1, -2.8F, properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flags) {
		super.appendHoverText(stack, level, list, flags);
		list.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}
}