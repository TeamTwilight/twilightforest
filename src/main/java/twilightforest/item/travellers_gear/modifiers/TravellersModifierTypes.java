package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.MapCodec;

import java.util.HashMap;
import java.util.Map;

public abstract class TravellersModifierTypes {
	private static boolean initialized = false;
	public static final Map<String, MapCodec<? extends TravellersModifier>> TYPE_TO_CODEC = new HashMap<>();
	public static void initialize() {
		if (!initialized) {
			BuiltinTravellersComponentModifier.setTypeName("builtin_modifier");
			TravellersEntryModifier.setTypeName("entry_modifier");
			TravellersComponentModifier.setTypeName("component_modifier");
			initialized = true;
		}
	}
}
