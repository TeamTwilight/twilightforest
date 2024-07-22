package twilightforest.data.helpers;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFChestBlock;
import twilightforest.block.TFTrappedChestBlock;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;

import java.util.concurrent.CompletableFuture;

public abstract class CraftingDataHelper extends RecipeProvider {
	public CraftingDataHelper(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	protected final void charmRecipe(RecipeOutput output, String name, DeferredHolder<Item, ? extends Item> result, DeferredHolder<Item, ? extends Item> item) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, result.get())
			.requires(item.get(), 4)
			.unlockedBy("has_item", has(item.get()))
			.save(output, TwilightForestMod.prefix(name));
	}

	protected final void castleBlock(RecipeOutput output, DeferredHolder<Block, ? extends Block> result, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 4)
			.pattern("##")
			.pattern("##")
			.define('#', Ingredient.of(ingredients))
			.unlockedBy("has_castle_brick", has(TFBlocks.CASTLE_BRICK.get()))
			.save(output, locCastle(BuiltInRegistries.BLOCK.getKey(result.get()).getPath()));
	}

	protected final void woodenStairsBlock(RecipeOutput output, ResourceLocation loc, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> criteria, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 8)
			.pattern("#  ")
			.pattern("## ")
			.pattern("###")
			.define('#', Ingredient.of(ingredients))
			.unlockedBy("has_item", has(criteria.get()))
			.group("wooden_stairs")
			.save(output, loc);
	}

	protected final void stairsBlock(RecipeOutput output, ResourceLocation loc, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> criteria, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 8)
			.pattern("#  ")
			.pattern("## ")
			.pattern("###")
			.define('#', Ingredient.of(ingredients))
			.unlockedBy("has_item", has(criteria.get()))
			.save(output, loc);
	}

	protected final void stairsRightBlock(RecipeOutput output, ResourceLocation loc, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> criteria, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 8)
			.pattern("###")
			.pattern(" ##")
			.pattern("  #")
			.define('#', Ingredient.of(ingredients))
			.unlockedBy("has_item", has(criteria.get()))
			.save(output, loc);
	}

	protected final void compressedBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, TagKey<Item> ingredient) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get())
			.pattern("###")
			.pattern("###")
			.pattern("###")
			.define('#', ingredient)
			.unlockedBy("has_item", has(ingredient))
			.save(output, TwilightForestMod.prefix("compressed_blocks/" + name));
	}

	protected final void reverseCompressBlock(RecipeOutput output, String name, DeferredHolder<Item, ? extends Item> result, TagKey<Item> ingredient) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), 9)
			.requires(ingredient)
			.unlockedBy("has_item", has(ingredient))
			.save(output, TwilightForestMod.prefix("compressed_blocks/reversed/" + name));
	}

	protected final void helmetItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material) {
		this.helmetItem(output, result, material, DataComponentPatch.builder());
	}

	protected final void helmetItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, new ItemStack(result, 1, component.build()))
			.pattern("###")
			.pattern("# #")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.save(output, locEquip(result.getKey().location().getPath()));
	}

	protected final void chestplateItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material) {
		this.chestplateItem(output, result, material, DataComponentPatch.builder());
	}

	protected final void chestplateItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, new ItemStack(result, 1, component.build()))
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.save(output, locEquip(result.getKey().location().getPath()));
	}

	protected final void leggingsItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material) {
		this.leggingsItem(output, result, material, DataComponentPatch.builder());
	}

	protected final void leggingsItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, new ItemStack(result, 1, component.build()))
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.save(output, locEquip(result.getKey().location().getPath()));
	}

	protected final void bootsItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material) {
		this.bootsItem(output, result, material, DataComponentPatch.builder());
	}

	protected final void bootsItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, new ItemStack(result, 1, component.build()))
			.pattern("# #")
			.pattern("# #")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.save(output, locEquip(result.getKey().location().getPath()));
	}

	protected final void pickaxeItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material, TagKey<Item> handle) {
		this.pickaxeItem(output, result, material, handle, DataComponentPatch.builder());
	}

	protected final void pickaxeItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material, TagKey<Item> handle, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, new ItemStack(result, 1, component.build()))
			.pattern("###")
			.pattern(" X ")
			.pattern(" X ")
			.define('#', material)
			.define('X', handle)
			.unlockedBy("has_item", has(material))
			.save(output, locEquip(result.getKey().location().getPath()));
	}

	protected final void swordItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material, TagKey<Item> handle) {
		this.swordItem(output, result, material, handle, DataComponentPatch.builder());
	}

	protected final void swordItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material, TagKey<Item> handle, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, new ItemStack(result, 1, component.build()))
			.pattern("#")
			.pattern("#")
			.pattern("X")
			.define('#', material)
			.define('X', handle)
			.unlockedBy("has_item", has(material))
			.save(output, locEquip(result.getKey().location().getPath()));
	}

	protected final void axeItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material, TagKey<Item> handle) {
		this.axeItem(output, result, material, handle, DataComponentPatch.builder());
	}

	protected final void axeItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material, TagKey<Item> handle, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, new ItemStack(result, 1, component.build()))
			.pattern("##")
			.pattern("#X")
			.pattern(" X")
			.define('#', material)
			.define('X', handle)
			.unlockedBy("has_item", has(material))
			.save(output, locEquip(result.getKey().location().getPath()));
	}

	protected final void shovelItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material, TagKey<Item> handle, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, new ItemStack(result, 1, component.build()))
			.pattern("#")
			.pattern("X")
			.pattern("X")
			.define('#', material)
			.define('X', handle)
			.unlockedBy("has_item", has(material))
			.save(output, locEquip(result.getKey().location().getPath()));
	}

	protected final void hoeItem(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, TagKey<Item> material, TagKey<Item> handle, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, new ItemStack(result, 1, component.build()))
			.pattern("##")
			.pattern(" X")
			.pattern(" X")
			.define('#', material)
			.define('X', handle)
			.unlockedBy("has_item", has(material))
			.save(output, locEquip(result.getKey().location().getPath()));
	}

	@SafeVarargs
	protected final DataComponentPatch.Builder buildEnchants(HolderLookup.Provider provider, Pair<ResourceKey<Enchantment>, Integer>... enchantments) {
		HolderLookup.RegistryLookup<Enchantment> lookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
		var itemEnchants = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		for (var pair : enchantments) {
			itemEnchants.set(lookup.getOrThrow(pair.getFirst()), pair.getSecond());
		}
		return DataComponentPatch.builder().set(DataComponents.ENCHANTMENTS, itemEnchants.toImmutable());
	}

	protected final void buttonBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> material) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, result.get())
			.requires(material.get())
			.unlockedBy("has_item", has(material.get()))
			.group("wooden_button")
			.save(output, locWood(name + "_button"));
	}

	protected final void doorBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, result.get(), 3)
			.pattern("##")
			.pattern("##")
			.pattern("##")
			.define('#', material.get())
			.unlockedBy("has_item", has(material.get()))
			.group("wooden_door")
			.save(output, locWood(name + "_door"));
	}

	protected final void fenceBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get(), 3)
			.pattern("#S#")
			.pattern("#S#")
			.define('#', material.get())
			.define('S', Tags.Items.RODS_WOODEN)
			.unlockedBy("has_item", has(material.get()))
			.group("wooden_fence")
			.save(output, locWood(name + "_fence"));
	}

	protected final void gateBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, result.get())
			.pattern("S#S")
			.pattern("S#S")
			.define('#', material.get())
			.define('S', Tags.Items.RODS_WOODEN)
			.unlockedBy("has_item", has(material.get()))
			.group("wooden_fence_gate")
			.save(output, locWood(name + "_gate"));
	}

	protected final void planksBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, TagKey<Item> material) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, result.get(), 4)
			.requires(material)
			.unlockedBy("has_item", has(material))
			.group("planks")
			.save(output, locWood(name + "_planks"));
	}

	protected final void plateBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, result.get())
			.pattern("##")
			.define('#', material.get())
			.unlockedBy("has_item", has(material.get()))
			.group("wooden_pressure_plate")
			.save(output, locWood(name + "_plate"));
	}

	protected final void woodenSlabBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 6)
			.pattern("###")
			.define('#', material.get())
			.unlockedBy("has_item", has(material.get()))
			.group("wooden_slab")
			.save(output, locWood(name + "_slab"));
	}

	protected final void slabBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 6)
			.pattern("###")
			.define('#', material.get())
			.unlockedBy("has_item", has(material.get()))
			.save(output, locWood(name + "_slab"));
	}

	protected final void bannerPattern(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> trophy, DeferredHolder<Item, ? extends Item> result) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get())
			.requires(Ingredient.of(ItemTagGenerator.PAPER))
			.requires(Ingredient.of(trophy.get().asItem()))
			.unlockedBy("has_trophy", has(trophy.get()))
			.save(output);
	}

	protected final void trapdoorBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, result.get(), 2)
			.pattern("###")
			.pattern("###")
			.define('#', material.get())
			.unlockedBy("has_item", has(material.get()))
			.group("wooden_trapdoor")
			.save(output, locWood(name + "_trapdoor"));
	}

	protected final void woodBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 3)
			.pattern("##")
			.pattern("##")
			.define('#', material.get())
			.unlockedBy("has_item", has(material.get()))
			.group("bark")
			.save(output, locWood(name + "_wood"));
	}

	protected final void strippedWoodBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 3)
			.pattern("##")
			.pattern("##")
			.define('#', material.get())
			.unlockedBy("has_item", has(material.get()))
			.save(output, locWood(name + "_stripped_wood"));
	}

	protected final void signBlock(RecipeOutput output, String name, DeferredHolder<Item, ? extends Item> result, DeferredHolder<Block, ? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get(), 3)
			.pattern("###")
			.pattern("###")
			.pattern(" - ")
			.define('#', material.get())
			.define('-', Tags.Items.RODS_WOODEN)
			.unlockedBy("has_item", has(material.get()))
			.group("wooden_sign")
			.save(output, locWood(name + "_sign"));
	}

	protected final void hangingSignBlock(RecipeOutput output, String name, DeferredHolder<Item, ? extends Item> result, DeferredHolder<Block, ? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get(), 6)
			.pattern("| |")
			.pattern("###")
			.pattern("###")
			.define('#', material.get())
			.define('|', Items.CHAIN)
			.unlockedBy("has_item", has(material.get()))
			.group("hanging_sign")
			.save(output, locWood(name + "_hanging_sign"));
	}

	protected final void banisterBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, DeferredHolder<Block, ? extends Block> material) {
		this.banisterBlock(output, name, result, material.get());
	}

	protected final void banisterBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends Block> result, Block material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get(), 3)
			.pattern("---")
			.pattern("| |")
			.define('-', material)
			.define('|', Tags.Items.RODS_WOODEN)
			.unlockedBy("has_item", has(material))
			.group("wooden_banister")
			.save(output, locWood(name + "_banister"));
	}

	protected final void chestBlock(RecipeOutput output, String name, DeferredHolder<Block, ? extends TFChestBlock> chest, DeferredHolder<Block, ? extends TFTrappedChestBlock> trapped, DeferredHolder<Block, ? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, chest.get(), 2)
			.pattern("###")
			.pattern("#C#")
			.pattern("###")
			.define('#', material.get())
			.define('C', Items.CHEST)
			.unlockedBy("has_item", has(material.get()))
			.group("chest")
			.save(output, locWood(name + "_chest"));

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, trapped.get(), 2)
			.pattern("###")
			.pattern("#C#")
			.pattern("###")
			.define('#', material.get())
			.define('C', Items.TRAPPED_CHEST)
			.unlockedBy("has_item", has(material.get()))
			.group("trapped_chest")
			.save(output, locWood(name + "_trapped_chest"));
	}

	protected final void fieryConversion(RecipeOutput output, DeferredHolder<Item, ? extends Item> result, Item armor, int vials) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, result.get())
			.requires(armor)
			.requires(Ingredient.of(ItemTagGenerator.FIERY_VIAL), vials)
			.unlockedBy("has_item", has(ItemTagGenerator.FIERY_VIAL))
			.group(result.getKey().location().getPath())
			.save(output, locEquip("fiery_" + BuiltInRegistries.ITEM.getKey(armor).getPath()));
	}

	protected final void buildBoats(RecipeOutput output, DeferredHolder<Item, ? extends Item> boat, DeferredHolder<Item, ? extends Item> chestBoat, DeferredHolder<Block, ? extends Block> planks) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, boat.get())
			.pattern("P P")
			.pattern("PPP")
			.define('P', planks.get())
			.group("boat")
			.unlockedBy("in_water", insideOf(Blocks.WATER))
			.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, chestBoat.get())
			.requires(boat.get())
			.requires(Tags.Items.CHESTS_WOODEN)
			.group("chest_boat")
			.unlockedBy("has_boat", has(ItemTags.BOATS))
			.save(output);
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

	protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> tag) {
		return inventoryTrigger(ItemPredicate.Builder.item().of(tag).build());
	}
}
