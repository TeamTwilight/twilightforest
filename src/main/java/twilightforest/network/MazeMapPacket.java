package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.item.mapdata.TFMazeMapData;
import twilightforest.item.MazeMapItem;

// Rewraps vanilla ClientboundMapItemDataPacket to properly add our own data
public record MazeMapPacket(ClientboundMapItemDataPacket inner, boolean ore,
							int yCenter) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("maze_map");

	public MazeMapPacket(FriendlyByteBuf buf) {
		this(new ClientboundMapItemDataPacket(buf), buf.readBoolean(), buf.readVarInt());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		this.inner().write(buf);
		buf.writeBoolean(this.ore());
		buf.writeVarInt(this.yCenter());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(MazeMapPacket message, PlayPayloadContext ctx) {
		//ensure this is only done on clients as this uses client only code
		if (ctx.flow().isClientbound()) {
			ctx.workHandler().execute(() -> {
				Level level = ctx.level().orElseThrow();
				// [VanillaCopy] ClientPlayNetHandler#handleMaps with our own mapdatas
				MapRenderer mapitemrenderer = Minecraft.getInstance().gameRenderer.getMapRenderer();
				String s = MazeMapItem.getMapName(message.inner().getMapId());
				TFMazeMapData mapdata = TFMazeMapData.getMazeMapData(level, s);
				if (mapdata == null) {
					mapdata = new TFMazeMapData(0, 0, message.inner().getScale(), false, false, message.inner().isLocked(), level.dimension());
					TFMazeMapData.registerMazeMapData(level, mapdata, s);
				}

				mapdata.ore = message.ore();
				mapdata.yCenter = message.yCenter();
				message.inner().applyToMap(mapdata);
				mapitemrenderer.update(message.inner().getMapId(), mapdata);
			});
		}
	}
}