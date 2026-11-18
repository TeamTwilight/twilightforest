package twilightforest.world.components.structures.icetower.floordecorators;

import java.util.List;

public abstract class FloorWith3x3Map implements IFloorDecorator {

	protected List<FloorParts> REQUIRED_FLOOR_PARTS;  // Required from the floor above the current one, because we generate top to bottom
	protected List<FloorParts> BLOCKED_FLOOR_PARTS;  // Blocked for the floor below

	@Override
	public List<FloorParts> getRequiredFloorParts() {
		return List.copyOf(REQUIRED_FLOOR_PARTS);
	}

	@Override
	public List<FloorParts> getBlockedFloorParts() {
		return List.copyOf(BLOCKED_FLOOR_PARTS);
	}
}

