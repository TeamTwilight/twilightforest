package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFRegistries;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorItem;

import java.util.function.Function;

public interface TravellersModifier {

	Codec<TravellersModifier> CODEC = TFRegistries.TRAVELLERS_MODIFIER_TYPE.byNameCodec().dispatch(TravellersModifier::codec, Function.identity());

	MapCodec<? extends TravellersModifier> codec();

	boolean hasModifier(ItemStack stack);

	boolean isAbility();

	default String getPrefix() {
		return "travellers_gear.modifier";
	}

	default boolean isActive(ItemStack stack, ResourceKey<TravellersModifier> modifier) {
		return this.hasModifier(stack) && (!TravellersArmorItem.isTravellersArmorAndBroken(stack) || TravellersModifiersManager.ALWAYS_ACTIVE.contains(modifier));
	}
}
