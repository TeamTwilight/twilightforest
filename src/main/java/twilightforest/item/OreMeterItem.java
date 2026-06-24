package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;
import twilightforest.components.item.OreScannerComponent;
import twilightforest.components.item.OreScannerData;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFSounds;
import twilightforest.tags.TFBlockTags;

import java.util.function.Consumer;

public class OreMeterItem extends Item {
	public static final int MAX_CHUNK_SEARCH_RANGE = 2;
	public static final int LOAD_TIME = 50;

	public OreMeterItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (level.isClientSide() || !stack.has(TFDataComponents.ORE_SCANNING))
			return;

		OreScannerComponent newScan = stack.get(TFDataComponents.ORE_SCANNING).tickScan(level);

		if (newScan.isEmpty()) {
			stack.remove(TFDataComponents.ORE_SCANNING);
			return;
		}

		if (!newScan.isFinished()) {
			stack.set(TFDataComponents.ORE_LOADING, newScan.getTickProgress());
			stack.set(TFDataComponents.ORE_SCANNING, newScan);
			return;
		}

		// Scanning completed, save results to item
		stack.set(TFDataComponents.ORE_DATA, OreScannerData.create(newScan.getResults(stack.get(TFDataComponents.ORE_FILTER)), newScan.centerChunkPos(), newScan.getVolume(level), getRange(stack)));

		stack.remove(TFDataComponents.ORE_LOADING);
		stack.remove(TFDataComponents.ORE_SCANNING);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		// FakePlayers should never be allowed to use the Ore Meter
		if (player.getClass() != ServerPlayer.class) return InteractionResult.FAIL;

		//if we're in the "loading" state don't try to run any logic
		if (isLoading(stack)) return InteractionResult.PASS;

		//if we're not crouching, put the ore meter into its "loading" state
		if (!player.isSecondaryUseActive()) {
			return beginScanning(level, player, stack);
		} else {
			return toggleRange(level, player, stack);
		}
	}

	private static InteractionResult beginScanning(Level level, Player player, ItemStack stack) {
		if (!level.isClientSide()) {
			int range = getRange(stack);

			// 50 base ticks plus 25 additional ticks for each range increment
			// Range 0: 50 ticks
			// Range 1: 75 ticks
			// Range 2: 100 ticks
			// It's not an exponent growth to match the increase in chunks, but range is capped at 2 anyway
			int scanTime = LOAD_TIME + range * 25;

			OreScannerComponent data = OreScannerComponent.scanFromCenter(player.blockPosition(), range, scanTime);
			stack.set(TFDataComponents.ORE_SCANNING, data);
		}

		level.playSound(player, player.blockPosition(), TFSounds.ORE_METER_CRACKLE.get(), SoundSource.PLAYERS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);

		return InteractionResult.PASS;
	}

	private static InteractionResult toggleRange(Level level, Player player, ItemStack stack) {
		//if we're crouching and not targeting a block, change the ore meter range instead
		HitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
		if (result.getType() == HitResult.Type.MISS) {
			if (!level.isClientSide()) {
				int newRange = Mth.positiveModulo(getRange(stack) + 1, MAX_CHUNK_SEARCH_RANGE + 1);

				stack.set(TFDataComponents.ORE_RANGE, newRange);
				player.sendOverlayMessage(Component.translatable("misc.twilightforest.ore_meter_new_range", newRange));
				level.playSound(null, player.blockPosition(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.PLAYERS, 0.25F, 0.75F + (newRange * 0.1F));
			}
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack stack = context.getItemInHand();
		//if we're crouching and targeting a block, attempt to save the block as the focused block
		if (context.isSecondaryUseActive()) {
			BlockState state = context.getLevel().getBlockState(context.getClickedPos());
			if (state.is(TFBlockTags.ORE_METER_TARGETABLE)) {
				stack.set(TFDataComponents.ORE_FILTER, state.getBlock());
				context.getPlayer().sendOverlayMessage(Component.translatable("misc.twilightforest.ore_meter_set_block", Component.translatable(state.getBlock().getDescriptionId())));
				context.getLevel().playSound(context.getPlayer(), context.getPlayer().blockPosition(), TFSounds.ORE_METER_TARGET_BLOCK.get(), SoundSource.PLAYERS, 0.5F, context.getLevel().getRandom().nextFloat() * 0.1F + 0.9F);
				return InteractionResult.SUCCESS;
			}
		}
		return super.useOn(context);
	}

	//don't make the player hand spazz out when the NBT changes
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || newStack.getItem() != oldStack.getItem();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		Block block = stack.get(TFDataComponents.ORE_FILTER);

		if (block != null)
			builder.accept(Component.translatable("misc.twilightforest.ore_meter_targeted_block", block.getDescriptionId()).withStyle(ChatFormatting.GRAY));

		super.appendHoverText(stack, context, display, builder, flag);
	}

	public static boolean isLoading(ItemStack stack) {
		return stack.has(TFDataComponents.ORE_LOADING);
	}

	public static int getLoadProgress(ItemStack stack) {
		return stack.getOrDefault(TFDataComponents.ORE_LOADING, 0);
	}

	public static Integer getRange(ItemStack stack) {
		return stack.getOrDefault(TFDataComponents.ORE_RANGE, 1);
	}

}