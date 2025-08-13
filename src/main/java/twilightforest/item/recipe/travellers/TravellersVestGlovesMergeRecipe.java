package twilightforest.item.recipe.travellers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;

import java.util.List;
import java.util.Optional;

public class TravellersVestGlovesMergeRecipe extends CustomRecipe {
	public TravellersVestGlovesMergeRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		Optional<InputPair> pair = this.resolve(input);
		if (pair.isEmpty())
			return false;
		ItemStack vest = pair.get().vest();
		return !vest.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		Optional<InputPair> pair = this.resolve(input);
		if (pair.isEmpty())
			return ItemStack.EMPTY;

		ItemStack vest = pair.get().vest();
		ItemStack result = new ItemStack(TFItems.TRAVELLERS_CHESTPLATE_GLOVES, 1, vest.getComponentsPatch());
		result.set(TFDataComponents.TRAVELLERS_HAS_GLOVES, Unit.INSTANCE);
		return vest;
	}


	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.TRAVELLERS_VEST_GLOVES_MERGE_RECIPE_SERIALIZER.get();
	}

	private Optional<InputPair> resolve(CraftingInput input) {
		List<ItemStack> items = input.items().stream().filter(stack -> !stack.isEmpty()).toList();
		if (items.size() != 2) return Optional.empty();

		Optional<ItemStack> vest = items.stream().filter(s -> s.is(TFItems.TRAVELLERS_CHESTPLATE.get())).findFirst();
		Optional<ItemStack> gloves = items.stream().filter(s -> s.is(TFItems.TRAVELLERS_GLOVES.get())).findFirst();

		return vest.flatMap(w -> gloves.map(b -> new InputPair(w, b)));
	}

	private record InputPair(ItemStack vest, ItemStack gloves) {}
}
