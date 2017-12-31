package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum HugeLilypadPiece implements IStringSerializable {
	NW,
	NE,
	SE,
	SW;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
