package twilightforest.network;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Per-player throttle for serverbound packets whose handlers do real work (recipe scans, inventory swaps, sounds,
 * slot resyncs) and that a client could otherwise send as fast as it likes. Only used from the server thread
 * (inside {@code enqueueWork}), so no synchronization. Entries die with the player object.
 */
public final class PacketRateLimiter {
	private static final Map<Player, Map<String, Window>> WINDOWS = new WeakHashMap<>();

	private PacketRateLimiter() {
	}

	/**
	 * @return whether this packet may be handled: at most {@code maxPerWindow} accepted packets of {@code key} per
	 * {@code windowTicks} ticks of the player's age.
	 */
	public static boolean tryAcquire(Player player, String key, int windowTicks, int maxPerWindow) {
		Window window = WINDOWS.computeIfAbsent(player, p -> new HashMap<>()).computeIfAbsent(key, k -> new Window());
		int now = player.tickCount;
		if (now - window.start >= windowTicks || now < window.start) {
			window.start = now;
			window.count = 0;
		}
		if (window.count >= maxPerWindow)
			return false;
		window.count++;
		return true;
	}

	private static final class Window {
		int start = Integer.MIN_VALUE / 2;
		int count;
	}
}
