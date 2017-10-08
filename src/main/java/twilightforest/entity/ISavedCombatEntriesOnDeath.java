package twilightforest.entity;

import net.minecraft.util.CombatEntry;

import java.util.List;

public interface ISavedCombatEntriesOnDeath {
    List<CombatEntry> getCombatList();
}
