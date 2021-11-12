package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.WaterLilyBlockItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;

public class HugeWaterLilyItem extends WaterLilyBlockItem {

	public HugeWaterLilyItem(Block block, Properties props) {
		super(block, props);
	}

	// [VanillaCopy] ItemLilyPad.onItemRightClick, edits noted
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
				if (!world.mayInteract(player, blockpos) || !player.mayUseItemAt(blockpos.relative(direction), direction, itemstack)) {
					return InteractionResultHolder.fail(itemstack);
				}

				BlockPos blockpos1 = blockpos.above();
				BlockState blockstate = world.getBlockState(blockpos);
				Material material = blockstate.getMaterial();
				FluidState ifluidstate = world.getFluidState(blockpos);
				if ((ifluidstate.getType() == Fluids.WATER || material == Material.ICE) && world.isEmptyBlock(blockpos1)) {

					// special case for handling block placement with water lilies
					net.minecraftforge.common.util.BlockSnapshot blocksnapshot = net.minecraftforge.common.util.BlockSnapshot.create(world.dimension(), world, blockpos1);
					// TF - getBlock() instead of hardcoded lilypad
					world.setBlock(blockpos1, getBlock().defaultBlockState(), 11);
					if (net.minecraftforge.event.ForgeEventFactory.onBlockPlace(player, blocksnapshot, net.minecraft.core.Direction.UP)) {
						blocksnapshot.restore(true, false);
						return InteractionResultHolder.fail(itemstack);
					}

					if (player instanceof ServerPlayer) {
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos1, itemstack);
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
