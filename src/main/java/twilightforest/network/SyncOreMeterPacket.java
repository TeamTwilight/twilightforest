package twilightforest.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;
import twilightforest.item.OreMeterItem;

public record SyncOreMeterPacket(CompoundTag tag, int slot) implements CustomPacketPayload {
	public static final ResourceLocation ID = TwilightForestMod.prefix("sync_ore_meter");

	public SyncOreMeterPacket(FriendlyByteBuf buf) {
		this(buf.readNbt(), buf.readInt());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeNbt(this.tag());
		buf.writeInt(this.slot());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(SyncOreMeterPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			Player player = ctx.player().orElseThrow();
			SlotAccess slot = player.getSlot(message.slot());
			ItemStack itemInSlot = slot.get();
			// Hacky inferencing but no other choice is given by inventoryTick()'s lack context about where in player's inventory an item may be
			if (itemInSlot.is(TFItems.ORE_METER)) { // Ensure we're not modifying nbt on some random item because of a desync/manmade packet
				itemInSlot.getOrCreateTag().put(OreMeterItem.NBT_SCAN_DATA, message.tag);
			} else {
				ItemStack offhandItem = player.getOffhandItem();
				if (offhandItem.is(TFItems.ORE_METER)) {
                    offhandItem.getOrCreateTag().put(OreMeterItem.NBT_SCAN_DATA, message.tag);
                }
            }
		});
	}
}
