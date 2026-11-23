package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.custom.TravellersModifiersManager;

import java.util.UUID;

public record GradualGlidePacket(boolean isGraduallyGliding, UUID playerUUID) implements CustomPacketPayload {
	public static final Type<GradualGlidePacket> TYPE = new Type<>(TwilightForestMod.prefix("gradual_glide_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, GradualGlidePacket> STREAM_CODEC = CustomPacketPayload.codec(GradualGlidePacket::write, GradualGlidePacket::new);

	public GradualGlidePacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		this(registryFriendlyByteBuf.readBoolean(), registryFriendlyByteBuf.readUUID());
	}

	public static void handle(GradualGlidePacket packet, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			if (ctx.flow() == PacketFlow.CLIENTBOUND) {
				Player player = ctx.player().level().getPlayerByUUID(packet.playerUUID);
				if (player != null)
					player.setData(TFDataAttachments.IS_GRADUALLY_GLIDING, packet.isGraduallyGliding);
				return;
			}
			// Serverbound: only ever act on the sender. The UUID field is ignored here so a client can't change
			// another player's state; it stays in the payload to keep the wire format compatible.
			Player player = ctx.player();
			boolean gliding = packet.isGraduallyGliding && TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.GRADUAL_GLIDE_MODIFIER);
			if (gliding == player.getData(TFDataAttachments.IS_GRADUALLY_GLIDING))
				return;
			player.setData(TFDataAttachments.IS_GRADUALLY_GLIDING, gliding);
			PacketDistributor.sendToPlayersTrackingEntity(player, new GradualGlidePacket(gliding, player.getUUID()));
		});
	}

	private void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		registryFriendlyByteBuf.writeBoolean(isGraduallyGliding);
		registryFriendlyByteBuf.writeUUID(playerUUID);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
