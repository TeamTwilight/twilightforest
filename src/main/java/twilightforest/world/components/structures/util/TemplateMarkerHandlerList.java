package twilightforest.world.components.structures.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.codec.RegistryFileCodec;
import twilightforest.TFRegistries;
import twilightforest.init.custom.TemplateMarkerHandlers;
import twilightforest.world.components.structures.markerhandler.TemplateMarkerHandler;

import java.util.Arrays;
import java.util.List;

public record TemplateMarkerHandlerList(List<TemplateMarkerHandler> markerHandlers) {
	public static final Codec<TemplateMarkerHandlerList> CODEC = TemplateMarkerHandlers.DISPATCH_CODEC.listOf().xmap(TemplateMarkerHandlerList::new, TemplateMarkerHandlerList::markerHandlers);
	public static final Codec<Holder<TemplateMarkerHandlerList>> HOLDER_CODEC = RegistryFileCodec.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, CODEC, true);

	public static TemplateMarkerHandlerList of(TemplateMarkerHandler... markerHandlers) {
		return new TemplateMarkerHandlerList(Arrays.asList(markerHandlers));
	}
}
