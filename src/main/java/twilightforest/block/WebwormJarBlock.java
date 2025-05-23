package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

public class WebwormJarBlock extends JarBlock {

	public WebwormJarBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (player.isShiftKeyDown()) {
			ItemEntity webworm = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(TFBlocks.WEBWORM));
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			if (level instanceof ServerLevel serverLevel) {
				webworm.spawnAtLocation(serverLevel, webworm.getItem());
				webworm.spawnAtLocation(serverLevel, TFItems.MASON_JAR.get());
			}
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			return InteractionResult.SUCCESS;
		}
		return super.useWithoutItem(state, level, pos, player, result);
	}

	@Override
	public Item getDefaultLid() {
		return TFBlocks.VEILWOOD_LOG.asItem();
	}
}
