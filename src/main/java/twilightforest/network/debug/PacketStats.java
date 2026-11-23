package twilightforest.network.debug;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Debug-only outbound packet counter. Enable with the JVM flag {@code -Dtwilightforest.packetStats=true}
 * (optionally {@code -Dtwilightforest.packetStats.interval=<ticks>}, default 200).
 * <p>
 * Installs two Netty handlers around each remote player's "encoder": one sees the {@link Packet} object and records its
 * type, the other sees the encoded (uncompressed) bytes and attributes the size to that type. Every interval the totals
 * across all players are logged, sorted by packet count, then reset.
 * In-memory (singleplayer) connections have no encoder and are skipped.
 */
public final class PacketStats {
	public static final boolean ENABLED = Boolean.getBoolean("twilightforest.packetStats");
	private static final int INTERVAL = Integer.getInteger("twilightforest.packetStats.interval", 200);
	private static final int TOP_N = 25;
	private static final String TYPE_HANDLER = "twilightforest:packet_stats_type";
	private static final String SIZE_HANDLER = "twilightforest:packet_stats_size";
	private static final AttributeKey<Stat> CURRENT = AttributeKey.valueOf("twilightforest:packet_stats_current");

	private static final Map<String, Stat> STATS = new ConcurrentHashMap<>();
	private static int ticks;

	private PacketStats() {
	}

	public static void init() {
		if (!ENABLED) return;
		TwilightForestMod.LOGGER.info("[PacketStats] enabled, reporting every {} ticks", INTERVAL);
		NeoForge.EVENT_BUS.addListener(PacketStats::onLogin);
		NeoForge.EVENT_BUS.addListener(PacketStats::onServerTick);
	}

	private static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) return;
		Channel channel = player.connection.getConnection().channel();
		if (channel == null) return;
		channel.eventLoop().execute(() -> {
			ChannelPipeline pipeline = channel.pipeline();
			if (pipeline.get("encoder") == null || pipeline.get(TYPE_HANDLER) != null) return;
			try {
				pipeline.addAfter("encoder", TYPE_HANDLER, new TypeHandler());
				pipeline.addBefore("encoder", SIZE_HANDLER, new SizeHandler());
			} catch (RuntimeException e) {
				TwilightForestMod.LOGGER.warn("[PacketStats] could not install handlers", e);
			}
		});
	}

	private static void onServerTick(ServerTickEvent.Post event) {
		if (++ticks < INTERVAL) return;
		ticks = 0;
		report(INTERVAL / 20.0D);
	}

	private static void report(double seconds) {
		List<Map.Entry<String, long[]>> rows = new ArrayList<>();
		long totalCount = 0, totalBytes = 0;
		for (Map.Entry<String, Stat> entry : STATS.entrySet()) {
			long count = entry.getValue().count.sumThenReset();
			long bytes = entry.getValue().bytes.sumThenReset();
			if (count == 0) continue;
			totalCount += count;
			totalBytes += bytes;
			rows.add(Map.entry(entry.getKey(), new long[]{count, bytes}));
		}
		if (rows.isEmpty()) return;
		rows.sort((a, b) -> Long.compare(b.getValue()[0], a.getValue()[0]));

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("[PacketStats] last %.0fs: %.1f pkt/s, %.1f KB/s (uncompressed, all players)%n",
			seconds, totalCount / seconds, totalBytes / seconds / 1024D));
		for (int i = 0; i < Math.min(TOP_N, rows.size()); i++) {
			Map.Entry<String, long[]> row = rows.get(i);
			sb.append(String.format("  %8.1f pkt/s %9.2f KB/s  %s%n",
				row.getValue()[0] / seconds, row.getValue()[1] / seconds / 1024D, row.getKey()));
		}
		TwilightForestMod.LOGGER.info(sb.toString().stripTrailing());
	}

	private static String keyOf(Object msg) {
		if (msg instanceof ClientboundCustomPayloadPacket custom)
			return "payload " + custom.payload().type().id();
		return msg.getClass().getSimpleName();
	}

	private static final class Stat {
		final LongAdder count = new LongAdder();
		final LongAdder bytes = new LongAdder();
	}

	/** Sits on the tail side of the encoder: sees packet objects. */
	private static final class TypeHandler extends ChannelOutboundHandlerAdapter {
		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			if (msg instanceof Packet<?>) {
				Stat stat = STATS.computeIfAbsent(keyOf(msg), k -> new Stat());
				stat.count.increment();
				ctx.channel().attr(CURRENT).set(stat);
			}
			super.write(ctx, msg, promise);
		}
	}

	/** Sits on the head side of the encoder: sees the encoded bytes of the packet just recorded. */
	private static final class SizeHandler extends ChannelOutboundHandlerAdapter {
		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			if (msg instanceof ByteBuf buf) {
				Stat stat = ctx.channel().attr(CURRENT).getAndSet(null);
				if (stat != null) stat.bytes.add(buf.readableBytes());
			}
			super.write(ctx, msg, promise);
		}
	}
}
