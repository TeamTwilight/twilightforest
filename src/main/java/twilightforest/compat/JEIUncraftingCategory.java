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
import net.minecraft.world.item.crafting.ShapelessRecipe;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.data.ItemTagGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        List<List<ItemStack>> outputList = new ArrayList<>();
        craftingRecipe.getIngredients().forEach(i -> outputList.add(Arrays.asList(i.getItems()))); //Collect each ingredient
        for (int i = 0; i < outputList.size(); i++) {
            List<ItemStack> newList = outputList.get(i);
            outputList.set(i, newList.stream()
                    .filter(o -> !(o.is(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS)))
                    .filter(o -> !(o.getItem().hasContainerItem(o)))
                    .collect(Collectors.toList()));//Remove any banned items
        }

        if (!(outputList.stream().allMatch(o -> o.stream().allMatch(ItemStack::isEmpty)))) { //Checks if all items were banned
            iIngredients.setInputLists(VanillaTypes.ITEM, ImmutableList.of(inputBuilder.build()));
            iIngredients.setOutputLists(VanillaTypes.ITEM, outputList);//Set the inputs as outputs
        }
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
