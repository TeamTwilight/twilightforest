package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.config.ConfigSetup;
import twilightforest.config.TFCommonConfig;
import twilightforest.config.TFConfig;
import twilightforest.TwilightForestMod;

import java.util.List;

public record SyncUncraftingTableConfigPacket(
		double uncraftingMultiplier, double repairingMultiplier,
		boolean allowShapeless, boolean disableIngredientSwitching, boolean disabledUncrafting, boolean disabledTable,
		List<? extends String> disabledRecipes, boolean flipRecipeList,
		List<? extends String> disabledModids, boolean flipModidList) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("sync_uncrafting_config");

	public SyncUncraftingTableConfigPacket(FriendlyByteBuf buf) {
		this(buf.readDouble(), buf.readDouble(),
				buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
				buf.readList(FriendlyByteBuf::readUtf), buf.readBoolean(),
				buf.readList(FriendlyByteBuf::readUtf), buf.readBoolean());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeDouble(this.uncraftingMultiplier());
		buf.writeDouble(this.repairingMultiplier());
		buf.writeBoolean(this.allowShapeless());
		buf.writeBoolean(this.disableIngredientSwitching());
		buf.writeBoolean(this.disabledUncrafting());
		buf.writeBoolean(this.disabledTable());
		buf.writeCollection(this.disabledRecipes(), FriendlyByteBuf::writeUtf);
		buf.writeBoolean(this.flipRecipeList());
		buf.writeCollection(this.disabledModids(), FriendlyByteBuf::writeUtf);
		buf.writeBoolean(this.flipModidList());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(SyncUncraftingTableConfigPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			TFConfig.uncraftingXpCostMultiplier = message.uncraftingMultiplier();
			TFConfig.repairingXpCostMultiplier = message.repairingMultiplier();
			TFConfig.allowShapelessUncrafting = message.allowShapeless();
			TFConfig.disableIngredientSwitching = message.disableIngredientSwitching();
			TFConfig.disableUncraftingOnly = message.disabledUncrafting();
			TFConfig.disableEntireTable = message.disabledTable();
			TFConfig.disableUncraftingRecipes = message.disabledRecipes();
			TFConfig.reverseRecipeBlacklist = message.flipRecipeList();
			TFConfig.blacklistedUncraftingModIds = message.disabledModids();
			TFConfig.flipUncraftingModIdList = message.flipModidList();
		});
	}
}
