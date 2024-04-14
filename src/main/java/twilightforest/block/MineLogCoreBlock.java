package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import twilightforest.config.TFConfig;
import twilightforest.init.TFSounds;
import twilightforest.item.OreMagnetItem;
import twilightforest.util.WorldUtil;

public class MineLogCoreBlock extends SpecialMagicLogBlock {

	public MineLogCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.disableMiningCore;
	}

	/**
	 * The miner's tree generates the ore magnet effect randomly every second
	 */
	@Override
	void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand) {
		BlockPos dPos = WorldUtil.randomOffset(rand, pos, TFConfig.miningCoreRange);
		int moved = OreMagnetItem.doMagnet(level, pos, dPos, true);

		if (moved > 0) {
			level.playSound(null, pos, TFSounds.MAGNET_GRAB.get(), SoundSource.BLOCKS, 0.1F, 1.0F);
		}
	}
}
