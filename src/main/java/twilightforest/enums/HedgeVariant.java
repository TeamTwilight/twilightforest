package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum HedgeVariant implements IStringSerializable {
	HEDGE,
	DARKWOOD_LEAVES;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
