package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.IceBlock;
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
import twilightforest.client.particle.data.LeafParticleData;
import twilightforest.network.SpawnFallenLeafFromPacket;

public class FallenLeavesBlock extends TFPlantBlock {

	public static final int MAX_HEIGHT = 8;
	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
	public static final MapCodec<FallenLeavesBlock> CODEC = simpleCodec(FallenLeavesBlock::new);
	protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.2D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	public FallenLeavesBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(LAYERS, 1));
	}

	@Override
	protected MapCodec<? extends BushBlock> codec() {
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

			int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), level, pos, 0);
			int r = Mth.clamp(((color >> 16) & 0xFF) + random.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int g = Mth.clamp(((color >> 8) & 0xFF) + random.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int b = Mth.clamp((color & 0xFF) + random.nextInt(0x22) - 0x11, 0x00, 0xFF);
			level.addParticle(new LeafParticleData(r, g, b), pos.getX() + random.nextFloat(), pos.getY() + dist - 0.25F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		super.entityInside(state, level, pos, entity);
		if (state.getValue(LAYERS) > 2) {
			entity.makeStuckInBlock(state, new Vec3(1.0D - (0.05D * (state.getValue(LAYERS) - 2)), 1.0D, 1.0D - (0.05D * (state.getValue(LAYERS) - 2))));
		}
		if (entity instanceof LivingEntity && (entity.getDeltaMovement().x() != 0 || entity.getDeltaMovement().z() != 0) && level.getRandom().nextBoolean()) {
			if (level.isClientSide()) {
				int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), level, pos, 0);
				int r = Mth.clamp(((color >> 16) & 0xFF) + level.getRandom().nextInt(0x22) - 0x11, 0x00, 0xFF);
				int g = Mth.clamp(((color >> 8) & 0xFF) + level.getRandom().nextInt(0x22) - 0x11, 0x00, 0xFF);
				int b = Mth.clamp((color & 0xFF) + level.getRandom().nextInt(0x22) - 0x11, 0x00, 0xFF);
				level.addParticle(new LeafParticleData(r, g, b),
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
