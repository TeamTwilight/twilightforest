package twilightforest.init;

import com.google.common.base.Suppliers;
import net.minecraft.Util;
import net.minecraft.world.level.GameRules;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.network.EnforceProgressionStatusPacket;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class TFGameRules {
	private static final Set<Supplier<GameRules.Key<?>>> GAME_RULES = new HashSet<>();

	public static final Supplier<GameRules.Key<GameRules.BooleanValue>> ENFORCED_PROGRESSION_RULE = register("tfEnforcedProgression",
		GameRules.Category.UPDATES,
		GameRules.BooleanValue.create(true, (server, value) ->
			// sends a packet to every player online when this changes
			PacketDistributor.sendToAllPlayers(new EnforceProgressionStatusPacket(value.get()))
		)
	);

	public static final Supplier<GameRules.Key<GameRules.IntegerValue>> RULE_PLAYERS_TF_PORTAL_DEFAULT_DELAY = register("playersTfPortalDefaultDelay",
		GameRules.Category.PLAYER,
		GameRules.IntegerValue.create(60)
	);

	public static final Supplier<GameRules.Key<GameRules.IntegerValue>> RULE_PLAYERS_TF_PORTAL_CREATIVE_DELAY = register("playersTfPortalCreativeDelay",
		GameRules.Category.PLAYER,
		GameRules.IntegerValue.create(1)
	);

	@SuppressWarnings("unchecked")
	private static <T extends GameRules.Value<T>> Supplier<GameRules.Key<T>> register(String name, GameRules.Category category, GameRules.Type<T> type) {
		Supplier<GameRules.Key<T>> supplier = Suppliers.memoize(() -> GameRules.register(name, category, type));
		GAME_RULES.add((Supplier<GameRules.Key<?>>) (Object) supplier);
		return supplier;
	}

	public static void register() {
		// Get main thread and use it to register our game rules early
		GAME_RULES.forEach(Supplier::get);
	}
}
