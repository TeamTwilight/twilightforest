package twilightforest.inventory;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import twilightforest.TFConfig;

import java.util.Map;

public class ContainerTFUncrafting extends Container {
	private InventoryTFGoblinUncrafting uncraftingMatrix = new InventoryTFGoblinUncrafting(this);
	private InventoryCrafting assemblyMatrix = new InventoryCrafting(this, 3, 3);
	private InventoryCrafting combineMatrix = new InventoryCrafting(this, 3, 3);
	private IInventory tinkerInput = new InventoryTFGoblinInput(this);
	private IInventory tinkerResult = new InventoryCraftResult();
	private World world;

	public ContainerTFUncrafting(InventoryPlayer inventory, World world, int x, int y, int z) {
		this.world = world;
		this.addSlotToContainer(new Slot(this.tinkerInput, 0, 13, 35));
		this.addSlotToContainer(new SlotTFGoblinCraftResult(inventory.player, this.tinkerInput, this.uncraftingMatrix, this.assemblyMatrix, this.tinkerResult, 0, 147, 35));

		int invX;
		int invY;

		for (invX = 0; invX < 3; ++invX) {
			for (invY = 0; invY < 3; ++invY) {
				this.addSlotToContainer(new SlotTFGoblinUncrafting(inventory.player, this.tinkerInput, this.uncraftingMatrix, this.assemblyMatrix, invY + invX * 3, 300000 + invY * 18, 17 + invX * 18));
			}
		}
		for (invX = 0; invX < 3; ++invX) {
			for (invY = 0; invY < 3; ++invY) {
				this.addSlotToContainer(new SlotTFGoblinAssembly(inventory.player, this.tinkerInput, this.assemblyMatrix, this.uncraftingMatrix, invY + invX * 3, 62 + invY * 18, 17 + invX * 18));
			}
		}

		for (invX = 0; invX < 3; ++invX) {
			for (invY = 0; invY < 9; ++invY) {
				this.addSlotToContainer(new Slot(inventory, invY + invX * 9 + 9, 8 + invY * 18, 84 + invX * 18));
			}
		}

		for (invX = 0; invX < 9; ++invX) {
			this.addSlotToContainer(new Slot(inventory, invX, 8 + invX * 18, 142));
		}

		this.onCraftMatrixChanged(this.assemblyMatrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory) {
		// we need to see what inventory is calling this, and update appropriately
		if (par1IInventory == this.tinkerInput) {
			// see if there is a recipe for the input
			ItemStack inputStack = tinkerInput.getStackInSlot(0);
			IRecipe recipe = getRecipeFor(inputStack);

			if (recipe != null) {
				int recipeWidth = getRecipeWidth(recipe);
				int recipeHeight = getRecipeHeight(recipe);
				ItemStack[] recipeItems = getIngredients(recipe);

				// empty whole grid to start with
				// let's not get leftovers if something changes like the recipe size
				for (int i = 0; i < this.uncraftingMatrix.getSizeInventory(); i++) {
					this.uncraftingMatrix.setInventorySlotContents(i, ItemStack.EMPTY);
				}

				// set uncrafting grid
				for (int invY = 0; invY < recipeHeight; invY++) {
					for (int invX = 0; invX < recipeWidth; invX++) {
						ItemStack ingredient = recipeItems[invX + invY * recipeWidth].copy();
						// fix weird recipe for diamond/ingot blocks
						if (!ingredient.isEmpty() && ingredient.getCount() > 1) {
							ingredient.setCount(1);
						}
						if (!ingredient.isEmpty() && ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
							ingredient.setItemDamage(0);
						}
						this.uncraftingMatrix.setInventorySlotContents(invX + invY * 3, ingredient);
					}
				}

				// mark the appropriate number of components damaged
				if (inputStack.isItemDamaged()) {
					int damagedParts = countDamagedParts(inputStack);

					for (int i = 0; i < 9 && damagedParts > 0; i++) {
						if (isDamageableComponent(this.uncraftingMatrix.getStackInSlot(i))) {
							// todo 1.11 this won't work anymore
							this.uncraftingMatrix.getStackInSlot(i).setCount(0);
							damagedParts--;
						}
					}
				}

				// mark banned items damaged
				for (int i = 0; i < 9; i++) {
					ItemStack ingredient = this.uncraftingMatrix.getStackInSlot(i);

					if (isIngredientProblematic(ingredient)) {
						// todo 1.11 this won't work anymore
						ingredient.setCount(0);
					}
				}

				// store number of items this recipe produces (and thus how many input items are required for uncrafting)
				this.uncraftingMatrix.numberOfInputItems = recipe.getRecipeOutput().getCount();
				this.uncraftingMatrix.uncraftingCost = calculateUncraftingCost();
				this.uncraftingMatrix.recraftingCost = 0;
			} else {
				for (int i = 0; i < 9; i++) {
					this.uncraftingMatrix.setInventorySlotContents(i, ItemStack.EMPTY);
				}
				this.uncraftingMatrix.numberOfInputItems = 0;
				this.uncraftingMatrix.uncraftingCost = 0;
			}
		}

		if (par1IInventory == this.assemblyMatrix || par1IInventory == this.tinkerInput) {
			if (this.tinkerInput.isEmpty()) {
				// display the output
				this.tinkerResult.setInventorySlotContents(0, CraftingManager.findMatchingResult(this.assemblyMatrix, this.world));
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
		if (par1IInventory != this.combineMatrix && !this.uncraftingMatrix.isEmpty() && !this.assemblyMatrix.isEmpty()) {
			// combine the two matrixen
			for (int i = 0; i < 9; i++) {
				if (!this.assemblyMatrix.getStackInSlot(i).isEmpty()) {
					this.combineMatrix.setInventorySlotContents(i, this.assemblyMatrix.getStackInSlot(i));
				} else if (!this.uncraftingMatrix.getStackInSlot(i).isEmpty() && this.uncraftingMatrix.getStackInSlot(i).getCount() > 0) {
					this.combineMatrix.setInventorySlotContents(i, this.uncraftingMatrix.getStackInSlot(i));
				} else {
					this.combineMatrix.setInventorySlotContents(i, ItemStack.EMPTY);
				}
			}
			// is there a result from this combined thing?
			ItemStack result = CraftingManager.findMatchingResult(this.combineMatrix, this.world);
			ItemStack input = this.tinkerInput.getStackInSlot(0);

			if (!result.isEmpty() && isValidMatchForInput(input, result)) {
				// copy the tag compound
				// or should we only copy enchantments?
				NBTTagCompound inputTags = null;
				if (input.getTagCompound() != null) {
					inputTags = input.getTagCompound().copy();
				}

				// if the result has innate enchantments, add them on to our enchantment map
				Map<Enchantment, Integer> resultInnateEnchantments = EnchantmentHelper.getEnchantments(result);

				// check if the input enchantments can even go onto the result item
				Map<Enchantment, Integer> inputEnchantments = EnchantmentHelper.getEnchantments(input);
				inputEnchantments.keySet().removeIf(enchantment -> !enchantment.canApply(result));

				if (inputTags != null) {
					// remove enchantments, copy tags, re-add filtered enchantments
					inputTags.removeTag("ench");
					result.setTagCompound(inputTags);
					EnchantmentHelper.setEnchantments(inputEnchantments, result);
				}

				// finally, add any innate enchantments back onto the result
				for (Enchantment ench : resultInnateEnchantments.keySet()) {
					int level = resultInnateEnchantments.get(ench);

					if (EnchantmentHelper.getEnchantmentLevel(ench, result) > level) {
						level = EnchantmentHelper.getEnchantmentLevel(ench, result);
					}

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

	private boolean isIngredientProblematic(ItemStack ingredient) {
		return !ingredient.isEmpty() && (ingredient.getItem().hasContainerItem(ingredient) || ingredient.getUnlocalizedName().contains("itemMatter"));
	}

	/**
	 * Get the first valid shaped recipe for the item in the input
	 */
	private IRecipe getRecipeFor(ItemStack inputStack) {
		if (!inputStack.isEmpty()) {
			for (IRecipe recipe : CraftingManager.REGISTRY) {
				if ((recipe instanceof ShapedRecipes || recipe instanceof ShapedOreRecipe)
						&& recipe.getRecipeOutput().getItem() == inputStack.getItem() && inputStack.getCount() >= recipe.getRecipeOutput().getCount()
						&& (!recipe.getRecipeOutput().getHasSubtypes() || recipe.getRecipeOutput().getItemDamage() == inputStack.getItemDamage())) {
					return recipe;
				}
			}
		}
		return null;
	}

	/**
	 * Checks if the result is a valid match for the input.  Currently only accepts armor or tools that are the same type as the input
	 */
	private boolean isValidMatchForInput(ItemStack inputStack, ItemStack resultStack) {
		if (inputStack.getItem() instanceof ItemPickaxe && resultStack.getItem() instanceof ItemPickaxe) {
			return true;
		}
		if (inputStack.getItem() instanceof ItemAxe && resultStack.getItem() instanceof ItemAxe) {
			return true;
		}
		if (inputStack.getItem() instanceof ItemSpade && resultStack.getItem() instanceof ItemSpade) {
			return true;
		}
		if (inputStack.getItem() instanceof ItemHoe && resultStack.getItem() instanceof ItemHoe) {
			return true;
		}
		if (inputStack.getItem() instanceof ItemSword && resultStack.getItem() instanceof ItemSword) {
			return true;
		}
		if (inputStack.getItem() instanceof ItemBow && resultStack.getItem() instanceof ItemBow) {
			return true;
		}

		if (inputStack.getItem() instanceof ItemArmor && resultStack.getItem() instanceof ItemArmor) {
			ItemArmor inputArmor = (ItemArmor) inputStack.getItem();
			ItemArmor resultArmor = (ItemArmor) resultStack.getItem();

			return inputArmor.armorType == resultArmor.armorType;
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
		if (!this.assemblyMatrix.isEmpty()) {
			return 0;
		} else {
			return countDamageableParts(this.uncraftingMatrix);
		}
	}

	/**
	 * Return the cost of recrafting, if any.  Return 0 if recrafting is not available at this time
	 */
	private int calculateRecraftingCost() {
		if (tinkerInput.getStackInSlot(0).isEmpty() || !tinkerInput.getStackInSlot(0).isItemEnchanted() || tinkerResult.getStackInSlot(0).isEmpty()) {
			return 0;
		} else {
			// okay, if we're here the input item must be enchanted, and we are repairing or recrafting it
			ItemStack input = tinkerInput.getStackInSlot(0);
			ItemStack output = tinkerResult.getStackInSlot(0);

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
	}

	public int countHighestEnchantmentCost(ItemStack itemStack) {
		int count = 0;

		for (Enchantment ench : ForgeRegistries.ENCHANTMENTS) {
			int level = EnchantmentHelper.getEnchantmentLevel(ench, itemStack);
			if (level > count) {
				//count = ench.getMinEnchantability(level); OLD
				count += getWeightModifier(ench) * level;
			}
		}

		return count;
	}

	private int countTotalEnchantmentCost(ItemStack itemStack) {
		int count = 0;

		for (Enchantment ench : ForgeRegistries.ENCHANTMENTS) {
			int level = EnchantmentHelper.getEnchantmentLevel(ench, itemStack);
			if (level > 0) {
				count += getWeightModifier(ench) * level;
				count += 1;
			}
		}

		return count;
	}

	private int getWeightModifier(Enchantment ench) {
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
	public ItemStack slotClick(int slotNum, int mouseButton, ClickType shiftHeld, EntityPlayer par4EntityPlayer) {

		// if the player is trying to take an item out of the assembly grid, and the assembly grid is empty, take the item from the uncrafting grid.
		if (slotNum > 0 && par4EntityPlayer.inventory.getItemStack().isEmpty() && this.inventorySlots.get(slotNum).inventory == this.assemblyMatrix && !this.inventorySlots.get(slotNum).getHasStack()) {
			// is the assembly matrix empty?
			if (this.assemblyMatrix.isEmpty()) {
				slotNum -= 9;
			}
		}

		// if the player is trying to take the result item and they don't have the XP to pay for it, reject them
		if (slotNum > 0 && this.inventorySlots.get(slotNum).inventory == this.tinkerResult
				&& calculateRecraftingCost() > par4EntityPlayer.experienceLevel && !par4EntityPlayer.capabilities.isCreativeMode) {
			return ItemStack.EMPTY;
		}

		// similarly, reject uncrafting if they can't do that either
		if (slotNum > 0 && this.inventorySlots.get(slotNum).inventory == this.uncraftingMatrix
				&& calculateUncraftingCost() > par4EntityPlayer.experienceLevel && !par4EntityPlayer.capabilities.isCreativeMode) {
			return ItemStack.EMPTY;
		}

		// don't allow uncrafting if the server option is turned off
		if (slotNum > 0 && this.inventorySlots.get(slotNum).inventory == this.uncraftingMatrix && TFConfig.disableUncrafting) {
			return ItemStack.EMPTY;
		}

		// finally, don't give them damaged goods
		if (slotNum > 0 && this.inventorySlots.get(slotNum).inventory == this.uncraftingMatrix && this.inventorySlots.get(slotNum).getStack().isEmpty()) {
			return ItemStack.EMPTY;
		}

		// also we may need to detect here when the player is increasing the stack size of the input slot
		ItemStack ret = super.slotClick(slotNum, mouseButton, shiftHeld, par4EntityPlayer);

		// just trigger this event whenever the input slot is clicked for any reason
		if (slotNum > 0 && this.inventorySlots.get(slotNum).inventory instanceof InventoryTFGoblinInput) {
			this.onCraftMatrixChanged(this.tinkerInput);
		}

		return ret;
	}

	// todo 1.12 evaluate if this logic needs to be moved elsewhere (method removed in 1.12)
	protected void retrySlotClick(int slotNum, int mouseButton, boolean par3, EntityPlayer par4EntityPlayer) {
		// if they are taking something out of the uncrafting matrix, bump the slot number back to the assembly matrix
		// otherwise we lose the stuff in the uncrafting matrix when we shift-click to take multiple things
		if (this.inventorySlots.get(slotNum).inventory == this.uncraftingMatrix) {
			slotNum += 9;
		}

		this.slotClick(slotNum, mouseButton, ClickType.QUICK_MOVE, par4EntityPlayer);
	}

	/**
	 * Should the specified item count for taking damage?
	 */
	private boolean isDamageableComponent(ItemStack itemStack) {
		return !itemStack.isEmpty() && itemStack.getItem() != Items.STICK;
	}

	/**
	 * Count how many items in an inventory can take damage
	 */
	private int countDamageableParts(IInventory matrix) {
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
		float damage = (float) input.getItemDamage() / (float) input.getMaxDamage();
		int damagedParts = (int) Math.ceil(totalMax4 * damage);
		return damagedParts;
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift clicking.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
		ItemStack copyItem = ItemStack.EMPTY;
		Slot transferSlot = this.inventorySlots.get(slotNum);

		if (transferSlot != null && transferSlot.getHasStack()) {
			ItemStack transferStack = transferSlot.getStack();
			copyItem = transferStack.copy();

			if (slotNum == 0 || slotNum == 1) {
				// result or input goes to inventory or hotbar
				if (!this.mergeItemStack(transferStack, 20, 56, true)) {
					return ItemStack.EMPTY;
				}

				transferSlot.onSlotChange(transferStack, copyItem);  // what does this do?
			} else if (slotNum >= 20 && slotNum < 47) {
				// inventory goes to hotbar
				if (!this.mergeItemStack(transferStack, 47, 56, false)) {
					return ItemStack.EMPTY;
				}
			} else if (slotNum >= 47 && slotNum < 56) {
				// hotbar goes to inventory
				if (!this.mergeItemStack(transferStack, 20, 47, false)) {
					return ItemStack.EMPTY;
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

		return copyItem;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);

		if (!player.world.isRemote) {
			clearContainer(player, world, assemblyMatrix);
			clearContainer(player, world, tinkerInput);
		}
	}

	private ItemStack[] getIngredients(IRecipe recipe) {
		// todo 1.12 recheck
		if (recipe instanceof ShapedRecipes || recipe instanceof ShapedOreRecipe) {
			ItemStack[] stacks = new ItemStack[recipe.getIngredients().size()];

			for (int i = 0; i < recipe.getIngredients().size(); i++) {
				ItemStack[] matchingStacks = recipe.getIngredients().get(i).getMatchingStacks();
				stacks[i] = matchingStacks.length > 0 ? matchingStacks[0] : ItemStack.EMPTY;
			}

			return stacks;
		}

		return null;
	}

	private int getRecipeWidth(IRecipe recipe) {
		if (recipe instanceof ShapedRecipes) {
			return ((ShapedRecipes) recipe).recipeWidth;
		}
		if (recipe instanceof ShapedOreRecipe) {
			return ((ShapedOreRecipe) recipe).getWidth();
		}
		return -1;
	}

	private int getRecipeHeight(IRecipe recipe) {
		if (recipe instanceof ShapedRecipes) {
			return ((ShapedRecipes) recipe).recipeHeight;
		}
		if (recipe instanceof ShapedOreRecipe) {
			return ((ShapedOreRecipe) recipe).getHeight();
		}
		return -1;
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}

}
