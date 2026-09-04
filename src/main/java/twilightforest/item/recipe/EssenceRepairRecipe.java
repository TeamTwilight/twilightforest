package twilightforest.item.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFItems;

public class EssenceRepairRecipe extends CustomRecipe {
	public static final EssenceRepairRecipe INSTANCE = new EssenceRepairRecipe();

	public static final MapCodec<EssenceRepairRecipe> MAP_CODEC =
		MapCodec.unit(INSTANCE);

	public static final StreamCodec<RegistryFriendlyByteBuf, EssenceRepairRecipe> STREAM_CODEC =
		StreamCodec.unit(INSTANCE);

	public static final RecipeSerializer<EssenceRepairRecipe> SERIALIZER =
		new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private EssenceRepairRecipe() {
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		boolean scepter = false;
		boolean essence = false;

		for (int i = 0; i < input.size(); ++i) {
			ItemStack stackInQuestion = input.getItem(i);
			if (!stackInQuestion.isEmpty()) {
				if (stackInQuestion.is(TFItemTags.SCEPTERS) && stackInQuestion.isDamaged()) {
					if (scepter) return false;
					scepter = true;
				} else if (stackInQuestion.is(TFItems.EXANIMATE_ESSENCE.get())) {
					if (essence) return false;
					essence = true;
				} else {
					return false;
				}
			}
		}
		return scepter && essence;
	}

	@Override
	public ItemStack assemble(CraftingInput craftingInput) {
		ItemStack scepter = null;
		for (int i = 0; i < craftingInput.size(); ++i) {
			ItemStack itemstack = craftingInput.getItem(i);
			if (!itemstack.isEmpty()) {
				if (itemstack.is(TFItemTags.SCEPTERS)) {
					if (scepter == null) {
						scepter = itemstack;
					} else {
						//Only accept 1 scepter
						return ItemStack.EMPTY;
					}
				}
			}
		}

		if (scepter != null && scepter.isDamaged()) {
			ItemStack repaired = scepter.copy();
			repaired.setDamageValue(0);
			return repaired;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}
}
