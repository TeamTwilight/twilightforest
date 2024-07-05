package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;

public record MovePlayerPacket(double motionX, double motionY, double motionZ) implements CustomPacketPayload {

	public static final Type<MovePlayerPacket> TYPE = new Type<>(TwilightForestMod.prefix("move_player"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MovePlayerPacket> STREAM_CODEC = CustomPacketPayload.codec(MovePlayerPacket::write, MovePlayerPacket::new);

	public MovePlayerPacket(FriendlyByteBuf buf) {
		this(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeDouble(this.motionX());
		buf.writeDouble(this.motionY());
		buf.writeDouble(this.motionZ());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(MovePlayerPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> ctx.player().push(message.motionX(), message.motionY(), message.motionZ()));
	}
}
