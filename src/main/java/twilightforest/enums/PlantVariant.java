package twilightforest.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum PlantVariant implements IStringSerializable {
	MOSSPATCH(),
	MAYAPPLE(),
	CLOVERPATCH(),
	FIDDLEHEAD(true),
	MUSHGLOOM(),
	FORESTGRASS(true),
	DEADBUSH(),
	TORCHBERRY(),
	ROOT_STRAND(),
	FALLEN_LEAVES(true, true);

	public final boolean isColored;
	public final boolean isLeaves;

	PlantVariant() {
		this(false);
	}

	PlantVariant(boolean isColored) {
		this(isColored, false);
	}

	PlantVariant(boolean isColored, boolean isLeaves) {
		this.isColored = isColored;
		this.isLeaves = isLeaves;
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
