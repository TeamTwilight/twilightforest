package twilightforest.client.properties;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.util.RandomSource;
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
		RandomSource rand = RandomSource.create();
		if (OreMeterItem.isLoading(stack)) {
			int totalLoadTime = OreMeterItem.LOAD_TIME + OreMeterItem.getRange(stack) * 25;
			int progress = OreMeterItem.getLoadProgress(stack);
			if (progress > 30 && progress < 50) return false; //
			rand.setSeed(totalLoadTime + Math.round(progress / 10.0F));
			return progress >= 50 || progress % 10 >= rand.nextInt(10);
		}
		return stack.has(TFDataComponents.ORE_DATA);
	}

	@Override
	public MapCodec<? extends ConditionalItemModelProperty> type() {
		return TYPE;
	}
}
