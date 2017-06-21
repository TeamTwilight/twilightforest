package twilightforest.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import twilightforest.TFAchievementPage;

public class ItemTFHydraChops extends ItemTFFood {

	public ItemTFHydraChops(int amount, float saturation) {
		super(amount, saturation, true);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase living) {
		// if the player is at zero food, achievements
		if (living instanceof EntityPlayer && ((EntityPlayer) living).getFoodStats().getFoodLevel() <= 0) {
			((EntityPlayer) living).addStat(TFAchievementPage.twilightHydraChop);
		}
		// then normal effects
		return super.onItemUseFinish(itemStack, world, living);
	}
}
