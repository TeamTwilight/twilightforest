package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum ThornVariant implements IStringSerializable {
	BROWN,
	GREEN;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
