package twilightforest.compat.jei.categories;

import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.MoonwormQueenRepairRecipe;

public class MoonwormQueenCategory implements IRecipeCategory<MoonwormQueenRepairRecipe> {

	public static final RecipeType<MoonwormQueenRepairRecipe> MOONWORM_QUEEN = RecipeType.create(TwilightForestMod.ID, "moonworm_queen_repair", MoonwormQueenRepairRecipe.class);
	private final IDrawable background;
	private final IDrawable icon;
	private final Component localizedName;

	public MoonwormQueenCategory(IGuiHelper guiHelper) {
		ResourceLocation location = ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "textures/jei/gui/gui_vanilla.png");
		this.background = guiHelper.createDrawable(location, 0, 60, RecipeViewerConstants.GENERIC_RECIPE_WIDTH, RecipeViewerConstants.GENERIC_RECIPE_HEIGHT);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TFItems.MOONWORM_QUEEN.get()));
		this.localizedName = Component.translatable("gui.twilightforest.moonworm_queen_jei");
	}

	@Override
	public RecipeType<MoonwormQueenRepairRecipe> getRecipeType() {
		return MOONWORM_QUEEN;
	}

	@Override
	public Component getTitle() {
		return this.localizedName;
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@SuppressWarnings("removal") //liar
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, MoonwormQueenRepairRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStack(RecipeViewerConstants.DAMAGED_MOONWORM_QUEEN);

		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 19, 1).addItemStack(new ItemStack(TFItems.TORCHBERRIES.get()));
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 37, 1).addItemStacks(RecipeViewerConstants.BERRY_2_LIST);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 1, 19).addItemStacks(RecipeViewerConstants.BERRY_3_LIST);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 19, 19).addItemStacks(RecipeViewerConstants.BERRY_4_LIST);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 37, 19).addItemStack(ItemStack.EMPTY);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 1, 37).addItemStack(ItemStack.EMPTY);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 19, 37).addItemStack(ItemStack.EMPTY);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 37, 37).addItemStack(ItemStack.EMPTY);

		builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19).addItemStacks(RecipeViewerConstants.MOONWORM_QUEEN_LIST).addTooltipCallback((view, tooltip) -> tooltip.add(RecipeViewerConstants.MOONWORM_QUEEN_TOOLTIP));
	}
}
