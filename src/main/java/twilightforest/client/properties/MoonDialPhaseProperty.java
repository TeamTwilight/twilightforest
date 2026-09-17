package twilightforest.client.properties;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import twilightforest.components.item.MoonDialComponent;
import twilightforest.init.TFDataComponents;

public record MoonDialPhaseProperty() implements RangeSelectItemModelProperty {
	public static final MapCodec<MoonDialPhaseProperty> MAP_CODEC = MapCodec.unit(new MoonDialPhaseProperty());

	@Override
	public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		MoonDialComponent data = stack.get(TFDataComponents.MOON_DIAL);
		return data != null ? data.phase().index() : 0.0F;
	}

	@Override
	public @NonNull MapCodec<? extends RangeSelectItemModelProperty> type() {
		return MAP_CODEC;
	}
}