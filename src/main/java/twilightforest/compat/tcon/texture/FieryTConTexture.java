package twilightforest.compat.tcon.texture;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.library.TinkerAPIException;
import twilightforest.client.texture.GradientMappedTexture;
import twilightforest.client.texture.GradientNode;

import java.awt.image.DirectColorModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

// This code contains some copied stuff from AbstractColoredTexture and ExtraUtilityTexture from TCon. Thanks, boni and RWTema! ~ Drullkus

public class FieryTConTexture extends GradientMappedTConTexture {
    private final ResourceLocation textureIn;

    private boolean[] translucent;
    private boolean[] edge;

    private static final GradientNode[] outlineColors = {
            new GradientNode(0f,    0xFF_FF_FF_FF),
            new GradientNode(1f/8f, 0xFF_FF_FA_96),
            new GradientNode(3f/8f, 0xFF_FB_AD_24),
            new GradientNode(0.5f,  0xFF_FB_96_24),
            new GradientNode(5f/8f, 0xFF_FB_AD_24),
            new GradientNode(7f/8f, 0xFF_FF_FA_96),
            new GradientNode(1f,    0xFF_FF_FF_FF)
    };

    public static final GradientNode[] innerColors = {
            new GradientNode(0.1f, 0xFF_3C_23_23),
            new GradientNode(0.7f, 0xFF_19_13_13),
            new GradientNode(0.9f, 0xFF_08_06_06)
    };

    private static final GradientNode[] innerColorsGlow = {
            new GradientNode(0.1f, 0xFF_77_35_11),
            new GradientNode(0.7f, 0xFF_66_2D_09),
            new GradientNode(0.9f, 0xFF_5d_26_03)
    };

    FieryTConTexture(ResourceLocation textureIn, String spriteName) {
        super(textureIn, spriteName, true, innerColors);

        this.textureIn = textureIn;
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
        this.framesTextureData = Lists.newArrayList();
        this.frameCounter = 0;
        this.tickCounter = 0;

        TextureAtlasSprite baseTexture = textureGetter.apply(textureIn);
        if(baseTexture == null || baseTexture.getFrameCount() <= 0) {
            this.width = 1;
            this.height = 1;
            // failure
            return false;
        }

        this.copyFrom(baseTexture);

        int[][] data;
        int[][] dataSecondFrame;
        int[][] original = baseTexture.getFrameTextureData(0);
        data = new int[original.length][];
        dataSecondFrame = new int[original.length][];
        data[0] = Arrays.copyOf(original[0], original[0].length);
        dataSecondFrame[0] = Arrays.copyOf(original[0], original[0].length);

        // do the transformation on the data for mipmap level 0
        // looks like other mipmaps are generated correctly
        try {
            preProcess(data[0]);
            // go over the base texture and color it
            for(int pxCoord = 0; pxCoord < data[0].length; pxCoord++) {
                data[0][pxCoord] = colorPixel(data[0][pxCoord], pxCoord);
                this.gradientMap = innerColorsGlow;
                dataSecondFrame[0][pxCoord] = colorPixel(dataSecondFrame[0][pxCoord], pxCoord);
                this.gradientMap = innerColors;
            }
        } catch(Exception e) {
            throw new TinkerAPIException("Error occured while processing: " + this.getIconName(), e);
        }

        if(this.framesTextureData.isEmpty()) {
            this.framesTextureData.add(data);
            this.framesTextureData.add(dataSecondFrame);
        }

        List<AnimationFrame> frames = new ArrayList<>();
        frames.add(new AnimationFrame(0));
        frames.add(new AnimationFrame(1));

        this.animationMetadata = new AnimationMetadataSection(frames, width, height, 18, true);

        return false;
    }

    private static final DirectColorModel colorModel = new DirectColorModel(32, 16711680, '\uff00', 255, -16777216);

    @Override
    protected void preProcess(final int[] data) {
        edge = new boolean[width * height];
        translucent = new boolean[width * height];
        int c;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) edge[coord(x, y)] = true;

                c = data[coord(x, y)];
                if (c == 0 || colorModel.getAlpha(c) < 64) {
                    translucent[coord(x, y)] = true;

                    if (x > 0) {
                        edge[coord(x - 1, y)] = true;
                    }

                    if (y > 0) {
                        edge[coord(x, y - 1)] = true;
                    }

                    if (x < width - 1) {
                        edge[coord(x + 1, y)] = true;
                    }

                    if (y < height - 1) {
                        edge[coord(x, y + 1)] = true;
                    }
                }
            }
        }

        int minimumValue = 255;
        int maximumValue = 0;

        for (int i = 0; i < data.length; i++) {
            int pixel = data[i];

            if (!edge[i] && !translucent[i]) {
                minimumValue = Math.min(minimumValue, getPerceptualBrightness(pixel));
                maximumValue = Math.max(maximumValue, getPerceptualBrightness(pixel));
            }
        }

        if (minimumValue > maximumValue) {
            this.minimumValue = maximumValue / 255f;
            this.maximumValue = minimumValue / 255f;
        } else {
            this.minimumValue = minimumValue / 255f;
            this.maximumValue = maximumValue / 255f;
        }
    }

    @Override
    protected int colorPixel(int pixel, int pxCoord) {
        if(!translucent[pxCoord]) {
            if(edge[pxCoord]) {
                short ff = 0xFF;
                int x = (getX(pxCoord) * 0xFF) / width;
                int y = (getY(pxCoord) * 0xFF) / height;
                int gray = (this.hashCode() + x - y) & ff;

                //noinspection NumericOverflow
                return GradientMappedTexture.getGradient((ff << 24) | (gray << 16) | (gray << 8) | gray, outlineColors, 0f, 1f);
            }
        }//*/

        return super.colorPixel(pixel, pxCoord);
    }
}
