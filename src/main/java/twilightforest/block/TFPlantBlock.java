package twilightforest.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import twilightforest.client.particle.data.LeafParticleData;
import twilightforest.enums.PlantVariant;
import twilightforest.network.SpawnFallenLeafFromPacket;
import twilightforest.network.TFPacketHandler;

import java.util.Random;

public class TFPlantBlock extends BushBlock implements BonemealableBlock {
	private static final VoxelShape MAYAPPLE_SHAPE = box(4, 0, 4, 13, 6, 13);
	private static final VoxelShape FALLEN_LEAVES_SHAPE = box(0, 0, 0, 16, 1, 16);
	private static final VoxelShape MUSHGLOOM_SHAPE = box(2, 0, 2, 14, 8, 14);
	private static final VoxelShape ROOT_SHAPE = box(2, 0, 2, 14, 16, 14);
	private static final VoxelShape TORCHBERRY_SHAPE = box(1, 2, 1, 15, 16, 15);
	private static final VoxelShape FIDDLEHEAD_SHAPE = box(3, 0, 3, 13, 14, 13);

	public final PlantVariant plantVariant;

	protected TFPlantBlock(PlantVariant plant, BlockBehaviour.Properties props) {
		super(props);
		plantVariant = plant;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockState soil = world.getBlockState(pos.below());

		return switch (plantVariant) {
			case TORCHBERRY, ROOT_STRAND -> TFPlantBlock.canPlaceRootAt(world, pos);
			case FALLEN_LEAVES, MUSHGLOOM, MOSSPATCH -> soil.isFaceSturdy(world, pos, Direction.UP);
			default -> (world.getMaxLocalRawBrightness(pos) >= 3 || world.canSeeSkyFromBelowWater(pos)) && soil.canSustainPlant(world, pos.below(), Direction.UP, this);
		};
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter access, BlockPos pos, CollisionContext context) {
		switch(plantVariant) {
			case MAYAPPLE -> { return MAYAPPLE_SHAPE; }
			case FIDDLEHEAD -> { return FIDDLEHEAD_SHAPE; }
			case MUSHGLOOM -> { return MUSHGLOOM_SHAPE; }
			case TORCHBERRY -> { return TORCHBERRY_SHAPE; }
			case ROOT_STRAND -> { return ROOT_SHAPE; }
			case FALLEN_LEAVES -> { return FALLEN_LEAVES_SHAPE; }
			default -> { return Shapes.block(); }
		}
	}

	public static boolean canPlaceRootAt(LevelReader world, BlockPos pos) {
		BlockState state = world.getBlockState(pos.above());
		if (state.getMaterial() == Material.DIRT || state.getMaterial() == Material.GRASS) {
			// can always hang below dirt blocks
			return true;
		} else {
			return (state.getBlock() == TFBlocks.ROOT_STRAND.get()
					|| state.is(TFBlocks.ROOT_BLOCK.get())
					|| state.is(TFBlocks.LIVEROOT_BLOCK.get())
					|| state.is(TFBlocks.MANGROVE_ROOT.get()));
		}
	}

	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() == this) {
			return switch (plantVariant) {
				case MOSSPATCH, MUSHGLOOM -> PlantType.CAVE;
				default -> PlantType.PLAINS;
			};
		}
		return PlantType.PLAINS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		super.animateTick(state, world, pos, random);

		if (state.getBlock() == TFBlocks.MOSS_PATCH.get() && random.nextInt(10) == 0) {
			world.addParticle(ParticleTypes.MYCELIUM, pos.getX() + random.nextFloat(), pos.getY() + 0.1F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		} else if (state.getBlock() == TFBlocks.FALLEN_LEAVES.get() && random.nextInt(50) == 0) {
			float dist = 10F;
			if (!world.canSeeSkyFromBelowWater(pos)) {
				for (int y = 0; y <= dist; y++)
					if (world.getBlockState(pos.above(y)).getMaterial() == Material.LEAVES) {
						dist = y;
						break;
					}
				if (dist > 10F)
					return;
			}

			int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), world, pos, 0);
			int r = Mth.clamp(((color >> 16) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int g = Mth.clamp(((color >> 8) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int b = Mth.clamp((color & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
			world.addParticle(new LeafParticleData(r, g, b), pos.getX() + random.nextFloat(), pos.getY() + dist - 0.25F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	@Deprecated
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entityIn) {
		super.entityInside(state, world, pos, entityIn);
		if (state.getBlock() == TFBlocks.FALLEN_LEAVES.get() && entityIn instanceof LivingEntity && (entityIn.getDeltaMovement().x() != 0 || entityIn.getDeltaMovement().z() != 0) && RANDOM.nextBoolean()) {
			if(world.isClientSide) {
				int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), world, pos, 0);
				int r = Mth.clamp(((color >> 16) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
				int g = Mth.clamp(((color >> 8) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
				int b = Mth.clamp((color & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
					world.addParticle(new LeafParticleData(r, g, b),
							pos.getX() + world.random.nextFloat(),
							pos.getY(),
							pos.getZ() + world.random.nextFloat(),

							(world.random.nextFloat() * -0.5F) * entityIn.getDeltaMovement().x(),
							world.random.nextFloat() * 0.5F + 0.25F,
							(world.random.nextFloat() * -0.5F) * entityIn.getDeltaMovement().z()
				);
			} else if (world instanceof ServerLevel)
				TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entityIn), new SpawnFallenLeafFromPacket(pos, entityIn.getDeltaMovement()));
		}
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
		return state.getBlock() == TFBlocks.ROOT_STRAND.get() && isBottomOpen(level, pos);
	}

	@Override
	public boolean isBonemealSuccess(Level level, Random random, BlockPos pos, BlockState state) {
		return state.getBlock() == TFBlocks.ROOT_STRAND.get() && isBottomOpen(level, pos);
	}

	@Override
	public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
		if(state.getBlock() == TFBlocks.ROOT_STRAND.get()) {
			BlockPos.MutableBlockPos mutable = pos.mutable();
			do {
				mutable.move(Direction.DOWN);
			} while(level.getBlockState(mutable).is(TFBlocks.ROOT_STRAND.get()));
			if(level.getBlockState(mutable).isAir() || level.getBlockState(mutable).getMaterial().isReplaceable()) {
				level.setBlockAndUpdate(mutable, TFBlocks.ROOT_STRAND.get().defaultBlockState());
			}
		}
	}

	private boolean isBottomOpen(BlockGetter level, BlockPos pos) {
		BlockPos.MutableBlockPos mutable = pos.mutable();
		do {
			mutable.move(Direction.DOWN);
		} while(level.getBlockState(mutable).is(TFBlocks.ROOT_STRAND.get()));

		return level.getBlockState(mutable).isAir() || level.getBlockState(mutable).getMaterial().isReplaceable();
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 100;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 60;
	}
}
