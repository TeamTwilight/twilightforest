package twilightforest.world.components.structures.icetower.floordecorators;

import java.util.List;

public class FloorTop extends FloorWith3x3Map {
	{
		BLOCKED_FLOOR_PARTS = List.of(
			FloorParts.MID,
			FloorParts.PILLARS
		);
		REQUIRED_FLOOR_PARTS = List.of();
	}
}
