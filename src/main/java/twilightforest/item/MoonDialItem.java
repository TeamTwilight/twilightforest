package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.MoonPhase;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFDataComponents;
import twilightforest.tags.TFDimensionTypeTags;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

public class MoonDialItem extends Item {

	public static final ResourceKey<DimensionType> DEFAULT_DIMENSION = BuiltinDimensionTypes.OVERWORLD;
	public static final ResourceKey<Level> DEFAULT_LEVEL = Level.OVERWORLD;

	public MoonDialItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (level.getGameTime() % 20 != 0) { // Let's not update too frequently...
			return;
		}

        ResourceKey<Level> levelResourceKey = stack.getOrDefault(TFDataComponents.MOON_DIAL_LEVEL, DEFAULT_LEVEL);
		ServerLevel monitoredLevel = levelResourceKey == level.dimension() ? level : level.getServer().getLevel(levelResourceKey);
        this.updatePhase(stack, owner.position(), monitoredLevel);
	}

	/**
	 * Right-clicked in a dimension to re-tune it to that dimension's moon phase, or else shift right-click to reset to OW
	 */
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.PASS;
		}

		if (!player.isShiftKeyDown() && level.dimensionTypeRegistration().is(TFDimensionTypeTags.MOON_DIAL_INDETERMINATE)) {
			return InteractionResult.FAIL;
		}

        ResourceKey<Level> tuneTarget = player.isShiftKeyDown() ? DEFAULT_LEVEL : level.dimension();
        if (tuneTarget == stack.getOrDefault(TFDataComponents.MOON_DIAL_LEVEL, DEFAULT_LEVEL)) {
            return InteractionResult.PASS;
        }

        if (tuneTarget != DEFAULT_LEVEL) {
			stack.set(TFDataComponents.MOON_DIAL_DIMENSION, level.dimensionTypeRegistration().getKey());
			stack.set(TFDataComponents.MOON_DIAL_LEVEL, level.dimension());
		} else {
			stack.remove(TFDataComponents.MOON_DIAL_DIMENSION);
			stack.remove(TFDataComponents.MOON_DIAL_LEVEL);
		}

        this.updatePhase(stack, player.position(), serverLevel);

		return InteractionResult.SUCCESS;
    }

	private void updatePhase(ItemStack stack, Vec3 samplePos, @Nullable ServerLevel monitoredLevel) {
		if (monitoredLevel == null || monitoredLevel.dimensionTypeRegistration().is(TFDimensionTypeTags.MOON_DIAL_INDETERMINATE)) {
			stack.set(TFDataComponents.MOON_DIAL_PHASE, Optional.empty());
		} else {
			MoonPhase moonPhase = monitoredLevel.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, samplePos);
			stack.set(TFDataComponents.MOON_DIAL_PHASE, Optional.of(moonPhase));
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		Optional<MoonPhase> moonPhase = stack.getOrDefault(TFDataComponents.MOON_DIAL_PHASE, Optional.empty());
		ResourceKey<Level> levelResourceKey = stack.getOrDefault(TFDataComponents.MOON_DIAL_LEVEL, DEFAULT_LEVEL);

		// Won't be wanting "Moon phase indeterminate in Overworld" to show on the tooltip, such as in JEI or fresh from a chest. Especially pairs with item model defaulting to the full moon
		if (!Level.OVERWORLD.equals(levelResourceKey) || moonPhase.isPresent()) {
            builder.accept(getDescriptionComponent(moonPhase.orElse(null), levelResourceKey));
        }
    }

	public static Component getDescriptionComponent(@Nullable MoonPhase phase, ResourceKey<Level> levelKey) {
		String key = phase != null ? "item.twilightforest.moon_dial.dimension" : "item.twilightforest.moon_dial.dimension_error";
		Component moonName = getMoonPhaseComponent(phase);
		Component dimensionName = Component.translatable(levelKey.identifier().toLanguageKey("dimension"));

        return Component.translatable(key, moonName, dimensionName).withStyle(ChatFormatting.GRAY);
	}

	public static Component getMoonPhaseComponent(@Nullable MoonPhase phase) {
		String phaseType = phase != null
			? String.valueOf(phase.index())
			: LocalDate.now().getMonthValue() == 4 && LocalDate.now().getDayOfMonth() == 1
			? "unknown_fools"
			: "unknown";

		return Component.translatable("item.twilightforest.moon_dial.phase_" + phaseType);
	}
}