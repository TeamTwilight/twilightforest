package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;

public record EnforceProgressionStatusPacket(boolean enforce) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("sync_progression_status");

	public EnforceProgressionStatusPacket(FriendlyByteBuf buf) {
		this(buf.readBoolean());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBoolean(this.enforce);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(EnforceProgressionStatusPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() ->
				ctx.level().orElseThrow().getGameRules().getRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE).set(message.enforce(), null)
		);
	}
}
