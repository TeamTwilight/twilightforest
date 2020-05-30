package twilightforest.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResultType;
import twilightforest.block.BlockTFLockedVanishing;
import twilightforest.block.TFBlocks;

import javax.annotation.Nonnull;

public class ItemTFTowerKey extends Item {
	ItemTFTowerKey(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		BlockState state = context.getWorld().getBlockState(context.getPos());
		if (state == TFBlocks.locked_vanishing_block.get().getDefaultState().with(BlockTFLockedVanishing.LOCKED, true)) {
			if (!context.getWorld().isRemote) {
				BlockTFLockedVanishing.unlockBlock(context.getWorld(), context.getPos());
				context.getPlayer().getHeldItem(context.getHand()).shrink(1);
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}
}
