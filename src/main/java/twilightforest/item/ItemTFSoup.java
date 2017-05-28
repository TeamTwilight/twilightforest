package twilightforest.item;

import net.minecraft.item.ItemSoup;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFSoup extends ItemSoup implements ModelRegisterCallback {
    public ItemTFSoup(int healAmount) {
        super(healAmount);
    }
}
