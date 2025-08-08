package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorItem;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public interface TravellersModifier {
	Map<String, MapCodec<? extends TravellersModifier>> TYPE_TO_CODEC = fillCodecs();
	Codec<TravellersModifier> CODEC = Codec.STRING.dispatch(
		"type",
		TravellersModifier::typeName,
		key -> {
			MapCodec<? extends TravellersModifier> codec = TYPE_TO_CODEC.get(key);
			if (codec == null) {
				throw new IllegalArgumentException("Unknown TravellersModifier type: " + key);
			}
			return codec;
		}
	);

	default String typeName() {
		return getClass().getSimpleName().toLowerCase(Locale.ROOT);
	}

	boolean hasModifier(ItemStack stack);
	ResourceLocation getName();

	default boolean isActive(ItemStack stack) {
		return hasModifier(stack) && (!TravellersArmorItem.isTravellersArmorAndBroken(stack) || TravellersModifiersManager.ALWAYS_ACTIVE.contains(getName()));
	}

	static Map<String, MapCodec<? extends TravellersModifier>> fillCodecs() {
		Map<String, MapCodec<? extends TravellersModifier>> map = new HashMap<>();
		registerCodec(map, TravellersComponentModifier.class, TravellersComponentModifier.MAP_CODEC);
		registerCodec(map, TravellersEntryModifier.class, TravellersEntryModifier.MAP_CODEC);
		registerCodec(map, BuiltinTravellersComponentModifier.class, BuiltinTravellersComponentModifier.MAP_CODEC);
		return map;
	}

	static <T extends TravellersModifier> void registerCodec(
		Map<String, MapCodec<? extends TravellersModifier>> map,
		Class<T> clazz, MapCodec<T> codec
	) {
		map.put(clazz.getSimpleName().toLowerCase(Locale.ROOT), codec);
	}
}
