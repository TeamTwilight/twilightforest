package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.enums.FireJetVariant;
import twilightforest.tileentity.TileEntityTFFlameJet;
import twilightforest.tileentity.TileEntityTFPoppingJet;

import javax.annotation.Nullable;

public class BlockTFEncasedFireJet extends Block {

	public static final EnumProperty<FireJetVariant> STATE = EnumProperty.create("state", FireJetVariant.class);

	protected BlockTFEncasedFireJet() {
		super(Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(1.5F, 0.0F).sound(SoundType.WOOD));
		this.setDefaultState(stateContainer.getBaseState().with(STATE, FireJetVariant.IDLE));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(STATE);
	}

	//TODO: We drop Encased Fire Jet, aight?
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
	public int getLightValue(BlockState state) {
		return state.get(STATE) == FireJetVariant.FLAME ? 15 : 0;
	}

	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity) {
		return state.get(STATE) == FireJetVariant.IDLE ? null : PathNodeType.DAMAGE_FIRE;
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isRemote) return;

		FireJetVariant variant = state.get(STATE);
		boolean powered = world.isBlockPowered(pos);

		if (variant == FireJetVariant.IDLE && powered) {
			world.setBlockState(pos, state.with(STATE, FireJetVariant.POPPING), 3);
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.get(STATE) == FireJetVariant.POPPING || state.get(STATE) == FireJetVariant.FLAME;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		switch (state.get(STATE)) {
			case POPPING:
				return new TileEntityTFPoppingJet(FireJetVariant.FLAME);
			case FLAME:
				return new TileEntityTFFlameJet(FireJetVariant.IDLE);
			case IDLE:
			default:
				return null;
		}
	}
}
