package twilightforest.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.jei.JEICompat;
import twilightforest.compat.jei.renderers.EntityRenderer;
import twilightforest.compat.jei.util.TransformationRecipe;
import twilightforest.init.TFItems;

public class TransformationPowderCategory implements IRecipeCategory<TransformationRecipe> {
	public static final RecipeType<TransformationRecipe> TRANSFORMATION = RecipeType.create(TwilightForestMod.ID, "transformation_powder", TransformationRecipe.class);
	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable arrow;
	private final IDrawable doubleArrow;
	private final Component localizedName;
	private final EntityRenderer entityRenderer = new EntityRenderer(32);

	public TransformationPowderCategory(IGuiHelper helper) {
		ResourceLocation location = TwilightForestMod.getGuiTexture("transformation_jei.png");
		this.background = helper.createDrawable(location, 0, 0, RecipeViewerConstants.GENERIC_RECIPE_WIDTH, RecipeViewerConstants.GENERIC_RECIPE_HEIGHT);
		this.arrow = helper.createDrawable(location, 116, 0, 23, 15);
		this.doubleArrow = helper.createDrawable(location, 116, 16, 23, 15);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, TFItems.TRANSFORMATION_POWDER.get().getDefaultInstance());
		this.localizedName = Component.translatable("gui.twilightforest.transformation_jei");
	}

	@Override
	public RecipeType<TransformationRecipe> getRecipeType() {
		return TRANSFORMATION;
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

	@Override
	public void draw(TransformationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		if (recipe.isReversible()) {
			this.doubleArrow.draw(graphics, 46, 19);
		} else {
			this.arrow.draw(graphics, 46, 19);
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, TransformationRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 8, 11)
			.setCustomRenderer(JEICompat.ENTITY_TYPE, this.entityRenderer)
			.addIngredient(JEICompat.ENTITY_TYPE, recipe.input());

		SpawnEggItem inputEgg = DeferredSpawnEggItem.byId(recipe.input().type());
		if (inputEgg != null) {
			//make it so hovering over the entity shows its name
			builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(new ItemStack(inputEgg));
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 11)
			.setCustomRenderer(JEICompat.ENTITY_TYPE, this.entityRenderer)
			.addIngredient(JEICompat.ENTITY_TYPE, recipe.output());

		SpawnEggItem outputEgg = DeferredSpawnEggItem.byId(recipe.output().type());
		if (outputEgg != null) {
			//make it so hovering over the entity shows its name
			builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(new ItemStack(outputEgg));
		}
	}
}
