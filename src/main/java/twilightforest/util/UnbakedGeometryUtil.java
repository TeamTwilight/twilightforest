package twilightforest.util;

import com.mojang.math.Quadrant;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;

public class UnbakedGeometryUtil {
	// [VanillaCopy] returned back this method from 1.21.1 Vanilla Sources
	public static CuboidFace.UVs uvsByFace(Direction face, CuboidModelElement element) {
		return switch (face) {
			case DOWN -> new CuboidFace.UVs(element.from().x(), 16.0F - element.to().z(), element.to().x(), 16.0F - element.from().z());
			case UP -> new CuboidFace.UVs(element.from().x(), element.from().z(), element.to().x(), element.to().z());
			case SOUTH -> new CuboidFace.UVs(element.from().x(), 16.0F - element.to().y(), element.to().x(), 16.0F - element.from().y());
			case WEST -> new CuboidFace.UVs(element.from().z(), 16.0F - element.to().y(), element.to().z(), 16.0F - element.from().y());
			case EAST -> new CuboidFace.UVs(16.0F - element.to().z(), 16.0F - element.to().y(), 16.0F - element.from().z(), 16.0F - element.from().y());
			default -> new CuboidFace.UVs(16.0F - element.to().x(), 16.0F - element.to().y(), 16.0F - element.from().x(), 16.0F - element.from().y());
		};
	}

	// [VanillaCopy] returned back this method from 1.21.1 Vanilla Sources
	public static BakedQuad bakeElementFace(ModelBaker baker, CuboidModelElement element, CuboidFace face, Material.Baked sprite, Direction direction, ModelState state) {
		return FaceBakery.bakeQuad(baker, element.from(), element.to(), face, sprite, direction, state, null, element.shade(), 0);
	}

	public static boolean areUVsEqual(CuboidFace.UVs first, CuboidFace.UVs second) {
		return first.minU() == second.minU() && first.minV() == second.minV() && first.maxU() == second.maxU() && first.maxV() == second.maxV();
	}

	public static int angleFromQuadrant(Quadrant quadrant) {
		return switch (quadrant) {
			case R90 -> 90;
			case R180 -> 180;
			case R270 -> 270;
			default -> 0;
		};
	}

	public static Quadrant quadrantFromAngle(int angle) {
		if (angle >= 0 && angle < 90) {
			return Quadrant.R90;
		} else if (angle >= 90 && angle < 180) {
			return Quadrant.R180;
		} else if (angle >= 180 && angle < 270) {
			return Quadrant.R270;
		} else {
			return Quadrant.R0;
		}
	}

	public static CuboidFace.UVs uvsFromArray(float[] array) {
		return new CuboidFace.UVs(array[0], array[1], array[2], array[3]);
	}
}
