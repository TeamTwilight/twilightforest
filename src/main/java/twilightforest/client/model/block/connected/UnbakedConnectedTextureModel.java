package twilightforest.client.model.block.connected;

import com.mojang.datafixers.util.Pair;
import com.mojang.math.Quadrant;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.NeoForgeModelProperties;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedElementsHelper;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

public class UnbakedConnectedTextureModel extends AbstractUnbakedModel implements CustomUnbakedBlockStateModel {

	private static final ModelDebugName DEBUG_NAME = () -> "twilightforest:connected_texture";

	protected final boolean renderOverlayOnAllFaces;
	protected final Set<Direction> connectedFaces;
	protected final List<Block> connectableBlocks;
	protected final boolean translucent;
	protected CuboidFace[][] baseFaces;
	protected CuboidFace[][][] connectedFacesArray;
	protected Vector3f[][] baseFrom;
	protected Vector3f[][] baseTo;
	protected Vector3f[][][] connectedFrom;
	protected Vector3f[][][] connectedTo;

	public UnbakedConnectedTextureModel(com.mojang.datafixers.util.Pair<Vector3f, Vector3f> element, Set<Direction> connectedFaces, boolean renderOnDisabledFaces, List<Block> connectableBlocks, int baseTintIndex, int baseEmissivity, int tintIndex, int emissivity, boolean translucent, StandardModelParameters parameters) {
		super(parameters);
		//a list of block faces that should have connected textures.
		this.connectedFaces = connectedFaces;
		//whether the overlay texture should render on all faces or not. Defaults to true
		this.renderOverlayOnAllFaces = renderOnDisabledFaces;
		//a list of blocks this block can connect its texture to
		this.connectableBlocks = connectableBlocks;
		this.translucent = translucent;
		//base elements - the base block. No Connected Textures on this bit.
		//the array is made of the directions and "sections". Each section is a corner quadrant of the block
		this.baseFaces = new CuboidFace[6][4];
		this.baseFrom = new Vector3f[6][4];
		this.baseTo = new Vector3f[6][4];
		//face elements - the connected bit of the model.
		//the array is made of the directions, "sections", and each logic value in the ConnectionLogic class
		this.connectedFacesArray = new CuboidFace[6][4][5];
		this.connectedFrom = new Vector3f[6][4][5];
		this.connectedTo = new Vector3f[6][4][5];

		int center = 8;

		for (Direction face : Direction.values()) {
			Direction cull = this.getCullface(face, element.getFirst(), element.getSecond());
			Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];

			for (int i = 0; i < 4; ++i) {
				Vec3i corner = face.getUnitVec3i().offset(planeDirections[i].getUnitVec3i()).offset(planeDirections[(i + 1) % 4].getUnitVec3i()).offset(1, 1, 1).multiply(8);
				Vector3f modifiedFrom = new Vector3f(
					Math.clamp(Math.min(center - (16 - element.getSecond().x()), corner.getX() + element.getFirst().x()), 0, 16),
					Math.clamp(Math.min(center - (16 - element.getSecond().y()), corner.getY() + element.getFirst().y()), 0, 16),
					Math.clamp(Math.min(center - (16 - element.getSecond().z()), corner.getZ() + element.getFirst().z()), 0, 16));
				Vector3f modifiedTo = new Vector3f(
					element.getSecond().x() < center ? element.getSecond().x() : Math.max(center, corner.getX() - (16 - element.getSecond().x())),
					element.getSecond().y() < center ? element.getSecond().y() : Math.max(center, corner.getY() - (16 - element.getSecond().y())),
					element.getSecond().z() < center ? element.getSecond().z() : Math.max(center, corner.getZ() - (16 - element.getSecond().z())));

				CuboidFace.UVs defaultUVs = computeDefaultUVs(modifiedFrom, modifiedTo, face);
				float[] remapped = ConnectionLogic.NONE.remapUVs(new float[]{defaultUVs.minU(), defaultUVs.minV(), defaultUVs.maxU(), defaultUVs.maxV()});
				this.baseFrom[face.get3DDataValue()][i] = modifiedFrom;
				this.baseTo[face.get3DDataValue()][i] = modifiedTo;
				this.baseFaces[face.get3DDataValue()][i] = new CuboidFace(cull, baseTintIndex, "", new CuboidFace.UVs(remapped[0], remapped[1], remapped[2], remapped[3]), Quadrant.R0);

				for (ConnectionLogic logic : ConnectionLogic.values()) {
					float[] remappedConnected = logic.remapUVs(new float[]{defaultUVs.minU(), defaultUVs.minV(), defaultUVs.maxU(), defaultUVs.maxV()});
					this.connectedFrom[face.get3DDataValue()][i][logic.ordinal()] = modifiedFrom;
					this.connectedTo[face.get3DDataValue()][i][logic.ordinal()] = modifiedTo;
					this.connectedFacesArray[face.get3DDataValue()][i][logic.ordinal()] = new CuboidFace(cull, tintIndex, "", new CuboidFace.UVs(remappedConnected[0], remappedConnected[1], remappedConnected[2], remappedConnected[3]), Quadrant.R0);
				}
			}
		}
	}

    private static CuboidFace.UVs computeDefaultUVs(Vector3f from, Vector3f to, Direction facing) {
		return switch (facing) {
			case DOWN -> new CuboidFace.UVs(from.x(), 16.0F - to.z(), to.x(), 16.0F - from.z());
			case UP -> new CuboidFace.UVs(from.x(), from.z(), to.x(), to.z());
			case NORTH -> new CuboidFace.UVs(16.0F - to.x(), 16.0F - to.y(), 16.0F - from.x(), 16.0F - from.y());
			case SOUTH -> new CuboidFace.UVs(from.x(), 16.0F - to.y(), to.x(), 16.0F - from.y());
			case WEST -> new CuboidFace.UVs(from.z(), 16.0F - to.y(), to.z(), 16.0F - from.y());
			case EAST -> new CuboidFace.UVs(16.0F - to.z(), 16.0F - to.y(), 16.0F - from.z(), 16.0F - from.y());
		};
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
	public BlockStateModel bake(ModelBaker baker) {
		ResolvedModel resolved = baker.resolveInlineModel(this, DEBUG_NAME);
		TextureSlots textureSlots = resolved.getTopTextureSlots();
		ContextMap additionalProperties = resolved.getTopAdditionalProperties();
		ModelState state = BlockModelRotation.IDENTITY;
		boolean useAmbientOcclusion = true;
		boolean usesBlockLight = true;
		ItemTransforms itemTransforms = this.parameters.itemTransforms();
		return bakeInternal(textureSlots, baker, state, useAmbientOcclusion, usesBlockLight, itemTransforms, additionalProperties);
	}

	public BlockStateModel bakeInternal(TextureSlots textureSlots, ModelBaker baker, ModelState state, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties) {
		Transformation rootTransform = additionalProperties.getOrDefault(NeoForgeModelProperties.TRANSFORM, Transformation.IDENTITY);
		if (!rootTransform.isIdentity())
			state = UnbakedElementsHelper.composeRootTransformIntoModelState(state, rootTransform);

		Map<Direction, BakedQuad[]> baseQuads = new HashMap<>();
		Set<Direction> unculledFaces = new HashSet<>();

		if (textureSlots.getMaterial("base_texture") != null) {
			Material.Baked baseMaterial = baker.materials().get(textureSlots.getMaterial("base_texture"), DEBUG_NAME);

			for (Direction dir : Direction.values()) {
				List<BakedQuad> quadList = new ArrayList<>();

				for (int i = 0; i < 4; i++) {
					Vector3f from = this.baseFrom[dir.get3DDataValue()][i];
					Vector3f to = this.baseTo[dir.get3DDataValue()][i];
					CuboidFace face = this.baseFaces[dir.get3DDataValue()][i];

					quadList.add(FaceBakery.bakeQuad(
						baker, from, to, face, baseMaterial, dir, state, null, true, 0
					));
				}
				baseQuads.put(dir, quadList.toArray(new BakedQuad[0]));
			}
		}

		//we'll use this to figure out which texture to use with the Connected Texture logic
		//NONE uses the first one, everything else uses the 2nd one
		Material.Baked overlayMaterial = baker.materials().get(textureSlots.getMaterial("overlay_texture"), DEBUG_NAME);
		Material.Baked connectedMaterial = baker.materials().get(textureSlots.getMaterial("overlay_connected"), DEBUG_NAME);
		Material.Baked particleMaterial;
		if (textureSlots.getMaterial("particle") != null) {
			particleMaterial = baker.materials().get(textureSlots.getMaterial("particle"), DEBUG_NAME);
		} else {
			particleMaterial = overlayMaterial;
		}
		Material.Baked[] sprites = new Material.Baked[]{overlayMaterial, connectedMaterial};

		Map<Direction, BakedQuad[][]> connectedQuads = new HashMap<>();

		for (Direction dir : Direction.values()) {
			BakedQuad[][] dirQuads = new BakedQuad[4][5];
			for (int quad = 0; quad < 4; quad++) {
				for (int type = 0; type < 5; type++) {
					Vector3f from = this.connectedFrom[dir.get3DDataValue()][quad][type];
					Vector3f to = this.connectedTo[dir.get3DDataValue()][quad][type];
					CuboidFace face = this.connectedFacesArray[dir.get3DDataValue()][quad][type];
					if (face.cullForDirection() == null) unculledFaces.add(dir);
					Material.Baked material = type == 0 ? overlayMaterial : connectedMaterial;

					dirQuads[quad][type] = FaceBakery.bakeQuad(
						baker, from, to, face, material, dir, state, null, true, 0
					);
				}
			}
			connectedQuads.put(dir, dirQuads);
		}

		return new ConnectedTextureModel(this.connectedFaces, unculledFaces, this.renderOverlayOnAllFaces, this.connectableBlocks, baseQuads, connectedQuads, particleMaterial, useAmbientOcclusion, this.translucent);
	}

	@Override
	@Nullable
	public UnbakedGeometry geometry() {
		return (textureSlots, baker, state, name) -> {
			QuadCollection.Builder builder = new QuadCollection.Builder();

			// Bake base quads if base texture exists
			if (textureSlots.getMaterial("base_texture") != null) {
				Material baseMaterial = textureSlots.getMaterial("base_texture");
				if (this.translucent) baseMaterial = baseMaterial.withForceTranslucent(true);
				Material.Baked baseBaked = baker.materials().get(baseMaterial, name);

				for (Direction dir : Direction.values()) {
					for (int i = 0; i < 4; i++) {
						Vector3f from = this.baseFrom[dir.get3DDataValue()][i];
						Vector3f to = this.baseTo[dir.get3DDataValue()][i];
						CuboidFace face = this.baseFaces[dir.get3DDataValue()][i];

						BakedQuad quad = FaceBakery.bakeQuad(
							baker, from, to, face, baseBaked, dir, state, null, true, 0
						);

						Direction cullDir = face.cullForDirection();
						if (cullDir != null) {
							builder.addCulledFace(cullDir, quad);
						} else {
							builder.addUnculledFace(quad);
						}
					}
				}
			}

			// Bake overlay quads using NONE type for static fallback
			Material overlayMaterial = textureSlots.getMaterial("overlay_texture");
			Material connectedMaterial = textureSlots.getMaterial("overlay_connected");
			if (overlayMaterial != null && connectedMaterial != null) {
				if (this.translucent) {
					overlayMaterial = overlayMaterial.withForceTranslucent(true);
					connectedMaterial = connectedMaterial.withForceTranslucent(true);
				}
				Material.Baked overlayBaked = baker.materials().get(overlayMaterial, name);
				Material.Baked connectedBaked = baker.materials().get(connectedMaterial, name);

				for (Direction dir : Direction.values()) {
					for (int quad = 0; quad < 4; quad++) {
						for (int type = 0; type < 5; type++) {
							Vector3f from = this.connectedFrom[dir.get3DDataValue()][quad][type];
							Vector3f to = this.connectedTo[dir.get3DDataValue()][quad][type];
							CuboidFace face = this.connectedFacesArray[dir.get3DDataValue()][quad][type];
							Material.Baked material = type == 0 ? overlayBaked : connectedBaked;

							BakedQuad bakedQuad = FaceBakery.bakeQuad(
								baker, from, to, face, material, dir, state, null, true, 0
							);

							Direction cullDir = face.cullForDirection();
							if (cullDir != null) {
								builder.addCulledFace(cullDir, bakedQuad);
							} else {
								builder.addUnculledFace(bakedQuad);
							}
						}
					}
				}
			}

			return builder.build();
		};
	}

	@Override
	public com.mojang.serialization.MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
		throw new UnsupportedOperationException("UnbakedConnectedTextureModel does not support codec serialization");
	}
}
