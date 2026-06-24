package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFSounds;
import twilightforest.init.custom.TravellersModifiersManager;

import java.util.UUID;

public record GogglesZoomPacket(boolean isUsingZoom, UUID playerUUID) implements CustomPacketPayload {
	public static final Type<GogglesZoomPacket> TYPE = new Type<>(TwilightForestMod.prefix("goggles_zoom_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, twilightforest.network.GogglesZoomPacket> STREAM_CODEC = CustomPacketPayload.codec(twilightforest.network.GogglesZoomPacket::write, twilightforest.network.GogglesZoomPacket::new);

	public GogglesZoomPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		this(registryFriendlyByteBuf.readBoolean(), registryFriendlyByteBuf.readUUID());
	}

	private void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		registryFriendlyByteBuf.writeBoolean(isUsingZoom);
		registryFriendlyByteBuf.writeUUID(playerUUID);
	}

	public static void handle(GogglesZoomPacket packet, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			Player player = ctx.player().level().getPlayerByUUID(packet.playerUUID);
			if (player == null)
				return;
			if (player.level().isClientSide()) {
				player.setData(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, packet.isUsingZoom);
				return;
			}

			boolean canChangeZoomState = TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ZOOM_ABILITY);
			if (canChangeZoomState) {
				player.setData(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, packet.isUsingZoom);
				player.playSound(packet.isUsingZoom ? TFSounds.GOGGLES_ZOOM_IN.get() : TFSounds.GOGGLES_ZOOM_OUT.get());
				PacketDistributor.sendToPlayersTrackingEntity(player, new GogglesZoomPacket(packet.isUsingZoom, player.getUUID()));
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
