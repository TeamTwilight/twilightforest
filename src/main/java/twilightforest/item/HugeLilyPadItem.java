package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WaterLilyBlockItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import twilightforest.block.HugeLilyPadBlock;

import static twilightforest.block.HugeLilyPadBlock.FACING;
import static twilightforest.block.HugeLilyPadBlock.PIECE;
import static twilightforest.enums.HugeLilypadPiece.NE;
import static twilightforest.enums.HugeLilypadPiece.NW;
import static twilightforest.enums.HugeLilypadPiece.SE;
import static twilightforest.enums.HugeLilypadPiece.SW;

import net.minecraft.world.item.Item.Properties;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;

public class HugeLilyPadItem extends WaterLilyBlockItem {

	public HugeLilyPadItem(HugeLilyPadBlock block, Properties props) {
		super(block, props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		HitResult raytraceresult = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
		if (raytraceresult.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(itemstack);
		} else {
			if (raytraceresult.getType() == HitResult.Type.BLOCK) {
				BlockHitResult blockraytraceresult = (BlockHitResult)raytraceresult;
				BlockPos blockpos = blockraytraceresult.getBlockPos();
				Direction direction = blockraytraceresult.getDirection();
				if (!world.mayInteract(player, blockpos) || !player.mayUseItemAt(blockpos.relative(direction), direction, itemstack)
								// TF - check east, south, southeast as well
								|| !world.mayInteract(player, blockpos.east()) || !player.mayUseItemAt(blockpos.relative(direction).east(), direction, itemstack)
								|| !world.mayInteract(player, blockpos.south()) || !player.mayUseItemAt(blockpos.relative(direction).south(), direction, itemstack)
								|| !world.mayInteract(player, blockpos.east().south()) || !player.mayUseItemAt(blockpos.relative(direction).east().south(), direction, itemstack)
				) {
					return InteractionResultHolder.fail(itemstack);
				}

				BlockPos blockpos1 = blockpos.above();
				BlockState blockstate = world.getBlockState(blockpos);
				Material material = blockstate.getMaterial();
				FluidState ifluidstate = world.getFluidState(blockpos);
				if ((ifluidstate.getType() == Fluids.WATER || material == Material.ICE) && world.isEmptyBlock(blockpos1)
								// TF - check east, south, southeast as well
								&& (world.getFluidState(blockpos.east()).getType() == Fluids.WATER || world.getBlockState(blockpos.east()).getMaterial() == Material.ICE) && world.isEmptyBlock(blockpos1.east())
								&& (world.getFluidState(blockpos.south()).getType() == Fluids.WATER || world.getBlockState(blockpos.south()).getMaterial() == Material.ICE) && world.isEmptyBlock(blockpos1.south())
								&& (world.getFluidState(blockpos.east().south()).getType() == Fluids.WATER || world.getBlockState(blockpos.east().south()).getMaterial() == Material.ICE) && world.isEmptyBlock(blockpos1.east().south())
				) {
					// TF - use our own block. dispense with the blocksnapshot stuff for now due to complexity. FIXME: Implement it
					final BlockState lilypad = getBlock().defaultBlockState().setValue(FACING, player.getDirection());
					world.setBlock(blockpos1, lilypad.setValue(PIECE, NW), 11);
					world.setBlock(blockpos1.east(), lilypad.setValue(PIECE, NE), 11);
					world.setBlock(blockpos1.east().south(), lilypad.setValue(PIECE, SE), 11);
					world.setBlock(blockpos1.south(), lilypad.setValue(PIECE, SW), 11);

					if (player instanceof ServerPlayer) {
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos1, itemstack);
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos1.east(), itemstack);
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos1.east().south(), itemstack);
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos1.south(), itemstack);
					}

					if (!player.getAbilities().instabuild) {
						itemstack.shrink(1);
					}

					player.awardStat(Stats.ITEM_USED.get(this));
					world.playSound(player, blockpos, SoundEvents.LILY_PAD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
					return InteractionResultHolder.success(itemstack);
				}
			}

			return InteractionResultHolder.fail(itemstack);
		}
	}
}
