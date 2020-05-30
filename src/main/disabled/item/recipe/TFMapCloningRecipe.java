package twilightforest.item.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.MapCloningRecipe;
import net.minecraft.world.World;

public class TFMapCloningRecipe extends MapCloningRecipe {
	private final Item fullMapID;
	private final Item blankMapID;

	public TFMapCloningRecipe(Item magicMap, Item emptyMagicMap) {
		this.fullMapID = magicMap;
		this.blankMapID = emptyMagicMap;
	}

	// [VanillaCopy] super with own items
	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		int i = 0;
		ItemStack itemstack = ItemStack.EMPTY;

		for (int j = 0; j < inv.getSizeInventory(); ++j) {
			ItemStack itemstack1 = inv.getStackInSlot(j);

			if (!itemstack1.isEmpty()) {
				if (itemstack1.getItem() == fullMapID) {
					if (!itemstack.isEmpty()) {
						return false;
					}

					itemstack = itemstack1;
				} else {
					if (itemstack1.getItem() != blankMapID) {
						return false;
					}

					++i;
				}
			}
		}

		return !itemstack.isEmpty() && i > 0;
	}

	// [VanillaCopy] super with own items
	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		int i = 0;
		ItemStack itemstack = ItemStack.EMPTY;

		for (int j = 0; j < inv.getSizeInventory(); ++j) {
			ItemStack itemstack1 = inv.getStackInSlot(j);

			if (!itemstack1.isEmpty()) {
				if (itemstack1.getItem() == fullMapID) {
					if (!itemstack.isEmpty()) {
						return ItemStack.EMPTY;
					}

					itemstack = itemstack1;
				} else {
					if (itemstack1.getItem() != blankMapID) {
						return ItemStack.EMPTY;
					}

					++i;
				}
			}
		}

		if (!itemstack.isEmpty() && i >= 1) {
			ItemStack itemstack2 = new ItemStack(fullMapID, i + 1, itemstack.getMetadata());

			if (itemstack.hasDisplayName()) {
				itemstack2.setDisplayName(itemstack.getDisplayName());
			}

			if (itemstack.hasTag()) {
				itemstack2.setTag(itemstack.getTag());
			}

			return itemstack2;
		} else {
			return ItemStack.EMPTY;
		}
	}
}
