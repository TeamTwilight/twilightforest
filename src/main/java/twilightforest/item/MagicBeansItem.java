package twilightforest.item;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

public class MagicBeansItem extends Item {

	public MagicBeansItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		ItemStack stack = context.getItemInHand();

		int maxY = Math.max(pos.getY() + 100, 175);
		if (pos.getY() < maxY && level.getBlockState(pos).is(TFBlocks.UBEROUS_SOIL) && level.getBlockState(pos.above()).isAir()) {
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos.above(), TFBlocks.BEANSTALK_GROWER.get().defaultBlockState());
				level.playSound(null, pos, TFSounds.BEANSTALK_GROWTH.get(), SoundSource.BLOCKS, 4.0F, 1.0F);
				if (player instanceof ServerPlayer sp) {
					player.awardStat(Stats.ITEM_USED.get(this));
					CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(sp, pos, stack);
				}
				stack.consume(1, player);
			}

			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.PASS;
		}
	}
}