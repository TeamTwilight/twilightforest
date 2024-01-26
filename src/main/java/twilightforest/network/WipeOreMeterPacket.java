package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;
import twilightforest.item.OreMeterItem;

public record WipeOreMeterPacket(int slot, InteractionHand hand) implements CustomPacketPayload {
	public static final ResourceLocation ID = TwilightForestMod.prefix("wipe_ore_meter");

	public WipeOreMeterPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.slot());
		buf.writeBoolean(this.hand() == InteractionHand.MAIN_HAND);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(WipeOreMeterPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			// Hacky inferencing but no other choice is given by inventoryTick()'s lack context about where in player's inventory an item may be
			if (message.hand == InteractionHand.MAIN_HAND) {
				SlotAccess slot = ctx.player().orElseThrow().getSlot(message.slot());
				ItemStack itemInSlot = slot.get();
				if (itemInSlot.is(TFItems.ORE_METER)) { // Ensure we're not modifying data on some random item because of a desync/manmade packet
					itemInSlot.getOrCreateTag().remove(OreMeterItem.NBT_SCAN_DATA);
                }
			} else {
				ItemStack itemInSlot = ctx.player().orElseThrow().getOffhandItem();
				if (itemInSlot.is(TFItems.ORE_METER)) {
                    itemInSlot.getOrCreateTag().remove(OreMeterItem.NBT_SCAN_DATA);
                }
			}
		});
	}
}
