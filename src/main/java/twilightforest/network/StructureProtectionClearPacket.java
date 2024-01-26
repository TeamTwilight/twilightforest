package twilightforest.network;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.DimensionSpecialEffectsManager;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.client.renderer.TFWeatherRenderer;

public class StructureProtectionClearPacket implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("clear_protection_renderer");

	@Override
	public void write(FriendlyByteBuf buf) {
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(StructureProtectionClearPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			DimensionSpecialEffects info = DimensionSpecialEffectsManager.getForType(TwilightForestMod.prefix("renderer"));

			// remove weather box if needed
			if (info instanceof TwilightForestRenderInfo) {
				TFWeatherRenderer.setProtectedBox(null);
			}
		});
	}
}
