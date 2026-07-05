package twilightforest.client.model.block.connected;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.quad.MutableQuad;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

public class UnbakedConnectedTextureModel extends AbstractUnbakedModel {

	protected final boolean renderOverlayOnAllFaces;
	protected final Set<Direction> connectedFaces;
	protected final List<Block> connectableBlocks;

	protected MutableQuad[][] baseElements;
	protected MutableQuad[][][] connectedElements;


	public UnbakedConnectedTextureModel(Pair<Vector3f, Vector3f> element, Set<Direction> connectedFaces, boolean renderOnDisabledFaces, List<Block> connectableBlocks, int baseEmissivity, int emissivity, StandardModelParameters parameters) {
		super(parameters);
		this.connectedFaces = connectedFaces;
		this.renderOverlayOnAllFaces = renderOnDisabledFaces;
		this.connectableBlocks = connectableBlocks;

		this.baseElements = new MutableQuad[6][4];
		this.connectedElements = new MutableQuad[6][4][5];

		int center = 8;

		for (Direction face : Direction.values()) {
			Direction cull = this.getCullface(face, element.getFirst(), element.getSecond());
			Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];

			for (int i = 0; i < 4; ++i) {
				net.minecraft.core.Vec3i corner = face.getUnitVec3i().offset(planeDirections[i].getUnitVec3i()).offset(planeDirections[(i + 1) % 4].getUnitVec3i()).offset(1, 1, 1).multiply(8);

				Vector3f from = new Vector3f(
					Mth.clamp(Math.min(center - (16 - element.getSecond().x()), corner.getX() + element.getFirst().x()), 0, 16),
					Mth.clamp(Math.min(center - (16 - element.getSecond().y()), corner.getY() + element.getFirst().y()), 0, 16),
					Mth.clamp(Math.min(center - (16 - element.getSecond().z()), corner.getZ() + element.getFirst().z()), 0, 16)
				);

				Vector3f to = new Vector3f(
					element.getSecond().x() < center ? element.getSecond().x() : Math.max(center, corner.getX() - (16 - element.getSecond().x())),
					element.getSecond().y() < center ? element.getSecond().y() : Math.max(center, corner.getY() - (16 - element.getSecond().y())),
					element.getSecond().z() < center ? element.getSecond().z() : Math.max(center, corner.getZ() - (16 - element.getSecond().z()))
				);

				MutableQuad baseQuad = new MutableQuad();
				baseQuad.setDirection(face);
				baseQuad.setShade(true);
				baseQuad.setLightEmission(baseEmissivity);
				setupQuadVertices(baseQuad, from, to, face);
				remapQuadUVs(baseQuad, ConnectionLogic.NONE, face);

				this.baseElements[face.get3DDataValue()][i] = baseQuad;

				for (ConnectionLogic logic : ConnectionLogic.values()) {
					MutableQuad connectedQuad = new MutableQuad();
					connectedQuad.setDirection(face);
					connectedQuad.setShade(true);
					connectedQuad.setLightEmission(emissivity);
					setupQuadVertices(connectedQuad, from, to, face);
					remapQuadUVs(connectedQuad, logic, face);

					this.connectedElements[face.get3DDataValue()][i][logic.ordinal()] = connectedQuad;
				}
			}
		}
	}

	private void setupQuadVertices(MutableQuad quad, org.joml.Vector3f from, org.joml.Vector3f to, Direction face) {
		for (int v = 0; v < 4; v++) {
			float x = (v == 1 || v == 2) ? to.x() / 16f : from.x() / 16f;
			float y = (v == 2 || v == 3) ? to.y() / 16f : from.y() / 16f;
			float z = (face.getAxis() == Direction.Axis.Z) ? to.z() / 16f : from.z() / 16f;
			quad.setPosition(v, new org.joml.Vector3f(x, y, z));
		}
	}

	private void remapQuadUVs(MutableQuad quad, ConnectionLogic logic, Direction face) {
		for (int v = 0; v < 4; v++) {
			float u = (v == 1 || v == 2) ? 1.0f : 0.0f;
			float vCoord = (v == 2 || v == 3) ? 1.0f : 0.0f;

			float[] uvs = new float[]{u, vCoord};
			float[] remapped = logic.remapUVs(uvs);

			quad.setUv(v, remapped[0], remapped[1]);
		}
	}


	@Nullable
	private Direction getCullface(Direction direction, Vector3f from, Vector3f to) {
		boolean cull = switch (direction) {
			case DOWN -> from.y() == 0.0F;
			case UP -> to.y() == 16.0F;
			case NORTH -> from.x() == 0.0F;
			case SOUTH -> to.x() == 16.0F;
			case WEST -> from.z() == 0.0F;
			case EAST -> to.z() == 16.0F;
		};

		return cull ? direction : null;
	}

	@Override
	public UnbakedGeometry geometry() {
		TextureAtlas atlas = net.minecraft.client.Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(TextureAtlas.LOCATION_BLOCKS);

		String modId = "twilightforest";

		Identifier idBase = Identifier.fromNamespaceAndPath(modId, "block/glass");
		Identifier idOverlay = Identifier.fromNamespaceAndPath(modId, "block/glass_overlay");
		Identifier idConnected = Identifier.fromNamespaceAndPath(modId, "block/glass_overlay_connected");

		Material matBase = new Material(idBase);
		Material matOverlay = new Material(idOverlay);
		Material matConnected = new Material(idConnected);

		matBase = matBase.withForceTranslucent(true);
		matOverlay = matOverlay.withForceTranslucent(true);
		matConnected = matConnected.withForceTranslucent(true);

		TextureAtlasSprite baseTexture = atlas.getSprite(matBase.sprite());
		TextureAtlasSprite overlayTexture = atlas.getSprite(matOverlay.sprite());
		TextureAtlasSprite connectedTexture = atlas.getSprite(matConnected.sprite());
		TextureAtlasSprite particleTexture = overlayTexture;

		TextureAtlasSprite[] sprites = new TextureAtlasSprite[]{overlayTexture, connectedTexture, particleTexture};

		Map<Direction, BakedQuad[]> finalBaseQuads = new HashMap<>();
		Map<Direction, BakedQuad[][]> finalConnectedQuads = new HashMap<>();
		Set<Direction> unculledFaces = new HashSet<>();

		for (Direction dir : Direction.values()) {
			int dirIdx = dir.get3DDataValue();

			List<BakedQuad> baseQuadList = new ArrayList<>();
			for (int i = 0; i < 4; i++) {
				MutableQuad quad = this.baseElements[dirIdx][i];
				for (int v = 0; v < 4; v++) {
					quad.setUv(v, baseTexture.getU(quad.u(v) * 16f), baseTexture.getV(quad.v(v) * 16f));
				}
				baseQuadList.add(quad.toBakedQuad());
			}
			finalBaseQuads.put(dir, baseQuadList.toArray(new BakedQuad[0]));

			BakedQuad[][] dirQuads = new BakedQuad[4][5];
			for (int quadIdx = 0; quadIdx < 4; quadIdx++) {
				for (int typeIdx = 0; typeIdx < 5; typeIdx++) {
					MutableQuad quad = this.connectedElements[dirIdx][quadIdx][typeIdx];

					TextureAtlasSprite chosenSprite = ConnectionLogic.values()[typeIdx].chooseTexture(sprites);

					for (int v = 0; v < 4; v++) {
						quad.setUv(v, chosenSprite.getU(quad.u(v) * 16f), chosenSprite.getV(quad.v(v) * 16f));
					}
					dirQuads[quadIdx][typeIdx] = quad.toBakedQuad();
				}
			}
			finalConnectedQuads.put(dir, dirQuads);
		}

		return new ConnectedTextureModel(
			this.connectedFaces,
			unculledFaces,
			this.renderOverlayOnAllFaces,
			this.connectableBlocks,
			finalBaseQuads,
			finalConnectedQuads
		);
	}
}