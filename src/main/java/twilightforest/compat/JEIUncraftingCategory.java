package twilightforest.compat;

import blusunrize.immersiveengineering.client.utils.GuiHelper;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.data.ItemTagGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIUncraftingCategory implements IRecipeCategory<CraftingRecipe> {
    public static ResourceLocation UNCRAFTING = TwilightForestMod.prefix("uncrafting_jei");
    public static final int width = 116;
    public static final int height = 54;
    private final IDrawable background;
    private final IDrawable icon;
    private final TranslatableComponent localizedName;

    public JEIUncraftingCategory(IGuiHelper guiHelper) {
        ResourceLocation location = TwilightForestMod.getGuiTexture("uncrafting_jei.png");
        this.background = guiHelper.createDrawable(location, 0, 0, width, height);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()));
        this.localizedName = new TranslatableComponent("gui.uncrafting_jei");
    }


    @Override
    public ResourceLocation getUid() {
        return UNCRAFTING;
    }

    @Override
    public Class<? extends CraftingRecipe> getRecipeClass() {
        return CraftingRecipe.class;
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
    public void setIngredients(CraftingRecipe craftingRecipe, IIngredients iIngredients) {
        ImmutableList.Builder<ItemStack> inputBuilder = ImmutableList.builder();
        inputBuilder.add(craftingRecipe.getResultItem()); //Setting the result item as the input, since the recipe will appear in reverse
        iIngredients.setInputLists(VanillaTypes.ITEM, ImmutableList.of(inputBuilder.build()));

        ImmutableList.Builder<List<ItemStack>> outputBuilder = ImmutableList.builder();
        for (Ingredient ingredient : craftingRecipe.getIngredients()) outputBuilder.add(Arrays.asList(ingredient.getItems())); //and vice versa
        iIngredients.setOutputLists(VanillaTypes.ITEM, outputBuilder.build());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, CraftingRecipe craftingRecipe, IIngredients iIngredients) {
        int i = 0;
        List<List<ItemStack>> outputs = iIngredients.getOutputs(VanillaTypes.ITEM);
        for (int j = 0, k = 0; j - k < outputs.size() && j < 9; j++) {
            int x = j % 3, y = j / 3;
            if ((craftingRecipe.canCraftInDimensions(x, 3) | craftingRecipe.canCraftInDimensions(3, y)) && !(craftingRecipe instanceof ShapelessRecipe)) {
                k++;
                continue;
            } //Skips empty spaces in shaped recipes
            iRecipeLayout.getItemStacks().init(++i, true, x * 18 + 62, y * 18); //Placement math
            iRecipeLayout.getItemStacks().set(i, outputs.get(j - k));
        }
        iRecipeLayout.getItemStacks().init(++i, false, 4, 18); //Draw the item you're uncrafting in the right spot as well
        iRecipeLayout.getItemStacks().set(i, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
    }
}
