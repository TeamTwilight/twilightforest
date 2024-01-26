package twilightforest.client.model.block.patch;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
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

    // POOLED - not threadsafe
    private static final Vector3f MIN = new Vector3f(0, 0, 0);
    private static final Vector3f MAX = new Vector3f(0, 0, 0);

    private void setVectors(AABB bb) {
        MIN.set((float) bb.minX, (float) bb.minY, (float) bb.minZ);
        MAX.set((float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
    }

    private void setVectors(AABB bb, boolean north, boolean east, boolean south, boolean west) {
        MIN.set(west ? 0 : (float) bb.minX, (float) bb.minY, north ? 0 : (float) bb.minZ);
        MAX.set(east ? 16 : (float) bb.maxX, (float) bb.maxY, south ? 16 : (float) bb.maxZ);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
        if (state == null)
            return this.getQuads(false, false, false, false, PatchBlock.AABBFromRandom(random), random);
        else
            return this.getQuads(state.getValue(PatchBlock.NORTH), state.getValue(PatchBlock.EAST), state.getValue(PatchBlock.SOUTH), state.getValue(PatchBlock.WEST), PatchBlock.AABBFromRandom(random), random);
    }

    private List<BakedQuad> getQuads(boolean north, boolean east, boolean south, boolean west, AABB bb, RandomSource random) {
        List<BakedQuad> list = new ArrayList<>();

        this.setVectors(bb, north, east, south, west);

        this.quadsFromAABB(list);

        if (!this.shaggify())
            return list;

        // This has to be set without connections or else there will be inconsistency problems
        this.setVectors(bb);

        // add on shaggy edges
        if (MIN.x() > 0) {
            float originalMaxZ = MAX.z();

            long seed = random.nextLong();
            seed = seed * seed * 42317861L + seed * 7L;

            int num0 = (int) (seed >> 12 & 3L) + 1;
            int num1 = (int) (seed >> 15 & 3L) + 1;
            int num2 = (int) (seed >> 18 & 3L) + 1;
            int num3 = (int) (seed >> 21 & 3L) + 1;

            MAX.x = MIN.x();
            MIN.add(-1, 0, num0);
            if (MAX.z() - ((num1 + num2 + num3)) > MIN.z()) {
                // draw two blobs
                MAX.z = MIN.z() + num1;
                this.quadsFromAABB(list);
                MAX.z = originalMaxZ - num2;
                MIN.z = MAX.z() - num3;
                this.quadsFromAABB(list);
            } else {
                //draw one blob
                MAX.add(0, 0, -num2);
                this.quadsFromAABB(list);
            }

            // reset render bounds
            this.setVectors(bb);
        }
        if (MAX.x() < 16F) {
            float originalMaxZ = MAX.z();

            long seed = random.nextLong();
            seed = seed * seed * 42317861L + seed * 17L;

            int num0 = (int) (seed >> 12 & 3L) + 1;
            int num1 = (int) (seed >> 15 & 3L) + 1;
            int num2 = (int) (seed >> 18 & 3L) + 1;
            int num3 = (int) (seed >> 21 & 3L) + 1;

            MIN.x = MAX.x();
            MAX.add(1, 0, 0);
            MIN.add(0, 0, num0);
            if (MAX.z() - ((num1 +num2 + num3)) > MIN.z()) {
                // draw two blobs
                MAX.z = MIN.z() + num1;
                this.quadsFromAABB(list);
                MAX.z = originalMaxZ - num2;
                MIN.z = MAX.z() - num3;
                this.quadsFromAABB(list);
            } else {
                //draw one blob
                MAX.add(0, 0, -num2);
                this.quadsFromAABB(list);
            }
            // reset render bounds
            this.setVectors(bb);
        }
        if (MIN.z() > 0) {
            float originalMaxX = MAX.x();

            long seed = random.nextLong();
            seed = seed * seed * 42317861L + seed * 23L;

            int num0 = (int) (seed >> 12 & 3L) + 1;
            int num1 = (int) (seed >> 15 & 3L) + 1;
            int num2 = (int) (seed >> 18 & 3L) + 1;
            int num3 = (int) (seed >> 21 & 3L) + 1;

            MAX.z = MIN.z();
            MIN.add(num0, 0, -1F);
            MAX.x = MIN.x() + num1;
            this.quadsFromAABB(list);
            MAX.x = originalMaxX - num2;
            MIN.x = MAX.x() - num3;
            this.quadsFromAABB(list);
            // reset render bounds
            this.setVectors(bb);
        }
        if (MAX.z() < 16F) {
            float originalMaxX = MAX.x();

            long seed = random.nextLong();
            seed = seed * seed * 42317861L + seed * 11L;

            int num0 = (int) (seed >> 12 & 3L) + 1;
            int num1 = (int) (seed >> 15 & 3L) + 1;
            int num2 = (int) (seed >> 18 & 3L) + 1;
            int num3 = (int) (seed >> 21 & 3L) + 1;

            MIN.z = MAX.z();
            MAX.add(0, 0, 1F);
            MIN.add(num0, 0, 0);
            MAX.x = MIN.x() + num1;
            this.quadsFromAABB(list);
            MAX.x = originalMaxX - num2;
            MIN.x = MAX.x() - num3;
            this.quadsFromAABB(list);
            // reset render bounds
            this.setVectors(bb);
        }

        return list;
    }

    // FIXME I'm open for a efficient way of putting quads but this is good enough without reading through a bunch of method chains to duplicate behavior
    // For now this works, especially when the blocks won't generate frequently on worldgen
    private void quadsFromAABB(List<BakedQuad> quads) {
        quads.add(this.quadFromVectors(MIN, MAX, Direction.UP));
        quads.add(this.quadFromVectors(MIN, MAX, Direction.NORTH));
        quads.add(this.quadFromVectors(MIN, MAX, Direction.EAST));
        quads.add(this.quadFromVectors(MIN, MAX, Direction.SOUTH));
        quads.add(this.quadFromVectors(MIN, MAX, Direction.WEST));
    }

    private BakedQuad quadFromVectors(Vector3f min, Vector3f max, Direction direction) {
        BlockElementFace face = new BlockElementFace(null, 0, this.texture().atlasLocation().toString(), switch (direction) {
            case NORTH -> new BlockFaceUV(new float[] { max.x(), min.z() + 1f, min.x(), min.z() }, 0);
            case EAST  -> new BlockFaceUV(new float[] { max.x(), min.z(), max.x() - 1f, max.z() }, 90);
            case SOUTH -> new BlockFaceUV(new float[] { min.x(), max.z(), max.x(), max.z() - 1f }, 0);
            case WEST  -> new BlockFaceUV(new float[] { min.x(), max.z(), min.x() + 1f, min.z() }, 90);
            default -> new BlockFaceUV(new float[] { min.x(), min.z(), max.x(), max.z() }, 0);
        });

        return BAKERY.bakeQuad(min, max, face, this.texture(), direction, new SimpleModelState(Transformation.identity()), null, true, this.location);
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
