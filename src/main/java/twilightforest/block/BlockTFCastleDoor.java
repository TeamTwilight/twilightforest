package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.core.jmx.Server;
import twilightforest.TFSounds;
import twilightforest.client.particle.ParticleAnnihilate;
import twilightforest.client.particle.TFParticleType;
import twilightforest.world.ChunkGeneratorTwilightBase;
import twilightforest.world.TFGenerationSettings;

import java.util.Random;

public class BlockTFCastleDoor extends Block {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	public static final BooleanProperty VANISHED = BooleanProperty.create("vanish");

	private static final VoxelShape REAPPEARING_BB = VoxelShapes.create(new AxisAlignedBB(0.375F, 0.375F, 0.375F, 0.625F, 0.625F, 0.625F));

	public BlockTFCastleDoor(Properties props) {
		super(props);
		this.setDefaultState(stateContainer.getBaseState().with(ACTIVE, false).with(VANISHED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(ACTIVE, VANISHED);
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return state.get(VANISHED) ? VoxelShapes.empty() : super.getCollisionShape(state, world, pos, context);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return state.get(VANISHED) ? REAPPEARING_BB : super.getShape(state, world, pos, context);
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		return onActivation(world, pos, state);
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!(block instanceof BlockTFCastleDoor) && world.isBlockPowered(pos)) {
			onActivation(world, pos, state);
		}
	}

	private ActionResultType onActivation(World world, BlockPos pos, BlockState state) {

		if (state.get(VANISHED) || state.get(ACTIVE)) return ActionResultType.FAIL;

		if (isBlockLocked(world, pos)) {
			world.playSound(null, pos, TFSounds.DOOR_ACTIVATED, SoundCategory.BLOCKS, 1.0F, 0.3F);
			return ActionResultType.PASS;
		} else {
			changeToActiveBlock(world, pos, state);
			return ActionResultType.SUCCESS;
		}
	}

	private static void changeToActiveBlock(World world, BlockPos pos, BlockState originState) {
		if (originState.getBlock() instanceof BlockTFCastleDoor) {
			world.setBlockState(pos, originState.with(ACTIVE, true));
		}
		world.getPendingBlockTicks().scheduleTick(pos, originState.getBlock(), 2 + world.rand.nextInt(5));
	}

	private static boolean isBlockLocked(World world, BlockPos pos) {
		// check if we are in a structure, and if that structure says that we are locked
		if (!world.isRemote) {
			ChunkGeneratorTwilightBase generator = TFGenerationSettings.getChunkGenerator(world);
			//return generator != null && generator.isStructureLocked(pos, lockIndex);
		}
		return false;
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(VANISHED)) {
			if (state.get(ACTIVE)) {
				world.setBlockState(pos, state.with(VANISHED, false).with(ACTIVE, false));
			} else {
				changeToActiveBlock(world, pos, state);
			}
			playReappearSound(world, pos);
		} else {
			if (state.get(ACTIVE)) {
				world.setBlockState(pos, state.with(VANISHED, true).with(ACTIVE, false));
				world.getPendingBlockTicks().scheduleTick(pos, this, 80);

				playVanishSound(world, pos);

				vanishParticles(world, pos);

				// activate all adjacent inactive doors
				for (Direction e : Direction.values()) {
					checkAndActivateCastleDoor(world, pos.offset(e));
				}
			}
		}
	}

	private static void playVanishSound(World world, BlockPos pos) {
		world.playSound(null, pos, TFSounds.DOOR_VANISH, SoundCategory.BLOCKS, 0.125f, world.rand.nextFloat() * 0.25F + 1.75F);
	}

	private static void playReappearSound(World world, BlockPos pos) {
		world.playSound(null, pos, TFSounds.DOOR_REAPPEAR, SoundCategory.BLOCKS, 0.125f, world.rand.nextFloat() * 0.25F + 1.25F);
	}

	/**
	 * If the targeted block is a vanishing block, activate it
	 */
	public static void checkAndActivateCastleDoor(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof BlockTFCastleDoor && !state.get(VANISHED) && !state.get(ACTIVE) && !isBlockLocked(world, pos)) {
			changeToActiveBlock(world, pos, state);
		}
	}

	private static void vanishParticles(World world, BlockPos pos) {
		Random rand = world.getRandom();
		if(world instanceof ServerWorld) {
			for (int dx = 0; dx < 4; ++dx) {
				for (int dy = 0; dy < 4; ++dy) {
					for (int dz = 0; dz < 4; ++dz) {

						double x = pos.getX() + (dx + 0.5D) / 4;
						double y = pos.getY() + (dy + 0.5D) / 4;
						double z = pos.getZ() + (dz + 0.5D) / 4;

						double speed = rand.nextGaussian() * 0.2D;

						((ServerWorld)world).spawnParticle(TFParticleType.ANNIHILATE.get(), x, y, z, 1, 0, 0, 0, speed);
					}
				}
			}
		}
	}
}
