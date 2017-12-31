package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum Leaves3Variant implements IStringSerializable {
	THORN,
	BEANSTALK;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
