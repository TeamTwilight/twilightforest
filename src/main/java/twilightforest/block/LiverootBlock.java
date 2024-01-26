package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ToolActions;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

public class LiverootBlock extends Block {

	public LiverootBlock(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (player.getItemInHand(hand).getItem().canPerformAction(player.getItemInHand(hand), ToolActions.AXE_STRIP)) {
			level.setBlockAndUpdate(pos, TFBlocks.ROOT_BLOCK.get().defaultBlockState());
			int amountOfRoots = 1;
			//fortune formula copied from ApplyBonusCount.OreDrops.calculateNewCount so it acts exactly like the loot table
			if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player.getItemInHand(hand)) > 0) {
				int i = level.getRandom().nextInt(EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player.getItemInHand(hand)) + 2) - 1;
				if (i < 0) {
					i = 0;
				}

				amountOfRoots = amountOfRoots * (i + 1);
			}
			ItemEntity liveroot = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(TFItems.LIVEROOT.get(), amountOfRoots));
			level.addFreshEntity(liveroot);
			level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			player.getItemInHand(hand).hurtAndBreak(1, player, evt -> evt.broadcastBreakEvent(hand));
			return InteractionResult.sidedSuccess(level.isClientSide());
		}
		return super.use(state, level, pos, player, hand, result);
	}
}
