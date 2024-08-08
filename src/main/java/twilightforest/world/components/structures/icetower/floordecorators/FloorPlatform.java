package twilightforest.world.components.structures.icetower.floordecorators;

import java.util.List;

public class FloorPlatform extends FloorWith3x3Map {
	{
		REQUIRED_FLOOR_PARTS = List.of(
			FloorParts.LEFT_BACK,
			FloorParts.LEFT
		);
		BLOCKED_FLOOR_PARTS = List.of(
			FloorParts.RIGHT_BACK,
			FloorParts.BACK,
			FloorParts.PILLARS
		);
	}
}
