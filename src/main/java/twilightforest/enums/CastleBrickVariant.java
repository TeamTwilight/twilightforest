package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum CastleBrickVariant implements IStringSerializable {
	NORMAL,
	WORN,
	CRACKED,
	ROOF,
	MOSSY,
	FRAME;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
