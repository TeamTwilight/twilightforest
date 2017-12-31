package twilightforest.enums;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public enum NagastoneVariant implements IStringSerializable {
	NORTH_HEAD,
	SOUTH_HEAD,
	WEST_HEAD,
	EAST_HEAD,
	NORTH_DOWN,
	SOUTH_DOWN,
	WEST_DOWN,
	EAST_DOWN,
	NORTH_UP,
	SOUTH_UP,
	EAST_UP,
	WEST_UP,
	AXIS_X,
	AXIS_Y,
	AXIS_Z,
	SOLID; // This can act as null

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static boolean isHead(NagastoneVariant variant) {
		return variant.ordinal() < 4;
	}

	public static NagastoneVariant rotate(NagastoneVariant variant, Rotation rotation) {
		if (!isHead(variant)) return variant;

		switch (rotation) {
			case NONE:
				return variant;
			case CLOCKWISE_90:
				switch (variant) {
					case NORTH_HEAD:
						return EAST_HEAD;
					case SOUTH_HEAD:
						return WEST_HEAD;
					case WEST_HEAD:
						return NORTH_HEAD;
					case EAST_HEAD:
						return SOUTH_HEAD;
				}
			case CLOCKWISE_180:
				switch (variant) {
					case NORTH_HEAD:
						return SOUTH_HEAD;
					case SOUTH_HEAD:
						return NORTH_HEAD;
					case WEST_HEAD:
						return EAST_HEAD;
					case EAST_HEAD:
						return WEST_HEAD;
				}
			case COUNTERCLOCKWISE_90:
				switch (variant) {
					case NORTH_HEAD:
						return WEST_HEAD;
					case SOUTH_HEAD:
						return EAST_HEAD;
					case WEST_HEAD:
						return SOUTH_HEAD;
					case EAST_HEAD:
						return NORTH_HEAD;
				}
		}

		return variant;
	}

	public static NagastoneVariant mirror(NagastoneVariant variant, Mirror mirror) {
		if (!isHead(variant)) return variant;

		switch (mirror) {
			case LEFT_RIGHT:
				switch (variant) {
					case NORTH_HEAD:
						return NORTH_HEAD;
					case SOUTH_HEAD:
						return SOUTH_HEAD;
					case WEST_HEAD:
						return EAST_HEAD;
					case EAST_HEAD:
						return WEST_HEAD;
					default:
						return variant;
				}
			case FRONT_BACK:
				switch (variant) {
					case NORTH_HEAD:
						return SOUTH_HEAD;
					case SOUTH_HEAD:
						return NORTH_HEAD;
					case WEST_HEAD:
						return WEST_HEAD;
					case EAST_HEAD:
						return EAST_HEAD;
					default:
						return variant;
				}
			default:
				return variant;
		}
	}

	public static NagastoneVariant getHeadFromFacing(EnumFacing facing) {
		switch (facing) {
			case NORTH:
				return NORTH_HEAD;
			case SOUTH:
				return SOUTH_HEAD;
			case WEST:
				return WEST_HEAD;
			case EAST:
				return EAST_HEAD;
			default:
				return SOLID;
		}
	}

	public static NagastoneVariant getVariantFromAxis(EnumFacing.Axis axis) {
		switch (axis) {
			case X:
				return AXIS_X;
			case Y:
				return AXIS_Y;
			case Z:
				return AXIS_Z;
			default:
				return SOLID;
		}
	}

	public static NagastoneVariant getVariantFromDoubleFacing(EnumFacing facing1, EnumFacing facing2) {
		if (facing1.getAxis() == facing2.getAxis()) // Pairs of 6 dirs and axes
			return getVariantFromAxis(facing1.getAxis()); // Both faces are on same axis
		else if (facing1.getAxis() != EnumFacing.Axis.Y && facing2.getAxis() != EnumFacing.Axis.Y)
			return SOLID; // Elbow connection doesn't have Y

		EnumFacing facingYAxis = facing1.getAxis() == EnumFacing.Axis.Y ? facing1 : facing2;
		EnumFacing otherFace = facing1.getAxis() != EnumFacing.Axis.Y ? facing1 : facing2;

		if (facingYAxis == EnumFacing.UP) {
			switch (otherFace) {
				case NORTH:
					return NORTH_UP;
				case SOUTH:
					return SOUTH_UP;
				case WEST:
					return WEST_UP;
				case EAST:
					return EAST_UP;
				default:
					return SOLID;
			}
		} else {
			switch (otherFace) {
				case NORTH:
					return NORTH_DOWN;
				case SOUTH:
					return SOUTH_DOWN;
				case WEST:
					return WEST_DOWN;
				case EAST:
					return EAST_DOWN;
				default:
					return SOLID;
			}
		}
	}
}
