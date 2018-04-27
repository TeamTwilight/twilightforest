package twilightforest.client.model.baked;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BakedMossModel implements IBakedModel {
    private static final FaceBakery bakery = new FaceBakery();
    private static final String spriteString = new ResourceLocation(TwilightForestMod.ID, "blocks/mosspatch").toString();
    private static final TextureAtlasSprite mossTex = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(spriteString);

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads  = new ArrayList<>();

        List<BakedQuad> ups    = new ArrayList<>();
        List<BakedQuad> norths = new ArrayList<>();
        List<BakedQuad> souths = new ArrayList<>();
        List<BakedQuad> wests  = new ArrayList<>();
        List<BakedQuad> easts  = new ArrayList<>();

        int xMin  = (int) (rand       & 0b11L) + 1;
        int xMax  = (int) (rand >>  2 & 0b11L) + 1;
        int zMin  = (int) (rand >>  4 & 0b11L) + 1;
        int zMax  = (int) (rand >>  6 & 0b11L) + 1;

        //UnpackedBakedQuad.Builder mainTop = new UnpackedBakedQuad.Builder(DefaultVertexFormats.ITEM);
        //mainTop.setQuadOrientation(EnumFacing.UP);
        //mainTop.setTexture(mossTex);

        // Main piece
        BakedQuad mainUp    = makeMossQuad(8 - xMin, 8 - zMin, 8 + xMax, 8 + zMax, EnumFacing.UP   , quads, ups   );
        BakedQuad mainNorth = makeMossQuad(8 - xMin, 8 - zMin, 8 + xMax, 8 + zMax, EnumFacing.NORTH, quads, norths);
        BakedQuad mainSouth = makeMossQuad(8 - xMin, 8 - zMin, 8 + xMax, 8 + zMax, EnumFacing.SOUTH, quads, souths);
        BakedQuad mainWest  = makeMossQuad(8 - xMin, 8 - zMin, 8 + xMax, 8 + zMax, EnumFacing.WEST , quads, wests );
        BakedQuad mainEast  = makeMossQuad(8 - xMin, 8 - zMin, 8 + xMax, 8 + zMax, EnumFacing.EAST , quads, easts );

        int num4  = (int) (rand >>  7 &  0b1L);
        int num5  = (int) (rand >>  9 & 0b11L) + 1;
        int num6  = (int) (rand >> 11 & 0b11L) + 1;

        int num7  = (int) (rand >> 12 &  0b1L);
        int num8  = (int) (rand >> 14 & 0b11L) + 1;
        int num9  = (int) (rand >> 16 & 0b11L) + 1;

        int num10 = (int) (rand >> 17 &  0b1L);
        int num11 = (int) (rand >> 19 & 0b11L) + 1;
        int num12 = (int) (rand >> 21 & 0b11L) + 1;

        int num13 = (int) (rand >> 22 &  0b1L);
        int num14 = (int) (rand >> 24 & 0b11L) + 1;
        int num15 = (int) (rand >> 26 & 0b11L) + 1;

        return null;
    }

    private static BakedQuad makeMossQuad(int xMin, int zMin, int xMax, int zMax, EnumFacing facing, List<BakedQuad> generalQuads, List<BakedQuad> faceQuads) {
        BakedQuad toReturn = bakery.makeBakedQuad(
                new Vector3f(xMin, 0.0f, zMin),
                new Vector3f(xMax, 1.0f, zMax),
                new BlockPartFace(null, -1, spriteString, new BlockFaceUV(new float[]{ 8 - xMin, 8 - zMin, 8 + xMax, 8 + zMax }, 0)),
                mossTex,
                facing,
                ModelRotation.X0_Y0,
                null,
                true,
                false);

        generalQuads.add(toReturn);
        faceQuads.add(toReturn);

        return toReturn;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}
