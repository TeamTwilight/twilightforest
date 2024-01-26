package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.inventory.UncraftingMenu;

public record UncraftingGuiPacket(int operationType) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("switch_uncrafting_operation");

	public UncraftingGuiPacket(FriendlyByteBuf buf) {
		this(buf.readInt());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.operationType());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(UncraftingGuiPacket message, PlayPayloadContext ctx) {
		if (ctx.flow().isServerbound()) {
			ctx.workHandler().execute(() -> {
				AbstractContainerMenu container = ctx.player().orElseThrow().containerMenu;

				if (container instanceof UncraftingMenu uncrafting) {
					switch (message.operationType()) {
						case 0 -> uncrafting.unrecipeInCycle++;
						case 1 -> uncrafting.unrecipeInCycle--;
						case 2 -> uncrafting.ingredientsInCycle++;
						case 3 -> uncrafting.ingredientsInCycle--;
						case 4 -> uncrafting.recipeInCycle++;
						case 5 -> uncrafting.recipeInCycle--;
					}

					if (message.operationType() < 4)
						uncrafting.slotsChanged(uncrafting.tinkerInput);

					if (message.operationType() >= 4)
						uncrafting.slotsChanged(uncrafting.assemblyMatrix);
				}
			});
		}
	}
}
