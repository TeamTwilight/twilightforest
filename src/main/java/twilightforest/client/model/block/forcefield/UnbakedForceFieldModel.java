package twilightforest.client.model.block.forcefield;

import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class UnbakedForceFieldModel extends AbstractUnbakedModel implements CustomUnbakedBlockStateModel {

	private static final ModelDebugName DEBUG_NAME = () -> "twilightforest:force_field";

	private final Map<CuboidModelElement, ForceFieldModelLoader.Condition> elementsAndConditions;

	public UnbakedForceFieldModel(Map<CuboidModelElement, ForceFieldModelLoader.Condition> elementsAndConditions, StandardModelParameters parameters) {
		super(parameters);
		this.elementsAndConditions = elementsAndConditions;
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

	public BlockStateModel bakeInternal(TextureSlots textures, ModelBaker baker, ModelState modelState, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties) {
		return new ForceFieldModel(this.elementsAndConditions, textures, baker, modelState, useAmbientOcclusion, usesBlockLight, itemTransforms);
	}

	@Override
	@Nullable
	public UnbakedGeometry geometry() {
		return (textureSlots, baker, state, name) -> {
			QuadCollection.Builder builder = new QuadCollection.Builder();
			ModelState modelState = BlockModelRotation.IDENTITY;

			for (Map.Entry<CuboidModelElement, ForceFieldModelLoader.Condition> entry : this.elementsAndConditions.entrySet()) {
				CuboidModelElement element = entry.getKey();

				for (Direction side : Direction.values()) {
					CuboidFace face = element.faces().get(side);
					if (face == null) continue;

					CuboidFace.UVs uvs;
					if (face.uvs() != null) {
						uvs = new CuboidFace.UVs(face.uvs().minU(), face.uvs().minV(), face.uvs().maxU(), face.uvs().maxV());
					} else {
						uvs = FaceBakery.defaultFaceUV(element.from(), element.to(), side);
					}

					CuboidFace cuboidFace = new CuboidFace(
						face.cullForDirection(),
						face.tintIndex(),
						face.texture(),
						uvs,
						face.rotation()
					);

					Material material = textureSlots.getMaterial(face.texture());
					if (material == null) continue;
					Material.Baked baked = baker.materials().get(material, name);

					BakedQuad quad = FaceBakery.bakeQuad(
						baker, element.from(), element.to(), cuboidFace, baked,
						side, modelState, element.rotation(), element.shade(), element.lightEmission()
					);

					Direction cullDir = face.cullForDirection();
					if (cullDir != null) {
						builder.addCulledFace(cullDir, quad);
					} else {
						builder.addUnculledFace(quad);
					}
				}
			}

			return builder.build();
		};
	}

	@Override
	public com.mojang.serialization.MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
		throw new UnsupportedOperationException("UnbakedForceFieldModel does not support codec serialization");
	}
}
