package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class ComplexRepairRecipe extends CustomRecipe {
	public static final MapCodec<ComplexRepairRecipe> MAP_CODEC =
		RecordCodecBuilder.mapCodec(i -> i.group(
			Ingredient.CODEC.fieldOf("input").forGetter(o -> o.input),
			Ingredient.CODEC.listOf().fieldOf("repair_ingredients").forGetter(o -> o.repairItems),
			Codec.INT.fieldOf("durability").forGetter(o -> o.durability)
		).apply(i, ComplexRepairRecipe::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, ComplexRepairRecipe> STREAM_CODEC =
		StreamCodec.composite(
		Ingredient.CONTENTS_STREAM_CODEC, o -> o.input,
		Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.repairItems,
		ByteBufCodecs.INT, o -> o.durability,
		ComplexRepairRecipe::new
	);

	public static final RecipeSerializer<ComplexRepairRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final Ingredient input;
	private final List<Ingredient> repairItems;
	private final int durability;

	public ComplexRepairRecipe(Ingredient input, List<Ingredient> repairItems, int repairDurability) {
		super();
		this.input = input;
		this.repairItems = repairItems;
		this.durability = repairDurability;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack toRepair = null;
		if (this.repairItems.size() == 1) {
			int ingredients = 0;
			for (int i = 0; i < input.size(); ++i) {
				ItemStack stackInQuestion = input.getItem(i);
				if (!stackInQuestion.isEmpty()) {
					if (this.input.test(stackInQuestion) && stackInQuestion.isDamaged()) {
						if (toRepair != null) return false;
						toRepair = stackInQuestion;
					} else if (this.repairItems.getFirst().test(stackInQuestion)) {
						ingredients++;
					} else {
						return false;
					}
				}
			}
			int duraRes = ingredients * this.getRepairDurability();
			return toRepair != null && (ingredients > 0 && (toRepair.getDamageValue() + this.getRepairDurability() - duraRes) > 0);
		} else {
			for (int i = 0; i < input.size(); ++i) {
				ItemStack stackInQuestion = input.getItem(i);
				if (!stackInQuestion.isEmpty()) {
					if (this.input.test(stackInQuestion) && stackInQuestion.isDamaged()) {
						if (toRepair != null) return false;
						toRepair = stackInQuestion;
					}
				}
			}
			return toRepair != null && this.repairItems.size() == input.ingredientCount() - 1 && input.stackedContents().canCraft(this, null);
		}

	}

	@Override
	public ItemStack assemble(CraftingInput craftingInput) {
		ItemStack toRepair = null;
		int ingredients = 0;
		for (int i = 0; i < craftingInput.size(); ++i) {
			ItemStack stackInQuestion = craftingInput.getItem(i);
			if (!stackInQuestion.isEmpty()) {
				if (this.input.test(stackInQuestion) && stackInQuestion.getDamageValue() > 0) {
					toRepair = stackInQuestion;
				} else if (this.repairItems.size() == 1 && this.repairItems.getFirst().test(stackInQuestion)) {
					ingredients++;
				}
			}
		}

		if (toRepair != null) {
			var copy = toRepair.copy();
			if (ingredients > 0) {
				copy.setDamageValue(toRepair.getDamageValue() - (this.getRepairDurability() * ingredients));
			} else {
				copy.setDamageValue(toRepair.getDamageValue() - this.durability);
			}
			return copy;
		}
		return ItemStack.EMPTY;
	}

	public Ingredient getInput() {
		return this.input;
	}

	public int getRepairDurability() {
		return this.durability;
	}

	public List<Ingredient> getRepairItems() {
		return this.repairItems;
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}
}
