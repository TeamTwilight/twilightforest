package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.MovingCicadaSoundInstance;

public record CreateMovingCicadaSoundPacket(int entityID) implements CustomPacketPayload {

	public static final Type<CreateMovingCicadaSoundPacket> TYPE = new Type<>(TwilightForestMod.prefix("create_cicada_sound"));
	public static final StreamCodec<RegistryFriendlyByteBuf, CreateMovingCicadaSoundPacket> STREAM_CODEC = CustomPacketPayload.codec(CreateMovingCicadaSoundPacket::write, CreateMovingCicadaSoundPacket::new);

	public CreateMovingCicadaSoundPacket(FriendlyByteBuf buf) {
		this(buf.readInt());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(CreateMovingCicadaSoundPacket message, IPayloadContext ctx) {
		if (ctx.flow().isClientbound()) {
			ctx.enqueueWork(new Runnable() {
				@Override
				public void run() {
					Entity entity = ctx.player().level().getEntity(message.entityID());
					if (entity instanceof LivingEntity living) {
						Minecraft.getInstance().getSoundManager().queueTickingSound(new MovingCicadaSoundInstance(living));
					}
				}
			});
		}
	}
}
