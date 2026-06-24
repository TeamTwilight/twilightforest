package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.item.recipe.UncraftingTableCondition;
import twilightforest.loot.LootingEnchantNumberProvider;
import twilightforest.loot.MultiplayerBasedAdditionLootFunction;
import twilightforest.loot.MultiplayerBasedNumberProvider;
import twilightforest.loot.conditions.GiantPickUsedCondition;
import twilightforest.loot.conditions.IsMinionCondition;
import twilightforest.loot.conditions.ModExistsCondition;
import twilightforest.loot.conditions.UncraftingTableEnabledCondition;

public class TFLoot {

	public static final DeferredRegister<MapCodec<? extends LootItemCondition>> CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, TwilightForestMod.ID);
	public static final DeferredRegister<MapCodec<? extends LootItemFunction>> FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, TwilightForestMod.ID);
	public static final DeferredRegister<MapCodec<? extends NumberProvider>> NUMBERS = DeferredRegister.create(Registries.LOOT_NUMBER_PROVIDER_TYPE, TwilightForestMod.ID);
	public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONALS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, TwilightForestMod.ID);

	public static final DeferredHolder<MapCodec<? extends LootItemCondition>, MapCodec<IsMinionCondition>> IS_MINION = CONDITIONS.register("is_minion", () -> IsMinionCondition.CODEC);
	public static final DeferredHolder<MapCodec<? extends LootItemCondition>, MapCodec<ModExistsCondition>> MOD_EXISTS = CONDITIONS.register("mod_exists", () -> ModExistsCondition.CODEC);
	public static final DeferredHolder<MapCodec<? extends LootItemCondition>, MapCodec<UncraftingTableEnabledCondition>> UNCRAFTING_TABLE_ENABLED = CONDITIONS.register("uncrafting_table_enabled", () -> UncraftingTableEnabledCondition.CODEC);
	public static final DeferredHolder<MapCodec<? extends LootItemCondition>, MapCodec<GiantPickUsedCondition>> GIANT_PICK_USED_CONDITION = CONDITIONS.register("giant_pick_used", () -> GiantPickUsedCondition.CODEC);

	public static final DeferredHolder<MapCodec<? extends LootItemFunction>, MapCodec<MultiplayerBasedAdditionLootFunction>> MULTIPLAYER_MULTIPLIER = FUNCTIONS.register("multiplayer_addition", () -> MultiplayerBasedAdditionLootFunction.CODEC);

	public static final DeferredHolder<MapCodec<? extends NumberProvider>, MapCodec<MultiplayerBasedNumberProvider>> MULTIPLAYER_ROLLS = NUMBERS.register("multiplayer_rolls", () -> MultiplayerBasedNumberProvider.CODEC);
	public static final DeferredHolder<MapCodec<? extends NumberProvider>, MapCodec<LootingEnchantNumberProvider>> LOOTING_ROLLS = NUMBERS.register("looting_rolls", () -> LootingEnchantNumberProvider.CODEC);

	public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<UncraftingTableCondition>> UNCRAFTING_TABLE_CONDITION = CONDITIONALS.register("uncrafting_table_enabled", () -> UncraftingTableCondition.CODEC);

}
