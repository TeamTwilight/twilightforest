package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import twilightforest.init.TFRecipes;

import java.util.List;

public class ScepterRepairRecipe extends CustomRecipe {

	private final Item scepter;
	private final List<Ingredient> repairItems;
	private final int durability;

	public ScepterRepairRecipe(Item scepter, List<Ingredient> repairItems, int repairDurability) {
		super();
		this.scepter = scepter;
		this.repairItems = repairItems;
		this.durability = repairDurability;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack scepter = null;
		if (this.repairItems.size() == 1) {
			int ingredients = 0;
			for (int i = 0; i < input.size(); ++i) {
				ItemStack stackInQuestion = input.getItem(i);
				if (!stackInQuestion.isEmpty()) {
					if (stackInQuestion.is(this.scepter) && stackInQuestion.getDamageValue() > 0) {
						if (scepter != null) return false;
						scepter = stackInQuestion;
					} else if (this.repairItems.getFirst().test(stackInQuestion)) {
						ingredients++;
					} else {
						return false;
					}
				}
			}
			int duraRes = ingredients * this.getRepairDurability();
			return scepter != null && (ingredients > 0 && (scepter.getDamageValue() + this.getRepairDurability() - duraRes) > 0);
		} else {
			for (int i = 0; i < input.size(); ++i) {
				ItemStack stackInQuestion = input.getItem(i);
				if (!stackInQuestion.isEmpty()) {
					if (stackInQuestion.is(this.scepter) && stackInQuestion.getDamageValue() > 0) {
						if (scepter != null) return false;
						scepter = stackInQuestion;
					}
				}
			}
			return scepter != null && this.repairItems.size() == input.ingredientCount() - 1 && input.stackedContents().canCraft(this, null);
		}

	}

	@Override
	public ItemStack assemble(CraftingInput input) {
		ItemStack scepter = null;
		int ingredients = 0;
		for (int i = 0; i < input.size(); ++i) {
			ItemStack stackInQuestion = input.getItem(i);
			if (!stackInQuestion.isEmpty()) {
				if (stackInQuestion.is(this.scepter) && stackInQuestion.getDamageValue() > 0) {
					scepter = stackInQuestion;
				} else if (this.repairItems.size() == 1 && this.repairItems.getFirst().test(stackInQuestion)) {
					ingredients++;
				}
			}
		}

		if (scepter != null) {
			var copy = new ItemStack(this.scepter);
			copy.applyComponents(scepter.getComponents());
			if (ingredients > 0) {
				copy.setDamageValue(scepter.getDamageValue() - (this.getRepairDurability() * ingredients));
			} else {
				copy.setDamageValue(scepter.getDamageValue() - this.durability);
			}
			return copy;
		}
		return ItemStack.EMPTY;
	}

	public Item getScepter() {
		return this.scepter;
	}

	public List<Ingredient> getRepairItems() {
		return this.repairItems;
	}

	public int getRepairDurability() {
		return this.durability;
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return TFRecipes.SCEPTER_REPAIR_RECIPE.get();
	}

	public static final MapCodec<ScepterRepairRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("scepter").forGetter(o -> o.scepter),
			Ingredient.CODEC.listOf().fieldOf("repair_ingredients").forGetter(o -> o.repairItems),
			Codec.INT.fieldOf("durability").forGetter(o -> o.durability)
		).apply(instance, ScepterRepairRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, ScepterRepairRecipe> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.registry(Registries.ITEM), o -> o.scepter,
		Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.repairItems,
		ByteBufCodecs.INT, o -> o.durability,
		ScepterRepairRecipe::new
	);

	public static final RecipeSerializer<ScepterRepairRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
}
