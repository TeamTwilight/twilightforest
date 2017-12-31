package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum MagicWoodVariant implements IStringSerializable {
	TIME,
	TRANS,
	MINE,
	SORT;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
