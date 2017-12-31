package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum TowerDeviceVariant implements IStringSerializable {
	REAPPEARING_INACTIVE,
	REAPPEARING_ACTIVE,
	VANISH_INACTIVE,
	VANISH_ACTIVE,
	VANISH_LOCKED,
	VANISH_UNLOCKED,
	BUILDER_INACTIVE,
	BUILDER_ACTIVE,
	BUILDER_TIMEOUT,
	ANTIBUILDER,
	GHASTTRAP_INACTIVE,
	GHASTTRAP_ACTIVE,
	REACTOR_INACTIVE,
	REACTOR_ACTIVE;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
