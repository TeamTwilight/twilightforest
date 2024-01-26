package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;

public record UpdateFeatherFanFallPacket(int entityID, boolean falling) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("update_feather_fan_attachment");

	public UpdateFeatherFanFallPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readBoolean());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
		buf.writeBoolean(this.falling());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(UpdateFeatherFanFallPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			Entity entity = ctx.level().orElseThrow().getEntity(message.entityID());
			if (entity instanceof Player) {
				entity.setData(TFDataAttachments.FEATHER_FAN, message.falling());
			}
		});
	}
}
