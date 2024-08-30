package twilightforest.item.recipe;

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
import twilightforest.init.TFRecipes;

import java.util.List;

public class ScepterRepairRecipe extends CustomRecipe {

	private final Item scepter;
	private final List<Ingredient> repairItems;

	public ScepterRepairRecipe(Item scepter, List<Ingredient> repairItems, CraftingBookCategory category) {
		super(category);
		this.scepter = scepter;
		this.repairItems = repairItems;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		if (input.ingredientCount() != this.repairItems.size() + 1) {
			return false;
		}
		ItemStack scepter = null;
		for (int i = 0; i < input.size(); ++i) {
			ItemStack stackInQuestion = input.getItem(i);
			if (!stackInQuestion.isEmpty()) {
				if (stackInQuestion.is(this.scepter) && stackInQuestion.getDamageValue() >= 0) {
					scepter = stackInQuestion;
					break;
				}
			}
		}
		return scepter != null && input.stackedContents().canCraft(this, null);
	}

	public Item getScepter() {
		return this.scepter;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.copyOf(this.repairItems);
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		return new ItemStack(this.scepter);
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
				CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(CustomRecipe::category)
			).apply(instance, ScepterRepairRecipe::new)
		);
		public static final StreamCodec<RegistryFriendlyByteBuf, ScepterRepairRecipe> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(Registries.ITEM), o -> o.scepter,
			Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.repairItems,
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
