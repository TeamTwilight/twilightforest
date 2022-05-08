package twilightforest.inventory;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.IShapedRecipe;
import twilightforest.TFConfig;
import twilightforest.block.TFBlocks;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.inventory.slot.AssemblySlot;
import twilightforest.inventory.slot.UncraftingResultSlot;
import twilightforest.inventory.slot.UncraftingSlot;
import twilightforest.item.recipe.TFRecipes;
import twilightforest.item.recipe.UncraftingRecipe;
import twilightforest.util.TFItemStackUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UncraftingContainer extends AbstractContainerMenu {

	private static final String TAG_MARKER = "TwilightForestMarker";

	// Inaccessible grid, for uncrafting logic
	private final UncraftingInventory uncraftingMatrix = new UncraftingInventory();
	// Accessible grid, for actual crafting
	public final CraftingContainer assemblyMatrix = new CraftingContainer(this, 3, 3);
	// Inaccessible grid, for recrafting logic
	private final CraftingContainer combineMatrix = new CraftingContainer(this, 3, 3);

	// Input slot for uncrafting
	public final Container tinkerInput = new UncraftingInputInventory(this);
	// Crafting Output
	private final ResultContainer tinkerResult = new ResultContainer();

	private final ContainerLevelAccess positionData;
	private final Level world;
	private final Player player;

	// Conflict resolution
	public int unrecipeInCycle = 0;
	public int ingredientsInCycle = 0;
	public int recipeInCycle = 0;

	// Need to store potential custom cost. If set to -1, will calculate uncrafting cost normally.
	private static int customCost;

	public static UncraftingContainer fromNetwork(int id, Inventory inventory) {
		return new UncraftingContainer(id, inventory, inventory.player.level, ContainerLevelAccess.NULL);
	}

	public UncraftingContainer(int id, Inventory inventory, Level world, ContainerLevelAccess positionData) {
		super(TFContainers.UNCRAFTING.get(), id);

		this.positionData = positionData;
		this.world = world;
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
	}

	@Override
	public void slotsChanged(Container inventory) {
		// we need to see what inventory is calling this, and update appropriately
		if (inventory == this.tinkerInput) {

			// empty whole grid to start with
			this.uncraftingMatrix.clearContent();

			// see if there is a recipe for the input
			ItemStack inputStack = tinkerInput.getItem(0);
			CraftingRecipe[] recipes = getRecipesFor(inputStack, world);

			int size = recipes.length;

			if (size > 0 && !inputStack.is(ItemTagGenerator.BANNED_UNCRAFTABLES)) {

				CraftingRecipe recipe = recipes[Math.floorMod(this.unrecipeInCycle, size)];
				customCost = recipe instanceof UncraftingRecipe ? ((UncraftingRecipe) recipe).getCost() : -1;
				ItemStack[] recipeItems = getIngredients(recipe);

				if (recipe instanceof IShapedRecipe<?> rec) {

					int recipeWidth = rec.getRecipeWidth();
					int recipeHeight = rec.getRecipeHeight();

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
					int damagedParts = countDamagedParts(inputStack);

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
				this.uncraftingMatrix.numberOfInputItems = recipe.getResultItem().getCount();
				if (recipe instanceof UncraftingRecipe) this.uncraftingMatrix.numberOfInputItems = ((UncraftingRecipe)recipe).getCount();//Uncrafting recipes need this method call
				this.uncraftingMatrix.uncraftingCost = calculateUncraftingCost();
				this.uncraftingMatrix.recraftingCost = 0;

			} else {
				customCost = -1;
				this.uncraftingMatrix.numberOfInputItems = 0;
				this.uncraftingMatrix.uncraftingCost = 0;
			}
		}
		// Now we've got the uncrafting logic set in, currently we don't modify the uncraftingMatrix. That's fine.
		if (inventory == this.assemblyMatrix || inventory == this.tinkerInput) {
			if (this.tinkerInput.isEmpty()) {
				// display the output
				chooseRecipe(this.assemblyMatrix);
				this.uncraftingMatrix.recraftingCost = 0;
			} else {
//    			if (isMatrixEmpty(this.assemblyMatrix)) {
//    				// we just emptied the assembly matrix and need to re-prepare for uncrafting
//	    			this.tinkerResult.setInventorySlotContents(0, ItemStack.EMPTY);
//        			this.uncraftingMatrix.uncraftingCost = calculateUncraftingCost();
//        			this.uncraftingMatrix.recraftingCost = 0;
//    			}
//    			else {
				// we placed an item in the assembly matrix, the next step will re-initialize these with correct values
				this.tinkerResult.setItem(0, ItemStack.EMPTY);
				this.uncraftingMatrix.uncraftingCost = calculateUncraftingCost();
				this.uncraftingMatrix.recraftingCost = 0;
//    			}
			}
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
			chooseRecipe(this.combineMatrix);

			ItemStack input = this.tinkerInput.getItem(0);
			ItemStack result = this.tinkerResult.getItem(0);

			if (!result.isEmpty() && isValidMatchForInput(input, result)) {
				// copy the tag compound
				// or should we only copy enchantments?
				CompoundTag inputTags = null;
				if (input.getTag() != null) {
					inputTags = input.getTag().copy();
				}

				// if the result has innate enchantments, add them on to our enchantment map
				Map<Enchantment, Integer> resultInnateEnchantments = EnchantmentHelper.getEnchantments(result);

				Map<Enchantment, Integer> inputEnchantments = EnchantmentHelper.getEnchantments(input);
				// check if the input enchantments can even go onto the result item
				inputEnchantments.keySet().removeIf(enchantment -> enchantment == null || !enchantment.canEnchant(result));

				if (inputTags != null) {
					// remove enchantments, copy tags, re-add filtered enchantments
					inputTags.remove("ench");
					result.setTag(inputTags);
					EnchantmentHelper.setEnchantments(inputEnchantments, result);
				}

				// finally, add any innate enchantments back onto the result
				for (Map.Entry<Enchantment, Integer> entry : resultInnateEnchantments.entrySet()) {

					Enchantment ench = entry.getKey();
					int level = entry.getValue();

					// only apply enchants that are better than what we already have
					if (EnchantmentHelper.getItemEnchantmentLevel(ench, result) < level) {
						result.enchant(ench, level);
					}
				}

				this.tinkerResult.setItem(0, result);
				this.uncraftingMatrix.uncraftingCost = 0;
				this.uncraftingMatrix.recraftingCost = calculateRecraftingCost();

				// if there is a recrafting cost, increment the repair cost of the output
				if (this.uncraftingMatrix.recraftingCost > 0 && !result.hasCustomHoverName()) {
					result.setRepairCost(input.getBaseRepairCost() + 2);
				}
			}
		}
	}

	public static void markStack(ItemStack stack) {
		stack.addTagElement(TAG_MARKER, ByteTag.valueOf((byte) 1));
	}

	public static boolean isMarked(ItemStack stack) {
		CompoundTag stackTag = stack.getTag();
		return stackTag != null && stackTag.getBoolean(TAG_MARKER);
	}

	public static void unmarkStack(ItemStack stack) {
		TFItemStackUtils.clearInfoTag(stack, TAG_MARKER);
	}

	private static boolean isIngredientProblematic(ItemStack ingredient) {
		return !ingredient.isEmpty() && ingredient.getItem().hasContainerItem(ingredient);
	}

	private static ItemStack normalizeIngredient(ItemStack ingredient) {
		if (ingredient.getCount() > 1) {
			ingredient.setCount(1);
		}
		return ingredient;
	}

	private static CraftingRecipe[] getRecipesFor(ItemStack inputStack, Level world) {

		List<CraftingRecipe> recipes = new ArrayList<>();

		if (!inputStack.isEmpty()) {
			for (Recipe<?> recipe : world.getRecipeManager().getRecipes()) {
				if (recipe instanceof CraftingRecipe rec &&
						recipe.canCraftInDimensions(3, 3) &&
						!recipe.getIngredients().isEmpty() &&
						matches(inputStack, recipe.getResultItem()) &&
						!TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingRecipes.get().contains(recipe.getId().toString())) {
					if(TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.flipUncraftingModIdList.get() == TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.get().contains(recipe.getId().getNamespace())) {
						recipes.add(rec);
					}
				}
			}
			for (UncraftingRecipe uncraftingRecipe : world.getRecipeManager().getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE.get())) {
				if (uncraftingRecipe.isItemStackAnIngredient(inputStack)) recipes.add(uncraftingRecipe);
			}
		}

		return recipes.toArray(new CraftingRecipe[0]);
	}

	private static boolean matches(ItemStack input, ItemStack output) {
		return input.getItem() == output.getItem() && input.getCount() >= output.getCount();
	}

	private static CraftingRecipe[] getRecipesFor(CraftingContainer matrix, Level world) {
		return world.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, matrix, world).toArray(new CraftingRecipe[0]);
	}

	private void chooseRecipe(CraftingContainer inventory) {

		CraftingRecipe[] recipes = getRecipesFor(inventory, world);

		if (recipes.length == 0) {
			this.tinkerResult.setItem(0, ItemStack.EMPTY);
			return;
		}

		CraftingRecipe recipe = recipes[Math.floorMod(this.recipeInCycle, recipes.length)];

		if (recipe != null && (recipe.isSpecial() || !this.world.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) || ((ServerPlayer) this.player).getRecipeBook().contains(recipe))) {
			this.tinkerResult.setRecipeUsed(recipe);
			this.tinkerResult.setItem(0, recipe.assemble(inventory));
		} else {
			this.tinkerResult.setItem(0, ItemStack.EMPTY);
		}
	}

	/**
	 * Checks if the result is a valid match for the input.  Currently only accepts armor or tools that are the same type as the input
	 */
	// TODO Should we also check the slot the armors can go into, in case they don't extend armor class..?
	private static boolean isValidMatchForInput(ItemStack inputStack, ItemStack resultStack) {
		if (inputStack.getItem() instanceof PickaxeItem && resultStack.getItem() instanceof PickaxeItem) {
			return true;
		}
		if (inputStack.getItem() instanceof AxeItem && resultStack.getItem() instanceof AxeItem) {
			return true;
		}
		if (inputStack.getItem() instanceof ShovelItem && resultStack.getItem() instanceof ShovelItem) {
			return true;
		}
		if (inputStack.getItem() instanceof HoeItem && resultStack.getItem() instanceof HoeItem) {
			return true;
		}
		if (inputStack.getItem() instanceof SwordItem && resultStack.getItem() instanceof SwordItem) {
			return true;
		}
		if (inputStack.getItem() instanceof BowItem && resultStack.getItem() instanceof BowItem) {
			return true;
		}

		if (inputStack.getItem() instanceof ArmorItem inputArmor && resultStack.getItem() instanceof ArmorItem resultArmor) {

			return inputArmor.getSlot() == resultArmor.getSlot();
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
	 * Calculate the cost of uncrafting, if any.  Return 0 if uncrafting is not available at this time
	 */
	private int calculateUncraftingCost() {
		// we don't want to display anything if there is anything in the assembly grid
		if (this.assemblyMatrix.isEmpty()) {
			return customCost >= 0 ? customCost : countDamageableParts(this.uncraftingMatrix);
		} else return 0;
	}

	/**
	 * Return the cost of recrafting, if any.  Return 0 if recrafting is not available at this time
	 */
	private int calculateRecraftingCost() {

		ItemStack input = tinkerInput.getItem(0);
		ItemStack output = tinkerResult.getItem(0);

		if (input.isEmpty() || !input.isEnchanted() || output.isEmpty()) {
			return 0;
		}

		// okay, if we're here the input item must be enchanted, and we are repairing or recrafting it
		int cost = 0;

		// add innate repair cost
		cost += input.getBaseRepairCost();

		// look at the input's enchantments and total them up
		int enchantCost = countTotalEnchantmentCost(input);
		cost += enchantCost;

		// broken pieces cost
		int damagedCost = (1 + countDamagedParts(input)) * EnchantmentHelper.getEnchantments(output).size();
		cost += damagedCost;

		// factor in enchantibility difference
		int enchantabilityDifference = input.getItem().getEnchantmentValue() - output.getItem().getEnchantmentValue();
		cost += enchantabilityDifference;

		// minimum cost of 1 if we're even calling this part
		cost = Math.max(1, cost);

		return cost;
	}

	private static int countTotalEnchantmentCost(ItemStack stack) {

		int count = 0;

		for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet()) {

			Enchantment ench = entry.getKey();
			int level = entry.getValue();

			if (ench != null && level > 0) {
				count += getWeightModifier(ench) * level;
				count += 1;
			}
		}

		return count;
	}

	private static int getWeightModifier(Enchantment ench) {
		return switch (ench.getRarity().getWeight()) {
			case 1 -> 8;
			case 2 -> 4;
			case 3, 4, 5 -> 2;
			default -> 1;
		};
	}

	@Override
	public void clicked(int slotNum, int mouseButton, ClickType clickType, Player player) {

		// if the player is trying to take an item out of the assembly grid, and the assembly grid is empty, take the item from the uncrafting grid.
		if (slotNum > 0 && this.slots.get(slotNum).container == this.assemblyMatrix
				&& player.containerMenu.getCarried().isEmpty() && !this.slots.get(slotNum).hasItem()) {

			// is the assembly matrix empty?
			if (this.assemblyMatrix.isEmpty()) {
				slotNum -= 9;
			}
		}

		// if the player is trying to take the result item and they don't have the XP to pay for it, reject them
		if (slotNum > 0 && this.slots.get(slotNum).container == this.tinkerResult
				&& calculateRecraftingCost() > player.experienceLevel && !player.getAbilities().instabuild) {

			return;
		}

		if (slotNum > 0 && this.slots.get(slotNum).container == this.uncraftingMatrix) {

			// similarly, reject uncrafting if they can't do that either
			if (calculateUncraftingCost() > player.experienceLevel && !player.getAbilities().instabuild) {
				return;
			}

			// don't allow uncrafting if the server option is turned off
			if (TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncrafting.get()) {
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
		if (slotNum > 0 && this.slots.get(slotNum).container == this.tinkerInput) {
			this.slotsChanged(this.tinkerInput);
		}
	}

	/**
	 * Should the specified item count for taking damage?
	 */
	private static boolean isDamageableComponent(ItemStack itemStack) {
		return !itemStack.isEmpty() && itemStack.getItem() != Items.STICK;
	}

	/**
	 * Count how many items in an inventory can take damage
	 */
	private static int countDamageableParts(Container matrix) {
		int count = 0;
		for (int i = 0; i < matrix.getContainerSize(); i++) {
			if (!matrix.getItem(i).isEmpty()) {
				count++;
			}
			if(isIngredientProblematic(matrix.getItem(i)) || isMarked(matrix.getItem(i))) {
				count--;
			}
		}
		return count;
	}

	/**
	 * Determine, based on the item damage, how many parts are damaged.  We're already
	 * assuming that the item is loaded into the uncrafing matrix.
	 */
	private int countDamagedParts(ItemStack input) {
		int totalMax4 = Math.max(4, countDamageableParts(this.uncraftingMatrix));
		float damage = (float) input.getDamageValue() / (float) input.getMaxDamage();
		return (int) Math.ceil(totalMax4 * damage);
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift clicking.
	 */
	@Override
	public ItemStack quickMoveStack(Player player, int slotNum) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNum);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (slotNum == 0) {
				if (!this.moveItemStackTo(itemstack1, 20, 56, false)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(itemstack1, itemstack);
			}  else if (slotNum == 1) {
				this.positionData.execute((p_39378_, p_39379_) -> {
					itemstack1.getItem().onCraftedBy(itemstack1, p_39378_, player);
				});
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
			clearContainer(player, assemblyMatrix);
			clearContainer(player, tinkerInput);
		});
	}

	private ItemStack[] getIngredients(CraftingRecipe recipe) {
		ItemStack[] stacks = new ItemStack[recipe.getIngredients().size()];

		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			ItemStack[] matchingStacks = Arrays.stream(recipe.getIngredients().get(i).getItems()).filter(s -> !s.is(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS)).toArray(ItemStack[]::new);
			stacks[i] = matchingStacks.length > 0 ? matchingStacks[Math.floorMod(this.ingredientsInCycle, matchingStacks.length)] : ItemStack.EMPTY;
		}

		return stacks;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(positionData, player, TFBlocks.UNCRAFTING_TABLE.get());
	}
}
