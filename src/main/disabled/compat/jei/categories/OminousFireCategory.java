package twilightforest.compat.jei.categories;

import com.mojang.blaze3d.platform.Lighting;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.jei.JEICompat;
import twilightforest.compat.jei.renderers.EntityRenderer;
import twilightforest.compat.jei.util.OminousFireRecipe;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

public class OminousFireCategory implements IRecipeCategory<OminousFireRecipe> {
	public static final RecipeType<OminousFireRecipe> OMINOUS_FIRE = RecipeType.create(TwilightForestMod.ID, "ominous_fire", OminousFireRecipe.class);
	private final IDrawable icon;
	private final IDrawable arrow;
	private final IDrawable slot;
	private final Component localizedName;
	private final EntityRenderer entityRenderer = new EntityRenderer(32);

	public OminousFireCategory(IGuiHelper helper) {
		this.arrow = helper.getRecipeArrow();
		this.slot = helper.drawableBuilder(TwilightForestMod.getGuiTexture("big_slot.png"), 0, 0, 34, 34).setTextureSize(34, 34).build();
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TFItems.EXANIMATE_ESSENCE.get()));
		this.localizedName = Component.translatable("gui.twilightforest.ominous_fire_jei");
	}

	@Override
	public RecipeType<OminousFireRecipe> getRecipeType() {
		return OMINOUS_FIRE;
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
	public void draw(OminousFireRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		this.arrow.draw(graphics, 46, 19);
		this.slot.draw(graphics, 7, 10);
		this.slot.draw(graphics, 75, 10);

		Minecraft minecraft = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
		graphics.pose().pushPose();
		Lighting.setupForFlatItems();
		graphics.pose().scale(20.0F, -20.0F, 20.0F);
		graphics.pose().translate(2.35F, -2.2F, 19.0F);
		minecraft.getBlockRenderer().renderSingleBlock(TFBlocks.OMINOUS_FIRE.get().defaultBlockState(), graphics.pose(), bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
		graphics.pose().popPose();
		bufferSource.endBatch();
		Lighting.setupFor3DItems();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, OminousFireRecipe recipe, IFocusGroup focuses) {
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
