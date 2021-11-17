package twilightforest.compat;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

import java.util.Arrays;
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
        for (List<ItemStack> stack : iIngredients.getOutputs(VanillaTypes.ITEM)) { //Draw the 3 by 3 cube of items on the correct position
            iRecipeLayout.getItemStacks().init(i + 1, true, (i % 3) * 18 + 62, (i / 3) * 18);
            iRecipeLayout.getItemStacks().set(++i, stack);
        }
        iRecipeLayout.getItemStacks().init(++i, false, 4, 18); //Draw the item you're uncrafting in the right spot as well
        iRecipeLayout.getItemStacks().set(i, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
    }
}
