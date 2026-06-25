package twilightforest.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.BlockEvent;
import twilightforest.init.TFDataMaps;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;
import twilightforest.util.WorldUtil;

public class CrumbleHornItem extends Item {

	public CrumbleHornItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		player.playSound(TFSounds.QUEST_RAM_AMBIENT.get(), 1.0F, 0.8F);
		return InteractionResult.CONSUME;
	}

	@Override
	public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
		if (count > 10 && count % 5 == 0 && level instanceof ServerLevel serverLevel) {
			this.doCrumble(serverLevel, living, stack);
			serverLevel.playSound(null, living.getX(), living.getY(), living.getZ(), TFSounds.QUEST_RAM_AMBIENT.get(), living.getSoundSource(), 1.0F, 0.8F);
		}
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.TOOT_HORN;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 72000;
	}

	@Override
	public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
		return oldStack.getItem() == newStack.getItem();
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || newStack.getItem() != oldStack.getItem();
	}

	private void doCrumble(ServerLevel serverLevel, LivingEntity living, ItemStack stack) {
		final double centerDistance = 3.0D;
		final int radius = 2;

		Vec3 eyePosition = living.getEyePosition();
		Vec3 lookVec = living.getLookAngle().scale(centerDistance);
		BlockPos center = BlockPos.containing(eyePosition.add(lookVec));

		AABB crumbleBox = AABB.encapsulatingFullBlocks(center.offset(-radius, -radius, -radius), center.offset(radius - 1, radius - 1, radius - 1));

		this.crumbleBlocksInAABB(serverLevel, living, crumbleBox, stack);
	}

	private void crumbleBlocksInAABB(ServerLevel serverLevel, LivingEntity living, AABB box, ItemStack stack) {
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			if (this.crumbleBlock(serverLevel, living, pos)) {
				if (living instanceof ServerPlayer player) {
					player.awardStat(TFStats.BLOCKS_CRUMBLED.get());
				}
				stack.hurtAndBreak(1, living,living.getUsedItemHand());
				if (stack.getDamageValue() >= stack.getMaxDamage()) break;
			}
		}
	}

	private boolean crumbleBlock(ServerLevel serverLevel, LivingEntity living, BlockPos pos) {
		BlockState state = serverLevel.getBlockState(pos);
		Block block = state.getBlock();
		var crumbleMap = block.builtInRegistryHolder().getData(TFDataMaps.CRUMBLE_HORN);

		if (state.isAir() || crumbleMap == null) return false;

		if (living instanceof Player) {
			if (NeoForge.EVENT_BUS.post(new net.neoforged.neoforge.event.level.block.BreakBlockEvent(serverLevel, pos, state, (Player) living)).isCanceled())
				return false;
		}

		if (crumbleMap.result() == Blocks.AIR) {
			if (serverLevel.getRandom().nextFloat() < crumbleMap.chanceToCrumble()) {
				if (living instanceof Player player) {
					if (block.canHarvestBlock(state, serverLevel, pos, player)) {
						serverLevel.removeBlock(pos, false);
						block.playerDestroy(serverLevel, player, pos, state, serverLevel.getBlockEntity(pos), ItemStack.EMPTY);
						serverLevel.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
						if (player instanceof ServerPlayer) {
							player.awardStat(Stats.ITEM_USED.get(this));
						}
						return true;
					}
				} else if (EventHooks.canEntityGrief(serverLevel, living)) {
					serverLevel.destroyBlock(pos, true);
					return true;
				}
			}
		} else {
			if (serverLevel.getRandom().nextFloat() < crumbleMap.chanceToCrumble()) {
				serverLevel.setBlock(pos, crumbleMap.result().withPropertiesOf(state), Block.UPDATE_ALL);
				serverLevel.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
				if (living instanceof ServerPlayer player) {
					player.awardStat(Stats.ITEM_USED.get(this));
				}
				return true;
			}
		}
		return false;
	}
}