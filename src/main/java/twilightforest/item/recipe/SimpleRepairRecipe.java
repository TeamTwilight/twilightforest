package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class SimpleRepairRecipe extends CustomRecipe {

	public static final MapCodec<SimpleRepairRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
		Ingredient.CODEC.fieldOf("input").forGetter(o -> o.input),
		Ingredient.CODEC.fieldOf("repair_ingredient").forGetter(o -> o.repairItem),
		Codec.intRange(0, 8).fieldOf("max_repair_items").forGetter(o -> o.maxRepairItems),
		Codec.INT.fieldOf("repair_durability_per_item").forGetter(o -> o.repairDurabilityPerItem)
	).apply(i, SimpleRepairRecipe::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, SimpleRepairRecipe> STREAM_CODEC = StreamCodec.composite(
		Ingredient.CONTENTS_STREAM_CODEC, o -> o.input,
		Ingredient.CONTENTS_STREAM_CODEC, o -> o.repairItem,
		ByteBufCodecs.INT, o -> o.maxRepairItems,
		ByteBufCodecs.INT, o -> o.repairDurabilityPerItem,
		SimpleRepairRecipe::new
	);

	public static final RecipeSerializer<SimpleRepairRecipe> SERIALIZER =
		new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final Ingredient input;
	private final Ingredient repairItem;
	private final int maxRepairItems;
	private final int repairDurabilityPerItem;

	public SimpleRepairRecipe(Ingredient input, Ingredient repairItem, int maxRepairItems, int repairDurabilityPerItem) {
		this.input = input;
		this.repairItem = repairItem;
		this.maxRepairItems = maxRepairItems;
		this.repairDurabilityPerItem = repairDurabilityPerItem;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack toRepair = null;
		List<ItemStack> repairIngredients = new ArrayList<>();

		for (int i = 0; i < input.size(); ++i) {
			ItemStack stackInQuestion = input.getItem(i);
			if (!stackInQuestion.isEmpty()) {
				if (this.input.test(stackInQuestion) && stackInQuestion.isDamaged()) {
					if (toRepair != null) return false;
					toRepair = stackInQuestion;
				} else if (this.repairItem.test(stackInQuestion)) {
					repairIngredients.add(stackInQuestion);
				} else {
					return false;
				}
			}
		}
		if (toRepair != null && !repairIngredients.isEmpty()) {
			int damage = toRepair.getDamageValue();
			return (damage - this.getRepairDurabilityPerItem() * repairIngredients.size()) >= 0;
		}
		return false;
	}

	@Override
	public ItemStack assemble(CraftingInput craftingInput) {
		ItemStack toRepair = null;
		List<ItemStack> repairIngredients = new ArrayList<>();
		for (int i = 0; i < craftingInput.size(); ++i) {
			ItemStack stackInQuestion = craftingInput.getItem(i);
			if (!stackInQuestion.isEmpty()) {
				if (this.input.test(stackInQuestion)) {
					if (toRepair == null) {
						toRepair = stackInQuestion;
					} else {
						//Only accept 1 item to repair
						return ItemStack.EMPTY;
					}
				}

				if (this.repairItem.test(stackInQuestion)) {
					//add all repair ingredients in the grid to a list to determine the amount of durability to repair
					repairIngredients.add(stackInQuestion);
				}
			}
		}

		if (!repairIngredients.isEmpty() && repairIngredients.size() <= this.maxRepairItems && toRepair != null && toRepair.isDamaged()) {
			ItemStack repaired = toRepair.copy();
			repaired.setDamageValue(toRepair.getDamageValue() - (repairIngredients.size() * this.repairDurabilityPerItem));
			return repaired;
		}

		return ItemStack.EMPTY;
	}

	public Ingredient getInput() {
		return this.input;
	}

	public Ingredient getRepairItem() {
		return this.repairItem;
	}

	public int getMaxRepairItems() {
		return this.maxRepairItems;
	}

	public int getRepairDurabilityPerItem() {
		return this.repairDurabilityPerItem;
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}
}
