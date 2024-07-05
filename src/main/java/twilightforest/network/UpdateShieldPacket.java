package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;

public record UpdateShieldPacket(int entityID, int temporaryShields, int permanentShields) implements CustomPacketPayload {

	public static final Type<UpdateShieldPacket> TYPE = new Type<>(TwilightForestMod.prefix("update_shield_attachment"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateShieldPacket> STREAM_CODEC = CustomPacketPayload.codec(UpdateShieldPacket::write, UpdateShieldPacket::new);

	public UpdateShieldPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readInt(), buf.readInt());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
		buf.writeInt(this.temporaryShields());
		buf.writeInt(this.permanentShields());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(UpdateShieldPacket message, IPayloadContext ctx) {
		//ensure this is only done on clients as this uses client only code
		//the level is not yet set in the payload context when a player logs in, so we need to fall back to the clientlevel instead
		if (ctx.flow().isClientbound()) {
			ctx.enqueueWork(new Runnable() {
				@Override
				public void run() {
					Entity entity = ctx.player().level().getEntity(message.entityID);
					if (entity instanceof LivingEntity living) {
						var attachment = living.getData(TFDataAttachments.FORTIFICATION_SHIELDS);
						attachment.setShields(living, message.temporaryShields, true);
						attachment.setShields(living, message.permanentShields, false);
					}
				}
			});
		}
	}
}
