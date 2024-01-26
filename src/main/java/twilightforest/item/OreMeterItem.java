package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFSounds;
import twilightforest.network.SyncOreMeterPacket;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OreMeterItem extends Item {
	public static final int MAX_CHUNK_SEARCH_RANGE = 2;
	public static final int LOAD_TIME = 50;
	public static final String NBT_SCAN_DATA = "ScanData";

	public OreMeterItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean held) {
		//loading logic. The ore meter doesn't display any info until it's been "loading" for 50 ticks.
		//While mostly a visual thing, this also prevents spamming the meter and constantly populating it with data.
		CompoundTag scanNbt = getScanData(stack);
		if (scanNbt != null && scanNbt.contains("Loading") && level.isClientSide()) {
			int loadTime = scanNbt.getInt("Loading");
			if (loadTime < LOAD_TIME) {
				scanNbt.putInt("Loading", loadTime + 1);
			} else {
				scanNbt.remove("Loading");
				int useX = Mth.floor(entity.getX());
				int useZ = Mth.floor(entity.getZ());
				String blockId = getAssignedBlock(stack);
				this.countOreInArea(stack, level, slot, useX, useZ, getRange(stack), blockId != null ? BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(blockId)) : null);
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		//if we're in the "loading" state don't try to run any logic
		if (isLoading(stack)) return InteractionResultHolder.pass(stack);

		CompoundTag scanNbt = getOrCreateScanData(stack);

		//if we're not crouching, put the ore meter into its "loading" state
		if (!player.isSecondaryUseActive()) {
			if (!scanNbt.contains("Loading")) {
				scanNbt.putInt("Loading", 0);
			}
			level.playSound(player, player.blockPosition(), TFSounds.ORE_METER_CRACKLE.get(), SoundSource.PLAYERS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
			return InteractionResultHolder.pass(stack);
		} else {
			//if we're crouching and not targeting a block, change the ore meter range instead
			HitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
			if (result.getType() == HitResult.Type.MISS) {
				int newRange = getRange(stack) + 1;
				if (newRange > MAX_CHUNK_SEARCH_RANGE) {
					newRange = 0;
				}
				stack.getOrCreateTag().putInt("Range", newRange);
				player.displayClientMessage(Component.translatable("misc.twilightforest.ore_meter_new_range", newRange), true);
				level.playSound(player, player.blockPosition(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.PLAYERS, 0.25F, 0.75F + (newRange * 0.1F));
				return InteractionResultHolder.success(stack);
			}
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
				getScanData(stack).putString("TargetedBlock", BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString());
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
			return scanNbt.getInt("Range");

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
			ListTag listTag = scanNbt.getList("ScanInfo", 10);
			for (Tag tag : listTag) {
				try {
					String name = ((CompoundTag) tag).getAllKeys().toArray()[0].toString();
					tooltips.put(name, ((CompoundTag) tag).getInt(name));
				} catch (Exception e) {
					TwilightForestMod.LOGGER.error("Error with deserializing ScanInfo from NBT", e);
				}
			}
		}
		return tooltips;
	}

	public static void saveScanInfo(ItemStack stack, Map<String, Integer> blockInfos, long chunkPos, int totalScanned) {
		ListTag tag = new ListTag();
		CompoundTag scanNbt = getOrCreateScanData(stack);

		for (Map.Entry<String, Integer> entry : blockInfos.entrySet()) {
			CompoundTag compTag = new CompoundTag();
			compTag.putInt(entry.getKey(), entry.getValue());
			tag.add(compTag);
		}

		tag.sort(new TagSorter());
		scanNbt.put("ScanInfo", tag);
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
		scanNbt.putLong("ID", (tag.hashCode() ^ chunkPos) * (getRange(stack) ^ totalScanned));
	}

	//note: this is only done client-side to avoid anyone lagging the server.
	//A packet is sent to the server to update the item NBT later down the line
	private void countOreInArea(ItemStack stack, Level level, int slot, int useX, int useZ, int radius, @Nullable Block targetedBlock) {
		Map<String, Integer> oreCounts = new HashMap<>();
		int chunkX = useX >> 4;
		int chunkZ = useZ >> 4;

		ScanResult dummy = new ScanResult();
		Map<Block, Integer> finalResults = new TreeMap<>(Comparator.comparing(Block::getDescriptionId));
		AtomicInteger totalScanned = new AtomicInteger();
		for (int cx = chunkX - radius; cx <= chunkX + radius; cx++) {
			for (int cz = chunkZ - radius; cz <= chunkZ + radius; cz++) {
				if (!level.hasChunk(cx, cz)) continue;
				Map<Block, ScanResult> results = this.countBlocksInChunk(level, cx, cz);
				results.forEach((state, scanResult) -> totalScanned.addAndGet(scanResult.count));
				if (targetedBlock != null) {
					if (results.getOrDefault(targetedBlock, dummy).count != 0) {
						if (finalResults.containsKey(targetedBlock)) {
							int existingCount = finalResults.get(targetedBlock);
							finalResults.put(targetedBlock, results.getOrDefault(targetedBlock, dummy).count + existingCount);
						} else {
							finalResults.put(targetedBlock, results.getOrDefault(targetedBlock, dummy).count);
						}
					}
				} else {
					for (Block block : BuiltInRegistries.BLOCK.getTag(Tags.Blocks.ORES).get().stream().map(Holder::value).toList()) {
						if (results.getOrDefault(block, dummy).count != 0) {
							if (finalResults.containsKey(block)) {
								int existingCount = finalResults.get(block);
								finalResults.put(block, results.getOrDefault(block, dummy).count + existingCount);
							} else {
								finalResults.put(block, results.getOrDefault(block, dummy).count);
							}
						}
					}
				}
			}
		}


		if (!finalResults.isEmpty()) {
			for (Map.Entry<Block, Integer> entry : finalResults.entrySet()) {
				oreCounts.put(entry.getKey().getDescriptionId(), entry.getValue());
			}
		}

		saveScanInfo(stack, oreCounts, new ChunkPos(chunkX, chunkZ).toLong(), totalScanned.get());

		if (level.isClientSide()) {
            PacketDistributor.SERVER.noArg().send(new SyncOreMeterPacket(getOrCreateScanData(stack), slot));
        }
	}

	private Map<Block, ScanResult> countBlocksInChunk(Level level, int cx, int cz) {
		Map<Block, ScanResult> ret = new IdentityHashMap<>();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for (int x = cx << 4; x < (cx << 4) + 16; x++) {
			for (int z = cz << 4; z < (cz << 4) + 16; z++) {
				for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
					BlockState state = level.getBlockState(pos.set(x, y, z));
					if (!state.isAir() && !(state.getBlock() instanceof LiquidBlock)) {
						ScanResult res = ret.computeIfAbsent(state.getBlock(), s -> new ScanResult());
						res.count++;
					}
				}
			}
		}

		return ret;
	}

	private static class ScanResult {
		int count;
	}

	public static class TagSorter implements Comparator<Tag> {

		@Override
		public int compare(Tag o1, Tag o2) {
			String key1 = (String) ((CompoundTag) o1).getAllKeys().toArray()[0];
			String key2 = (String) ((CompoundTag) o2).getAllKeys().toArray()[0];
			return Integer.compare(((CompoundTag) o2).getInt(key2), ((CompoundTag) o1).getInt(key1));
		}
	}
}