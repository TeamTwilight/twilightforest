package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.emitter.PortalItemEmitter;

public record AddEmitterPacket(int entityID) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<AddEmitterPacket> TYPE = new CustomPacketPayload.Type<>(TwilightForestMod.prefix("create_invalid_portal_emitter"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AddEmitterPacket> STREAM_CODEC = CustomPacketPayload.codec(AddEmitterPacket::write, AddEmitterPacket::new);

	public AddEmitterPacket(FriendlyByteBuf buf) {
		this(buf.readInt());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(AddEmitterPacket message, IPayloadContext ctx) {
		if (ctx.flow().isClientbound()) {
			ctx.enqueueWork(() -> {
				ClientLevel level = Minecraft.getInstance().level;
				if (level == null)
					return;

				Entity entity = level.getEntity(message.entityID());
				if (!(entity instanceof ItemEntity item))
					return;

				boolean alreadyExists = Minecraft.getInstance().particleEngine.trackingEmitters.stream()
					.filter(PortalItemEmitter.class::isInstance)
					.map(PortalItemEmitter.class::cast)
					.anyMatch(emitter -> emitter.isTrackingEntity(item));

				if (!alreadyExists) {
					Minecraft.getInstance().particleEngine.trackingEmitters.add(new PortalItemEmitter(level, item));
				}
			});
		}
	}
}
