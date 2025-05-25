package twilightforest.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.jei.FakeEntityType;
import twilightforest.compat.jei.renderers.EntityRenderer;
import twilightforest.compat.jei.util.TransformationRecipe;
import twilightforest.init.TFItems;

public class TransformationPowderCategory implements IRecipeCategory<TransformationRecipe> {
	public static final IRecipeType<TransformationRecipe> TRANSFORMATION = IRecipeType.create(TwilightForestMod.prefix("transformation_powder"), TransformationRecipe.class);
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
	public IRecipeType<TransformationRecipe> getRecipeType() {
		return TRANSFORMATION;
	}

	@Override
	public Component getTitle() {
		return this.localizedName;
	}

	@Override
	@SuppressWarnings("removal")
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
			.setCustomRenderer(FakeEntityType.ENTITY_TYPE, this.entityRenderer)
			.add(FakeEntityType.ENTITY_TYPE, recipe.input());

		SpawnEggItem inputEgg = SpawnEggItem.byId(BuiltInRegistries.ENTITY_TYPE.getValue(recipe.input().type()));
		if (inputEgg != null) {
			//make it so hovering over the entity shows its name
			builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).add(new ItemStack(inputEgg));
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 11)
			.setCustomRenderer(FakeEntityType.ENTITY_TYPE, this.entityRenderer)
			.add(FakeEntityType.ENTITY_TYPE, recipe.output());

		SpawnEggItem outputEgg = SpawnEggItem.byId(BuiltInRegistries.ENTITY_TYPE.getValue(recipe.output().type()));
		if (outputEgg != null) {
			//make it so hovering over the entity shows its name
			builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).add(new ItemStack(outputEgg));
		}
	}
}
