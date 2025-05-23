package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;
import twilightforest.enums.FireJetVariant;
import twilightforest.init.TFSounds;

public class EncasedFireJetBlock extends FireJetBlock {

	public static final EnumProperty<FireJetVariant> STATE = FireJetBlock.STATE;

	public EncasedFireJetBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean isMoving) {
		FireJetVariant variant = state.getValue(STATE);
		boolean powered = level.hasNeighborSignal(pos);

		if (variant == FireJetVariant.IDLE && powered) {
			level.setBlockAndUpdate(pos, state.setValue(STATE, FireJetVariant.POPPING));
			level.playSound(null, pos, TFSounds.JET_START.get(), SoundSource.BLOCKS, 0.3F, 0.6F);

		} else if (variant == FireJetVariant.TIMEOUT && !powered) {
			level.setBlockAndUpdate(pos, state.setValue(STATE, FireJetVariant.IDLE));
		}
	}
}
