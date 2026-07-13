package twilightforest.item.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;

public class CasketRepairRecipe extends CustomRecipe {
	public static final MapCodec<CasketRepairRecipe> MAP_CODEC =
		MapCodec.unit(CasketRepairRecipe::new);

	public static final StreamCodec<RegistryFriendlyByteBuf, CasketRepairRecipe> STREAM_CODEC =
		StreamCodec.unit(new CasketRepairRecipe());

	public static final RecipeSerializer<CasketRepairRecipe> SERIALIZER =
		new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	public CasketRepairRecipe() {
		super();
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		boolean casket = false;
		boolean charm = false;

		for (int i = 0; i < input.size(); ++i) {
			ItemStack stackInQuestion = input.getItem(i);
			if (!stackInQuestion.isEmpty()) {
				if (stackInQuestion.is(TFItems.KEEPSAKE_CASKET) && stackInQuestion.getOrDefault(TFDataComponents.CASKET_DAMAGE, 0) > 0) {
					if (casket) return false;
					casket = true;
				} else if (stackInQuestion.is(TFItems.CHARM_OF_KEEPING_3.get())) {
					if (charm) return false;
					charm = true;
				} else {
					return false;
				}
			}
		}
		return casket && charm;
	}

	@Override
	public ItemStack assemble(CraftingInput craftingInput) {
		ItemStack casket = null;
		for (int i = 0; i < craftingInput.size(); ++i) {
			ItemStack itemstack = craftingInput.getItem(i);
			if (!itemstack.isEmpty()) {
				if (itemstack.is(TFItems.KEEPSAKE_CASKET)) {
					if (casket == null) {
						casket = itemstack;
					} else {
						//Only accept 1 casket
						return ItemStack.EMPTY;
					}
				}
			}
		}

		if (casket != null && casket.getOrDefault(TFDataComponents.CASKET_DAMAGE, 0) > 0) {
			ItemStack repaired = casket.copy();
			repaired.update(TFDataComponents.CASKET_DAMAGE, casket.get(TFDataComponents.CASKET_DAMAGE), integer -> integer - 1);
			return repaired;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}
}
