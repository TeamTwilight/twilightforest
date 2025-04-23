package twilightforest.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Constants;
import mezz.jei.common.util.Translator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.jei.FakeItemEntity;
import twilightforest.compat.jei.JEICompat;
import twilightforest.compat.jei.renderers.FakeItemEntityRenderer;
import twilightforest.compat.jei.util.CrumbleRecipe;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.DryingRecipe;

import java.util.List;

public class DryingCategory implements IRecipeCategory<DryingRecipe> {

	public static final RecipeType<DryingRecipe> DRYING = RecipeType.create(TwilightForestMod.ID, "drying", DryingRecipe.class);
	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable slot;
	private final IDrawable arrow;
	private final Component localizedName;

	public DryingCategory(IGuiHelper helper) {
		this.background = helper.createBlankDrawable(70, 30);
		this.slot = helper.getSlotDrawable();
		this.arrow = helper.createAnimatedRecipeArrow(20 * 60);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, TFBlocks.SORTING_DRYING_RACK.get().asItem().getDefaultInstance());
		this.localizedName = Component.translatable("gui.twilightforest.drying_jei");
	}

	@Override
	public RecipeType<DryingRecipe> getRecipeType() {
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
	public void draw(DryingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		this.background.draw(graphics);
		this.slot.draw(graphics);
		this.slot.draw(graphics, 52, 0);
		this.arrow.draw(graphics, 23, 1);

		float dryingTicks = recipe.getDryingTime();
		int dryingMinutes = (int) Math.floor(dryingTicks / 60 / 20);
		Component time = Component.translatable("gui.twilightforest.drying_minutes", dryingMinutes);

		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		graphics.drawString(font, time, 35 - font.width(time.getString()) / 2, 20, 0xFF808080, false);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, DryingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (mouseX > 23 && mouseX < 47 && mouseY > 1 && mouseY < 17) {
			tooltip.add(Component.translatable("gui.twilightforest.drying_ticks", recipe.getDryingTime()).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DryingRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().getFirst());

		builder.addSlot(RecipeIngredientRole.OUTPUT, 53, 1).addItemStack(recipe.getResult());
	}
}
