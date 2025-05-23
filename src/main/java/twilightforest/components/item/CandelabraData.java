package twilightforest.components.item;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record CandelabraData(Optional<Block> one, Optional<Block> two, Optional<Block> three) {
	public static final CandelabraData EMPTY = new CandelabraData(Optional.empty(), Optional.empty(), Optional.empty());
	public static final Codec<CandelabraData> CODEC = BuiltInRegistries.BLOCK
		.byNameCodec()
		.sizeLimitedListOf(3)
		.xmap(CandelabraData::new, CandelabraData::ordered);
	public static final StreamCodec<RegistryFriendlyByteBuf, CandelabraData> STREAM_CODEC = ByteBufCodecs.registry(Registries.BLOCK)
		.apply(ByteBufCodecs.list(3))
		.map(CandelabraData::new, CandelabraData::ordered);

	public CandelabraData(List<Block> blocks) {
		this(getItem(blocks, 0), getItem(blocks, 1), getItem(blocks, 2));
	}

	public CandelabraData(Block one, Block two, Block three) {
		this(List.of(one, two, three));
	}

	public static Optional<Block> getItem(List<Block> candles, int index) {
		if (index >= candles.size()) {
			return Optional.empty();
		} else {
			Block candle = candles.get(index);
			return candle == Blocks.AIR ? Optional.empty() : Optional.of(candle);
		}
	}

	public CompoundTag save(CompoundTag tag) {
		if (!this.equals(EMPTY)) {
			tag.put("Candles", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow());
		}
		return tag;
	}

	public List<Block> ordered() {
		return Stream.of(this.one, this.two, this.three).map(block -> block.orElse(Blocks.AIR)).toList();
	}

	public static CandelabraData load(@Nullable CompoundTag tag) {
		return tag != null && tag.contains("Candles") ? CODEC.parse(NbtOps.INSTANCE, tag.get("Candles")).result().orElse(EMPTY) : EMPTY;
	}
}
