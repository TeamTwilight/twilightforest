package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum StructureVariant implements IStringSerializable {
    TWILIGHT_PORTAL,
    HEDGE_MAZE,
    HOLLOW_HILL,
    QUEST_GROVE,
    MUSHROOM_TOWER,
    NAGA_COURTYARD,
    LICH_TOWER,
    MINOTAUR_LABYRINTH,
    HYDRA_LAIR,
    GOBLIN_STRONGHOLD,
    DARK_TOWER,
    YETI_CAVE,
    AURORA_PALACE,
    TROLL_CAVE_AND_CLOUD_CASTLE,
    FINAL_CASTLE;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
