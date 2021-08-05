package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFDoor extends ItemDoor implements ModelRegisterCallback {
    public ItemTFDoor(Block block) {
        super(block);
    }
}
