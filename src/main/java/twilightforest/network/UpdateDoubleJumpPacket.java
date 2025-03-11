package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;

public class UpdateDoubleJumpPacket implements CustomPacketPayload {
	public static final Type<UpdateDoubleJumpPacket> TYPE = new Type<>(TwilightForestMod.prefix("update_double_jump_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateDoubleJumpPacket> STREAM_CODEC = CustomPacketPayload.codec(UpdateDoubleJumpPacket::write, UpdateDoubleJumpPacket::new);
	protected boolean hasDoubleJump;

	public UpdateDoubleJumpPacket(boolean hasDoubleJump) {
		this.hasDoubleJump = hasDoubleJump;
	}
	public UpdateDoubleJumpPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		new UpdateDoubleJumpPacket(registryFriendlyByteBuf.readBoolean());
	}

	private void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		registryFriendlyByteBuf.writeBoolean(hasDoubleJump);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(UpdateDoubleJumpPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			ctx.player().setData(TFDataAttachments.HAS_DOUBLE_JUMP, message.hasDoubleJump);
		});
	}
}
