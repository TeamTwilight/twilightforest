package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum MazestoneVariant implements IStringSerializable {
	PLAIN,
	BRICK,
	CHISELED,
	DECORATIVE,
	CRACKED,
	MOSSY,
	MOSAIC,
	BORDER;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
