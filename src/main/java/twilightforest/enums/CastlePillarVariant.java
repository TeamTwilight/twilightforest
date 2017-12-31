package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum CastlePillarVariant implements IStringSerializable {
    ENCASED,
    BOLD;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
