package twilightforest.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.block.OminousCandleBlock;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

public class ExanimateEssenceItem extends Item {
	public ExanimateEssenceItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		boolean flag = false;
		BlockState state = level.getBlockState(blockpos);
		if (state.getBlock() instanceof CandleBlock candleBlock && OminousCandleBlock.CANDLE_MAP.containsKey(candleBlock) && state.getValue(CandleBlock.LIT)) {
			this.playSound(level, blockpos);
			level.setBlockAndUpdate(blockpos, OminousCandleBlock.CANDLE_MAP.get(candleBlock).get().defaultBlockState().setValue(OminousCandleBlock.CANDLES, state.getValue(CandleBlock.CANDLES)));
			level.gameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, blockpos);
			OminousCandleBlock.eruptFlameParticles(level, blockpos, level.getBlockState(blockpos));
			flag = true;
		} else {
			blockpos = blockpos.relative(context.getClickedFace());
			state = level.getBlockState(blockpos);

			if (state.canBeReplaced() && TFBlocks.OMINOUS_FIRE.get().canSurvive(TFBlocks.OMINOUS_FIRE.get().defaultBlockState(), level, blockpos)) {
				this.playSound(level, blockpos);
				level.setBlockAndUpdate(blockpos, TFBlocks.OMINOUS_FIRE.get().defaultBlockState());
				level.gameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, blockpos);
				flag = true;
			}
		}

		if (flag) {
			context.getItemInHand().shrink(1);
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.FAIL;
		}
	}

	private void playSound(Level level, BlockPos pos) {
		RandomSource randomsource = level.getRandom();
		level.playSound(
			null, pos, TFSounds.OMINOUS_FIRE.get(), SoundSource.BLOCKS, 1.5F, (randomsource.nextFloat() - randomsource.nextFloat()) * 0.2F + 0.75F
		);
	}
}
