package twilightforest.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDamageTypes;

import java.util.List;

public class KnightmetalBlock extends Block implements SimpleWaterloggedBlock {
	private static final MutableComponent TOOLTIP = Component.translatable("block.twilightforest.knightmetal_block.desc").withStyle(ChatFormatting.GRAY);

	private static final VoxelShape SHAPE = Shapes.create(new AABB(1 / 16F, 1 / 16F, 1 / 16F, 15 / 16F, 15 / 16F, 15 / 16F));
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final float BLOCK_DAMAGE = 4;

	public KnightmetalBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return defaultBlockState().setValue(WATERLOGGED, fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			accessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		}

		return super.updateShape(state, facing, facingState, accessor, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED);
	}

	@Nullable
	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter getter, BlockPos pos, @Nullable Mob entity) {
		return BlockPathTypes.DAMAGE_OTHER;
	}

	@Override
	@Deprecated
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		entity.hurt(TFDamageTypes.getDamageSource(level, TFDamageTypes.KNIGHTMETAL), BLOCK_DAMAGE);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(TOOLTIP);
	}
}
