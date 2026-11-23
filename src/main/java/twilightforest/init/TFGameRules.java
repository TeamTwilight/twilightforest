package twilightforest.init;

import com.google.common.base.Suppliers;
import net.minecraft.world.level.GameRules;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.network.EnforceProgressionStatusPacket;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class TFGameRules {

	/**
	 * Clients get a fresh level (with default game rules) on login, respawn and dimension change, but the progression
	 * rule was only sent when it changed, so a server with it off showed clients locked-area effects. Resend it then.
	 */
	public static void syncProgressionRule(net.neoforged.neoforge.event.entity.player.PlayerEvent event) {
		if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player)
			PacketDistributor.sendToPlayer(player, new EnforceProgressionStatusPacket(player.serverLevel().getGameRules().getBoolean(ENFORCED_PROGRESSION_RULE.get())));
	}
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

	public static void register(FMLCommonSetupEvent event) {
		// Register our gamerules out of parallel-modloading
		event.enqueueWork(() -> GAME_RULES.forEach(Supplier::get));
	}
}
