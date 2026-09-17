package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.MoonPhase;
import org.jspecify.annotations.Nullable;
import twilightforest.components.item.MoonDialComponent;
import twilightforest.init.TFDataComponents;
import twilightforest.tags.TFDimensionTypeTags;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

public class MoonDialItem extends Item {
	public MoonDialItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.SUCCESS;
		}

		MoonDialComponent data = stack.get(TFDataComponents.MOON_DIAL);
		MoonDialComponent.DisplayMode current = data != null ? data.mode() : MoonDialComponent.DisplayMode.CURRENT_DIMENSION;

		MoonDialComponent.DisplayMode mode = player.isShiftKeyDown()
			? (current == MoonDialComponent.DisplayMode.CURRENT_DIMENSION ? MoonDialComponent.DisplayMode.OVERWORLD : MoonDialComponent.DisplayMode.CURRENT_DIMENSION)
			: current;

		attune(stack, serverLevel, mode);
		return InteractionResult.SUCCESS;
	}

	private static void attune(ItemStack stack, ServerLevel level, MoonDialComponent.DisplayMode mode) {
		ResourceKey<Level> dimension;
		Optional<MoonPhase> phase;

		if (mode == MoonDialComponent.DisplayMode.OVERWORLD) {
			ServerLevel overworld = level.getServer().overworld();
			dimension = overworld.dimension();
			phase = Optional.of(overworld.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, BlockPos.ZERO));
		} else if (level.dimensionTypeRegistration().is(TFDimensionTypeTags.MOON_DIAL_INDETERMINATE)) {
			dimension = level.dimension();
			phase = Optional.empty();
		} else {
			dimension = level.dimension();
			phase = Optional.of(level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, BlockPos.ZERO));
		}

		stack.set(TFDataComponents.MOON_DIAL.get(), new MoonDialComponent(dimension, phase, mode));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		MoonDialComponent data = stack.get(TFDataComponents.MOON_DIAL);

		builder.accept(getMoonPhaseComponent(data != null ? data.phase().orElse(null) : null).withStyle(ChatFormatting.GRAY)); // Indeterminate if null

		if (data == null) {
			return; // Not yet attuned (e.g. in the creative menu)
		}

		builder.accept(
			Component.translatable(
				"item.twilightforest.moon_dial.dimension",
				Component.translatable(
					"dimension." + data.dimension().identifier().toString().replace(':', '.')
				)
			).withStyle(ChatFormatting.GRAY)
		);
	}

	public static MutableComponent getMoonPhaseComponent(@Nullable MoonPhase phase) {
		String phaseType = phase != null
			? String.valueOf(phase.index())
			: LocalDate.now().getMonthValue() == 4 && LocalDate.now().getDayOfMonth() == 1
			? "unknown_fools"
			: "unknown";

		return Component.translatable("item.twilightforest.moon_dial.phase_" + phaseType);
	}
}