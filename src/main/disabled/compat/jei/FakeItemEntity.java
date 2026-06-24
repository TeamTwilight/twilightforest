package twilightforest.compat.jei;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;

//I have to wrap the itemstack in a class like this because otherwise it conflicts with JEI's VanillaTypes.ITEM_STACK
public record FakeItemEntity(ItemStack stack) {
	public static final Codec<FakeItemEntity> CODEC = ItemStack.STRICT_SINGLE_ITEM_CODEC.xmap(
		FakeItemEntity::new,
		FakeItemEntity::stack
	);
}
