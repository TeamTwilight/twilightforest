package twilightforest.enums;

import net.minecraft.util.StringRepresentable;

public enum BrazierLight implements StringRepresentable {
	OFF("off", 0, 0.0F),
	DIM("dim", 3, 0.25F),
	HALF("half", 6, 0.5F),
	BRIGHT("bright", 9, 0.75F),
	FULL("full", 12, 1.0F);

	private final String name;
	private final int light;
	private final float size;

	BrazierLight(String name, int light, float size) {
		this.name = name;
		this.light = light;
		this.size = size;
	}

	public int getLight() {
		return this.light;
	}

	public float getFireSize() {
		return this.size;
	}

	public boolean isLit() {
		return this.light > 0;
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}
