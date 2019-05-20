package twilightforest.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class TFAdvancements {
    public static final HasAdvancementTrigger ADVANCEMENT_UNLOCKED = CriteriaTriggers.register(new HasAdvancementTrigger());
    public static final MakePortalTrigger MADE_TF_PORTAL = CriteriaTriggers.register(new MakePortalTrigger());
    public static final HydraChopTrigger CONSUME_HYDRA_CHOP = CriteriaTriggers.register(new HydraChopTrigger());
    public static final QuestRamCompletionTrigger QUEST_RAM_COMPLETED = CriteriaTriggers.register(new QuestRamCompletionTrigger());
    public static final TrophyPedestalTrigger PLACED_TROPHY_ON_PEDESTAL = CriteriaTriggers.register(new TrophyPedestalTrigger());
    public static final ActivateGhastTrapTrigger ACTIVATED_GHAST_TRAP = CriteriaTriggers.register(new ActivateGhastTrapTrigger());
    public static final StructureClearedTrigger STRUCTURE_CLEARED = CriteriaTriggers.register(new StructureClearedTrigger());
    public static final ItemUseTrigger ITEM_USE_TRIGGER = CriteriaTriggers.register(new ItemUseTrigger());
    public static final ArmorInventoryChangedTrigger ARMOR_CHANGED = CriteriaTriggers.register(new ArmorInventoryChangedTrigger());

    public static void init() {}
}
