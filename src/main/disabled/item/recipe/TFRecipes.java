package twilightforest.item.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.item.TFItems;

public class TFRecipes {

	//FIXME: Put a Serializer on the Cloning Recipes. Currently used ones aren't liking it
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, TwilightForestMod.ID);

	public static final RegistryObject<SpecialRecipeSerializer<TFArmorDyeingRecipe>> ARCTIC_ARMOR_DYEING = RECIPE_SERIALIZERS.register("armor_dyeing_tf",
			() -> new SpecialRecipeSerializer<>(TFArmorDyeingRecipe::new));
	public static final RegistryObject<SpecialRecipeSerializer<TFMapCloningRecipe>> MAGIC_MAP_CLONE = RECIPE_SERIALIZERS.register("magic_map_cloning",
			() -> new SpecialRecipeSerializer<>(new TFMapCloningRecipe(TFItems.magic_map.get(), TFItems.magic_map_empty.get())));
	public static final RegistryObject<SpecialRecipeSerializer<TFMapCloningRecipe>> MAZE_MAP_CLONE = RECIPE_SERIALIZERS.register("maze_map_cloning",
			() -> new SpecialRecipeSerializer<>(new TFMapCloningRecipe(TFItems.maze_map.get(), TFItems.maze_map_empty.get())));
	public static final RegistryObject<SpecialRecipeSerializer<TFMapCloningRecipe>> ORE_MAP_CLONE = RECIPE_SERIALIZERS.register("ore_map_cloning",
			() -> new SpecialRecipeSerializer<>(new TFMapCloningRecipe(TFItems.ore_map.get(), TFItems.ore_map_empty.get())));

	// recipe sorter
	// RecipeSorter.register(TwilightForestMod.ID + ":mapcloning", TFMapCloningRecipe.class, SHAPELESS, "after:minecraft:shapeless");
}
