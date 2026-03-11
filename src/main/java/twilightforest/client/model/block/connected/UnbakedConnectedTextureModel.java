package twilightforest.client.model.block.connected;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.ExtraFaceData;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.client.model.geometry.UnbakedGeometryHelper;
import org.apache.commons.lang3.mutable.MutableObject;
import org.joml.Vector3f;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UnbakedConnectedTextureModel implements IUnbakedGeometry<UnbakedConnectedTextureModel> {

	private final boolean renderOnDisabledFaces;
	private final EnumSet<Direction> enabledFaces;
	private final List<Block> connectableBlocks;
	private final BlockElement[][] baseElements;
	private final BlockElement[][][] faceElements;

	public UnbakedConnectedTextureModel(EnumSet<Direction> enabledFaces, boolean renderOnDisabledFaces, List<Block> connectableBlocks, int baseTintIndex, int baseEmissivity, int tintIndex, int emissivity) {
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
		ExtraFaceData baseFace = new ExtraFaceData(-1, baseEmissivity, baseEmissivity, false);
		ExtraFaceData overlayFace = new ExtraFaceData(-1, emissivity, emissivity, true);
		Vec3i center = new Vec3i(8, 8, 8);

		for (Direction face : Direction.values()) {
			Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];

			for(int i = 0; i < 4; ++i) {
				Vec3i corner = face.getNormal().offset(planeDirections[i].getNormal()).offset(planeDirections[(i + 1) % 4].getNormal()).offset(1, 1, 1).multiply(8);
				BlockElement element = new BlockElement(new Vector3f((float)Math.min(center.getX(), corner.getX()), (float)Math.min(center.getY(), corner.getY()), (float)Math.min(center.getZ(), corner.getZ())), new Vector3f((float)Math.max(center.getX(), corner.getX()), (float)Math.max(center.getY(), corner.getY()), (float)Math.max(center.getZ(), corner.getZ())), Map.of(), null, true);
				this.baseElements[face.get3DDataValue()][i] = new BlockElement(element.from, element.to, Map.of(face, new BlockElementFace(face, baseTintIndex, "", new BlockFaceUV(ConnectionLogic.NONE.remapUVs(element.uvsByFace(face)), 0), baseFace, new MutableObject<>())), null, true);

				for (ConnectionLogic logic : ConnectionLogic.values()) {
					this.faceElements[face.get3DDataValue()][i][logic.ordinal()] = new BlockElement(element.from, element.to, Map.of(face, new BlockElementFace(face, tintIndex, "", new BlockFaceUV(logic.remapUVs(element.uvsByFace(face)), 0), overlayFace, new MutableObject<>())), null, true);
				}
			}
		}
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
		Transformation transformation = context.getRootTransform();
		if (!transformation.isIdentity()) {
			modelState = new SimpleModelState(modelState.getRotation().compose(transformation), modelState.isUvLocked());
		}

		//making an array list like this is cursed, would not recommend
		@SuppressWarnings("unchecked") //this is fine, I hope
		List<BakedQuad>[] baseQuads = (List<BakedQuad>[]) Array.newInstance(List.class, 6);

		if (context.hasMaterial("base_texture")) {
			TextureAtlasSprite baseTexture = spriteGetter.apply(context.getMaterial("base_texture"));

			for (int dir = 0; dir < 6; dir++) {
				baseQuads[dir] = new ArrayList<>();

				for (BlockElement element : this.baseElements[dir]) {
					baseQuads[dir].add(UnbakedGeometryHelper.bakeElementFace(element, element.faces.values().iterator().next(), baseTexture, Direction.values()[dir], modelState));
				}
			}
		} else {
			baseQuads = null;
		}

		//we'll use this to figure out which texture to use with the Connected Texture logic
		//NONE uses the first one, everything else uses the 2nd one
		TextureAtlasSprite[] sprites = new TextureAtlasSprite[]{spriteGetter.apply(context.getMaterial("overlay_texture")), spriteGetter.apply(context.getMaterial("overlay_connected")), spriteGetter.apply(context.getMaterial("particle"))};
		if (!context.hasMaterial("particle")) {
			sprites[2] = sprites[0];
		}

		BakedQuad[][][] quads = new BakedQuad[6][4][5];

		for (int dir = 0; dir < 6; dir++) {
			for (int quad = 0; quad < 4; quad++) {
				for (int type = 0; type < 5; type++) {
					BlockElement element = this.faceElements[dir][quad][type];
					quads[dir][quad][type] = UnbakedGeometryHelper.bakeElementFace(element, element.faces.values().iterator().next(), ConnectionLogic.values()[type].chooseTexture(sprites), Direction.values()[dir], modelState);
				}
			}
		}

		ResourceLocation renderTypeHint = context.getRenderTypeHint();
		RenderTypeGroup renderTypes = renderTypeHint != null ? context.getRenderType(renderTypeHint) : RenderTypeGroup.EMPTY;
		return new ConnectedTextureModel(this.enabledFaces, this.renderOnDisabledFaces, this.connectableBlocks, baseQuads, quads, sprites[2], overrides, context.getTransforms(), renderTypes);
	}
}