package twilightforest.data;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;
import twilightforest.loot.conditions.GiantPickUsedCondition;
import twilightforest.loot.modifiers.FieryToolSmeltingModifier;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;

public class LootModifierGenerator extends GlobalLootModifierProvider {
	public LootModifierGenerator(PackOutput output) {
		super(output, TwilightForestMod.ID);
	}

	@Override
	protected void start() {
		add("fiery_pick_smelting", new FieryToolSmeltingModifier(new LootItemCondition[]{MatchTool.toolMatches(ItemPredicate.Builder.item().of(TFItems.FIERY_PICKAXE.get())).build()}));
		add("giant_pick_grouping", new GiantToolGroupingModifier(new LootItemCondition[]{GiantPickUsedCondition.builder(LootContext.EntityTarget.THIS).build()}));
	}
}
