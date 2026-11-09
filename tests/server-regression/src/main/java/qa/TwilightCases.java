package qa;

import java.lang.reflect.*;
import java.util.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.CraftingRecipe.CraftingBookInfo;
import net.minecraft.world.item.crafting.Recipe.CommonInfo;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import twilightforest.inventory.UncraftingMenu;

public final class TwilightCases {
  static ShapelessRecipe recipe(Ingredient i) {
    return new ShapelessRecipe(
        new CommonInfo(false),
        new CraftingBookInfo(CraftingBookCategory.MISC, ""),
        new ItemStackTemplate(Items.BREAD),
        List.of(i));
  }

  public static void run(ServerLevel level) throws Exception {
    Object menu = Checks.blank(UncraftingMenu.class);
    Field f = UncraftingMenu.class.getDeclaredField("level");
    f.setAccessible(true);
    f.set(menu, level);
    Method m = UncraftingMenu.class.getDeclaredMethod("getIngredients", CraftingRecipe.class);
    m.setAccessible(true);
    Checks.test(
        "uncrafting_vanilla_ingredient",
        () -> {
          var a = (ItemStack[]) m.invoke(menu, recipe(Ingredient.of(Items.WHEAT)));
          Checks.require(a.length == 1 && a[0].is(Items.WHEAT), "Vanilla ingredient changed");
        });
    Checks.test(
        "uncrafting_custom_components_preserved",
        () -> {
          var stack = new ItemStack(Items.DIAMOND);
          stack.set(DataComponents.CUSTOM_NAME, Component.literal("SyntheticOnly"));
          Ingredient i = DataComponentIngredient.of(false, stack);
          var a = (ItemStack[]) m.invoke(menu, recipe(i));
          Checks.require(
              a.length == 1 && i.test(a[0]) && a[0].has(DataComponents.CUSTOM_NAME),
              "Custom component example lost");
        });
    Checks.test(
        "uncrafting_custom_cycle_safe",
        () -> {
          var i =
              DataComponentIngredient.of(
                  DataComponents.CUSTOM_NAME, Component.literal("SyntheticOnly"), Items.DIAMOND);
          UncraftingMenu.class.getField("ingredientsInCycle").set(menu, -1);
          var a = (ItemStack[]) m.invoke(menu, recipe(i));
          Checks.require(a.length == 1 && i.test(a[0]), "Negative cycling failed");
        });
  }
}
