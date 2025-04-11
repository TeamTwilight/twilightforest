package twilightforest.item.recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import snownee.jade.util.JsonConfig;
import twilightforest.TwilightForestMod;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.modifiers.TravellersGearComponentModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiers;

import java.util.Optional;
import java.util.stream.Stream;

public abstract class TravellersGearModifierRecipe extends CustomRecipe {
	protected final TravellersGearComponentModifier travellersModifier;
	public TravellersGearModifierRecipe(TravellersGearComponentModifier travellersModifier) {
		super(CraftingBookCategory.EQUIPMENT);
		this.travellersModifier = travellersModifier;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		for (ItemStack stack : getTravellersArmor(input).toList()) {
			if (TravellersModifiers.countModifiers(stack) >= 3 || TravellersModifiers.findAllModifiers(stack).toList().contains(travellersModifier))
				return false;
		}
		return true;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		Optional<ItemStack> travellerArmorStack = getTravellersArmor(input).findFirst();
		if (travellerArmorStack.isEmpty()) {
			TwilightForestMod.LOGGER.error("No traveller's gear item found for {}. Please report this to https://github.com/TeamTwilight/twilightforest/issues", input);
			return ItemStack.EMPTY;
		}
		ItemStack stack = travellerArmorStack.get().copy();
		travellersModifier.addModifier(stack);
		return stack;
	}

	protected static Stream<ItemStack> getTravellersArmor(CraftingInput input) {
		return input.items().stream().filter(stack -> stack.getItem() instanceof TravellersArmorItem);
	}

	public ResourceLocation getId() {
		return travellersModifier.getDatagenOnlyComponentId().withPrefix("add_modifier_to_travellers_gear/").withSuffix("_modifier");
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
			JsonElement jsonelementDeserialized = GsonHelper.fromJson(JsonConfig.GSON, buf.readUtf(), JsonElement.class);
			return codec.codec().decode(registryops, jsonelementDeserialized).getOrThrow().getFirst();
		}

		public void toNetwork(RegistryFriendlyByteBuf buf, T recipe) {
			RegistryOps<JsonElement> registryops = buf.registryAccess().createSerializationContext(JsonOps.INSTANCE);
			JsonElement jsonelement = codec.codec().encodeStart(registryops, recipe).getOrThrow();
			buf.writeUtf(jsonelement.toString());
		}
	}
}
