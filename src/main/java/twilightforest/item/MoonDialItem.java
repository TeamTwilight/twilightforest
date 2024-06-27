package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.time.LocalDate;
import java.util.List;

public class MoonDialItem extends Item {
	public MoonDialItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		boolean aprilFools = LocalDate.of(LocalDate.now().getYear(), 4, 1).equals(LocalDate.now());
		var level = Minecraft.getInstance().level; // FIXME Might need a different way to access the Minecraft level. Hope this method is never called on server
		String phaseType = (level != null && level.dimensionType().natural() ? String.valueOf(level.getMoonPhase()) : aprilFools ? "unknown_fools" : "unknown");
		tooltip.add(Component.translatable("item.twilightforest.moon_dial.phase_" + phaseType).withStyle(ChatFormatting.GRAY));
	}
}