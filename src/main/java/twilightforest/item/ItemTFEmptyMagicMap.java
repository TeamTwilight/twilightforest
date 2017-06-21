package twilightforest.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import twilightforest.TFAchievementPage;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFEmptyMagicMap extends ItemMapBase implements ModelRegisterCallback {
	protected ItemTFEmptyMagicMap() {
		this.setCreativeTab(TFItems.creativeTab);
	}

	// [VanillaCopy] ItemEmptyMap.onItemRightClick, edits noted
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		// TF - scale at 4
		ItemStack itemstack = ItemTFMagicMap.setupNewMap(worldIn, playerIn.posX, playerIn.posZ, (byte) 4, true, false);
		ItemStack itemstack1 = playerIn.getHeldItem(handIn);
		itemstack1.shrink(1);

		// TF - achievement
		playerIn.addStat(TFAchievementPage.twilightMagicMap);

		if (itemstack1.isEmpty()) {
			return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
		} else {
			if (!playerIn.inventory.addItemStackToInventory(itemstack.copy())) {
				playerIn.dropItem(itemstack, false);
			}

			playerIn.addStat(StatList.getObjectUseStats(this));
			return new ActionResult<>(EnumActionResult.SUCCESS, itemstack1);
		}
	}
}
