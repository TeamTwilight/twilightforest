package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;

public class MoonDialItem extends Item {
	public MoonDialItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(getMoonPhase(context.level()).withStyle(ChatFormatting.GRAY));
	}

	public static MutableComponent getMoonPhase(@Nullable Level level) {
		boolean aprilFools = LocalDate.of(LocalDate.now().getYear(), 4, 1).equals(LocalDate.now());
		String phaseType = (level != null && level.dimensionType().natural() ? String.valueOf(level.getMoonPhase()) : aprilFools ? "unknown_fools" : "unknown");
		return Component.translatable("item.twilightforest.moon_dial.phase_" + phaseType);
	}
}