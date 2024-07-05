package twilightforest.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.IShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFMenuTypes;
import twilightforest.init.TFRecipes;
import twilightforest.inventory.slot.AssemblySlot;
import twilightforest.inventory.slot.UncraftingResultSlot;
import twilightforest.inventory.slot.UncraftingSlot;
import twilightforest.item.recipe.UncraftingRecipe;
import twilightforest.util.TFItemStackUtils;

import java.util.*;

public class UncraftingMenu extends RecipeBookMenu<RecipeInput, Recipe<RecipeInput>> {

	private static final String TAG_MARKER = "TwilightForestMarker";

	// Inaccessible grid, for uncrafting logic
	private final UncraftingContainer uncraftingMatrix = new UncraftingContainer(this);
	// Accessible grid, for actual crafting
	public final CraftingContainer assemblyMatrix = new TransientCraftingContainer(this, 3, 3);
	// Inaccessible grid, for recrafting logic
	private final CraftingContainer combineMatrix = new TransientCraftingContainer(this, 3, 3);

	// Input slot for uncrafting
	public final Container tinkerInput = new UncraftingInputContainer(this);
	// Crafting Output
	private final ResultContainer tinkerResult = new ResultContainer();

	private final ContainerLevelAccess positionData;
	private final Level level;
	private final Player player;

	// Conflict resolution
	public int unrecipeInCycle = 0;
	public int ingredientsInCycle = 0;
	public int recipeInCycle = 0;

	// Store the currently selected recipe for use later down the line.
	// Currently used for determining if the recipe is an uncrafting one and for determining custom costs
	@Nullable
	public Recipe<?> storedGhostRecipe = null;

	public static UncraftingMenu fromNetwork(int id, Inventory inventory) {
		return new UncraftingMenu(id, inventory, inventory.player.level(), ContainerLevelAccess.NULL);
	}

	public UncraftingMenu(int id, Inventory inventory, Level level, ContainerLevelAccess positionData) {
		super(TFMenuTypes.UNCRAFTING.get(), id);

		this.positionData = positionData;
		this.level = level;
		this.player = inventory.player;

		this.addSlot(new Slot(this.tinkerInput, 0, 13, 35));
		this.addSlot(new UncraftingResultSlot(inventory.player, this.tinkerInput, this.uncraftingMatrix, this.assemblyMatrix, this.tinkerResult, 0, 147, 35));

		int invX;
		int invY;

		for (invX = 0; invX < 3; ++invX) {
			for (invY = 0; invY < 3; ++invY) {
				this.addSlot(new UncraftingSlot(inventory.player, this.tinkerInput, this.uncraftingMatrix, this.assemblyMatrix, invY + invX * 3, 300000 + invY * 18, 17 + invX * 18));
			}
		}
		for (invX = 0; invX < 3; ++invX) {
			for (invY = 0; invY < 3; ++invY) {
				this.addSlot(new AssemblySlot(this.assemblyMatrix, invY + invX * 3, 62 + invY * 18, 17 + invX * 18));
			}
		}

		for (invX = 0; invX < 3; ++invX) {
			for (invY = 0; invY < 9; ++invY) {
				this.addSlot(new Slot(inventory, invY + invX * 9 + 9, 8 + invY * 18, 84 + invX * 18));
			}
		}

		for (invX = 0; invX < 9; ++invX) {
			this.addSlot(new Slot(inventory, invX, 8 + invX * 18, 142));
		}

		this.slotsChanged(this.assemblyMatrix);

		if (!FMLLoader.isProduction()) {
			// Debug slot listing
			NonNullList<Slot> slots = this.slots;

			StringJoiner joiner = new StringJoiner(",\n", "Uncrafting Menu Slots:\n", "(" + slots.size() + " total slots)");

			for (Slot slot : this.slots) {
				joiner.add("[index " + slot.index + ": " + slot.getClass().getName() + " (container slot: " + slot.getContainerSlot() + ")]");
			}

			TwilightForestMod.LOGGER.info(joiner.toString());
		}
	}

	@Override
	public void slotsChanged(Container inventory) {
		// we need to see what inventory is calling this, and update appropriately
		if (inventory == this.tinkerInput) {

			// empty whole grid to start with
			this.uncraftingMatrix.clearContent();

			// see if there is a recipe for the input
			ItemStack inputStack = tinkerInput.getItem(0);
			Recipe<?>[] recipes = getRecipesFor(inputStack, this.level);

			int size = recipes.length;

			if (size > 0 && !inputStack.is(ItemTagGenerator.BANNED_UNCRAFTABLES)) {

				Recipe<?> recipe = recipes[Math.floorMod(this.unrecipeInCycle, size)];
				this.storedGhostRecipe = recipe;
				ItemStack[] recipeItems = this.getIngredients(recipe);

				if (recipe instanceof IShapedRecipe<?> rec) {

					int recipeWidth = rec.getWidth();
					int recipeHeight = rec.getHeight();

					// set uncrafting grid
					for (int invY = 0; invY < recipeHeight; invY++) {
						for (int invX = 0; invX < recipeWidth; invX++) {

							int index = invX + invY * recipeWidth;
							if (index >= recipeItems.length) continue;

							ItemStack ingredient = normalizeIngredient(recipeItems[index].copy());
							this.uncraftingMatrix.setItem(invX + invY * 3, ingredient);
						}
					}
				} else {
					for (int i = 0; i < this.uncraftingMatrix.getContainerSize(); i++) {
						if (i < recipeItems.length) {
							ItemStack ingredient = normalizeIngredient(recipeItems[i].copy());
							this.uncraftingMatrix.setItem(i, ingredient);
						}
					}
				}


				// mark the appropriate number of damaged components
				if (inputStack.isDamaged()) {
					int damagedParts = this.countDamagedParts(inputStack);

					for (int i = 0; i < 9 && damagedParts > 0; i++) {
						ItemStack stack = this.uncraftingMatrix.getItem(i);
						if (isDamageableComponent(stack)) {
							markStack(stack);
							damagedParts--;
						}
					}
				}

				// mark banned items
				for (int i = 0; i < 9; i++) {
					ItemStack ingredient = this.uncraftingMatrix.getItem(i);
					if (isIngredientProblematic(ingredient)) {
						markStack(ingredient);
					}
				}

				// store number of items this recipe produces (and thus how many input items are required for uncrafting)
				this.uncraftingMatrix.numberOfInputItems = recipe instanceof UncraftingRecipe uncraftingRecipe ? uncraftingRecipe.count() : recipe.getResultItem(this.level.registryAccess()).getCount(); //Uncrafting recipes need this method call
				this.uncraftingMatrix.uncraftingCost = this.calculateUncraftingCost();
				this.uncraftingMatrix.recraftingCost = 0;

			} else {
				this.storedGhostRecipe = null;
				this.uncraftingMatrix.numberOfInputItems = 0;
				this.uncraftingMatrix.uncraftingCost = 0;
			}
		}
		// Now we've got the uncrafting logic set in, currently we don't modify the uncraftingMatrix. That's fine.
		if (inventory == this.assemblyMatrix || inventory == this.tinkerInput) {
			if (this.tinkerInput.isEmpty()) {
				// display the output
				this.chooseRecipe(this.assemblyMatrix.asCraftInput());
			} else {
				// we placed an item in the assembly matrix, the next step will re-initialize these with correct values
				this.tinkerResult.setItem(0, ItemStack.EMPTY);
				this.uncraftingMatrix.uncraftingCost = this.calculateUncraftingCost();
			}
			this.uncraftingMatrix.recraftingCost = 0;
		}

		// repairing / recrafting: if there is an input item, and items in both grids, can we combine them to produce an output item that is the same type as the input item?
		if (inventory != this.combineMatrix && !this.uncraftingMatrix.isEmpty() && !this.assemblyMatrix.isEmpty()) {
			// combine the two matrices
			for (int i = 0; i < 9; i++) {

				ItemStack assembly = this.assemblyMatrix.getItem(i);
				ItemStack uncrafting = this.uncraftingMatrix.getItem(i);

				if (!assembly.isEmpty()) {
					this.combineMatrix.setItem(i, assembly);
				} else if (!uncrafting.isEmpty() && !isMarked(uncrafting)) {
					this.combineMatrix.setItem(i, uncrafting);
				} else {
					this.combineMatrix.setItem(i, ItemStack.EMPTY);
				}
			}
			// is there a result from this combined thing?
			this.chooseRecipe(this.combineMatrix.asCraftInput());

			ItemStack input = this.tinkerInput.getItem(0);
			ItemStack result = this.tinkerResult.getItem(0);

			if (!result.isEmpty() && isValidMatchForInput(input, result)) {
				//store copy of input enchants
				ItemEnchantments.Mutable enchants = new ItemEnchantments.Mutable(input.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY));
				//add all resulting item enchants to the list. This allows pre-enchanted gear to keep its enchants
				if (result.has(DataComponents.ENCHANTMENTS)) {
					result.get(DataComponents.ENCHANTMENTS).entrySet().forEach(enchantment -> enchants.set(enchantment.getKey(), enchantment.getIntValue()));
				}
				//remove any incompatible enchants
				enchants.removeIf(holder -> !holder.value().canEnchant(result));

				//remove enchantments and replace with filtered list
				result.remove(DataComponents.ENCHANTMENTS);
				EnchantmentHelper.setEnchantments(result, enchants.toImmutable());

				this.tinkerResult.setItem(0, result);
				this.uncraftingMatrix.uncraftingCost = 0;
				this.uncraftingMatrix.recraftingCost = this.calculateRecraftingCost();
			}
		}
	}

	public static void markStack(ItemStack stack) {
		TFItemStackUtils.addInfoTag(stack, TAG_MARKER);
	}

	public static boolean isMarked(ItemStack stack) {
		return TFItemStackUtils.hasInfoTag(stack, TAG_MARKER);
	}

	//might be handy one day
	@SuppressWarnings("unused")
	public static void unmarkStack(ItemStack stack) {
		TFItemStackUtils.clearInfoTag(stack, TAG_MARKER);
	}

	private static boolean isIngredientProblematic(ItemStack ingredient) {
		return (!ingredient.isEmpty() && ingredient.getItem().hasCraftingRemainingItem(ingredient)) || ingredient.is(Items.BARRIER);
	}

	private static ItemStack normalizeIngredient(ItemStack ingredient) {
		if (ingredient.getCount() > 1) {
			ingredient.setCount(1);
		}
		return ingredient;
	}

	private static Recipe<?>[] getRecipesFor(ItemStack inputStack, Level world) {

		List<Recipe<?>> recipes = new ArrayList<>();

		if (!inputStack.isEmpty()) {
			for (RecipeHolder<?> recipe : world.getRecipeManager().getRecipes()) {
				if (isRecipeSupported(recipe.value()) &&
					!recipe.value().isIncomplete() &&
					recipe.value().canCraftInDimensions(3, 3) &&
					!recipe.value().getIngredients().isEmpty() &&
					matches(inputStack, recipe.value().getResultItem(world.registryAccess())) &&
					TFConfig.reverseRecipeBlacklist == TFConfig.disableUncraftingRecipes.contains(recipe.id().toString())) {
					if (TFConfig.flipUncraftingModIdList == TFConfig.blacklistedUncraftingModIds.contains(recipe.id().getNamespace())) {
						recipes.add(recipe.value());
					}
				}
			}
			for (RecipeHolder<UncraftingRecipe> uncraftingRecipe : world.getRecipeManager().getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE.get())) {
				if (uncraftingRecipe.value().isItemStackAnIngredient(inputStack)) recipes.add(uncraftingRecipe.value());
			}
		}

		return recipes.toArray(new Recipe<?>[0]);
	}

	private static boolean isRecipeSupported(Recipe<?> recipe) {
		return TFConfig.allowShapelessUncrafting ? recipe instanceof CraftingRecipe : recipe instanceof ShapedRecipe;
	}

	private static boolean matches(ItemStack input, ItemStack output) {
		return input.is(output.getItem()) && input.getCount() >= output.getCount();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static RecipeHolder<CraftingRecipe>[] getRecipesFor(CraftingInput input, Level level) {
		return level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, input, level).toArray(new RecipeHolder[0]);
	}

	private void chooseRecipe(CraftingInput input) {

		RecipeHolder<CraftingRecipe>[] recipes = getRecipesFor(input, this.level);

		if (recipes.length == 0) {
			this.tinkerResult.setItem(0, ItemStack.EMPTY);
			return;
		}

		RecipeHolder<CraftingRecipe> recipe = recipes[Math.floorMod(this.recipeInCycle, recipes.length)];

		if (recipe != null && (!this.level.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) || ((ServerPlayer) this.player).getRecipeBook().contains(recipe.id()))) {
			this.tinkerResult.setRecipeUsed(recipe);
			this.tinkerResult.setItem(0, recipe.value().assemble(input, this.level.registryAccess()));
		} else {
			this.tinkerResult.setItem(0, ItemStack.EMPTY);
		}
	}

	/**
	 * Checks if the result is a valid match for the input. Currently, only accepts armor or tools that are the same type as the input
	 */
	private static boolean isValidMatchForInput(ItemStack inputStack, ItemStack resultStack) {
		if (inputStack.is(ItemTags.PICKAXES) && resultStack.is(ItemTags.PICKAXES)) {
			return true;
		}
		if (inputStack.is(ItemTags.AXES) && resultStack.is(ItemTags.AXES)) {
			return true;
		}
		if (inputStack.is(ItemTags.SHOVELS) && resultStack.is(ItemTags.SHOVELS)) {
			return true;
		}
		if (inputStack.is(ItemTags.HOES) && resultStack.is(ItemTags.HOES)) {
			return true;
		}
		if (inputStack.is(ItemTags.SWORDS) && resultStack.is(ItemTags.SWORDS)) {
			return true;
		}
		if (inputStack.is(Tags.Items.TOOLS_BOW) && resultStack.is(Tags.Items.TOOLS_BOW)) {
			return true;
		}
		if (inputStack.is(Tags.Items.TOOLS_CROSSBOW) && resultStack.is(Tags.Items.TOOLS_CROSSBOW)) {
			return true;
		}
		if (inputStack.is(Tags.Items.TOOLS_FISHING_ROD) && resultStack.is(Tags.Items.TOOLS_FISHING_ROD)) {
			return true;
		}

		if (inputStack.getItem() instanceof ArmorItem input && resultStack.getItem() instanceof ArmorItem result) {
			return input.getEquipmentSlot() == result.getEquipmentSlot();
		}

		return false;
	}

	public int getUncraftingCost() {
		return this.uncraftingMatrix.uncraftingCost;
	}

	public int getRecraftingCost() {
		return this.uncraftingMatrix.recraftingCost;
	}

	/**
	 * Calculate the cost of uncrafting, if any. Return 0 if uncrafting is not available at this time
	 */
	private int calculateUncraftingCost() {
		// we don't want to display anything if there is anything in the assembly grid
		if ((!TFConfig.disableUncraftingOnly || this.storedGhostRecipe instanceof UncraftingRecipe) && this.assemblyMatrix.isEmpty()) {
			return this.storedGhostRecipe instanceof UncraftingRecipe recipe ? recipe.cost() : (int) Math.round(countDamageableParts(this.uncraftingMatrix) * TFConfig.uncraftingXpCostMultiplier);
		}
		return 0;
	}

	/**
	 * Return the cost of recrafting, if any.  Return 0 if recrafting is not available at this time
	 */
	private int calculateRecraftingCost() {

		ItemStack input = this.tinkerInput.getItem(0);
		ItemStack output = this.tinkerResult.getItem(0);

		if (input.isEmpty() || output.isEmpty()) {
			return 0;
		}

		// okay, if we're here the input item must be enchanted, and we are repairing or recrafting it
		int cost = 0;

		if (!ItemStack.isSameItem(input, output)) {
			// add each ingredient being used to the cost if recrafting
			cost += this.assemblyMatrix.getItems().stream().filter(stack -> !stack.isEmpty()).toList().size();
		}

		// look at the input's enchantments and total them up
		int enchantCost = countTotalEnchantmentCost(input);
		cost += enchantCost;

		// broken pieces cost
		int damagedCost = (1 + this.countDamagedParts(input)) * output.getEnchantments().size();
		cost += damagedCost;

		// minimum cost of 1 if we're even calling this part
		cost = Math.max(1, cost);

		return (int) Math.round(cost * TFConfig.repairingXpCostMultiplier);
	}

	private static int countTotalEnchantmentCost(ItemStack stack) {
		int count = 0;

		for (Object2IntMap.Entry<Holder<Enchantment>> entry : stack.getEnchantments().entrySet()) {
			Enchantment ench = entry.getKey().value();
			int level = entry.getIntValue();

			if (level > 0) {
				count += getWeightModifier(ench) * level;
				count += 1;
			}
		}

		return count;
	}

	private static int getWeightModifier(Enchantment ench) {
		return switch (ench.getWeight()) {
			case 1 -> 8;
			case 2 -> 4;
			case 3, 4, 5 -> 2;
			default -> 1;
		};
	}

	@Override
	public void clicked(int slotNum, int mouseButton, ClickType clickType, Player player) {

		// if the player is trying to take an item out of the assembly grid, and the assembly grid is empty, take the item from the uncrafting grid.
		if (slotNum > 0 && this.getSlotContainer(slotNum) == this.assemblyMatrix
			&& player.containerMenu.getCarried().isEmpty() && !this.slots.get(slotNum).hasItem()) {

			// is the assembly matrix empty?
			if (this.assemblyMatrix.isEmpty() && (clickType != ClickType.SWAP || player.getInventory().getItem(mouseButton).isEmpty())) {
				slotNum -= 9;
			}
		}

		// if the player is trying to take the result item and they don't have the XP to pay for it, reject them
		if (slotNum > 0 && this.getSlotContainer(slotNum) == this.tinkerResult
			&& this.calculateRecraftingCost() > player.experienceLevel && !player.getAbilities().instabuild) {

			return;
		}

		if (slotNum > 0 && this.getSlotContainer(slotNum) == this.uncraftingMatrix) {

			// don't allow uncrafting normal recipes if the server option is turned off
			if (TFConfig.disableUncraftingOnly && !(this.storedGhostRecipe instanceof UncraftingRecipe)) {
				return;
			}

			// similarly, reject uncrafting if they can't do that either
			if (this.calculateUncraftingCost() > player.experienceLevel && !player.getAbilities().instabuild) {
				return;
			}

			// finally, don't give them damaged goods
			ItemStack stackInSlot = this.slots.get(slotNum).getItem();
			if (stackInSlot.isEmpty() || isMarked(stackInSlot)) {
				return;
			}
		}

		super.clicked(slotNum, mouseButton, clickType, player);

		// just trigger this event whenever the input slot is clicked for any reason
		if (slotNum > 0 && this.getSlotContainer(slotNum) == this.tinkerInput) {
			this.slotsChanged(this.tinkerInput);
		}
	}

	@NotNull
	private Container getSlotContainer(int slotNum) {
		return this.slots.get(slotNum).container;
	}

	/**
	 * Should the specified item count for taking damage?
	 */
	private static boolean isDamageableComponent(ItemStack stack) {
		return !stack.isEmpty() && !stack.is(ItemTagGenerator.UNCRAFTING_IGNORES_COST);
	}

	/**
	 * Count how many items in an inventory can take damage
	 */
	private static int countDamageableParts(Container matrix) {
		int count = matrix.getContainerSize();
		for (int i = 0; i < matrix.getContainerSize(); i++) {

			if (isIngredientProblematic(matrix.getItem(i)) || isMarked(matrix.getItem(i)) || !isDamageableComponent(matrix.getItem(i))) {
				count--;
			}
		}
		return count;
	}

	/**
	 * Determine, based on the item damage, how many parts are damaged.  We're already
	 * assuming that the item is loaded into the uncrafting matrix.
	 */
	private int countDamagedParts(ItemStack input) {
		int totalMax4 = Math.max(4, countDamageableParts(this.uncraftingMatrix));
		float damage = (float) input.getDamageValue() / (float) input.getMaxDamage();
		return (int) Math.ceil(totalMax4 * damage);
	}

	/**
	 * Called to transfer a stack from one inventory to the other e.g. when shift clicking.
	 */
	@Override
	public ItemStack quickMoveStack(Player player, int slotNum) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNum);
		//noinspection ConstantConditions
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (slotNum == 0) {
				if (!this.moveItemStackTo(itemstack1, 20, 56, false)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (slotNum == 1) {
				this.positionData.execute((p_39378_, p_39379_) -> itemstack1.getItem().onCraftedBy(itemstack1, p_39378_, player));
				if (!this.moveItemStackTo(itemstack1, 20, 56, true)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (slotNum >= 20 && slotNum < 56) {
				if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (slot.container == this.assemblyMatrix) {
				if (!this.moveItemStackTo(itemstack1, 20, 56, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (this.moveItemStackTo(itemstack1, 20, 56, false)) {
					slot.onTake(player, itemstack1);
					return ItemStack.EMPTY;
				}
			}
			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
			if (slotNum == 1) {
				player.drop(itemstack1, false);
			}
		}
		return itemstack;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.positionData.execute((world, pos) -> {
			this.clearContainer(player, this.assemblyMatrix);
			this.clearContainer(player, this.tinkerInput);
		});
	}

	private ItemStack[] getIngredients(Recipe<?> recipe) {
		ItemStack[] stacks = new ItemStack[recipe.getIngredients().size()];

		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			ItemStack[] matchingStacks = Arrays.stream(recipe.getIngredients().get(i).getItems()).filter(s -> !s.is(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS)).toArray(ItemStack[]::new);
			stacks[i] = matchingStacks.length > 0 ? matchingStacks[Math.floorMod(this.ingredientsInCycle, matchingStacks.length)] : ItemStack.EMPTY;
		}

		return stacks;
	}

	@Override
	public boolean stillValid(Player player) {
		return !TFConfig.disableEntireTable && stillValid(this.positionData, player, TFBlocks.UNCRAFTING_TABLE.get());
	}

	@Override
	public void fillCraftSlotsStackedContents(StackedContents stackedContents) {
		this.assemblyMatrix.fillStackedContents(stackedContents);
	}

	@Override
	public void clearCraftingContent() {
		this.tinkerInput.clearContent();
		this.assemblyMatrix.clearContent();
		this.tinkerResult.clearContent();
	}

	@Override
	public int getResultSlotIndex() {
		return 1; // tinkerResult slot
	}

	@Override
	public int getGridWidth() {
		return this.assemblyMatrix.getWidth();
	}

	@Override
	public int getGridHeight() {
		return this.assemblyMatrix.getHeight();
	}

	@Override
	public int getSize() {
		return 20;
	}

	@Override
	public RecipeBookType getRecipeBookType() {
		return RecipeBookType.CRAFTING;
	}

	@Override
	public boolean shouldMoveToInventory(int slot) {
		return slot == 0 || (11 <= slot && slot <= 19);
	}

	@Override
	public boolean recipeMatches(RecipeHolder<Recipe<RecipeInput>> recipeHolder) {
		return recipeHolder.value().matches(this.assemblyMatrix.asCraftInput(), this.player.level());
	}

	@SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
	@Override
	public void handlePlacement(boolean placeAll, RecipeHolder<?> recipe, ServerPlayer player) {
		new UncrafterPlaceRecipe<>(this).recipeClicked(player, (RecipeHolder<Recipe<RecipeInput>>) recipe, placeAll);
	}
}
