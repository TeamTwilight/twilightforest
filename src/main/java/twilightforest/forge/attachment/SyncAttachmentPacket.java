package twilightforest.forge.attachment;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;
import twilightforest.forge.network.IPayloadContext;

/**
 * La valeur d'un attachement synchronise d'une entite, envoyee au client.
 *
 * <p>La valeur voyage deja encodee par le codec de son type ({@link AttachmentType#streamCodec}) :
 * le paquet n'a pas a connaitre les types d'attachements.</p>
 */
public record SyncAttachmentPacket(int entityId, String typeId, boolean present, byte[] data) implements CustomPacketPayload {

	public static final Type<SyncAttachmentPacket> TYPE = new Type<>(TwilightForestMod.prefix("sync_attachment"));

	public static final StreamCodec<RegistryFriendlyByteBuf, SyncAttachmentPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, SyncAttachmentPacket::entityId,
		ByteBufCodecs.STRING_UTF8, SyncAttachmentPacket::typeId,
		ByteBufCodecs.BOOL, SyncAttachmentPacket::present,
		ByteBufCodecs.BYTE_ARRAY, SyncAttachmentPacket::data,
		SyncAttachmentPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SyncAttachmentPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> Attachments.applySync(context.player(), packet));
	}
}
