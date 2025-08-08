package twilightforest.item.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;

import java.util.List;
import java.util.Optional;

public class TravellersBeltWingsMergeRecipe extends CustomRecipe {
	public TravellersBeltWingsMergeRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		Optional<InputPair> pair = resolve(input);
		if (pair.isEmpty())
			return false;
		ItemStack wings = pair.get().wings();
		return TravellersModifiersManager.countInsertableModifiers(wings) + 1 <= maxSlots();
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		Optional<InputPair> pair = resolve(input);
		if (pair.isEmpty())
			return ItemStack.EMPTY;

		ItemStack wings = pair.get().wings();
		ItemStack result = new ItemStack(TFItems.TRAVELLERS_WINGS_BELT, 1, wings.getComponentsPatch());
		result.set(DataComponents.CONTAINER, pair.get().belt().get(DataComponents.CONTAINER));
		return result;
	}


	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.TRAVELLERS_BELT_WING_MERGE_RECIPE_SERIALIZER.get();
	}

	private int maxSlots() {
		return ((TravellersArmorBeltItem) TFItems.TRAVELLERS_WINGS_BELT.get()).getModifierSlots();
	}

	private Optional<InputPair> resolve(CraftingInput input) {
		List<ItemStack> items = input.items().stream().filter(stack -> !stack.isEmpty()).toList();
		if (items.size() != 2) return Optional.empty();

		Optional<ItemStack> wings = items.stream().filter(s -> s.is(TFItems.TRAVELLERS_WINGS.get())).findFirst();
		Optional<ItemStack> belt = items.stream().filter(s -> s.is(TFItems.TRAVELLERS_BELT.get())).findFirst();

		return wings.flatMap(w -> belt.map(b -> new InputPair(w, b)));
	}

	private record InputPair(ItemStack wings, ItemStack belt) {}
}
