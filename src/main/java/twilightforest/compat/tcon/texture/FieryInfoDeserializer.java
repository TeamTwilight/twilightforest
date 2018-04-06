package twilightforest.compat.tcon.texture;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.material.deserializers.AbstractRenderInfoDeserializer;

public class FieryInfoDeserializer extends AbstractRenderInfoDeserializer {
    @Override
    public MaterialRenderInfo getMaterialRenderInfo() {
        return new MaterialRenderInfo.AbstractMaterialRenderInfo() {
            @Override
            public TextureAtlasSprite getTexture(ResourceLocation baseTexture, String location) {
                return new FieryTConTexture(baseTexture, location);
            }
        };
    }
}
