package twilightforest.item.recipe.travellers;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import twilightforest.data.helpers.TFLangProvider;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiable;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.StreamSupport;

public abstract class TravellersGearModifierRecipe extends CustomRecipe {
	protected final ResourceKey<TravellersModifier> travellersModifierKey;
	public TravellersGearModifierRecipe(ResourceKey<TravellersModifier> travellersModifier) {
		super(CraftingBookCategory.EQUIPMENT);
		this.travellersModifierKey = travellersModifier;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack stack = getModifiableArmor(input);
		if (stack == null)
			return false;
		int slots = 0;
		if (stack.getItem() instanceof TravellersModifiable travellersModifiableItem)
			slots = travellersModifiableItem.getModifierSlots();
		return TravellersModifiersManager.countInsertableModifiers(level.registryAccess(), stack) < slots && !TravellersModifiersManager.hasTravellersModifier(level.registryAccess(), stack, this.travellersModifierKey);
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		ItemStack travellerArmorStack = getModifiableArmor(input);
		if (travellerArmorStack == null)
			return ItemStack.EMPTY;  // Should never happen

		ItemStack stack = travellerArmorStack.copy();
		return applyModifier(registries, stack);
	}

	public ItemStack applyModifier(HolderLookup.Provider registries, ItemStack stack) {
		return TravellersModifiersManager.addModifier(registries, stack, this.travellersModifierKey) ? stack : ItemStack.EMPTY;
	}

	public abstract boolean isShapeless();

	public abstract int getWidth();

	public abstract int getHeight();

	protected static @Nullable ItemStack getModifiableArmor(CraftingInput input) {
		return getModifiableArmor(input.items());
	}

	protected static @Nullable ItemStack getModifiableArmor(Iterable<ItemStack> items) {
		return StreamSupport.stream(items.spliterator(), false)
			.filter(stack -> stack.getItem() instanceof TravellersModifiable).findFirst().orElse(null);
	}

	public static ItemStack getModifiableArmorFromIngredients(Iterable<Ingredient> ingredients) {
		return StreamSupport.stream(ingredients.spliterator(), false)
			.flatMap(ingredient -> Arrays.stream(ingredient.getItems()))
			.filter(stack -> stack.getItem() instanceof TravellersModifiable).findFirst().orElseThrow();
	}

	public ResourceLocation getId() {
		return travellersModifierKey.location()
			.withPrefix(StringUtils.substringAfterLast(getModifiableArmorFromIngredients(getIngredients()).getDescriptionId(), '.') + "/")
			.withPrefix("add_modifier_to_travellers_gear/")
			.withSuffix("_modifier");
	}

	public static class AbstractModifierRecipeSerializer<T extends TravellersGearModifierRecipe> implements RecipeSerializer<T> {
		protected final MapCodec<T> codec;

		protected AbstractModifierRecipeSerializer(MapCodec<T> codec) {
			this.codec = codec;
		}

		@Override
		public MapCodec<T> codec() {
			return codec;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
			return StreamCodec.of(this::toNetwork, this::fromNetwork);
		}

		public T fromNetwork(RegistryFriendlyByteBuf buf) {
			RegistryOps<JsonElement> registryops = buf.registryAccess().createSerializationContext(JsonOps.INSTANCE);
			JsonElement jsonelementDeserialized = GsonHelper.fromJson(TFLangProvider.GSON, buf.readUtf(), JsonElement.class);
			return codec.codec().decode(registryops, jsonelementDeserialized).getOrThrow().getFirst();
		}

		public void toNetwork(RegistryFriendlyByteBuf buf, T recipe) {
			RegistryOps<JsonElement> registryops = buf.registryAccess().createSerializationContext(JsonOps.INSTANCE);
			JsonElement jsonelement = codec.codec().encodeStart(registryops, recipe).getOrThrow();
			buf.writeUtf(jsonelement.toString());
		}
	}
}
