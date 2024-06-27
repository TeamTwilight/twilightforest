package twilightforest.compat.jei;

import net.minecraft.world.item.ItemStack;

//I have to wrap the itemstack in a class like this because otherwise it conflicts with JEI's VanillaTypes.ITEM_STACK
public record FakeItemEntity(ItemStack stack) {

}
