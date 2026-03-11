package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;

public record UpdateDeathTimePacket(int entityID, int deathTime) implements CustomPacketPayload {

	public static final Type<UpdateDeathTimePacket> TYPE = new Type<>(TwilightForestMod.prefix("death_time_update"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateDeathTimePacket> STREAM_CODEC = CustomPacketPayload.codec(UpdateDeathTimePacket::write, UpdateDeathTimePacket::read);

	public static UpdateDeathTimePacket read(RegistryFriendlyByteBuf buf) {
		return new UpdateDeathTimePacket(buf.readInt(), buf.readInt());
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
		buf.writeInt(this.deathTime());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(UpdateDeathTimePacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			ClientLevel level = Minecraft.getInstance().level;
			if (level != null && Minecraft.getInstance().level.getEntity(message.entityID) instanceof LivingEntity living) {
                living.deathTime = message.deathTime();
            }
		});
	}
}
