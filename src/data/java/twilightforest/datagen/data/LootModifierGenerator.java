package twilightforest.datagen.data;

import net.minecraft.advancements.criterion.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
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

import java.util.concurrent.CompletableFuture;

public class LootModifierGenerator extends GlobalLootModifierProvider {
	public LootModifierGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, TwilightForestMod.ID);
	}

	@Override
	protected void start() {
		add("fiery_pick_smelting", new FieryToolSmeltingModifier(new LootItemCondition[]{MatchTool.toolMatches(ItemPredicate.Builder.item().of(this.registries.lookupOrThrow(Registries.ITEM), TFItems.FIERY_PICKAXE.get())).build()}, 0));
		add("giant_pick_grouping", new GiantToolGroupingModifier(new LootItemCondition[]{GiantPickUsedCondition.builder(LootContext.EntityTarget.THIS).build()}, 0));
	}
}
