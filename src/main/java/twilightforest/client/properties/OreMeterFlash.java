package twilightforest.client.properties;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDataComponents;
import twilightforest.item.OreMeterItem;

public record OreMeterFlash() implements ConditionalItemModelProperty {

	public static final MapCodec<OreMeterFlash> TYPE = MapCodec.unit(new OreMeterFlash());

	@Override
	public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext context) {
		if (OreMeterItem.isLoading(stack)) {
			int totalLoadTime = OreMeterItem.LOAD_TIME + OreMeterItem.getRange(stack) * 25;
			int progress = OreMeterItem.getLoadProgress(stack);
			return progress % 5 >= 2 + (int) (Math.random() * 2) && progress <= totalLoadTime - 15;
		}
		return stack.has(TFDataComponents.ORE_DATA);
	}

	@Override
	public MapCodec<? extends ConditionalItemModelProperty> type() {
		return TYPE;
	}
}
