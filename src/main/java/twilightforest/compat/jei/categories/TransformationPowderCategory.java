package twilightforest.compat.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import twilightforest.TwilightForestMod;
import twilightforest.compat.jei.renderers.EntityRenderer;
import twilightforest.compat.jei.JEICompat;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.TransformPowderRecipe;

public class TransformationPowderCategory implements IRecipeCategory<TransformPowderRecipe> {
	public static final RecipeType<TransformPowderRecipe> TRANSFORMATION = RecipeType.create(TwilightForestMod.ID, "transformation", TransformPowderRecipe.class);
	public static final int WIDTH = 116;
	public static final int HEIGHT = 54;
	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable arrow;
	private final IDrawable doubleArrow;
	private final Component localizedName;
	private final EntityRenderer entityRenderer = new EntityRenderer(32);

	public TransformationPowderCategory(IGuiHelper helper) {
		ResourceLocation location = TwilightForestMod.getGuiTexture("transformation_jei.png");
		this.background = helper.createDrawable(location, 0, 0, WIDTH, HEIGHT);
		this.arrow = helper.createDrawable(location, 116, 0, 23, 15);
		this.doubleArrow = helper.createDrawable(location, 116, 16, 23, 15);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, TFItems.TRANSFORMATION_POWDER.get().getDefaultInstance());
		this.localizedName = Component.translatable("gui.twilightforest.transformation_jei");
	}

	@Override
	public RecipeType<TransformPowderRecipe> getRecipeType() {
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
	public void draw(TransformPowderRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
		if (recipe.isReversible()) {
			this.doubleArrow.draw(stack, 46, 19);
		} else {
			this.arrow.draw(stack, 46, 19);
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, TransformPowderRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 8, 11)
				.setCustomRenderer(JEICompat.ENTITY_TYPE, this.entityRenderer)
				.addIngredient(JEICompat.ENTITY_TYPE, recipe.input());

		//make it so hovering over the entity shows its name
		builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(new ItemStack(ForgeSpawnEggItem.fromEntityType(recipe.input())));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 11)
				.setCustomRenderer(JEICompat.ENTITY_TYPE, this.entityRenderer)
				.addIngredient(JEICompat.ENTITY_TYPE, recipe.result());

		//make it so hovering over the entity shows its name
		builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(new ItemStack(ForgeSpawnEggItem.fromEntityType(recipe.result())));
	}
}
