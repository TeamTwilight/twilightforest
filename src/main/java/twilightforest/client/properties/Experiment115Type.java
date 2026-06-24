package twilightforest.client.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDataComponents;

public class Experiment115Type implements SelectItemModelProperty<String> {

	public static final SelectItemModelProperty.Type<Experiment115Type, String> TYPE = SelectItemModelProperty.Type.create(
		MapCodec.unit(new Experiment115Type()), Codec.STRING
	);

	@Nullable
	@Override
	public String get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext context) {
		return stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS);
	}

	@Override
	public Codec<String> valueCodec() {
		return Codec.STRING;
	}

	@Override
	public Type<? extends SelectItemModelProperty<String>, String> type() {
		return TYPE;
	}
}
