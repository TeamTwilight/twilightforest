package twilightforest.world.components.structures.icetower.floordecorators;

import java.util.List;

public class FloorPillarParkour extends FloorWith3x3Map {
	{
		REQUIRED_FLOOR_PARTS = List.of(
			FloorParts.LEFT,
			FloorParts.MID,
			FloorParts.RIGHT,
			FloorParts.LEFT_FRONT,
			FloorParts.FRONT,
			FloorParts.RIGHT_FRONT,
			FloorParts.PILLARS
		);
		BLOCKED_FLOOR_PARTS = List.of(
			FloorParts.MID,
			FloorParts.RIGHT,
			FloorParts.PILLARS
		);
	}
}
