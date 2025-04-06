package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.item.TravellersArmorBeltItem;
import twilightforest.item.TravellersArmorItem;

public record SwapHotbarPacket() implements CustomPacketPayload {
	public static final Type<SwapHotbarPacket> TYPE = new Type<>(TwilightForestMod.prefix("swap_hotbar_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SwapHotbarPacket> STREAM_CODEC = CustomPacketPayload.codec(SwapHotbarPacket::write, SwapHotbarPacket::new);

	public SwapHotbarPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		this();
	}

	private void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {

	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SwapHotbarPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			TravellersArmorBeltItem.travellersTrySwapHotbar(ctx.player());});
	}
}
