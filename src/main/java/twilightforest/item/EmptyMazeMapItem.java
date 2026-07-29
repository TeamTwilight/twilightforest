package twilightforest.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.inventory.InventoryUtil;

public class EmptyMazeMapItem extends Item {
	final boolean mapOres;

	public EmptyMazeMapItem(boolean mapOres, Properties properties) {
		super(properties);
		this.mapOres = mapOres;
	}

	// [VanillaCopy] EmptyMapItem.use calling own setup method
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (level instanceof ServerLevel serverLevel) {
			itemStack.consume(1, player);
			player.awardStat(Stats.ITEM_USED.get(this));
			serverLevel.playSound(null, player, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, player.getSoundSource(), 1.0F, 1.0F);
			ItemStack map = MazeMapItem.setupNewMap(serverLevel, Mth.floor(player.getX()), Mth.floor(player.getZ()), (byte) 0, true, false, Mth.floor(player.getY()), this.mapOres);
			if (itemStack.isEmpty()) {
				return InteractionResult.SUCCESS.heldItemTransformedTo(map);
			} else {
				InventoryUtil.giveItemToPlayer(player, map.copy());

				return InteractionResult.SUCCESS;
			}
		} else {
			return InteractionResult.SUCCESS;
		}
	}
}