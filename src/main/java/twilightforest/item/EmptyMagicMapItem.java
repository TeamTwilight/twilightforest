package twilightforest.item;

import net.minecraft.network.chat.Component;
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
import twilightforest.tags.TFDimensionTypeTags;

public class EmptyMagicMapItem extends Item {
	public EmptyMagicMapItem(Properties properties) {
		super(properties);
	}

	// [VanillaCopy] EmptyMapItem.use, edits noted
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack emptyMapStack = player.getItemInHand(hand);
		if (level instanceof ServerLevel serverLevel) {
			//TF - only allow magic maps to be created in allowed dimensions (controlled via tag)
			if (!level.dimensionTypeRegistration().is(TFDimensionTypeTags.ALLOWS_MAGIC_MAP_CHARTING)) {
				player.sendOverlayMessage(Component.translatable("misc.twilightforest.magic_map_fail"));
				return InteractionResult.FAIL;
			}

			emptyMapStack.consume(1, player);
			player.awardStat(Stats.ITEM_USED.get(this));
			serverLevel.playSound(null, player, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, player.getSoundSource(), 1.0F, 1.0F);

			// TF - scale at 4
			ItemStack newMapStack = MagicMapItem.setupNewMap(serverLevel, Mth.floor(player.getX()), Mth.floor(player.getZ()), (byte) 4, true, false);

			if (emptyMapStack.isEmpty()) {
				return InteractionResult.SUCCESS.heldItemTransformedTo(newMapStack);
			} else {
				InventoryUtil.giveItemToPlayer(player, newMapStack.copy());
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.SUCCESS;
	}
}