package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum RootVariant implements IStringSerializable {
	ROOT,
	LIVEROOT;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
