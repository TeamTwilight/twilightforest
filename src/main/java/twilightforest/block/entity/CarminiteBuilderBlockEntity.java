package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.BuilderBlock;
import twilightforest.block.TranslucentBuiltBlock;
import twilightforest.enums.TowerDeviceVariant;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

public class CarminiteBuilderBlockEntity extends BlockEntity {
	private static final int RANGE = 16;
	private final BlockState blockBuiltState = TFBlocks.BUILT_BLOCK.get().defaultBlockState().setValue(TranslucentBuiltBlock.ACTIVE, false);
	public boolean makingBlocks = false;
	private int ticksRunning = 0;
	private int blockedCounter = 0;
	private int ticksStopped = 0;
	private int blocksMade = 0;
	@Nullable
	private BlockPos lastBlockCoords;
	@Nullable
	private Player trackedPlayer;

	public CarminiteBuilderBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.TOWER_BUILDER.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, CarminiteBuilderBlockEntity te) {
		if (!level.isClientSide() && te.makingBlocks) {
			// if we are not tracking the nearest player, start tracking them
			if (te.trackedPlayer == null) {
				te.trackedPlayer = te.findClosestValidPlayer();
			}

			// find player facing
			Direction nextFacing = te.findNextFacing();

			++te.ticksRunning;

			// if we are at the half second marker, make a block and advance the block cursor
			if (te.ticksRunning % 10 == 0 && te.lastBlockCoords != null && nextFacing != null) {
				BlockPos nextPos = te.lastBlockCoords.relative(nextFacing);

				// make a block
				if (te.blocksMade <= RANGE && level.isEmptyBlock(nextPos)) {
					level.setBlock(nextPos, te.blockBuiltState, Block.UPDATE_ALL);

					level.playSound(null, pos, TFSounds.BUILDER_CREATE.get(), SoundSource.BLOCKS, 0.75F, 1.2F);

					te.lastBlockCoords = nextPos;

					te.blockedCounter = 0;
					te.blocksMade++;
				} else {
					te.blockedCounter++;
				}
			}

			// if we're blocked for more than a second, shut down block making
			if (te.blockedCounter > 0) {
				te.makingBlocks = false;
				te.trackedPlayer = null;
				te.ticksStopped = 0;
			}
		} else if (!level.isClientSide() && !te.makingBlocks) {
			te.trackedPlayer = null;
			if (++te.ticksStopped == 60) {
				// force the builder back into an inactive state
				level.setBlockAndUpdate(pos, state.setValue(BuilderBlock.STATE, TowerDeviceVariant.BUILDER_TIMEOUT));
				level.scheduleTick(pos, state.getBlock(), 4);
			}
		}
	}

	/**
	 * Start building stuffs
	 */
	public void startBuilding() {
		this.makingBlocks = true;
		resetStats();
	}

	public void resetStats() {
		this.blocksMade = 0;
		this.lastBlockCoords = getBlockPos();
		this.ticksStopped = 0;
		this.blockedCounter = 0;
	}

	@Nullable
	private Direction findNextFacing() {
		if (this.trackedPlayer != null) {
			// check up and down
			int pitch = Mth.floor(trackedPlayer.getXRot() * 4.0F / 360.0F + 1.5D) & 3;

			if (pitch == 0) {
				return Direction.UP;
			} else if (pitch == 2) {
				return Direction.DOWN;
			} else {
				return trackedPlayer.getDirection();
			}
		}

		return null;
	}

	/**
	 * Who is the closest player? Used to find which player we should track when building
	 */
	@Nullable
	private Player findClosestValidPlayer() {
		return this.getLevel().getNearestPlayer(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, 16, false);
	}
}
