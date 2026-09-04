package twilightforest.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFBlocks;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.ArrayList;
import java.util.List;

public class JEIUncraftingCategory implements IRecipeCategory<RecipeHolder<CraftingRecipe>> {
	public static final IRecipeHolderType<CraftingRecipe> UNCRAFTING = IRecipeHolderType.create(TwilightForestMod.prefix("uncrafting"));
	private final IDrawable arrow;
	private final IDrawable icon;
	private final Component localizedName;

	public JEIUncraftingCategory(IGuiHelper guiHelper) {
		this.arrow = guiHelper.getRecipeArrow();
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()));
		this.localizedName = Component.translatable("gui.twilightforest.uncrafting_jei");
	}

	@Override
	public IRecipeType<RecipeHolder<CraftingRecipe>> getRecipeType() {
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
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CraftingRecipe> recipeHolder, IFocusGroup focuses) {
		CraftingRecipe recipe = recipeHolder.value();

		List<IRecipeSlotBuilder> gridSlots = new ArrayList<>();
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				gridSlots.add(builder.addSlot(RecipeIngredientRole.OUTPUT, x * 18 + 63, y * 18 + 1).setStandardSlotBackground());
			}
		}

		ContextMap context = gridSlots.getFirst().getContextMap();
		List<SlotDisplay> ingredients = getIngredientDisplays(recipe);
		int width = RecipeViewerConstants.getDisplayWidth(recipe);
		int height = RecipeViewerConstants.getDisplayHeight(recipe);

		for (int i = 0; i < ingredients.size() && i < gridSlots.size(); i++) {
			List<ItemStack> stacks = ingredients.get(i).resolveForStacks(context).stream()
				.filter(stack -> !stack.is(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS))
				.filter(stack -> stack.getCraftingRemainder() == null)
				.toList();
			if (!stacks.isEmpty()) {
				gridSlots.get(RecipeViewerConstants.getCraftingIndex(width, height, i)).addItemStacks(stacks);
			}
		}

		IRecipeSlotBuilder inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 5, 19).setOutputSlotBackground();
		if (recipe instanceof UncraftingRecipe uncraftingRecipe) {
			List<ItemStack> stacks = uncraftingRecipe.getInput().items()
				.map(item -> new ItemStack(item, uncraftingRecipe.getCount()))
				.toList();
			inputSlot.addItemStacks(stacks);
		} else {
			inputSlot.add(RecipeViewerConstants.getDisplayResult(recipe, context));
		}
	}

	private static List<SlotDisplay> getIngredientDisplays(CraftingRecipe recipe) {
		List<RecipeDisplay> displays = recipe.display();
		if (displays.isEmpty()) {
			return List.of();
		}
		return switch (displays.getFirst()) {
			case ShapedCraftingRecipeDisplay shaped -> shaped.ingredients();
			case ShapelessCraftingRecipeDisplay shapeless -> shapeless.ingredients();
			default -> List.of();
		};
	}

	@Override
	public void draw(RecipeHolder<CraftingRecipe> recipeHolder, IRecipeSlotsView views, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
		this.arrow.draw(graphics, 33, 18);
		int cost = recipeHolder.value() instanceof UncraftingRecipe ur ? ur.getCost() : RecipeViewerConstants.getRecipeCost(views.getSlotViews(RecipeIngredientRole.OUTPUT).stream().map(view -> view.getDisplayedItemStack().orElse(ItemStack.EMPTY)).toList());
		if (cost > 0) {
			String costStr = cost + "";
			graphics.text(Minecraft.getInstance().font, costStr, 45 - Minecraft.getInstance().font.width(costStr), 22, RecipeViewerConstants.getXPColor(cost), true);
		}
	}
}
