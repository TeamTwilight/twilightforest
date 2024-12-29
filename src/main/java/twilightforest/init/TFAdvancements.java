package twilightforest.init;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.*;

public class TFAdvancements {

	public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> MADE_TF_PORTAL = TRIGGERS.register("make_tf_portal", SimpleAdvancementTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> CONSUME_HYDRA_CHOP = TRIGGERS.register("consume_hydra_chop_on_low_hunger", SimpleAdvancementTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> QUEST_RAM_COMPLETED = TRIGGERS.register("complete_quest_ram", SimpleAdvancementTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> PLACED_TROPHY_ON_PEDESTAL = TRIGGERS.register("placed_on_trophy_pedestal", SimpleAdvancementTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> ACTIVATED_GHAST_TRAP = TRIGGERS.register("activate_ghast_trap", SimpleAdvancementTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, StructureClearedTrigger> STRUCTURE_CLEARED = TRIGGERS.register("structure_cleared", StructureClearedTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, DrinkFromFlaskTrigger> DRINK_FROM_FLASK = TRIGGERS.register("drink_from_flask", DrinkFromFlaskTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, KillBugTrigger> KILL_BUG = TRIGGERS.register("kill_bug", KillBugTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, HurtBossTrigger> HURT_BOSS = TRIGGERS.register("hurt_boss", HurtBossTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> KILL_ALL_PHANTOMS = TRIGGERS.register("kill_all_phantoms", SimpleAdvancementTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, UncraftItemTrigger> UNCRAFT_ITEM = TRIGGERS.register("uncraft_item", UncraftItemTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> BROKE_GLASS_SWORD = TRIGGERS.register("broke_glass_sword", SimpleAdvancementTrigger::new);
}
