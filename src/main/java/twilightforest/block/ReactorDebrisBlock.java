package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.ReactorDebrisBlockEntity;
import twilightforest.init.TFBlockEntities;

public class ReactorDebrisBlock extends BaseEntityBlock {

	public VoxelShape SHAPE;
	public static final MapCodec<ReactorDebrisBlock> CODEC = simpleCodec(ReactorDebrisBlock::new);

	public ReactorDebrisBlock(Properties properties) {
		super(properties);
		SHAPE = Shapes.empty();
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (level.getBlockEntity(pos) instanceof ReactorDebrisBlockEntity reactorDebrisBlockEntity)
			return reactorDebrisBlockEntity.SHAPE;
		return ReactorDebrisBlockEntity.calculateVoxelShape();
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.block();
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
		if(!level.isClientSide() && level.getBlockEntity(pos) instanceof ReactorDebrisBlockEntity blockEntity) {  //  blockEntity should always be
			blockEntity.randomizeDimensions();
			blockEntity.randomizeTextures();
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.is(this))
			level.destroyBlock(pos, false);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.REACTOR_DEBRIS.get(), ReactorDebrisBlockEntity::tick);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new ReactorDebrisBlockEntity(blockPos, blockState);
	}

	@Override
	public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
		return true;  // Actually disables landing particle effects after fall.
	}
}
