package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
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
		BlockPos pos = Minecraft.getInstance().player != null ? Minecraft.getInstance().player.blockPosition() : BlockPos.ZERO;
		builder.accept(getMoonPhase(context.level(), pos).withStyle(ChatFormatting.GRAY));
	}

	public static MutableComponent getMoonPhase(@Nullable Level level, BlockPos pos) {
		String phaseType;
		if (level != null && !level.dimensionType().hasFixedTime()) {
			MoonPhase phase = level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, pos);
			phaseType = String.valueOf(phase.index());
		} else {
			boolean aprilFools = LocalDate.of(LocalDate.now().getYear(), 4, 1).equals(LocalDate.now());
			phaseType = aprilFools ? "unknown_fools" : "unknown";
		}
		return Component.translatable("item.twilightforest.moon_dial.phase_" + phaseType);
	}
}