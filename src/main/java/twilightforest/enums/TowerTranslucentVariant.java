package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum TowerTranslucentVariant implements IStringSerializable {
	REAPPEARING_INACTIVE,
	REAPPEARING_ACTIVE,
	BUILT_INACTIVE,
	BUILT_ACTIVE,
	REVERTER_REPLACEMENT,
	REACTOR_DEBRIS,
	FAKE_GOLD,
	FAKE_DIAMOND;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
