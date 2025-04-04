package twilightforest.client.properties;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import twilightforest.item.MoonwormQueenItem;

public record MoonwormQueenPulse() implements ConditionalItemModelProperty {

	public static final MapCodec<MoonwormQueenPulse> TYPE = MapCodec.unit(new MoonwormQueenPulse());

	@Override
	public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext context) {
		if (entity != null && entity.getUseItem() == stack) {
			int useTime = stack.getUseDuration(entity) - entity.getUseItemRemainingTicks();
			return useTime >= MoonwormQueenItem.FIRING_TIME && (useTime >>> 1) % 2 == 0;
		}
		return false;
	}

	@Override
	public MapCodec<? extends ConditionalItemModelProperty> type() {
		return TYPE;
	}
}
