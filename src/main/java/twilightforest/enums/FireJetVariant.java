package twilightforest.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum FireJetVariant implements StringRepresentable {
	IDLE,
	POPPING,
	FLAME,
	TIMEOUT;

	@Override
	public String toString() {
		return getSerializedName();
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public boolean isVariantOn() {
		return this == POPPING || this == FLAME;
	}
}
