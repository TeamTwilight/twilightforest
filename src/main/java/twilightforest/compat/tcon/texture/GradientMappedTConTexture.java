package twilightforest.compat.tcon.texture;

import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.library.client.RenderUtil;
import slimeknights.tconstruct.library.client.texture.AbstractColoredTexture;
import twilightforest.client.texture.GradientMappedTexture;
import twilightforest.client.texture.GradientNode;

import static net.minecraft.util.math.MathHelper.sqrt;

@SuppressWarnings("WeakerAccess")
public class GradientMappedTConTexture extends AbstractColoredTexture {
    protected GradientNode[] gradientMap;
    protected boolean shouldStretchMinimumMaximum;
    protected float minimumValue;
    protected float maximumValue;

    GradientMappedTConTexture(ResourceLocation baseTextureLocation, String spriteName, boolean shouldStretchMinimumMaximum, GradientNode[] gradientMap) {
        super(baseTextureLocation, spriteName);
        this.shouldStretchMinimumMaximum = shouldStretchMinimumMaximum;
        this.gradientMap = gradientMap;
    }

    @Override
    protected void preProcess(final int[] data) {
        if (shouldStretchMinimumMaximum) {
            int minimumValue = 255;
            int maximumValue = 0;

            for (int pixel : data) {
                if (RenderUtil.alpha(pixel) == 0) continue;

                minimumValue = Math.min(minimumValue, getPerceptualBrightness(pixel));
                maximumValue = Math.max(maximumValue, getPerceptualBrightness(pixel));
            }

            if (minimumValue > maximumValue) {
                this.minimumValue = maximumValue / 255f;
                this.maximumValue = minimumValue / 255f;
            } else {
                this.minimumValue = minimumValue / 255f;
                this.maximumValue = maximumValue / 255f;
            }
        } else {
            this.minimumValue = 0f;
            this.maximumValue = 1f;
        }
    }

    @Override
    protected int colorPixel(int pixel, int pxCoord) {
        int a = RenderUtil.alpha(pixel);
        if(a == 0) {
            return pixel;
        }

        int rFrom = RenderUtil.red  (pixel);
        int gFrom = RenderUtil.green(pixel);
        int bFrom = RenderUtil.blue (pixel);

        int rTo = 0, gTo = 0, bTo = 0;

        // average it
        float gray = getModifiedValue((rFrom + gFrom + bFrom) / (3.0f * 255.0f));

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
                    return GradientMappedTexture.getColorFromBetweenNodes(GradientMappedTexture.getBalancedValue(gray, gradientMap[i].node, gradientMap[i+1].node), gradientMap[i].color, gradientMap[i+1].color, a);
                }
            }
        }

        return RenderUtil.compose(rTo, gTo, bTo, a);
    }

    protected final float getModifiedValue(float valueIn) {
        if (shouldStretchMinimumMaximum)
            //return (valueIn * (maximumValue - minimumValue)) + minimumValue;
            return (valueIn - minimumValue) / (maximumValue - minimumValue);
        else
            return valueIn;
    }
}
