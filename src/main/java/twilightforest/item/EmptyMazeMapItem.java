package twilightforest.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EmptyMazeMapItem extends ComplexItem {
	final boolean mapOres;

	public EmptyMazeMapItem(boolean mapOres, Properties properties) {
		super(properties);
		this.mapOres = mapOres;
	}

	// [VanillaCopy] MapItem.onItemRightClick calling own setup method
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = MazeMapItem.setupNewMap(level, Mth.floor(player.getX()), Mth.floor(player.getZ()), (byte) 0, true, false, Mth.floor(player.getY()), this.mapOres);
		ItemStack itemstack1 = player.getItemInHand(hand);
		itemstack1.consume(1, player);

		if (itemstack1.isEmpty()) {
			return InteractionResultHolder.success(itemstack);
		} else {
			if (!player.getInventory().add(itemstack.copy())) {
				player.drop(itemstack, false);
			}

			player.awardStat(Stats.ITEM_USED.get(this));
			player.level().playSound(null, player, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, player.getSoundSource(), 1.0F, 1.0F);
			return InteractionResultHolder.success(itemstack1);
		}
	}
}