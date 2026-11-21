package twilightforest.world.components.structures.icetower.floordecorators;

import java.util.ArrayList;
import java.util.List;

public interface IFloorDecorator {
	default List<FloorParts> getBlockedFloorParts() {
		return new ArrayList<FloorParts>();
	}

	default List<FloorParts> getRequiredFloorParts() {
		return new ArrayList<FloorParts>();
	}
}
