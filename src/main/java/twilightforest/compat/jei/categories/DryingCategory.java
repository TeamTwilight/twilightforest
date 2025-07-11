package twilightforest.compat.jei.categories;

import com.mojang.blaze3d.platform.Lighting;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.init.TFBlocks;
import twilightforest.item.recipe.DryingRecipe;

public class DryingCategory implements IRecipeCategory<DryingRecipe> {

	public static final RecipeType<DryingRecipe> DRYING = RecipeType.create(TwilightForestMod.ID, "drying", DryingRecipe.class);
	private final IDrawable icon;
	private final IDrawable arrow;
	private final Component localizedName;

	public DryingCategory(IGuiHelper helper) {
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
	public int getWidth() {
		return 70;
	}

	@Override
	public int getHeight() {
		return 30;
	}

	@Override
	public void draw(DryingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		this.arrow.draw(graphics, 23, 1);

		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		Component time = RecipeViewerConstants.getDryingTime(recipe.getDryingTime());
		graphics.drawString(font, time, 35 - font.width(time.getString()) / 2, 20, 0xFF808080, false);

		MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
		graphics.pose().pushPose();
		Lighting.setupForFlatItems();
		graphics.pose().scale(20.0F, -20.0F, 20.0F);
		graphics.pose().translate(0.45F, -0.45F, 19.0F);
		minecraft.getItemRenderer().renderStatic(new ItemStack(TFBlocks.OAK_DRYING_RACK), ItemDisplayContext.NONE, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, graphics.pose(), bufferSource, null, 0);
		graphics.pose().translate(2.6F, 0.0F, 0.0F);
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
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().getFirst());

		builder.addSlot(RecipeIngredientRole.OUTPUT, 53, 1).addItemStack(recipe.getResult());
	}
}
