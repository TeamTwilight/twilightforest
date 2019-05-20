package twilightforest.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import twilightforest.advancements.TFAdvancements;

import javax.annotation.Nonnull;

public class ItemTFHydraChops extends ItemTFFood {

	public ItemTFHydraChops(int amount, float saturation) {
		super(amount, saturation, true);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase living) {
		// if the player is at zero food, achievements
		if (living instanceof EntityPlayerMP && ((EntityPlayerMP) living).getFoodStats().getFoodLevel() <= 0) {
			TFAdvancements.CONSUME_HYDRA_CHOP.trigger((EntityPlayerMP) living);
		}
		// then normal effects
		return super.onItemUseFinish(itemStack, world, living);
	}

	private static final EnumRarity RARITY = EnumRarity.UNCOMMON;

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return stack.isItemEnchanted() ? EnumRarity.RARE.compareTo(RARITY) > 0 ? EnumRarity.RARE : RARITY : RARITY;
	}
}
