package twilightforest.inventory;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.*;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;
import twilightforest.TFConfig;
import twilightforest.block.TFBlocks;
import twilightforest.util.TFItemStackUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContainerTFUncrafting extends Container {

	private static final String TAG_MARKER = "TwilightForestMarker";

	// Inaccessible grid, for uncrafting logic
	private final InventoryTFGoblinUncrafting uncraftingMatrix = new InventoryTFGoblinUncrafting();
	// Accessible grid, for actual crafting
	public final CraftingInventory assemblyMatrix = new CraftingInventory(this, 3, 3);
	// Inaccessible grid, for recrafting logic
	private final CraftingInventory combineMatrix = new CraftingInventory(this, 3, 3);

	// Input slot for uncrafting
	public final IInventory tinkerInput = new InventoryTFGoblinInput(this);
	// Crafting Output
	private final CraftResultInventory tinkerResult = new CraftResultInventory();

	private final IWorldPosCallable positionData;
	private final World world;
	private final PlayerEntity player;

	// Conflict resolution
	public int unrecipeInCycle = 0;
	public int ingredientsInCycle = 0;
	public int recipeInCycle = 0;

	public static ContainerTFUncrafting fromNetwork(int id, PlayerInventory inventory) {
		return new ContainerTFUncrafting(id, inventory, inventory.player.world, IWorldPosCallable.DUMMY);
	}

	public ContainerTFUncrafting(int id, PlayerInventory inventory, World world, IWorldPosCallable positionData) {
		super(TFContainers.UNCRAFTING.get(), id);

		this.positionData = positionData;
		this.world = world;
		this.player = inventory.player;

		this.addSlot(new Slot(this.tinkerInput, 0, 13, 35));
		this.addSlot(new SlotTFGoblinCraftResult(inventory.player, this.tinkerInput, this.uncraftingMatrix, this.assemblyMatrix, this.tinkerResult, 0, 147, 35));

		int invX;
		int invY;

		for (invX = 0; invX < 3; ++invX) {
			for (invY = 0; invY < 3; ++invY) {
				this.addSlot(new SlotTFGoblinUncrafting(inventory.player, this.tinkerInput, this.uncraftingMatrix, this.assemblyMatrix, invY + invX * 3, 300000 + invY * 18, 17 + invX * 18));
			}
		}
		for (invX = 0; invX < 3; ++invX) {
			for (invY = 0; invY < 3; ++invY) {
				this.addSlot(new SlotTFGoblinAssembly(this.assemblyMatrix, invY + invX * 3, 62 + invY * 18, 17 + invX * 18));
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

		this.onCraftMatrixChanged(this.assemblyMatrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		// we need to see what inventory is calling this, and update appropriately
		if (inventory == this.tinkerInput) {

			// empty whole grid to start with
			this.uncraftingMatrix.clear();

			// see if there is a recipe for the input
			ItemStack inputStack = tinkerInput.getStackInSlot(0);
			ICraftingRecipe[] recipes = getRecipesFor(inputStack, world);

			int size = recipes.length;

			if (size > 0) {

				ICraftingRecipe recipe = recipes[Math.floorMod(this.unrecipeInCycle, size)];
				ItemStack[] recipeItems = getIngredients(recipe);

				if (recipe instanceof IShapedRecipe) {

					int recipeWidth  = ((IShapedRecipe) recipe).getRecipeWidth();
					int recipeHeight = ((IShapedRecipe) recipe).getRecipeHeight();

					// set uncrafting grid
					for (int invY = 0; invY < recipeHeight; invY++) {
						for (int invX = 0; invX < recipeWidth; invX++) {

							int index = invX + invY * recipeWidth;
							if (index >= recipeItems.length) continue;

							ItemStack ingredient = normalizeIngredient(recipeItems[index].copy());
							this.uncraftingMatrix.setInventorySlotContents(invX + invY * 3, ingredient);
						}
					}
				} else {
					for (int i = 0; i < this.uncraftingMatrix.getSizeInventory(); i++) {
						if (i < recipeItems.length) {
							ItemStack ingredient = normalizeIngredient(recipeItems[i].copy());
							this.uncraftingMatrix.setInventorySlotContents(i, ingredient);
						}
					}
				}

				// mark the appropriate number of damaged components
				if (inputStack.isDamaged()) {
					int damagedParts = countDamagedParts(inputStack);

					for (int i = 0; i < 9 && damagedParts > 0; i++) {
						ItemStack stack = this.uncraftingMatrix.getStackInSlot(i);
						if (isDamageableComponent(stack)) {
							markStack(stack);
							damagedParts--;
						}
					}
				}

				// mark banned items
				for (int i = 0; i < 9; i++) {
					ItemStack ingredient = this.uncraftingMatrix.getStackInSlot(i);
					if (isIngredientProblematic(ingredient)) {
						markStack(ingredient);
					}
				}

				// store number of items this recipe produces (and thus how many input items are required for uncrafting)
				this.uncraftingMatrix.numberOfInputItems = recipe.getRecipeOutput().getCount();
				this.uncraftingMatrix.uncraftingCost = calculateUncraftingCost();
				this.uncraftingMatrix.recraftingCost = 0;

			} else {
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
				this.tinkerResult.setInventorySlotContents(0, ItemStack.EMPTY);
				this.uncraftingMatrix.uncraftingCost = calculateUncraftingCost();
				this.uncraftingMatrix.recraftingCost = 0;
//    			}
			}
		}

		// repairing / recrafting: if there is an input item, and items in both grids, can we combine them to produce an output item that is the same type as the input item?
		if (inventory != this.combineMatrix && !this.uncraftingMatrix.isEmpty() && !this.assemblyMatrix.isEmpty()) {
			// combine the two matrices
			for (int i = 0; i < 9; i++) {

				ItemStack assembly = this.assemblyMatrix.getStackInSlot(i);
				ItemStack uncrafting = this.uncraftingMatrix.getStackInSlot(i);

				if (!assembly.isEmpty()) {
					this.combineMatrix.setInventorySlotContents(i, assembly);
				} else if (!uncrafting.isEmpty() && !isMarked(uncrafting)) {
					this.combineMatrix.setInventorySlotContents(i, uncrafting);
				} else {
					this.combineMatrix.setInventorySlotContents(i, ItemStack.EMPTY);
				}
			}
			// is there a result from this combined thing?
			chooseRecipe(this.combineMatrix);

			ItemStack input = this.tinkerInput.getStackInSlot(0);
			ItemStack result = this.tinkerResult.getStackInSlot(0);

			if (!result.isEmpty() && isValidMatchForInput(input, result)) {
				// copy the tag compound
				// or should we only copy enchantments?
				CompoundNBT inputTags = null;
				if (input.getTag() != null) {
					inputTags = input.getTag().copy();
				}

				// if the result has innate enchantments, add them on to our enchantment map
				Map<Enchantment, Integer> resultInnateEnchantments = EnchantmentHelper.getEnchantments(result);

				Map<Enchantment, Integer> inputEnchantments = EnchantmentHelper.getEnchantments(input);
				// check if the input enchantments can even go onto the result item
				inputEnchantments.keySet().removeIf(enchantment -> enchantment == null || !enchantment.canApply(result));

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
					if (EnchantmentHelper.getEnchantmentLevel(ench, result) < level) {
						result.addEnchantment(ench, level);
					}
				}

				this.tinkerResult.setInventorySlotContents(0, result);
				this.uncraftingMatrix.uncraftingCost = 0;
				this.uncraftingMatrix.recraftingCost = calculateRecraftingCost();

				// if there is a recrafting cost, increment the repair cost of the output
				if (this.uncraftingMatrix.recraftingCost > 0 && !result.hasDisplayName()) {
					result.setRepairCost(input.getRepairCost() + 2);
				}
			}
		}
	}

	public static void markStack(ItemStack stack) {
		stack.setTagInfo(TAG_MARKER, ByteNBT.valueOf((byte) 1));
	}

	public static boolean isMarked(ItemStack stack) {
		CompoundNBT stackTag = stack.getTag();
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

	private static ICraftingRecipe[] getRecipesFor(ItemStack inputStack, World world) {

		List<ICraftingRecipe> recipes = new ArrayList<>();

		if (!inputStack.isEmpty()) {
			for (IRecipe<?> recipe : world.getRecipeManager().getRecipes()) {
				if (recipe instanceof ICraftingRecipe && recipe.canFit(3, 3) && !recipe.getIngredients().isEmpty() && matches(inputStack, recipe.getRecipeOutput())) {
					recipes.add((ICraftingRecipe) recipe);
				}
			}
		}

		return recipes.toArray(new ICraftingRecipe[0]);
	}

	private static boolean matches(ItemStack input, ItemStack output) {
		return input.getItem() == output.getItem() && input.getCount() >= output.getCount();
	}

	private static ICraftingRecipe[] getRecipesFor(CraftingInventory matrix, World world) {
		return world.getRecipeManager().getRecipes(IRecipeType.CRAFTING, matrix, world).toArray(new ICraftingRecipe[0]);
	}

	private void chooseRecipe(CraftingInventory inventory) {

		ICraftingRecipe[] recipes = getRecipesFor(inventory, world);

		if (recipes.length == 0) {
			this.tinkerResult.setInventorySlotContents(0, ItemStack.EMPTY);
			return;
		}

		ICraftingRecipe recipe = recipes[Math.floorMod(this.recipeInCycle, recipes.length)];

		if (recipe != null && (recipe.isDynamic() || !this.world.getGameRules().getBoolean(GameRules.DO_LIMITED_CRAFTING) || ((ServerPlayerEntity) this.player).getRecipeBook().isUnlocked(recipe))) {
			this.tinkerResult.setRecipeUsed(recipe);
			this.tinkerResult.setInventorySlotContents(0, recipe.getCraftingResult(inventory));
		} else {
			this.tinkerResult.setInventorySlotContents(0, ItemStack.EMPTY);
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

		if (inputStack.getItem() instanceof ArmorItem && resultStack.getItem() instanceof ArmorItem) {
			ArmorItem inputArmor = (ArmorItem) inputStack.getItem();
			ArmorItem resultArmor = (ArmorItem) resultStack.getItem();

			return inputArmor.getEquipmentSlot() == resultArmor.getEquipmentSlot();
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
		return !this.assemblyMatrix.isEmpty() ? 0 : countDamageableParts(this.uncraftingMatrix);
	}

	/**
	 * Return the cost of recrafting, if any.  Return 0 if recrafting is not available at this time
	 */
	private int calculateRecraftingCost() {

		ItemStack input = tinkerInput.getStackInSlot(0);
		ItemStack output = tinkerResult.getStackInSlot(0);

		if (input.isEmpty() || !input.isEnchanted() || output.isEmpty()) {
			return 0;
		}

		// okay, if we're here the input item must be enchanted, and we are repairing or recrafting it
		int cost = 0;

		// add innate repair cost
		cost += input.getRepairCost();

		// look at the input's enchantments and total them up
		int enchantCost = countTotalEnchantmentCost(input);
		cost += enchantCost;

		// broken pieces cost
		int damagedCost = (1 + countDamagedParts(input)) * EnchantmentHelper.getEnchantments(output).size();
		cost += damagedCost;

		// factor in enchantibility difference
		int enchantabilityDifference = input.getItem().getItemEnchantability() - output.getItem().getItemEnchantability();
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
		switch (ench.getRarity().getWeight()) {
			case 1:
				return 8;
			case 2:
				return 4;
			case 3:
			case 4:
			case 5:
				return 2;
			case 6:
			case 7:
			case 8:
			case 9:
			default:
			case 10:
				return 1;
		}
	}

	@Override
	public ItemStack slotClick(int slotNum, int mouseButton, ClickType clickType, PlayerEntity player) {

		// if the player is trying to take an item out of the assembly grid, and the assembly grid is empty, take the item from the uncrafting grid.
		if (slotNum > 0 && this.inventorySlots.get(slotNum).inventory == this.assemblyMatrix
				&& player.inventory.getItemStack().isEmpty() && !this.inventorySlots.get(slotNum).getHasStack()) {

			// is the assembly matrix empty?
			if (this.assemblyMatrix.isEmpty()) {
				slotNum -= 9;
			}
		}

		// if the player is trying to take the result item and they don't have the XP to pay for it, reject them
		if (slotNum > 0 && this.inventorySlots.get(slotNum).inventory == this.tinkerResult
				&& calculateRecraftingCost() > player.experienceLevel && !player.abilities.isCreativeMode) {

			return ItemStack.EMPTY;
		}

		if (slotNum > 0 && this.inventorySlots.get(slotNum).inventory == this.uncraftingMatrix) {

			// similarly, reject uncrafting if they can't do that either
			if (calculateUncraftingCost() > player.experienceLevel && !player.abilities.isCreativeMode) {
				return ItemStack.EMPTY;
			}

			// don't allow uncrafting if the server option is turned off
			if (TFConfig.COMMON_CONFIG.disableUncrafting.get()) {
				return ItemStack.EMPTY;
			}

			// finally, don't give them damaged goods
			ItemStack stackInSlot = this.inventorySlots.get(slotNum).getStack();
			if (stackInSlot.isEmpty() || isMarked(stackInSlot)) {
				return ItemStack.EMPTY;
			}
		}

		// also we may need to detect here when the player is increasing the stack size of the input slot
		ItemStack ret = super.slotClick(slotNum, mouseButton, clickType, player);

		// just trigger this event whenever the input slot is clicked for any reason
		if (slotNum > 0 && this.inventorySlots.get(slotNum).inventory == this.tinkerInput) {
			this.onCraftMatrixChanged(this.tinkerInput);
		}

		return ret;
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
	private static int countDamageableParts(IInventory matrix) {
		int count = 0;
		for (int i = 0; i < matrix.getSizeInventory(); i++) {
			if (isDamageableComponent(matrix.getStackInSlot(i))) {
				count++;
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
		float damage = (float) input.getDamage() / (float) input.getMaxDamage();
		return (int) Math.ceil(totalMax4 * damage);
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift clicking.
	 */
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotNum) {

		Slot transferSlot = this.inventorySlots.get(slotNum);

		if (transferSlot == null || !transferSlot.getHasStack()) {
			return ItemStack.EMPTY;
		}

		ItemStack transferStack = transferSlot.getStack();
		ItemStack copyItem = transferStack.copy();

		if (slotNum == 0) {
			// result or input goes to inventory or hotbar
			if (!this.mergeItemStack(transferStack, 20, 56, true)) {
				return ItemStack.EMPTY;
			}

			transferSlot.onSlotChange(transferStack, copyItem);  // what does this do?
		} else if (slotNum == 1) {
			transferStack.getItem().onCreated(transferStack, this.world, player);

			if (!this.mergeItemStack(transferStack, 20, 56, true))
				return ItemStack.EMPTY;

			transferSlot.onSlotChange(transferStack, copyItem);
		} else if (slotNum >= 20 && slotNum < 47) {
			// Checks uncrafting input slot first
			if (!this.mergeItemStack(transferStack, 0, 1, false)) {
				// inventory goes to hotbar
				if (!this.mergeItemStack(transferStack, 47, 56, false)) {
					return ItemStack.EMPTY;
				}
			}
		} else if (slotNum >= 47 && slotNum < 56) {
			// Checks uncrafting input slot first
			if (!this.mergeItemStack(transferStack, 0, 1, false)) {
				// hotbar goes to inventory
				if (!this.mergeItemStack(transferStack, 20, 47, false)) {
					return ItemStack.EMPTY;
				}
			}
		} else if (!this.mergeItemStack(transferStack, 20, 56, false)) {
			// crafting area goes to inventory or hotbar
			return ItemStack.EMPTY;
		}

		if (transferStack.getCount() == 0) {
			transferSlot.putStack(ItemStack.EMPTY);
		} else {
			transferSlot.onSlotChanged();
		}

		if (transferStack.getCount() == copyItem.getCount()) {
			return ItemStack.EMPTY;
		}

		return transferSlot.onTake(player, transferStack);
	}

	@Override
	public void onContainerClosed(PlayerEntity player) {
		super.onContainerClosed(player);
		this.positionData.consume((world, pos) -> {
			clearContainer(player, world, assemblyMatrix);
			clearContainer(player, world, tinkerInput);
		});
	}

	private ItemStack[] getIngredients(ICraftingRecipe recipe) {
		ItemStack[] stacks = new ItemStack[recipe.getIngredients().size()];

		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			ItemStack[] matchingStacks = recipe.getIngredients().get(i).getMatchingStacks();
			stacks[i] = matchingStacks.length > 0 ? matchingStacks[Math.floorMod(this.ingredientsInCycle, matchingStacks.length)] : ItemStack.EMPTY;
		}

		return stacks;
	}

	@Override
	public boolean canInteractWith(PlayerEntity player) {
		return isWithinUsableDistance(positionData, player, TFBlocks.uncrafting_table.get());
	}
}
