package twilightforest.enums;

import net.minecraft.util.StringRepresentable;

public enum BrazierLight implements StringRepresentable {
	OFF("off", 0, 0.0F, -1),
	DIM("dim", 4, 0.25F, 10),
	HALF("half", 6, 0.5F, 4),
	BRIGHT("bright", 9, 0.75F, 2),
	FULL("full", 12, 1.0F, 1);

	private final String name;
	private final int light;
	private final float size;
	private final int smokeRate;

	BrazierLight(String name, int light, float size, int smoke) {
		this.name = name;
		this.light = light;
		this.size = size;
		this.smokeRate = smoke;
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

	public int getSmokeRate() {
		return this.smokeRate;
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}
