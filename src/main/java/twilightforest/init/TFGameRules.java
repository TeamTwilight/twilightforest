package twilightforest.init;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;

public class TFGameRules {
	public static final DeferredRegister<GameRule<?>> RULES = DeferredRegister.create(Registries.GAME_RULE, TwilightForestMod.ID);

	public static final DeferredHolder<GameRule<?>, GameRule<Boolean>> ENFORCED_PROGRESSION_RULE = registerBoolean("twilightforest_enforced_progression", GameRuleCategory.UPDATES, true);
	public static final DeferredHolder<GameRule<?>, GameRule<Integer>> TF_PORTAL_DEFAULT_DELAY = registerInteger("players_twilight_portal_default_delay", GameRuleCategory.PLAYER, 60, 0);
	public static final DeferredHolder<GameRule<?>, GameRule<Integer>> TF_PORTAL_CREATIVE_DELAY = registerInteger("players_twilight_portal_creative_delay", GameRuleCategory.PLAYER, 0, 0);

	public static DeferredHolder<GameRule<?>, GameRule<Boolean>> registerBoolean(String id, GameRuleCategory category, boolean defaultValue) {
		return RULES.register(id, () -> new GameRule<>(category, GameRuleType.BOOL, BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean, Codec.BOOL, b -> b ? 1 : 0, defaultValue, FeatureFlagSet.of()));
	}

	public static DeferredHolder<GameRule<?>, GameRule<Integer>> registerInteger(String id, GameRuleCategory category, int defaultValue, int min) {
		return RULES.register(id, () -> new GameRule<>(category, GameRuleType.INT, IntegerArgumentType.integer(min, Integer.MAX_VALUE), GameRuleTypeVisitor::visitInteger, Codec.INT, i -> i, defaultValue, FeatureFlagSet.of()));
	}
}
