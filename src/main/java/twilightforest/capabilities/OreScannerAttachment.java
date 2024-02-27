package twilightforest.capabilities;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class OreScannerAttachment {
	private static final OreScannerAttachment EMPTY = new OreScannerAttachment(BlockPos.ZERO, 0, 0, 0);

	private final int xSpan, zSpan;
	private final int area;
	private final int scanDurationTicks;

	private final BlockPos origin;
	private final Object2IntMap<Block> blockCounter;

	private int ticksProgressed = 0;

	public static OreScannerAttachment scanFromCenter(BlockPos center, int range, int scanDurationTicks) {
		int xChunkCenter = center.getX() >> 4;
		int zChunkCenter = center.getZ() >> 4;

		// Enforce alignment with chunk edges
		BlockPos origin = new BlockPos(SectionPos.sectionToBlockCoord(xChunkCenter - range), 0, SectionPos.sectionToBlockCoord(zChunkCenter - range));
		int xSpan = SectionPos.sectionToBlockCoord(xChunkCenter + range, 15) - origin.getX();
		int zSpan = SectionPos.sectionToBlockCoord(zChunkCenter + range, 15) - origin.getZ();

		return new OreScannerAttachment(origin, xSpan, zSpan, scanDurationTicks);
	}

	public OreScannerAttachment(BlockPos origin, int xSpan, int zSpan, int scanDurationTicks) {
		this.origin = origin;
		this.xSpan = xSpan;
		this.zSpan = zSpan;

		// Total horizontal coverage that the scanning region encompasses,
		//  to be later multiplied by building height for actual volume
		this.area = this.xSpan * this.zSpan;

		this.scanDurationTicks = scanDurationTicks;

		this.blockCounter = this.area <= 0 ? Object2IntMaps.emptyMap() : new Object2IntArrayMap<>();
	}

	public boolean tickScan(BlockGetter reader) {
		BlockPos originPos = this.origin.atY(reader.getMinBuildHeight());
		int volume = this.area * reader.getMaxBuildHeight();
		int march = Mth.ceil((float) volume / Mth.abs(this.scanDurationTicks));
		int totalProgress = this.ticksProgressed * march;

		for (int scanSteps = 0; scanSteps < march && totalProgress + scanSteps < volume; scanSteps++) {
			int xDelta = (totalProgress + scanSteps) % this.xSpan;
			int zDelta = (totalProgress + scanSteps) % (this.xSpan * this.zSpan) / this.xSpan;
			int yDelta = (totalProgress + scanSteps) / (this.xSpan * this.zSpan);

			BlockPos pos = originPos.offset(xDelta, yDelta, zDelta);

			Block blockFound = reader.getBlockState(pos).getBlock();
			this.blockCounter.put(blockFound, 1 + this.blockCounter.getOrDefault(blockFound, 0));
		}

		this.ticksProgressed++;

		// Method returns true if scanning is complete, and results ready for syncing to itemstack nbt
		return this.ticksProgressed >= this.scanDurationTicks;
	}

	public Map<String, Integer> getResults(@Nullable String assignedBlock) {
		if (assignedBlock != null && !assignedBlock.isBlank()) {
			Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(assignedBlock));

			return ImmutableMap.of(block.getDescriptionId(), this.blockCounter.getOrDefault(block, 0));
		}

		ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();

		for (Object2IntMap.Entry<Block> entry : this.blockCounter.object2IntEntrySet()) {
			if (entry.getIntValue() > 0 && entry.getKey().builtInRegistryHolder().is(Tags.Blocks.ORES)) {
				builder.put(entry.getKey().getDescriptionId(), entry.getIntValue());
			}
		}

		return builder.build();
	}

	public int getVolume(BlockGetter reader) {
		return this.area * reader.getMaxBuildHeight();
	}

	public int getTickProgress() {
		return this.ticksProgressed;
	}

	public ChunkPos centerChunkPos() {
		return new ChunkPos(Mth.floor(this.origin.getX() + this.xSpan / 2f) >> 4, Mth.floor(this.origin.getZ() + this.zSpan / 2f) >> 4);
	}

	public boolean isEmpty() {
		return this.area <= 0;
	}

	public static OreScannerAttachment getEmpty() {
		return EMPTY;
	}

	// This attachment goes onto ItemStacks, so this seems better suited over using a Codec
	public static class Serializer implements IAttachmentSerializer<CompoundTag, OreScannerAttachment> {
		public static final OreScannerAttachment.Serializer INSTANCE = new Serializer();

		private Serializer() {
		}

		@Override
		public OreScannerAttachment read(IAttachmentHolder holder, CompoundTag tag) {
			BlockPos origin = NbtUtils.readBlockPos(tag);

			int xSpan = tag.getInt("span_x");
			int zSpan = tag.getInt("span_z");

			int scanTimeTicks = tag.getInt("duration");

			if (xSpan <= 0 || zSpan <= 0 || scanTimeTicks <= 0) {
				return EMPTY;
			}

			OreScannerAttachment attachment = new OreScannerAttachment(origin, xSpan, zSpan, scanTimeTicks);

			ListTag list = tag.getList("counts", 10);

			for (Tag tagEntry : list) {
				if (tagEntry instanceof CompoundTag compoundEntry) {
					Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(compoundEntry.getString("block")));
					int counted = compoundEntry.getInt("counted");

					attachment.blockCounter.put(block, counted);
				}
			}

			return attachment;
		}

		@Nullable
		@Override
		public CompoundTag write(OreScannerAttachment attachment) {
			if (attachment.area <= 0)
				return null;

			CompoundTag tag = NbtUtils.writeBlockPos(attachment.origin);

			tag.putInt("span_x", attachment.xSpan);
			tag.putInt("span_z", attachment.zSpan);

			tag.putInt("duration", attachment.scanDurationTicks);

			ObjectSet<Object2IntMap.Entry<Block>> scanCounts = attachment.blockCounter.object2IntEntrySet();
			ListTag list = new ListTag();

			for (Object2IntMap.Entry<Block> counter : scanCounts) {
				CompoundTag countTag = new CompoundTag();

				countTag.putString("block", BuiltInRegistries.BLOCK.getKey(counter.getKey()).toString());
				countTag.putInt("counted", counter.getIntValue());

				list.add(countTag);
			}

			tag.put("counts", list);

			return tag;
		}
	}
}
