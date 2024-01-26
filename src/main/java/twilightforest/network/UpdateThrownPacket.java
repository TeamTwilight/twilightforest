package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;

public record UpdateThrownPacket(int entityID, boolean thrown, int thrower, int throwCooldown) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("update_thrown_attachment");

	public UpdateThrownPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readBoolean(), buf.readInt(), buf.readInt());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
		buf.writeBoolean(this.thrown());
		buf.writeInt(this.thrower());
		buf.writeInt(this.throwCooldown());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(UpdateThrownPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			Level level = ctx.level().orElseThrow();
			Entity entity = level.getEntity(message.entityID());
			if (entity instanceof Player player) {
				var attachment = player.getData(TFDataAttachments.YETI_THROWING);
				LivingEntity thrower = message.thrower() != 0 ? (LivingEntity) level.getEntity(message.thrower()) : null;
				attachment.setThrown(player, message.thrown(), thrower);
				attachment.setThrowCooldown(player, message.throwCooldown());
			}
		});
	}
}
