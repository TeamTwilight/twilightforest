package twilightforest.client.model.block.patch;

import com.mojang.math.Quadrant;
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
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class UnbakedPatchModel extends AbstractUnbakedModel implements CustomUnbakedBlockStateModel {

	private static final ModelDebugName DEBUG_NAME = () -> "twilightforest:patch";

        private final boolean shaggify;

        public UnbakedPatchModel(boolean shaggify, StandardModelParameters parameters) {
                super(parameters);
                this.shaggify = shaggify;
        }

        @Override
        public BlockStateModel bake(ModelBaker baker) {
                ResolvedModel resolved = baker.resolveInlineModel(this, DEBUG_NAME);
                TextureSlots textureSlots = resolved.getTopTextureSlots();
                ContextMap additionalProperties = resolved.getTopAdditionalProperties();
                ModelState state = BlockModelRotation.IDENTITY;
                boolean hasAmbientOcclusion = true;
                boolean useBlockLight = true;
                ItemTransforms transforms = this.parameters.itemTransforms();
                return bakeInternal(textureSlots, baker, state, hasAmbientOcclusion, useBlockLight, transforms, additionalProperties);
        }

        public BlockStateModel bakeInternal(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, boolean hasAmbientOcclusion, boolean useBlockLight, ItemTransforms transforms, ContextMap additionalProperties) {
                return new PatchModel(
                        baker.materials().get(textureSlots.getMaterial("texture"), DEBUG_NAME),
                        this.shaggify,
                        baker.materials().get(textureSlots.getMaterial("particle"), DEBUG_NAME),
                        hasAmbientOcclusion,
                        useBlockLight,
                        transforms
                );
        }

        @Override
        @Nullable
        public UnbakedGeometry geometry() {
                return (textureSlots, baker, state, name) -> {
                        QuadCollection.Builder builder = new QuadCollection.Builder();
                        Material material = textureSlots.getMaterial("texture");
                        if (material == null) return builder.build();
                        Material.Baked baked = baker.materials().get(material, name);

                        quadsFromAABB(builder, baked, 4.0f, 8.0f, 4.0f, 12.0f, 9.0f, 12.0f, state, baker);

                        return builder.build();
                };
        }

        private static void quadsFromAABB(QuadCollection.Builder builder, Material.Baked baked, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, ModelState state, ModelBaker baker) {
                for (Direction dir : Direction.values()) {
                        CuboidFace.UVs uvs = switch (dir) {
                                case DOWN -> new CuboidFace.UVs(minX, 16.0f - maxZ, maxX, 16.0f - minZ);
                                case UP -> new CuboidFace.UVs(minX, minZ, maxX, maxZ);
                                case NORTH -> new CuboidFace.UVs(16.0f - maxX, 16.0f - maxY, 16.0f - minX, 16.0f - minY);
                                case SOUTH -> new CuboidFace.UVs(minX, 16.0f - maxY, maxX, 16.0f - minY);
                                case WEST -> new CuboidFace.UVs(minZ, 16.0f - maxY, maxZ, 16.0f - minY);
                                case EAST -> new CuboidFace.UVs(16.0f - maxZ, 16.0f - maxY, 16.0f - minZ, 16.0f - minY);
                        };
                        CuboidFace face = new CuboidFace(dir, -1, "", uvs, Quadrant.R0);
                        BakedQuad quad = FaceBakery.bakeQuad(
                                baker, new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ),
                                face, baked, dir, state, null, true, 0
                        );
                        builder.addCulledFace(dir, quad);
                }
        }

        @Override
        public com.mojang.serialization.MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
                throw new UnsupportedOperationException("UnbakedPatchModel does not support codec serialization");
        }
}
