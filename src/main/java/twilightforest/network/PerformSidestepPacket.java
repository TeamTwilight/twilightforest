package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.TravellersGearLogic;

public record PerformSidestepPacket(boolean isLeftStepSide) implements CustomPacketPayload {
	public static final Type<twilightforest.network.PerformSidestepPacket> TYPE = new Type<>(TwilightForestMod.prefix("perform_sidestep_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, twilightforest.network.PerformSidestepPacket> STREAM_CODEC = CustomPacketPayload.codec(twilightforest.network.PerformSidestepPacket::write, twilightforest.network.PerformSidestepPacket::new);
	public PerformSidestepPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		 this(registryFriendlyByteBuf.readBoolean());
	}

	private void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		registryFriendlyByteBuf.writeBoolean(isLeftStepSide);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(twilightforest.network.PerformSidestepPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			if (!TravellersGearLogic.tryPerformSidestep(ctx.player(), message.isLeftStepSide))
				TravellersGearLogic.handleSidestepAbuse(ctx.player());
		});
	}
}
