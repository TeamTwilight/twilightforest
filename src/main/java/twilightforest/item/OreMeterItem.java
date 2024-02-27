package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.capabilities.OreScannerAttachment;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFSounds;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OreMeterItem extends Item {
	public static final int MAX_CHUNK_SEARCH_RANGE = 2;
	public static final int LOAD_TIME = 50;
	public static final String NBT_SCAN_DATA = "ScanData";

	public OreMeterItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean held) {
		if (level.isClientSide() || !stack.hasData(TFDataAttachments.ORE_SCANNER)) return;

		OreScannerAttachment data = stack.getData(TFDataAttachments.ORE_SCANNER);

		if (data.isEmpty()) {
			stack.removeData(TFDataAttachments.ORE_SCANNER);
			return;
		}

		if (!data.tickScan(level)) {
			getOrCreateScanData(stack).putInt("Loading", data.getTickProgress());
			return;
		}

		CompoundTag scanData = getScanData(stack);
		if (scanData != null) scanData.remove("Loading");

		saveScanInfo(stack, data.getResults(getAssignedBlock(stack)), data.centerChunkPos().toLong(), data.getVolume(level));

		stack.removeData(TFDataAttachments.ORE_SCANNER);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		//if we're in the "loading" state don't try to run any logic
		if (isLoading(stack)) return InteractionResultHolder.pass(stack);

		//if we're not crouching, put the ore meter into its "loading" state
		if (!player.isSecondaryUseActive()) {
			return beginScanning(level, player, stack);
		} else {
			return toggleRange(level, player, stack);
		}
	}

	@NotNull
	private static InteractionResultHolder<ItemStack> beginScanning(Level level, Player player, ItemStack stack) {
		if (!level.isClientSide) {
			int range = getRange(stack);
			OreScannerAttachment data = OreScannerAttachment.scanFromCenter(player.blockPosition(), range, LOAD_TIME);
			stack.setData(TFDataAttachments.ORE_SCANNER, data);
		}

		level.playSound(player, player.blockPosition(), TFSounds.ORE_METER_CRACKLE.get(), SoundSource.PLAYERS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);

		return InteractionResultHolder.pass(stack);
	}

	@NotNull
	private static InteractionResultHolder<ItemStack> toggleRange(Level level, Player player, ItemStack stack) {
		//if we're crouching and not targeting a block, change the ore meter range instead
		HitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
		if (result.getType() == HitResult.Type.MISS) {
			if (!level.isClientSide) {
				int newRange = Mth.positiveModulo(getRange(stack) + 1, MAX_CHUNK_SEARCH_RANGE + 1);

				getOrCreateScanData(stack).putInt("Range", newRange);
				player.displayClientMessage(Component.translatable("misc.twilightforest.ore_meter_new_range", newRange), true);
				level.playSound(null, player.blockPosition(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.PLAYERS, 0.25F, 0.75F + (newRange * 0.1F));
			}
			return InteractionResultHolder.success(stack);
		}

		return InteractionResultHolder.pass(stack);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack stack = context.getItemInHand();
		//if we're crouching and targeting a block, attempt to save the block as the focused block
		if (context.isSecondaryUseActive()) {
			BlockState state = context.getLevel().getBlockState(context.getClickedPos());
			if (state.is(BlockTagGenerator.ORE_METER_TARGETABLE)) {
				getOrCreateScanData(stack).putString("TargetedBlock", BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString());
				context.getPlayer().displayClientMessage(Component.translatable("misc.twilightforest.ore_meter_set_block", Component.translatable(state.getBlock().getDescriptionId())), true);
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
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		String block = getAssignedBlock(stack);

		if (block != null)
			tooltip.add(Component.translatable("misc.twilightforest.ore_meter_targeted_block", block).withStyle(ChatFormatting.GRAY));

		super.appendHoverText(stack, level, tooltip, flag);
	}

	@Nullable
	public static CompoundTag getScanData(ItemStack stack) {
		CompoundTag baseStackTag = stack.getTag();
		return baseStackTag != null ? baseStackTag.getCompound(NBT_SCAN_DATA) : null;
	}

	@NotNull
	public static CompoundTag getOrCreateScanData(ItemStack stack) {
		stack.getOrCreateTag(); // Ensures existence of base tag, because getOrCreateTagElement() doesn't check for that
		return stack.getOrCreateTagElement(NBT_SCAN_DATA);
	}

	public static long getID(ItemStack stack) {
		CompoundTag scanNbt = getScanData(stack);

		if (scanNbt != null && scanNbt.contains("ID"))
			return scanNbt.getLong("ID");

		return 0L;
	}

	public static int getRange(ItemStack stack) {
		CompoundTag scanNbt = getScanData(stack);

		if (scanNbt != null && scanNbt.contains("Range"))
			return Mth.clamp(scanNbt.getInt("Range"), 0, MAX_CHUNK_SEARCH_RANGE);

		return 1;
	}

	public static int getScannedBlocks(ItemStack stack) {
		CompoundTag scanNbt = getScanData(stack);

		if (scanNbt != null && scanNbt.contains("ScannedBlocks"))
			return scanNbt.getInt("ScannedBlocks");

		return 0;
	}

	public static ChunkPos getScannedChunk(ItemStack stack) {
		CompoundTag scanNbt = getScanData(stack);

		if (scanNbt != null && scanNbt.contains("ScannedChunk")) {
			return new ChunkPos(scanNbt.getLong("ScannedChunk"));
		}
		return new ChunkPos(0, 0);
	}

	@Nullable
	public static String getAssignedBlock(ItemStack stack) {
		CompoundTag scanNbt = getScanData(stack);

		if (scanNbt != null && scanNbt.contains("TargetedBlock")) {
			return scanNbt.getString("TargetedBlock");
		}
		return null;
	}

	public static void clearAssignedBlock(ItemStack stack) {
		CompoundTag scanNbt = getScanData(stack);

		if (scanNbt != null && scanNbt.contains("TargetedBlock")) {
			scanNbt.remove("TargetedBlock");
		}
	}

	public static boolean isLoading(ItemStack stack) {
		CompoundTag scanNbt = getScanData(stack);

		return scanNbt != null && scanNbt.contains("Loading");
	}

	public static int getLoadProgress(ItemStack stack) {
		CompoundTag scanNbt = getScanData(stack);

		if (scanNbt != null && scanNbt.contains("Loading")) {
			return scanNbt.getInt("Loading");
		}

		return 0;
	}

	public static Map<String, Integer> getScanInfo(ItemStack stack) {
		Map<String, Integer> tooltips = new LinkedHashMap<>();
		CompoundTag scanNbt = getScanData(stack);

		if (scanNbt != null && scanNbt.contains("ScanInfo")) {
			CompoundTag listTag = scanNbt.getCompound("ScanInfo");
			for (var key : listTag.getAllKeys()) {
				tooltips.put(key, listTag.getInt(key));
			}
		}

		return tooltips;
	}

	public static void saveScanInfo(ItemStack stack, Map<String, Integer> blockInfos, long chunkPos, int totalScanned) {
		CompoundTag blocksCounted = new CompoundTag();

		for (Map.Entry<String, Integer> countEntry : blockInfos.entrySet()) {
			blocksCounted.putInt(countEntry.getKey(), countEntry.getValue());
		}

		CompoundTag scanNbt = getOrCreateScanData(stack);
		scanNbt.put("ScanInfo", blocksCounted);

		if (chunkPos != 0L) {
			scanNbt.putLong("ScannedChunk", chunkPos);
		} else {
			scanNbt.remove("ScannedChunk");
		}
		if (totalScanned != 0) {
			scanNbt.putInt("ScannedBlocks", totalScanned);
		} else {
			scanNbt.remove("ScannedBlocks");
		}
		scanNbt.putLong("ID", (blocksCounted.hashCode() ^ chunkPos) * (getRange(stack) ^ totalScanned));
	}
}