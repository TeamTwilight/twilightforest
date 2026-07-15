package twilightforest.item.recipe.travellers;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;

import java.util.List;
import java.util.Optional;

public class TravellersVestGlovesMergeRecipe extends CustomRecipe {
	public static final MapCodec<TravellersVestGlovesMergeRecipe> MAP_CODEC =
		MapCodec.unit(TravellersVestGlovesMergeRecipe::new);

	public static final StreamCodec<RegistryFriendlyByteBuf, TravellersVestGlovesMergeRecipe> STREAM_CODEC =
		StreamCodec.unit(new TravellersVestGlovesMergeRecipe());

	public static final RecipeSerializer<TravellersVestGlovesMergeRecipe> SERIALIZER =
		new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	public TravellersVestGlovesMergeRecipe() {
		super();
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		Optional<InputPair> pair = this.resolve(input);
		if (pair.isEmpty())
			return false;
		ItemStack vest = pair.get().vest();
		return !vest.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);
	}

	@Override
	public ItemStack assemble(CraftingInput craftingInput) {
		Optional<InputPair> pair = this.resolve(craftingInput);
		if (pair.isEmpty())
			return ItemStack.EMPTY;

		ItemStack vest = pair.get().vest().copy();
		vest.set(TFDataComponents.TRAVELLERS_HAS_GLOVES, Unit.INSTANCE);
		return vest;
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}

	private Optional<InputPair> resolve(CraftingInput input) {
		List<ItemStack> items = input.items().stream().filter(stack -> !stack.isEmpty()).toList();
		if (items.size() != 2) return Optional.empty();

		Optional<ItemStack> vest = items.stream().filter(s -> s.is(TFItems.TRAVELLERS_VEST.get())).findFirst();
		Optional<ItemStack> gloves = items.stream().filter(s -> s.is(TFItems.TRAVELLERS_GLOVES.get())).findFirst();

		return vest.flatMap(w -> gloves.map(b -> new InputPair(w, b)));
	}

	private record InputPair(ItemStack vest, ItemStack gloves) {}
}
