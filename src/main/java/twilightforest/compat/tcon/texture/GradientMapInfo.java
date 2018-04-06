package twilightforest.compat.tcon.texture;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import twilightforest.client.texture.GradientNode;

@SuppressWarnings("WeakerAccess")
public class GradientMapInfo extends MaterialRenderInfo.AbstractMaterialRenderInfo {

    private GradientNode[] gradientMap;
    private boolean shouldStretchMinimumMaximum;

    public GradientMapInfo(boolean shouldStretchMinimumMaximum, GradientNode... gradientMap) {
        this.gradientMap = gradientMap;
        this.shouldStretchMinimumMaximum = shouldStretchMinimumMaximum;
    }

    @Override
    public TextureAtlasSprite getTexture(ResourceLocation baseTexture, String location) {
        return new GradientMappedTConTexture(baseTexture, location, shouldStretchMinimumMaximum, gradientMap);
    }
}
