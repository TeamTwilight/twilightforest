package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.inventory.UncraftingMenu;

public record UpdateUncraftingCostPacket(int uncraftingCost, int recraftingCost) implements CustomPacketPayload {

	public static final Type<UpdateUncraftingCostPacket> TYPE = new Type<>(TwilightForestMod.prefix("update_uncrafting_cost"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateUncraftingCostPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, UpdateUncraftingCostPacket::uncraftingCost,
		ByteBufCodecs.INT, UpdateUncraftingCostPacket::recraftingCost,
		UpdateUncraftingCostPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(UpdateUncraftingCostPacket message, IPayloadContext ctx) {
		if (ctx.flow().isClientbound()) {
			ctx.enqueueWork(() -> {
				AbstractContainerMenu container = ctx.player().containerMenu;

				if (container instanceof UncraftingMenu uncrafting) {
					uncrafting.updateCosts(message.uncraftingCost(), message.recraftingCost());
				}
			});
		}
	}
}
