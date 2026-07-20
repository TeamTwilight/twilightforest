package twilightforest.item.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.init.TFItems;

import java.util.ArrayList;
import java.util.List;

public class MoonwormQueenRepairRecipe extends CustomRecipe {
	public static final MapCodec<MoonwormQueenRepairRecipe> MAP_CODEC =
		MapCodec.unit(MoonwormQueenRepairRecipe::new);

	public static final StreamCodec<RegistryFriendlyByteBuf, MoonwormQueenRepairRecipe> STREAM_CODEC =
		StreamCodec.unit(new MoonwormQueenRepairRecipe());

	public static final RecipeSerializer<MoonwormQueenRepairRecipe> SERIALIZER =
		new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	public MoonwormQueenRepairRecipe() {
		super();
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack queen = null;
		List<ItemStack> berries = new ArrayList<>();

		for (int i = 0; i < input.size(); ++i) {
			ItemStack stackInQuestion = input.getItem(i);
			if (!stackInQuestion.isEmpty()) {
				if (stackInQuestion.is(TFItems.MOONWORM_QUEEN.get()) && stackInQuestion.isDamaged()) {
					if (queen != null) return false;
					queen = stackInQuestion;
				} else if (stackInQuestion.is(TFItems.TORCHBERRIES.get())) {
					berries.add(stackInQuestion);
				} else {
					return false;
				}
			}
		}
		return queen != null && !berries.isEmpty();
	}

	@Override
	public ItemStack assemble(CraftingInput craftingInput) {
		List<Item> berries = new ArrayList<>();
		ItemStack queen = null;
		for (int i = 0; i < craftingInput.size(); ++i) {
			ItemStack itemstack = craftingInput.getItem(i);
			if (!itemstack.isEmpty()) {
				if (itemstack.is(TFItems.MOONWORM_QUEEN.get())) {
					if (queen == null) {
						queen = itemstack;
					} else {
						//Only accept 1 queen
						return ItemStack.EMPTY;
					}
				}

				if (itemstack.is(TFItems.TORCHBERRIES.get())) {
					//add all berries in the grid to a list to determine the amount to repair
					berries.add(itemstack.getItem());
				}
			}
		}

		if (!berries.isEmpty() && queen != null && queen.isDamaged()) {
			ItemStack newQueen = TFItems.MOONWORM_QUEEN.get().getDefaultInstance();
			//each berry repairs 64 durability
			newQueen.setDamageValue(queen.getDamageValue() - (berries.size() * 64));
			return newQueen;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}
}
