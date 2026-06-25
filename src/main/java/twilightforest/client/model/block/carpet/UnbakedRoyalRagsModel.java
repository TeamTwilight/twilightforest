package twilightforest.client.model.block.carpet;

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
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.NeoForgeModelProperties;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedElementsHelper;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.client.model.block.connected.ConnectionLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//for now, im keeping this hardcoded to a 2 layer block, with the overlay layer being fullbright and tinted.
//It might be worth expanding this in the future to be more flexible for other kinds of blocks (1 layer blocks, determining emissivity and tinting per layer, maybe >2 layer blocks?) but for now, I see no point.
//I only wanted this system for castle doors after all!
public class UnbakedRoyalRagsModel extends AbstractUnbakedModel implements CustomUnbakedBlockStateModel {

	private static final ModelDebugName DEBUG_NAME = () -> "twilightforest:royal_rags";

	private final CuboidFace[][] baseFaces;
	private final CuboidFace[][][] faceFaces;
	private final Vector3f[][] baseFrom;
	private final Vector3f[][] baseTo;
	private final Vector3f[][][] faceFrom;
	private final Vector3f[][][] faceTo;

	public UnbakedRoyalRagsModel(StandardModelParameters parameters) {
		super(parameters);
		//base elements - the side faces without ctm. No Connected Textures on this bit.
		//the array is made of horizontal directions (Direction.get2DDataValue) and quads
		this.baseFaces = new CuboidFace[4][4];
		this.baseFrom = new Vector3f[4][4];
		this.baseTo = new Vector3f[4][4];

		//face elements - the connected bit of the model.
		//the array is made of the directions, quads, and each logic value in the ConnectionLogic class
		//Topmost array indexes up/down directions (Direction.get3DDataValue, down = 0, up = 1) then inside are quads
		this.faceFaces = new CuboidFace[2][4][5];
		this.faceFrom = new Vector3f[2][4][5];
		this.faceTo = new Vector3f[2][4][5];
		Vec3i center = new Vec3i(8, 8, 8);

		for (Direction face : Direction.values()) {
			Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];

			for (int quad = 0; quad < 4; quad++) {
				Vec3i corner = face.getUnitVec3i().offset(planeDirections[quad].getUnitVec3i()).offset(planeDirections[(quad + 1) % 4].getUnitVec3i()).offset(1, 1, 1).multiply(8);

				Vector3f from = new Vector3f((float) Math.min(center.getX(), corner.getX()), (float) Math.min(center.getY(), corner.getY()) / 16f, (float) Math.min(center.getZ(), corner.getZ()));
				Vector3f to = new Vector3f((float) Math.max(center.getX(), corner.getX()), (float) Math.max(center.getY(), corner.getY()) / 16f, (float) Math.max(center.getZ(), corner.getZ()));

				// Compute default UVs using the same logic as FaceBakery.defaultFaceUV
				CuboidFace.UVs defaultUVs = computeDefaultUVs(from, to, face);

				if (face.getAxis().isHorizontal()) {
					this.baseFrom[face.get2DDataValue()][quad] = from;
					this.baseTo[face.get2DDataValue()][quad] = to;
					float[] remapped = ConnectionLogic.NONE.remapUVs(new float[]{defaultUVs.minU(), defaultUVs.minV(), defaultUVs.maxU(), defaultUVs.maxV()});
					this.baseFaces[face.get2DDataValue()][quad] = new CuboidFace(face, -1, "", new CuboidFace.UVs(remapped[0], remapped[1], remapped[2], remapped[3]), Quadrant.R0);
				} else {
					for (ConnectionLogic connectionType : ConnectionLogic.values()) {
						int dirIdx = face.get3DDataValue();
						this.faceFrom[dirIdx][quad][connectionType.ordinal()] = from;
						this.faceTo[dirIdx][quad][connectionType.ordinal()] = to;
						float[] remapped = connectionType.remapUVs(new float[]{defaultUVs.minU(), defaultUVs.minV(), defaultUVs.maxU(), defaultUVs.maxV()});
						this.faceFaces[dirIdx][quad][connectionType.ordinal()] = new CuboidFace(face, 0, "", new CuboidFace.UVs(remapped[0], remapped[1], remapped[2], remapped[3]), Quadrant.R0);
					}
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
		if (!rootTransform.isIdentity()) {
			state = UnbakedElementsHelper.composeRootTransformIntoModelState(state, rootTransform);
		}

		//making an array list like this is cursed, would not recommend
		@SuppressWarnings({"unchecked", "rawtypes"})
		List<BakedQuad>[] baseQuads = (List<BakedQuad>[]) new List[4];
		Material.Baked baseMaterial = baker.materials().get(Objects.requireNonNull(textureSlots.getMaterial("wool")), DEBUG_NAME);

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			baseQuads[direction.get2DDataValue()] = new ArrayList<>();

			for (int quad = 0; quad < 4; quad++) {
				Vector3f from = this.baseFrom[direction.get2DDataValue()][quad];
				Vector3f to = this.baseTo[direction.get2DDataValue()][quad];
				CuboidFace face = this.baseFaces[direction.get2DDataValue()][quad];

				baseQuads[direction.get2DDataValue()].add(FaceBakery.bakeQuad(
					baker, from, to, face, baseMaterial, direction, state, null, true, 0
				));
			}
		}

		//we'll use this to figure out which texture to use with the Connected Texture logic
		//NONE uses the first one, everything else uses the 2nd one
		Material.Baked woolMaterial = baker.materials().get(Objects.requireNonNull(textureSlots.getMaterial("wool")), DEBUG_NAME);
		Material.Baked ctmMaterial = baker.materials().get(Objects.requireNonNull(textureSlots.getMaterial("wool_ctm")), DEBUG_NAME);
		Material.Baked[] materials = new Material.Baked[]{woolMaterial, ctmMaterial};

		BakedQuad[][][] quads = new BakedQuad[2][4][5];

		for (int dir = 0; dir < 2; dir++) {
			for (int quad = 0; quad < 4; quad++) {
				for (int type = 0; type < 5; type++) {
					Vector3f from = this.faceFrom[dir][quad][type];
					Vector3f to = this.faceTo[dir][quad][type];
					CuboidFace face = this.faceFaces[dir][quad][type];
					Material.Baked material = ConnectionLogic.values()[type].ordinal() == 0 ? woolMaterial : ctmMaterial;

					quads[dir][quad][type] = FaceBakery.bakeQuad(
						baker, from, to, face, material, Direction.values()[dir], state, null, true, 0
					);
				}
			}
		}

		return new RoyalRagsModel(baseQuads, quads, woolMaterial);
	}

	@Override
	@Nullable
	public UnbakedGeometry geometry() {
		return (textureSlots, baker, state, name) -> {
			QuadCollection.Builder builder = new QuadCollection.Builder();

			Material.Baked baseMaterial = baker.materials().get(textureSlots.getMaterial("wool"), name);
			Material.Baked ctmMaterial = baker.materials().get(textureSlots.getMaterial("wool_ctm"), name);

			// Bake base horizontal quads
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				for (int quad = 0; quad < 4; quad++) {
					Vector3f from = this.baseFrom[direction.get2DDataValue()][quad];
					Vector3f to = this.baseTo[direction.get2DDataValue()][quad];
					CuboidFace face = this.baseFaces[direction.get2DDataValue()][quad];

					BakedQuad bakedQuad = FaceBakery.bakeQuad(
						baker, from, to, face, baseMaterial, direction, state, null, true, 0
					);
					builder.addCulledFace(direction, bakedQuad);
				}
			}

			// Bake face quads (up/down) with all connection variants
			for (int dir = 0; dir < 2; dir++) {
				Direction faceDir = Direction.values()[dir];
				for (int quad = 0; quad < 4; quad++) {
					for (int type = 0; type < 5; type++) {
						Vector3f from = this.faceFrom[dir][quad][type];
						Vector3f to = this.faceTo[dir][quad][type];
						CuboidFace face = this.faceFaces[dir][quad][type];
						Material.Baked material = type == 0 ? baseMaterial : ctmMaterial;

						BakedQuad bakedQuad = FaceBakery.bakeQuad(
							baker, from, to, face, material, faceDir, state, null, true, 0
						);
						builder.addCulledFace(faceDir, bakedQuad);
					}
				}
			}

			return builder.build();
		};
	}

	@Override
	public com.mojang.serialization.MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
		throw new UnsupportedOperationException("UnbakedRoyalRagsModel does not support codec serialization");
	}
}
