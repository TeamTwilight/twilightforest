package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;

import java.util.List;

public class ScepterRepairRecipe extends CustomRecipe {

	private final Item scepter;
	private final List<Ingredient> repairItems;
	private final int durability;

	public ScepterRepairRecipe(Item scepter, List<Ingredient> repairItems, int repairDurability, CraftingBookCategory category) {
		super(category);
		this.scepter = scepter;
		this.repairItems = repairItems;
		this.durability = repairDurability;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack scepter = null;
		boolean hasEssence = false;
		int ingredients = 0;
		for (int i = 0; i < input.size(); ++i) {
			ItemStack stackInQuestion = input.getItem(i);
			if (!stackInQuestion.isEmpty()) {
				if (stackInQuestion.is(this.scepter) && stackInQuestion.getDamageValue() > 0) {
					scepter = stackInQuestion;
				} else if (stackInQuestion.is(TFItems.EXANIMATE_ESSENCE)) {
					if (hasEssence) return false; //only 1 essence
					hasEssence = true;
				} else if (this.repairItems.size() == 1 && this.repairItems.getFirst().test(stackInQuestion)) {
					ingredients++;
				} else if (this.repairItems.size() == 1 && !this.repairItems.getFirst().test(stackInQuestion)) {
					break;
				}
			}
		}
		int duraRes = ingredients * this.durability;
		return scepter != null && (hasEssence || (ingredients > 0 && (duraRes + (scepter.getMaxDamage() - scepter.getDamageValue())) < scepter.getMaxDamage()) || input.stackedContents().canCraft(this, null));
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		ItemStack scepter = null;
		boolean hasEssence = false;
		int ingredients = 0;
		for (int i = 0; i < input.size(); ++i) {
			ItemStack stackInQuestion = input.getItem(i);
			if (!stackInQuestion.isEmpty()) {
				if (stackInQuestion.is(this.scepter) && stackInQuestion.getDamageValue() > 0) {
					scepter = stackInQuestion;
				} else if (stackInQuestion.is(TFItems.EXANIMATE_ESSENCE)) {
					hasEssence = true;
				} else if (this.repairItems.size() == 1 && this.repairItems.getFirst().test(stackInQuestion)) {
					ingredients++;
				}
			}
		}

		if (scepter != null) {
			if (hasEssence) {
				return new ItemStack(this.scepter);
			} else if (ingredients > 0) {
				var copy = new ItemStack(this.scepter);
				copy.setDamageValue(scepter.getDamageValue() - (this.durability * ingredients));
				return copy;
			} else {
				var copy = new ItemStack(this.scepter);
				copy.setDamageValue(scepter.getDamageValue() - this.durability);
				return copy;
			}
		}
		return ItemStack.EMPTY;
	}

	public Item getScepter() {
		return this.scepter;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.copyOf(this.repairItems);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return this.repairItems.size() + 1 > width * height;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.SCEPTER_REPAIR_RECIPE.get();
	}

	public static class Serializer implements RecipeSerializer<ScepterRepairRecipe> {

		public static final MapCodec<ScepterRepairRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				BuiltInRegistries.ITEM.byNameCodec().fieldOf("scepter").forGetter(o -> o.scepter),
				Ingredient.CODEC_NONEMPTY.listOf().fieldOf("repair_ingredients").forGetter(o -> o.repairItems),
				Codec.INT.fieldOf("durability").forGetter(o -> o.durability),
				CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(CustomRecipe::category)
			).apply(instance, ScepterRepairRecipe::new)
		);
		public static final StreamCodec<RegistryFriendlyByteBuf, ScepterRepairRecipe> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(Registries.ITEM), o -> o.scepter,
			Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.repairItems,
			ByteBufCodecs.INT, o -> o.durability,
			CraftingBookCategory.STREAM_CODEC, CustomRecipe::category,
			ScepterRepairRecipe::new
		);

		@Override
		public MapCodec<ScepterRepairRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ScepterRepairRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
