package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.MissingAdvancementToast;

public record MissingAdvancementToastPacket(Component title, ItemStack icon) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("missing_advancement_toast");

	public MissingAdvancementToastPacket(FriendlyByteBuf buf) {
		this(buf.readComponent(), buf.readItem());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeComponent(this.title());
		buf.writeItem(this.icon());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(MissingAdvancementToastPacket packet, PlayPayloadContext ctx) {
		//ensure this is only done on clients as this uses client only code
		if (ctx.flow().isClientbound()) {
			ctx.workHandler().execute(new Runnable() {
				@Override
				public void run() {
					Minecraft.getInstance().getToasts().addToast(new MissingAdvancementToast(packet.title(), packet.icon()));
				}
			});
		}
	}
}