package twilightforest.init;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.item.recipe.*;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapedRecipe;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapelessRecipe;
import twilightforest.item.recipe.travellers.TravellersVestGlovesMergeRecipe;

import java.util.function.Supplier;

// TODO: Update recipes to use new codec serialization system. Check RecipeSerializers and ShapedRecipe classes for reference implementation.
public class TFRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, TwilightForestMod.ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, TwilightForestMod.ID);

	private static <T extends CustomRecipe> MapCodec<T> simpleCodec(Supplier<T> factory) {
		return RecordCodecBuilder.mapCodec(
			instance -> instance.group(
				CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(r -> r.category())
			).apply(instance, category -> factory.get())
		);
	}

	private static <T extends CustomRecipe> StreamCodec<RegistryFriendlyByteBuf, T> simpleStreamCodec(Supplier<T> factory) {
		return StreamCodec.of((buf, obj) -> {}, buf -> factory.get());
	}

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CasketRepairRecipe>> CASKET_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("casket_repair_recipe", () -> new RecipeSerializer<>(simpleCodec(CasketRepairRecipe::new), simpleStreamCodec(CasketRepairRecipe::new)));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EmperorsClothRecipe>> EMPERORS_CLOTH_RECIPE = RECIPE_SERIALIZERS.register("emperors_cloth_recipe", () -> new RecipeSerializer<>(simpleCodec(EmperorsClothRecipe::new), simpleStreamCodec(EmperorsClothRecipe::new)));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EssenceRepairRecipe>> ESSENCE_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("essence_repair_recipe", () -> new RecipeSerializer<>(simpleCodec(EssenceRepairRecipe::new), simpleStreamCodec(EssenceRepairRecipe::new)));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MagicMapCloningRecipe>> MAGIC_MAP_CLONING_RECIPE = RECIPE_SERIALIZERS.register("magic_map_cloning_recipe", () -> new RecipeSerializer<>(simpleCodec(MagicMapCloningRecipe::new), simpleStreamCodec(MagicMapCloningRecipe::new)));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MazeMapCloningRecipe>> MAZE_MAP_CLONING_RECIPE = RECIPE_SERIALIZERS.register("maze_map_cloning_recipe", () -> new RecipeSerializer<>(simpleCodec(MazeMapCloningRecipe::new), simpleStreamCodec(MazeMapCloningRecipe::new)));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MoonwormQueenRepairRecipe>> MOONWORM_QUEEN_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("moonworm_queen_repair_recipe", () -> new RecipeSerializer<>(simpleCodec(MoonwormQueenRepairRecipe::new), simpleStreamCodec(MoonwormQueenRepairRecipe::new)));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ScepterRepairRecipe>> SCEPTER_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("scepter_repair", () -> ScepterRepairRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<UncraftingRecipe>> UNCRAFTING_SERIALIZER = RECIPE_SERIALIZERS.register("uncrafting", () -> UncraftingRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TravellersGearModifierShapelessRecipe>> MODIFIER_SHAPELESS_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("travellers_gear_modifier_shapeless_recipe", () -> TravellersGearModifierShapelessRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TravellersGearModifierShapedRecipe>> MODIFIER_SHAPED_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("travellers_gear_modifier_shaped_recipe", () -> TravellersGearModifierShapedRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TravellersVestGlovesMergeRecipe>> TRAVELLERS_VEST_GLOVES_MERGE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("travellers_vest_gloves_merge_recipe", () -> new RecipeSerializer<>(simpleCodec(TravellersVestGlovesMergeRecipe::new), simpleStreamCodec(TravellersVestGlovesMergeRecipe::new)));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<NoTemplateSmithingRecipe>> NO_TEMPLATE_SMITHING_SERIALIZER = RECIPE_SERIALIZERS.register("no_template_smithing", () -> NoTemplateSmithingRecipe.SERIALIZER);
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DryingRecipe>> DRYING_SERIALIZER = RECIPE_SERIALIZERS.register("drying", () -> DryingRecipe.SERIALIZER);

	public static final DeferredHolder<RecipeType<?>, RecipeType<UncraftingRecipe>> UNCRAFTING_RECIPE = RECIPE_TYPES.register("uncrafting", () -> RecipeType.simple(TwilightForestMod.prefix("uncrafting")));
	public static final DeferredHolder<RecipeType<?>, RecipeType<DryingRecipe>> DRYING_RECIPE = RECIPE_TYPES.register("drying", () -> RecipeType.simple(TwilightForestMod.prefix("drying")));
}
