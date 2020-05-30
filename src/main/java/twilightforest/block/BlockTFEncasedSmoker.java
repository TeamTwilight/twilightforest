package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.tileentity.TileEntityTFSmoker;

import javax.annotation.Nullable;

public class BlockTFEncasedSmoker extends Block {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	protected BlockTFEncasedSmoker() {
		super(Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(1.5F, 0.0F).sound(SoundType.WOOD));
		//this.setCreativeTab(TFItems.creativeTab); TODO 1.14
		this.setDefaultState(stateContainer.getBaseState().with(ACTIVE, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(ACTIVE);
	}

	//TODO: We drop Encased Smoker, aight?
//	@Override
//	public int damageDropped(BlockState state) {
//		switch (state.get(VARIANT)) {
//			case ENCASED_SMOKER_ON:
//				state = state.with(VARIANT, FireJetVariant.ENCASED_SMOKER_OFF);
//				break;
//			case ENCASED_JET_POPPING:
//				state = state.with(VARIANT, FireJetVariant.ENCASED_JET_IDLE);
//				break;
//			case ENCASED_JET_FLAME:
//				state = state.with(VARIANT, FireJetVariant.ENCASED_JET_IDLE);
//				break;
//			case JET_POPPING:
//				state = state.with(VARIANT, FireJetVariant.JET_IDLE);
//				break;
//			case JET_FLAME:
//				state = state.with(VARIANT, FireJetVariant.JET_IDLE);
//				break;
//			default:
//				break;
//		}
//
//		return getMetaFromState(state);
//	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isRemote) return;

		boolean powered = world.isBlockPowered(pos);

		if (!state.get(ACTIVE) && powered) {
			world.setBlockState(pos, state.with(ACTIVE, true), 3);
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}

		if (state.get(ACTIVE) && !powered) {
			world.setBlockState(pos, state.with(ACTIVE, false), 3);
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.get(ACTIVE);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityTFSmoker();
	}
}
