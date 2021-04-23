package twilightforest.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import twilightforest.item.TFItems;

import java.util.Random;

public class BlockTFExperiment115 extends Block {

    public static final IntegerProperty BITES_TAKEN = IntegerProperty.create("omnomnom", 0, 7);
    public static final BooleanProperty REGENERATE = BooleanProperty.create("regenerate");

    private static final VoxelShape QUARTER_SHAPE = makeCuboidShape(1, 0, 1, 8, 8, 8);
    private static final VoxelShape HALF_SHAPE = makeCuboidShape(1, 0, 1, 8, 8, 15);
    private static final VoxelShape THREE_QUARTER_SHAPE = VoxelShapes.combineAndSimplify(HALF_SHAPE, makeCuboidShape(8, 0, 8, 15, 8, 15), IBooleanFunction.OR);
    private static final VoxelShape FULL_SHAPE = makeCuboidShape(1, 0, 1, 15, 8, 15);

    public BlockTFExperiment115() {
        super(Properties.create(Material.CAKE, MaterialColor.IRON).hardnessAndResistance(0.5F).sound(SoundType.CLOTH).tickRandomly());
        this.setDefaultState(this.stateContainer.getBaseState().with(BITES_TAKEN, 7).with(REGENERATE, false));
    }

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(BITES_TAKEN)) {
			default:
				return FULL_SHAPE;
			case 2:
			case 3:
				return THREE_QUARTER_SHAPE;
			case 4:
			case 5:
				return HALF_SHAPE;
			case 6:
			case 7:
				return QUARTER_SHAPE;
		}
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		int bitesTaken = state.get(BITES_TAKEN);
		ItemStack stack = player.getHeldItem(hand);

		if (!player.isSneaking()) {
			if (bitesTaken > 0 && stack.getItem() == TFItems.experiment_115.get()) {
				worldIn.setBlockState(pos, state.with(BITES_TAKEN, bitesTaken - 1));
				if (!player.isCreative()) stack.shrink(1);
				if (player instanceof ServerPlayerEntity) CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, stack);
				return ActionResultType.SUCCESS;
			} else if (((!state.get(REGENERATE)) && stack.getItem() == Items.REDSTONE) && (player.isCreative() || bitesTaken == 0)) {
				worldIn.setBlockState(pos, state.with(REGENERATE,true));
				if (!player.isCreative()) stack.shrink(1);
				if (player instanceof ServerPlayerEntity) CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, stack);
				return ActionResultType.SUCCESS;
			}
		}
		return this.eatCake(worldIn, pos, state, player);
	}

	private ActionResultType eatCake(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.canEat(false)) return ActionResultType.PASS;
        else {
            player.addStat(Stats.EAT_CAKE_SLICE);
            player.getFoodStats().addStats(4, 0.3F);
            int i = state.get(BITES_TAKEN);

            if (i < 7) {
            	world.setBlockState(pos, state.with(BITES_TAKEN, Integer.valueOf(i + 1)), 3);
            } else {
            	world.removeBlock(pos, false);
            }

            if (player instanceof ServerPlayerEntity)
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, new ItemStack(TFItems.experiment_115.get(), 8 - i));

            return ActionResultType.SUCCESS;
        }
    }

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (state.get(REGENERATE) && state.get(BITES_TAKEN) != 0) {
			worldIn.setBlockState(pos, state.with(BITES_TAKEN, state.get(BITES_TAKEN) - 1));
		}
	}

	@Override
	@Deprecated
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
	}

	@Override
	@Deprecated
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BITES_TAKEN, REGENERATE);
	}

    @Override
    @Deprecated
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        return 15-(state.get(BITES_TAKEN)*2);
    }

    @Override
	@Deprecated
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return true;
    }

    @Override
	@Deprecated
    public boolean canProvidePower(BlockState state) {
        return state.get(REGENERATE);
    }

	@Override
	@Deprecated
	public int getWeakPower(BlockState state, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return state.get(REGENERATE) ? 15-(state.get(BITES_TAKEN)*2) : 0;
	}
}
