package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BoundingBoxUtils {
	@Deprecated // Use `BoundingBox#getCenter` directly
	public static Vec3i getCenter(BoundingBox sbb) {
		return sbb.getCenter();
	}

	// This method has been renamed to be the intersection because it functionally is.
	// If you're looking for the union equivalent, use `BoundingBox#encapsulate`
	@Nullable
	public static BoundingBox getIntersectionOfSBBs(BoundingBox box1, BoundingBox box2) {
		if (!box1.intersects(box2))
			return null;

		return new BoundingBox(
			Math.max(box1.minX(), box2.minX()),
			Math.max(box1.minY(), box2.minY()),
			Math.max(box1.minZ(), box2.minZ()),
			Math.min(box1.maxX(), box2.maxX()),
			Math.min(box1.maxY(), box2.maxY()),
			Math.min(box1.maxZ(), box2.maxZ()));
	}

	public static CompoundTag boundingBoxToNBT(BoundingBox box) {
		return boundingBoxToExistingNBT(box, new CompoundTag());
	}

	public static CompoundTag boundingBoxToExistingNBT(BoundingBox box, CompoundTag tag) {
		tag.putInt("minX", box.minX());
		tag.putInt("minY", box.minY());
		tag.putInt("minZ", box.minZ());
		tag.putInt("maxX", box.maxX());
		tag.putInt("maxY", box.maxY());
		tag.putInt("maxZ", box.maxZ());

		return tag;
	}

	public static BoundingBox NBTToBoundingBox(CompoundTag nbt) {
		return new BoundingBox(
			nbt.getInt("minX"),
			nbt.getInt("minY"),
			nbt.getInt("minZ"),
			nbt.getInt("maxX"),
			nbt.getInt("maxY"),
			nbt.getInt("maxZ")
		);
	}

	public static BoundingBox clone(BoundingBox box) {
		return new BoundingBox(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
	}

	public static BoundingBox cloneWithAdjustments(BoundingBox box, int x1, int y1, int z1, int x2, int y2, int z2) {
		return new BoundingBox(box.minX() + x1, box.minY() + y1, box.minZ() + z1, box.maxX() + x2, box.maxY() + y2, box.maxZ() + z2);
	}

	public static BoundingBox getComponentToAddBoundingBox(int x, int y, int z, int minX, int minY, int minZ, int spanX, int spanY, int spanZ, @Nullable Direction dir, boolean centerBounds) {
		// CenterBounds is true for ONLY Hollow Hills, Hydra Lair, & Yeti Caves
		if (centerBounds) {
			x += (spanX + minX) / 4;
			y += (spanY + minY) / 4;
			z += (spanZ + minZ) / 4;
		}

		return switch (dir) {
			case WEST -> // '\001'
				new BoundingBox(x - spanZ + minZ, y + minY, z + minX, x + minZ, y + spanY + minY, z + spanX + minX);
			case NORTH -> // '\002'
				new BoundingBox(x - spanX - minX, y + minY, z - spanZ - minZ, x - minX, y + spanY + minY, z - minZ);
			case EAST -> // '\003'
				new BoundingBox(x + minZ, y + minY, z - spanX, x + spanZ + minZ, y + spanY + minY, z + minX);
			case null, default -> // '\0'
				new BoundingBox(x + minX, y + minY, z + minZ, x + spanX + minX, y + spanY + minY, z + spanZ + minZ);
		};
	}

	@Nullable
	public static AABB vectorsMinMax(List<Vec3> vec3List, double expand) {
		if (vec3List.isEmpty()) return null;

		Vec3 first = vec3List.get(0);

		return new AABB(
			vec3List.stream().mapToDouble(Vec3::x).reduce(first.x, Math::min) - expand,
			vec3List.stream().mapToDouble(Vec3::y).reduce(first.y, Math::min) - expand,
			vec3List.stream().mapToDouble(Vec3::z).reduce(first.z, Math::min) - expand,
			vec3List.stream().mapToDouble(Vec3::x).reduce(first.x, Math::max) + expand,
			vec3List.stream().mapToDouble(Vec3::y).reduce(first.y, Math::max) + expand,
			vec3List.stream().mapToDouble(Vec3::z).reduce(first.z, Math::max) + expand
		);
	}

	public static int getVolume(BoundingBox box) {
		return box.getXSpan() * box.getYSpan() * box.getZSpan();
	}

	public static boolean isEmpty(BoundingBox box) {
		return getVolume(box) == 0;
	}

	public static BoundingBox extrusionFrom(BoundingBox box, Direction direction, int length) {
		return extrusionFrom(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ(), direction, length);
	}

	public static BoundingBox extrusionFrom(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Direction direction, int length) {
		return switch (direction) {
			case WEST -> new BoundingBox(minX - length, minY, minZ, minX - 1, maxY, maxZ);
			case EAST -> new BoundingBox(maxX + 1, minY, minZ, maxX + length, maxY, maxZ);
			case DOWN -> new BoundingBox(minX, minY - length, minZ, maxX, minY - 1, maxZ);
			case UP -> new BoundingBox(minX, maxY + 1, minZ, maxX, maxY + length, maxZ);
			case NORTH -> new BoundingBox(minX, minY, minZ - length, maxX, maxY, minZ - 1);
			case SOUTH -> new BoundingBox(minX, minY, maxZ + 1, maxX, maxY, maxZ + length);
		};
	}

	public static int getSpan(BoundingBox box, Direction.Axis axis) {
		return switch (axis) {
			case X -> box.getXSpan();
			case Y -> box.getYSpan();
			case Z -> box.getZSpan();
		};
	}

	public static BoundingBox safeRetract(BoundingBox box, Direction direction, int length) {
		int span = getSpan(box, direction.getAxis());

		if (span <= length) return box;

		return switch (direction) {
			case WEST -> cloneWithAdjustments(box, length, 0, 0, 0, 0, 0);
			case EAST -> cloneWithAdjustments(box, 0, 0, 0, -length, 0, 0);
			case DOWN -> cloneWithAdjustments(box, 0, length, 0, 0, 0, 0);
			case UP -> cloneWithAdjustments(box, 0, 0, 0, 0, -length, 0);
			case NORTH -> cloneWithAdjustments(box, 0, 0, length, 0, 0, 0);
			case SOUTH -> cloneWithAdjustments(box, 0, 0, 0, 0, 0, -length);
		};
	}

	public static boolean isPosWithinBox(BlockPos origin, BlockPos.MutableBlockPos offset, int range) {
		return range >= Mth.absMax(offset.getY() - origin.getY(), Mth.absMax(offset.getX() - origin.getX(), offset.getZ() - origin.getZ()));
	}

	public static BlockPos bottomCenterOf(BoundingBox box) {
		return new BlockPos(
			box.minX() + (box.maxX() - box.minX() + 1) / 2, box.minY(), box.minZ() + (box.maxZ() - box.minZ() + 1) / 2
		);
	}

	public static BlockPos clampedInside(BoundingBox box, BlockPos toClamp) {
		return new BlockPos(Mth.clamp(toClamp.getX(), box.minX(), box.maxX()), Mth.clamp(toClamp.getY(), box.minY(), box.maxY()), Mth.clamp(toClamp.getZ(), box.minZ(), box.maxZ()));
	}

	public static int manhattanDistance(BoundingBox box, BlockPos pos) {
		BlockPos clamped = clampedInside(box, pos);
		return Math.abs(clamped.getX() - pos.getX()) + Math.abs(clamped.getY() - pos.getY()) + Math.abs(clamped.getZ() - pos.getZ());
	}

	public static int greatestAxalDistance(BoundingBox box, BlockPos pos) {
		BlockPos clamped = clampedInside(box, pos);
		return Math.max(Math.max(Math.abs(clamped.getX() - pos.getX()), Math.abs(clamped.getY() - pos.getY())), Math.abs(clamped.getZ() - pos.getZ()));
	}

	public static int horizontalManhattanDistance(BoundingBox box, BlockPos pos) {
		int xClamped = Mth.clamp(pos.getX(), box.minX(), box.maxX());
		int zClamped = Mth.clamp(pos.getZ(), box.minZ(), box.maxZ());
		return Math.abs(xClamped - pos.getX()) + Math.abs(zClamped - pos.getZ());
	}

	public static BoundingBox wrappedCoordinates(int padding, BlockPos pos1, BlockPos pos2) {
		int minX = Math.min(pos1.getX(), pos2.getX()) - padding;
		int minY = Math.min(pos1.getY(), pos2.getY()) - padding;
		int minZ = Math.min(pos1.getZ(), pos2.getZ()) - padding;
		int maxX = Math.max(pos1.getX(), pos2.getX()) + padding;
		int maxY = Math.max(pos1.getY(), pos2.getY()) + padding;
		int maxZ = Math.max(pos1.getZ(), pos2.getZ()) + padding;

		return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public static BlockPos lerpPosInside(BoundingBox box, Direction.Axis axis, float delta) {
		BlockPos center = box.getCenter();

		return switch (axis) {
			case X -> new BlockPos(Mth.lerpDiscrete(delta, box.minX(), box.maxX()), center.getY(), center.getZ());
			case Y -> new BlockPos(center.getX(), Mth.lerpDiscrete(delta, box.minY(), box.maxY()), center.getZ());
			case Z -> new BlockPos(center.getX(), center.getY(), Mth.lerpDiscrete(delta, box.minZ(), box.maxZ()));
		};
	}
}
