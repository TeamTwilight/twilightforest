package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.item.recipe.*;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapedRecipe;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapelessRecipe;
import twilightforest.item.recipe.travellers.TravellersVestGlovesMergeRecipe;

public class TFRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, TwilightForestMod.ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CasketRepairRecipe>> CASKET_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("casket_repair_recipe", () -> CasketRepairRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EmperorsClothRecipe>> EMPERORS_CLOTH_RECIPE = RECIPE_SERIALIZERS.register("emperors_cloth_recipe", () -> EmperorsClothRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SimpleRepairRecipe>> SIMPLE_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("simple_repair", () -> SimpleRepairRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ComplexRepairRecipe>> COMPLEX_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("complex_repair", () -> ComplexRepairRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<UncraftingRecipe>> UNCRAFTING_SERIALIZER = RECIPE_SERIALIZERS.register("uncrafting", () -> UncraftingRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TravellersGearModifierShapelessRecipe>> MODIFIER_SHAPELESS_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("travellers_gear_modifier_shapeless_recipe", () -> TravellersGearModifierShapelessRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TravellersGearModifierShapedRecipe>> MODIFIER_SHAPED_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("travellers_gear_modifier_shaped_recipe", () -> TravellersGearModifierShapedRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TravellersVestGlovesMergeRecipe>> TRAVELLERS_VEST_GLOVES_MERGE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("travellers_vest_gloves_merge_recipe", () -> TravellersVestGlovesMergeRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ComponentAttachingSmithingRecipe>> NO_TEMPLATE_SMITHING_SERIALIZER = RECIPE_SERIALIZERS.register("no_template_smithing", () -> ComponentAttachingSmithingRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DryingRecipe>> DRYING_SERIALIZER = RECIPE_SERIALIZERS.register("drying", () -> DryingRecipe.SERIALIZER);

	public static final DeferredHolder<RecipeType<?>, RecipeType<UncraftingRecipe>> UNCRAFTING_RECIPE = RECIPE_TYPES.register("uncrafting", () -> RecipeType.simple(TwilightForestMod.prefix("uncrafting")));
	public static final DeferredHolder<RecipeType<?>, RecipeType<DryingRecipe>> DRYING_RECIPE = RECIPE_TYPES.register("drying", () -> RecipeType.simple(TwilightForestMod.prefix("drying")));
}
