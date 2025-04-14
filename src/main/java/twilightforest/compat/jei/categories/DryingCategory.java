package twilightforest.compat.jei.categories;

import com.mojang.blaze3d.platform.Lighting;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.item.recipe.DryingRecipe;

public class DryingCategory implements IRecipeCategory<DryingRecipe> {

	public static final RecipeType<DryingRecipe> DRYING = RecipeType.create(TwilightForestMod.ID, "drying", DryingRecipe.class);
	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable arrow;
	private final Component localizedName;

	public DryingCategory(IGuiHelper helper) {
		ResourceLocation location = TwilightForestMod.getGuiTexture("drying_rack_jei.png");
		this.background = helper.createDrawable(location, 0, 0, 72, 36);
		this.arrow = helper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 128, 24, 17).buildAnimated(20 * 60, IDrawableAnimated.StartDirection.LEFT, false);
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
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void draw(DryingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		this.arrow.draw(graphics, 24, 7);

		Component time = this.calculateTime(recipe.getDryingTime());

		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		graphics.drawString(font, time, 35 - font.width(time.getString()) / 2, 27, 0xFF808080, false);

		MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
		graphics.pose().pushPose();
		Lighting.setupForFlatItems();
		graphics.pose().scale(20.0F, -20.0F, 20.0F);
		graphics.pose().translate(0.6F, -0.75F, 19.0F);
		minecraft.getItemRenderer().renderStatic(new ItemStack(TFBlocks.OAK_DRYING_RACK), ItemDisplayContext.NONE, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, graphics.pose(), bufferSource, null, 0);
		graphics.pose().translate(2.4F, 0.0F, 0.0F);
		minecraft.getItemRenderer().renderStatic(new ItemStack(TFBlocks.OAK_DRYING_RACK), ItemDisplayContext.NONE, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, graphics.pose(), bufferSource, null, 0);
		graphics.pose().popPose();
		bufferSource.endBatch();
		Lighting.setupFor3DItems();
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, DryingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (mouseX > 23 && mouseX < 47 && mouseY > 1 && mouseY < 17) {
			tooltip.add(Component.translatable("gui.twilightforest.drying_ticks", recipe.getDryingTime()).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DryingRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 4, 7).addIngredients(recipe.getIngredients().getFirst());

		builder.addSlot(RecipeIngredientRole.OUTPUT, 52, 7).addItemStack(recipe.getResult());
	}

	private Component calculateTime(int dryingTicks) {
		int minutes = dryingTicks / 20 / 60;

		int seconds = (dryingTicks / 20) % 60;

		if (minutes > 0 && seconds > 0) {
			return Component.translatable("gui.twilightforest.drying_time", minutes, seconds);
		} else if (minutes > 0 && seconds == 0) {
			return Component.translatable(minutes == 1 ? "gui.twilightforest.drying_minute": "gui.twilightforest.drying_minutes", minutes);
		}
		return Component.translatable(seconds == 1 ? "gui.twilightforest.drying_second" : "gui.twilightforest.drying_seconds", seconds);
	}
}
