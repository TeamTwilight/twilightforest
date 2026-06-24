package twilightforest.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.plugins.vanilla.crafting.JeiShapedRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFBlocks;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JEIUncraftingCategory implements IRecipeCategory<CraftingRecipe> {
	public static final RecipeType<CraftingRecipe> UNCRAFTING = RecipeType.create(TwilightForestMod.ID, "uncrafting", CraftingRecipe.class);
	private final IDrawable arrow;
	private final IDrawable icon;
	private final Component localizedName;

	public JEIUncraftingCategory(IGuiHelper guiHelper) {
		this.arrow = guiHelper.getRecipeArrow();
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()));
		this.localizedName = Component.translatable("gui.twilightforest.uncrafting_jei");
	}

	@Override
	public RecipeType<CraftingRecipe> getRecipeType() {
		return UNCRAFTING;
	}

	@Override
	public Component getTitle() {
		return this.localizedName;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public int getWidth() {
		return RecipeViewerConstants.GENERIC_RECIPE_WIDTH;
	}

	@Override
	public int getHeight() {
		return RecipeViewerConstants.GENERIC_RECIPE_HEIGHT;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CraftingRecipe recipe, IFocusGroup focuses) {
		List<Ingredient> outputs = new ArrayList<>(recipe.getIngredients()); //Collect each ingredient
		outputs.replaceAll(ingredient -> Ingredient.of(Arrays.stream(ingredient.getItems())
			.filter(o -> !(o.is(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS)))
			.filter(o -> !(o.getItem().hasCraftingRemainingItem(o))))//Remove any banned items
		);

		//create all 9 slots to fill with items below
		List<IRecipeSlotBuilder> inputSlots = new ArrayList<>();
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				IRecipeSlotBuilder slot = builder.addInputSlot(x * 18 + 63, y * 18 + 1).setStandardSlotBackground();
				inputSlots.add(slot);
			}
		}

		//fill slots with items, if applicable
		for (int j = 0, k = 0; j - k < outputs.size() && j < 9; j++) {
			int x = j % 3, y = j / 3;
			if ((recipe.canCraftInDimensions(x, y)) && !(recipe instanceof ShapelessRecipe)) {
				k++;
				continue;
			} //Skips empty spaces in shaped recipes
			inputSlots.get(RecipeViewerConstants.getCraftingIndex(recipe, j - k)).addIngredients(outputs.get(j - k)); //Set input as output and place in the grid
		}

		if (recipe instanceof UncraftingRecipe uncraftingRecipe) {
			ItemStack[] stacks = uncraftingRecipe.getInput().getItems();
			ItemStack[] stackedStacks = new ItemStack[stacks.length];
			for (int i = 0; i < stacks.length; i++) stackedStacks[i] = new ItemStack(stacks[0].getItem(), uncraftingRecipe.getCount());
			builder.addSlot(RecipeIngredientRole.INPUT, 5, 19).addIngredients(Ingredient.of(stackedStacks)).setOutputSlotBackground();//If the recipe is an uncrafting recipe, we need to get the ingredient instead of an itemStack
		} else {
			builder.addSlot(RecipeIngredientRole.INPUT, 5, 19).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess())).setOutputSlotBackground();//Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
		}
	}

	@Override
	public void draw(CraftingRecipe recipe, IRecipeSlotsView views, GuiGraphics graphics, double mouseX, double mouseY) {
		this.arrow.draw(graphics, 33, 18);
		int cost = recipe instanceof UncraftingRecipe ur ? ur.getCost() : RecipeViewerConstants.getRecipeCost(views.getSlotViews(RecipeIngredientRole.OUTPUT).stream().map(view -> view.getDisplayedItemStack().orElse(ItemStack.EMPTY)).toList());
		if (cost > 0) {
			String costStr = cost + "";
			graphics.drawString(Minecraft.getInstance().font, costStr, 45 - Minecraft.getInstance().font.width(costStr), 22, RecipeViewerConstants.getXPColor(cost), true);
		}
	}
}