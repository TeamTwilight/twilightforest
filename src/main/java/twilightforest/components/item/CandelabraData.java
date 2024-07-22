package twilightforest.components.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.entity.CandelabraBlockEntity;

public record CandelabraData(int one, int two, int three) {
	public static final Codec<CandelabraData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		Codec.INT.fieldOf("one").forGetter(CandelabraData::one),
		Codec.INT.fieldOf("two").forGetter(CandelabraData::two),
		Codec.INT.fieldOf("three").forGetter(CandelabraData::three)
	).apply(inst, CandelabraData::new));

	public static final CandelabraData DEFAULT = new CandelabraData(17, 17, 17);

	public static CandelabraData dataFromBE(CandelabraBlockEntity be) {
		return new CandelabraData(blockToNumber(be, 0), blockToNumber(be, 1), blockToNumber(be, 2));
	}

	public static int blockToNumber(CandelabraBlockEntity be, int slot) {
		Block candle = be.getCandle(slot);
		return candle == Blocks.AIR ? 17 : AbstractSkullCandleBlock.candleToCandleColor(candle.asItem()).ordinal();
	}

	public static void setCandlesOf(CandelabraBlockEntity be, CandelabraData data) {
		be.getCandles()[0] = data.one == 17 ? Blocks.AIR : AbstractSkullCandleBlock.candleColorToCandle(AbstractSkullCandleBlock.CandleColors.colorFromInt(data.one));
		be.getCandles()[1] = data.two == 17 ? Blocks.AIR : AbstractSkullCandleBlock.candleColorToCandle(AbstractSkullCandleBlock.CandleColors.colorFromInt(data.two));
		be.getCandles()[2] = data.three == 17 ? Blocks.AIR : AbstractSkullCandleBlock.candleColorToCandle(AbstractSkullCandleBlock.CandleColors.colorFromInt(data.three));
	}
}
