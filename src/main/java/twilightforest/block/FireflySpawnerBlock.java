package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFParticleType;

public class FireflySpawnerBlock extends AbstractParticleSpawnerBlock implements SimpleWaterloggedBlock {

	private static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public FireflySpawnerBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		super.animateTick(state, level, pos, rand);

		if (rand.nextInt(5) == 0) {
			double dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
			double dy = pos.getY() + 0.4F + ((rand.nextFloat() - rand.nextFloat()) * 0.3F);
			double dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
			level.addParticle(TFParticleType.FIREFLY.get(), dx, dy, dz, 0, 0, 0);
		}
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.getItem() == TFBlocks.FIREFLY.get().asItem() && !player.isShiftKeyDown() && state.getValue(RADIUS) < 10) {
			level.setBlockAndUpdate(pos, state.setValue(RADIUS, state.getValue(RADIUS) + 1));
			stack.consume(1, player);
			player.displayClientMessage(Component.translatable("misc.twilightforest.firefly_spawner_radius", state.getValue(RADIUS) + 1), true);
			return InteractionResult.SUCCESS;
		} else if (player.isShiftKeyDown() && state.getValue(RADIUS) > 1) {
			level.setBlockAndUpdate(pos, state.setValue(RADIUS, state.getValue(RADIUS) - 1));
			ItemEntity bug = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D, new ItemStack(TFBlocks.FIREFLY.get()));
			level.addFreshEntity(bug);
			player.displayClientMessage(Component.translatable("misc.twilightforest.firefly_spawner_radius", state.getValue(RADIUS) - 1), true);
			return InteractionResult.SUCCESS;
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
	public ParticleOptions getParticlesToSpawn() {
		return TFParticleType.PARTICLE_SPAWNER_FIREFLY.get();
	}

	@Override
	public int getParticleCountPerSpawn(BlockState state) {
		return (int) Math.ceil((double) state.getValue(RADIUS) / 2);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		boolean flag = fluidstate.is(Fluids.WATER);
		return super.getStateForPlacement(context).setValue(WATERLOGGED, flag);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos facingPos, BlockState facingState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			access.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(reader));
		}

		return super.updateShape(state, reader, access, pos, direction, facingPos, facingState, random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(WATERLOGGED));
	}
}
