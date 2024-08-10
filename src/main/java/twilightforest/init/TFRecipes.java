package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.item.recipe.*;

public class TFRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, TwilightForestMod.ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EmperorsClothRecipe>> EMPERORS_CLOTH_RECIPE = RECIPE_SERIALIZERS.register("emperors_cloth_recipe", () -> new SimpleCraftingRecipeSerializer<>(EmperorsClothRecipe::new));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MagicMapCloningRecipe>> MAGIC_MAP_CLONING_RECIPE = RECIPE_SERIALIZERS.register("magic_map_cloning_recipe", () -> new SimpleCraftingRecipeSerializer<>(MagicMapCloningRecipe::new));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MazeMapCloningRecipe>> MAZE_MAP_CLONING_RECIPE = RECIPE_SERIALIZERS.register("maze_map_cloning_recipe", () -> new SimpleCraftingRecipeSerializer<>(MazeMapCloningRecipe::new));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MoonwormQueenRepairRecipe>> MOONWORM_QUEEN_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("moonworm_queen_repair_recipe", () -> new SimpleCraftingRecipeSerializer<>(MoonwormQueenRepairRecipe::new));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ScepterRepairRecipe>> SCEPTER_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("scepter_repair", ScepterRepairRecipe.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<UncraftingRecipe>> UNCRAFTING_SERIALIZER = RECIPE_SERIALIZERS.register("uncrafting", UncraftingRecipe.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<NoTemplateSmithingRecipe>> NO_TEMPLATE_SMITHING_SERIALIZER = RECIPE_SERIALIZERS.register("no_template_smithing", NoTemplateSmithingRecipe.Serializer::new);

	public static final DeferredHolder<RecipeType<?>, RecipeType<UncraftingRecipe>> UNCRAFTING_RECIPE = RECIPE_TYPES.register("uncrafting", () -> RecipeType.simple(TwilightForestMod.prefix("uncrafting")));
}
