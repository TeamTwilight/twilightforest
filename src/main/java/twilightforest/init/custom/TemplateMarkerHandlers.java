package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.markerhandler.*;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;

public class TemplateMarkerHandlers {

	public static final DeferredRegister<TemplateMarkerHandlerType> TEMPLATE_MARKER_HANDLER_TYPES = DeferredRegister.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_TYPE, TwilightForestMod.ID);
	public static final Codec<TemplateMarkerHandlerType> TYPE_CODEC = Codec.lazyInitialized(TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES::byNameCodec);
	public static final Codec<TemplateMarkerHandler> DISPATCH_CODEC = TYPE_CODEC.dispatch("type", TemplateMarkerHandler::getType, TemplateMarkerHandlerType::getCodec);
	public static final Codec<Holder<TemplateMarkerHandler>> HOLDER_CODEC = RegistryFileCodec.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER, DISPATCH_CODEC);

	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> BLOCK_PLACEMENT = TEMPLATE_MARKER_HANDLER_TYPES.register("block_placement", () -> () -> BlockPlaceMarkerHandler.CODEC);
	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> HANDLER_SWITCH = TEMPLATE_MARKER_HANDLER_TYPES.register("handler_switch", () -> () -> SwitchMarkerHandler.CODEC);
	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> ROTATION = TEMPLATE_MARKER_HANDLER_TYPES.register("rotation", () -> () -> RotationMarkerHandler.CODEC);
	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> DRYING_RACK = TEMPLATE_MARKER_HANDLER_TYPES.register("drying_rack", () -> () -> DryingRackMarkerHandler.CODEC);
	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> PAINTING = TEMPLATE_MARKER_HANDLER_TYPES.register("painting", () -> () -> PaintingMarkerHandler.CODEC);
	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> LOOT = TEMPLATE_MARKER_HANDLER_TYPES.register("loot", () -> () -> LootMarkerHandler.CODEC);

	public static final ResourceKey<TemplateMarkerHandlerList> CAMP_MARKER_HANDLERS = ResourceKey.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TwilightForestMod.prefix("camp_marker_handlers"));

	// TODO
	//  Lich Tower:
	//    Dangling handler
	//    List handler
	//    Mason jar handler
	//    Skull handler
	//    Candle handler
	//    Candled Skull handler
	//    Bookshelf handler
	//    Bookshelf Mimic handler
	//    Lectern handler
	//    Spawner handler

}
