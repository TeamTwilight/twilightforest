package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.event.LockedBiomeToastHandler;
import twilightforest.client.renderer.TFWeatherRenderer;

public record EnforceProgressionStatusPacket(boolean enforce) implements CustomPacketPayload {

	public static final Type<EnforceProgressionStatusPacket> TYPE = new Type<>(TwilightForestMod.prefix("sync_progression_status"));
	public static final StreamCodec<RegistryFriendlyByteBuf, EnforceProgressionStatusPacket> STREAM_CODEC = CustomPacketPayload.codec(EnforceProgressionStatusPacket::write, EnforceProgressionStatusPacket::new);

	public EnforceProgressionStatusPacket(FriendlyByteBuf buf) {
		this(buf.readBoolean());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBoolean(this.enforce);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(EnforceProgressionStatusPacket message, IPayloadContext ctx) {
		boolean enforce = message.enforce;
		ctx.enqueueWork(() -> {
			TFWeatherRenderer.setProgressionEnforced(enforce);
			LockedBiomeToastHandler.setProgressionEnforced(enforce);
		});
	}
}
