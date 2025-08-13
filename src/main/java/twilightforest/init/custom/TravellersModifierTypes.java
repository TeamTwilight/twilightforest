package twilightforest.init.custom;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.item.travellers_gear.modifiers.BuiltinTravellersComponentModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersComponentModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersEntryModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

public class TravellersModifierTypes {

	public static final DeferredRegister<MapCodec<? extends TravellersModifier>> TYPES = DeferredRegister.create(TFRegistries.Keys.TRAVELLERS_MODIFIER_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<MapCodec<? extends TravellersModifier>, MapCodec<TravellersEntryModifier>> ATTRIBUTE_ENTRY = TYPES.register("attribute", () -> TravellersEntryModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends TravellersModifier>, MapCodec<BuiltinTravellersComponentModifier>> BUILTIN = TYPES.register("builtin", () -> BuiltinTravellersComponentModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends TravellersModifier>, MapCodec<TravellersComponentModifier>> COMPONENT = TYPES.register("component", () -> TravellersComponentModifier.CODEC);
}
