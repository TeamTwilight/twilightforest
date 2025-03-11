package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.item.TravellersArmorItem;

public class PerformDoubleJumpPacket implements CustomPacketPayload {
	public static final Type<PerformDoubleJumpPacket> TYPE = new Type<>(TwilightForestMod.prefix("perform_double_jump_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PerformDoubleJumpPacket> STREAM_CODEC = CustomPacketPayload.codec(PerformDoubleJumpPacket::write, PerformDoubleJumpPacket::new);
	protected boolean performDoubleJump;

	public PerformDoubleJumpPacket(boolean performDoubleJump) {
		this.performDoubleJump = performDoubleJump;
	}
	public PerformDoubleJumpPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		new PerformDoubleJumpPacket(registryFriendlyByteBuf.readBoolean());
	}

	private void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		registryFriendlyByteBuf.writeBoolean(performDoubleJump);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PerformDoubleJumpPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			if (message.performDoubleJump)
				TravellersArmorItem.performDoubleJump(ctx.player());
		});
	}
}
