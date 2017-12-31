package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum FireJetVariant implements IStringSerializable {
	SMOKER(true),
	ENCASED_SMOKER_OFF(false),
	ENCASED_SMOKER_ON(false),
	JET_IDLE(true),
	JET_POPPING(true),
	JET_FLAME(true),
	ENCASED_JET_IDLE(false),
	ENCASED_JET_POPPING(false),
	ENCASED_JET_FLAME(false);

	FireJetVariant(boolean hasGrassColor) {
		this.hasGrassColor = hasGrassColor;
	}

	public final boolean hasGrassColor;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
