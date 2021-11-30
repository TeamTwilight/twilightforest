package twilightforest.item.recipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("NullableProblems")
public class UncraftingRecipe implements IUncraftingRecipe, IShapedRecipe<CraftingContainer> {
    private final ResourceLocation recipeID;
    private final int cost;
    private final int width;
    private final int height;
    private final Ingredient ingredient;
    private final int count;
    private final NonNullList<Ingredient> resultItems;

    public UncraftingRecipe(ResourceLocation recipeID, int cost, int width, int height, Ingredient ingredient, int count, NonNullList<Ingredient> resultItems) {
        this.recipeID = recipeID;
        this.cost = cost;
        this.width = width;
        this.height = height;
        this.ingredient = ingredient;
        this.count = count;
        this.resultItems = resultItems;
    }

    @Override //This method is never used, but it has to be implemented
    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        return false;
    }

    @Override //We have to implement this method, can't really be used since we have multiple outputs
    public ItemStack assemble(CraftingContainer pContainer) {
        return ItemStack.EMPTY;
    }

    @Override //We have to implement this method, returns the count just in case
    public ItemStack getResultItem() {
        return new ItemStack(Items.AIR, this.count);
    }

    @Override //Could probably be set to return true, since the recipe serializer doesn't let a bigger number through.
    public boolean canCraftInDimensions(int width, int height) {
        return (width >= this.width && height >= this.height);
    }

    //Checks if the itemStack is a part of the ingredient when UncraftingContainer's getRecipesFor() method iterates through all recipes.
    public boolean isItemStackAnIngredient(ItemStack itemStack) {
        return Arrays.stream(this.ingredient.getItems()).anyMatch(i -> (itemStack.getItem() == i.getItem() && itemStack.getCount() >= i.getCount()));
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeID;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TFRecipes.UNCRAFTING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TFRecipes.UNCRAFTING_RECIPE;
    }

    public int getCost() {
        return this.cost;
    }

    @Override
    public int getRecipeWidth() {
        return this.width;
    }

    @Override
    public int getRecipeHeight() {
        return this.height;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.resultItems;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    //Since our recipe can need multiple input items for a recipe and ingredients can't directly store count, we store it separately
    public int getCount() {
        return this.count;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<UncraftingRecipe> {
        /**
         * This is mostly vanilla's shaped recipe serializer, with some changes made to make it work with the slightly different recipe type.
         * The recipe json has inputs for "cost", which determines how many levels the recipe will cost.
         * "ingredient", which is made to be an ingredient instead of an itemStack, so the recipe can have multiple input options, such as any member of an item tag.
         * "count" is how many of the same item are required by the recipe, since we're dealing with ingredients and not itemStacks, we get this separately.
         * "key" and "pattern", which work just like vanilla except this is output and not input, since we're uncrafting.
         * Width and height get assigned automatically.
         */
        @Override
        public UncraftingRecipe fromJson(ResourceLocation id, JsonObject json) {
            int cost = GsonHelper.getAsInt(json, "cost");

            JsonElement jsonelement = (GsonHelper.isArrayNode(json, "ingredient") ? GsonHelper.getAsJsonArray(json, "ingredient") : GsonHelper.getAsJsonObject(json, "ingredient"));
            Ingredient ingredient = Ingredient.fromJson(jsonelement);

            int count = GsonHelper.getAsInt(json, "count");

            Map<String, Ingredient> key = keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] pattern = shrink(patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));

            int width = pattern[0].length();
            int height = pattern.length;

            NonNullList<Ingredient> ingredients = dissolvePattern(pattern, key, width, height);

            return new UncraftingRecipe(id, cost, width, height, ingredient, count, ingredients);
        }

        private static Map<String, Ingredient> keyFromJson(JsonObject json) {
            Map<String, Ingredient> map = Maps.newHashMap();
            for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
                if (entry.getKey().length() != 1)
                    throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
                if (" ".equals(entry.getKey()))
                    throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
                map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
            }
            map.put(" ", Ingredient.EMPTY);
            return map;
        }

        static String[] shrink(String... prePattern) {
            int i = Integer.MAX_VALUE;
            int j = 0;
            int k = 0;
            int l = 0;

            for(int i1 = 0; i1 < prePattern.length; ++i1) {
                String s = prePattern[i1];
                i = Math.min(i, firstNonSpace(s));
                int j1 = lastNonSpace(s);
                j = Math.max(j, j1);
                if (j1 < 0) {
                    if (k == i1) ++k;
                    ++l;
                } else l = 0;
            }

            if (prePattern.length == l) return new String[0];
            else {
                String[] shrunk = new String[prePattern.length - l - k];
                for(int k1 = 0; k1 < shrunk.length; ++k1) shrunk[k1] = prePattern[k1 + k].substring(i, j + 1);
                return shrunk;
            }
        }

        private static int firstNonSpace(String first) {
            int i;
            i = 0;
            while (i < first.length() && first.charAt(i) == ' ') ++i;
            return i;
        }

        private static int lastNonSpace(String last) {
            int i;
            i = last.length() - 1;
            while (i >= 0 && last.charAt(i) == ' ') --i;
            return i;
        }

        private static String[] patternFromJson(JsonArray pattern) {
            String[] stringPattern = new String[pattern.size()];
            if (stringPattern.length > 3) {
                throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
            } else if (stringPattern.length == 0) {
                throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            } else {
                for(int i = 0; i < stringPattern.length; ++i) {
                    String s = GsonHelper.convertToString(pattern.get(i), "pattern[" + i + "]");
                    if (s.length() > 3)
                        throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                    if (i > 0 && stringPattern[0].length() != s.length())
                        throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                    stringPattern[i] = s;
                }
                return stringPattern;
            }
        }

        private static NonNullList<Ingredient> dissolvePattern(String[] pattern, Map<String, Ingredient> key, int width, int height) {
            NonNullList<Ingredient> results = NonNullList.withSize(width * height, Ingredient.EMPTY);
            Set<String> set = Sets.newHashSet(key.keySet());
            set.remove(" ");

            for(int i = 0; i < pattern.length; ++i) {
                for(int j = 0; j < pattern[i].length(); ++j) {
                    String s = pattern[i].substring(j, j + 1);
                    Ingredient ingredient = key.get(s);
                    if (ingredient == null)
                        throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                    set.remove(s);
                    results.set(j + width * i, ingredient);
                }
            }

            if (!set.isEmpty()) throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
            else return results;
        }

        @Nullable
        @Override
        public UncraftingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            int cost = buffer.readVarInt();
            int width = buffer.readVarInt();
            int height = buffer.readVarInt();
            Ingredient result = Ingredient.fromNetwork(buffer);
            int count = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
            for(int k = 0; k < ingredients.size(); ++k) ingredients.set(k, Ingredient.fromNetwork(buffer));
            return new UncraftingRecipe(id, cost, width, height, result, count, ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, UncraftingRecipe recipe) {
            buffer.writeVarInt(recipe.cost);
            buffer.writeVarInt(recipe.width);
            buffer.writeVarInt(recipe.height);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeVarInt(recipe.count);
            for (Ingredient i : recipe.resultItems) i.toNetwork(buffer);
        }
    }
}