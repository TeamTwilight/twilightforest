package twilightforest.util;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;

public final class RotationUtil {
	public static final Rotation[] ROTATIONS = Rotation.values();
	public static final Direction[] CARDINALS = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

	private RotationUtil() {
	}

	public static Rotation getRandomRotation(RandomSource random) {
		return ROTATIONS[random.nextInt(ROTATIONS.length)];
	}

	public static Rotation add(Rotation original, int rotations) {
		return original.getRotated(ROTATIONS[(rotations + 4) & 3]);
	}

	public static Rotation subtract(Rotation original, Rotation rotation) {
		return switch (rotation) {
			case CLOCKWISE_180 -> switch (original) {
				case NONE -> Rotation.CLOCKWISE_180;
				case CLOCKWISE_90 -> Rotation.COUNTERCLOCKWISE_90;
				case CLOCKWISE_180 -> Rotation.NONE;
				case COUNTERCLOCKWISE_90 -> Rotation.CLOCKWISE_90;
				//default -> original;
			};
			case COUNTERCLOCKWISE_90 -> switch (original) {
				case NONE -> Rotation.CLOCKWISE_90;
				case CLOCKWISE_90 -> Rotation.CLOCKWISE_180;
				case CLOCKWISE_180 -> Rotation.COUNTERCLOCKWISE_90;
				case COUNTERCLOCKWISE_90 -> Rotation.NONE;
				//default -> original;
			};
			case CLOCKWISE_90 -> switch (original) {
				case NONE -> Rotation.COUNTERCLOCKWISE_90;
				case CLOCKWISE_90 -> Rotation.NONE;
				case CLOCKWISE_180 -> Rotation.CLOCKWISE_90;
				case COUNTERCLOCKWISE_90 -> Rotation.CLOCKWISE_180;
				//default -> original;
			};
			default -> original;
		};
	}

	public static Rotation getRelativeRotation(Direction original, Direction destination) {
		return switch (original) {
			case SOUTH -> switch (destination) {
				case NORTH -> Rotation.CLOCKWISE_180;
				case WEST -> Rotation.CLOCKWISE_90;
				case EAST -> Rotation.COUNTERCLOCKWISE_90;
				default -> Rotation.NONE;
			};
			case EAST -> switch (destination) {
				case WEST -> Rotation.CLOCKWISE_180;
				case SOUTH -> Rotation.CLOCKWISE_90;
				case NORTH -> Rotation.COUNTERCLOCKWISE_90;
				default -> Rotation.NONE;
			};
			case WEST -> switch (destination) {
				case EAST -> Rotation.CLOCKWISE_180;
				case NORTH -> Rotation.CLOCKWISE_90;
				case SOUTH -> Rotation.COUNTERCLOCKWISE_90;
				default -> Rotation.NONE;
			};
			default -> switch (destination) {
				case SOUTH -> Rotation.CLOCKWISE_180;
				case EAST -> Rotation.CLOCKWISE_90;
				case WEST -> Rotation.COUNTERCLOCKWISE_90;
				default -> Rotation.NONE;
			};
		};
	}

	public static Direction getRandomFacing(RandomSource random) {
		return CARDINALS[random.nextInt(CARDINALS.length)];
	}
}
