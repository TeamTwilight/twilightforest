package twilightforest.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import twilightforest.TwilightForestMod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;



public class ContainerTFUncrafting extends Container {

    /** The crafting matrix inventory (3x3). */
    public InventoryTFGoblinUncrafting uncraftingMatrix = new InventoryTFGoblinUncrafting(this);
    public InventoryCrafting assemblyMatrix = new InventoryCrafting(this, 3, 3);
    public InventoryCrafting combineMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory tinkerInput = new InventoryTFGoblinInput(this);
    public IInventory tinkerResult = new InventoryCraftResult();
    private World world;
    
    public ContainerTFUncrafting(InventoryPlayer inventory, World world, int x, int y, int z) {
		this.world = world;
		this.addSlotToContainer(new Slot(this.tinkerInput, 0, 13, 35));
        this.addSlotToContainer(new SlotTFGoblinCraftResult(inventory.player, this.tinkerInput, this.uncraftingMatrix, this.assemblyMatrix, this.tinkerResult, 0, 147, 35));

        int invX;
        int invY;

        for (invX = 0; invX < 3; ++invX)
        {
            for (invY = 0; invY < 3; ++invY)
            {
                this.addSlotToContainer(new SlotTFGoblinUncrafting(inventory.player, this.tinkerInput, this.uncraftingMatrix, this.assemblyMatrix, invY + invX * 3, 300000 + invY * 18, 17 + invX * 18));
            }
        }
        for (invX = 0; invX < 3; ++invX)
        {
            for (invY = 0; invY < 3; ++invY)
            {
                this.addSlotToContainer(new SlotTFGoblinAssembly(inventory.player, this.tinkerInput, this.assemblyMatrix, this.uncraftingMatrix, invY + invX * 3, 62 + invY * 18, 17 + invX * 18));
            }
        }

        for (invX = 0; invX < 3; ++invX)
        {
            for (invY = 0; invY < 9; ++invY)
            {
                this.addSlotToContainer(new Slot(inventory, invY + invX * 9 + 9, 8 + invY * 18, 84 + invX * 18));
            }
        }

        for (invX = 0; invX < 9; ++invX)
        {
            this.addSlotToContainer(new Slot(inventory, invX, 8 + invX * 18, 142));
        }

        this.onCraftMatrixChanged(this.assemblyMatrix);
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
    {
    	// we need to see what inventory is calling this, and update appropriately
    	if (par1IInventory == this.tinkerInput) {
	        // see if there is a recipe for the input
    		ItemStack inputStack = tinkerInput.getStackInSlot(0);
	    	IRecipe recipe = getRecipeFor(inputStack);
	    	
	    	if (recipe != null) {
	    		int recipeWidth = getRecipeWidth(recipe);
	    		int recipeHeight = getRecipeHeight(recipe);
	    		ItemStack[] recipeItems = getRecipeItems(recipe);
	    		
	    		// empty whole grid to start with
	    		// let's not get leftovers if something changes like the recipe size
	    		for (int i = 0; i < this.uncraftingMatrix.getSizeInventory(); i++)
	    		{
	    			this.uncraftingMatrix.setInventorySlotContents(i, null);
	    		}
	    		
	    		// set uncrafting grid
	    		for (int invY = 0; invY < recipeHeight; invY++)  {
	    			for (int invX = 0; invX < recipeWidth; invX++)  {
	    				ItemStack ingredient = ItemStack.copyItemStack(recipeItems[invX + invY * recipeWidth]);
	    				// fix weird recipe for diamond/ingot blocks
	    				if (ingredient != null && ingredient.stackSize > 1)
	    				{
	    					ingredient.stackSize = 1;
	    				}
	    				if (ingredient != null && ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE)
	    				{
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
		    				this.uncraftingMatrix.getStackInSlot(i).stackSize = 0;
		    				damagedParts--;
		    			}
		    		}
	    		}
	    		
	    		// mark banned items damaged
	    		for (int i = 0; i < 9; i++)
	    		{
	    			ItemStack ingredient = this.uncraftingMatrix.getStackInSlot(i);
	    			
	    			if (isIngredientProblematic(ingredient))
	    			{
	    				ingredient.stackSize = 0;
	    			}
	    		}
	    		
	    		// store number of items this recipe produces (and thus how many input items are required for uncrafting)
	    		this.uncraftingMatrix.numberOfInputItems = recipe.getRecipeOutput().stackSize;
	    		this.uncraftingMatrix.uncraftingCost = calculateUncraftingCost();
	    		this.uncraftingMatrix.recraftingCost = 0;
	    	}
	    	else {
	    		//System.out.println("Could not find a recipe for input " + this.tinkerInput.getStackInSlot(0));
	    		
	    		for (int i = 0; i < 9; i++) {
	    			this.uncraftingMatrix.setInventorySlotContents(i, null);
	    		}
	    		this.uncraftingMatrix.numberOfInputItems = 0;
    			this.uncraftingMatrix.uncraftingCost = 0;
	    	}
    	}
    	
    	if (par1IInventory == this.assemblyMatrix || par1IInventory == this.tinkerInput) {
    		if (isMatrixEmpty(this.tinkerInput)) {
		    	// display the output
		    	this.tinkerResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.assemblyMatrix, this.world));
    			this.uncraftingMatrix.recraftingCost = 0;
    		}
    		else {
//    			if (isMatrixEmpty(this.assemblyMatrix)) {
//    				// we just emptied the assembly matrix and need to re-prepare for uncrafting
//	    			this.tinkerResult.setInventorySlotContents(0, null);
//        			this.uncraftingMatrix.uncraftingCost = calculateUncraftingCost();
//        			this.uncraftingMatrix.recraftingCost = 0;
//    			}
//    			else {
    				// we placed an item in the assembly matrix, the next step will re-initialize these with correct values
	    			this.tinkerResult.setInventorySlotContents(0, null);
	    			this.uncraftingMatrix.uncraftingCost = calculateUncraftingCost();
	    			this.uncraftingMatrix.recraftingCost = 0;
//    			}
    		}
    	}
    	
    	// repairing / recrafting: if there is an input item, and items in both grids, can we combine them to produce an output item that is the same type as the input item?
    	if (par1IInventory != this.combineMatrix && !isMatrixEmpty(this.uncraftingMatrix) && !isMatrixEmpty(this.assemblyMatrix)) {
    		// combine the two matrixen
    		for (int i = 0; i < 9; i++) {
    			if (this.assemblyMatrix.getStackInSlot(i) != null) {
    				this.combineMatrix.setInventorySlotContents(i, this.assemblyMatrix.getStackInSlot(i));
    			}
    			else if (this.uncraftingMatrix.getStackInSlot(i) != null && this.uncraftingMatrix.getStackInSlot(i).stackSize > 0) {
    				this.combineMatrix.setInventorySlotContents(i, this.uncraftingMatrix.getStackInSlot(i));
    			}
    			else {
    				this.combineMatrix.setInventorySlotContents(i, null);
    			}
    		}
    		// is there a result from this combined thing?
    		ItemStack result = CraftingManager.getInstance().findMatchingRecipe(this.combineMatrix, this.world);
    		ItemStack input = this.tinkerInput.getStackInSlot(0);
    		
    		if (result != null && isValidMatchForInput(input, result)) {
    			// copy the tag compound
    			// or should we only copy enchantments?
    			NBTTagCompound inputTags = input.getTagCompound();
    			if (inputTags != null)
    			{
    				inputTags = (NBTTagCompound) inputTags.copy();
    			}
    			
    			// if the result has innate enchantments, add them on to our enchantment map
    			Map<Enchantment, Integer> resultInnateEnchantments = null;
    			if (result.isItemEnchanted())
    			{
    				resultInnateEnchantments = EnchantmentHelper.getEnchantments(result);
    			}
    			
    			// check if the input enchantments can even go onto the result item
    			Map<Enchantment, Integer> inputEnchantments = null;
    			if (input.isItemEnchanted())
    			{
    				inputEnchantments = EnchantmentHelper.getEnchantments(input);
					inputEnchantments.keySet().removeIf(enchantment -> !enchantment.canApply(result));
	    		}
    			
    			if (inputTags != null) {
    				// remove enchantments, copy tags, re-add filtered enchantments
    				inputTags.removeTag("ench");
    				result.setTagCompound(inputTags.copy());
    				if (inputEnchantments != null)
    				{
    					EnchantmentHelper.setEnchantments(inputEnchantments, result);
    				}
    			}

    			
    			this.tinkerResult.setInventorySlotContents(0, result);
	    		this.uncraftingMatrix.uncraftingCost = 0;
	    		this.uncraftingMatrix.recraftingCost = calculateRecraftingCost();
	    		
	    		// if there is a recrafting cost, increment the repair cost of the output
	    		if (this.uncraftingMatrix.recraftingCost > 0 && !result.hasDisplayName())
	    		{
	    			result.setRepairCost(input.getRepairCost() + 2);
	    		}
	    		
	    		// finally, add any innate enchantments back onto the result
	    		if (resultInnateEnchantments != null && resultInnateEnchantments.size() > 0)
	    		{
	    			for (Enchantment ench : resultInnateEnchantments.keySet())
	    			{
	    				int level = resultInnateEnchantments.get(ench);

	    				if (EnchantmentHelper.getEnchantmentLevel(ench, result) > level)
	    				{
	    					level = EnchantmentHelper.getEnchantmentLevel(ench, result);
	    				}
	    				
	    				if (EnchantmentHelper.getEnchantmentLevel(ench, result) < level)
	    				{
	    					result.addEnchantment(ench, level);
	    				}
	    			}
	    		}
    		}
    	}
    }

    /**
     * Problematic ingredients are not allowed to be uncrafted in the uncrafting table.
     */
	protected boolean isIngredientProblematic(ItemStack ingredient) {
		return ingredient != null && (ingredient.getItem().hasContainerItem(ingredient) || ingredient.getUnlocalizedName().contains("itemMatter"));
	}

    /**
     * Get the first valid shaped recipe for the item in the input 
     * 
     * @param inputStack
     * @return
     */
	public IRecipe getRecipeFor(ItemStack inputStack) {
    	if (inputStack != null) {
	    	for (IRecipe recipe : CraftingManager.getInstance().getRecipeList()) {
	    		if ((recipe instanceof ShapedRecipes || recipe instanceof ShapedOreRecipe) 
	    				&& recipe.getRecipeOutput().getItem() == inputStack.getItem() && inputStack.stackSize >= recipe.getRecipeOutput().stackSize
	    				&& (!recipe.getRecipeOutput().getHasSubtypes() || recipe.getRecipeOutput().getItemDamage() == inputStack.getItemDamage())) {
	    			return recipe;
	    		}
	    	}
    	}
    	return null;
    }
    
    /**
     * Checks if the result is a valid match for the input.  Currently only accepts armor or tools that are the same type as the input
     * 
     * @param resultStack
     */
    public boolean isValidMatchForInput(ItemStack inputStack, ItemStack resultStack) {
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
    		ItemArmor inputArmor = (ItemArmor)inputStack.getItem();
    		ItemArmor resultArmor = (ItemArmor)resultStack.getItem();
    		
    		return inputArmor.armorType == resultArmor.armorType;
    	}
    	
    	// nope!
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
     * 
     * @return
     */
    public int calculateUncraftingCost() {
    	// we don't want to display anything if there is anything in the assembly grid
    	if (!isMatrixEmpty(this.assemblyMatrix)) {
    		return 0;
    	}
    	else {
    		return countDamageableParts(this.uncraftingMatrix);
    	}
    }
    
    
    /**
     * Return the cost of recrafting, if any.  Return 0 if recrafting is not available at this time
     * 
     * @return
     */
	public int calculateRecraftingCost() {
		if (tinkerInput.getStackInSlot(0) == null || !tinkerInput.getStackInSlot(0).isItemEnchanted() || tinkerResult.getStackInSlot(0) == null) {
			return 0;
		}
		else {
			// okay, if we're here the input item must be enchanted, and we are repairing or recrafting it
			ItemStack input = tinkerInput.getStackInSlot(0);
			ItemStack output = tinkerResult.getStackInSlot(0);
			
			int cost = 0;
			
			// add innate repair cost
			//System.out.println("Innate repair cost is " + input.getRepairCost());
			cost += input.getRepairCost();
			
			// look at the input's enchantments and total them up
			int enchantCost = countTotalEnchantmentCost(input);
			//System.out.println("Enchantment cost is " + enchantCost);
			cost += enchantCost;
			
			// broken pieces cost
    		int damagedCost = (1 + countDamagedParts(input)) * EnchantmentHelper.getEnchantments(output).size();
			//System.out.println("damagedCost is " + damagedCost);
    		cost += damagedCost;
    		
    		// factor in enchantibility difference
    		int enchantabilityDifference = input.getItem().getItemEnchantability() - output.getItem().getItemEnchantability();
			//System.out.println("enchantabilityDifference cost is " + enchantabilityDifference);
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

	public int countTotalEnchantmentCost(ItemStack itemStack) {
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
	
	public int getWeightModifier(Enchantment ench)
	{
        switch (ench.getRarity().getWeight())
        {
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

    
    /**
     * Override the SlotClick method to get what we want
     */
    @Override
	public ItemStack slotClick(int slotNum, int mouseButton, ClickType shiftHeld, EntityPlayer par4EntityPlayer) {
		
    	// if the player is trying to take an item out of the assembly grid, and the assembly grid is empty, take the item from the uncrafting grid.
    	if (slotNum > 0 && par4EntityPlayer.inventory.getItemStack() == null && ((Slot)this.inventorySlots.get(slotNum)).inventory == this.assemblyMatrix && !((Slot)this.inventorySlots.get(slotNum)).getHasStack()) {
    		// is the assembly matrix empty?
    		if(isMatrixEmpty(this.assemblyMatrix)) {
    			slotNum -= 9;
    		}
    	}
    	
    	// if the player is trying to take the result item and they don't have the XP to pay for it, reject them
    	if (slotNum > 0 &&((Slot)this.inventorySlots.get(slotNum)).inventory == this.tinkerResult 
    			&& calculateRecraftingCost() > par4EntityPlayer.experienceLevel && !par4EntityPlayer.capabilities.isCreativeMode) {
    		return null;
    	}
    	
    	// similarly, reject uncrafting if they can't do that either
    	if (slotNum > 0 &&((Slot)this.inventorySlots.get(slotNum)).inventory == this.uncraftingMatrix 
    			&& calculateUncraftingCost() > par4EntityPlayer.experienceLevel && !par4EntityPlayer.capabilities.isCreativeMode) {
    		return null;
    	}
    	
    	// don't allow uncrafting if the server option is turned off
    	if (slotNum > 0 &&((Slot)this.inventorySlots.get(slotNum)).inventory == this.uncraftingMatrix && TwilightForestMod.disableUncrafting) {
    		// send the player a message
    		//par4EntityPlayer.sendChatToPlayer("Uncrafting is disabled in the server configuration.");
    		return null;
    	}
    	
    	// finally, don't give them damaged goods
    	if (slotNum > 0 &&((Slot)this.inventorySlots.get(slotNum)).inventory == this.uncraftingMatrix 
    			&& (((Slot)this.inventorySlots.get(slotNum)).getStack() == null || ((Slot)this.inventorySlots.get(slotNum)).getStack().stackSize == 0)) {
    		return null;
    	}
    	
    	// also we may need to detect here when the player is increasing the stack size of the input slot
		ItemStack ret = super.slotClick(slotNum, mouseButton, shiftHeld, par4EntityPlayer);
		
		// just trigger this event whenever the input slot is clicked for any reason
    	if (slotNum > 0 && ((Slot)this.inventorySlots.get(slotNum)).inventory instanceof InventoryTFGoblinInput) {
    		this.onCraftMatrixChanged(this.tinkerInput);
    	}

    	return ret;
	}
    
    @Override
    protected void retrySlotClick(int slotNum, int mouseButton, boolean par3, EntityPlayer par4EntityPlayer)
    {
    	// if they are taking something out of the uncrafting matrix, bump the slot number back to the assembly matrix
    	// otherwise we lose the stuff in the uncrafting matrix when we shift-click to take multiple things
    	if (((Slot)this.inventorySlots.get(slotNum)).inventory == this.uncraftingMatrix)
    	{
    		slotNum += 9;
    	}
    	
        this.slotClick(slotNum, mouseButton, ClickType.QUICK_MOVE, par4EntityPlayer);
    }

    /**
     * Checks if an inventory has any items in it
     * @param matrix
     * @return
     */
	private boolean isMatrixEmpty(IInventory matrix) {
		boolean matrixEmpty = true;
		for (int i = 0; i < matrix.getSizeInventory(); i++) {
			if (matrix.getStackInSlot(i) != null) {
				matrixEmpty = false;
			}
		}
		return matrixEmpty;
	}
	
    
	/**
	 * Should the specified item count for taking damage?
	 * 
	 * @param itemStack
	 * @return
	 */
    public boolean isDamageableComponent(ItemStack itemStack) {
    	return itemStack != null && itemStack.getItem() != Items.STICK;
    }
    
    /**
     * Count how many items in an inventory can take damage
     * 
     * @param matrix
     * @return
     */
    public int countDamageableParts(IInventory matrix) {
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
    public int countDamagedParts(ItemStack input) {
    	int totalMax4 = Math.max(4, countDamageableParts(this.uncraftingMatrix));
    	float damage = (float)input.getItemDamage() / (float)input.getMaxDamage();
    	int damagedParts = (int) Math.ceil(totalMax4 * damage);
    	return damagedParts;
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNum)
    {
        ItemStack copyItem = null;
        Slot transferSlot = (Slot)this.inventorySlots.get(slotNum);

        if (transferSlot != null && transferSlot.getHasStack())
        {
            ItemStack transferStack = transferSlot.getStack();
            copyItem = transferStack.copy();

            if (slotNum == 0 || slotNum == 1)
            {
            	// result or input goes to inventory or hotbar
                if (!this.mergeItemStack(transferStack, 20, 56, true))
                {
                    return null;
                }

                transferSlot.onSlotChange(transferStack, copyItem);  // what does this do?
            }
            else if (slotNum >= 20 && slotNum < 47)
            {
            	// inventory goes to hotbar
                if (!this.mergeItemStack(transferStack, 47, 56, false))
                {
                    return null;
                }
            }
            else if (slotNum >= 47 && slotNum < 56)
            {
            	// hotbar goes to inventory
                if (!this.mergeItemStack(transferStack, 20, 47, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(transferStack, 20, 56, false))
            {
            	// crafting area goes to inventory or hotbar
                return null;
            }

            if (transferStack.stackSize == 0)
            {
                transferSlot.putStack((ItemStack)null);
            }
            else
            {
                transferSlot.onSlotChanged();
            }

            if (transferStack.stackSize == copyItem.stackSize)
            {
                return null;
            }

            transferSlot.onPickupFromSlot(player, transferStack);
        }

        return copyItem;
    }

    
    
    
    /**
     * Callback for when the crafting gui is closed.
     */
    @Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);

        if (!this.world.isRemote)
        {
        	// drop items in assembly grid
            for (int i = 0; i < 9; ++i)
            {
                ItemStack assemblyStack = this.assemblyMatrix.removeStackFromSlot(i);

                if (assemblyStack != null)
                {
                    par1EntityPlayer.dropItem(assemblyStack, false);
                }
            }
            
            // drop input
            ItemStack inputStack = this.tinkerInput.removeStackFromSlot(0);
            if (inputStack != null)
            {
                par1EntityPlayer.dropItem(inputStack, false);
            }
        }
    }

	/**
	 * If we can determine this recipe's items, return it.
	 */
	public ItemStack[] getRecipeItems(IRecipe recipe) {
		if (recipe instanceof ShapedRecipes)
		{
			return getRecipeItemsShaped((ShapedRecipes) recipe);
		}
		if (recipe instanceof ShapedOreRecipe)
		{
			return getRecipeItemsOre((ShapedOreRecipe) recipe);
		}
		return null;
	}
    
    /**
     * Uses ModLoader.getPrivateValue to get the items associated with a recipe.
     * @param shaped
     * @return
     */
	public ItemStack[] getRecipeItemsShaped(ShapedRecipes shaped) {
		return shaped.recipeItems;
		
//    	try {
//			return (ItemStack[])(ObfuscationReflectionHelper.getPrivateValue(ShapedRecipes.class, shaped, 2));
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		}
//    	return null;
    }    
    
    /**
     * Uses ModLoader.getPrivateValue to get the items associated with a recipe.
     * @param shaped
     * @return
     */
	@SuppressWarnings("unchecked")
	public ItemStack[] getRecipeItemsOre(ShapedOreRecipe shaped) {
    	try {
    		// ShapedOreRecipes can have either an ItemStack or an ArrayList of ItemStacks
    		Object[] objects = ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shaped, 3);
    		ItemStack[] items = new ItemStack[objects.length];
    		
    		for (int i = 0; i < objects.length; i++)
    		{
    			if (objects[i] instanceof ItemStack)
    			{
    				items[i] = (ItemStack)objects[i];
    			}
    			if (objects[i] instanceof ArrayList && ((ArrayList<ItemStack>)objects[i]).size() > 0)
    			{
    				items[i] = ((ArrayList<ItemStack>)objects[i]).get(0);
    			}
    		}
    		
			return items;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
    	return null;
    }    
    
	/**
	 * If we can determine this recipe's width, return it.
	 */
	public int getRecipeWidth(IRecipe recipe) {
		if (recipe instanceof ShapedRecipes)
		{
			return getRecipeWidthShaped((ShapedRecipes) recipe);
		}
		if (recipe instanceof ShapedOreRecipe)
		{
			return getRecipeWidthOre((ShapedOreRecipe) recipe);
		}
		return -1;
	}

	/**
     * Uses ModLoader.getPrivateValue to get the recipe width
     * @param shaped
     * @return
     */
    public int getRecipeWidthShaped(ShapedRecipes shaped) {
    	return shaped.recipeWidth;
    	
//    	try {
//			return ((Integer)(ObfuscationReflectionHelper.getPrivateValue(ShapedRecipes.class, shaped, 0))).intValue();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		}
//    	return 0;
    }    
    
	/**
     * Uses ModLoader.getPrivateValue to get the recipe width
     * @param shaped
     * @return
     */
    public int getRecipeWidthOre(ShapedOreRecipe shaped) {
    	try {
			return ((Integer)(ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shaped, 4))).intValue();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
    	return 0;
    }    
    
	/**
	 * If we can determine this recipe's width, return it.
	 */
    public int getRecipeHeight(IRecipe recipe) {
		if (recipe instanceof ShapedRecipes)
		{
			return getRecipeHeightShaped((ShapedRecipes) recipe);
		}
		if (recipe instanceof ShapedOreRecipe)
		{
			return getRecipeHeightOre((ShapedOreRecipe) recipe);
		}
		return -1;
	}
    
    /**
     * Uses ModLoader.getPrivateValue to get the recipe height
     * @param shaped
     * @return
     */
    public int getRecipeHeightShaped(ShapedRecipes shaped) {
    	return shaped.recipeHeight;
//    	try {
//			return ((Integer)(ObfuscationReflectionHelper.getPrivateValue(ShapedRecipes.class, shaped, 1))).intValue();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		}
//    	return 0;
    }

    /**
     * Uses ModLoader.getPrivateValue to get the recipe height
     * @param shaped
     * @return
     */
    public int getRecipeHeightOre(ShapedOreRecipe shaped) {
    	try {
			return ((Integer)(ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shaped, 5))).intValue();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
    	return 0;
    }


	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}

}
