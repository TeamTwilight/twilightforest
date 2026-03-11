package twilightforest.world.components.structures.icetower.floordecorators;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface IFloorDecorator {
	@NotNull
	default List<FloorParts> getBlockedFloorParts() {
		return new ArrayList<FloorParts>();
	}

	@NotNull
	default List<FloorParts> getRequiredFloorParts() {
		return new ArrayList<FloorParts>();
	}
}
