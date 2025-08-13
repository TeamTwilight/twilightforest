package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFRegistries;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorItem;

import java.util.function.Function;

public interface TravellersModifier {

	Codec<TravellersModifier> CODEC = TFRegistries.TRAVELLERS_MODIFIER_TYPE.byNameCodec().dispatch(TravellersModifier::codec, Function.identity());

	ResourceLocation name();

	MapCodec<? extends TravellersModifier> codec();

	boolean hasModifier(ItemStack stack);

	default String getLangKey() {
		return "travellers_gear.modifier." + this.name().toString().replace(":", ".");
	}

	default boolean isActive(ItemStack stack) {
		return hasModifier(stack) && (!TravellersArmorItem.isTravellersArmorAndBroken(stack) || TravellersModifiersManager.ALWAYS_ACTIVE.contains(name()));
	}
}
