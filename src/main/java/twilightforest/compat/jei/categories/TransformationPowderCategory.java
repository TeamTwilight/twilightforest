package twilightforest.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.jei.JEICompat;
import twilightforest.compat.jei.renderers.EntityRenderer;
import twilightforest.compat.jei.util.TransformationRecipe;
import twilightforest.init.TFItems;

public class TransformationPowderCategory implements IRecipeCategory<TransformationRecipe> {
	public static final IRecipeType<TransformationRecipe> TRANSFORMATION = IRecipeType.create(TwilightForestMod.ID, "transformation_powder", TransformationRecipe.class);
	private final IDrawable icon;
	private final IDrawable arrow;
	private final IDrawable slot;
	private final IDrawable doubleArrow;
	private final Component localizedName;
	private final EntityRenderer entityRenderer = new EntityRenderer(32);

	public TransformationPowderCategory(IGuiHelper helper) {
		this.arrow = helper.drawableBuilder(TwilightForestMod.getGuiTexture("transformation_arrow.png"), 0, 0, 23, 30).setTextureSize(23, 30).build();
		this.doubleArrow = helper.drawableBuilder(TwilightForestMod.getGuiTexture("transformation_double_arrow.png"), 0, 0, 23, 30).setTextureSize(23, 30).build();
		this.slot = helper.drawableBuilder(TwilightForestMod.getGuiTexture("big_slot.png"), 0, 0, 34, 34).setTextureSize(34, 34).build();
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, TFItems.TRANSFORMATION_POWDER.get().getDefaultInstance());
		this.localizedName = Component.translatable("gui.twilightforest.transformation_jei");
	}

	@Override
	public IRecipeType<TransformationRecipe> getRecipeType() {
		return TRANSFORMATION;
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
	public void draw(TransformationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
		if (recipe.isReversible()) {
			this.doubleArrow.draw(graphics, 46, 7);
		} else {
			this.arrow.draw(graphics, 46, 7);
		}
		this.slot.draw(graphics, 7, 10);
		this.slot.draw(graphics, 75, 10);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, TransformationRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 8, 11)
			.setCustomRenderer(JEICompat.ENTITY_TYPE, this.entityRenderer)
			.add(JEICompat.ENTITY_TYPE, recipe.input());

		//make it so hovering over the entity shows its name
		SpawnEggItem.byId(recipe.input().type()).ifPresent(egg -> builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).add(new ItemStack(egg)));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 11)
			.setCustomRenderer(JEICompat.ENTITY_TYPE, this.entityRenderer)
			.add(JEICompat.ENTITY_TYPE, recipe.output());

		//make it so hovering over the entity shows its name
		SpawnEggItem.byId(recipe.output().type()).ifPresent(egg -> builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).add(new ItemStack(egg)));
	}
}
