package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.MovingCicadaSoundInstance;

public record CreateMovingCicadaSoundPacket(int entityID) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("create_cicada_sound");

	public CreateMovingCicadaSoundPacket(FriendlyByteBuf buf) {
		this(buf.readInt());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(CreateMovingCicadaSoundPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(new Runnable() {
			@Override
			public void run() {
				Entity entity = ctx.level().orElseThrow().getEntity(message.entityID());
				if (entity instanceof LivingEntity living) {
					Minecraft.getInstance().getSoundManager().queueTickingSound(new MovingCicadaSoundInstance(living));
				}
			}
		});
	}
}
