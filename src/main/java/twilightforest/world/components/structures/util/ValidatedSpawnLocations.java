package twilightforest.world.components.structures.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public interface ValidatedSpawnLocations {
	boolean canSpawnMob(BlockPos pos, BoundingBox structureBounds);
}