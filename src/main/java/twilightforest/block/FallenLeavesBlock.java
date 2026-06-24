package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFParticleType;
import twilightforest.network.SpawnFallenLeafFromPacket;

public class FallenLeavesBlock extends TFPlantBlock {

	public static final int MAX_HEIGHT = 8;
	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
	public static final MapCodec<FallenLeavesBlock> CODEC = simpleCodec(FallenLeavesBlock::new);
	protected static final VoxelShape[] SHAPE_BY_LAYER = Util.make(new VoxelShape[9], arr -> {
		arr[0] = Block.box(0.0, 0.0, 0.0, 16.0, 0.2, 16.0);
		for (int i = 1; i <= 8; i++) {
			double height = i * 2.0;
			arr[i] = Block.box(0.0, 0.0, 0.0, 16.0, height, 16.0);
		}
	});

	public FallenLeavesBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(LAYERS, 1));
	}

	@Override
	public MapCodec<? extends VegetationBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return reader.getBlockState(pos.below()).isFaceSturdy(reader, pos, Direction.UP) || reader.getFluidState(pos.below()).getType() == Fluids.WATER;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pos, CollisionContext pContext) {
		return SHAPE_BY_LAYER[state.getValue(LAYERS) - 1];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return Shapes.empty();
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
		return super.mayPlaceOn(state, getter, pos) || ((getter.getFluidState(pos).getType() == Fluids.WATER || state.getBlock() instanceof IceBlock) && getter.getFluidState(pos.above()).getType() == Fluids.EMPTY);
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		int i = state.getValue(LAYERS);
		boolean waterBelow = context.getLevel().getBlockState(context.getClickedPos().below()).liquid();

		if (!waterBelow) {
			if (context.getItemInHand().is(this.asItem()) && i < MAX_HEIGHT) {
				if (context.replacingClickedOnBlock()) {
					return context.getClickedFace() == Direction.UP;
				} else {
					return true;
				}
			} else {
				return i == 1;
			}
		}
		return false;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());

		if (blockstate.is(this)) {
			int i = blockstate.getValue(LAYERS);
			return blockstate.setValue(LAYERS, Math.min(MAX_HEIGHT, i + 1));
		} else {
			return super.getStateForPlacement(context);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LAYERS);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		super.animateTick(state, level, pos, random);
		if (random.nextInt(50) == 0) {
			float dist = 10F;
			if (!level.canSeeSkyFromBelowWater(pos)) {
				for (int y = 0; y <= dist; y++)
					if (level.getBlockState(pos.above(y)).is(BlockTags.LEAVES)) {
						dist = y;
						break;
					}
				if (dist > 10F)
					return;
			}

			level.addParticle(ColorParticleOption.create(TFParticleType.FALLEN_LEAF.get(), level.getClientLeafTintColor(pos)), pos.getX() + random.nextFloat(), pos.getY() + dist - 0.25F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
		if (state.getValue(LAYERS) > 2) {
			entity.makeStuckInBlock(state, new Vec3(1.0D - (0.05D * (state.getValue(LAYERS) - 2)), 1.0D, 1.0D - (0.05D * (state.getValue(LAYERS) - 2))));
		}
		if (entity instanceof LivingEntity && (entity.getDeltaMovement().x() != 0 || entity.getDeltaMovement().z() != 0) && level.getRandom().nextBoolean()) {
			if (level.isClientSide()) {
				level.addParticle(ColorParticleOption.create(TFParticleType.FALLEN_LEAF.get(), level.getClientLeafTintColor(pos)),
					pos.getX() + level.getRandom().nextFloat(),
					pos.getY() + ((2F / 16F) * (state.getValue(LAYERS) - 1)),
					pos.getZ() + level.getRandom().nextFloat(),
					(level.getRandom().nextFloat() * -0.5F) * entity.getDeltaMovement().x(),
					level.getRandom().nextFloat() * 0.5F + 0.25F,
					(level.getRandom().nextFloat() * -0.5F) * entity.getDeltaMovement().z()
				);
			} else if (level instanceof ServerLevel)
				PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new SpawnFallenLeafFromPacket(pos, entity.getDeltaMovement()));
		}
	}
}
