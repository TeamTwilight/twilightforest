package twilightforest.world.components.structures.icetower.floordecorators;

import java.util.List;

public class FloorFarWallSteps extends FloorWith3x3Map {
	{
	REQUIRED_FLOOR_PARTS = List.of(
		FloorParts.LEFT,
		FloorParts.LEFT_BACK
	);
	BLOCKED_FLOOR_PARTS = List.of(
		FloorParts.LEFT_BACK,
		FloorParts.LEFT,
		FloorParts.LEFT_FRONT
	);
	}
}
