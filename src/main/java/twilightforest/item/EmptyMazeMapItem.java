package twilightforest.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EmptyMazeMapItem extends Item {
	final boolean mapOres;

	public EmptyMazeMapItem(boolean mapOres, Properties properties) {
		super(properties);
		this.mapOres = mapOres;
	}

	// [VanillaCopy] EmptyMapItem.use calling own setup method
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			itemstack.consume(1, player);
			player.awardStat(Stats.ITEM_USED.get(this));
			player.level().playSound(null, player, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, player.getSoundSource(), 1.0F, 1.0F);
			ItemStack itemstack1 = MazeMapItem.setupNewMap(level, Mth.floor(player.getX()), Mth.floor(player.getZ()), (byte) 0, true, false, Mth.floor(player.getY()), this.mapOres);
			if (itemstack.isEmpty()) {
				return InteractionResult.SUCCESS.heldItemTransformedTo(itemstack1);
			} else {
				if (!player.getInventory().add(itemstack1.copy())) {
					player.drop(itemstack1, false);
				}

				return InteractionResult.SUCCESS;
			}
		}
	}
}