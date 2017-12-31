package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum PlantVariant implements IStringSerializable {
	MOSSPATCH(false),
	MAYAPPLE(false),
	CLOVERPATCH(false),
	FIDDLEHEAD(true),
	MUSHGLOOM(false),
	FORESTGRASS(true),
	DEADBUSH(false),
	TORCHBERRY(false),
	ROOT_STRAND(false);

	public final boolean isGrassColored;

	PlantVariant(boolean isGrassColored) {
		this.isGrassColored = isGrassColored;
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
