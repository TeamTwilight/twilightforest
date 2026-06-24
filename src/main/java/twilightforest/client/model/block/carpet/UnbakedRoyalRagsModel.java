package twilightforest.client.model.block.carpet;

import com.mojang.blaze3d.platform.Transparency;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import org.joml.Vector3f;
import twilightforest.client.model.block.connected.ConnectionLogic;

import java.awt.*;

//for now, im keeping this hardcoded to a 2 layer block, with the overlay layer being fullbright and tinted.
//It might be worth expanding this in the future to be more flexible for other kinds of blocks (1 layer blocks, determining emissivity and tinting per layer, maybe >2 layer blocks?) but for now, I see no point.
//I only wanted this system for castle doors after all!
public class UnbakedRoyalRagsModel implements UnbakedGeometry, UnbakedModel {
	private final Vector3f[][][] baseElements; // [horizontal_dir][quad][0 = from, 1 = to]
	private final Vector3f[][][][] faceElements; // [up_down_dir][quad][connection_logic][0 = from, 1 = to]

	public UnbakedRoyalRagsModel() {
		// Properly size the arrays to handle the extra [2] dimension at the end for from/to
		this.baseElements = new Vector3f[4][4][2];
		this.faceElements = new Vector3f[2][4][5][2];
		Vec3i center = new Vec3i(8, 8, 8);

		for (Direction face : Direction.values()) {
			Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];

			for (int quad = 0; quad < 4; quad++) {
				Vec3i vFace = face.getUnitVec3i();
				Vec3i vP1 = planeDirections[quad].getUnitVec3i();
				Vec3i vP2 = planeDirections[(quad + 1) % 4].getUnitVec3i();

				int cornerX = (vFace.getX() + vP1.getX() + vP2.getX() + 1) * 8;
				int cornerY = (vFace.getY() + vP1.getY() + vP2.getY() + 1) * 8;
				int cornerZ = (vFace.getZ() + vP1.getZ() + vP2.getZ() + 1) * 8;

				Vector3f from = new Vector3f(
					(float) Math.min(center.getX(), cornerX),
					(float) Math.min(center.getY(), cornerY) / 16f,
					(float) Math.min(center.getZ(), cornerZ)
				);

				Vector3f to = new Vector3f(
					(float) Math.max(center.getX(), cornerX),
					(float) Math.max(center.getY(), cornerY) / 16f,
					(float) Math.max(center.getZ(), cornerZ)
				);

				if (face.getAxis().isHorizontal()) {
					this.baseElements[face.get2DDataValue()][quad][0] = from;
					this.baseElements[face.get2DDataValue()][quad][1] = to;
				} else {
					for (ConnectionLogic connectionType : ConnectionLogic.values()) {
						this.faceElements[face.get3DDataValue()][quad][connectionType.ordinal()][0] = from;
						this.faceElements[face.get3DDataValue()][quad][connectionType.ordinal()][1] = to;
					}
				}
			}
		}
	}

	@Override
	public QuadCollection bake(TextureSlots textureSlots, ModelBaker modelBaker, ModelState modelState, ModelDebugName modelDebugName) {
		QuadCollection.Builder builder = new QuadCollection.Builder();

		TextureAtlasSprite baseTexture = modelBaker.materials().resolveSlot(textureSlots, "wool", modelDebugName).sprite();
		TextureAtlasSprite ctmTexture = modelBaker.materials().resolveSlot(textureSlots, "wool_ctm", modelDebugName).sprite();
		TextureAtlasSprite[] textures = new TextureAtlasSprite[]{baseTexture, ctmTexture};

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			int horSideIndex = direction.get2DDataValue();

			for (int quad = 0; quad < 4; quad++) {
				Vector3f from = this.baseElements[horSideIndex][quad][0];
				Vector3f to = this.baseElements[horSideIndex][quad][1];

				if (from == null || to == null) continue;

				Material.Baked material = modelBaker.materials().get(textureSlots.getMaterial("wool"), modelDebugName);
				BakedQuad bakedQuad = createModernQuad(from, to, direction, material, baseTexture, -1, baseTexture.transparency());
				builder.addUnculledFace(bakedQuad);
			}
		}

		for (int direction = 0; direction < 2; direction++) {
			Direction actualDirection = direction == 0 ? Direction.DOWN: Direction.UP;

			for (int quad = 0; quad < 4; quad++) {
				for (int connectionState = 0; connectionState < 5; connectionState++) {
					Vector3f from = this.faceElements[direction][quad][connectionState][0];
					Vector3f to = this.faceElements[direction][quad][connectionState][1];

					if (from == null || to == null) continue;

					TextureAtlasSprite chosenTexture = ConnectionLogic.values()[connectionState].chooseTexture(textures);

					Material.Baked material = modelBaker.materials().get(textureSlots.getMaterial(chosenTexture == baseTexture ? "wool" : "wool_ctm"), modelDebugName);
					BakedQuad bakedQuad = createModernQuad(from, to, actualDirection, material, chosenTexture, 0, baseTexture.transparency());
					builder.addCulledFace(actualDirection, bakedQuad);
				}
			}
		}

		return builder.build();
	}

	private BakedQuad createModernQuad(
		Vector3f from, Vector3f to,
		Direction direction,
		Material.Baked material,
		TextureAtlasSprite activeSprite,
		int tintIndex,
		Transparency transparency) {

		float x0 = from.x(), y0 = from.y(), z0 = from.z();
		float x1 = to.x(),   y1 = to.y(),   z1 = to.z();

		Vector3f p0 = new Vector3f();
		Vector3f p1 = new Vector3f();
		Vector3f p2 = new Vector3f();
		Vector3f p3 = new Vector3f();

		switch (direction) {
			case DOWN -> {
				p0.set(x0, y0, z0); p1.set(x1, y0, z0); p2.set(x1, y0, z1); p3.set(x0, y0, z1);
			}
			case UP -> {
				p0.set(x0, y1, z1); p1.set(x1, y1, z1); p2.set(x1, y1, z0); p3.set(x0, y1, z0);
			}
			case NORTH -> {
				p0.set(x1, y1, z0); p1.set(x1, y0, z0); p2.set(x0, y0, z0); p3.set(x0, y1, z0);
			}
			case SOUTH -> {
				p0.set(x0, y1, z1); p1.set(x0, y0, z1); p2.set(x1, y0, z1); p3.set(x1, y1, z1);
			}
			case WEST -> {
				p0.set(x0, y1, z0); p1.set(x0, y0, z0); p2.set(x0, y0, z1); p3.set(x0, y1, z1);
			}
			case EAST -> {
				p0.set(x1, y1, z1); p1.set(x1, y0, z1); p2.set(x1, y0, z0); p3.set(x1, y1, z0);
			}
		}

		float u0 = activeSprite.getU0(), u1 = activeSprite.getU1();
		float v0 = activeSprite.getV0(), v1 = activeSprite.getV1();

		long uv0 = ((long) Float.floatToRawIntBits(u0) << 32) | (Float.floatToRawIntBits(v0) & 0xFFFFFFFFL);
		long uv1 = ((long) Float.floatToRawIntBits(u1) << 32) | (Float.floatToRawIntBits(v0) & 0xFFFFFFFFL);
		long uv2 = ((long) Float.floatToRawIntBits(u1) << 32) | (Float.floatToRawIntBits(v1) & 0xFFFFFFFFL);
		long uv3 = ((long) Float.floatToRawIntBits(u0) << 32) | (Float.floatToRawIntBits(v1) & 0xFFFFFFFFL);

		BakedQuad.MaterialInfo materialInfo = BakedQuad.MaterialInfo.of(
			material,
			transparency,
			tintIndex,
			true,
			0,
			true
		);

		return new BakedQuad(p0, p1, p2, p3, uv0, uv1, uv2, uv3, direction, materialInfo);
	}
}