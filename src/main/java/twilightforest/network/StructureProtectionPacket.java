package twilightforest.network;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.client.DimensionSpecialEffectsManager;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.init.TFDimension;
import twilightforest.util.Codecs;

import java.util.Optional;

public record StructureProtectionPacket(Optional<BoundingBox> box) implements CustomPacketPayload {

	public static final Type<StructureProtectionPacket> TYPE = new Type<>(TwilightForestMod.prefix("change_protection_renderer"));
	public static final StreamCodec<RegistryFriendlyByteBuf, StructureProtectionPacket> STREAM_CODEC = StreamCodec.composite(
		Codecs.BOX_STREAM_CODEC.apply(ByteBufCodecs::optional), StructureProtectionPacket::box, StructureProtectionPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(StructureProtectionPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			DimensionSpecialEffects info = DimensionSpecialEffectsManager.getForType(TFDimension.DIMENSION_RENDERER);

			// add weather box if needed
			if (info instanceof TwilightForestRenderInfo) {
				TFWeatherRenderer.setProtectedBox(message.box().orElse(null));
			}
		});
	}
}
