package twilightforest.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.WroughtIronFenceBlock;
import twilightforest.init.TFSounds;

public class WroughtIronFenceItem extends BlockItem {
	public WroughtIronFenceItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.isSecondaryUseActive() && context.getLevel().getBlockState(context.getClickedPos()).is(this.getBlock())) {
			Level level = context.getLevel();
			BlockPos pos = context.getClickedPos();
			BlockState state =level.getBlockState(pos);
			level.setBlockAndUpdate(pos, state.setValue(WroughtIronFenceBlock.POST, WroughtIronFenceBlock.PostState.CAPPED));
			level.playSound(null, pos, TFSounds.WROUGHT_IRON_FENCE_EXTENDED.get(), SoundSource.BLOCKS, 0.35F, level.getRandom().nextFloat() * 0.1F + 0.75F);
			return InteractionResult.SUCCESS;
		}
		return super.useOn(context);
	}
}
