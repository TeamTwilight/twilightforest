package twilightforest.components.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.apache.commons.lang3.text.WordUtils;
import twilightforest.block.AbstractSkullCandleBlock;

import java.util.function.Consumer;

public record SkullCandles(int color, int count) implements TooltipProvider {
	public static final Codec<SkullCandles> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		Codec.INT.fieldOf("color").forGetter(SkullCandles::color),
		Codec.intRange(1, 4).fieldOf("count").forGetter(SkullCandles::count)
	).apply(inst, SkullCandles::new));

	public static final StreamCodec<? super RegistryFriendlyByteBuf, SkullCandles> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, SkullCandles::color,
		ByteBufCodecs.INT, SkullCandles::count,
		SkullCandles::new
	);

	public static final SkullCandles DEFAULT = new SkullCandles(0, 1);

	@Override
	public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
		consumer.accept(Component.translatable(this.count() > 1 ?
					"item.twilightforest.skull_candle.desc.multiple" :
					"item.twilightforest.skull_candle.desc",
				this.count(),
				WordUtils.capitalize(AbstractSkullCandleBlock.CandleColors.colorFromInt(this.color()).getSerializedName()
					.replace("\"", "").replace("_", " ")))
			.withStyle(ChatFormatting.GRAY));
	}
}
