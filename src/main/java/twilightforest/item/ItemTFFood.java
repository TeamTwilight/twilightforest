package twilightforest.item;

import net.minecraft.item.ItemFood;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFFood extends ItemFood implements ModelRegisterCallback {
	public ItemTFFood(int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
	}
}
