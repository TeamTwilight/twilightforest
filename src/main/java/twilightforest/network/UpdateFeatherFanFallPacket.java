package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;

public record UpdateFeatherFanFallPacket(int entityID, boolean falling) implements CustomPacketPayload {

	public static final Type<UpdateFeatherFanFallPacket> TYPE = new Type<>(TwilightForestMod.prefix("update_feather_fan_attachment"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateFeatherFanFallPacket> STREAM_CODEC = CustomPacketPayload.codec(UpdateFeatherFanFallPacket::write, UpdateFeatherFanFallPacket::new);

	public UpdateFeatherFanFallPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readBoolean());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
		buf.writeBoolean(this.falling());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(UpdateFeatherFanFallPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			Entity entity = ctx.player().level().getEntity(message.entityID());
			if (entity instanceof Player) {
				entity.setData(TFDataAttachments.FEATHER_FAN, message.falling());
			}
		});
	}
}
