package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
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

	public static final Type<SyncUncraftingTableConfigPacket> TYPE = new Type<>(TwilightForestMod.prefix("sync_uncrafting_config"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncUncraftingTableConfigPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncUncraftingTableConfigPacket::write, SyncUncraftingTableConfigPacket::new);

	public SyncUncraftingTableConfigPacket(FriendlyByteBuf buf) {
		this(buf.readDouble(), buf.readDouble(),
			buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
			buf.readList(FriendlyByteBuf::readUtf), buf.readBoolean(),
			buf.readList(FriendlyByteBuf::readUtf), buf.readBoolean());
	}

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
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SyncUncraftingTableConfigPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
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
