package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;

import java.util.UUID;

public record GradualGlidePacket(boolean isGraduallyGliding, UUID playerUUID) implements CustomPacketPayload {
	public static final Type<GradualGlidePacket> TYPE = new Type<>(TwilightForestMod.prefix("gradual_glide_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, GradualGlidePacket> STREAM_CODEC = CustomPacketPayload.codec(GradualGlidePacket::write, GradualGlidePacket::new);

	public GradualGlidePacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		this(registryFriendlyByteBuf.readBoolean(), registryFriendlyByteBuf.readUUID());
	}

	public static void handle(GradualGlidePacket packet, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			Player player = ctx.player().level().getPlayerByUUID(packet.playerUUID);
			if (player == null)
				return;
			if (player.level().isClientSide()) {
				player.setData(TFDataAttachments.IS_GRADUALLY_GLIDING, packet.isGraduallyGliding);
				return;
			}
			player.setData(TFDataAttachments.IS_GRADUALLY_GLIDING, packet.isGraduallyGliding);
			PacketDistributor.sendToPlayersTrackingEntity(player, new GradualGlidePacket(packet.isGraduallyGliding, player.getUUID()));
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
