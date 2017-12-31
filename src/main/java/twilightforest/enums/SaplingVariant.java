package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum SaplingVariant implements IStringSerializable {
	OAK,
	CANOPY,
	MANGROVE,
	DARKWOOD,
	HOLLOW_OAK,
	TIME,
	TRANSFORMATION,
	MINING,
	SORTING,
	RAINBOW;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
