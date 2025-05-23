package twilightforest.client.properties;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.init.TFDataComponents;

import javax.annotation.Nullable;

public record PotionFlaskTintSource(int defaultColor) implements ItemTintSource {
	public static final MapCodec<PotionFlaskTintSource> TYPE = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(PotionFlaskTintSource::defaultColor))
		.apply(instance, PotionFlaskTintSource::new));

	public PotionFlaskTintSource() {
		this(-13083194);
	}

	@Override
	public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
		PotionFlaskComponent flask = stack.get(TFDataComponents.POTION_FLASK_CONTENTS);
		return flask != null ? ARGB.opaque(flask.potion().getColorOr(this.defaultColor)) : ARGB.opaque(this.defaultColor);
	}

	@Override
	public MapCodec<PotionFlaskTintSource> type() {
		return TYPE;
	}
}
