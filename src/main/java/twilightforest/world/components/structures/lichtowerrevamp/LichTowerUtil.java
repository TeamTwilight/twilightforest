package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import twilightforest.util.IntPair;

import java.util.Random;

public final class LichTowerUtil {
    // Segment step refers to the stair level for a given side. Consider it almost like a min-chunk
    static IntPair yOffsetForOpening(Direction side, Random random, int segmentLevel, boolean sidesFaceXAxis) {
        // Each segment is a parallel set of stairs, rotating by 90 degrees. This offsetting is XORed by `sidesFaceXAxis`
        int height = (side.getAxis() == Direction.Axis.X) != sidesFaceXAxis ? 4 : 0;

        // Left refers to the left side of each face, as stairs ascend
        int offsetFromLeft = random.nextInt(CentralTowerSegment.ATTACHMENT_POINT_RANGE);

        // Stairs have a slope of 0.25
        height += (offsetFromLeft - 1) >> 2;
        height += segmentLevel * CentralTowerSegment.HEIGHT;

        offsetFromLeft += CentralTowerSegment.ATTACHMENT_POINT_START;

        return new IntPair(offsetFromLeft, height);
    }

    static BlockPos getRandomOpeningPlacementPos(BlockPos origin, Direction side, Mirror mirror, Random random, int segmentLevel, boolean sidesFaceXAxis) {
        IntPair pair = yOffsetForOpening(side, random, segmentLevel, sidesFaceXAxis);

        return switch (side) {
            case EAST -> origin.offset(-1, pair.z, mirror == Mirror.FRONT_BACK ? CentralTowerSegment.SIDE_LENGTH - pair.x : pair.x);
            case WEST -> origin.offset(CentralTowerSegment.SIDE_LENGTH, pair.z, mirror == Mirror.FRONT_BACK ? pair.x : CentralTowerSegment.SIDE_LENGTH - pair.x);
            case SOUTH -> origin.offset(mirror == Mirror.LEFT_RIGHT ? pair.x : CentralTowerSegment.SIDE_LENGTH - pair.x, pair.z, -1);
            default -> origin.offset(mirror == Mirror.LEFT_RIGHT ? CentralTowerSegment.SIDE_LENGTH - pair.x : pair.x, pair.z, CentralTowerSegment.SIDE_LENGTH);
        };
    }
}
