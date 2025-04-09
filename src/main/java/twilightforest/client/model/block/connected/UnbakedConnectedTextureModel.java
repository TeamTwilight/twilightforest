package twilightforest.client.model.block.connected;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.RenderTypeGroup;
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
	protected final RenderTypeGroup group;

	public UnbakedConnectedTextureModel(EnumSet<Direction> enabledFaces, boolean renderOnDisabledFaces, List<Block> connectableBlocks, int baseTintIndex, int baseEmissivity, int tintIndex, int emissivity, StandardModelParameters parameters, RenderTypeGroup group) {
		super(parameters);
		//a list of block faces that should have connected textures.
		this.enabledFaces = enabledFaces;
		//whether or not the overlay texture should render on disabled faces or not. Defaults to true
		this.renderOnDisabledFaces = renderOnDisabledFaces;
		//a list of blocks this block can connect its texture to
		this.connectableBlocks = connectableBlocks;
		//base elements - the base block. No Connected Textures on this bit.
		//the array is made of the directions and quads
		this.baseElements = new BlockElement[6][4];

		//face elements - the connected bit of the model.
		//the array is made of the directions, quads, and each logic value in the ConnectionLogic class
		this.faceElements = new BlockElement[6][4][5];
		Vec3i center = new Vec3i(8, 8, 8);

		for (Direction face : Direction.values()) {
			Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];

			for(int i = 0; i < 4; ++i) {
				Vec3i corner = face.getUnitVec3i().offset(planeDirections[i].getUnitVec3i()).offset(planeDirections[(i + 1) % 4].getUnitVec3i()).offset(1, 1, 1).multiply(8);
				BlockElement element = new BlockElement(new Vector3f((float)Math.min(center.getX(), corner.getX()), (float)Math.min(center.getY(), corner.getY()), (float)Math.min(center.getZ(), corner.getZ())), new Vector3f((float)Math.max(center.getX(), corner.getX()), (float)Math.max(center.getY(), corner.getY()), (float)Math.max(center.getZ(), corner.getZ())), Map.of(), null, true, 0);
				this.baseElements[face.get3DDataValue()][i] = new BlockElement(element.from, element.to, Map.of(face, new BlockElementFace(face, baseTintIndex, "", new BlockFaceUV(ConnectionLogic.NONE.remapUVs(element.uvsByFace(face)), 0))), null, true, baseEmissivity);

				for (ConnectionLogic logic : ConnectionLogic.values()) {
					this.faceElements[face.get3DDataValue()][i][logic.ordinal()] = new BlockElement(element.from, element.to, Map.of(face, new BlockElementFace(face, tintIndex, "", new BlockFaceUV(logic.remapUVs(element.uvsByFace(face)), 0))), null, true, emissivity);
				}
			}
		}
		this.group = group;
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

		return new ConnectedTextureModel(this.enabledFaces, this.renderOnDisabledFaces, this.connectableBlocks, baseQuads, quads, sprites[2], itemTransforms, this.group);
	}
}