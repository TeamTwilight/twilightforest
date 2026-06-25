package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.item.MazeMapItem;
import twilightforest.item.mapdata.TFMazeMapData;

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
					Level level = ctx.player().level();
					// [VanillaCopy] ClientPlayNetHandler#handleMaps with our own mapdatas
					MapId mapId = message.inner().mapId();
					TFMazeMapData mapdata = TFMazeMapData.getMazeMapData(level, mapId);
					if (mapdata == null) {
						mapdata = new TFMazeMapData(0, 0, message.inner().scale(), false, false, message.inner().locked(), level.dimension());
						TFMazeMapData.registerMazeMapData(level, mapdata, mapId);
					}

					mapdata.ore = message.ore();
					mapdata.yCenter = message.yCenter();
					message.inner().applyToMap(mapdata);
					//TF: update map texture from received color data
					Minecraft.getInstance().getMapTextureManager().update(message.inner().mapId(), mapdata);
				}
			});
		}
	}
}