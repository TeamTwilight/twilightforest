package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorItem;

public interface TravellersModifier {
	Codec<TravellersModifier> CODEC = Codec.STRING.dispatch(
		"type",
		TravellersModifier::getTypeName,
		key -> {
			MapCodec<? extends TravellersModifier> codec = TravellersModifierTypes.TYPE_TO_CODEC.get(key);
			if (codec == null) {
				throw new IllegalArgumentException("Unknown TravellersModifier type: " + key);
			}
			return codec;
		}
	);

	String getTypeName();

	default String getLangKey() {
		return "travellers_gear.modifier." + this.getName().toString().replace(":", ".");
	}

	boolean hasModifier(ItemStack stack);
	ResourceLocation getName();

	default boolean isActive(ItemStack stack) {
		return hasModifier(stack) && (!TravellersArmorItem.isTravellersArmorAndBroken(stack) || TravellersModifiersManager.ALWAYS_ACTIVE.contains(getName()));
	}
}
