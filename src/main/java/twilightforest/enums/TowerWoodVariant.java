package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum TowerWoodVariant implements IStringSerializable {
	PLAIN,
	ENCASED,
	CRACKED,
	MOSSY,
	INFESTED;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
