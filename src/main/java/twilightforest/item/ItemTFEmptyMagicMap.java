package twilightforest.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AbstractMapItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemTFEmptyMagicMap extends AbstractMapItem {
	protected ItemTFEmptyMagicMap(Properties props) {
		super(props);
	}

	// [VanillaCopy] ItemEmptyMap.onItemRightClick, edits noted
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		// TF - scale at 4
		ItemStack itemstack = ItemTFMagicMap.setupNewMap(worldIn, MathHelper.floor(playerIn.getX()), MathHelper.floor(playerIn.getZ()), (byte) 4, true, false);
		ItemStack itemstack1 = playerIn.getHeldItem(handIn);
		if (!playerIn.abilities.isCreativeMode) {
			itemstack1.shrink(1);
		}

		if (itemstack1.isEmpty()) {
			return ActionResult.success(itemstack);
		} else {
			if (!playerIn.inventory.addItemStackToInventory(itemstack.copy())) {
				playerIn.dropItem(itemstack, false);
			}

			playerIn.addStat(Stats.ITEM_USED.get(this));
			return ActionResult.success(itemstack1);
		}
	}
}
