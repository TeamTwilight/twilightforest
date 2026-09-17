package twilightforest.client.properties;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.MoonPhase;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import twilightforest.components.item.MoonDialComponent;
import twilightforest.init.TFDataComponents;

public record MoonDialPhaseProperty() implements RangeSelectItemModelProperty {
	public static final MapCodec<MoonDialPhaseProperty> MAP_CODEC = MapCodec.unit(MoonDialPhaseProperty::new);

	private static final int PHASE_COUNT = MoonPhase.values().length;
	private static final int TICKS_PER_PHASE = 1;

	@Override
	public float get(@NonNull ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		if (level == null) {
			return 0.0F; // General fallback
		}

		MoonDialComponent data = stack.get(TFDataComponents.MOON_DIAL);
		if (data != null && data.phase().isPresent()) {
			return data.phase().orElseThrow().index(); // Regular use
		}

		long time = level.getGameTime();
		return ((float) time / TICKS_PER_PHASE) % PHASE_COUNT; // Indeterminate
	}

	@Override
	public @NonNull MapCodec<? extends RangeSelectItemModelProperty> type() {
		return MAP_CODEC;
	}
}