package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum UnderBrickVariant implements IStringSerializable {
	NORMAL,
	MOSSY,
	CRACKED,
	FLOOR;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
