package twilightforest.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AbstractMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemTFEmptyMazeMap extends AbstractMapItem {
	boolean mapOres;

	protected ItemTFEmptyMazeMap(boolean mapOres, Properties props) {
		super(props);
		this.mapOres = mapOres;
	}

	// [VanillaCopy] MapItem.onItemRightClick calling own setup method
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = ItemTFMazeMap.setupNewMap(worldIn, MathHelper.floor(playerIn.getX()), MathHelper.floor(playerIn.getZ()), (byte) 0, true, false, MathHelper.floor(playerIn.getY()), this.mapOres);
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
