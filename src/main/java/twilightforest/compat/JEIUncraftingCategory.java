package twilightforest.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.data.tags.ItemTagGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JEIUncraftingCategory implements IRecipeCategory<CraftingRecipe> {
    public static final RecipeType<CraftingRecipe> UNCRAFTING = RecipeType.create(TwilightForestMod.ID, "uncrafting", CraftingRecipe.class);
    public static final int width = 116;
    public static final int height = 54;
    private final IDrawable background;
    private final IDrawable icon;
    private final TranslatableComponent localizedName;

    public JEIUncraftingCategory(IGuiHelper guiHelper) {
        ResourceLocation location = TwilightForestMod.getGuiTexture("uncrafting_jei.png");
        this.background = guiHelper.createDrawable(location, 0, 0, width, height);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TFBlocks.UNCRAFTING_TABLE.get()));
        this.localizedName = new TranslatableComponent("gui.uncrafting_jei");
    }

    @Override
    public RecipeType<CraftingRecipe> getRecipeType() {
        return UNCRAFTING;
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return UNCRAFTING.getUid();
    }


    @SuppressWarnings("removal")
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
    public void setRecipe(IRecipeLayoutBuilder builder, CraftingRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> outputs = new ArrayList<>(recipe.getIngredients()); //Collect each ingredient
        for (int i = 0; i < outputs.size(); i++) {
            outputs.set(i, Ingredient.of(Arrays.stream(outputs.get(i).getItems())
                    .filter(o -> !(o.is(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS)))
                    .filter(o -> !(o.getItem().hasContainerItem(o)))));//Remove any banned items
        }

        for (int j = 0, k = 0; j - k < outputs.size() && j < 9; j++) {
            int x = j % 3, y = j / 3;
            if ((recipe.canCraftInDimensions(x, 3) | recipe.canCraftInDimensions(3, y)) && !(recipe instanceof ShapelessRecipe)) {
                k++;
                continue;
            } //Skips empty spaces in shaped recipes
            builder.addSlot(RecipeIngredientRole.OUTPUT, x * 18 + 63, y * 18 + 1).addIngredients(outputs.get(j - k)); //Set input as output and place in the grid
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 5, 19).addItemStack(recipe.getResultItem());//Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
    }
}