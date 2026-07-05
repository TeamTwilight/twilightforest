package twilightforest.client;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.Nullable;
import twilightforest.inventory.UncraftingPlaceRecipe;

import java.util.List;

public class UncraftingRecipeBookComponent extends RecipeBookComponent<RecipeBookMenu> implements UncraftingPlaceRecipe<Ingredient> {
	@Nullable
	private GhostSlots currentGhostSlots = new GhostSlots(() -> 0);

	public UncraftingRecipeBookComponent(RecipeBookMenu menu, List<TabInfo> list) {
		super(menu, list);
	}

	@Override
	protected WidgetSprites getFilterButtonTextures() {
		return RecipeBookComponent.RECIPE_BUTTON_SPRITES;
	}


	@Override
	protected boolean isCraftingSlot(Slot slot) {
		return slot.index >= 11 && slot.index <= 19;
	}

	@Override
	protected void selectMatchingRecipes(RecipeCollection recipeCollection, StackedItemContents stackedItemContents) {
		recipeCollection.selectRecipes(stackedItemContents, _ -> true);
		this.recipesUpdated();
	}


	@Override
	protected Component getRecipeFilterName() {
		return Component.translatable("gui.recipebook.toggleRecipes.craftable");
	}

	@Override
	protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
		this.currentGhostSlots = ghostSlots;

		Slot resultSlot = this.menu.slots.get(1);
		SlotDisplay resultDisplay = recipeDisplay.result();

		ghostSlots.setResult(resultSlot, contextMap, resultDisplay);

		List<ItemStack> ingredients = recipeDisplay.craftingStation().resolveForStacks(contextMap);
		int matrixStartSlot = 11;

		for (int i = 0; i < ingredients.size(); i++) {
			if (i >= 9) break;

			Slot inputSlot = this.menu.slots.get(matrixStartSlot + i);
			ItemStack ingredientDisplay = ingredients.get(i);

			if (!ingredientDisplay.isEmpty()) {
				ghostSlots.setInput(inputSlot, contextMap, new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(ingredientDisplay)));
			}
		}

		this.currentGhostSlots = null;
	}

	@Override
	public void addItemToSlot(Ingredient ingredient, int slotIndex, int maxAmount, int gridY, int gridX) {
		if (currentGhostSlots != null && !ingredient.isEmpty()) {
			Slot targetSlot = this.menu.slots.get(slotIndex);

			List<Holder<Item>> items = ingredient.getValues().stream().toList();
			if (!items.isEmpty()) {
				Item item = items.getFirst().value();
				ItemStack stack = new ItemStack(item);
				ItemStackTemplate template = ItemStackTemplate.fromNonEmptyStack(stack);
				SlotDisplay.ItemStackSlotDisplay display = new SlotDisplay.ItemStackSlotDisplay(template);

				this.currentGhostSlots.setInput(targetSlot, ContextMap.EMPTY, display);
			}
		}
	}
}
