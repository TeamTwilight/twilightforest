package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum CompressedVariant implements IStringSerializable {
    IRONWOOD,
    FIERY,
    STEELLEAF,
    ARCTIC_FUR,
    CARMINITE;

    @Override
    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
