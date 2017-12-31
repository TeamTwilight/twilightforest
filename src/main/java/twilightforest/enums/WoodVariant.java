package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum WoodVariant implements IStringSerializable {
	OAK,
	CANOPY,
	MANGROVE,
	DARK;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
