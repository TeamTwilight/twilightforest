package twilightforest.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3x2f;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.gui.GuiBlockRenderState;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.init.TFBlocks;
import twilightforest.item.recipe.DryingRecipe;

public class DryingCategory implements IRecipeCategory<DryingRecipe> {

	public static final IRecipeType<DryingRecipe> DRYING = IRecipeType.create(TwilightForestMod.ID, "drying", DryingRecipe.class);
	private final IDrawable icon;
	private final IDrawable arrow;
	private final Component localizedName;
	private final BlockState rack;

	public DryingCategory(IGuiHelper helper) {
		this.arrow = helper.createAnimatedRecipeArrow(20 * 60);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, TFBlocks.SORTING_DRYING_RACK.get().asItem().getDefaultInstance());
		this.localizedName = Component.translatable("gui.twilightforest.drying_jei");
		this.rack = TFBlocks.OAK_DRYING_RACK.get().defaultBlockState();
	}

	@Override
	public IRecipeType<DryingRecipe> getRecipeType() {
		return DRYING;
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
		return 70;
	}

	@Override
	public int getHeight() {
		return 30;
	}

	@Override
	public void draw(DryingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
		this.arrow.draw(graphics, 23, 1);

		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		Component time = RecipeViewerConstants.getDryingTime(recipe.getDryingTime());
		graphics.text(font, time, 35 - font.width(time.getString()) / 2, 20, 0xFF808080, false);

		Matrix3x2f pose = new Matrix3x2f(graphics.pose());
		ScreenRectangle scissor = graphics.peekScissorStack();
		graphics.submitPictureInPictureRenderState(new GuiBlockRenderState(this.rack, pose, -1, -1, 19, 19, 20.0F, scissor));
		graphics.submitPictureInPictureRenderState(new GuiBlockRenderState(this.rack, pose, 51, -1, 71, 19, 20.0F, scissor));
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, DryingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (mouseX > 23 && mouseX < 47 && mouseY > 1 && mouseY < 17) {
			tooltip.add(Component.translatable("gui.twilightforest.drying_ticks", recipe.getDryingTime()).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DryingRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).add(recipe.getInput());

		builder.addSlot(RecipeIngredientRole.OUTPUT, 53, 1).add(recipe.getResult());
	}
}
