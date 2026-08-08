package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.item.mapdata.MapDataManager;
import twilightforest.item.mapdata.TFMagicMapData;

import java.util.List;
import java.util.stream.StreamSupport;

// Rewraps vanilla ClientboundMapItemDataPacket to sync conquered status of structures
public record MagicMapPacket(ClientboundMapItemDataPacket inner, List<String> conqueredStructures) implements CustomPacketPayload {

	public static final Type<MagicMapPacket> TYPE = new Type<>(TwilightForestMod.prefix("magic_map"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MagicMapPacket> STREAM_CODEC = StreamCodec.composite(
		ClientboundMapItemDataPacket.STREAM_CODEC, MagicMapPacket::inner,
		ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), MagicMapPacket::conqueredStructures,
		MagicMapPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(MagicMapPacket message, IPayloadContext ctx) {
		//ensure this is only done on clients as this uses client only code
		if (ctx.flow().isClientbound()) {
			ctx.enqueueWork(new Runnable() {
				@Override
				public void run() {
					if (!(ctx.player().level() instanceof ClientLevel clientLevel)) return;

					MapId mapId = message.inner.mapId();
					TFMagicMapData mapdata = MapDataManager.getClientMagicMapData(mapId);
					if (mapdata == null) {
						mapdata = new TFMagicMapData(0, 0, message.inner.scale(), false, false, message.inner.locked(), clientLevel.dimension());
						MapDataManager.saveClientMagicMapData(mapId, mapdata);
					}

					message.inner.applyToMap(mapdata);
					//TF: sync conquered structures for map
					mapdata.conqueredStructures.clear();
					mapdata.conqueredStructures.addAll(message.conqueredStructures());

					MapItemSavedData saved = clientLevel.getMapData(message.inner.mapId());

					if (saved != null) {
						saved.addClientSideDecorations(
							StreamSupport.stream(mapdata.getDecorations().spliterator(), false).toList()
						);
					}
					Minecraft.getInstance().getMapTextureManager().update(message.inner.mapId(), mapdata);
				}
			});
		}
	}
}