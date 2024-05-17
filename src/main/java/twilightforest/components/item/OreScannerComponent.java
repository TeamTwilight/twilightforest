package twilightforest.components.item;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class OreScannerComponent {
	public static Codec<OreScannerComponent> CODEC = RecordCodecBuilder.<OreScannerComponent>create(inst -> inst.group(
		BlockPos.CODEC.fieldOf("origin").forGetter(s -> s.origin),
		Codec.INT.fieldOf("span_x").forGetter(s -> s.xSpan),
		Codec.INT.fieldOf("span_z").forGetter(s -> s.zSpan),
		Codec.INT.fieldOf("duration").forGetter(s -> s.scanDurationTicks),
		Codec.unboundedMap(BuiltInRegistries.BLOCK.byNameCodec(), Codec.INT).<Object2IntMap<Block>>xmap(Object2IntArrayMap::new, Function.identity()).fieldOf("counts").forGetter(s -> s.blockCounter),
		Codec.INT.fieldOf("progression").forGetter(s -> s.ticksProgressed)
	).apply(inst, OreScannerComponent::new)).orElseGet(OreScannerComponent::getEmpty);

	private static final OreScannerComponent EMPTY = new OreScannerComponent(BlockPos.ZERO, 0, 0, 0, 0);

	private final int xSpan, zSpan;
	private final int area;
	private final int scanDurationTicks;

	private final BlockPos origin;
	private final Object2IntMap<Block> blockCounter;

	private final int ticksProgressed;

	public static OreScannerComponent scanFromCenter(BlockPos center, int range, int scanDurationTicks) {
		int xChunkCenter = center.getX() >> 4;
		int zChunkCenter = center.getZ() >> 4;

		// Enforce alignment with chunk edges
		BlockPos origin = new BlockPos(SectionPos.sectionToBlockCoord(xChunkCenter - range), 0, SectionPos.sectionToBlockCoord(zChunkCenter - range));
		int xSpan = SectionPos.sectionToBlockCoord(xChunkCenter + range, 15) - origin.getX();
		int zSpan = SectionPos.sectionToBlockCoord(zChunkCenter + range, 15) - origin.getZ();

		return new OreScannerComponent(origin, xSpan, zSpan, scanDurationTicks, 0);
	}

	public OreScannerComponent(BlockPos origin, int xSpan, int zSpan, int scanDurationTicks, int ticksProgressed) {
		this(origin, xSpan, zSpan, scanDurationTicks, xSpan * zSpan <= 0 ? Object2IntMaps.emptyMap() : new Object2IntArrayMap<>(), ticksProgressed);
	}

	public OreScannerComponent(BlockPos origin, int xSpan, int zSpan, int scanDurationTicks, Object2IntMap<Block> blockCounter, int ticksProgressed) {
		this.origin = origin;
		this.xSpan = xSpan;
		this.zSpan = zSpan;

		// Total horizontal coverage that the scanning region encompasses,
		//  to be later multiplied by building height for actual volume
		this.area = this.xSpan * this.zSpan;

		this.scanDurationTicks = scanDurationTicks;

		this.blockCounter = Object2IntMaps.unmodifiable(blockCounter.isEmpty() ? Object2IntMaps.emptyMap() : blockCounter);

		this.ticksProgressed = ticksProgressed;
	}

	public OreScannerComponent tickScan(BlockGetter reader) {
		BlockPos originPos = this.origin.atY(reader.getMinBuildHeight());
		int volume = this.area * reader.getMaxBuildHeight();
		int march = Mth.ceil((float) volume / Mth.abs(this.scanDurationTicks));
		int totalProgress = this.ticksProgressed * march;
		Object2IntMap<Block> nextCounter = new Object2IntArrayMap<>(this.blockCounter);

		for (int scanSteps = 0; scanSteps < march && totalProgress + scanSteps < volume; scanSteps++) {
			int xDelta = (totalProgress + scanSteps) % this.xSpan;
			int zDelta = (totalProgress + scanSteps) % (this.xSpan * this.zSpan) / this.xSpan;
			int yDelta = (totalProgress + scanSteps) / (this.xSpan * this.zSpan);

			BlockPos pos = originPos.offset(xDelta, yDelta, zDelta);

			Block blockFound = reader.getBlockState(pos).getBlock();
			nextCounter.put(blockFound, 1 + nextCounter.getOrDefault(blockFound, 0));
		}

		// Method returns true if scanning is complete, and results ready for syncing to itemstack nbt
		return new OreScannerComponent(this.origin, this.xSpan, this.zSpan, this.scanDurationTicks, nextCounter, this.ticksProgressed + 1);
	}

	public Map<String, Integer> getResults(@Nullable Block assignedBlock) {
		if (assignedBlock != null) {
			return ImmutableMap.of(assignedBlock.getDescriptionId(), this.blockCounter.getOrDefault(assignedBlock, 0));
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

	public static OreScannerComponent getEmpty() {
		return EMPTY;
	}

	public boolean isFinished() {
		return this.isEmpty() || this.ticksProgressed >= this.scanDurationTicks;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OreScannerComponent that = (OreScannerComponent) o;
		return xSpan == that.xSpan && zSpan == that.zSpan && scanDurationTicks == that.scanDurationTicks && ticksProgressed == that.ticksProgressed && Objects.equals(origin, that.origin) && Objects.equals(blockCounter, that.blockCounter);
	}

	@Override
	public int hashCode() {
		return Objects.hash(xSpan, zSpan, scanDurationTicks, origin, blockCounter, ticksProgressed);
	}

	@Override
	public String toString() {
		return "OreScannerComponent{" +
			"xSpan=" + xSpan +
			", zSpan=" + zSpan +
			", area=" + area +
			", scanDurationTicks=" + scanDurationTicks +
			", origin=" + origin +
			", blockCounter=" + blockCounter +
			", ticksProgressed=" + ticksProgressed +
			'}';
	}
}
