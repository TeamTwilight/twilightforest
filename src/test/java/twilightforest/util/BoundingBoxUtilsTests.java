package twilightforest.util;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoundingBoxUtilsTests {

	@Test
	public void getIntersectionOfSBBs() {
		BoundingBox result = BoundingBoxUtils.getIntersectionOfSBBs(
			new BoundingBox(1, 2, 3, 4, 5, 6),
			new BoundingBox(3, 4, 5, 6, 7, 8)
		);

		assertNotNull(result);
		assertEquals(3, result.minX());
		assertEquals(4, result.minY());
		assertEquals(5, result.minZ());
		assertEquals(4, result.maxX());
		assertEquals(5, result.maxY());
		assertEquals(6, result.maxZ());
	}

	@Test
	public void getIntersectionOfSBBsNoIntersection() {
		BoundingBox result = BoundingBoxUtils.getIntersectionOfSBBs(
			new BoundingBox(1, 2, 3, 4, 5, 6),
			new BoundingBox(5, 6, 7, 8, 9, 10)
		);

		assertNull(result);
	}

	@Test
	public void boundingBoxToExistingNBT() {
		CompoundTag result = BoundingBoxUtils.boundingBoxToExistingNBT(new BoundingBox(1, 2, 3, 4, 5, 6), new CompoundTag());

		assertNotNull(result);
		assertEquals(1, result.getInt("minX"));
		assertEquals(2, result.getInt("minY"));
		assertEquals(3, result.getInt("minZ"));
		assertEquals(4, result.getInt("maxX"));
		assertEquals(5, result.getInt("maxY"));
		assertEquals(6, result.getInt("maxZ"));
	}

	@Test
	public void NBTToBoundingBox() {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("minX", 1);
		nbt.putInt("minY", 2);
		nbt.putInt("minZ", 3);
		nbt.putInt("maxX", 4);
		nbt.putInt("maxY", 5);
		nbt.putInt("maxZ", 6);

		BoundingBox result = BoundingBoxUtils.NBTToBoundingBox(nbt);

		assertNotNull(result);
		assertEquals(1, result.minX());
		assertEquals(2, result.minY());
		assertEquals(3, result.minZ());
		assertEquals(4, result.maxX());
		assertEquals(5, result.maxY());
		assertEquals(6, result.maxZ());
	}

	@Test
	public void cloneTest() {
		BoundingBox check = new BoundingBox(1, 2, 3, 4, 5, 6);

		BoundingBox result = BoundingBoxUtils.clone(check);

		assertNotSame(check, result);
		assertEquals(check, result);
	}

	@Test
	public void cloneWithAdjustments() {

		BoundingBox result = BoundingBoxUtils.cloneWithAdjustments(
			new BoundingBox(1, 2, 3, 4, 5, 6),
			1, 2, 3, 4, 5, 6
		);

		assertNotNull(result);
		assertEquals(2, result.minX());
		assertEquals(4, result.minY());
		assertEquals(6, result.minZ());
		assertEquals(8, result.maxX());
		assertEquals(10, result.maxY());
		assertEquals(12, result.maxZ());
	}

	@Test
	public void getComponentToAddBoundingBoxDefaultDirections() {
		getComponentToAddBoundingBoxDefaultDirections(null);
		getComponentToAddBoundingBoxDefaultDirections(Direction.UP);
		getComponentToAddBoundingBoxDefaultDirections(Direction.DOWN);
		getComponentToAddBoundingBoxDefaultDirections(Direction.SOUTH);
	}

	private void getComponentToAddBoundingBoxDefaultDirections(@Nullable Direction direction) {
		BoundingBox result = BoundingBoxUtils.getComponentToAddBoundingBox(
			1, 2, 3,
			4, 5, 6,
			7, 8, 9,
			direction,
			false
		);

		assertNotNull(result);
		assertEquals(1 + 4, result.minX());
		assertEquals(2 + 5, result.minY());
		assertEquals(3 + 6, result.minZ());
		assertEquals(1 + 4 + 7, result.maxX());
		assertEquals(2 + 5 + 8, result.maxY());
		assertEquals(3 + 6 + 9, result.maxZ());
	}

	@Test
	public void getComponentToAddBoundingBoxNorth() {
		BoundingBox result = BoundingBoxUtils.getComponentToAddBoundingBox(
			1, 2, 3,
			4, 5, 6,
			7, 8, 9,
			Direction.NORTH,
			false
		);

		assertNotNull(result);
		assertEquals(1 - 7 - 4, result.minX());
		assertEquals(2 + 5, result.minY());
		assertEquals(3 - 9 - 6, result.minZ());
		assertEquals(1 - 4, result.maxX());
		assertEquals(2 + 5 + 8, result.maxY());
		assertEquals(3 - 6, result.maxZ());
	}

	@Test
	public void getComponentToAddBoundingBoxEast() {
		BoundingBox result = BoundingBoxUtils.getComponentToAddBoundingBox(
			1, 2, 3,
			4, 5, 6,
			7, 8, 9,
			Direction.EAST,
			false
		);

		assertNotNull(result);
		assertEquals(1 + 6, result.minX());
		assertEquals(2 + 5, result.minY());
		assertEquals(3 - 7, result.minZ());
		assertEquals(1 + 9 + 6, result.maxX());
		assertEquals(2 + 5 + 8, result.maxY());
		assertEquals(3 + 4, result.maxZ());
	}

	@Test
	public void getComponentToAddBoundingBoxWest() {
		BoundingBox result = BoundingBoxUtils.getComponentToAddBoundingBox(
			1, 2, 3,
			4, 5, 6,
			7, 8, 9,
			Direction.WEST,
			false
		);

		assertNotNull(result);
		assertEquals(1 - 9 + 6, result.minX());
		assertEquals(2 + 5, result.minY());
		assertEquals(3 + 4, result.minZ());
		assertEquals(1 + 6, result.maxX());
		assertEquals(2 + 5 + 8, result.maxY());
		assertEquals(3 + 7 + 4, result.maxZ());
	}

	@Test
	public void getComponentToAddBoundingBoxCentered() {
		BoundingBox result = BoundingBoxUtils.getComponentToAddBoundingBox(
			1, 2, 3,
			4, 5, 6,
			7, 8, 9,
			null,
			true
		);

		assertNotNull(result);
		assertEquals(1 + ((7 + 4) / 4) + 4, result.minX());
		assertEquals(2 + ((8 + 5) / 4) + 5, result.minY());
		assertEquals(3 + ((9 + 6) / 4) + 6, result.minZ());
		assertEquals(1 + ((7 + 4) / 4) + 4 + 7, result.maxX());
		assertEquals(2 + ((8 + 5) / 4) + 5 + 8, result.maxY());
		assertEquals(3 + ((9 + 6) / 4) + 6 + 9, result.maxZ());
	}

	@Test
	public void vectorsMinMax() {
		AABB result = BoundingBoxUtils.vectorsMinMax(List.of(
			new Vec3(1, 6, 7),
			new Vec3(2, 5, 9),
			new Vec3(3, 4, 8)
		), 2);

		assertNotNull(result);
		assertEquals(1 - 2, result.minX);
		assertEquals(4 - 2, result.minY);
		assertEquals(7 - 2, result.minZ);
		assertEquals(3 + 2, result.maxX);
		assertEquals(6 + 2, result.maxY);
		assertEquals(9 + 2, result.maxZ);
	}

	@Test
	public void vectorsMinMaxNull() {
		AABB result = BoundingBoxUtils.vectorsMinMax(List.of(), 2);

		assertNull(result);
	}

}
