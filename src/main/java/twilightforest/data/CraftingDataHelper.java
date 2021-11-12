package twilightforest.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.fmllegacy.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.block.TwilightChest;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class CraftingDataHelper extends RecipeProvider {
	public CraftingDataHelper(DataGenerator generator) {
		super(generator);
	}

	protected final Ingredient itemWithNBT(RegistryObject<? extends ItemLike> item, Consumer<CompoundTag> nbtSetter) {
		return itemWithNBT(item.get(), nbtSetter);
	}

	protected final Ingredient itemWithNBT(ItemLike item, Consumer<CompoundTag> nbtSetter) {
		ItemStack stack = new ItemStack(item);

		CompoundTag nbt = new CompoundTag();
		nbtSetter.accept(nbt);
		stack.setTag(nbt);

		try {
			Constructor<NBTIngredient> constructor = NBTIngredient.class.getDeclaredConstructor(ItemStack.class);

			constructor.setAccessible(true);

			return constructor.newInstance(stack);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		// This will just defer to the regular Ingredient method instead of some overridden thing, but whatever.
		// Forge PRs are too slow to even feel motivated about fixing it on the Forge end.
		return Ingredient.of(stack);
	}

	protected final Ingredient multipleIngredients(Ingredient... ingredientArray) {
		List<Ingredient> ingredientList = ImmutableList.copyOf(ingredientArray);

		try {
			Constructor<CompoundIngredient> constructor = CompoundIngredient.class.getDeclaredConstructor(List.class);

			constructor.setAccessible(true);

			return constructor.newInstance(ingredientList);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		// This will just defer to the regular Ingredient method instead of some overridden thing, but whatever.
		// Forge PRs are too slow to even feel motivated about fixing it on the Forge end.
		return Ingredient.merge(ingredientList);
	}

	protected final void charmRecipe(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, Supplier<? extends Item> item) {
		ShapelessRecipeBuilder.shapeless(result.get())
				.requires(item.get(), 4)
				.unlockedBy("has_item", has(item.get()))
				.save(consumer, TwilightForestMod.prefix(name));
	}

	protected final void castleBlock(Consumer<FinishedRecipe> consumer, Supplier<? extends Block> result, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(result.get(), 4)
				.pattern("##")
				.pattern("##")
				.define('#', Ingredient.of(ingredients))
				.unlockedBy("has_castle_brick", has(TFBlocks.CASTLE_BRICK.get()))
				.save(consumer, locCastle(result.get().getRegistryName().getPath()));
	}

	protected final void stairsBlock(Consumer<FinishedRecipe> consumer, ResourceLocation loc, Supplier<? extends Block> result, Supplier<? extends Block> criteria, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(result.get(),  8)
				.pattern("#  ")
				.pattern("## ")
				.pattern("###")
				.define('#', Ingredient.of(ingredients))
				.unlockedBy("has_item", has(criteria.get()))
				.save(consumer, loc);
	}

	protected final void stairsRightBlock(Consumer<FinishedRecipe> consumer, ResourceLocation loc, Supplier<? extends Block> result, Supplier<? extends Block> criteria, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(result.get(),  8)
				.pattern("###")
				.pattern(" ##")
				.pattern("  #")
				.define('#', Ingredient.of(ingredients))
				.unlockedBy("has_item", has(criteria.get()))
				.save(consumer, loc);
	}

	protected final void compressedBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Tag.Named<Item> ingredient) {
		ShapedRecipeBuilder.shaped(result.get())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ingredient)
				.unlockedBy("has_item", has(ingredient))
				.save(consumer, TwilightForestMod.prefix("compressed_blocks/" + name));
	}

	protected final void reverseCompressBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, Tag.Named<Item> ingredient) {
		ShapelessRecipeBuilder.shapeless(result.get(), 9)
				.requires(ingredient)
				.unlockedBy("has_item", has(ingredient))
				.save(consumer, TwilightForestMod.prefix("compressed_blocks/reversed/" + name));
	}

	protected final void helmetItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, Tag.Named<Item> material) {
		ShapedRecipeBuilder.shaped(result.get())
				.pattern("###")
				.pattern("# #")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void chestplateItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, Tag.Named<Item> material) {
		ShapedRecipeBuilder.shaped(result.get())
				.pattern("# #")
				.pattern("###")
				.pattern("###")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void leggingsItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, Tag.Named<Item> material) {
		ShapedRecipeBuilder.shaped(result.get())
				.pattern("###")
				.pattern("# #")
				.pattern("# #")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void bootsItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, Tag.Named<Item> material) {
		ShapedRecipeBuilder.shaped(result.get())
				.pattern("# #")
				.pattern("# #")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void pickaxeItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, Tag.Named<Item> material, Tag.Named<Item> handle) {
		ShapedRecipeBuilder.shaped(result.get())
				.pattern("###")
				.pattern(" X ")
				.pattern(" X ")
				.define('#', material)
				.define('X', handle)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void swordItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, Tag.Named<Item> material, Tag.Named<Item> handle) {
		ShapedRecipeBuilder.shaped(result.get())
				.pattern("#")
				.pattern("#")
				.pattern("X")
				.define('#', material)
				.define('X', handle)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void axeItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, Tag.Named<Item> material, Tag.Named<Item> handle) {
		ShapedRecipeBuilder.shaped(result.get())
				.pattern("##")
				.pattern("#X")
				.pattern(" X")
				.define('#', material)
				.define('X', handle)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void buttonBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapelessRecipeBuilder.shapeless(result.get())
				.requires(material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_button"));
	}

	protected final void doorBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(result.get(), 3)
				.pattern("##")
				.pattern("##")
				.pattern("##")
				.define('#', material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_door"));
	}

	protected final void fenceBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(result.get(), 3)
				.pattern("#S#")
				.pattern("#S#")
				.define('#', material.get())
				.define('S', Tags.Items.RODS_WOODEN)
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_fence"));
	}

	protected final void gateBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(result.get())
				.pattern("S#S")
				.pattern("S#S")
				.define('#', material.get())
				.define('S', Tags.Items.RODS_WOODEN)
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_gate"));
	}

	protected final void planksBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapelessRecipeBuilder.shapeless(result.get(), 4)
				.requires(material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_planks"));
	}

	protected final void plateBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(result.get())
				.pattern("##")
				.define('#', material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_plate"));
	}

	protected final void slabBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(result.get(), 6)
				.pattern("###")
				.define('#', material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_slab"));
	}

	protected final void bannerPattern(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> trophy, Supplier<? extends Item> result) {
		ShapelessRecipeBuilder.shapeless(result.get())
				.requires(Ingredient.of(ItemTagGenerator.PAPER))
				.requires(Ingredient.of(trophy.get().asItem()))
				.unlockedBy("has_trophy", has(trophy.get()))
				.save(consumer);
	}

	protected final void trapdoorBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(result.get(), 2)
				.pattern("###")
				.pattern("###")
				.define('#', material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_trapdoor"));
	}
	
	protected final void woodBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(result.get(), 3)
				.pattern("##")
				.pattern("##")
				.define('#', material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_wood"));
	}

	protected final void strippedWoodBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(result.get(), 3)
				.pattern("##")
				.pattern("##")
				.define('#', material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_stripped_wood"));
	}

	protected final void signBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(result.get(), 3)
				.pattern("###")
				.pattern("###")
				.pattern(" - ")
				.define('#', material.get())
				.define('-', Tags.Items.RODS_WOODEN)
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_wood"));
	}

	protected final void banisterBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		this.banisterBlock(consumer, name, result, material.get());
	}

	protected final void banisterBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Block material) {
		ShapedRecipeBuilder.shaped(result.get(), 3)
				.pattern("---")
				.pattern("| |")
				.define('-', material)
				.define('|', Tags.Items.RODS_WOODEN)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_banister"));
	}

	protected final void chestBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends TwilightChest> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(result.get(), 2)
				.pattern("###")
				.pattern("#C#")
				.pattern("###")
				.define('#', material.get())
				.define('C', Tags.Items.CHESTS_WOODEN)
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_chest"));
	}

	protected final void fieryConversion(Consumer<FinishedRecipe> consumer, Supplier<? extends Item> result, Item armor, int vials) {
		ShapelessRecipeBuilder.shapeless(result.get())
				.requires(armor)
				.requires(Ingredient.of(ItemTagGenerator.FIERY_VIAL), vials)
				.unlockedBy("has_item", has(ItemTagGenerator.FIERY_VIAL))
				.save(consumer, locEquip("fiery_" + armor.getRegistryName().getPath()));
	}

	protected final ResourceLocation locCastle(String name) {
		return TwilightForestMod.prefix("castleblock/" + name);
	}

	protected final ResourceLocation locEquip(String name) {
		return TwilightForestMod.prefix("equipment/" + name);
	}

	protected final ResourceLocation locNaga(String name) {
		return TwilightForestMod.prefix("nagastone/" + name);
	}

	protected final ResourceLocation locWood(String name) {
		return TwilightForestMod.prefix("wood/" + name);
	}
}
