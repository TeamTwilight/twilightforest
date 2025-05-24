package twilightforest.compat.jei.categories;

import it.unimi.dsi.fastutil.ints.IntList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.init.TFBlocks;
import twilightforest.inventory.UncraftingMenu;
import twilightforest.item.recipe.UncraftingRecipe;
import twilightforest.tags.TFItemTags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class JEIUncraftingCategory implements IRecipeCategory<CraftingRecipe> {
	public static final IRecipeType<CraftingRecipe> UNCRAFTING = IRecipeType.create(TwilightForestMod.prefix("uncrafting"), CraftingRecipe.class);
	private final IDrawable background;
	private final IDrawable icon;
	private final Component localizedName;

	public JEIUncraftingCategory(IGuiHelper guiHelper) {
		ResourceLocation location = TwilightForestMod.getGuiTexture("uncrafting_jei.png");
		this.background = guiHelper.createDrawable(location, 0, 0, RecipeViewerConstants.GENERIC_RECIPE_WIDTH, RecipeViewerConstants.GENERIC_RECIPE_HEIGHT);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()));
		this.localizedName = Component.translatable("gui.twilightforest.uncrafting_jei");
	}

	@Override
	public IRecipeType<CraftingRecipe> getRecipeType() {
		return UNCRAFTING;
	}

	@Override
	public Component getTitle() {
		return this.localizedName;
	}

	@Override
	@SuppressWarnings("removal")//FIXME?
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CraftingRecipe recipe, IFocusGroup focuses) {
		List<Ingredient> outputs = new ArrayList<>(recipe.placementInfo().ingredients()); //Collect each ingredient
		outputs.replaceAll(ingredient -> {
				Item[] array = extractItems(ingredient)
					.filter(o -> !o.is(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS)) //Remove any banned items
					.filter(o -> o.value().getCraftingRemainder(o.value().getDefaultInstance()).isEmpty()) //Can't uncraft into items that don't get used
					.map(Holder::value).toArray(Item[]::new);

				return array.length > 0 ? Ingredient.of(array) : null;
			}
		);

		IntList slotIndex = recipe.placementInfo().slotsToIngredientIndex();

		for (int j = 0, k = 0; j - k < slotIndex.size(); j++) {
			int x = j % 3, y = j / 3;
			if (recipe instanceof ShapedRecipe shapedRecipe && (UncraftingMenu.canCraftInDimensions(shapedRecipe, x, 3) || UncraftingMenu.canCraftInDimensions(shapedRecipe, 3, y))) {
				k++;
				continue;
			} //Skips empty spaces in shaped recipes

			int index = slotIndex.getInt(j - k);
			@Nullable Ingredient ingredient = index < 0 ? null : outputs.get(index);
			if (ingredient != null) builder.addSlot(RecipeIngredientRole.OUTPUT, x * 18 + 63, y * 18 + 1).add(ingredient); //Set input as output and place in the grid
		}

		switch (recipe) {
			case UncraftingRecipe uncraftingRecipe -> builder.addSlot(RecipeIngredientRole.INPUT, 5, 19).add(uncraftDisplay(uncraftingRecipe));//If the recipe is an uncrafting recipe, we need to get the ingredient instead of an itemStack
			case ShapedRecipe shapedRecipe -> builder.addSlot(RecipeIngredientRole.INPUT, 5, 19).add(shapedRecipe.result.copy()); //Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
			case ShapelessRecipe shapelessRecipe -> builder.addSlot(RecipeIngredientRole.INPUT, 5, 19).add(shapelessRecipe.result.copy()); //Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
			default -> {
			}
		}
	}

	@Override
	public void draw(CraftingRecipe recipe, IRecipeSlotsView views, GuiGraphics graphics, double mouseX, double mouseY) {
		int cost = recipe instanceof UncraftingRecipe ur ? ur.getCost() : RecipeViewerConstants.getRecipeCost(views.getSlotViews(RecipeIngredientRole.OUTPUT).stream().map(view -> view.getDisplayedItemStack().orElse(ItemStack.EMPTY)).toList());
		if (cost > 0) {
			String costStr = cost + "";
			graphics.drawString(Minecraft.getInstance().font, costStr, 45 - Minecraft.getInstance().font.width(costStr), 22, RecipeViewerConstants.getXPColor(cost), true);
		}
	}

	public static Stream<Holder<Item>> extractItems(Ingredient ingredient) {
		if (ingredient.isCustom()) return Objects.requireNonNull(ingredient.getCustomIngredient()).items();
		else return ingredient.getValues().stream();
	}

	public static SlotDisplay uncraftDisplay(UncraftingRecipe recipe) {
		List<SlotDisplay> displayList = new ArrayList<>();
		extractItems(recipe.getInput()).map(Holder::value).forEach(item -> displayList.add(new SlotDisplay.ItemStackSlotDisplay(new ItemStack(item, recipe.getCount()))));
		return new SlotDisplay.Composite(displayList);
	}
}