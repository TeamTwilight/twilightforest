package twilightforest.components.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;

import java.util.Collections;
import java.util.Map;

public record OreScannerData(Map<String, Integer> counts, ChunkPos scannedChunk, int totalScannedBlocks, long universalId) {
	public static Codec<OreScannerData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		Codec.unboundedMap(ExtraCodecs.NON_EMPTY_STRING, ExtraCodecs.NON_NEGATIVE_INT).fieldOf("counts").forGetter(OreScannerData::counts),
		Codec.LONG.xmap(ChunkPos::new, ChunkPos::toLong).fieldOf("scanned_chunk").forGetter(OreScannerData::scannedChunk),
		ExtraCodecs.NON_NEGATIVE_INT.fieldOf("scanned_block_count").forGetter(OreScannerData::totalScannedBlocks),
		Codec.LONG.fieldOf("universal_id").forGetter(OreScannerData::universalId)
	).apply(inst, OreScannerData::new));

	public static StreamCodec<RegistryFriendlyByteBuf, OreScannerData> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.map(Object2IntArrayMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.INT), OreScannerData::counts,
		ByteBufCodecs.VAR_LONG.map(ChunkPos::new, ChunkPos::toLong), OreScannerData::scannedChunk,
		ByteBufCodecs.INT, OreScannerData::totalScannedBlocks,
		ByteBufCodecs.VAR_LONG, OreScannerData::universalId,
		OreScannerData::new
	);

	public static OreScannerData create(Map<String, Integer> counts, ChunkPos scannedChunk, int totalScannedBlocks, int scannedRange) {
		Map<String, Integer> immutableCounted = Collections.unmodifiableMap(counts);
		return new OreScannerData(immutableCounted, scannedChunk, totalScannedBlocks, (immutableCounted.hashCode() ^ scannedChunk.toLong()) * (scannedRange ^ totalScannedBlocks));
	}
}
