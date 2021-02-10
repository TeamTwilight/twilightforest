package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.enums.FireJetVariant;

public class BlockTFEncasedFireJet extends BlockTFFireJet {

	public static final EnumProperty<FireJetVariant> STATE = EnumProperty.create("state", FireJetVariant.class);

	protected BlockTFEncasedFireJet(Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		FireJetVariant variant = state.get(STATE);
		boolean powered = world.isBlockPowered(pos);

		if (variant == FireJetVariant.IDLE && powered) {
			world.setBlockState(pos, state.with(STATE, FireJetVariant.POPPING));
			world.playSound(null, pos, TFSounds.JET_START, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
	}
}
