package twilightforest.data.custom;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import twilightforest.item.recipe.ScepterRepairRecipe;

import java.util.ArrayList;
import java.util.List;

public class ScepterRecipeBuilder {
	private final Item scepter;
	private final List<Ingredient> repairItems = new ArrayList<>();

	private ScepterRecipeBuilder(Item scepter) {
		this.scepter = scepter;
	}

	public static ScepterRecipeBuilder repairFor(Item scepter) {
		return new ScepterRecipeBuilder(scepter);
	}

	public <T> ScepterRecipeBuilder addRepairIngredient(Ingredient item) {
		this.repairItems.add(item);
		return this;
	}

	public <T> ScepterRecipeBuilder addRepairIngredient(ItemStack item) {
		this.repairItems.add(Ingredient.of(item));
		return this;
	}

	public <T> ScepterRecipeBuilder addRepairIngredient(TagKey<Item> item) {
		this.repairItems.add(Ingredient.of(item));
		return this;
	}

	public <T> ScepterRecipeBuilder addRepairIngredient(ItemLike item) {
		this.repairItems.add(Ingredient.of(item));
		return this;
	}

	public void save(RecipeOutput output, ResourceLocation id) {
		ScepterRepairRecipe recipe = new ScepterRepairRecipe(this.scepter, this.repairItems, CraftingBookCategory.MISC);
		output.accept(id, recipe, null);
	}
}
