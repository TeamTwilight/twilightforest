package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;

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
		ctx.enqueueWork(() ->
			Minecraft.getInstance().level.getGameRules().getRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE).set(message.enforce(), null)
		);
	}
}
