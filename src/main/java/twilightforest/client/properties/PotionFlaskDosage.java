package twilightforest.client.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.init.TFDataComponents;
import twilightforest.item.PotionFlaskItem;

public record PotionFlaskDosage(boolean normalize) implements RangeSelectItemModelProperty {

	public static final MapCodec<PotionFlaskDosage> TYPE = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.BOOL.optionalFieldOf("normalize", true).forGetter(PotionFlaskDosage::normalize))
		.apply(instance, PotionFlaskDosage::new)
	);

	@Override
	public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		PotionFlaskComponent contents = stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY);
		return this.normalize ? Mth.clamp((float) contents.doses() / PotionFlaskItem.DOSES, 0.0F, 1.0F) : Mth.clamp(contents.doses(), 0.0F, PotionFlaskItem.DOSES);
	}

	@Override
	public MapCodec<PotionFlaskDosage> type() {
		return TYPE;
	}
}
