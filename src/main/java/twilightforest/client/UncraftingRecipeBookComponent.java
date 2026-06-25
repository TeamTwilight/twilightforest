package twilightforest.client;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jetbrains.annotations.Nullable;
import twilightforest.inventory.UncraftingMenu;
import twilightforest.inventory.UncraftingPlaceRecipe;

import java.util.List;

public class UncraftingRecipeBookComponent extends RecipeBookComponent<UncraftingMenu> implements UncraftingPlaceRecipe<Ingredient> {

	public UncraftingRecipeBookComponent(UncraftingMenu menu) {
		super(menu, List.of());
	}

	@Override
	public void setupGhostRecipe(RecipeHolder<?> recipe, List<Slot> slots) {
		CraftingRecipe craftingRecipe = (CraftingRecipe) recipe.value();
		ItemStack itemstack = craftingRecipe.assemble(CraftingInput.EMPTY);
		this.placeRecipe(this.menu.getStateId(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, craftingRecipe.placementInfo().ingredients().iterator(), 0);
	}

	@Override
	protected WidgetSprites getFilterButtonTextures() {
		return null;
	}

	@Override
	protected boolean isCraftingSlot(Slot slot) {
		return false;
	}

	@Override
	protected void selectMatchingRecipes(RecipeCollection recipeCollection, StackedItemContents stackedItemContents) {

	}

	@Override
	protected Component getRecipeFilterName() {
		return null;
	}

	@Override
	protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {

	}

	@Override
	public void recipeClicked(ServerPlayer player, @Nullable RecipeHolder<?> recipe, boolean placeAll) {

	}

	@Override
	public void addItemToSlot(Ingredient ingredient, int slot, int maxAmount, int gridY, int gridX) {

	}
}
