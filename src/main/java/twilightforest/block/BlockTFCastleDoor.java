package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
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
import net.minecraftforge.fml.network.PacketDistributor;
import twilightforest.TFSounds;
import twilightforest.network.PacketAnnihilateBlock;
import twilightforest.network.TFPacketHandler;
import twilightforest.world.ChunkGeneratorTFBase;
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

	//TODO: Material cannot be dynamic
//	@Override
//	@Deprecated
//	public Material getMaterial(BlockState state) {
//		return state.get(VANISHED) ? Material.GLASS : super.getMaterial(state);
//	}

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
			ChunkGeneratorTFBase generator = TFGenerationSettings.getChunkGenerator(world);
			return generator != null /*&& generator.isStructureLocked(pos, lockIndex)*/;
		}
		return false;
	}

	//TODO: Does not exist. Must be specified in scheduleTick() directly
//	@Override
//	public int tickRate() {
//		return 5;
//	}

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

				this.sendAnnihilateBlockPacket(world, pos);

				// activate all adjacent inactive doors
				for (Direction e : Direction.values()) {
					checkAndActivateCastleDoor(world, pos.offset(e));
				}
			}
		}
	}

	private void sendAnnihilateBlockPacket(World world, BlockPos pos) {
		PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 64, world.getDimensionKey()); //RegistryKey<World>
		TFPacketHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> targetPoint), new PacketAnnihilateBlock(pos));
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

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(ACTIVE)) {
			for (int i = 0; i < 1; ++i) {
				//this.sparkle(world, x, y, z, random);
			}
		}
	}

	// [VanillaCopy] BlockRedStoneOre.spawnParticles with own rand
	//@SuppressWarnings("unused")
//	private void sparkle(World worldIn, BlockPos pos, Random rand) {
//		Random random = rand;
//		double d0 = 0.0625D;
//
//		for (int i = 0; i < 6; ++i) {
//			double d1 = (double) ((float) pos.getX() + random.nextFloat());
//			double d2 = (double) ((float) pos.getY() + random.nextFloat());
//			double d3 = (double) ((float) pos.getZ() + random.nextFloat());
//
//			if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube()) {
//				d2 = (double) pos.getY() + 0.0625D + 1.0D;
//			}
//
//			if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube()) {
//				d2 = (double) pos.getY() - 0.0625D;
//			}
//
//			if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube()) {
//				d3 = (double) pos.getZ() + 0.0625D + 1.0D;
//			}
//
//			if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube()) {
//				d3 = (double) pos.getZ() - 0.0625D;
//			}
//
//			if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube()) {
//				d1 = (double) pos.getX() + 0.0625D + 1.0D;
//			}
//
//			if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube()) {
//				d1 = (double) pos.getX() - 0.0625D;
//			}
//
//			if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D || d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1)) {
//				worldIn.spawnParticle(ParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D, new int[0]);
//			}
//		}
//	}
}
