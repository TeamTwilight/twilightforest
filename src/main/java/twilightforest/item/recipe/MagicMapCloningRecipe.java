package twilightforest.item.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.init.TFItems;

public class MagicMapCloningRecipe extends CustomRecipe {
	public static final MapCodec<MagicMapCloningRecipe> MAP_CODEC =
		MapCodec.unit(MagicMapCloningRecipe::new);

	public static final StreamCodec<RegistryFriendlyByteBuf, MagicMapCloningRecipe> STREAM_CODEC =
		StreamCodec.unit(new MagicMapCloningRecipe());

	public static final RecipeSerializer<MagicMapCloningRecipe> SERIALIZER =
		new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	public MagicMapCloningRecipe() {
		super();
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		int i = 0;
		ItemStack itemstack = ItemStack.EMPTY;

		for (int j = 0; j < input.size(); j++) {
			ItemStack itemstack1 = input.getItem(j);
			if (!itemstack1.isEmpty()) {
				if (itemstack1.is(TFItems.FILLED_MAGIC_MAP.get())) {
					if (!itemstack.isEmpty()) {
						return false;
					}

					itemstack = itemstack1;
				} else {
					if (!itemstack1.is(TFItems.MAGIC_MAP.get())) {
						return false;
					}

					i++;
				}
			}
		}

		return !itemstack.isEmpty() && i > 0;
	}

	@Override
	public ItemStack assemble(CraftingInput craftingInput) {
		int i = 0;
		ItemStack itemstack = ItemStack.EMPTY;

		for (int j = 0; j < craftingInput.size(); j++) {
			ItemStack itemstack1 = craftingInput.getItem(j);
			if (!itemstack1.isEmpty()) {
				if (itemstack1.is(TFItems.FILLED_MAGIC_MAP.get())) {
					if (!itemstack.isEmpty()) {
						return ItemStack.EMPTY;
					}

					itemstack = itemstack1;
				} else {
					if (!itemstack1.is(TFItems.MAGIC_MAP.get())) {
						return ItemStack.EMPTY;
					}

					i++;
				}
			}
		}

		if (!itemstack.isEmpty() && i >= 1) {
			ItemStack itemstack2 = itemstack.copy();
			itemstack2.setCount(i + 1);
			return itemstack2;
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}
}
