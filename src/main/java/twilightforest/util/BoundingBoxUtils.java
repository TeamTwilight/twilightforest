package twilightforest.util;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.NotNull;
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
	@SuppressWarnings("unused")
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

    @NotNull
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
            default -> // '\0'
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
}
