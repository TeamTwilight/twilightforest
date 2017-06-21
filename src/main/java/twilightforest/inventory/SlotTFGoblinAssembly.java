package twilightforest.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;


public class SlotTFGoblinAssembly extends Slot {


	public SlotTFGoblinAssembly(EntityPlayer par1EntityPlayer, IInventory inputSlot, IInventory assemblyMatrix, IInventory uncraftingMatrix, int slotNum, int x, int y) {
		super(assemblyMatrix, slotNum, x, y);
	}


}
