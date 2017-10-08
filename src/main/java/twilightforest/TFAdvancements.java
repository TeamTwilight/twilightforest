package twilightforest;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import twilightforest.advancements.*;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TFAdvancements {
    public static final HasAdvancementTrigger ADVANCEMENT_UNLOCKED = CriteriaTriggers.register(new HasAdvancementTrigger());
    public static final MakePortalTrigger MADE_TF_PORTAL = CriteriaTriggers.register(new MakePortalTrigger());
    public static final HydraChopTrigger CONSUME_HYDRA_CHOP = CriteriaTriggers.register(new HydraChopTrigger());
    public static final QuestRamCompletionTrigger QUEST_RAM_COMPLETED = CriteriaTriggers.register(new QuestRamCompletionTrigger());
    public static final TrophyPedestalTrigger PLACED_TROPHY_ON_PEDESTAL = CriteriaTriggers.register(new TrophyPedestalTrigger());
    public static final GroupKillTrigger GANG_BANG = CriteriaTriggers.register(new GroupKillTrigger());

    public static void init() {
    }
}