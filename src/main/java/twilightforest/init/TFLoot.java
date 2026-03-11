package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
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

	public static final DeferredRegister<LootItemConditionType> CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, TwilightForestMod.ID);
	public static final DeferredRegister<LootItemFunctionType<?>> FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, TwilightForestMod.ID);
	public static final DeferredRegister<LootNumberProviderType> NUMBERS = DeferredRegister.create(Registries.LOOT_NUMBER_PROVIDER_TYPE, TwilightForestMod.ID);
	public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONALS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, TwilightForestMod.ID);

	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> IS_MINION = CONDITIONS.register("is_minion", () -> new LootItemConditionType(IsMinionCondition.CODEC));
	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> MOD_EXISTS = CONDITIONS.register("mod_exists", () -> new LootItemConditionType(ModExistsCondition.CODEC));
	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> UNCRAFTING_TABLE_ENABLED = CONDITIONS.register("uncrafting_table_enabled", () -> new LootItemConditionType(UncraftingTableEnabledCondition.CODEC));
	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> GIANT_PICK_USED_CONDITION = CONDITIONS.register("giant_pick_used", () -> new LootItemConditionType(GiantPickUsedCondition.CODEC));
	public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<MultiplayerBasedAdditionLootFunction>> MULTIPLAYER_MULTIPLIER = FUNCTIONS.register("multiplayer_addition", () -> new LootItemFunctionType<>(MultiplayerBasedAdditionLootFunction.CODEC));
	public static final DeferredHolder<LootNumberProviderType, LootNumberProviderType> MULTIPLAYER_ROLLS = NUMBERS.register("multiplayer_rolls", () -> new LootNumberProviderType(MultiplayerBasedNumberProvider.CODEC));
	public static final DeferredHolder<LootNumberProviderType, LootNumberProviderType> LOOTING_ROLLS = NUMBERS.register("looting_rolls", () -> new LootNumberProviderType(LootingEnchantNumberProvider.CODEC));

	public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<UncraftingTableCondition>> UNCRAFTING_TABLE_CONDITION = CONDITIONALS.register("uncrafting_table_enabled", () -> UncraftingTableCondition.CODEC);

}
