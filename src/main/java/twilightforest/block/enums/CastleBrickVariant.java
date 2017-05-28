package twilightforest.block.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum CastleBrickVariant implements IStringSerializable {
    NORMAL,
    WORN,
    CRACKED,
    ROOF,
    MOSSY;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
