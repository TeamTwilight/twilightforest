package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum LeavesVariant implements IStringSerializable {
	OAK,
	CANOPY,
	MANGROVE,
	RAINBOAK;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
