package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;

public class TrollRootBlock extends Block {

	protected static final VoxelShape AABB = Shapes.create(new AABB(0.1, 0.0, 0.1, 0.9, 1.0, 0.9));

	public TrollRootBlock(Properties properties) {
		super(properties);
	}

	public static boolean canPlaceRootBelow(LevelReader reader, BlockPos pos) {
		BlockState state = reader.getBlockState(pos);

		return state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(TFBlocks.TROLLVIDR) || state.is(TFBlocks.TROLLBER) || state.is(TFBlocks.UNRIPE_TROLLBER);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (state.is(TFBlocks.TROLLBER)) {
			level.setBlock(pos, TFBlocks.TROLLVIDR.get().defaultBlockState(), Block.UPDATE_CLIENTS);
			level.playSound(null, pos, TFSounds.PICKED_TORCHBERRIES.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			ItemEntity torchberries = new ItemEntity(level, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, new ItemStack(TFItems.TORCHBERRIES.get()));
			level.addFreshEntity(torchberries);
			if (player instanceof ServerPlayer) player.awardStat(TFStats.TORCHBERRIES_HARVESTED.get());
			return InteractionResult.SUCCESS;
		}
		return super.useWithoutItem(state, level, pos, player, result);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return canPlaceRootBelow(reader, pos.above());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos facingPos, BlockState facingState, RandomSource random) {
		if (direction == Direction.UP) {
			return this.canSurvive(state, reader, pos) ? state : Blocks.AIR.defaultBlockState();
		}
		return state;
	}
}
