package twilightforest.advancements.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.SingleComponentItemPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public record ItemColorPredicate(int color) implements SingleComponentItemPredicate<DyedItemColor> {

	public static final Codec<ItemColorPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.optionalFieldOf("color", -1).forGetter(ItemColorPredicate::color))
		.apply(instance, ItemColorPredicate::new));

	@Override
	public DataComponentType<DyedItemColor> componentType() {
		return DataComponents.DYED_COLOR;
	}

	@Override
	public boolean matches(ItemStack stack, DyedItemColor value) {
		if (stack.has(DataComponents.DYED_COLOR) && this.color() == -1) return true;
		return value.rgb() == this.color();
	}

	public static ItemColorPredicate anyColor() {
		return new ItemColorPredicate(-1);
	}

	public static ItemColorPredicate withColor(int color) {
		return new ItemColorPredicate(color);
	}
}
