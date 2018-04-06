package twilightforest.client.texture;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.library.client.RenderUtil;
import twilightforest.TwilightForestMod;

import java.util.Collection;
import java.util.function.Function;

import static net.minecraft.util.math.MathHelper.sqrt;

public class GradientMappedTexture extends TextureAtlasSprite {
    private final ResourceLocation textureDependency;

    private boolean shouldStretchMinimumMaximum;
    private final GradientNode[] GRADIENT_MAP;
    private float minimumValue;
    private float maximumValue;


    public GradientMappedTexture(ResourceLocation textureDependency, ResourceLocation spriteName, boolean shouldStretchMinimumMaximum, GradientNode[] gradient_map) {
        super(spriteName.toString());
        this.textureDependency = textureDependency;
        this.GRADIENT_MAP = gradient_map;
        this.shouldStretchMinimumMaximum = shouldStretchMinimumMaximum;
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
        final TextureAtlasSprite sprite = textureGetter.apply(textureDependency);

        copyFrom(sprite);
        this.animationMetadata = sprite.animationMetadata;

        this.framesTextureData = Lists.newArrayList();

        int minimumValue = 255;
        int maximumValue = 0;

        for (int i = 0; i < sprite.getFrameCount(); i++) {
            final int[][] textureFrom = sprite.getFrameTextureData(i).clone();
            int [][] textureTo = new int[textureFrom.length][];

            for (int j = 0; j < textureFrom.length; j++) {
                textureTo[j] = new int[textureFrom[j].length];
                System.arraycopy(textureFrom[j], 0, textureTo[j], 0, textureFrom[j].length);
            }

            if (this.shouldStretchMinimumMaximum) {
                for (int pixel : textureTo[0]) {
                    if (RenderUtil.alpha(pixel) == 0) continue;

                    minimumValue = Math.min(minimumValue, getPerceptualBrightness(pixel));
                    maximumValue = Math.max(maximumValue, getPerceptualBrightness(pixel));
                }
            }

            this.framesTextureData.add(i, textureTo);
        }

        if (shouldStretchMinimumMaximum) {
            if (minimumValue > maximumValue) {
                this.minimumValue = maximumValue / 255f;
                this.maximumValue = minimumValue / 255f;
            } else {
                this.minimumValue = minimumValue / 255f;
                this.maximumValue = maximumValue / 255f;
            }

            for (int i = 0; i < framesTextureData.size(); i++) {
                int [][] texture = this.framesTextureData.get(i);

                for (int j = 0; j < texture[0].length; j++) {
                    texture[0][j] = getGradient(texture[0][j], GRADIENT_MAP, this.minimumValue, this.maximumValue);
                }
            }

            TwilightForestMod.LOGGER.info("Autogenerated " + this.getIconName() + " from " + this.textureDependency + " with min value of " + this.minimumValue + " and max value " + this.maximumValue);
        } else {
            for (int i = 0; i < framesTextureData.size(); i++) {
                int [][] texture = this.framesTextureData.get(i);

                for (int j = 0; j < texture[0].length; j++) {
                    texture[0][j] = getGradient(texture[0][j], GRADIENT_MAP, 0.0f, 1.0f);
                }
            }

            TwilightForestMod.LOGGER.info("Autogenerated " + this.getIconName() + " from " + this.textureDependency);
        }

        return false;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.of(textureDependency);
    }

    // borrowed from Shadows of Physis
    // Thanks TTFTCUTS! :)
    private static int getPerceptualBrightness(int col) {
        return getPerceptualBrightness(RenderUtil.red(col) / 255.0, RenderUtil.green(col) / 255.0, RenderUtil.blue(col) / 255.0);
    }

    private static int getPerceptualBrightness(double r, double g, double b) {
        return (int) (Math.sqrt(0.241 * r * r + 0.691 * g * g + 0.068 * b * b) * 255);
    }

    public static int getGradient(int packedColor, GradientNode[] gradientMap, float minimumValue, float maximumValue) {
        int a = RenderUtil.alpha(packedColor);

        if(a == 0) {
            return packedColor;
        }

        int rTo = 0, gTo = 0, bTo = 0;

        // average it
        float gray = getBalancedValue(getPerceptualBrightness(packedColor) / 255.0F, minimumValue, maximumValue);

        if (gray <= gradientMap[0].node) {
            rTo = RenderUtil.red  (gradientMap[0].color);
            gTo = RenderUtil.green(gradientMap[0].color);
            bTo = RenderUtil.blue (gradientMap[0].color);
        } else if (gray >= gradientMap[gradientMap.length-1].node) {
            int i = gradientMap[gradientMap.length-1].color;

            rTo = RenderUtil.red  (i);
            gTo = RenderUtil.green(i);
            bTo = RenderUtil.blue (i);
        } else {
            for (int i = 0; i < gradientMap.length - 1; i++) {
                if (gray == gradientMap[i].node) {
                    rTo = RenderUtil.red  (gradientMap[i].color);
                    gTo = RenderUtil.green(gradientMap[i].color);
                    bTo = RenderUtil.blue (gradientMap[i].color);
                } else if (gray >= gradientMap[i].node && gray <= gradientMap[i+1].node) {
                    return getColorFromBetweenNodes(getBalancedValue(gray, gradientMap[i].node, gradientMap[i+1].node), gradientMap[i].color, gradientMap[i+1].color, a);
                }
            }
        }

        return RenderUtil.compose(rTo, gTo, bTo, a);
    }

    public static int getColorFromBetweenNodes(float placement, int color1, int color2, int alpha) {
        int r1 = RenderUtil.red  (color1);
        int g1 = RenderUtil.green(color1);
        int b1 = RenderUtil.blue (color1);
        int r2 = RenderUtil.red  (color2);
        int g2 = RenderUtil.green(color2);
        int b2 = RenderUtil.blue (color2);

        return RenderUtil.compose(
                pickIntInBetween(placement, r1, r2),
                pickIntInBetween(placement, g1, g2),
                pickIntInBetween(placement, b1, b2), alpha);
    }

    public static int pickIntInBetween(float placement, float v1, float v2) {
        return (int) sqrt(((v1 * v1) * (1.0f - placement)) + ((v2 * v2) * placement));
    }

    public static float getBalancedValue(float valueIn, float minimumValue, float maximumValue) {
        return (valueIn - minimumValue) / (maximumValue - minimumValue);
    }
}
