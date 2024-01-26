package twilightforest.loot.conditions;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import twilightforest.TFConfig;
import twilightforest.init.TFLoot;

public class UncraftingTableEnabledCondition implements LootItemCondition {

	private static final UncraftingTableEnabledCondition INSTANCE = new UncraftingTableEnabledCondition();
	public static final Codec<UncraftingTableEnabledCondition> CODEC = Codec.unit(INSTANCE);


	@Override
	public LootItemConditionType getType() {
		return TFLoot.UNCRAFTING_TABLE_ENABLED.get();
	}

	@Override
	public boolean test(LootContext context) {
		return !TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableEntireTable.get();
	}

	public static LootItemCondition.Builder uncraftingTableEnabled() {
		return UncraftingTableEnabledCondition::new;
	}
}
