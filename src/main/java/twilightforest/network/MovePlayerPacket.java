package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;

public record MovePlayerPacket(double motionX, double motionY, double motionZ) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("move_player");

	public MovePlayerPacket(FriendlyByteBuf buf) {
		this(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeDouble(this.motionX());
		buf.writeDouble(this.motionY());
		buf.writeDouble(this.motionZ());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(MovePlayerPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() ->
				ctx.player().orElseThrow().push(message.motionX(), message.motionY(), message.motionZ())
		);
	}
}
