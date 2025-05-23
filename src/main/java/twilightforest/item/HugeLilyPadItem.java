package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import twilightforest.block.HugeLilyPadBlock;

import static twilightforest.block.HugeLilyPadBlock.FACING;
import static twilightforest.block.HugeLilyPadBlock.PIECE;
import static twilightforest.enums.HugeLilypadPiece.*;

public class HugeLilyPadItem extends PlaceOnWaterBlockItem {

	public HugeLilyPadItem(HugeLilyPadBlock block, Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		BlockHitResult raytraceresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
		if (raytraceresult.getType() == HitResult.Type.MISS) {
			return InteractionResult.PASS;
		} else {
			if (raytraceresult.getType() == HitResult.Type.BLOCK) {
				BlockPos blockpos = raytraceresult.getBlockPos();
				Direction direction = raytraceresult.getDirection();
				if (!level.mayInteract(player, blockpos) || !player.mayUseItemAt(blockpos.relative(direction), direction, itemstack)
					// TF - check east, south, southeast as well
					|| !level.mayInteract(player, blockpos.east()) || !player.mayUseItemAt(blockpos.relative(direction).east(), direction, itemstack)
					|| !level.mayInteract(player, blockpos.south()) || !player.mayUseItemAt(blockpos.relative(direction).south(), direction, itemstack)
					|| !level.mayInteract(player, blockpos.east().south()) || !player.mayUseItemAt(blockpos.relative(direction).east().south(), direction, itemstack)
				) {
					return InteractionResult.FAIL;
				}

				BlockPos blockpos1 = blockpos.above();
				BlockState blockstate = level.getBlockState(blockpos);
				FluidState ifluidstate = level.getFluidState(blockpos);
				if ((ifluidstate.getType() == Fluids.WATER || blockstate.is(BlockTags.ICE)) && level.isEmptyBlock(blockpos1)
					// TF - check east, south, southeast as well
					&& (level.getFluidState(blockpos.east()).getType() == Fluids.WATER || level.getBlockState(blockpos.east()).is(BlockTags.ICE)) && level.isEmptyBlock(blockpos1.east())
					&& (level.getFluidState(blockpos.south()).getType() == Fluids.WATER || level.getBlockState(blockpos.south()).is(BlockTags.ICE)) && level.isEmptyBlock(blockpos1.south())
					&& (level.getFluidState(blockpos.east().south()).getType() == Fluids.WATER || level.getBlockState(blockpos.east().south()).is(BlockTags.ICE)) && level.isEmptyBlock(blockpos1.east().south())
				) {
					// TF - use our own block. dispense with the blocksnapshot stuff for now due to complexity. FIXME: Implement it
					final BlockState lilypad = getBlock().defaultBlockState().setValue(FACING, player.getDirection());
					level.setBlock(blockpos1, lilypad.setValue(PIECE, NW), Block.UPDATE_ALL_IMMEDIATE);
					level.setBlock(blockpos1.east(), lilypad.setValue(PIECE, NE), Block.UPDATE_ALL_IMMEDIATE);
					level.setBlock(blockpos1.east().south(), lilypad.setValue(PIECE, SE), Block.UPDATE_ALL_IMMEDIATE);
					level.setBlock(blockpos1.south(), lilypad.setValue(PIECE, SW), Block.UPDATE_ALL_IMMEDIATE);

					if (player instanceof ServerPlayer) {
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos1, itemstack);
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos1.east(), itemstack);
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos1.east().south(), itemstack);
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos1.south(), itemstack);
					}

					itemstack.consume(1, player);

					player.awardStat(Stats.ITEM_USED.get(this));
					level.playSound(player, blockpos, SoundEvents.LILY_PAD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
					return InteractionResult.SUCCESS;
				}
			}

			return InteractionResult.FAIL;
		}
	}
}