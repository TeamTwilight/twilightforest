package twilightforest.world.components.structures.icetower.floordecorators;

public enum FloorParts {
	LEFT_FRONT,
	FRONT,
	RIGHT_FRONT,
	LEFT,
	MID,
	RIGHT,
	LEFT_BACK,
	BACK,
	RIGHT_BACK,
	PILLARS;

	/*
	These anchors are for Aurora Palace generation. They are 3x3 except of pillars, which are four 1x1 zones.
	Location of these anchors, view from above, default TF rotation:

		LF LF LF FR FR FR RF RF RF
		LF LF LF FR FR FR RF RF RF
		LF LF PI FR FR FR PI RF RF
		LE LE LE MI MI MI RI RI RI
		LE LE LE MI MI MI RI RI RI
		LE LE LE MI MI MI RI RI RI
		LB LB PI BA BA BA PI RB RB
		LB LB LB BA BA BA LB RB RB
		LB LB LB BA BA BA LB RB RB
	 */

	public FloorParts rotateClockwise() {
		return switch (this) {
			case LEFT_FRONT -> FloorParts.RIGHT_FRONT;
			case FRONT -> FloorParts.RIGHT;
			case RIGHT_FRONT -> FloorParts.RIGHT_BACK;
			case RIGHT -> FloorParts.BACK;
			case RIGHT_BACK -> FloorParts.LEFT_BACK;
			case BACK -> FloorParts.LEFT;
			case LEFT_BACK -> FloorParts.LEFT_FRONT;
			case LEFT -> FloorParts.FRONT;
			default -> this;
		};
	}

	public FloorParts rotateClockwise(int times) {
		FloorParts partNew = this;
		for (int i = 0; i < times % 4; i++) {
			partNew = partNew.rotateClockwise();
		}

		return partNew;
	}
}
