package twilightforest.client.model.block.carpet;

import com.mojang.math.Quadrant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.data.AtlasIds;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.client.model.block.connected.ConnectionLogic;
import twilightforest.util.UnbakedGeometryUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//for now, im keeping this hardcoded to a 2 layer block, with the overlay layer being fullbright and tinted.
//It might be worth expanding this in the future to be more flexible for other kinds of blocks (1 layer blocks, determining emissivity and tinting per layer, maybe >2 layer blocks?) but for now, I see no point.
//I only wanted this system for castle doors after all!
public class UnbakedRoyalRagsModel implements UnbakedGeometry {

	private final CuboidModelElement[][] baseElements;
	private final CuboidModelElement[][][] faceElements;

	public UnbakedRoyalRagsModel() {
		//base elements - the side faces without ctm. No Connected Textures on this bit.
		//the array is made of horizontal directions (Direction.get2DDataValue) and quads
		this.baseElements = new CuboidModelElement[4][4];

		//face elements - the connected bit of the model.
		//the array is made of the directions, quads, and each logic value in the ConnectionLogic class
		//Topmost array indexes to up/dpwn directions (Direction.get3DDataValue, down = 0, up = 1) then inside are quads
		this.faceElements = new CuboidModelElement[2][4][5];
		Vec3i center = new Vec3i(8, 8, 8);

		for (Direction face : Direction.values()) {
			Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];

			for (int quad = 0; quad < 4; quad++) {
				Vec3i corner = face.getUnitVec3i().offset(planeDirections[quad].getUnitVec3i()).offset(planeDirections[(quad + 1) % 4].getUnitVec3i()).offset(1, 1, 1).multiply(8);
				CuboidModelElement element = new CuboidModelElement(new Vector3f((float) Math.min(center.getX(), corner.getX()), (float) Math.min(center.getY(), corner.getY()) / 16f, (float) Math.min(center.getZ(), corner.getZ())), new Vector3f((float) Math.max(center.getX(), corner.getX()), (float) Math.max(center.getY(), corner.getY()) / 16f, (float) Math.max(center.getZ(), corner.getZ())), Map.of());

				if (face.getAxis().isHorizontal()) {
					this.baseElements[face.get2DDataValue()][quad] = new CuboidModelElement(element.from(), element.to(), Map.of(face, new CuboidFace(face, -1, "", ConnectionLogic.NONE.remapUVs(UnbakedGeometryUtil.uvsByFace(face, element)), Quadrant.R0)));
				} else {
					for (ConnectionLogic connectionType : ConnectionLogic.values()) {
						this.faceElements[face.get3DDataValue()][quad][connectionType.ordinal()] = new CuboidModelElement(element.from(), element.to(), Map.of(face, new CuboidFace(face, 0, "", connectionType.remapUVs(UnbakedGeometryUtil.uvsByFace(face, element)), Quadrant.R0)));
					}
				}
			}
		}
	}

	public QuadCollection getQuads(@Nullable Direction side, List<BakedQuad>[] baseQuads, BakedQuad[][][] quads) {
		if (side != null) {
			QuadCollection.Builder builder = new QuadCollection.Builder();
			if (side.getAxis().isHorizontal()) {
				if (baseQuads != null) {
					for (BakedQuad bakedQuad : baseQuads[side.get2DDataValue()]) {
						builder.addCulledFace(side, bakedQuad);
					}
				}
			} else {
				int faceIndex = side.get3DDataValue();
				for (int quad = 0; quad < 4; ++quad) {
					ConnectionLogic connectionType = ConnectionLogic.NONE;
					builder.addCulledFace(side, quads[faceIndex][quad][connectionType.ordinal()]);
				}
			}

			return builder.build();
		} else {
			return QuadCollection.EMPTY;
		}
	}

	@Override
	public QuadCollection bake(TextureSlots textureSlots, ModelBaker modelBaker, ModelState modelState, ModelDebugName modelDebugName) {
		//making an array list like this is cursed, would not recommend
		@SuppressWarnings("unchecked") //this is fine, I hope
		List<BakedQuad>[] baseQuads = (List<BakedQuad>[]) Array.newInstance(List.class, 4);
		Material baseMaterial = textureSlots.getMaterial("wool");
		TextureAtlasSprite baseTexture = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(baseMaterial.sprite());
		Material.Baked baseBakedMaterial = new Material.Baked(baseTexture, baseMaterial.forceTranslucent());

		Material ctmMaterial = textureSlots.getMaterial("wool_ctm");
		TextureAtlasSprite ctmTexture = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(ctmMaterial.sprite());

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			baseQuads[direction.get2DDataValue()] = new ArrayList<>();

			for (CuboidModelElement element : this.baseElements[direction.get2DDataValue()]) {
				baseQuads[direction.get2DDataValue()].add(UnbakedGeometryUtil.bakeElementFace(modelBaker, element, element.faces().values().iterator().next(), baseBakedMaterial, direction, modelState));
			}
		}

		//we'll use this to figure out which texture to use with the Connected Texture logic
		//NONE uses the first one, everything else uses the 2nd one
		TextureAtlasSprite[] sprites = new TextureAtlasSprite[]{baseTexture, ctmTexture};
		Material[] materials = new Material[]{baseMaterial, ctmMaterial};

		BakedQuad[][][] quads = new BakedQuad[2][4][5];

		for (int dir = 0; dir < 2; dir++) {
			for (int quad = 0; quad < 4; quad++) {
				for (int type = 0; type < 5; type++) {
					CuboidModelElement element = this.faceElements[dir][quad][type];
					Material.Baked bakedChoice = UnbakedGeometryUtil.chooseAndBake(ConnectionLogic.values()[type], sprites, materials);
					quads[dir][quad][type] = UnbakedGeometryUtil.bakeElementFace(modelBaker, element, element.faces().values().iterator().next(), bakedChoice, Direction.values()[dir], modelState);
				}
			}
		}

		QuadCollection.Builder builder = new QuadCollection.Builder();

		for (Direction value : Direction.values()) {
			builder.addAll(getQuads(value, baseQuads, quads));
		}

		return builder.build();
	}
}