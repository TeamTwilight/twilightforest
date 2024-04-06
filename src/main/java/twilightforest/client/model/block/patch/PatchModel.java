package twilightforest.client.model.block.patch;

import com.google.common.collect.ImmutableList;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.block.PatchBlock;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;
import java.util.List;

/* Unlike the usual way models are done, this takes more of a state-oriented approach.
 * It is suboptimal but please, be my guest if you wish to see it improved */
public record PatchModel(ResourceLocation location, TextureAtlasSprite texture, boolean shaggify) implements BakedModel {
    private static final FaceBakery BAKERY = new FaceBakery();

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
        if (state == null)
            return this.getQuads(false, false, false, false, random);
        else
            return this.getQuads(state.getValue(PatchBlock.NORTH), state.getValue(PatchBlock.EAST), state.getValue(PatchBlock.SOUTH), state.getValue(PatchBlock.WEST), random);
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

        int minX = bb.minX();
        int minY = bb.minY();
        int minZ = bb.minZ();
        int maxX = bb.maxX();
        int maxY = bb.maxY();
        int maxZ = bb.maxZ();

        // add on shaggy edges
        if (!west) {
            Vector3f min = new Vector3f(minX, minY, minZ);
            Vector3f max = new Vector3f(maxX, maxY, maxZ);
            float originalMaxZ = max.z;

            long seed = westSeed;
            seed = seed * seed * 42317861L + seed * 7L;

            int num0 = (int) (seed >> 12 & 3L) + 1;
            int num1 = (int) (seed >> 15 & 3L) + 1;
            int num2 = (int) (seed >> 18 & 3L) + 1;
            int num3 = (int) (seed >> 21 & 3L) + 1;

            max.x = min.x;
            min.add(-1, 0, num0);
            if (max.z - ((num1 + num2 + num3)) > min.z) {
                // draw two blobs
                max.z = min.z + num1;
                this.quadsFromAABB(list, min.x, min.y, min.z, max.x, max.y, max.z);
                max.z = originalMaxZ - num2;
                min.z = max.z - num3;
                this.quadsFromAABB(list, min.x, min.y, min.z, max.x, max.y, max.z);
            } else {
                //draw one blob
                max.add(0, 0, -num2);
                this.quadsFromAABB(list, min.x, min.y, min.z, max.x, max.y, max.z);
            }
        }

        if (!east) {
            Vector3f min = new Vector3f(minX, minY, minZ);
            Vector3f max = new Vector3f(maxX, maxY, maxZ);
            float originalMaxZ = max.z;

            long seed = eastSeed;
            seed = seed * seed * 42317861L + seed * 17L;

            int num0 = (int) (seed >> 12 & 3L) + 1;
            int num1 = (int) (seed >> 15 & 3L) + 1;
            int num2 = (int) (seed >> 18 & 3L) + 1;
            int num3 = (int) (seed >> 21 & 3L) + 1;

            min.x = max.x;
            max.add(1, 0, 0);
            min.add(0, 0, num0);
            if (max.z - ((num1 + num2 + num3)) > min.z) {
                // draw two blobs
                max.z = min.z + num1;
                this.quadsFromAABB(list, min.x, min.y, min.z, max.x, max.y, max.z);
                max.z = originalMaxZ - num2;
                min.z = max.z - num3;
                this.quadsFromAABB(list, min.x, min.y, min.z, max.x, max.y, max.z);
            } else {
                //draw one blob
                max.add(0, 0, -num2);
                this.quadsFromAABB(list, min.x, min.y, min.z, max.x, max.y, max.z);
            }
        }

        if (!north) {
            Vector3f min = new Vector3f(minX, minY, minZ);
            Vector3f max = new Vector3f(maxX, maxY, maxZ);
            float originalMaxX = max.x;

            long seed = northSeed;
            seed = seed * seed * 42317861L + seed * 23L;

            int num0 = (int) (seed >> 12 & 3L) + 1;
            int num1 = (int) (seed >> 15 & 3L) + 1;
            int num2 = (int) (seed >> 18 & 3L) + 1;
            int num3 = (int) (seed >> 21 & 3L) + 1;

            max.z = min.z;
            min.add(num0, 0, -1F);
            max.x = min.x + num1;
            this.quadsFromAABB(list, min.x, min.y, min.z, max.x, max.y, max.z);
            max.x = originalMaxX - num2;
            min.x = max.x - num3;
            this.quadsFromAABB(list, min.x, min.y, min.z, max.x, max.y, max.z);
        }

        if (!south) {
            Vector3f min = new Vector3f(minX, minY, minZ);
            Vector3f max = new Vector3f(maxX, maxY, maxZ);
            float originalMaxX = max.x;

            long seed = southSeed;
            seed = seed * seed * 42317861L + seed * 11L;

            int num0 = (int) (seed >> 12 & 3L) + 1;
            int num1 = (int) (seed >> 15 & 3L) + 1;
            int num2 = (int) (seed >> 18 & 3L) + 1;
            int num3 = (int) (seed >> 21 & 3L) + 1;

            min.z = max.z;
            max.add(0, 0, 1F);
            min.add(num0, 0, 0);
            max.x = min.x + num1;
            this.quadsFromAABB(list, min.x, min.y, min.z, max.x, max.y, max.z);
            max.x = originalMaxX - num2;
            min.x = max.x - num3;
            this.quadsFromAABB(list, min.x, min.y, min.z, max.x, max.y, max.z);
        }

        return ImmutableList.copyOf(list);
    }

    private void quadsFromAABB(List<BakedQuad> quads, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        quads.add(this.quadFromVectors(Direction.UP, minX, minY, minZ, maxX, maxY, maxZ));
        quads.add(this.quadFromVectors(Direction.NORTH, minX, minY, minZ, maxX, maxY, maxZ));
        quads.add(this.quadFromVectors(Direction.EAST, minX, minY, minZ, maxX, maxY, maxZ));
        quads.add(this.quadFromVectors(Direction.SOUTH, minX, minY, minZ, maxX, maxY, maxZ));
        quads.add(this.quadFromVectors(Direction.WEST, minX, minY, minZ, maxX, maxY, maxZ));
    }

    private BakedQuad quadFromVectors(Direction direction, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		BlockElementFace face = new BlockElementFace(null, 0, this.texture.atlasLocation().toString(), switch (direction) {
            case NORTH -> new BlockFaceUV(new float[] {maxX, minZ + 1f, minX, minZ}, 0);
            case EAST  -> new BlockFaceUV(new float[] {maxX, minZ, maxX - 1f, maxZ}, 90);
            case SOUTH -> new BlockFaceUV(new float[] {minX, maxZ, maxX, maxZ - 1f }, 0);
            case WEST  -> new BlockFaceUV(new float[] {minX, maxZ, minX + 1f, minZ}, 90);
            default -> new BlockFaceUV(new float[] {minX, minZ, maxX, maxZ}, 0);
        });

        return BAKERY.bakeQuad(new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ), face, this.texture, direction, new SimpleModelState(Transformation.identity()), null, true, this.location);
    }

    // --- Boilerplating ---------------------------------------------------

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.texture;
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY; //I doubt we need to do anything here
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        if (state.is(TFBlocks.CLOVER_PATCH)) {
            return ChunkRenderTypeSet.of(RenderType.cutout());
        }
        return BakedModel.super.getRenderTypes(state, rand, data);
    }
}
