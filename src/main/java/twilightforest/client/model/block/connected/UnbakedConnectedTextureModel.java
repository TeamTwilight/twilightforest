package twilightforest.client.model.block.connected;

import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.NeoForgeModelProperties;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedElementsHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

public class UnbakedConnectedTextureModel extends AbstractUnbakedModel {

	protected final boolean renderOverlayOnAllFaces;
	protected final Set<Direction> connectedFaces;
	protected final List<Block> connectableBlocks;
//	protected BlockElement[][] baseElements;
//	protected BlockElement[][][] connectedElements;

	public UnbakedConnectedTextureModel(Pair<Vector3f, Vector3f> element, Set<Direction> connectedFaces, boolean renderOnDisabledFaces, List<Block> connectableBlocks, int baseTintIndex, int baseEmissivity, int tintIndex, int emissivity, StandardModelParameters parameters) {
		super(parameters);
		//a list of block faces that should have connected textures.
		this.connectedFaces = connectedFaces;
		//whether the overlay texture should render on all faces or not. Defaults to true
		this.renderOverlayOnAllFaces = renderOnDisabledFaces;
		//a list of blocks this block can connect its texture to
		this.connectableBlocks = connectableBlocks;
		//base elements - the base block. No Connected Textures on this bit.
		//the array is made of the directions and "sections". Each section is a corner quadrant of the block
//		this.baseElements = new BlockElement[6][4];
		//face elements - the connected bit of the model.
		//the array is made of the directions, "sections", and each logic value in the ConnectionLogic class
//		this.connectedElements = new BlockElement[6][4][5];

		int center = 8;

		for (Direction face : Direction.values()) {
			Direction cull = this.getCullface(face, element.getFirst(), element.getSecond());
			Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];

			for (int i = 0; i < 4; ++i) {
				Vec3i corner = face.getUnitVec3i().offset(planeDirections[i].getUnitVec3i()).offset(planeDirections[(i + 1) % 4].getUnitVec3i()).offset(1, 1, 1).multiply(8);
//				BlockElement modifiedElement = new BlockElement(
//					new Vector3f(
//						Math.clamp(Math.min(center - (16 - element.getSecond().x()), corner.getX() + element.getFirst().x()), 0, 16),
//						Math.clamp(Math.min(center - (16 - element.getSecond().y()), corner.getY() + element.getFirst().y()), 0, 16),
//						Math.clamp(Math.min(center - (16 - element.getSecond().z()), corner.getZ() + element.getFirst().z()), 0, 16)),
//					new Vector3f(
//						element.getSecond().x() < center ? element.getSecond().x() : Math.max(center, corner.getX() - (16 - element.getSecond().x())),
//						element.getSecond().y() < center ? element.getSecond().y() : Math.max(center, corner.getY() - (16 - element.getSecond().y())),
//						element.getSecond().z() < center ? element.getSecond().z() : Math.max(center, corner.getZ() - (16 - element.getSecond().z()))),
//					Map.of(), null, true, 0);
//				this.baseElements[face.get3DDataValue()][i] = new BlockElement(modifiedElement.from, modifiedElement.to, Map.of(face, new BlockElementFace(cull, baseTintIndex, "", new BlockFaceUV(ConnectionLogic.NONE.remapUVs(modifiedElement.uvsByFace(face)), 0))), null, true, baseEmissivity);

				for (ConnectionLogic logic : ConnectionLogic.values()) {
//					this.connectedElements[face.get3DDataValue()][i][logic.ordinal()] = new BlockElement(modifiedElement.from, modifiedElement.to, Map.of(face, new BlockElementFace(cull, tintIndex, "", new BlockFaceUV(logic.remapUVs(modifiedElement.uvsByFace(face)), 0))), null, true, emissivity);
				}
			}
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

//	@Override
//	public BakedModel bake(TextureSlots textureSlots, ModelBaker baker, ModelState state, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties) {
//		Transformation rootTransform = additionalProperties.getOrDefault(NeoForgeModelProperties.TRANSFORM, Transformation.identity());
//		if (!rootTransform.isIdentity())
//			state = UnbakedElementsHelper.composeRootTransformIntoModelState(state, rootTransform);
//
//		Map<Direction, BakedQuad[]> baseQuads = new HashMap<>();
//		Set<Direction> unculledFaces = new HashSet<>();
//
//		if (textureSlots.getMaterial("base_texture") != null) {
//			TextureAtlasSprite baseTexture = baker.findSprite(textureSlots, "base_texture");
//
//			for (Direction dir : Direction.values()) {
//				List<BakedQuad> quadList = new ArrayList<>();
//
//				for (BlockElement element : this.baseElements[dir.get3DDataValue()]) {
//					quadList.add(FaceBakery.bakeQuad(element.from, element.to, element.faces.get(dir), baseTexture, dir, state, element.rotation, element.shade, element.lightEmission));
//				}
//				baseQuads.put(dir, quadList.toArray(new BakedQuad[0]));
//			}
//		}
//
//		//we'll use this to figure out which texture to use with the Connected Texture logic
//		//NONE uses the first one, everything else uses the 2nd one
//		TextureAtlasSprite[] sprites = new TextureAtlasSprite[]{baker.findSprite(textureSlots, "overlay_texture"), baker.findSprite(textureSlots, "overlay_connected"), baker.findSprite(textureSlots, "particle")};
//		if (textureSlots.getMaterial("particle") == null) {
//			sprites[2] = sprites[0];
//		}
//
//		Map<Direction, BakedQuad[][]> connectedQuads = new HashMap<>();
//
//		for (Direction dir : Direction.values()) {
//			BakedQuad[][] dirQuads = new BakedQuad[4][5];
//			for (int quad = 0; quad < 4; quad++) {
//				for (int type = 0; type < 5; type++) {
//					BlockElement element = this.connectedElements[dir.get3DDataValue()][quad][type];
//					BlockElementFace face = element.faces.get(dir);
//					if (face.cullForDirection() == null) unculledFaces.add(dir);
//					dirQuads[quad][type] = FaceBakery.bakeQuad(element.from, element.to, face, ConnectionLogic.values()[type].chooseTexture(sprites), dir, state, element.rotation, element.shade, element.lightEmission);
//				}
//			}
//			connectedQuads.put(dir, dirQuads);
//		}
//
//		return new ConnectedTextureModel(this.connectedFaces, unculledFaces, this.renderOverlayOnAllFaces, this.connectableBlocks, baseQuads, connectedQuads, sprites[2], useAmbientOcclusion, usesBlockLight, itemTransforms, this.parameters.renderTypeGroup());
//	}
}