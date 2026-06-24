package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.item.mapdata.TFMagicMapData;

import java.util.List;

// Rewraps vanilla ClientboundMapItemDataPacket to sync conquered status of structures
public record MagicMapPacket(ClientboundMapItemDataPacket inner, List<String> conqueredStructures) implements CustomPacketPayload {
	public static final Type<@NotNull MagicMapPacket> TYPE = new Type<>(TwilightForestMod.prefix("magic_map"));
	public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull MagicMapPacket> STREAM_CODEC = StreamCodec.composite(
		ClientboundMapItemDataPacket.STREAM_CODEC, MagicMapPacket::inner,
		ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), MagicMapPacket::conqueredStructures,
		MagicMapPacket::new);

	@Override
	public Type<? extends @NotNull CustomPacketPayload> type() {
		return TYPE;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(MagicMapPacket message, IPayloadContext ctx) {
		if (ctx.flow().isClientbound()) {
			ctx.enqueueWork(new Runnable() {
				@Override
				public void run() {
					ClientLevel level = (ClientLevel) ctx.player().level();
					MapId s = message.inner.mapId();

					TFMagicMapData mapdata = TFMagicMapData.getMagicMapData(level, s);
					if (mapdata == null) {
						mapdata = new TFMagicMapData(0, 0, message.inner.scale(), false, false, message.inner.locked(), level.dimension());
						TFMagicMapData.registerMagicMapData(level, mapdata, s);
					}

					message.inner.applyToMap(mapdata);
					mapdata.conqueredStructures.clear();
					mapdata.conqueredStructures.addAll(message.conqueredStructures());

					level.overrideMapData(s, mapdata);

					Minecraft.getInstance().getMapTextureManager().update(s, mapdata);
				}
			});
		}
	}
}