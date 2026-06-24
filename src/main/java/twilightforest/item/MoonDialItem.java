package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.MoonPhase;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.function.Consumer;

public class MoonDialItem extends Item {
	public MoonDialItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		builder.accept(getMoonPhase(context.level()).withStyle(ChatFormatting.GRAY));
	}

	public static MutableComponent getMoonPhase(@Nullable Level level) {
		String phaseType;
		if (level != null && !level.dimensionType().hasFixedTime()) {
			MoonPhase phase = level.environmentAttributes().getDimensionValue(EnvironmentAttributes.MOON_PHASE);
			phaseType = phase.getSerializedName();
		} else {
			boolean aprilFools = LocalDate.of(LocalDate.now().getYear(), 4, 1).equals(LocalDate.now());
			phaseType = aprilFools ? "unknown_fools" : "unknown";
		}
		return Component.translatable("item.twilightforest.moon_dial.phase_" + phaseType);
	}
}