package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
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

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CasketRepairRecipe>> CASKET_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("casket_repair_recipe", () -> new RecipeSerializer<>(MapCodec.unit(CasketRepairRecipe::new), StreamCodec.unit(new CasketRepairRecipe())));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EmperorsClothRecipe>> EMPERORS_CLOTH_RECIPE = RECIPE_SERIALIZERS.register("emperors_cloth_recipe", () -> new RecipeSerializer<>(MapCodec.unit(EmperorsClothRecipe::new), StreamCodec.unit(new EmperorsClothRecipe())));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EssenceRepairRecipe>> ESSENCE_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("essence_repair_recipe", () -> new RecipeSerializer<>(MapCodec.unit(EssenceRepairRecipe::new), StreamCodec.unit(new EssenceRepairRecipe())));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MagicMapCloningRecipe>> MAGIC_MAP_CLONING_RECIPE = RECIPE_SERIALIZERS.register("magic_map_cloning_recipe", () -> new RecipeSerializer<>(MapCodec.unit(MagicMapCloningRecipe::new), StreamCodec.unit(new MagicMapCloningRecipe())));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MazeMapCloningRecipe>> MAZE_MAP_CLONING_RECIPE = RECIPE_SERIALIZERS.register("maze_map_cloning_recipe", () -> new RecipeSerializer<>(MapCodec.unit(MazeMapCloningRecipe::new), StreamCodec.unit(new MazeMapCloningRecipe())));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MoonwormQueenRepairRecipe>> MOONWORM_QUEEN_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("moonworm_queen_repair_recipe", () -> new RecipeSerializer<>(MapCodec.unit(MoonwormQueenRepairRecipe::new), StreamCodec.unit(new MoonwormQueenRepairRecipe())));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ScepterRepairRecipe>> SCEPTER_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("scepter_repair", () -> new RecipeSerializer<>(ScepterRepairRecipe.CODEC, ScepterRepairRecipe.STREAM_CODEC));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<? extends ShapedRecipe>> UNCRAFTING_SERIALIZER = RECIPE_SERIALIZERS.register("uncrafting", () -> new RecipeSerializer<>(UncraftingRecipe.CODEC, UncraftingRecipe.streamCodec()));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TravellersGearModifierShapelessRecipe>> MODIFIER_SHAPELESS_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("travellers_gear_modifier_shapeless_recipe",  () -> new RecipeSerializer<>(TravellersGearModifierShapelessRecipe.CODEC, TravellersGearModifierShapelessRecipe.streamCodec()));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TravellersGearModifierShapedRecipe>> MODIFIER_SHAPED_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("travellers_gear_modifier_shaped_recipe", () -> new RecipeSerializer<>(TravellersGearModifierShapedRecipe.CODEC, TravellersGearModifierShapedRecipe.streamCodec()));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TravellersVestGlovesMergeRecipe>> TRAVELLERS_VEST_GLOVES_MERGE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("travellers_vest_gloves_merge_recipe", () -> new RecipeSerializer<>(MapCodec.unit(TravellersVestGlovesMergeRecipe::new), StreamCodec.unit(new TravellersVestGlovesMergeRecipe())));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<NoTemplateSmithingRecipe>> NO_TEMPLATE_SMITHING_SERIALIZER = RECIPE_SERIALIZERS.register("no_template_smithing", () -> new RecipeSerializer<>(NoTemplateSmithingRecipe.CODEC, NoTemplateSmithingRecipe.STREAM_CODEC));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DryingRecipe>> DRYING_SERIALIZER = RECIPE_SERIALIZERS.register("drying", () -> new RecipeSerializer<>(DryingRecipe.CODEC, DryingRecipe.STREAM_CODEC));

	public static final DeferredHolder<RecipeType<?>, RecipeType<CraftingRecipe>> UNCRAFTING_RECIPE = RECIPE_TYPES.register("uncrafting", () -> RecipeType.simple(TwilightForestMod.prefix("uncrafting")));
	public static final DeferredHolder<RecipeType<?>, RecipeType<DryingRecipe>> DRYING_RECIPE = RECIPE_TYPES.register("drying", () -> RecipeType.simple(TwilightForestMod.prefix("drying")));
}
