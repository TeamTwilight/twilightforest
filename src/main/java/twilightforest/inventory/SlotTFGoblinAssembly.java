package twilightforest.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class SlotTFGoblinAssembly extends Slot {

	public SlotTFGoblinAssembly(PlayerEntity player, IInventory inputSlot, IInventory assemblyMatrix, IInventory uncraftingMatrix, int slotNum, int x, int y) {
		super(assemblyMatrix, slotNum, x, y);
	}
}
