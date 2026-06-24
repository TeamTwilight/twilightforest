package twilightforest.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.init.TFBlocks;
import twilightforest.item.recipe.UncraftingRecipe;
import twilightforest.tags.TFItemTags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JEIUncraftingCategory implements IRecipeCategory<@NotNull CraftingRecipe> {
	public static final IRecipeType<@NotNull CraftingRecipe> UNCRAFTING = IRecipeType.create(TwilightForestMod.ID, "uncrafting", CraftingRecipe.class);
	private final IDrawable arrow;
	private final IDrawable icon;
	private final Component localizedName;

	public JEIUncraftingCategory(IGuiHelper guiHelper) {
		this.arrow = guiHelper.getRecipeArrow();
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()));
		this.localizedName = Component.translatable("gui.twilightforest.uncrafting_jei");
	}

	@Override
	public IRecipeType<@NotNull CraftingRecipe> getRecipeType() {
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
		List<Ingredient> outputs = new ArrayList<>(recipe.placementInfo().ingredients()); //Collect each ingredient
		outputs.replaceAll(ingredient -> Ingredient.of(ingredient.getValues().stream()
			.filter(o -> !(o.is(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS)))
			.filter(o -> !(o.value().getDefaultInstance().getCraftingRemainder() == null)).map(Holder::value))//Remove any banned items
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
		List<RecipeDisplay> d = recipe.display();
		ShapedCraftingRecipeDisplay shapedDisplay = null;
		if (!d.isEmpty() && d.getFirst() instanceof ShapedCraftingRecipeDisplay) {
			shapedDisplay = (ShapedCraftingRecipeDisplay) d.getFirst();
		}
		for (int j = 0, k = 0; j - k < outputs.size() && j < 9; j++) {
			int x = j % 3;
			int y = j / 3;
			if (shapedDisplay != null) {
				if (x >= shapedDisplay.width() || y >= shapedDisplay.height()) {
					k++;
					continue;
				}
			}
			inputSlots.get(RecipeViewerConstants.getCraftingIndex(recipe, j - k)).add(outputs.get(j - k));
		}


		if (recipe instanceof UncraftingRecipe uncraftingRecipe) {
			ItemStack[] stacks = (ItemStack[]) uncraftingRecipe.getInput().getValues().stream().map(Holder::value).map(Item::getDefaultInstance).toArray();
			ItemStack[] stackedStacks = new ItemStack[stacks.length];
			for (int i = 0; i < stacks.length; i++) stackedStacks[i] = new ItemStack(stacks[0].getItem(), uncraftingRecipe.getCount());
			builder.addSlot(RecipeIngredientRole.INPUT, 5, 19).add(Ingredient.of(Arrays.stream(stackedStacks).map(ItemStack::getItem))).setOutputSlotBackground();//If the recipe is an uncrafting recipe, we need to get the ingredient instead of an itemStack
		} else {
			List<RecipeDisplay> displays = recipe.display();
			ItemStack resultStack = ItemStack.EMPTY;
			if (!displays.isEmpty()) {
				List<ItemStack> resolvedStacks = new ArrayList<>();
				displays.getFirst().result().resolve(
					net.minecraft.util.context.ContextMap.EMPTY,
					(DisplayContentsFactory.ForStacks<@NotNull ItemStack>) stack -> {
						resolvedStacks.add(stack);
						return stack;
					}
				);

				if (!resolvedStacks.isEmpty()) {
					resultStack = resolvedStacks.getFirst();
				}
			}
			builder.addSlot(RecipeIngredientRole.INPUT, 5, 19).add(resultStack).setOutputSlotBackground();
		}
	}

	@Override
	public void draw(CraftingRecipe recipe, IRecipeSlotsView views, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
		this.arrow.draw(graphics, 33, 18);
		int cost = recipe instanceof UncraftingRecipe ur ? ur.getCost() : RecipeViewerConstants.getRecipeCost(views.getSlotViews(RecipeIngredientRole.OUTPUT).stream().map(view -> view.getDisplayedItemStack().orElse(ItemStack.EMPTY)).toList());
		if (cost > 0) {
			String costStr = cost + "";
			graphics.text(Minecraft.getInstance().font, costStr, 45 - Minecraft.getInstance().font.width(costStr), 22, RecipeViewerConstants.getXPColor(cost), true);
		}
	}
}