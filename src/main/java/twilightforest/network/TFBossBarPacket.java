package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.world.BossEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.bar.ClientTFBossBar;
import twilightforest.entity.boss.bar.ServerTFBossBar;

import java.util.UUID;

public abstract class TFBossBarPacket implements CustomPacketPayload {
	protected final UUID id;

	protected TFBossBarPacket(ServerTFBossBar bossEvent) {
		this.id = bossEvent.getId();
	}

	public TFBossBarPacket(RegistryFriendlyByteBuf buf) {
		this.id = buf.readUUID();
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeUUID(this.id);
	}

	public static class AddTFBossBarPacket extends TFBossBarPacket {
		private final Component name;
		private final float progress;
		private final int color;
		private final BossEvent.BossBarOverlay overlay;
		private final boolean darkenScreen;
		private final boolean playMusic;
		private final boolean createWorldFog;

		public static final Type<AddTFBossBarPacket> TYPE = new Type<>(TwilightForestMod.prefix("add_tf_boss_bar"));
		public static final StreamCodec<RegistryFriendlyByteBuf, AddTFBossBarPacket> STREAM_CODEC = CustomPacketPayload.codec(AddTFBossBarPacket::write, AddTFBossBarPacket::new);

		public AddTFBossBarPacket(ServerTFBossBar bossEvent) {
			super(bossEvent);
			this.name = bossEvent.getName();
			this.progress = bossEvent.getProgress();
			this.color = bossEvent.getBarColor();
			this.overlay = bossEvent.getOverlay();
			this.darkenScreen = bossEvent.shouldDarkenScreen();
			this.playMusic = bossEvent.shouldPlayBossMusic();
			this.createWorldFog = bossEvent.shouldCreateWorldFog();
		}

		public AddTFBossBarPacket(RegistryFriendlyByteBuf buf) {
			super(buf);
			this.name = ComponentSerialization.STREAM_CODEC.decode(buf);
			this.progress = buf.readFloat();
			this.color = buf.readInt();
			this.overlay = buf.readEnum(BossEvent.BossBarOverlay.class);
			int i = buf.readUnsignedByte();
			this.darkenScreen = (i & 1) > 0;
			this.playMusic = (i & 2) > 0;
			this.createWorldFog = (i & 4) > 0;
		}

		@Override
		public void write(RegistryFriendlyByteBuf buf) {
			super.write(buf);
			ComponentSerialization.STREAM_CODEC.encode(buf, this.name);
			buf.writeFloat(this.progress);
			buf.writeInt(this.color);
			buf.writeEnum(this.overlay);
			buf.writeByte(ClientboundBossEventPacket.encodeProperties(this.darkenScreen, this.playMusic, this.createWorldFog));
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		@SuppressWarnings("Convert2Lambda")
		public static void handle(AddTFBossBarPacket packet, IPayloadContext ctx) {
			if (ctx.flow().isClientbound()) {
				ctx.enqueueWork(new Runnable() {
					@Override
					public void run() {
						Minecraft minecraft = Minecraft.getInstance();
						minecraft.gui.getBossOverlay().events.put(packet.id, new ClientTFBossBar(packet.id, packet.name, packet.progress, packet.color, packet.overlay, packet.darkenScreen, packet.playMusic, packet.createWorldFog));
					}
				});
			}
		}
	}

	public static class UpdateTFBossBarStylePacket extends TFBossBarPacket {
		private final int color;
		private final BossEvent.BossBarOverlay overlay;
		private final boolean allowLerp;

		public static final Type<UpdateTFBossBarStylePacket> TYPE = new Type<>(TwilightForestMod.prefix("update_tf_boss_bar_style"));
		public static final StreamCodec<RegistryFriendlyByteBuf, UpdateTFBossBarStylePacket> STREAM_CODEC = CustomPacketPayload.codec(UpdateTFBossBarStylePacket::write, UpdateTFBossBarStylePacket::new);

		public UpdateTFBossBarStylePacket(ServerTFBossBar bossEvent, boolean allowLerp) {
			super(bossEvent);
			this.color = bossEvent.getBarColor();
			this.overlay = bossEvent.getOverlay();
			this.allowLerp = allowLerp;
		}

		public UpdateTFBossBarStylePacket(RegistryFriendlyByteBuf buf) {
			super(buf);
			this.color = buf.readInt();
			this.overlay = buf.readEnum(BossEvent.BossBarOverlay.class);
			this.allowLerp = buf.readBoolean();
		}

		@Override
		public void write(RegistryFriendlyByteBuf buf) {
			super.write(buf);
			buf.writeInt(this.color);
			buf.writeEnum(this.overlay);
			buf.writeBoolean(this.allowLerp);
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		@SuppressWarnings("Convert2Lambda")
		public static void handle(UpdateTFBossBarStylePacket packet, IPayloadContext ctx) {
			if (ctx.flow().isClientbound()) {
				ctx.enqueueWork(new Runnable() {
					@Override
					public void run() {
						Minecraft minecraft = Minecraft.getInstance();
						if (minecraft.gui.getBossOverlay().events.get(packet.id) instanceof ClientTFBossBar bossEvent) {
							bossEvent.setBarColor(packet.color);
							bossEvent.setOverlay(packet.overlay);
							if (!packet.allowLerp) bossEvent.setSetTime(bossEvent.getSetTime() - 200L); // Boss bars lerp over 100 milliseconds, we sometimes don't want that
						}
					}
				});
			}
		}
	}
}
