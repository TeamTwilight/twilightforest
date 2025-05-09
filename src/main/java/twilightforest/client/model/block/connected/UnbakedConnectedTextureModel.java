package twilightforest.client.model.block.connected;

import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.NeoForgeModelProperties;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedElementsHelper;
import org.joml.Vector3f;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class UnbakedConnectedTextureModel extends AbstractUnbakedModel {

	protected final boolean renderOnDisabledFaces;
	protected final EnumSet<Direction> enabledFaces;
	protected final List<Block> connectableBlocks;
	protected BlockElement[][] baseElements;
	protected BlockElement[][][] faceElements;
	protected final EnumSet<Direction> unculledFaces;

	public UnbakedConnectedTextureModel(Pair<Vector3f, Vector3f> element, EnumSet<Direction> enabledFaces, EnumSet<Direction> unculledFaces, boolean renderOnDisabledFaces, List<Block> connectableBlocks, int baseTintIndex, int baseEmissivity, int tintIndex, int emissivity, StandardModelParameters parameters) {
		super(parameters);
		//a list of block faces that should have connected textures.
		this.enabledFaces = enabledFaces;
		//a list of block faces that should not be culled. Probably cuz they don't extend to the edge of the block.
		this.unculledFaces = unculledFaces;
		//whether or not the overlay texture should render on disabled faces or not. Defaults to true
		this.renderOnDisabledFaces = renderOnDisabledFaces;
		//a list of blocks this block can connect its texture to
		this.connectableBlocks = connectableBlocks;
		//base elements - the base block. No Connected Textures on this bit.
		//the array is made of the directions and "sections". Each section is a corner quadrant of the block
		this.baseElements = new BlockElement[6][4];

		//face elements - the connected bit of the model.
		//the array is made of the directions, "sections", and each logic value in the ConnectionLogic class
		this.faceElements = new BlockElement[6][4][5];
		int center = 8;

		for (Direction face : Direction.values()) {
			Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];

			for (int i = 0; i < 4; ++i) {
				Vec3i corner = face.getUnitVec3i().offset(planeDirections[i].getUnitVec3i()).offset(planeDirections[(i + 1) % 4].getUnitVec3i()).offset(1, 1, 1).multiply(8);
				BlockElement modifiedElement = new BlockElement(
					new Vector3f(
						Math.clamp(Math.min(center - (16 - element.getSecond().x()), corner.getX() + element.getFirst().x()), 0, 16),
						Math.clamp(Math.min(center - (16 - element.getSecond().y()), corner.getY() + element.getFirst().y()), 0, 16),
						Math.clamp(Math.min(center - (16 - element.getSecond().z()), corner.getZ() + element.getFirst().z()), 0, 16)),
					new Vector3f(
						element.getSecond().x() < center ? element.getSecond().x() : Math.max(center, corner.getX() - (16 - element.getSecond().x())),
						element.getSecond().y() < center ? element.getSecond().y() : Math.max(center, corner.getY() - (16 - element.getSecond().y())),
						element.getSecond().z() < center ? element.getSecond().z() : Math.max(center, corner.getZ() - (16 - element.getSecond().z()))),
					Map.of(), null, true, 0);
				this.baseElements[face.get3DDataValue()][i] = new BlockElement(modifiedElement.from, modifiedElement.to, Map.of(face, new BlockElementFace(null, baseTintIndex, "", new BlockFaceUV(ConnectionLogic.NONE.remapUVs(modifiedElement.uvsByFace(face)), 0))), null, true, baseEmissivity);

				for (ConnectionLogic logic : ConnectionLogic.values()) {
					this.faceElements[face.get3DDataValue()][i][logic.ordinal()] = new BlockElement(modifiedElement.from, modifiedElement.to, Map.of(face, new BlockElementFace(null, tintIndex, "", new BlockFaceUV(logic.remapUVs(modifiedElement.uvsByFace(face)), 0))), null, true, emissivity);
				}
			}
		}
	}

	private float getElementScalar(Vector3f corner, Direction direction) {
		return switch (direction.getAxis()) {
			case X -> corner.x();
			case Y -> corner.y();
			case Z -> corner.z();
		};
	}

	@Override
	public BakedModel bake(TextureSlots textureSlots, ModelBaker baker, ModelState state, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties) {
		Transformation rootTransform = additionalProperties.getOrDefault(NeoForgeModelProperties.TRANSFORM, Transformation.identity());
		if (!rootTransform.isIdentity())
			state = UnbakedElementsHelper.composeRootTransformIntoModelState(state, rootTransform);

		@SuppressWarnings("unchecked") //this is fine, I hope
		List<BakedQuad>[] baseQuads = (List<BakedQuad>[]) Array.newInstance(List.class, 6);

		if (textureSlots.getMaterial("base_texture") != null) {
			TextureAtlasSprite baseTexture = baker.findSprite(textureSlots, "base_texture");

			for (int dir = 0; dir < 6; dir++) {
				baseQuads[dir] = new ArrayList<>();

				for (BlockElement element : this.baseElements[dir]) {
					baseQuads[dir].add(FaceBakery.bakeQuad(element.from, element.to, element.faces.values().iterator().next(), baseTexture, Direction.values()[dir], state, element.rotation, element.shade, element.lightEmission));
				}
			}
		} else {
			baseQuads = null;
		}

		//we'll use this to figure out which texture to use with the Connected Texture logic
		//NONE uses the first one, everything else uses the 2nd one
		TextureAtlasSprite[] sprites = new TextureAtlasSprite[]{baker.findSprite(textureSlots, "overlay_texture"), baker.findSprite(textureSlots, "overlay_connected"), baker.findSprite(textureSlots, "particle")};
		if (textureSlots.getMaterial("particle") == null) {
			sprites[2] = sprites[0];
		}

		BakedQuad[][][] quads = new BakedQuad[6][4][5];

		for (int dir = 0; dir < 6; dir++) {
			for (int quad = 0; quad < 4; quad++) {
				for (int type = 0; type < 5; type++) {
					BlockElement element = this.faceElements[dir][quad][type];
					quads[dir][quad][type] = FaceBakery.bakeQuad(element.from, element.to, element.faces.values().iterator().next(), ConnectionLogic.values()[type].chooseTexture(sprites), Direction.values()[dir], state, element.rotation, element.shade, element.lightEmission);
				}
			}
		}

		return new ConnectedTextureModel(this.enabledFaces, this.unculledFaces, this.renderOnDisabledFaces, this.connectableBlocks, baseQuads, quads, sprites[2], useAmbientOcclusion, usesBlockLight, itemTransforms, this.parameters.renderTypeGroup());
	}
}