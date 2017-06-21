package twilightforest.block.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum FireJetVariant implements IStringSerializable {
	SMOKER,
	ENCASED_SMOKER_OFF,
	ENCASED_SMOKER_ON,
	JET_IDLE,
	JET_POPPING,
	JET_FLAME,
	ENCASED_JET_IDLE,
	ENCASED_JET_POPPING,
	ENCASED_JET_FLAME;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
