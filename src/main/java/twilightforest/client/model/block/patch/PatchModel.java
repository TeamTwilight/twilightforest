package twilightforest.client.model.block.patch;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Transparency;
import com.mojang.math.Quadrant;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import twilightforest.block.PatchBlock;

import java.util.ArrayList;
import java.util.List;

public class PatchModel implements DynamicBlockStateModel {

        private static final ModelBaker.Interner IDENTITY_INTERNER = new ModelBaker.Interner() {
                @Override
                public Vector3fc vector(Vector3fc vector) {
                        return vector;
                }

                @Override
                public BakedQuad.MaterialInfo materialInfo(BakedQuad.MaterialInfo material) {
                        return material;
                }
        };

        private final Material.Baked texture;
        private final boolean shaggify;
        private final Material.Baked particle;
        private final boolean usesAO;
        private final boolean usesBlockLight;
        private final ItemTransforms transforms;

        public PatchModel(Material.Baked texture, boolean shaggify, Material.Baked particle, boolean usesAO, boolean usesBlockLight, ItemTransforms transforms) {
                this.texture = texture;
                this.shaggify = shaggify;
                this.particle = particle;
                this.usesAO = usesAO;
                this.usesBlockLight = usesBlockLight;
                this.transforms = transforms;
        }

        @Override
        public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
                boolean north = state.getValue(PatchBlock.NORTH);
                boolean east = state.getValue(PatchBlock.EAST);
                boolean south = state.getValue(PatchBlock.SOUTH);
                boolean west = state.getValue(PatchBlock.WEST);

                parts.add(new PatchModelPart(this.getQuads(north, east, south, west, random)));
        }

        @Override
        @Deprecated
        public void collectParts(RandomSource random, @NotNull List<BlockStateModelPart> parts) {
                parts.add(new PatchModelPart(this.getQuads(false, false, false, false, random)));
        }

        private List<BakedQuad> getQuads(boolean north, boolean east, boolean south, boolean west, RandomSource posRandom) {
                List<BakedQuad> list = new ArrayList<>();

                BoundingBox bb = PatchBlock.AABBFromRandom(posRandom);

                this.quadsFromAABB(list, west ? 0 : bb.minX(), bb.minY(), north ? 0 : bb.minZ(), east ? 16 : bb.maxX(), bb.maxY(), south ? 16 : bb.maxZ());

                if (!this.shaggify)
                        return ImmutableList.copyOf(list);

                // Poll these seeds before entering branching code, otherwise placing neighbors will cause odd changes
                long westSeed = posRandom.nextLong();
                long eastSeed = posRandom.nextLong();
                long northSeed = posRandom.nextLong();
                long southSeed = posRandom.nextLong();

                int minY = bb.minY();
                int maxY = bb.maxY();

                // add on shaggy edges
                if (!west) {
                        long seed = westSeed;
                        seed = seed * seed * 42317861L + seed * 7L;

                        int num0 = (int) (seed >> 12 & 3L) + 1;
                        int num1 = (int) (seed >> 15 & 3L) + 1;
                        int num2 = (int) (seed >> 18 & 3L) + 1;
                        int num3 = (int) (seed >> 21 & 3L) + 1;

                        int minZ = bb.minZ() + num0;
                        int maxZ = bb.maxZ();

                        if (maxZ - ((num1 + num2 + num3)) > minZ) {
                                // draw two blobs
                                int innerZ = bb.maxZ() - num2;
                                this.quadsFromAABB(list, bb.minX() - 1, minY, minZ, bb.minX(), maxY, minZ + num1);
                                this.quadsFromAABB(list, bb.minX() - 1, minY, innerZ - num3, bb.minX(), maxY, innerZ);
                        } else {
                                //draw one blob
                                this.quadsFromAABB(list, bb.minX() - 1, minY, minZ, bb.minX(), maxY, maxZ - num2);
                        }
                }

                if (!east) {
                        long seed = eastSeed;
                        seed = seed * seed * 42317861L + seed * 17L;

                        int num0 = (int) (seed >> 12 & 3L) + 1;
                        int num1 = (int) (seed >> 15 & 3L) + 1;
                        int num2 = (int) (seed >> 18 & 3L) + 1;
                        int num3 = (int) (seed >> 21 & 3L) + 1;

                        int minZ = bb.minZ() + num0;
                        int maxZ = bb.maxZ();

                        if (maxZ - ((num1 + num2 + num3)) > minZ) {
                                // draw two blobs
                                int innerZ = maxZ - num2;
                                this.quadsFromAABB(list, bb.maxX(), minY, minZ, bb.maxX() + 1, maxY, minZ + num1);
                                this.quadsFromAABB(list, bb.maxX(), minY, innerZ - num3, bb.maxX() + 1, maxY, innerZ);
                        } else {
                                //draw one blob
                                this.quadsFromAABB(list, bb.maxX(), minY, minZ, bb.maxX() + 1, maxY, maxZ - num2);
                        }
                }

                if (!north) {
                        long seed = northSeed;
                        seed = seed * seed * 42317861L + seed * 23L;

                        int num0 = (int) (seed >> 12 & 3L) + 1;
                        int num1 = (int) (seed >> 15 & 3L) + 1;
                        int num2 = (int) (seed >> 18 & 3L) + 1;
                        int num3 = (int) (seed >> 21 & 3L) + 1;

                        int minX = bb.minX() + num0;
                        int innerX = minX + num1;
                        int maxX = bb.maxX() - num2;

                        this.quadsFromAABB(list, minX, minY, bb.minZ() - 1, innerX, maxY, bb.minZ());
                        this.quadsFromAABB(list, maxX - num3, minY, bb.minZ() - 1, maxX, maxY, bb.minZ());
                }

                if (!south) {
                        long seed = southSeed;
                        seed = seed * seed * 42317861L + seed * 11L;

                        int num0 = (int) (seed >> 12 & 3L) + 1;
                        int num1 = (int) (seed >> 15 & 3L) + 1;
                        int num2 = (int) (seed >> 18 & 3L) + 1;
                        int num3 = (int) (seed >> 21 & 3L) + 1;

                        int minX = bb.minX() + num0;
                        int maxX = bb.maxX() - num2;

                        this.quadsFromAABB(list, minX, minY, bb.maxZ(), minX + num1, maxY, bb.maxZ() + 1);
                        this.quadsFromAABB(list, maxX - num3, minY, bb.maxZ(), maxX, maxY, bb.maxZ() + 1);
                }

                return ImmutableList.copyOf(list);
        }

        private void quadsFromAABB(List<BakedQuad> quads, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
                quads.add(this.quadFromVectors(Direction.UP, minX, minY, minZ, maxX, maxY, maxZ));
                quads.add(this.quadFromVectors(Direction.NORTH, minX, minY, minZ, maxX, maxY, maxZ));
                quads.add(this.quadFromVectors(Direction.EAST, minX, minY, minZ, maxX, maxY, maxZ));
                quads.add(this.quadFromVectors(Direction.SOUTH, minX, minY, minZ, maxX, maxY, maxZ));
                quads.add(this.quadFromVectors(Direction.WEST, minX, minY, minZ, maxX, maxY, maxZ));
                quads.add(this.quadFromVectors(Direction.DOWN, minX, minY, minZ, maxX, maxY, maxZ));
        }

        private BakedQuad quadFromVectors(Direction direction, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
                CuboidFace.UVs uvs = switch (direction) {
                        case NORTH -> new CuboidFace.UVs(maxX, minZ + 1f, minX, minZ);
                        case EAST -> new CuboidFace.UVs(maxX, minZ, maxX - 1f, maxZ);
                        case SOUTH -> new CuboidFace.UVs(minX, maxZ, maxX, maxZ - 1f);
                        case WEST -> new CuboidFace.UVs(minX, maxZ, minX + 1f, minZ);
                        default -> new CuboidFace.UVs(minX, minZ, maxX, maxZ);
                };

                Transparency transparency = this.texture.forceTranslucent()
                        ? Transparency.TRANSLUCENT
                        : this.texture.sprite().transparency();

                BakedQuad.MaterialInfo materialInfo = BakedQuad.MaterialInfo.of(this.texture, transparency, 0, true, 0);

                return FaceBakery.bakeQuad(
                        IDENTITY_INTERNER,
                        new Vector3f(minX, minY, minZ),
                        new Vector3f(maxX, maxY, maxZ),
                        uvs,
                        Quadrant.R0,
                        materialInfo,
                        direction,
                        BlockModelRotation.IDENTITY,
                        null
                );
        }

        @Override
        public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
                return this.particle;
        }

        @Override
        @Deprecated
        public Material.Baked particleMaterial() {
                return this.particle;
        }

        @Override
        public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
                return 0;
        }

        @Override
        @Deprecated
        public int materialFlags() {
                return 0;
        }

        public ItemTransforms getTransforms() {
                return this.transforms;
        }

        private final class PatchModelPart implements BlockStateModelPart {
                private final List<BakedQuad> quads;

                private PatchModelPart(List<BakedQuad> quads) {
                        this.quads = quads;
                }

                @Override
                public List<BakedQuad> getQuads(@Nullable Direction direction) {
                        if (direction == null) return List.of();
                        List<BakedQuad> result = new ArrayList<>();
                        for (BakedQuad quad : this.quads) {
                                if (quad.direction() == direction) {
                                        result.add(quad);
                                }
                        }
                        return result;
                }

                @Override
                @Deprecated
                public boolean useAmbientOcclusion() {
                        return usesAO;
                }

                @Override
                public Material.Baked particleMaterial() {
                        return particle;
                }

                @Override
                public int materialFlags() {
                        return 0;
                }
        }
}

