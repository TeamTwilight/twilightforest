package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MobEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import twilightforest.enums.FireJetVariant;
import twilightforest.tileentity.TileEntityTFFlameJet;
import twilightforest.tileentity.TileEntityTFPoppingJet;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTFFireJet extends Block {

	public static final EnumProperty<FireJetVariant> STATE = EnumProperty.create("state", FireJetVariant.class);

	protected BlockTFFireJet() {
		super(Properties.create(Material.ROCK, MaterialColor.GRASS).hardnessAndResistance(1.5F, 0.0F).sound(SoundType.WOOD).tickRandomly());
		//this.setCreativeTab(TFItems.creativeTab); TODO 1.14
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(STATE);
	}

	//TODO: We drop just Fire Jet now
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
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!world.isRemote && state.get(STATE) == FireJetVariant.IDLE) {
			BlockPos lavaPos = findLavaAround(world, pos.down());

			if (isLava(world, lavaPos)) {
				world.setBlockState(lavaPos, Blocks.AIR.getDefaultState());
				world.setBlockState(pos, state.with(STATE, FireJetVariant.POPPING), 2);
			}
		}
	}

	/**
	 * Find a full block of lava near the designated block.  This is intentionally not really that reliable.
	 */
	private BlockPos findLavaAround(World world, BlockPos pos) {
		if (isLava(world, pos)) {
			return pos;
		}

		for (int i = 0; i < 3; i++) {
			BlockPos randPos = pos.add(world.rand.nextInt(3) - 1, 0, world.rand.nextInt(3) - 1);
			if (isLava(world, randPos)) {
				return randPos;
			}
		}

		return pos;
	}

	private boolean isLava(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block b = state.getBlock();
		IntegerProperty levelProp = b instanceof FlowingFluidBlock
				? FlowingFluidBlock.LEVEL
				: null;
		return state.getMaterial() == Material.LAVA
				&& (levelProp == null || state.get(levelProp) == 0);
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
