package twilightforest.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class EmptyMazeMapItem extends ComplexItem {
	boolean mapOres;

	protected EmptyMazeMapItem(boolean mapOres, Properties props) {
		super(props);
		this.mapOres = mapOres;
	}

	// [VanillaCopy] MapItem.onItemRightClick calling own setup method
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = MazeMapItem.setupNewMap(worldIn, Mth.floor(playerIn.getX()), Mth.floor(playerIn.getZ()), (byte) 0, true, false, Mth.floor(playerIn.getY()), this.mapOres);
		ItemStack itemstack1 = playerIn.getItemInHand(handIn);
		if (!playerIn.getAbilities().instabuild) {
			itemstack1.shrink(1);
		}

		if (itemstack1.isEmpty()) {
			return InteractionResultHolder.success(itemstack);
		} else {
			if (!playerIn.getInventory().add(itemstack.copy())) {
				playerIn.drop(itemstack, false);
			}

			playerIn.awardStat(Stats.ITEM_USED.get(this));
			playerIn.level.playSound(null, playerIn, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, playerIn.getSoundSource(), 1.0F, 1.0F);
			return InteractionResultHolder.success(itemstack1);
		}
	}
}
