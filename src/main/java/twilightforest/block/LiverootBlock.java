package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolActions;
import twilightforest.item.TFItems;

public class LiverootBlock extends Block {

	public LiverootBlock(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if(player.getItemInHand(hand).getItem().canPerformAction(player.getItemInHand(hand), ToolActions.AXE_STRIP)) {
			level.setBlockAndUpdate(pos, TFBlocks.ROOT_BLOCK.get().defaultBlockState());
			ItemEntity liveroot = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(TFItems.LIVEROOT.get()));
			level.addFreshEntity(liveroot);
			level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			player.getItemInHand(hand).hurtAndBreak(1, player, evt -> evt.broadcastBreakEvent(hand));
		}
		return super.use(state, level, pos, player, hand, hitResult);
	}
}
