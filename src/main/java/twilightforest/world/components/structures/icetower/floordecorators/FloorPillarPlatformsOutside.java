package twilightforest.world.components.structures.icetower.floordecorators;


import java.util.List;

public class FloorPillarPlatformsOutside extends FloorWith3x3Map {
	{
		REQUIRED_FLOOR_PARTS = List.of(
			FloorParts.RIGHT_BACK,
			FloorParts.RIGHT,
			FloorParts.RIGHT_FRONT
		);
		BLOCKED_FLOOR_PARTS = List.of(
			FloorParts.LEFT_BACK,
			FloorParts.RIGHT_BACK,
			FloorParts.LEFT,
			FloorParts.LEFT_FRONT,
			FloorParts.PILLARS
		);
	}
}
