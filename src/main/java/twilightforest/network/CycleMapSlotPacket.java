package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFSounds;
import twilightforest.init.custom.TravellersModifiersManager;

public record CycleMapSlotPacket() implements CustomPacketPayload {
	public static final CycleMapSlotPacket INSTANCE = new CycleMapSlotPacket();
	public static final Type<CycleMapSlotPacket> TYPE = new Type<>(TwilightForestMod.prefix("cycle_map_slot_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, CycleMapSlotPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	public static void handle(CycleMapSlotPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			Player player = ctx.player();
			if (!(player instanceof ServerPlayer serverPlayer))
				return;

			ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
			ItemDisplayContents contents = headStack.get(TFDataComponents.ITEM_DISPLAY);
			if (contents == null || contents.isEmpty() || !TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ITEM_DISPLAY_MODIFIER))
				return;

			ItemDisplayContents.Mutable mutable = new ItemDisplayContents.Mutable(contents);
			int oldIndex = mutable.chosenMapSlot();
			int newIndex = mutable.cycleChosenMapSlot();

			if (oldIndex != newIndex) {
				ItemDisplayContents updatedContents = mutable.toImmutable();
				headStack.set(TFDataComponents.ITEM_DISPLAY, updatedContents);
				serverPlayer.getInventory().setChanged();
				player.playSound(newIndex == -1 ? TFSounds.CYCLE_MAPS_EMPTY.get() : TFSounds.CYCLE_MAPS.get(), 1F, 1F);
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
