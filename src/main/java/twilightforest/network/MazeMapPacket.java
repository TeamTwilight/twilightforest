package twilightforest.network;

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
import twilightforest.item.mapdata.TFMazeMapData;

import java.util.stream.StreamSupport;

// Rewraps vanilla ClientboundMapItemDataPacket to properly add our own data
public record MazeMapPacket(ClientboundMapItemDataPacket inner, boolean ore, int yCenter) implements CustomPacketPayload {

	public static final Type<MazeMapPacket> TYPE = new Type<>(TwilightForestMod.prefix("maze_map"));

	public static final StreamCodec<RegistryFriendlyByteBuf, MazeMapPacket> STREAM_CODEC = StreamCodec.composite(
		ClientboundMapItemDataPacket.STREAM_CODEC, MazeMapPacket::inner,
		ByteBufCodecs.BOOL, MazeMapPacket::ore,
		ByteBufCodecs.INT, MazeMapPacket::yCenter,
		MazeMapPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(MazeMapPacket message, IPayloadContext ctx) {
		//ensure this is only done on clients as this uses client only code
		if (ctx.flow().isClientbound()) {
			ctx.enqueueWork(new Runnable() {
				@Override
				public void run() {
					if (!(ctx.player().level() instanceof ClientLevel clientLevel)) return;

					MapId mapId = message.inner.mapId();
					TFMazeMapData mapdata = MapDataManager.getClientMazeMapData(mapId);
					if (mapdata == null) {
						mapdata = new TFMazeMapData(
							0, 0,
							message.inner().scale(),
							false,
							false,
							message.inner().locked(),
							clientLevel.dimension()
						);
						MapDataManager.saveClientMazeMapData(mapId, mapdata);
					}

					mapdata.ore = message.ore();
					mapdata.yCenter = message.yCenter();
					message.inner().applyToMap(mapdata);

					MapItemSavedData saved = clientLevel.getMapData(message.inner().mapId());

					if (saved != null) {
						saved.addClientSideDecorations(
							StreamSupport.stream(mapdata.getDecorations().spliterator(), false).toList()
						);
					}
				}
			});
		}
	}
}